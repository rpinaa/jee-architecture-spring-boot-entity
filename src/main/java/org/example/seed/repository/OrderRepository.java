package org.example.seed.repository;

import org.example.seed.catalog.OrderStatus;
import org.example.seed.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by PINA on 30/06/2017.
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    @Query("SELECT oe FROM ClientEntity ce JOIN ce.orders AS oe WHERE ce.id = ?1 AND oe.id = ?2")
    OrderEntity findOneByClientAndOrder(final String idClient, final String idOrder);

    @Query("SELECT oe FROM ClientEntity ce JOIN ce.orders AS oe WHERE ce.id = ?1 AND oe.id = ?2 AND oe.status = ?3")
    Optional<OrderEntity> findOneByClientAndOrder(final String idClient, final String idOrder, final OrderStatus status);
}
