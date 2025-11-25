package com.kaoutar.SmartShop.Mapper;

import com.kaoutar.SmartShop.DTO.AdminDTO;
import com.kaoutar.SmartShop.DTO.ClientDTO;
import com.kaoutar.SmartShop.model.Admin;
import com.kaoutar.SmartShop.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
     AdminDTO toDto(Admin admin);
    Admin toEntity(AdminDTO dto);
}

