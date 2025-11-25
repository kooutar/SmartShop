package com.kaoutar.SmartShop.controller;

import com.kaoutar.SmartShop.DTO.ClientDTO;

import com.kaoutar.SmartShop.service.ClientService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@AllArgsConstructor

@RestController

public class ClientController {
    private final ClientService clientService;

    @RequestMapping("/api/admin/clients")
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO request) {
        ClientDTO dto = clientService.createClient(request);
        return ResponseEntity.ok(dto);
    }


    @RequestMapping("/api/client/profile")
    @GetMapping
    public ResponseEntity<ClientDTO> getProfile(HttpSession session){
        ClientDTO dto =clientService.getProfile(session);
        return  ResponseEntity.ok(dto);
    }

    @RequestMapping("/api/client/profile/update")
    @PutMapping
    public ResponseEntity<ClientDTO> updateProfile(ClientDTO request, HttpSession session){
       ClientDTO dto = clientService.updateProfile(request,session);
       return  ResponseEntity.ok(dto);
    }


}
