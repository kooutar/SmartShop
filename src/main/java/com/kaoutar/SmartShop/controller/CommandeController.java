package com.kaoutar.SmartShop.controller;

import com.kaoutar.SmartShop.DTO.CommandeDTO;
import com.kaoutar.SmartShop.service.CommandeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/commande")
@AllArgsConstructor
public class CommandeController {
    private  final CommandeService commandeService;
    @PostMapping
    public ResponseEntity<CommandeDTO> createCommande( @RequestBody CommandeDTO commandeDTO) {
         CommandeDTO commande= commandeService.createCommande(commandeDTO);
         return  ResponseEntity.ok(commande);
    }

}
