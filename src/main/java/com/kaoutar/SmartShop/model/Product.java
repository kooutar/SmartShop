package com.kaoutar.SmartShop.model;
import  jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 100, message = "Le nom du produit ne peut pas dépasser 100 caractères")
    private String nom;

    @Min(value = 0, message = "Le prix unitaire ne peut pas être négatif")
    private double prixUnitaire;

    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private int stock;

    private boolean deleted = false;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;
}
