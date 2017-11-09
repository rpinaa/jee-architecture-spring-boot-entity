package org.example.seed.service.impl;

import org.example.seed.catalog.ClientStatus;
import org.example.seed.domain.Client;
import org.example.seed.entity.ClientEntity;
import org.example.seed.entity.TelephoneEntity;
import org.example.seed.event.client.*;
import org.example.seed.mapper.ClientMapper;
import org.example.seed.repository.ClientRepository;
import org.example.seed.service.ClientService;
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
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public ListenableFuture<ResponseClientsEvent> requestClients(final RequestClientsEvent event) {

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
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseClientEvent> createClient(final CreateClientEvent event) {

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
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseClientEvent> registerClient(final RegisterClientEvent event) {

    final Client client = this.clientMapper
      .map(this.clientRepository
        .findByIdAndStatus(event.getClient().getId(), ClientStatus.REGISTERED)
        .map(clientEntity -> {

          // TODO: to implement keygen activation

          clientEntity.setRating(0F);
          clientEntity.setStatus(ClientStatus.ACTIVATED);
          clientEntity.setSecret(KeyGenUtil.encode(event.getClient().getCredential()));

          this.clientRepository.save(clientEntity);

          // TODO: sending welcome email

          return clientEntity;
        })
        .orElseThrow(() -> new RuntimeException("ERROR-00002")));

    return new AsyncResult<>(ResponseClientEvent.builder().client(client).build());
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public ListenableFuture<ResponseClientEvent> requestClient(final RequestClientEvent event) {
    return new AsyncResult<>(ResponseClientEvent.builder()
      .client(this.clientMapper
        .map(this.clientRepository
          .findById(event.getId())
          .orElseGet(null)))
      .build());
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseClientEvent> updateClient(final UpdateClientEvent event) {

    final Client client = this.clientMapper
      .map(this.clientRepository
        .findByIdAndStatus(event.getClient().getId(), ClientStatus.ACTIVATED)
        .map(clientEntity -> {

          clientEntity.setRating(event.getClient().getRating());
          clientEntity.setLastName(event.getClient().getLastName());
          clientEntity.setFirstName(event.getClient().getFirstName());
          clientEntity.setTelephone(PhoneGenUtil
            .map(event.getClient().getTelephone().getNumber(), event.getClient().getCountry())
            .map(phoneDto -> TelephoneEntity.builder()
              .lada(phoneDto.getLada())
              .id(UUID.randomUUID().toString())
              .number(phoneDto.getPhoneNumber())
              .type(event.getClient().getTelephone().getType())
              .name(event.getClient().getTelephone().getName())
              .build())
            .orElseThrow(() -> new RuntimeException("ERROR-00003")));

          this.clientRepository.save(clientEntity);

          return clientEntity;
        })
        .orElseThrow(() -> new RuntimeException("ERROR-00002")));

    return new AsyncResult<>(ResponseClientEvent.builder().client(client).build());
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseClientEvent> deleteClient(final DeleteClientEvent event) {

    this.clientRepository.deleteById(event.getId());

    return new AsyncResult<>(null);
  }
}
