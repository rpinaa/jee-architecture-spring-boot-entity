package org.example.seed.repository;

import org.example.seed.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by PINA on 01/07/2017.
 */
@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, String> { }
