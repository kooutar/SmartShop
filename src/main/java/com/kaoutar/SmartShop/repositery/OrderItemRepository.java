package com.kaoutar.SmartShop.repositery;


import com.kaoutar.SmartShop.enums.OrderStatus;
import com.kaoutar.SmartShop.model.Commande;
import com.kaoutar.SmartShop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByCommandeId(Long commandeId);
}
