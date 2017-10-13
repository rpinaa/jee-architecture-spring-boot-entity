package org.example.seed.service;

import org.example.seed.event.order.*;

import java.util.concurrent.Future;

/**
 * Created by PINA on 30/06/2017.
 */
public interface OrderService {

  Future<CatalogOrderEvent> requestOrders(final RequestAllOrderEvent event);

  Future<ResponseOrderEvent> requestOrder(final RequestAllOrderEvent event);

  void createOrder(final ProcessOrderEvent event);

  void registerOrder(final ProcessOrderEvent event);

  void processOrder(final ProcessOrderEvent event);

  Future<ResponseOrderEvent> updateOrder(final ProcessOrderEvent event);

  void deleteOrder(final DeleteOrderEvent event);
}
