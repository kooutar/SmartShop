package com.kaoutar.SmartShop.controller;

import com.kaoutar.SmartShop.DTO.PaiementDTO;
import com.kaoutar.SmartShop.service.CommandeService;
import com.kaoutar.SmartShop.service.PaiementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/paiement")
public class PaiementController {

    private final PaiementService paiementService;

    @PostMapping("/{orderId}")
    public ResponseEntity<PaiementDTO> addPaiment(@RequestBody PaiementDTO req, @PathVariable Long orderId){
        return ResponseEntity.ok(paiementService.addPayement(orderId,req));
    }

}
