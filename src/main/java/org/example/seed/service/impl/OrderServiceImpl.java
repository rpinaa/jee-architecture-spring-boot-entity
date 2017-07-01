package org.example.seed.service.impl;

import org.example.seed.event.order.*;
import org.example.seed.mapper.OrderMapper;
import org.example.seed.repository.OrderRepository;
import org.example.seed.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by PINA on 30/06/2017.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Future<CatalogOrderEvent> requestOrders(final RequestAllOrderEvent event) {
        return null;
    }

    @Override
    public Future<CatalogOrderEvent> requestOrder(final RequestAllOrderEvent event) {
        return null;
    }

    @Override
    public Future<ResponseOrderEvent> createOrder(final ProcessOrderEvent event) {
        return null;
    }

    @Override
    public Future<ResponseOrderEvent> registerOrder(final ProcessOrderEvent event) {
        return null;
    }

    @Override
    public Future<ResponseOrderEvent> processOrder(final ProcessOrderEvent event) {
        return null;
    }

    @Override
    public Future<ResponseOrderEvent> updateOrder(final ProcessOrderEvent event) {
        return null;
    }

    @Override
    public Future<ResponseOrderEvent> deleteOrder(final DeleteOrderEvent event) {
        return null;
    }
}
