package org.example.seed.service;

import org.example.seed.event.order.*;

import java.util.concurrent.Future;

/**
 * Created by PINA on 30/06/2017.
 */
public interface OrderService {

    Future<CatalogOrderEvent> requestOrders(final RequestAllOrderEvent event);

    Future<CatalogOrderEvent> requestOrder(final RequestAllOrderEvent event);

    Future<ResponseOrderEvent> createOrder(final ProcessOrderEvent event);

    Future<ResponseOrderEvent> registerOrder(final ProcessOrderEvent event);

    Future<ResponseOrderEvent> processOrder(final ProcessOrderEvent event);

    Future<ResponseOrderEvent> updateOrder(final ProcessOrderEvent event);

    Future<ResponseOrderEvent> deleteOrder(final DeleteOrderEvent event);
}
