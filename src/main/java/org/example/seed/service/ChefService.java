package org.example.seed.service;

import org.example.seed.event.chef.*;
import reactor.core.publisher.Mono;

/**
 * Created by PINA on 15/06/2017.
 */
public interface ChefService {

    Mono<CatalogChefEvent> requestChefs(final RequestAllChefEvent event);

    Mono<ResponseChefEvent> createChef(final CreateChefEvent event);

    Mono<ResponseChefEvent> requestChef(final RequestChefEvent event);

    Mono<ResponseChefEvent> updateChef(final UpdateChefEvent event);

    Mono<ResponseChefEvent> deleteChef(final DeleteChefEvent event);
}
