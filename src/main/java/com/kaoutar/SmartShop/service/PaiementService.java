package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.DTO.PaiementDTO;
import com.kaoutar.SmartShop.Mapper.PaiementMapper;
import com.kaoutar.SmartShop.enums.PaymentStatus;
import com.kaoutar.SmartShop.exception.BusinessException;
import com.kaoutar.SmartShop.model.Commande;
import com.kaoutar.SmartShop.model.Paiement;
import com.kaoutar.SmartShop.repositery.CommandeRepository;
import com.kaoutar.SmartShop.repositery.PaiementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class PaiementService {

     private  final CommandeRepository commandeRepository;
     private  final CommandeService commandeService;
     private  final PaiementRepository paymentRepository;
     private  final PaiementMapper paiementMapper;

    public PaiementDTO addPayement(Long orderId , PaiementDTO request){
        Commande Order = commandeRepository.findById(orderId).orElseThrow(()->new BusinessException("commande introuvable"));
        if (request.getTypePaiement().equals("CHÈQUE")) {
            request.setStatut("EN_ATTENTE");
        } else {
            request.setStatut("ENCAISSE");
            request.setDateEncaissement(LocalDate.now());
            commandeService.decrementRemainingAmountByPayment(orderId, request.getMontant());
        }
        Paiement payment = paiementMapper.toEntity(request);
        payment.setCommande(Order);
        return  paiementMapper.toDto(paymentRepository.save(payment));

    }
}
