package org.example.seed.domain;

import lombok.Getter;
import lombok.Setter;
import org.example.seed.constraint.Denomination;
import org.example.seed.group.chef.ChefCreateGroup;
import org.example.seed.group.chef.ChefRegisterGroup;
import org.example.seed.group.chef.ChefUpdateGroup;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Created by PINA on 26/06/2017.
 */

@Setter
@Getter
public class Account {

  private String id;

  @NotNull(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  @Denomination(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  @Size(min = 2, max = 80, groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  private String firstName;

  @NotNull(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  @Denomination(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  @Size(min = 2, max = 80, groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  private String lastName;

  @Email(groups = {ChefCreateGroup.class})
  @NotNull(groups = {ChefCreateGroup.class})
  @Size(max = 45, groups = {ChefCreateGroup.class})
  private String email;

  @NotNull(groups = {ChefRegisterGroup.class})
  @Size(min = 8, max = 16, groups = {ChefRegisterGroup.class})
  @Null(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
  private String credential;

  public Account() {

    this.id = UUID.randomUUID().toString();
  }
}
