package com.kaoutar.SmartShop.DTO;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaiementDTO {
    private Long id;
    private Integer numeroPaiement;
    private Double montant;
    private String typePaiement;
    private LocalDate datePaiement;
    private LocalDate dateEncaissement;
    private String statut;
    private String reference;
    private String banque;
    private LocalDate echeance;
}

