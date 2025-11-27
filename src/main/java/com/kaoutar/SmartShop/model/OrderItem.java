package com.kaoutar.SmartShop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "La quantité doit être au moins de 1")
    private int quantite;

    @Min(value = 0, message = "Le prix unitaire ne peut pas être négatif")
    private double prixUnitaire;

    @Min(value = 0, message = "Le total de la ligne ne peut pas être négatif")
    private double totalLigne;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    @NotNull(message = "La commande est obligatoire")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull(message = "Le produit est obligatoire")
    private Product product;
}
