package com.kaoutar.SmartShop.Mapper;

import com.kaoutar.SmartShop.DTO.PaiementDTO;
import com.kaoutar.SmartShop.model.Paiement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaiementMapper {
    PaiementDTO toDto(Paiement paiement);
    Paiement toEntity(PaiementDTO dto);
}