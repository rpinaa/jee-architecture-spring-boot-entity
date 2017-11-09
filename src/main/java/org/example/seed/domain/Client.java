package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.seed.catalog.ClientStatus;
import org.example.seed.constraint.Denomination;
import org.example.seed.group.client.ClientCreateGroup;
import org.example.seed.group.client.ClientRegisterGroup;
import org.example.seed.group.client.ClientUpdateGroup;
import org.hibernate.validator.constraints.Email;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.UUID;

/**
 * Created by PINA on 26/06/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends Dates {

  @NotNull(groups = {ClientRegisterGroup.class, ClientUpdateGroup.class})
  @Size(min = 36, max = 36, groups = {ClientRegisterGroup.class, ClientUpdateGroup.class})
  private String id;

  @NotNull(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
  @Email(groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  @Size(max = 45, groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  private String email;

  @NotNull(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
  @Denomination(groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  @Size(min = 2, max = 80, groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  private String firstName;

  @Denomination(groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  @NotNull(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
  @Size(min = 2, max = 80, groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  private String lastName;

  @NotNull(groups = {ClientRegisterGroup.class})
  @Size(min = 8, max = 16, groups = {ClientRegisterGroup.class})
  @Null(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
  private String credential;

  @NotNull(groups = {ClientUpdateGroup.class})
  @Size(min = 2, max = 2, groups = {ClientUpdateGroup.class})
  @Null(groups = {ClientCreateGroup.class, ClientRegisterGroup.class})
  private String country;

  @Null(groups = {ClientCreateGroup.class})
  @Min(value = 0, groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  @Max(value = 5, groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  private Float rating;

  @NotNull(groups = {ClientRegisterGroup.class, ClientUpdateGroup.class})
  private ClientStatus status;

  @Valid
  @NotNull(groups = {ClientUpdateGroup.class})
  @Null(groups = {ClientCreateGroup.class, ClientRegisterGroup.class})
  private Telephone telephone;

  public Client() {
    this.id = UUID.randomUUID().toString();
  }
}
