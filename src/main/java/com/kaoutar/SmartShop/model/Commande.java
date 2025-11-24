package com.kaoutar.SmartShop.model;

import com.kaoutar.SmartShop.enums.OrderStatus;
import jakarta.persistence.*;
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

    private LocalDate date;

    private double sousTotalHT;
    private double remise;
    private double montantHT;
    private double tva;
    private double totalTTC;
    private String codePromo;

    @Enumerated(EnumType.STRING)
    private OrderStatus statut;

    private double montantRestant;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<Paiement> paiements;
}

