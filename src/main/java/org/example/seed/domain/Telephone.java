package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.seed.catalog.TelephoneType;
import org.example.seed.group.chef.ChefUpdateGroup;
import org.example.seed.group.client.ClientUpdateGroup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Created by PINA on 26/06/2017.
 */
@Data
@EqualsAndHashCode
public class Telephone {

  @NotNull(groups = {ClientUpdateGroup.class, ChefUpdateGroup.class})
  @Size(min = 36, max = 36, groups = {ClientUpdateGroup.class, ChefUpdateGroup.class})
  private String id;

  @NotNull(groups = {ChefUpdateGroup.class})
  @Size(min = 2, max = 15, groups = {ChefUpdateGroup.class})
  private String name;

  @NotNull(groups = {ChefUpdateGroup.class})
  @Size(min = 5, max = 12, groups = {ChefUpdateGroup.class})
  private String number;

  private String lada;

  @NotNull(groups = {ChefUpdateGroup.class})
  private TelephoneType type;

  public Telephone() {
    this.id = UUID.randomUUID().toString();
  }
}
