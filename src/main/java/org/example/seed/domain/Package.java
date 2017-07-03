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

    public Package() {
        this.id = UUID.randomUUID().toString();
    }

    private String id;
    private Float price;
    private Integer quantity;
    private Dish dish;
}
