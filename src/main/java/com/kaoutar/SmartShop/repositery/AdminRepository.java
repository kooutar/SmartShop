package com.kaoutar.SmartShop.repositery;

import com.kaoutar.SmartShop.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByUsername(String username);
}