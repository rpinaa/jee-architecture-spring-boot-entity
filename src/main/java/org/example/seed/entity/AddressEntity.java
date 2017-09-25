package org.example.seed.entity;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by PINA on 30/06/2017.
 */
@Data
@Entity
@Table(name = "address")
@SQLDelete(sql = "UPDATE address SET deleted = 1 WHERE address_id = ?")
@Where(clause = "deleted <> 1")
public class AddressEntity extends DatesEntity {

    @Id
    @Column(name = "address_id", length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "int_number", length = 5)
    private String intNumber;

    @Column(name = "ext_number", length = 5)
    private String exNumber;

    @Column(name = "block", length = 5)
    private String block;

    @Column(name = "number", length = 5)
    private String number;

    @Column(name = "street", length = 15)
    private String street;

    @Column(name = "colony", length = 15)
    private String colony;

    @Column(name = "municipality", length = 20)
    private String municipality;

    @Column(name = "state", length = 15)
    private String state;

    @Column(name = "country", length = 10)
    private String country;

    @Column(name = "deleted")
    private boolean deleted;
}
