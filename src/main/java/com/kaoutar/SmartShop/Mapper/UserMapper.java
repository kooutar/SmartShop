package com.kaoutar.SmartShop.Mapper;

import com.kaoutar.SmartShop.DTO.UserDTO;
import com.kaoutar.SmartShop.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    User toEntity(UserDTO dto);
}
