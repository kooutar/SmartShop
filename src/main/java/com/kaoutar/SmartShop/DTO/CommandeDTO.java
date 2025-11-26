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
    private Double sousTotalHT;
    private Double remise;
    private Double montantHT;
    private Double tva;
    private Double totalTTC;
    private String codePromo;
    private String statut;
    private Double montantRestant;
    private List<OrderItemDTO> items;
}
