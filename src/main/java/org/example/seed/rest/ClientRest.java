package org.example.seed.rest;

import lombok.RequiredArgsConstructor;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by PINA on 25/06/2017.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ClientRest.CLIENT_ROOT_PATH)
public class ClientRest {

  public static final String CLIENT_ROOT_PATH = "/clients";
  public static final String CLIENT_CRUD_PATH = "/{clientId}";
  public static final String ORDER_CRUD_PATH = "/{clientId}/orders";

  private final ClientService clientService;
  private final OrderService orderService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public DeferredResult<ResponseClientsEvent> getClients(
    @RequestParam("page") final int page,
    @RequestParam("limit") final int limit
  ) {

    final RequestClientsEvent event = RequestClientsEvent.builder().page(page).limit(limit).build();
    final DeferredResult<ResponseClientsEvent> dResult = new DeferredResult<>();

    this.clientService.requestClients(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseClientEvent> createClient(
    @RequestBody @Validated(value = {ClientCreateGroup.class}) final CreateClientEvent event
  ) {

    final DeferredResult<ResponseClientEvent> dResult = new DeferredResult<>();

    this.clientService.createClient(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseClientEvent> registerClient(
    @RequestBody @Validated(value = {ClientRegisterGroup.class}) final RegisterClientEvent event
  ) {

    final DeferredResult<ResponseClientEvent> dResult = new DeferredResult<>();

    this.clientService.registerClient(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @GetMapping(ClientRest.CLIENT_CRUD_PATH)
  @ResponseStatus(HttpStatus.OK)
  public DeferredResult<ResponseClientEvent> getClient(@PathVariable("clientId") final String id) {

    final RequestClientEvent event = RequestClientEvent.builder().id(id).build();
    final DeferredResult<ResponseClientEvent> dResult = new DeferredResult<>();

    this.clientService.requestClient(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PutMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseClientEvent> updateClient(
    @RequestBody @Validated(value = {ClientUpdateGroup.class}) final UpdateClientEvent event
  ) {

    final DeferredResult<ResponseClientEvent> dResult = new DeferredResult<>();

    this.clientService.updateClient(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @DeleteMapping(ClientRest.CLIENT_CRUD_PATH)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseClientEvent> deleteClient(@PathVariable("clientId") final String id) {

    final DeleteClientEvent event = DeleteClientEvent.builder().id(id).build();
    final DeferredResult<ResponseClientEvent> dResult = new DeferredResult<>();

    this.clientService.deleteClient(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @GetMapping(ClientRest.ORDER_CRUD_PATH)
  @ResponseStatus(HttpStatus.OK)
  public DeferredResult<ResponseOrdersEvent> getOrdersByClient(
    @RequestParam("page") final int page,
    @RequestParam("limit") final int limit,
    @PathVariable("clientId") final String clientId
  ) {

    final RequestOrdersEvent event = RequestOrdersEvent.builder().page(page).limit(limit).build();
    final DeferredResult<ResponseOrdersEvent> dResult = new DeferredResult<>();

    this.orderService.requestOrdersByClient(clientId, event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PostMapping(ClientRest.ORDER_CRUD_PATH)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseOrderEvent> createOrder(
    @RequestParam("clientId") final String clientId,
    @RequestBody @Validated(value = {OrderCreateGroup.class}) final ProcessOrderEvent event
  ) {

    final DeferredResult<ResponseOrderEvent> dResult = new DeferredResult<>();

    this.orderService.createOrder(clientId, event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PatchMapping(ClientRest.ORDER_CRUD_PATH)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseOrderEvent> registerOrder(
    @RequestParam("clientId") final String clientId,
    @RequestBody @Validated(value = {OrderRegisterGroup.class}) final ProcessOrderEvent event
  ) {

    final DeferredResult<ResponseOrderEvent> dResult = new DeferredResult<>();

    this.orderService.registerOrder(clientId, event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PutMapping(ClientRest.ORDER_CRUD_PATH)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseOrderEvent> updateOrder(
    @RequestParam("clientId") final String clientId,
    @RequestBody @Validated(value = {OrderCreateGroup.class}) final ProcessOrderEvent event
  ) {

    final DeferredResult<ResponseOrderEvent> dResult = new DeferredResult<>();

    this.orderService.updateOrder(clientId, event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }
}
