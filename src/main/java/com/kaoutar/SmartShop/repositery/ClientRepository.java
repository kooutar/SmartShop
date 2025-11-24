package com.kaoutar.SmartShop.repositery;

import com.kaoutar.SmartShop.enums.CustomerTier;
import com.kaoutar.SmartShop.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    @Query("SELECT c FROM Client c WHERE c.tier = :tier")
    List<Client> findByTier(@Param("tier") CustomerTier tier);
}
