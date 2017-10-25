package org.example.seed.service.impl;

import org.example.seed.catalog.ClientStatus;
import org.example.seed.entity.ClientEntity;
import org.example.seed.event.client.*;
import org.example.seed.mapper.ClientMapper;
import org.example.seed.repository.ClientRepository;
import org.example.seed.service.ClientService;
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

import java.util.concurrent.Future;


/**
 * Created by PINA on 25/06/2017.
 */
@Service
public class ClientServiceImpl implements ClientService {

  private final ClientMapper clientMapper;
  private final ClientRepository clientRepository;

  @Autowired
  public ClientServiceImpl(final ClientMapper clientMapper, final ClientRepository clientRepository) {
    this.clientMapper = clientMapper;
    this.clientRepository = clientRepository;
  }

  @Override
  @Async
  @Cacheable(value = "client")
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public Future<ResponseClientsEvent> requestClients(final RequestClientsEvent event) {

    final Page<ClientEntity> clients = this.clientRepository
      .findAll(PageRequest
        .of(event.getPage() - 1, event.getLimit()));

    return new AsyncResult<>(ResponseClientsEvent.builder()
      .clients(this.clientMapper
        .mapListReverse(clients.getContent()))
      .total(clients.getTotalElements())
      .build());
  }

  @Override
  @Async
  @CacheEvict(value = "client", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseClientEvent> createClient(final CreateClientEvent event) {

    event.getClient().setRating(null);
    event.getClient().setTelephone(null);
    event.getClient().setStatus(ClientStatus.REGISTERED);

    if (this.clientRepository.existsByEmail(event.getClient().getEmail())) {
      throw new RuntimeException("ERROR-00001");
    }

    this.clientRepository
      .save(this.clientMapper
        .map(event.getClient()));

    // TODO: sending activation email

    return new AsyncResult<>(null);
  }

  @Override
  @Async
  @CacheEvict(value = "client", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseClientEvent> registerClient(final RegisterClientEvent event) {
    return new AsyncResult<>(ResponseClientEvent.builder()
      .client(this.clientMapper
        .map(this.clientRepository
          .findByIdAndStatus(event.getClient().getId(), ClientStatus.REGISTERED)
          .map(clientEntity -> {

            // TODO: to implement keygen activation

            clientEntity.setRating(0F);
            clientEntity.setTelephone(null);
            clientEntity.setStatus(ClientStatus.ACTIVATED);
            clientEntity.setSecret(KeyGenUtil.encode(event.getClient().getCredential()));

            this.clientRepository.save(clientEntity);

            // TODO: sending welcome email

            return clientEntity;
          })
          .orElseThrow(() -> new RuntimeException("ERROR-00002"))))
      .build());
  }

  @Override
  @Async
  @Cacheable(value = "client")
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public Future<ResponseClientEvent> requestClient(final RequestClientEvent event) {
    return new AsyncResult<>(ResponseClientEvent.builder()
      .client(this.clientMapper
        .map(this.clientRepository
          .findById(event.getId())
          .orElseGet(null)))
      .build());
  }

  @Override
  @Async
  @CacheEvict(value = "client", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseClientEvent> updateClient(final UpdateClientEvent event) {
    return new AsyncResult<>(ResponseClientEvent.builder()
      .client(this.clientMapper
        .map(this.clientRepository
          .findByIdAndStatus(event.getClient().getId(), ClientStatus.ACTIVATED)
          .map(clientEntity -> {

            clientEntity.setRating(event.getClient().getRating());
            clientEntity.setLastName(event.getClient().getLastName());
            clientEntity.setFirstName(event.getClient().getFirstName());

            this.clientRepository.save(clientEntity);

            return clientEntity;
          })
          .orElseThrow(() -> new RuntimeException("ERROR-00002"))))
      .build());
  }

  @Override
  @Async
  @CacheEvict(value = "client", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseClientEvent> deleteClient(final DeleteClientEvent event) {

    this.clientRepository.deleteById(event.getId());

    return new AsyncResult<>(null);
  }
}
