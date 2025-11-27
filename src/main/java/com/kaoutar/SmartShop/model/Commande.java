package com.kaoutar.SmartShop.model;

import com.kaoutar.SmartShop.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull(message = "La date est obligatoire")
    private LocalDate date;

    @Min(value = 0, message = "Le sous-total ne peut pas être négatif")
    private double sousTotalHT;

    @Min(value = 0, message = "La remise ne peut pas être négative")
    private double remise;

    @Min(value = 0, message = "Le montant HT ne peut pas être négatif")
    private double montantHT;

    @Min(value = 0, message = "La TVA ne peut pas être négative")
    private double tva;

    @Min(value = 0, message = "Le total TTC ne peut pas être négatif")
    private double totalTTC;

    @Size(max = 20, message = "Code promo trop long")
    private String codePromo;

    @Enumerated(EnumType.STRING)
    private OrderStatus statut;

    @Min(value = 0, message = "Le montant restant ne peut pas être négatif")
    private double montantRestant;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<Paiement> paiements;
}

