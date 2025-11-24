package com.kaoutar.SmartShop.repositery;

import com.kaoutar.SmartShop.enums.OrderStatus;
import com.kaoutar.SmartShop.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClientId(Long clientId);

    @Query("SELECT c FROM Commande c WHERE c.statut = :statut")
    List<Commande> findByStatut(@Param("statut") OrderStatus statut);
}
