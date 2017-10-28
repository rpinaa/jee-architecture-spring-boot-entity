package org.example.seed.service.impl;

import org.example.seed.catalog.ChefStatus;
import org.example.seed.entity.ChefEntity;
import org.example.seed.event.chef.*;
import org.example.seed.mapper.ChefMapper;
import org.example.seed.mapper.TelephoneMapper;
import org.example.seed.repository.ChefRepository;
import org.example.seed.repository.TelephoneRepository;
import org.example.seed.service.ChefService;
import org.example.seed.util.KeyGenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by PINA on 25/06/2017.
 */
@Service
public class ChefServiceImpl implements ChefService {

  private final ChefMapper chefMapper;
  private final ChefRepository chefRepository;
  private final TelephoneMapper telephoneMapper;
  private final TelephoneRepository telephoneRepository;

  @Autowired
  public ChefServiceImpl(
    final ChefMapper chefMapper,
    final ChefRepository chefRepository,
    final TelephoneMapper telephoneMapper,
    final TelephoneRepository telephoneRepository
  ) {
    this.chefMapper = chefMapper;
    this.chefRepository = chefRepository;
    this.telephoneMapper = telephoneMapper;
    this.telephoneRepository = telephoneRepository;
  }

  @Override
  @Async
  @Cacheable(value = "chefs")
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public Future<ResponseChefsEvent> requestChefs(final RequestChefsEvent event) {

    final Page<ChefEntity> chefs = this.chefRepository
      .findAll(PageRequest
        .of(event.getPage() - 1, event.getLimit()));

    return new AsyncResult<>(ResponseChefsEvent.builder()
      .chefs(this.chefMapper
        .mapListReverse(chefs.getContent()))
      .total(chefs.getTotalElements())
      .build());
  }

  @Override
  @Async
  @CacheEvict(value = "chefs", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseChefEvent> createChef(final CreateChefEvent event) {

    event.getChef().setRating(null);
    event.getChef().setActive(false);
    event.getChef().setTelephones(null);
    event.getChef().setStatus(ChefStatus.REGISTERED);

    if (this.chefRepository.existsByEmail(event.getChef().getAccount().getEmail())) {
      throw new RuntimeException("ERROR-01001");
    }

    this.chefRepository
      .save(this.chefMapper
        .map(event.getChef()));

    // TODO: sending activation email

    return new AsyncResult<>(null);
  }

  @Async
  @Override
  @CacheEvict(value = "chefs", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseChefEvent> registerChef(final RegisterChefEvent event) {
    return new AsyncResult<>(ResponseChefEvent.builder()
      .chef(this.chefMapper
        .map(this.chefRepository
          .findByIdAndStatus(event.getChef().getId(), ChefStatus.REGISTERED)
          .map(chefEntity -> {

            // TODO: to implement keygen activation

            chefEntity.setRating(0F);
            chefEntity.setActive(false);
            chefEntity.setStatus(ChefStatus.ACTIVATED);
            chefEntity.setRfc(event.getChef().getRfc());
            chefEntity.setCurp(event.getChef().getCurp());
            chefEntity.getAccount()
              .setSecret(KeyGenUtil.encode(event.getChef().getAccount().getCredential()));

            this.chefRepository.save(chefEntity);

            // TODO: sending welcome email

            return chefEntity;
          })
          .orElseThrow(() -> new RuntimeException("ERROR-01002"))))
      .build());
  }

  @Override
  @Async
  @Cacheable(value = "chefs")
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public Future<ResponseChefEvent> requestChef(final RequestChefEvent event) {
    return new AsyncResult<>(ResponseChefEvent.builder()
      .chef(this.chefMapper
        .map(this.chefRepository
          .findById(event.getId())
          .orElseGet(null))
      )
      .build());
  }

  @Override
  @Async
  @CacheEvict(value = "chefs", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseChefEvent> updateChef(final UpdateChefEvent event) {
    return new AsyncResult<>(ResponseChefEvent.builder()
      .chef(this.chefMapper
        .map(this.chefRepository
          .findByIdAndStatus(event.getChef().getId(), ChefStatus.ACTIVATED)
          .map(chefEntity -> {

            this.telephoneRepository.deleteInBatch(chefEntity.getTelephones());

            chefEntity.setRfc(event.getChef().getRfc());
            chefEntity.setCurp(event.getChef().getCurp());
            chefEntity.getAccount().setLastName(event.getChef().getAccount().getLastName());
            chefEntity.getAccount().setFirstName(event.getChef().getAccount().getFirstName());

            chefEntity.setTelephones(this.telephoneMapper
              .mapList(event.getChef().getTelephones())
              .parallelStream()
              .peek(t -> {
                t.setId(UUID.randomUUID().toString());
                t.setChef(chefEntity);
              })
              .collect(Collectors.toList()));

            return this.chefRepository.save(chefEntity);
          })
          .orElseThrow(() -> new RuntimeException("ERROR-00002"))))
      .build());
  }

  @Override
  @Async
  @CacheEvict(value = "chefs", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseChefEvent> deleteChef(final DeleteChefEvent event) {

    this.chefRepository.deleteById(event.getId());

    return new AsyncResult<>(null);
  }
}
