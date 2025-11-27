package com.kaoutar.SmartShop.controller;

import com.kaoutar.SmartShop.DTO.CommandeDTO;
import com.kaoutar.SmartShop.service.CommandeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{orderId}")
    public ResponseEntity<String> confirmOrder(@PathVariable Long orderId) {
         String message=commandeService.confirmOrder(orderId);
        return  ResponseEntity.ok(message);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCommande(
            @PathVariable Long id,
            @RequestBody CommandeDTO request) {

        String result = commandeService.updateCommande(id, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommande(@PathVariable Long id) {
        String result = commandeService.deleteCommande(id);
        return ResponseEntity.ok(result);
    }
}
