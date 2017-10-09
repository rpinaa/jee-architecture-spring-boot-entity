package org.example.seed.rest.impl;

import org.example.seed.event.client.*;
import org.example.seed.group.client.ClientCreateGroup;
import org.example.seed.group.client.ClientUpdateGroup;
import org.example.seed.rest.ClientRest;
import org.example.seed.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

/**
 * Created by PINA on 25/06/2017.
 */
@RestController
public class ClientRestImpl implements ClientRest {

  @Autowired
  private ClientService clientService;

  @Override
  public Mono<CatalogClientEvent> getClients(@RequestParam("page") final int page, @RequestParam("limit") final int limit)
    throws ExecutionException, InterruptedException {
    return this.clientService.requestClients(RequestAllClientEvent.builder().page(page).limit(limit).build());
  }

  @Override
  public Mono<ResponseClientEvent> createClient(@RequestBody @Validated(value = {ClientCreateGroup.class}) final CreateClientEvent event)
    throws ExecutionException, InterruptedException {
    return this.clientService.createClient(event);
  }

  @Override
  public Mono<ResponseClientEvent> getClient(@PathVariable("id") final String id)
    throws ExecutionException, InterruptedException {
    return this.clientService.requestClient(RequestClientEvent.builder().id(id).build());
  }

  @Override
  public Mono<ResponseClientEvent> updateClient(@RequestBody @Validated(value = {ClientUpdateGroup.class}) final UpdateClientEvent event)
    throws ExecutionException, InterruptedException {
    return this.clientService.updateClient(event);
  }

  @Override
  public Mono<ResponseClientEvent> deleteClient(@PathVariable("id") final String id)
    throws ExecutionException, InterruptedException {
    return this.clientService.deleteClient(DeleteClientEvent.builder().id(id).build());
  }
}
