package com.kaoutar.SmartShop.Mapper;

import com.kaoutar.SmartShop.DTO.ClientDTO;
import com.kaoutar.SmartShop.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toDto(Client client);
    Client toEntity(ClientDTO dto);
}

