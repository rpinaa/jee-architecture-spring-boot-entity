package org.example.seed.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.example.seed.catalog.DishType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by PINA on 01/07/2017.
 */
@Data
@Entity
@Table(name = "t_dish")
@SQLDelete(sql = "UPDATE t_dish SET deleted = 1 WHERE id = ?")
@Where(clause = "deleted <> 1")
@EqualsAndHashCode(callSuper = true)
public class DishEntity extends DatesEntity {

  @Id
  @Column(name = "id", length = 36, nullable = false, updatable = false)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_id_chef")
  @Getter(AccessLevel.NONE)
  private ChefEntity chef;

  @Column(name = "name", length = 30)
  private String name;

  @Column(name = "description", length = 40)
  private String description;

  @Column(name = "uuid_image", length = 36)
  private String uuidImage;

  @Column(name = "path_image", length = 50)
  private String pathImage;

  @Column(name = "allergens", length = 20)
  private String allergens;

  @Column(name = "price")
  private Float price;

  @Column(name = "type", length = 20)
  @Enumerated(EnumType.STRING)
  private DishType type;

  @Column(name = "active")
  private boolean active;

  @Column(name = "deleted")
  private boolean deleted;
}
