package org.example.seed.rest;

import org.example.seed.event.chef.*;
import org.example.seed.event.order.RequestOrdersEvent;
import org.example.seed.event.order.ResponseOrdersEvent;
import org.example.seed.group.chef.ChefCreateGroup;
import org.example.seed.group.chef.ChefRegisterGroup;
import org.example.seed.group.chef.ChefUpdateGroup;
import org.example.seed.service.ChefService;
import org.example.seed.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutionException;

/**
 * Created by PINA on 25/06/2017.
 */
@RestController
@RequestMapping(path = ChefRest.CHEF_ROOT_PATH)
public class ChefRest {

  public static final String CHEF_ROOT_PATH =  "/chefs";
  public static final String CHEF_CRUD_PATH =  "/{chefId}";
  public static final String ORDER_CRUD_PATH =  "/{chefId}/orders";

  private final ChefService chefService;
  private final OrderService orderService;

  @Autowired
  public ChefRest(final ChefService chefService, final OrderService orderService) {
    this.chefService = chefService;
    this.orderService = orderService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public DeferredResult<ResponseChefsEvent> getChefs(@RequestParam("page") final int page, @RequestParam("limit") final int limit) {

    final RequestChefsEvent event = RequestChefsEvent.builder().page(page).limit(limit).build();
    final DeferredResult<ResponseChefsEvent> dResult = new DeferredResult<>();

    this.chefService.requestChefs(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @GetMapping(ChefRest.ORDER_CRUD_PATH)
  @ResponseStatus(HttpStatus.OK)
  public DeferredResult<ResponseOrdersEvent> getOrdersByClient(
    @RequestParam("page") final int page,
    @RequestParam("limit") final int limit,
    @PathVariable("chefId") final String chefId
  )
    throws ExecutionException, InterruptedException {

    final RequestOrdersEvent event = RequestOrdersEvent.builder().chefId(chefId).page(page).limit(limit).build();
    final DeferredResult<ResponseOrdersEvent> dResult = new DeferredResult<>();

    this.orderService.requestOrdersByChef(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseChefEvent> createChef(
    @RequestBody @Validated(value = {ChefCreateGroup.class}) final CreateChefEvent event
  )
    throws ExecutionException, InterruptedException {

    final DeferredResult<ResponseChefEvent> dResult = new DeferredResult<>();

    this.chefService.createChef(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseChefEvent> registerClient(
    @RequestBody @Validated(value = {ChefRegisterGroup.class}) final RegisterChefEvent event
  )
    throws ExecutionException, InterruptedException {

    final DeferredResult<ResponseChefEvent> dResult = new DeferredResult<>();

    this.chefService.registerChef(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @GetMapping(ChefRest.CHEF_CRUD_PATH)
  @ResponseStatus(HttpStatus.OK)
  public DeferredResult<ResponseChefEvent> getChef(@PathVariable("chefId") final String id)
    throws ExecutionException, InterruptedException {

    final RequestChefEvent event = RequestChefEvent.builder().id(id).build();
    final DeferredResult<ResponseChefEvent> dResult = new DeferredResult<>();

    this.chefService.requestChef(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @PutMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseChefEvent> updateChef(
    @RequestBody @Validated(value = {ChefUpdateGroup.class}) final UpdateChefEvent event
  )
    throws ExecutionException, InterruptedException {

    final DeferredResult<ResponseChefEvent> dResult = new DeferredResult<>();

    this.chefService.updateChef(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }

  @DeleteMapping(ChefRest.CHEF_CRUD_PATH)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public DeferredResult<ResponseChefEvent> deleteChef(@PathVariable("chefId") final String id)
    throws ExecutionException, InterruptedException {

    final DeleteChefEvent event = DeleteChefEvent.builder().id(id).build();
    final DeferredResult<ResponseChefEvent> dResult = new DeferredResult<>();

    this.chefService.deleteChef(event)
      .addCallback(
        dResult::setResult,
        e -> dResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)));

    return dResult;
  }
}
