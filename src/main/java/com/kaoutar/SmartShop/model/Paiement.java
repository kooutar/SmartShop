package com.kaoutar.SmartShop.model;

import com.kaoutar.SmartShop.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numeroPaiement;
    private double montant;

    @Enumerated(EnumType.STRING)
    private PaymentType typePaiement; // ESPECES, CHEQUE, VIREMENT

    private LocalDate datePaiement;
    private LocalDate dateEncaissement;

    @Enumerated(EnumType.STRING)
    private PaymentStatus statut;

    private String reference;
    private String banque;
    private LocalDate echeance;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;
}

