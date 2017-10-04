package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.seed.constraint.Denomination;
import org.example.seed.group.chef.ChefCreateGroup;
import org.example.seed.group.chef.ChefUpdateGroup;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Created by PINA on 26/06/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends Dates {

  private String id;

  @Denomination(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  @Size(min = 2, max = 80, groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  @NotNull(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  private String firstName;

  @Denomination(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  @Size(min = 2, max = 80, groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  @NotNull(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  private String lastName;

  @Email(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  @Size(max = 45, groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  @NotNull(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  private String email;

  public Account() {
    this.id = UUID.randomUUID().toString();
  }
}
