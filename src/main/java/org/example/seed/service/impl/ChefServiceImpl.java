package org.example.seed.service.impl;

import org.example.seed.catalog.ChefStatus;
import org.example.seed.domain.Chef;
import org.example.seed.entity.ChefEntity;
import org.example.seed.event.chef.*;
import org.example.seed.mapper.ChefMapper;
import org.example.seed.mapper.TelephoneMapper;
import org.example.seed.repository.ChefRepository;
import org.example.seed.repository.TelephoneRepository;
import org.example.seed.service.ChefService;
import org.example.seed.util.KeyGenUtil;
import org.example.seed.util.PhoneGenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.UUID;
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
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public ListenableFuture<ResponseChefsEvent> requestChefs(final RequestChefsEvent event) {

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
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseChefEvent> createChef(final CreateChefEvent event) {

    event.getChef().setActive(false);
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
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseChefEvent> registerChef(final RegisterChefEvent event) {

    final Chef chef = this.chefMapper
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
        .orElseThrow(() -> new RuntimeException("ERROR-01002")));

    return new AsyncResult<>(ResponseChefEvent.builder().chef(chef).build());
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public ListenableFuture<ResponseChefEvent> requestChef(final RequestChefEvent event) {
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
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseChefEvent> updateChef(final UpdateChefEvent event) {

    final Chef chef = this.chefMapper
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
            .peek(t -> PhoneGenUtil
              .map(t.getNumber(), chefEntity.getCountry())
              .ifPresent(p -> {
                t.setChef(chefEntity);
                t.setLada(p.getLada());
                t.setNumber(p.getPhoneNumber());
                t.setId(UUID.randomUUID().toString());
              }))
            .collect(Collectors.toList()));

          return this.chefRepository.save(chefEntity);
        })
        .orElseThrow(() -> new RuntimeException("ERROR-00002")));

    return new AsyncResult<>(ResponseChefEvent.builder().chef(chef).build());
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseChefEvent> deleteChef(final DeleteChefEvent event) {

    this.chefRepository.deleteById(event.getId());

    return new AsyncResult<>(null);
  }
}
