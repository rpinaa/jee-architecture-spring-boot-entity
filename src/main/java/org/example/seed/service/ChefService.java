package org.example.seed.service;

import org.example.seed.event.chef.*;

import java.util.concurrent.Future;

/**
 * Created by PINA on 15/06/2017.
 */
public interface ChefService {

  Future<ResponseChefsEvent> requestChefs(final RequestChefsEvent event);

  Future<ResponseChefEvent> createChef(final CreateChefEvent event);

  Future<ResponseChefEvent> registerChef(final CreateChefEvent event);

  Future<ResponseChefEvent> requestChef(final RequestChefEvent event);

  Future<ResponseChefEvent> updateChef(final UpdateChefEvent event);

  Future<ResponseChefEvent> deleteChef(final DeleteChefEvent event);
}
