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

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
  private OrderRepository orderRepository;

  @Autowired
  private PackageRepository packageRepository;

  @Autowired
  private OrderMapper orderMapper;

  @Autowired
  private PackageMapper packageMapper;

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

    this.clientRepository.findById(event.getIdClient())
      .ifPresent(clientEntity -> {

        event.getOrder().setComment(null);
        event.getOrder().setAddress(null);
        event.getOrder().setLatitude(null);
        event.getOrder().setLongitude(null);
        event.getOrder().setRejectedDate(null);
        event.getOrder().setFinishedDate(null);
        event.getOrder().setScheduledDate(null);
        event.getOrder().setRegisteredDate(null);
        event.getOrder().setStatus(OrderStatus.CREATED);

        final OrderEntity orderEntity = this.mergePackages(event, OrderStatus.CREATED);

        orderEntity.setClient(clientEntity);

        this.orderRepository.save(orderEntity);
      });

    return new AsyncResult<>(null);
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseOrderEvent> registerOrder(final ProcessOrderEvent event) {

    final OrderEntity currentOrder = this.mergePackages(event, OrderStatus.CREATED);

    currentOrder.setRejectedDate(null);
    currentOrder.setFinishedDate(null);
    currentOrder.setRegisteredDate(new Date());
    currentOrder.setTimeZone(event.getTimeZone());
    currentOrder.setStatus(OrderStatus.PENDING_TO_ACCEPT);

    this.orderRepository.save(currentOrder);

    return new AsyncResult<>(null);
  }

  @Override
  public Future<ResponseOrderEvent> processOrder(final ProcessOrderEvent event) {

    this.orderRepository
      .findByClientAndOrder(event.getIdClient(), event.getOrder().getId(), OrderStatus.PENDING_TO_ACCEPT)
      .ifPresent(orderEntity -> {

        final OrderEntity currentOrder = this.mergePackages(event, OrderStatus.PENDING_TO_ACCEPT);

        currentOrder.setScheduledDate(new Date().toString());
        currentOrder.setStatus(OrderStatus.ACCEPTED);

        this.orderRepository.save(currentOrder);
      });

    return new AsyncResult<>(null);
  }

  @Override
  public Future<ResponseOrderEvent> updateOrder(final ProcessOrderEvent event) {

    if (this.orderRepository
      .findIdByClientAndOrder(event.getIdClient(), event.getOrder().getId(), OrderStatus.CREATED)
      .isPresent()) {
      this.orderRepository.save(this.mergePackages(event, OrderStatus.CREATED));
    }

    return new AsyncResult<>(null);
  }

  @Override
  public Future<ResponseOrderEvent> deleteOrder(final DeleteOrderEvent event) {

    this.orderRepository.findById(event.getId())
      .ifPresent(orderEntity -> {

        if (orderEntity.getStatus().equals(OrderStatus.CREATED)) {
          this.orderRepository.deleteById(event.getId());
        }
      });

    return new AsyncResult<>(null);
  }

  private OrderEntity mergePackages(final ProcessOrderEvent event, final OrderStatus status) {

    final Stream<PackageEntity> packageEntities = this.packageMapper
      .mapList(event.getOrder().getPackages())
      .parallelStream()
      .distinct()
      .sorted(Comparator.comparing(pe -> pe.getDish().getId()));

    final List<DishEntity> dishEntities = this.dishRepository
      .findAllById(packageEntities
        .map(PackageEntity::getId)
        .collect(Collectors.toList()));

    final ChefEntity chefEntity = this.chefRepository
      .findOneByDish(dishEntities.get(0).getId());

    this.dishRepository.findAllByChef(chefEntity.getId())
      .ifPresent(currentDishEntities -> {
        if (!currentDishEntities.containsAll(dishEntities)) {
          throw new RuntimeException();
        }
      });

    final OrderEntity orderEntity = this.orderRepository
      .findByClientAndOrder(event.getIdClient(), event.getOrder().getId(), status)
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
