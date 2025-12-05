package com.kaoutar.SmartShop.controller;

import com.kaoutar.SmartShop.DTO.ClientDTO;

import com.kaoutar.SmartShop.service.ClientService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
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

    @RequestMapping("/api/admin/profile/update/{ClientId}")
    @PutMapping
    public ResponseEntity<ClientDTO> updateProfile( @RequestBody ClientDTO request,    @PathVariable Long ClientId){
       ClientDTO dto = clientService.updateProfile(request,ClientId);
       return  ResponseEntity.ok(dto);
    }

    @DeleteMapping("/api/admin/profile/delete/{clientId}")
    public ResponseEntity<String> deleteClient(@PathVariable Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok("client est supprimée ");
    }

    @GetMapping("/api/admin/getAllCleint")
    public Page<ClientDTO> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {
        return clientService.getAllClients(page, size);
    }


}
