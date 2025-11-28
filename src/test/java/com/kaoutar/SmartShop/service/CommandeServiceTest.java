package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.DTO.CommandeDTO;
import com.kaoutar.SmartShop.DTO.OrderItemDTO;
import com.kaoutar.SmartShop.Mapper.CommandeMapper;
import com.kaoutar.SmartShop.Mapper.OrderItemMapper;
import com.kaoutar.SmartShop.enums.CustomerTier;
import com.kaoutar.SmartShop.enums.OrderStatus;
import com.kaoutar.SmartShop.exception.BusinessException;
import com.kaoutar.SmartShop.model.Client;
import com.kaoutar.SmartShop.model.Commande;
import com.kaoutar.SmartShop.model.OrderItem;
import com.kaoutar.SmartShop.model.Product;
import com.kaoutar.SmartShop.repositery.ClientRepository;
import com.kaoutar.SmartShop.repositery.CommandeRepository;
import com.kaoutar.SmartShop.repositery.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandeServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CommandeRepository commandeRepository;

    @Mock
    private CommandeMapper mapperCommande;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private CommandeService commandeService;
    @Mock
    private ProductService productService;

    @Mock
    private ClientService clientService;




    @Test
    void createCommande_ShouldReturnCommandeDTO_WhenEverythingIsOk() {
        // Client
        Client client = new Client();
        client.setId(1L);
        client.setTotalOrders(5);
        client.setTotalSpent(500.0);
        client.setTier(CustomerTier.BASIC); // <-- Ajouter ceci !

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // Produit
        Product product = new Product();
        product.setId(1L);
        product.setNom("Casque");
        product.setPrixUnitaire(100.0);
        product.setStock(10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Items
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(1L);
        itemDTO.setQuantite(2);

        CommandeDTO request = new CommandeDTO();
        request.setClientId(1L);
        request.setItems(List.of(itemDTO));
        request.setCodePromo("PROMO-ABCD");

        // Mapping
        Commande commandeEntity = new Commande();
        when(mapperCommande.toEntity(request)).thenReturn(commandeEntity);
        when(orderItemMapper.toEntity(itemDTO)).thenAnswer(inv -> {
            OrderItem entity = new OrderItem();
            entity.setQuantite(itemDTO.getQuantite());
            return entity;
        });

        CommandeDTO resultDTO = new CommandeDTO();
        when(mapperCommande.toDto(commandeEntity)).thenReturn(resultDTO);

        // Appel
        CommandeDTO result = commandeService.createCommande(request);

        // Vérifications
        assertNotNull(result);
        assertEquals(1, commandeEntity.getItems().size());
        assertEquals(OrderStatus.PENDING, commandeEntity.getStatut());
        verify(commandeRepository).save(commandeEntity);
    }

    @Test
    void confirmOrder_ShouldThrowException_WhenOrderNotFound() {
        when(commandeRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            commandeService.confirmOrder(1L);
        });

        assertEquals("commande introuvable", ex.getMessage());
        verify(commandeRepository).findById(1L);
        verifyNoMoreInteractions(commandeRepository, productService, clientService);
    }
    @Test
    void confirmOrder_ShouldConfirmOrder_WhenPaymentComplete() {
        Client client = new Client();
        client.setTotalOrders(1);
        client.setTotalSpent(100.0);

        Product product = new Product();
        product.setId(1L);
        product.setStock(10);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantite(2);

        Commande order = new Commande();
        order.setId(1L);
        order.setMontantRestant(0.0);
        order.setMontantHT(200.0);
        order.setClient(client);
        order.setItems(List.of(item));

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(order));

        String result = commandeService.confirmOrder(1L);

        assertEquals("commande confirmée ", result);
        assertEquals(OrderStatus.CONFIRMED, order.getStatut());
        assertEquals(2, client.getTotalOrders());
        assertEquals(300.0, client.getTotalSpent());

        verify(commandeRepository).findById(1L);
        verify(commandeRepository).save(order);
        verify(productService).decreaseStockAfterOrder(product, 2);
        verify(clientService).updateFidelite(client);
    }
    @Test
    void confirmOrder_ShouldReturnPaymentIncomplete_WhenMontantRestantNotZero() {
        Commande order = new Commande();
        order.setId(1L);
        order.setMontantRestant(50.0);

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(order));

        String result = commandeService.confirmOrder(1L);

        assertEquals("le paiement n'est pas complet ", result);
        verify(commandeRepository).findById(1L);
        verifyNoMoreInteractions(commandeRepository, productService, clientService);
    }

    @Test
    void updateCommande_ShouldThrow_WhenOrderNotFound() {
        when(commandeRepository.findById(1L)).thenReturn(Optional.empty());

        CommandeDTO req = new CommandeDTO();
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            commandeService.updateCommande(1L, req);
        });

        assertEquals("Commande introuvable", ex.getMessage());
    }
    @Test
    void updateCommande_ShouldThrow_WhenStatusNotPending() {
        Commande order = new Commande();
        order.setStatut(OrderStatus.CONFIRMED);
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(order));

        CommandeDTO req = new CommandeDTO();
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            commandeService.updateCommande(1L, req);
        });

        assertTrue(ex.getMessage().contains("Impossible de modifier une commande dont le statut est"));
    }
    @Test
    void updateCommande_ShouldThrow_WhenClientNotFound() {
        Commande order = new Commande();
        order.setStatut(OrderStatus.PENDING);
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(order));
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        CommandeDTO req = new CommandeDTO();
        req.setClientId(1L);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            commandeService.updateCommande(1L, req);
        });

        assertEquals("Client introuvable", ex.getMessage());
    }
    @Test
    void updateCommande_ShouldThrow_WhenStockInsufficient() {
        Commande order = new Commande();
        order.setStatut(OrderStatus.PENDING);

        // IMPORTANT : éviter NullPointerException
        Client client = new Client();
        client.setTier(CustomerTier.BASIC); // si calculateLoyaltyDiscount utilise tier
        order.setClient(client);

        order.setItems(new ArrayList<>());
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(order));

        Product product = new Product();
        product.setId(1L);
        product.setNom("Casque");
        product.setStock(1); // stock trop faible
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(1L);
        itemDTO.setQuantite(2);

        CommandeDTO req = new CommandeDTO();
        req.setItems(List.of(itemDTO));

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            commandeService.updateCommande(1L, req);
        });

        assertTrue(ex.getMessage().contains("Stock insuffisant"));
    }


    @Test
    void deleteCommande_ShouldThrow_WhenOrderNotFound() {
        when(commandeRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            commandeService.deleteCommande(1L);
        });

        assertEquals("Commande introuvable", ex.getMessage());
    }
    @Test
    void deleteCommande_ShouldThrow_WhenStatusNotPending() {
        Commande order = new Commande();
        order.setStatut(OrderStatus.CONFIRMED);
        order.setItems(new ArrayList<>()); // éviter NPE
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(order));

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            commandeService.deleteCommande(1L);
        });

        assertTrue(ex.getMessage().contains("Impossible de supprimer une commande dont le statut est"));
    }
    @Test
    void deleteCommande_ShouldDeleteSuccessfully_WhenPending() {
        Commande order = new Commande();
        order.setStatut(OrderStatus.PENDING);
        order.setItems(new ArrayList<>()); // items existants
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(order));

        String result = commandeService.deleteCommande(1L);

        // Vérifier la suppression
        verify(commandeRepository, times(1)).delete(order);
        assertEquals("Commande supprimée avec succès", result);
        assertTrue(order.getItems().isEmpty()); // items effacés
    }






}