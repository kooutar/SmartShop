package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.DTO.AdminDTO;
import com.kaoutar.SmartShop.Mapper.AdminMapper;
import com.kaoutar.SmartShop.enums.UserRole;
import com.kaoutar.SmartShop.model.Admin;
import com.kaoutar.SmartShop.repositery.AdminRepository;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private  final AdminMapper mapper;


    public AdminDTO createAdmin(AdminDTO request) {

        if (adminRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username existe déjà !");
        }

        Admin admin = mapper.toEntity(request);
        String hashPassword = BCrypt.hashpw(request.getPassword(),BCrypt.gensalt());
        admin.setPassword(hashPassword);
        admin.setRole(UserRole.ADMIN);

        return mapper.toDto(adminRepository.save(admin));
    }
}
