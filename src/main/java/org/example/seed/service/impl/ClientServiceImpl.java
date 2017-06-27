package org.example.seed.service.impl;

import org.example.seed.catalog.ClientStatus;
import org.example.seed.entity.ClientEntity;
import org.example.seed.event.client.*;
import org.example.seed.mapper.ClientMapper;
import org.example.seed.repository.ClientRepository;
import org.example.seed.service.ClientService;
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
import java.util.concurrent.Future;

/**
 * Created by PINA on 25/06/2017.
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Override
    @Async
    @Cacheable(value = "client")
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Future<CatalogClientEvent> requestClients(final RequestAllClientEvent event) {

        final Pageable pageable = new PageRequest(event.getPage() - 1, event.getLimit());
        final Page<ClientEntity> clients = this.clientRepository.findAll(pageable);

        return new AsyncResult<>(CatalogClientEvent.builder()
                .clients(this.clientMapper.mapListReverse(clients.getContent()))
                .total(clients.getTotalElements())
                .build());
    }

    @Override
    @Async
    @CacheEvict(value = "client", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<ResponseClientEvent> createClient(final CreateClientEvent event) {

        event.getClient().setStatus(ClientStatus.REGISTERED);
        event.getClient().setRating(0F);

        this.clientRepository.save(this.clientMapper.map(event.getClient()));

        return new AsyncResult<>(null);
    }

    @Override
    @Async
    @Cacheable(value = "client")
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Future<ResponseClientEvent> requestClient(final RequestClientEvent event) {

        return new AsyncResult<>(ResponseClientEvent.builder()
                .client(this.clientMapper.map(this.clientRepository.findOne(event.getId())))
                .build());
    }

    @Override
    @Async
    @CacheEvict(value = "client", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<ResponseClientEvent> updateClient(final UpdateClientEvent event) {

        Optional.of(this.clientRepository.findOne(event.getClient().getId()))
                .ifPresent(clientEntity -> {

                    clientEntity.setFirstName(event.getClient().getFirstName());
                    clientEntity.setLastName(event.getClient().getLastName());
                    clientEntity.setRating(event.getClient().getRating());
                    clientEntity.setStatus(ClientStatus.REGISTERED);

                    this.clientRepository.save(clientEntity);
                });

        return new AsyncResult<>(null);
    }

    @Override
    @Async
    @CacheEvict(value = "client", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<ResponseClientEvent> deleteClient(final DeleteClientEvent event) {

        this.clientRepository.delete(event.getId());

        return new AsyncResult<>(null);
    }
}
