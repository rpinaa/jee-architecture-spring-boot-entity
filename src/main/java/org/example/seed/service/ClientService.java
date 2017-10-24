package org.example.seed.service;

import org.example.seed.event.client.*;

import java.util.concurrent.Future;

/**
 * Created by PINA on 16/06/2017.
 */
public interface ClientService {

  Future<ResponseClientsEvent> requestClients(final RequestClientsEvent event);

  Future<ResponseClientEvent> createClient(final CreateClientEvent event);

  Future<ResponseClientEvent> registerClient(final RegisterClientEvent event);

  Future<ResponseClientEvent> requestClient(final RequestClientEvent event);

  Future<ResponseClientEvent> updateClient(final UpdateClientEvent event);

  Future<ResponseClientEvent> deleteClient(final DeleteClientEvent event);
}
