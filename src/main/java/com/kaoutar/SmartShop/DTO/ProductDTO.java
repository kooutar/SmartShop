package com.kaoutar.SmartShop.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductDTO {
    private Long id;
    private String nom;
    private double prixUnitaire;
    private int stock;
}