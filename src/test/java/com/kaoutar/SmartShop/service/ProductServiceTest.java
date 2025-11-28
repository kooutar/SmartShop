package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.DTO.ProductDTO;
import com.kaoutar.SmartShop.Mapper.ProductMapper;
import com.kaoutar.SmartShop.model.Product;
import com.kaoutar.SmartShop.repositery.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService productService;


    @Test
    void createProduct_ShouldCreateAndReturnProductDTO() {
        ProductDTO dto = new ProductDTO();
        dto.setNom("Casque");
        dto.setPrixUnitaire(150.0);

        Product entity = new Product();
        entity.setNom("Casque");
        entity.setPrixUnitaire(150.0);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ProductDTO result = productService.createProduct(dto);

        assertNotNull(result);
        assertEquals("Casque", result.getNom());
        assertEquals(150.0, result.getPrixUnitaire());
        verify(mapper).toEntity(dto);
        verify(productRepository).save(entity);
        verify(mapper).toDto(entity);
    }

    @Test
    void getActiveProducts_ShouldReturnOnlyNonDeletedProducts() {
        // Produits simulés
        Product product1 = new Product();
        product1.setId(1L);
        product1.setDeleted(false);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setDeleted(false);

        // Produit supprimé
        Product product3 = new Product();
        product3.setId(3L);
        product3.setDeleted(true);

        // Liste renvoyée par le repo
        List<Product> repoResult = List.of(product1, product2);

        when(productRepository.findByDeletedFalse()).thenReturn(repoResult);

        // Mapping DTO
        ProductDTO dto1 = new ProductDTO();
        dto1.setId(1L);
        ProductDTO dto2 = new ProductDTO();
        dto2.setId(2L);

        when(mapper.toDto(product1)).thenReturn(dto1);
        when(mapper.toDto(product2)).thenReturn(dto2);

        // Appel de la méthode
        List<ProductDTO> result = productService.getActiveProducts();

        // Vérifications
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
        verify(productRepository).findByDeletedFalse();
        verify(mapper).toDto(product1);
        verify(mapper).toDto(product2);
    }

    @Test
    void getActiveProducts_ShouldReturnEmptyList_WhenNoActiveProducts() {
        when(productRepository.findByDeletedFalse()).thenReturn(Collections.emptyList());

        List<ProductDTO> result = productService.getActiveProducts();

        assertTrue(result.isEmpty());
        verify(productRepository).findByDeletedFalse();
    }
    @Test
    void deleteProduct_ShouldMarkProductAsDeleted_WhenProductExists() {
        Product product = new Product();
        product.setId(1L);
        product.setDeleted(false);

        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        assertTrue(product.isDeleted());
        verify(productRepository).findByIdAndDeletedFalse(1L);
        verify(productRepository).save(product);
    }

    @Test
    void deleteProduct_ShouldThrowException_WhenProductDoesNotExist() {
        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Produit non trouvé", exception.getMessage());
        verify(productRepository).findByIdAndDeletedFalse(1L);
        verify(productRepository, never()).save(any(Product.class));
    }


}