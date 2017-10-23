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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public Future<CatalogOrderEvent> requestOrdersByClient(final RequestAllOrderEvent event) {

    final Page<OrderEntity> orderEntities = this.orderRepository
      .findAllByClient(event.getIdClient(), PageRequest.of(event.getPage() - 1, event.getLimit()));

    return new AsyncResult<>(CatalogOrderEvent
      .builder()
      .total(orderEntities.getTotalElements())
      .orders(this.orderMapper
        .mapListReverse(orderEntities.getContent()))
      .build());
  }

  @Override
  @Async
  @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
  public Future<CatalogOrderEvent> requestOrdersByChef(final RequestAllOrderEvent event) {

    final Page<OrderEntity> orderEntities = this.orderRepository
      .findAllByChef(event.getIdClient(), PageRequest.of(event.getPage() - 1, event.getLimit()));

    return new AsyncResult<>(CatalogOrderEvent
      .builder()
      .total(orderEntities.getTotalElements())
      .orders(this.orderMapper
        .mapListReverse(orderEntities.getContent()))
      .build());
  }

  @Override
  public Future<ResponseOrderEvent> requestOrder(final RequestAllOrderEvent event) {
    return null;
  }

  @Override
  @Async
  @SuppressWarnings({"unchecked"})
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseOrderEvent> createOrder(final ProcessOrderEvent event) {
    return new AsyncResult(ResponseOrderEvent
      .builder()
      .order(this.orderMapper
        .map(this.clientRepository
          .findById(event.getIdClient())
          .map(clientEntity -> {

            event.getOrder().setComment(null);
            event.getOrder().setAddress(null);
            event.getOrder().setLatitude(null);
            event.getOrder().setLongitude(null);
            event.getOrder().setRejectedDate(null);
            event.getOrder().setFinishedDate(null);
            event.getOrder().setScheduledDate(null);
            event.getOrder().setRegisteredDate(null);
            event.getOrder().setStatus(OrderStatus.CREATED);

            final OrderEntity currentOrder = this.mergePackages(event, OrderStatus.CREATED);

            currentOrder.setClient(clientEntity);

            this.orderRepository.save(currentOrder);

            return currentOrder;
          })
          .orElseThrow(RuntimeException::new))));
  }

  @Override
  @Async
  @SuppressWarnings({"unchecked"})
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseOrderEvent> registerOrder(final ProcessOrderEvent event) {
    return new AsyncResult(ResponseOrderEvent
      .builder()
      .order(this.orderMapper
        .map(this.orderRepository
          .findByClientAndOrder(event.getIdClient(), event.getOrder().getId(), OrderStatus.CREATED)
          .map(orderEntity -> {

            final OrderEntity currentOrder = this.mergePackages(event, OrderStatus.CREATED);

            currentOrder.setRejectedDate(null);
            currentOrder.setFinishedDate(null);
            currentOrder.setRegisteredDate(new Date());
            currentOrder.setTimeZone(event.getTimeZone());
            currentOrder.setStatus(OrderStatus.PENDING_TO_ACCEPT);

            this.orderRepository.save(currentOrder);

            return currentOrder;
          })
          .orElseThrow(RuntimeException::new))));
  }

  @Override
  @Async
  @SuppressWarnings({"unchecked"})
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseOrderEvent> processOrder(final ProcessOrderEvent event) {
    return new AsyncResult(ResponseOrderEvent
      .builder()
      .order(this.orderMapper
        .map(this.orderRepository
          .findByClientAndOrder(event.getIdClient(), event.getOrder().getId(), OrderStatus.PENDING_TO_ACCEPT)
          .map(orderEntity -> {

            final OrderEntity currentOrder = this.mergePackages(event, OrderStatus.PENDING_TO_ACCEPT);

            currentOrder.setScheduledDate(new Date().toString());
            currentOrder.setStatus(OrderStatus.ACCEPTED);

            this.orderRepository.save(currentOrder);

            return currentOrder;
          })
          .orElseThrow(RuntimeException::new))));
  }

  @Override
  @Async
  @SuppressWarnings({"unchecked"})
  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Future<ResponseOrderEvent> updateOrder(final ProcessOrderEvent event) {
    return new AsyncResult(ResponseOrderEvent
      .builder()
      .order(this.orderMapper
        .map(this.orderRepository
          .findByClientAndOrder(event.getIdClient(), event.getOrder().getId(), OrderStatus.CREATED)
          .map(orderEntity -> this.orderRepository.save(this.mergePackages(event, OrderStatus.CREATED)))
          .orElseThrow(RuntimeException::new))));
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
