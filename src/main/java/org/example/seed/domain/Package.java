package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * Created by PINA on 01/07/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Package extends Dates {

  private String id;
  private Dish dish;
  private Float price;
  private Integer quantity;

  public Package() {
    this.id = UUID.randomUUID().toString();
  }
}
