package org.example.seed.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.example.seed.catalog.OrderStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by PINA on 29/06/2017.
 */
@Data
@Entity
@Table(name = "order")
@SQLDelete(sql = "UPDATE order SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted <> 1")
@EqualsAndHashCode(callSuper = true)
public class OrderEntity extends DatesEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_id_address")
    private AddressEntity address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_chef")
    @Getter(value = AccessLevel.NONE)
    private ChefEntity chef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_client")
    @Getter(value = AccessLevel.NONE)
    private ClientEntity client;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = {CascadeType.ALL})
    @Fetch(FetchMode.SELECT)
    private List<PackageEntity> packages;

    @Column(name = "total")
    private Float total;

    @Column(name = "comment", length = 50)
    private String comment;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "time_zone")
    private TimeZone timeZone;

    @Column(name = "scheduled_date")
    private String scheduledDate;

    @Column(name = "registered_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredDate;

    @Column(name = "rejected_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rejectedDate;

    @Column(name = "finished_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedDate;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "deleted")
    private boolean deleted;
}
