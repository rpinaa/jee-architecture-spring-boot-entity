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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
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
  public DeferredResult<ResponseClientsEvent> getClients(
    @RequestParam("page") final int page,
    @RequestParam("limit") final int limit
  )
    throws ExecutionException, InterruptedException {

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
  )
    throws ExecutionException, InterruptedException {

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
  )
    throws ExecutionException, InterruptedException {

    final DeferredResult<ResponseClientEvent> dResult = new DeferredResult<>();

    this.clientService.registerClient(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @GetMapping(value = "/{clientId}")
  @ResponseStatus(HttpStatus.OK)
  public DeferredResult<ResponseClientEvent> getClient(@PathVariable("clientId") final String id)
    throws ExecutionException, InterruptedException {

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
    @RequestBody @Validated(value = {ClientUpdateGroup.class}) final UpdateClientEvent event,
    final HttpServletRequest request
  )
    throws ExecutionException, InterruptedException {

    final DeferredResult<ResponseClientEvent> dResult = new DeferredResult<>();

    event.setIp(request.getRemoteAddr());

    this.clientService.updateClient(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @DeleteMapping(value = "/{clientId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseClientEvent> deleteClient(@PathVariable("clientId") final String id)
    throws ExecutionException, InterruptedException {

    final DeleteClientEvent event = DeleteClientEvent.builder().id(id).build();
    final DeferredResult<ResponseClientEvent> dResult = new DeferredResult<>();

    this.clientService.deleteClient(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @GetMapping("/{clientId}/orders")
  @ResponseStatus(HttpStatus.OK)
  public DeferredResult<ResponseOrdersEvent> getOrdersByClient(
    @RequestParam("page") final int page,
    @RequestParam("limit") final int limit,
    @PathVariable("clientId") final String clientId
  )
    throws ExecutionException, InterruptedException {

    final RequestOrdersEvent event = RequestOrdersEvent.builder().clientId(clientId).page(page).limit(limit).build();
    final DeferredResult<ResponseOrdersEvent> dResult = new DeferredResult<>();

    this.orderService.requestOrdersByClient(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PostMapping("/{clientId}/orders")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseOrderEvent> createOrder(
    @RequestBody @Validated(value = {OrderCreateGroup.class}) final ProcessOrderEvent event
  )
    throws ExecutionException, InterruptedException {

    final DeferredResult<ResponseOrderEvent> dResult = new DeferredResult<>();

    this.orderService.createOrder(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PatchMapping("/{clientId}/orders")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseOrderEvent> registerOrder(
    @RequestBody @Validated(value = {OrderRegisterGroup.class}) final ProcessOrderEvent event
  )
    throws ExecutionException, InterruptedException {

    final DeferredResult<ResponseOrderEvent> dResult = new DeferredResult<>();

    this.orderService.registerOrder(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PutMapping("/{clientId}/orders")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseOrderEvent> updateOrder(
    @RequestBody @Validated(value = {OrderCreateGroup.class}) final ProcessOrderEvent event
  )
    throws ExecutionException, InterruptedException {

    final DeferredResult<ResponseOrderEvent> dResult = new DeferredResult<>();

    this.orderService.updateOrder(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }
}
