package org.example.seed.repository;

import org.example.seed.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by PINA on 01/07/2017.
 */
@Repository
public interface DishRepository extends JpaRepository<DishEntity, String> {

  @Query("SELECT de FROM DishEntity de JOIN de.chef AS ce WHERE ce.active = TRUE AND ce.id = ?1")
  Optional<List<DishEntity>> findAllByChef(final String idChef);
}
