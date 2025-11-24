package com.kaoutar.SmartShop.Mapper;

import com.kaoutar.SmartShop.DTO.CommandeDTO;
import com.kaoutar.SmartShop.model.Commande;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaiementMapper.class})
public interface CommandeMapper {
    @Mapping(source = "client.id", target = "clientId")
    CommandeDTO toDto(Commande commande);

    @InheritInverseConfiguration
    Commande toEntity(CommandeDTO dto);
}
