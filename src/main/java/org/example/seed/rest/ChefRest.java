package org.example.seed.rest;

import org.example.seed.event.chef.*;
import org.example.seed.event.order.RequestOrdersEvent;
import org.example.seed.event.order.ResponseOrdersEvent;
import org.example.seed.group.chef.ChefCreateGroup;
import org.example.seed.group.chef.ChefUpdateGroup;
import org.example.seed.service.ChefService;
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
@RequestMapping(path = "/chefs")
public class ChefRest {

  private final ChefService chefService;
  private final OrderService orderService;

  @Autowired
  public ChefRest(final ChefService chefService, final OrderService orderService) {
    this.chefService = chefService;
    this.orderService = orderService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseChefsEvent> getChefs(@RequestParam("page") final int page, @RequestParam("limit") final int limit)
    throws ExecutionException, InterruptedException {

    final RequestChefsEvent event = RequestChefsEvent.builder().page(page).limit(limit).build();

    return Mono
      .justOrEmpty(this.chefService
        .requestChefs(event)
        .get());
  }

  @GetMapping("/{chefId}/orders")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseOrdersEvent> getOrdersByClient(
    @RequestParam("page") final int page,
    @RequestParam("limit") final int limit,
    @PathVariable("chefId") final String chefId
  )
    throws ExecutionException, InterruptedException {

    final RequestOrdersEvent event = RequestOrdersEvent.builder().chefId(chefId).page(page).limit(limit).build();

    return Mono
      .justOrEmpty(this.orderService
        .requestOrdersByChef(event)
        .get());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseChefEvent> createChef(
    @RequestBody @Validated(value = {ChefCreateGroup.class}) final CreateChefEvent event
  )
    throws ExecutionException, InterruptedException {
    return Mono
      .justOrEmpty(this.chefService
        .createChef(event)
        .get());
  }

  @GetMapping(value = "/{chefId}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseChefEvent> getChef(@PathVariable("chefId") final String id)
    throws ExecutionException, InterruptedException {

    final RequestChefEvent event = RequestChefEvent.builder().id(id).build();

    return Mono
      .justOrEmpty(this.chefService
        .requestChef(event)
        .get());
  }

  @PutMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseChefEvent> updateChef(
    @RequestBody @Validated(value = {ChefUpdateGroup.class}) final UpdateChefEvent event
  )
    throws ExecutionException, InterruptedException {
    return Mono
      .justOrEmpty(this.chefService
        .updateChef(event)
        .get());
  }

  @DeleteMapping(value = "/{chefId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<ResponseChefEvent> deleteChef(@PathVariable("chefId") final String id)
    throws ExecutionException, InterruptedException {

    final DeleteChefEvent event = DeleteChefEvent.builder().id(id).build();

    return Mono
      .justOrEmpty(this.chefService
        .deleteChef(event)
        .get());
  }
}
