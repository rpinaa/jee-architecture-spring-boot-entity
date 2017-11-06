package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.seed.constraint.Location;
import org.example.seed.group.address.AddressCreateGroup;
import org.example.seed.group.address.AddressUpdateGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Created by PINA on 30/06/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Address extends Dates {

  @NotEmpty(groups = {AddressUpdateGroup.class})
  @Size(min = 36, max = 36, groups = {AddressUpdateGroup.class})
  private String id;

  @Location(groups = {AddressCreateGroup.class})
  @NotEmpty(groups = {AddressCreateGroup.class})
  @Size(min = 1, max = 5, groups = {AddressCreateGroup.class})
  private String intNumber;

  @Location(groups = {AddressCreateGroup.class})
  @NotEmpty(groups = {AddressCreateGroup.class})
  @Size(min = 1, max = 5, groups = {AddressCreateGroup.class})
  private String exNumber;

  @Location(groups = {AddressCreateGroup.class})
  @NotEmpty(groups = {AddressCreateGroup.class})
  @Size(min = 1, max = 5, groups = {AddressCreateGroup.class})
  private String block;

  @Location(groups = {AddressCreateGroup.class})
  @NotEmpty(groups = {AddressCreateGroup.class})
  @Size(min = 1, max = 5, groups = {AddressCreateGroup.class})
  private String number;

  @Location(groups = {AddressCreateGroup.class})
  @NotEmpty(groups = {AddressCreateGroup.class})
  @Size(min = 3, max = 15, groups = {AddressCreateGroup.class})
  private String street;

  @Location(groups = {AddressCreateGroup.class})
  @NotEmpty(groups = {AddressCreateGroup.class})
  @Size(min = 3, max = 15, groups = {AddressCreateGroup.class})
  private String colony;

  @Location(groups = {AddressCreateGroup.class})
  @NotEmpty(groups = {AddressCreateGroup.class})
  @Size(min = 3, max = 20, groups = {AddressCreateGroup.class})
  private String municipality;

  @Location(groups = {AddressCreateGroup.class})
  @NotEmpty(groups = {AddressCreateGroup.class})
  @Size(min = 3, max = 15, groups = {AddressCreateGroup.class})
  private String state;

  @Location(groups = {AddressCreateGroup.class})
  @NotEmpty(groups = {AddressCreateGroup.class})
  @Size(min = 3, max = 10, groups = {AddressCreateGroup.class})
  private String country;

  public Address() {
    this.id = UUID.randomUUID().toString();
  }
}
