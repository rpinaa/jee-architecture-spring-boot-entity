package org.example.seed.entity;

import lombok.*;
import org.example.seed.catalog.ChefStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * Created by PINA on 24/06/2017.
 */
@Data
@Entity
@Table(name = "chef")
@SQLDelete(sql = "UPDATE chef SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted <> 1")
@EqualsAndHashCode(callSuper = true)
public class ChefEntity extends DatesEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_id_account")
    private AccountEntity account;

    @OneToMany(mappedBy = "chef", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<TelephoneEntity> telephones;

    @OneToMany(mappedBy = "chef", cascade = {CascadeType.REMOVE})
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<DishEntity> dishes;

    @OneToMany(mappedBy = "chef", cascade = {CascadeType.REMOVE})
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<OrderEntity> orders;

    @Column(name = "curp", length = 18)
    private String curp;

    @Column(name = "rfc", length = 13)
    private String rfc;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private ChefStatus status;

    @Column(name = "active")
    private boolean active;

    @Column(name = "deleted")
    private boolean deleted;
}
