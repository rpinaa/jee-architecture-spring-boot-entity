package org.example.seed.repository;

import org.example.seed.catalog.ClientStatus;
import org.example.seed.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by PINA on 25/06/2017.
 */
@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, String> {

  @Query("SELECT CASE WHEN COUNT(ce) > 0 THEN true ELSE false END FROM ClientEntity ce WHERE ce.email = ?1")
  boolean existsByEmail(final String email);

  Optional<ClientEntity> findByIdAndStatus(final String id, final ClientStatus status);

  Optional<ClientEntity> findById(final String id);
}
