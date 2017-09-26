package org.example.seed.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.example.seed.catalog.TelephoneType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by PINA on 28/05/2017.
 */
@Data
@Entity
@Table(name = "telephone")
public class TelephoneEntity implements Serializable {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_chef")
    @Getter(AccessLevel.NONE)
    private ChefEntity chef;

    @Column(name = "name", length = 15)
    private String name;

    @Column(name = "lada", length = 5)
    private String lada;

    @Column(name = "number", length = 15)
    private String number;

    @Column(name = "type", length = 10)
    @Enumerated(EnumType.STRING)
    private TelephoneType type;
}
