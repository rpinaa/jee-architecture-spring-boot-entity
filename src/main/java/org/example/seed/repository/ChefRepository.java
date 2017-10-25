package org.example.seed.repository;

import org.example.seed.catalog.ChefStatus;
import org.example.seed.entity.ChefEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by PINA on 25/06/2017.
 */
@Repository
public interface ChefRepository extends JpaRepository<ChefEntity, String> {

  Optional<ChefEntity> findById(final String id);

  Optional<ChefEntity> findByIdAndStatus(final String id, final ChefStatus status);

  @Query("SELECT CASE WHEN COUNT(ce) > 0 THEN true ELSE false END FROM ChefEntity ce WHERE ce.account.email = ?1")
  boolean existsByEmail(final String email);

  @Query("SELECT ce FROM ChefEntity ce LEFT JOIN ce.account AS ae JOIN ce.dishes AS se WHERE ce.active = TRUE AND se.id = ?1")
  ChefEntity findOneByDish(final String idDish);
}
