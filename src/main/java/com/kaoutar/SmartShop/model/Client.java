package com.kaoutar.SmartShop.model;
import com.kaoutar.SmartShop.enums.CustomerTier;
import com.kaoutar.SmartShop.enums.UserRole;
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

    @Builder
    public Client(Long id, String username, String password, UserRole role,
                  String nom, String email, CustomerTier tier,
                  int totalOrders, double totalSpent, List<Commande> commandes) {

        super(id, username, password, role);

        this.nom = nom;
        this.email = email;
        this.tier = tier;
        this.totalOrders = totalOrders;
        this.totalSpent = totalSpent;
        this.commandes = commandes;
    }
}
