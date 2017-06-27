package org.example.seed.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.seed.catalog.ClientStatus;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by PINA on 28/05/2017.
 */
@Data
@Entity
@Table(name = "client")
@SQLDelete(sql = "UPDATE client SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted <> 1")
@EqualsAndHashCode(callSuper = true)
public class ClientEntity extends DatesEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_id_telephone")
    private TelephoneEntity telephone;

    @Column(name = "fist_name", length = 80)
    private String firstName;

    @Column(name = "last_name", length = 80)
    private String lastName;

    @Column(name = "email", length = 45)
    private String email;

    @Column(name = "rating")
    private Float rating;

    @Lob
    @Column(name = "secret")
    private byte[] secret;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private ClientStatus status;

    @Column(name = "deleted")
    private boolean deleted;
}
