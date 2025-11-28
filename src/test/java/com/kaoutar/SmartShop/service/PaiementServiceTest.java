package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.DTO.PaiementDTO;
import com.kaoutar.SmartShop.Mapper.PaiementMapper;
import com.kaoutar.SmartShop.exception.BusinessException;
import com.kaoutar.SmartShop.model.Commande;
import com.kaoutar.SmartShop.model.Paiement;
import com.kaoutar.SmartShop.repositery.CommandeRepository;
import com.kaoutar.SmartShop.repositery.PaiementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaiementServiceTest {

    @Mock
    private CommandeRepository commandeRepository;

    @Mock
    private PaiementRepository paymentRepository;

    @Mock
    private PaiementMapper paiementMapper;

    @Mock
    private CommandeService commandeService;

    @InjectMocks
    private PaiementService paiementService;

    @Test
    void addPayement_ShouldThrow_WhenOrderNotFound() {
        PaiementDTO dto = new PaiementDTO();
        dto.setTypePaiement("ESPECE");
        dto.setMontant(100.0);

        when(commandeRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            paiementService.addPayement(1L, dto);
        });

        assertEquals("commande introuvable", ex.getMessage());
    }
    @Test
    void addPayement_ShouldSetEnAttente_WhenCheque() {
        Commande order = new Commande();
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(order));

        PaiementDTO dto = new PaiementDTO();
        dto.setTypePaiement("CHÈQUE");

        Paiement entity = new Paiement();
        when(paiementMapper.toEntity(dto)).thenReturn(entity);
        when(paymentRepository.save(entity)).thenReturn(entity);
        when(paiementMapper.toDto(entity)).thenReturn(dto);

        PaiementDTO result = paiementService.addPayement(1L, dto);

        assertEquals("EN_ATTENTE", result.getStatut());
        verify(commandeService, never()).decrementRemainingAmountByPayment(anyLong(), anyDouble());
    }
    @Test
    void addPayement_ShouldSetEncaisse_WhenNotCheque() {
        Commande order = new Commande();
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(order));

        PaiementDTO dto = new PaiementDTO();
        dto.setTypePaiement("ESPECE");
        dto.setMontant(200.0);

        Paiement entity = new Paiement();
        when(paiementMapper.toEntity(dto)).thenReturn(entity);
        when(paymentRepository.save(entity)).thenReturn(entity);
        when(paiementMapper.toDto(entity)).thenReturn(dto);

        PaiementDTO result = paiementService.addPayement(1L, dto);

        assertEquals("ENCAISSE", result.getStatut());
        assertNotNull(result.getDateEncaissement());
        verify(commandeService, times(1)).decrementRemainingAmountByPayment(1L, 200.0);
    }

    

}
