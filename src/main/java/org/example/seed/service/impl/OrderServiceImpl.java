package org.example.seed.service.impl;

import org.example.seed.catalog.OrderStatus;
import org.example.seed.entity.ChefEntity;
import org.example.seed.entity.DishEntity;
import org.example.seed.entity.OrderEntity;
import org.example.seed.entity.PackageEntity;
import org.example.seed.event.order.*;
import org.example.seed.mapper.OrderMapper;
import org.example.seed.mapper.PackageMapper;
import org.example.seed.repository.*;
import org.example.seed.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by PINA on 30/06/2017.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ChefRepository chefRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PackageMapper packageMapper;

    @Autowired
    private PackageRepository packageRepository;

    @Override
    public Future<CatalogOrderEvent> requestOrders(final RequestAllOrderEvent event) {
        return null;
    }

    @Override
    public Future<CatalogOrderEvent> requestOrder(final RequestAllOrderEvent event) {
        return null;
    }

    @Override
    @Async
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<ResponseOrderEvent> createOrder(final ProcessOrderEvent event) {

        event.getOrder().setComment(null);
        event.getOrder().setAddress(null);
        event.getOrder().setLatitude(null);
        event.getOrder().setLongitude(null);
        event.getOrder().setRejectedDate(null);
        event.getOrder().setFinishedDate(null);
        event.getOrder().setRegisteredDate(null);
        event.getOrder().setStatus(OrderStatus.CREATED);

        this.orderRepository.save(this.mergePackages(event, OrderStatus.CREATED));

        return new AsyncResult<>(null);
    }

    @Override
    @Async
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<ResponseOrderEvent> registerOrder(final ProcessOrderEvent event) {

        final OrderEntity orderEntity = this.mergePackages(event, OrderStatus.CREATED);

        orderEntity.setRejectedDate(null);
        orderEntity.setFinishedDate(null);
        orderEntity.setRegisteredDate(new Date());
        orderEntity.setTimeZone(event.getTimeZone());
        orderEntity.setStatus(OrderStatus.PENDING_TO_ACCEPT);
        orderEntity.setClient(this.clientRepository.findOne(event.getIdClient()));

        this.orderRepository.save(orderEntity);

        return new AsyncResult<>(null);
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

    private OrderEntity mergePackages(final ProcessOrderEvent event, final OrderStatus status) {

        final Stream<PackageEntity> packageEntities = this.packageMapper
                .mapList(event.getOrder().getPackages())
                .parallelStream()
                .distinct()
                .sorted(Comparator.comparing(pe -> pe.getDish().getId()));

        final List<DishEntity> dishEntities = this.dishRepository
                .findAll(packageEntities
                        .map(PackageEntity::getId)
                        .collect(Collectors.toList()));

        final ChefEntity chefEntity = this.chefRepository
                .findOneByDish(dishEntities.get(0).getId());

        Optional.of(this.dishRepository.findAllByChef(chefEntity.getId()))
                .filter(de -> de.size() < 0 && de.containsAll(dishEntities))
                .orElseThrow(RuntimeException::new);

        final OrderEntity orderEntity = Optional.of(this.orderRepository
                .findOneByClientAndOrder(event.getIdClient(), event.getOrder().getId(), status))
                .map(oe -> {
                    this.packageRepository.deleteInBatch(oe.getPackages());

                    return oe;
                })
                .orElseGet(() -> this.orderMapper.map(event.getOrder()));

        final AtomicInteger atomicInteger = new AtomicInteger(0);

        packageEntities.forEach(pe -> {
            pe.setId(UUID.randomUUID().toString());
            pe.setOrder(orderEntity);
            pe.setDish(dishEntities.get(atomicInteger.get()));
            pe.setPrice(dishEntities.get(atomicInteger.getAndIncrement()).getPrice());
        });

        orderEntity.setPackages(packageEntities.collect(Collectors.toList()));
        orderEntity.setChef(chefEntity);
        orderEntity.setTotal((float) packageEntities
                .mapToDouble(pe -> pe.getPrice() * pe.getQuantity())
                .sum());

        return orderEntity;
    }
}
