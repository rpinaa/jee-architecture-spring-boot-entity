package org.example.seed.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.seed.catalog.ChefStatus;
import org.example.seed.constraint.Curp;
import org.example.seed.constraint.Rfc;
import org.example.seed.group.chef.ChefUpdateGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * Created by PINA on 26/06/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Chef extends Dates {

    public Chef() {
        this.id = UUID.randomUUID().toString();
    }

    @Size(min = 36, max = 36)
    @NotNull(groups = {ChefUpdateGroup.class})
    private String id;

    @Rfc
    @NotNull(groups = {ChefUpdateGroup.class})
    private String rfc;

    @Curp
    @NotNull(groups = {ChefUpdateGroup.class})
    private String curp;

    @Min(value = 0)
    @Max(value = 5)
    @NotNull(groups = {ChefUpdateGroup.class})
    private Float rating;

    @NotNull(groups = {ChefUpdateGroup.class})
    private ChefStatus status;

    @Valid
    private Account account;

    @Valid
    @NotEmpty(groups = {ChefUpdateGroup.class})
    private List<Telephone> telephones;
}
