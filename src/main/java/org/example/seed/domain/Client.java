package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.seed.catalog.ClientStatus;
import org.example.seed.constraint.Denomination;
import org.example.seed.group.chef.ChefCreateGroup;
import org.example.seed.group.chef.ChefUpdateGroup;
import org.example.seed.group.client.ClientCreateGroup;
import org.example.seed.group.client.ClientUpdateGroup;
import org.hibernate.validator.constraints.Email;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Created by PINA on 26/06/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends Dates {

    public Client() {
        this.id = UUID.randomUUID().toString();
    }

    @Size(min = 36, max = 36, groups = {ClientUpdateGroup.class})
    @NotNull(groups = {ClientUpdateGroup.class})
    private String id;

    @Email(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
    @Size(max = 45, groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
    @NotNull(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
    private String email;

    @Denomination(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
    @Size(min = 2, max = 80, groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
    @NotNull(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
    private String firstName;

    @Denomination(groups = {ChefCreateGroup.class, ChefUpdateGroup.class})
    @Size(min = 2, max = 80, groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
    @NotNull(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
    private String lastName;

    @Min(value = 0, groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
    @Max(value = 5, groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
    @NotNull(groups = {ClientCreateGroup.class, ClientUpdateGroup.class})
    private Float rating;

    @NotNull(groups = {ClientUpdateGroup.class})
    private ClientStatus status;

    @Valid
    private Telephone telephone;
}
