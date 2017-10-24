package org.example.seed.rest;

import org.example.seed.event.client.*;
import org.example.seed.group.client.ClientCreateGroup;
import org.example.seed.group.client.ClientUpdateGroup;
import org.example.seed.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

/**
 * Created by PINA on 25/06/2017.
 */
@RestController
@RequestMapping(path = "/clients")
public class ClientRest {

  private final ClientService clientService;

  @Autowired
  public ClientRest(final ClientService clientService) {
    this.clientService = clientService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<CatalogClientEvent> getClients(
    @RequestParam("page") final int page, @RequestParam("limit") final int limit
  )
    throws ExecutionException, InterruptedException {
    return Mono.justOrEmpty(this.clientService
      .requestClients(RequestAllClientEvent.builder()
        .page(page)
        .limit(limit)
        .build())
      .get());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseClientEvent> createClient(
    @RequestBody @Validated(value = {ClientCreateGroup.class}) final CreateClientEvent event
  )
    throws ExecutionException, InterruptedException {
    return Mono.justOrEmpty(this.clientService.createClient(event).get());
  }

  @GetMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseClientEvent> getClient(@PathVariable("id") final String id)
    throws ExecutionException, InterruptedException {
    return Mono.justOrEmpty(this.clientService
      .requestClient(RequestClientEvent.builder()
        .id(id)
        .build())
      .get());
  }

  @PutMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseClientEvent> updateClient(
    @RequestBody @Validated(value = {ClientUpdateGroup.class}) final UpdateClientEvent event
  )
    throws ExecutionException, InterruptedException {
    return Mono.justOrEmpty(this.clientService.updateClient(event).get());
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseClientEvent> deleteClient(@PathVariable("id") final String id)
    throws ExecutionException, InterruptedException {
    return Mono.justOrEmpty(this.clientService
      .deleteClient(DeleteClientEvent.builder()
        .id(id)
        .build())
      .get());
  }
}
