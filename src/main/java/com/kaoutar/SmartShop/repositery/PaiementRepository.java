package com.kaoutar.SmartShop.repositery;

import com.kaoutar.SmartShop.enums.PaymentStatus;
import com.kaoutar.SmartShop.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByCommandeId(Long commandeId);

    @Query("SELECT p FROM Paiement p WHERE p.statut = :statut")
    List<Paiement> findByStatut(@Param("statut") PaymentStatus statut);
}
