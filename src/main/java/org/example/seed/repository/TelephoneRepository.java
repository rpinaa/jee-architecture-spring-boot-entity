package org.example.seed.repository;

import org.example.seed.entity.TelephoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by PINA on 25/06/2017.
 */
@Repository
public interface TelephoneRepository extends JpaRepository<TelephoneEntity, String> { }
