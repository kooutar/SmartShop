package com.kaoutar.SmartShop.repositery;

import com.kaoutar.SmartShop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByDeletedFalse();

    @Query("SELECT p FROM Product p WHERE p.nom LIKE %:keyword%")
    List<Product> searchByName(@Param("keyword") String keyword);
}
