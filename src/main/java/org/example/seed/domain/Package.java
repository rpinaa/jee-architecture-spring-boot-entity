package org.example.seed.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by PINA on 01/07/2017.
 */

@Getter
@Setter
public class Package extends Dates {

  private String id;
  private Dish dish;
  private Float price;
  private Integer quantity;

  public Package() {

    this.id = UUID.randomUUID().toString();
  }
}
