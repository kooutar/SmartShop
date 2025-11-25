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
    private Integer totalOrders;
    private Double totalSpent;
    private  String password;

    private List<CommandeDTO> commandes;
}

