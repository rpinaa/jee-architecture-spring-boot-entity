package org.example.seed.rest;

import org.example.seed.event.client.CatalogClientEvent;
import org.example.seed.event.client.CreateClientEvent;
import org.example.seed.event.client.ResponseClientEvent;
import org.example.seed.event.client.UpdateClientEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

/**
 * Created by PINA on 16/06/2017.
 */
@RequestMapping(path = "/clients")
public interface ClientRest {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Mono<CatalogClientEvent> getClients(final int page, final int limit) throws ExecutionException, InterruptedException;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<ResponseClientEvent> createClient(final CreateClientEvent event) throws ExecutionException, InterruptedException;

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    Mono<ResponseClientEvent> getClient(final String id) throws ExecutionException, InterruptedException;

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<ResponseClientEvent> updateClient(final UpdateClientEvent event) throws ExecutionException, InterruptedException;

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<ResponseClientEvent> deleteClient(final String id) throws ExecutionException, InterruptedException;
}
