package org.example.seed.service.impl;

import org.example.seed.catalog.ChefStatus;
import org.example.seed.entity.ChefEntity;
import org.example.seed.event.chef.*;
import org.example.seed.mapper.ChefMapper;
import org.example.seed.mapper.TelephoneMapper;
import org.example.seed.repository.ChefRepository;
import org.example.seed.repository.TelephoneRepository;
import org.example.seed.service.ChefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by PINA on 25/06/2017.
 */
@Service
public class ChefServiceImpl implements ChefService {

  @Autowired
  private ChefMapper chefMapper;

  @Autowired
  private ChefRepository chefRepository;

  @Autowired
  private TelephoneMapper telephoneMapper;

  @Autowired
  private TelephoneRepository telephoneRepository;

  @Override
  @Cacheable(value = "chefs")
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public Mono<CatalogChefEvent> requestChefs(final RequestAllChefEvent event) {

    final Page<ChefEntity> chefs = this.chefRepository
      .findAll(PageRequest
        .of(event.getPage() - 1, event.getLimit()));

    return Mono.justOrEmpty(CatalogChefEvent.builder()
      .chefs(this.chefMapper
        .mapListReverse(chefs.getContent()))
      .total(chefs.getTotalElements())
      .build());
  }

  @Override
  @CacheEvict(value = "chefs", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Mono<ResponseChefEvent> createChef(final CreateChefEvent event) {

    event.getChef().setRating(0F);
    event.getChef().setActive(false);
    event.getChef().setTelephones(null);
    event.getChef().setStatus(ChefStatus.REGISTERED);

    this.chefRepository
      .save(this.chefMapper
        .map(event.getChef()));

    return Mono.justOrEmpty(ResponseChefEvent.builder().chef(null).build());
  }

  @Override
  @Cacheable(value = "chefs")
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public Mono<ResponseChefEvent> requestChef(final RequestChefEvent event) {
    return Mono.justOrEmpty(ResponseChefEvent.builder()
      .chef(this.chefMapper
        .map(this.chefRepository
          .findById(event.getId())
          .orElseGet(null))
      )
      .build());
  }

  @Override
  @CacheEvict(value = "chefs", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Mono<ResponseChefEvent> updateChef(final UpdateChefEvent event) {

    this.chefRepository.findById(event.getChef().getId())
      .ifPresent(chefEntity -> {

        this.telephoneRepository.deleteInBatch(chefEntity.getTelephones());

        chefEntity.setStatus(ChefStatus.ACTIVATED);
        chefEntity.setRfc(event.getChef().getRfc());
        chefEntity.setCurp(event.getChef().getCurp());
        chefEntity.setRating(event.getChef().getRating());
        chefEntity.getAccount().setFirstName(event.getChef().getAccount().getFirstName());
        chefEntity.getAccount().setLastName(event.getChef().getAccount().getLastName());
        chefEntity.setTelephones(this.telephoneMapper
          .mapList(event.getChef().getTelephones())
          .parallelStream()
          .peek(t -> {
            t.setId(UUID.randomUUID().toString());
            t.setChef(chefEntity);

          })
          .collect(Collectors.toList()));

        this.chefRepository.save(chefEntity);
      });

    return Mono.justOrEmpty(ResponseChefEvent.builder().chef(null).build());
  }

  @Override
  @CacheEvict(value = "chefs", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Mono<ResponseChefEvent> deleteChef(final DeleteChefEvent event) {

    this.chefRepository.deleteById(event.getId());

    return Mono.justOrEmpty(ResponseChefEvent.builder().chef(null).build());
  }
}
