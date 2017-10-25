package org.example.seed.rest;

import org.example.seed.event.client.*;
import org.example.seed.event.order.ProcessOrderEvent;
import org.example.seed.event.order.RequestOrdersEvent;
import org.example.seed.event.order.ResponseOrderEvent;
import org.example.seed.event.order.ResponseOrdersEvent;
import org.example.seed.group.client.ClientCreateGroup;
import org.example.seed.group.client.ClientRegisterGroup;
import org.example.seed.group.client.ClientUpdateGroup;
import org.example.seed.group.order.OrderCreateGroup;
import org.example.seed.group.order.OrderRegisterGroup;
import org.example.seed.service.ClientService;
import org.example.seed.service.OrderService;
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
  private final OrderService orderService;

  @Autowired
  public ClientRest(final ClientService clientService, final OrderService orderService) {
    this.clientService = clientService;
    this.orderService = orderService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseClientsEvent> getClients(
    @RequestParam("page") final int page,
    @RequestParam("limit") final int limit
  )
    throws ExecutionException, InterruptedException {

    final RequestClientsEvent event = RequestClientsEvent.builder().page(page).limit(limit).build();

    return Mono
      .justOrEmpty(this.clientService
        .requestClients(event)
        .get());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseClientEvent> createClient(
    @RequestBody @Validated(value = {ClientCreateGroup.class}) final CreateClientEvent event
  )
    throws ExecutionException, InterruptedException {
    return Mono
      .justOrEmpty(this.clientService
        .createClient(event)
        .get());
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseClientEvent> registerClient(
    @RequestBody @Validated(value = {ClientRegisterGroup.class}) final RegisterClientEvent event
  )
    throws ExecutionException, InterruptedException {
    return Mono
      .justOrEmpty(this.clientService
        .registerClient(event)
        .get());
  }

  @GetMapping(value = "/{clientId}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseClientEvent> getClient(@PathVariable("clientId") final String id)
    throws ExecutionException, InterruptedException {

    final RequestClientEvent event = RequestClientEvent.builder().id(id).build();

    return Mono
      .justOrEmpty(this.clientService
        .requestClient(event)
        .get());
  }

  @PutMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseClientEvent> updateClient(
    @RequestBody @Validated(value = {ClientUpdateGroup.class}) final UpdateClientEvent event
  )
    throws ExecutionException, InterruptedException {
    return Mono
      .justOrEmpty(this.clientService
        .updateClient(event)
        .get());
  }

  @DeleteMapping(value = "/{clientId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseClientEvent> deleteClient(@PathVariable("clientId") final String id)
    throws ExecutionException, InterruptedException {

    final DeleteClientEvent event = DeleteClientEvent.builder().id(id).build();

    return Mono
      .justOrEmpty(this.clientService
        .deleteClient(event)
        .get());
  }

  @GetMapping("/{clientId}/orders")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseOrdersEvent> getOrdersByClient(
    @RequestParam("page") final int page,
    @RequestParam("limit") final int limit,
    @PathVariable("clientId") final String clientId
  )
    throws ExecutionException, InterruptedException {

    final RequestOrdersEvent event = RequestOrdersEvent.builder().clientId(clientId).page(page).limit(limit).build();

    return Mono
      .justOrEmpty(this.orderService
        .requestOrdersByClient(event)
        .get());
  }

  @PostMapping("/{clientId}/orders")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseOrderEvent> createOrder(
    @RequestBody @Validated(value = {OrderCreateGroup.class}) final ProcessOrderEvent event
  )
    throws ExecutionException, InterruptedException {
    return Mono
      .justOrEmpty(this.orderService
        .createOrder(event)
        .get());
  }

  @PatchMapping("/{clientId}/orders")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseOrderEvent> registerOrder(
    @RequestBody @Validated(value = {OrderRegisterGroup.class}) final ProcessOrderEvent event
  )
    throws ExecutionException, InterruptedException {
    return Mono
      .justOrEmpty(this.orderService
        .registerOrder(event)
        .get());
  }

  @PutMapping("/{clientId}/orders")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseOrderEvent> updateOrder(
    @RequestBody @Validated(value = {OrderCreateGroup.class}) final ProcessOrderEvent event
  )
    throws ExecutionException, InterruptedException {
    return Mono
      .justOrEmpty(this.orderService
        .updateOrder(event)
        .get());
  }
}
