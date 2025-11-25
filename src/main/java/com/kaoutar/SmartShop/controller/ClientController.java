package com.kaoutar.SmartShop.controller;

import com.kaoutar.SmartShop.DTO.ClientDTO;
import com.kaoutar.SmartShop.model.Client;
import com.kaoutar.SmartShop.service.ClientService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@AllArgsConstructor

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;




    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO request) {
        ClientDTO dto = clientService.createClient(request);


        return ResponseEntity.ok(dto);
    }
}
