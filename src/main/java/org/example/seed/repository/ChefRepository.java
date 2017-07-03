package org.example.seed.repository;

import org.example.seed.entity.ChefEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by PINA on 25/06/2017.
 */
@Repository
public interface ChefRepository extends JpaRepository<ChefEntity, String> {

    @Query("SELECT ce FROM ChefEntity ce LEFT JOIN ce.account AS ae JOIN ce.dishes AS se WHERE ce.active = TRUE AND se.id = ?1")
    ChefEntity findOneByDish(final String idDish);
}
