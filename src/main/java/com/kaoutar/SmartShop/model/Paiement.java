package com.kaoutar.SmartShop.model;

import com.kaoutar.SmartShop.enums.PaymentStatus;
import com.kaoutar.SmartShop.enums.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @Min(value = 1, message = "Le numéro de paiement doit être supérieur à 0")
    private Integer numeroPaiement;

    @Min(value = 0, message = "Le montant doit être positif")
    private Double montant;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de paiement est obligatoire")
    private PaymentType typePaiement; // ESPECES, CHEQUE, VIREMENT

    @NotNull(message = "La date de paiement est obligatoire")
    private LocalDate datePaiement;

    private LocalDate dateEncaissement;

    @Enumerated(EnumType.STRING)
    private PaymentStatus statut;

    @Size(max = 50, message = "Référence trop longue")
    private String reference;

    @Size(max = 50, message = "Nom de banque trop long")
    private String banque;

    private LocalDate echeance;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    @NotNull(message = "La commande associée est obligatoire")
    private Commande commande;
}

