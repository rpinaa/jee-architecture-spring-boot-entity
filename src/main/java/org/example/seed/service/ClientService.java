package org.example.seed.service;

import org.example.seed.event.client.*;
import reactor.core.publisher.Mono;

/**
 * Created by PINA on 16/06/2017.
 */
public interface ClientService {

    Mono<CatalogClientEvent> requestClients(final RequestAllClientEvent event);

    Mono<ResponseClientEvent> createClient(final CreateClientEvent event);

    Mono<ResponseClientEvent> requestClient(final RequestClientEvent event);

    Mono<ResponseClientEvent> updateClient(final UpdateClientEvent event);

    Mono<ResponseClientEvent> deleteClient(final DeleteClientEvent event);
}
