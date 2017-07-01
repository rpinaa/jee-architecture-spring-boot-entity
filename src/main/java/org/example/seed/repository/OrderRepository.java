package org.example.seed.repository;

import org.example.seed.domain.Order;
import org.example.seed.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by PINA on 30/06/2017.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, OrderEntity> {
}
