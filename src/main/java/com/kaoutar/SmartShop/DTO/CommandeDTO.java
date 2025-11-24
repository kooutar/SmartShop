package com.kaoutar.SmartShop.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeDTO {
    private Long id;
    private Long clientId;
    private LocalDate date;
    private double sousTotalHT;
    private double remise;
    private double montantHT;
    private double tva;
    private double totalTTC;
    private String codePromo;
    private String statut;
    private double montantRestant;
    private List<OrderItemDTO> items;
}
