package com.kaoutar.SmartShop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private String username;
    private String nom;
    private String email;
    private String tier;
    private int totalOrders;
    private double totalSpent;

    private List<CommandeDTO> commandes;
}

