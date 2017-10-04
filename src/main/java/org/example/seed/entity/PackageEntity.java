package org.example.seed.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by PINA on 01/07/2017.
 */
@Data
@Entity
@Table(name = "t_package")
@SQLDelete(sql = "UPDATE t_package SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted <> 1")
public class PackageEntity extends DatesEntity {

  @Id
  @Column(name = "id", length = 36, nullable = false, updatable = false)
  private String id;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_id_dish")
  private DishEntity dish;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_id_order")
  @Getter(AccessLevel.NONE)
  private OrderEntity order;

  @Column(name = "quantity")
  private Integer quantity;

  @Column(name = "price")
  private Float price;

  @Column(name = "deleted")
  private boolean deleted;
}
