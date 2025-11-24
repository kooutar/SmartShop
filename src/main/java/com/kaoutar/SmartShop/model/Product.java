package com.kaoutar.SmartShop.model;
import  jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private double prixUnitaire;
    private int stock;
    private boolean deleted = false;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;
}
