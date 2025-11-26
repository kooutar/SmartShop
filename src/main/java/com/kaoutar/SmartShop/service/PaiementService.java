package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.DTO.PaiementDTO;
import com.kaoutar.SmartShop.Mapper.PaiementMapper;
import com.kaoutar.SmartShop.exception.BusinessException;
import com.kaoutar.SmartShop.model.Commande;
import com.kaoutar.SmartShop.model.Paiement;
import com.kaoutar.SmartShop.repositery.CommandeRepository;
import com.kaoutar.SmartShop.repositery.PaiementRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class PaiementService {

     private  final CommandeRepository commandeRepository;
     private  final PaiementRepository paymentRepository;
     private  final PaiementMapper paiementMapper;

    public PaiementDTO addPayement(Long orderId , PaiementDTO request){
        Commande Order = commandeRepository.findById(orderId).orElseThrow(()->new BusinessException("commande introuvable"));
        Paiement payment = paiementMapper.toEntity(request);
        return  paiementMapper.toDto(paymentRepository.save(payment));

    }
}
