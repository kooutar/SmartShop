package com.kaoutar.SmartShop.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private Integer quantite;
    private Double prixUnitaire;
    private Double totalLigne;
}