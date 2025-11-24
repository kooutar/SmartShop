package com.kaoutar.SmartShop.Mapper;

import com.kaoutar.SmartShop.DTO.OrderItemDTO;
import com.kaoutar.SmartShop.model.OrderItem;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.nom", target = "productName")
    OrderItemDTO toDto(OrderItem orderItem);

    @InheritInverseConfiguration
    OrderItem toEntity(OrderItemDTO dto);
}
