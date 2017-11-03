package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.seed.catalog.OrderStatus;
import org.example.seed.group.order.OrderCreateGroup;
import org.example.seed.group.order.OrderRegisterGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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

  @Size(min = 36, max = 36, groups = {OrderRegisterGroup.class})
  @NotEmpty(groups = {OrderRegisterGroup.class})
  private String id;

  @Null(groups = {OrderCreateGroup.class})
  private Double latitude;

  @Null(groups = {OrderCreateGroup.class})
  private Double longitude;

  @Null(groups = {OrderCreateGroup.class})
  @NotEmpty(groups = {OrderRegisterGroup.class})
  @Size(max = 50, groups = {OrderRegisterGroup.class})
  private String comment;

  @Null(groups = {OrderCreateGroup.class})
  @NotEmpty(groups = {OrderRegisterGroup.class})
  private String scheduledDate;

  @NotNull(groups = {OrderRegisterGroup.class})
  private Float total;

  @Null(groups = {OrderCreateGroup.class})
  private Date registeredDate;

  @Null(groups = {OrderCreateGroup.class, OrderRegisterGroup.class})
  private Date rejectedDate;

  @Null(groups = {OrderCreateGroup.class, OrderRegisterGroup.class})
  private Date finishedDate;

  @NotNull(groups = {OrderRegisterGroup.class})
  private OrderStatus status;

  @Valid
  @Null(groups = {OrderCreateGroup.class})
  private Address address;

  @Valid
  private List<Package> packages;

  public Order() {
    this.id = UUID.randomUUID().toString();
  }
}
