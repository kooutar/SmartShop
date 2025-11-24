package com.kaoutar.SmartShop.model;
import com.kaoutar.SmartShop.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Client extends User {

    private String nom;
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerTier tier;

    private int totalOrders;
    private double totalSpent;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Commande> commandes;
}