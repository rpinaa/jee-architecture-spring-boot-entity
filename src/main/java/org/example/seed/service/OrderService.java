package org.example.seed.service;

import org.example.seed.event.order.*;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Created by PINA on 30/06/2017.
 */
public interface OrderService {

  ListenableFuture<ResponseOrdersEvent> requestOrdersByClient(final String clientId, final RequestOrdersEvent event);

  ListenableFuture<ResponseOrdersEvent> requestOrdersByChef(final String chefId, final RequestOrdersEvent event);

  ListenableFuture<ResponseOrderEvent> requestOrder(final RequestOrdersEvent event);

  ListenableFuture<ResponseOrderEvent> createOrder(final String clientId, final ProcessOrderEvent event);

  ListenableFuture<ResponseOrderEvent> registerOrder(final String clientId, final ProcessOrderEvent event);

  ListenableFuture<ResponseOrderEvent> processOrder(final String clientId, final ProcessOrderEvent event);

  ListenableFuture<ResponseOrderEvent> updateOrder(final String clientId, final ProcessOrderEvent event);

  void deleteOrder(final DeleteOrderEvent event);
}
