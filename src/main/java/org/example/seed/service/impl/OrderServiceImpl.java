package org.example.seed.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.seed.catalog.OrderStatus;
import org.example.seed.domain.Order;
import org.example.seed.entity.ChefEntity;
import org.example.seed.entity.DishEntity;
import org.example.seed.entity.OrderEntity;
import org.example.seed.entity.PackageEntity;
import org.example.seed.event.order.*;
import org.example.seed.mapper.OrderMapper;
import org.example.seed.mapper.PackageMapper;
import org.example.seed.repository.*;
import org.example.seed.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by PINA on 30/06/2017.
 */

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final ChefRepository chefRepository;
  private final ClientRepository clientRepository;
  private final DishRepository dishRepository;
  private final OrderRepository orderRepository;
  private final PackageRepository packageRepository;
  private final OrderMapper orderMapper;
  private final PackageMapper packageMapper;

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public ListenableFuture<ResponseOrdersEvent> requestOrdersByClient(
    final String clientId, final RequestOrdersEvent event
  ) {

    final Page<OrderEntity> orderEntities = this.orderRepository
      .findAllByClient(clientId, PageRequest.of(event.getPage() - 1, event.getLimit()));

    return new AsyncResult<>(ResponseOrdersEvent.builder()
      .total(orderEntities.getTotalElements())
      .orders(this.orderMapper
        .mapListReverse(orderEntities.getContent()))
      .build());
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public ListenableFuture<ResponseOrdersEvent> requestOrdersByChef(final String chefId, final RequestOrdersEvent event) {

    final Page<OrderEntity> orderEntities = this.orderRepository
      .findAllByChef(chefId, PageRequest.of(event.getPage() - 1, event.getLimit()));

    return new AsyncResult<>(ResponseOrdersEvent
      .builder()
      .total(orderEntities.getTotalElements())
      .orders(this.orderMapper
        .mapListReverse(orderEntities.getContent()))
      .build());
  }

  @Override
  public ListenableFuture<ResponseOrderEvent> requestOrder(final RequestOrdersEvent event) {

    return null;
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseOrderEvent> createOrder(
    final String clientId, final ProcessOrderEvent event
  ) {

    final Order order = this.orderMapper
      .map(this.clientRepository
        .findById(clientId)
        .map(clientEntity -> {

          event.getOrder().setStatus(OrderStatus.CREATED);

          final OrderEntity currentOrder = this.mergePackages(event, OrderStatus.CREATED, clientId);

          currentOrder.setClient(clientEntity);

          this.orderRepository.save(currentOrder);

          return currentOrder;
        })
        .orElseThrow(RuntimeException::new));

    return new AsyncResult<>(ResponseOrderEvent.builder().order(order).build());
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseOrderEvent> registerOrder(
    final String clientId, final ProcessOrderEvent event
  ) {

    final Order order = this.orderMapper
      .map(this.orderRepository
        .findByClientAndOrder(clientId, event.getOrder().getId(), OrderStatus.CREATED)
        .map(orderEntity -> {

          final OrderEntity currentOrder = this.mergePackages(event, OrderStatus.CREATED, clientId);

          currentOrder.setRegisteredDate(new Date());
          currentOrder.setTimeZone(event.getTimeZone());
          currentOrder.setStatus(OrderStatus.PENDING_TO_ACCEPT);

          this.orderRepository.save(currentOrder);

          return currentOrder;
        })
        .orElseThrow(RuntimeException::new));

    return new AsyncResult<>(ResponseOrderEvent.builder().order(order).build());
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseOrderEvent> processOrder(
    final String clientId, final ProcessOrderEvent event
  ) {

    final Order order = this.orderMapper
      .map(this.orderRepository
        .findByClientAndOrder(clientId, event.getOrder().getId(), OrderStatus.PENDING_TO_ACCEPT)
        .map(orderEntity -> {

          final OrderEntity currentOrder = this.mergePackages(event, OrderStatus.PENDING_TO_ACCEPT, clientId);

          currentOrder.setStatus(OrderStatus.ACCEPTED);

          this.orderRepository.save(currentOrder);

          return currentOrder;
        })
        .orElseThrow(RuntimeException::new));

    return new AsyncResult<>(ResponseOrderEvent.builder().order(order).build());
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public ListenableFuture<ResponseOrderEvent> updateOrder(
    final String clientId, final ProcessOrderEvent event
  ) {

    final Order order = this.orderMapper
      .map(this.orderRepository
        .findByClientAndOrder(clientId, event.getOrder().getId(), OrderStatus.CREATED)
        .map(orderEntity -> this.orderRepository.save(this.mergePackages(event, OrderStatus.CREATED, clientId)))
        .orElseThrow(RuntimeException::new));

    return new AsyncResult<>(ResponseOrderEvent.builder().order(order).build());
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public void deleteOrder(final DeleteOrderEvent event) {

    this.orderRepository.findById(event.getId())
      .ifPresent(orderEntity -> {

        if (orderEntity.getStatus().equals(OrderStatus.CREATED)) {
          this.orderRepository.deleteById(event.getId());
        }
      });
  }

  private OrderEntity mergePackages(
    final ProcessOrderEvent event, final OrderStatus status, final String clientId
  ) {

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
      .findByClientAndOrder(clientId, event.getOrder().getId(), status)
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
