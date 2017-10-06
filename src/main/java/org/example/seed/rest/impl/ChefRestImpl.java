package org.example.seed.rest.impl;

import org.example.seed.event.chef.*;
import org.example.seed.group.chef.ChefCreateGroup;
import org.example.seed.group.chef.ChefUpdateGroup;
import org.example.seed.rest.ChefRest;
import org.example.seed.service.ChefService;
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
public class ChefRestImpl implements ChefRest {

  @Autowired
  private ChefService chefService;

  @Override
  public Mono<CatalogChefEvent> getChefs(@RequestParam("page") final int page, @RequestParam("limit") final int limit)
    throws ExecutionException, InterruptedException {
    return Mono.justOrEmpty(this.chefService.requestChefs(RequestAllChefEvent.builder().page(page).limit(limit).build()).get());
  }

  @Override
  public Mono<ResponseChefEvent> createChef(@RequestBody @Validated(value = {ChefCreateGroup.class}) final CreateChefEvent event)
    throws ExecutionException, InterruptedException {
    return Mono.justOrEmpty(this.chefService.createChef(event).get());
  }

  @Override
  public Mono<ResponseChefEvent> getChef(@PathVariable("id") final String id)
    throws ExecutionException, InterruptedException {
    return Mono.justOrEmpty(this.chefService.requestChef(RequestChefEvent.builder().id(id).build()).get());
  }

  @Override
  public Mono<ResponseChefEvent> updateChef(@RequestBody @Validated(value = {ChefUpdateGroup.class}) final UpdateChefEvent event)
    throws ExecutionException, InterruptedException {
    return Mono.justOrEmpty(this.chefService.updateChef(event).get());
  }

  @Override
  public Mono<ResponseChefEvent> deleteChef(@PathVariable("id") final String id)
    throws ExecutionException, InterruptedException {
    return Mono.justOrEmpty(this.chefService.deleteChef(DeleteChefEvent.builder().id(id).build()).get());
  }
}
