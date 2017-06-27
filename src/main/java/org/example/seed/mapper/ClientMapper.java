package org.example.seed.mapper;

import org.example.seed.domain.Client;
import org.example.seed.entity.ClientEntity;
import org.example.seed.mapper.jpa.GenericMapper;
import org.mapstruct.Mapper;

/**
 * Created by PINA on 26/06/2017.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends GenericMapper<Client, ClientEntity> {

    Client map(final ClientEntity clientEntity);

    ClientEntity map(final Client client);
}
