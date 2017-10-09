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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;


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
  @Cacheable(value = "client")
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public Mono<CatalogClientEvent> requestClients(final RequestAllClientEvent event) {

    final Page<ClientEntity> clients = this.clientRepository
      .findAll(PageRequest
        .of(event.getPage() - 1, event.getLimit()));

    return Mono.justOrEmpty(CatalogClientEvent
      .builder()
      .clients(this.clientMapper
        .mapListReverse(clients.getContent()))
      .total(clients.getTotalElements())
      .build());
  }

  @Override
  @CacheEvict(value = "client", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Mono<ResponseClientEvent> createClient(final CreateClientEvent event) {

    event.getClient().setStatus(ClientStatus.REGISTERED);
    event.getClient().setRating(0F);

    this.clientRepository
      .save(this.clientMapper
        .map(event.getClient()));

    return Mono.justOrEmpty(ResponseClientEvent.builder().client(null).build());
  }

  @Override
  @Cacheable(value = "client")
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public Mono<ResponseClientEvent> requestClient(final RequestClientEvent event) {
    return Mono.justOrEmpty(ResponseClientEvent.builder()
      .client(this.clientMapper
        .map(this.clientRepository
          .findById(event.getId())
          .orElseGet(null)))
      .build());
  }

  @Override
  @CacheEvict(value = "client", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Mono<ResponseClientEvent> updateClient(final UpdateClientEvent event) {

    this.clientRepository.findById(event.getClient().getId())
      .ifPresent(clientEntity -> {

        clientEntity.setFirstName(event.getClient().getFirstName());
        clientEntity.setLastName(event.getClient().getLastName());
        clientEntity.setRating(event.getClient().getRating());
        clientEntity.setStatus(ClientStatus.REGISTERED);

        this.clientRepository.save(clientEntity);
      });

    return Mono.justOrEmpty(ResponseClientEvent.builder().client(null).build());
  }

  @Override
  @CacheEvict(value = "client", allEntries = true)
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Mono<ResponseClientEvent> deleteClient(final DeleteClientEvent event) {

    this.clientRepository.deleteById(event.getId());

    return Mono.justOrEmpty(ResponseClientEvent.builder().client(null).build());
  }
}
