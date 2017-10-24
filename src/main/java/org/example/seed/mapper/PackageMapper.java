package org.example.seed.mapper;

import org.example.seed.domain.Package;
import org.example.seed.entity.PackageEntity;
import org.example.seed.mapper.jpa.GenericMapper;
import org.mapstruct.Mapper;

/**
 * Created by PINA on 02/07/2017.
 */
@Mapper(componentModel = "spring")
public interface PackageMapper extends GenericMapper<Package, PackageEntity> {

  Package map(final PackageEntity packageEntity);

  PackageEntity map(final Package sPackage);
}
