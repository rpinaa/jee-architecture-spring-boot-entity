package org.example.seed.mapper;

import org.example.seed.domain.Order;
import org.example.seed.entity.OrderEntity;
import org.example.seed.mapper.jpa.GenericMapper;
import org.mapstruct.Mapper;

/**
 * Created by PINA on 30/06/2017.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends GenericMapper<Order, OrderEntity> {

    Order map(final OrderEntity orderEntity);

    OrderEntity map(final Order order);
}
