package org.example.seed.mapper;

import org.example.seed.domain.Chef;
import org.example.seed.entity.ChefEntity;
import org.example.seed.mapper.jpa.GenericMapper;
import org.mapstruct.Mapper;

/**
 * Created by PINA on 26/06/2017.
 */
@Mapper(componentModel = "spring")
public interface ChefMapper extends GenericMapper<Chef, ChefEntity> {

    Chef map(final ChefEntity chefEntity);

    ChefEntity map(final Chef chef);
}
