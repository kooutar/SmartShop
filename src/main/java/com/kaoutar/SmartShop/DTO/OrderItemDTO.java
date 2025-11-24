package com.kaoutar.SmartShop.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private int quantite;
    private double prixUnitaire;
    private double totalLigne;
}