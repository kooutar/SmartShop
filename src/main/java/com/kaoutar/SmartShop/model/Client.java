package com.kaoutar.SmartShop.model;
import com.kaoutar.SmartShop.enums.CustomerTier;
import com.kaoutar.SmartShop.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.*;
@Entity
@DiscriminatorValue("CLIENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    @Size(max = 100, message = "Email trop long")
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le niveau client (tier) est obligatoire")
    private CustomerTier tier;

    @Min(value = 0, message = "totalOrders ne peut pas être négatif")
    private int totalOrders;

    @Min(value = 0, message = "totalSpent ne peut pas être négatif")
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
