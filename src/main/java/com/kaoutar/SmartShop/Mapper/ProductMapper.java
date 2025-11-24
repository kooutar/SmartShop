package com.kaoutar.SmartShop.Mapper;

import com.kaoutar.SmartShop.DTO.ProductDTO;
import com.kaoutar.SmartShop.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product product);
    Product toEntity(ProductDTO dto);
}
