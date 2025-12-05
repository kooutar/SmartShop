package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.DTO.ProductDTO;
import com.kaoutar.SmartShop.Mapper.ProductMapper;
import com.kaoutar.SmartShop.model.Product;
import com.kaoutar.SmartShop.repositery.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private  final ProductMapper mapper;

    public ProductDTO createProduct(ProductDTO dto) {
        Product product = mapper.toEntity(dto);
        Product savedProduct = productRepository.save(product);
        return mapper.toDto(savedProduct);
    }

    public List<ProductDTO> getActiveProducts() {
        return productRepository.findByDeletedFalse()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        return mapper.toDto(product);
    }

    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if(dto.getNom() != null) product.setNom(dto.getNom());
        if(dto.getPrixUnitaire() != 0) product.setPrixUnitaire(dto.getPrixUnitaire());
        if(dto.getStock() != 0) product.setStock(dto.getStock());

        Product updatedProduct = productRepository.save(product);
        return mapper.toDto(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        product.setDeleted(true);
        productRepository.save(product);
    }

    public void decreaseStockAfterOrder(Product product, int quantityOrdered) {
        product.setStock(product.getStock()-quantityOrdered);
        productRepository.save(product);
    }





}
