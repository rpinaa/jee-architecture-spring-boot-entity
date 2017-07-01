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
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;
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
    @Async
    @Cacheable(value = "chefs")
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Future<CatalogChefEvent> requestChefs(final RequestAllChefEvent event) {

        final Pageable pageable = new PageRequest(event.getPage() - 1, event.getLimit());
        final Page<ChefEntity> chefs = this.chefRepository.findAll(pageable);

        return new AsyncResult<>(CatalogChefEvent.builder()
                .chefs(this.chefMapper.mapListReverse(chefs.getContent()))
                .total(chefs.getTotalElements())
                .build());
    }

    @Override
    @Async
    @CacheEvict(value = "chefs", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<ResponseChefEvent> createChef(final CreateChefEvent event) {

        event.getChef().setStatus(ChefStatus.REGISTERED);
        event.getChef().setRating(0F);
        event.getChef().setTelephones(null);

        this.chefRepository.save(this.chefMapper.map(event.getChef()));

        return new AsyncResult<>(null);
    }

    @Override
    @Async
    @Cacheable(value = "chefs")
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Future<ResponseChefEvent> requestChef(final RequestChefEvent event) {

        return new AsyncResult<>(ResponseChefEvent.builder()
                .chef(this.chefMapper.map(this.chefRepository.findOne(event.getId())))
                .build());
    }

    @Override
    @Async
    @CacheEvict(value = "chefs", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<ResponseChefEvent> updateChef(final UpdateChefEvent event) {

        Optional.of(this.chefRepository.findOne(event.getChef().getId()))
                .ifPresent(chefEntity -> {

                    this.telephoneRepository.deleteInBatch(chefEntity.getTelephones());

                    chefEntity.setRating(event.getChef().getRating());
                    chefEntity.setRfc(event.getChef().getRfc());
                    chefEntity.setCurp(event.getChef().getCurp());
                    chefEntity.setStatus(ChefStatus.ACTIVATED);
                    chefEntity.getAccount().setFirstName(event.getChef().getAccount().getFirstName());
                    chefEntity.getAccount().setLastName(event.getChef().getAccount().getLastName());
                    chefEntity.setTelephones(this.telephoneMapper
                            .mapList(event.getChef().getTelephones())
                            .parallelStream()
                            .map(t -> {
                                t.setId(UUID.randomUUID().toString());
                                t.setChef(chefEntity);

                                return t;
                            })
                            .collect(Collectors.toList()));

                    this.chefRepository.save(chefEntity);
                });

        return new AsyncResult<>(null);
    }

    @Override
    @Async
    @CacheEvict(value = "chefs", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<ResponseChefEvent> deleteChef(final DeleteChefEvent event) {

        this.chefRepository.delete(event.getId());

        return new AsyncResult<>(null);
    }
}
