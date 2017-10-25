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

  @Size(min = 36, max = 36, groups = {ClientRegisterGroup.class, ClientUpdateGroup.class})
  @NotNull(groups = {ClientRegisterGroup.class, ClientUpdateGroup.class})
  private String id;

  @Email(groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  @Size(max = 45, groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  @NotNull(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
  private String email;

  @Denomination(groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  @Size(min = 2, max = 80, groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  @NotNull(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
  private String firstName;

  @Denomination(groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  @Size(min = 2, max = 80, groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  @NotNull(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
  private String lastName;

  @Null(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
  @NotNull(groups = {ClientRegisterGroup.class})
  @Size(min = 8, max = 16, groups = {ClientRegisterGroup.class})
  private String credential;

  @Null(groups = {ClientCreateGroup.class})
  @Min(value = 0, groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  @Max(value = 5, groups = {ClientCreateGroup.class, ClientRegisterGroup.class, ClientUpdateGroup.class})
  private Float rating;

  @NotNull(groups = {ClientRegisterGroup.class, ClientUpdateGroup.class})
  private ClientStatus status;

  @Valid
  private Telephone telephone;

  public Client() {
    this.id = UUID.randomUUID().toString();
  }
}
