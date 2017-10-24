package org.example.seed.mapper;

import org.example.seed.domain.Telephone;
import org.example.seed.entity.TelephoneEntity;
import org.example.seed.mapper.jpa.GenericMapper;
import org.mapstruct.Mapper;

/**
 * Created by PINA on 26/06/2017.
 */
@Mapper(componentModel = "spring")
public interface TelephoneMapper extends GenericMapper<Telephone, TelephoneEntity> {

  Telephone map(final TelephoneEntity telephoneEntity);

  TelephoneEntity map(final Telephone telephone);
}
