package org.example.seed.domain;

import lombok.Getter;
import lombok.Setter;
import org.example.seed.catalog.ChefStatus;
import org.example.seed.constraint.Curp;
import org.example.seed.constraint.One;
import org.example.seed.constraint.Rfc;
import org.example.seed.group.chef.ChefCreateGroup;
import org.example.seed.group.chef.ChefRegisterGroup;
import org.example.seed.group.chef.ChefUpdateGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

/**
 * Created by PINA on 26/06/2017.
 */

@Getter
@Setter
public class Chef extends Dates {

  @NotNull(groups = {ChefRegisterGroup.class, ChefUpdateGroup.class})
  @Size(min = 36, max = 36, groups = {ChefRegisterGroup.class, ChefUpdateGroup.class})
  private String id;

  @Null(groups = {ChefCreateGroup.class})
  @Rfc(groups = {ChefRegisterGroup.class, ChefUpdateGroup.class})
  @NotNull(groups = {ChefRegisterGroup.class, ChefUpdateGroup.class})
  @Size(min = 13, max = 13, groups = {ChefRegisterGroup.class, ChefUpdateGroup.class})
  private String rfc;

  @Null(groups = {ChefCreateGroup.class})
  @Curp(groups = {ChefRegisterGroup.class, ChefUpdateGroup.class})
  @NotNull(groups = {ChefRegisterGroup.class, ChefUpdateGroup.class})
  @Size(min = 18, max = 18, groups = {ChefRegisterGroup.class, ChefUpdateGroup.class})
  private String curp;

  @NotNull(groups = {ChefUpdateGroup.class})
  @Size(min = 2, max = 2, groups = {ChefUpdateGroup.class})
  @Null(groups = {ChefCreateGroup.class, ChefRegisterGroup.class})
  private String country;

  @Null(groups = {ChefCreateGroup.class, ChefRegisterGroup.class})
  @Min(value = 0, groups = {ChefCreateGroup.class, ChefRegisterGroup.class, ChefUpdateGroup.class})
  @Max(value = 5, groups = {ChefCreateGroup.class, ChefRegisterGroup.class, ChefUpdateGroup.class})
  private Float rating;

  @NotNull(groups = {ChefRegisterGroup.class, ChefUpdateGroup.class})
  private ChefStatus status;

  private boolean active;

  @Valid
  @NotNull(groups = {ChefCreateGroup.class, ChefRegisterGroup.class, ChefUpdateGroup.class})
  private Account account;

  @Valid
  @Null(groups = {ChefCreateGroup.class})
  @One(groups = {ChefRegisterGroup.class})
  @NotEmpty(groups = {ChefUpdateGroup.class})
  private List<Telephone> telephones;

  public Chef() {

    this.id = UUID.randomUUID().toString();
  }
}
