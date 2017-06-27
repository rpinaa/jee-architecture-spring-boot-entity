package org.example.seed.repository;

import org.example.seed.entity.ChefEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.Future;

/**
 * Created by PINA on 25/06/2017.
 */
@Repository
public interface ChefRepository extends JpaRepository<ChefEntity, String> {

    @Async
    @Query("SELECT ce FROM ChefEntity ce JOIN ce.telephones")
    Future<Page<ChefEntity>> findAllWithTelephones(final Pageable pageable);
}
