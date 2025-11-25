package com.kaoutar.SmartShop.controller;

import com.kaoutar.SmartShop.DTO.AdminDTO;
import com.kaoutar.SmartShop.model.Admin;
import com.kaoutar.SmartShop.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody AdminDTO request) {
        AdminDTO admin = adminService.createAdmin(request);



        return ResponseEntity.ok(admin);
    }
}
