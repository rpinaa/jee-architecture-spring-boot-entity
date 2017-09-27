package org.example.seed.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by PINA on 25/05/2017.
 */
@Data
@Entity
@Table(name = "t_account")
@SQLDelete(sql = "UPDATE t_account SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted <> 1")
@EqualsAndHashCode(callSuper = true)
public class AccountEntity extends DatesEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "fist_name", length = 80)
    private String firstName;

    @Column(name = "last_name", length = 80)
    private String lastName;

    @Column(name = "email", length = 45)
    private String email;

    @Lob
    @Column(name = "secret")
    private byte[] secret;

    @Column(name = "deleted")
    private boolean deleted;
}
