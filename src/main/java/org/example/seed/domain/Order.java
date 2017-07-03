package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.seed.catalog.OrderStatus;
import org.example.seed.group.order.OrderCreateGroup;
import org.example.seed.group.order.OrderRegisterGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by PINA on 30/06/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends Dates {

    public Order() {
        this.id = UUID.randomUUID().toString();
    }

    @Size(min = 36, max = 36, groups = {OrderRegisterGroup.class})
    @NotEmpty(groups = {OrderRegisterGroup.class})
    private String id;

    @NotNull(groups = {OrderCreateGroup.class})
    private Double latitude;

    @NotNull(groups = {OrderCreateGroup.class})
    private Double longitude;

    @Size(max = 50, groups = {OrderCreateGroup.class})
    @NotEmpty(groups = {OrderCreateGroup.class})
    private String comment;

    @NotEmpty(groups = {OrderCreateGroup.class})
    private String scheduledDate;

    @NotNull(groups = {OrderRegisterGroup.class})
    private Float total;

    private Date registeredDate;
    private Date rejectedDate;
    private Date finishedDate;

    @NotNull(groups = {OrderRegisterGroup.class})
    private OrderStatus status;

    @Valid
    private Address address;

    @Valid
    private List<Package> packages;
}
