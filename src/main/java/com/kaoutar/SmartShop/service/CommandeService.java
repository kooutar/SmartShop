package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.DTO.CommandeDTO;
import com.kaoutar.SmartShop.DTO.OrderItemDTO;
import com.kaoutar.SmartShop.Mapper.CommandeMapper;
import com.kaoutar.SmartShop.Mapper.OrderItemMapper;
import com.kaoutar.SmartShop.Mapper.PaiementMapper;
import com.kaoutar.SmartShop.enums.OrderStatus;
import com.kaoutar.SmartShop.exception.BusinessException;
import com.kaoutar.SmartShop.model.*;
import com.kaoutar.SmartShop.repositery.ClientRepository;
import com.kaoutar.SmartShop.repositery.CommandeRepository;
import com.kaoutar.SmartShop.repositery.PaiementRepository;
import com.kaoutar.SmartShop.repositery.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

@AllArgsConstructor
@Service
@Transactional
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final CommandeMapper mapperCommande;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderItemMapper orderItemMapper;
    private  final  ClientService  clientService;
    private  final  ProductService productService;

    public CommandeDTO createCommande(CommandeDTO request) {
        Client client = clientRepository.findById(request.getClientId()).orElseThrow(() -> new BusinessException("Client introuvable"));

        Commande commande = mapperCommande.toEntity(request);
        commande.setDate(LocalDate.now());
        commande.setStatut(OrderStatus.PENDING);
        commande.setItems(new ArrayList<>());
        double subTotal = 0.0;
        for (OrderItemDTO itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId()).orElseThrow(() -> new BusinessException("Produit introuvable "));

            if (product.getStock() < itemReq.getQuantite()) {
                commande.setStatut(OrderStatus.REJECTED);
                throw new BusinessException("Stock insuffisant pour" + product.getNom());
            }

            double totalLigne = product.getPrixUnitaire() * itemReq.getQuantite();
            itemReq.setTotalLigne(totalLigne);
            OrderItem orderItemEntity = orderItemMapper.toEntity(itemReq);
            orderItemEntity.setCommande(commande);
            commande.getItems().add(orderItemEntity);
            subTotal += totalLigne;
        }

        double discount = calculateLoyaltyDiscount(client, subTotal);
        if (request.getCodePromo() != null && request.getCodePromo().matches("PROMO-[A-Z0-9]{4}")) {
            discount += subTotal * 0.5;
        }

        double htAfterDiscount = subTotal - discount;
        double tva = htAfterDiscount * 0.20;
        double totalTTC = htAfterDiscount + tva;

        commande.setTotalTTC(totalTTC);
        commande.setRemise(discount);
        commande.setTva(tva);
        commande.setMontantHT(htAfterDiscount);
        commande.setSousTotalHT(subTotal);
        commande.setMontantRestant(htAfterDiscount);

        commandeRepository.save(commande);

        return mapperCommande.toDto(commande);


    }

    /*Application des remises selon le niveau actue
     * SILVER : 5% si sous-total ≥ 500
     * GOLD : 10% si sous-total ≥ 800
     * PLATINUM : 15% si sous-total ≥ 1200
     */

    private double calculateLoyaltyDiscount(Client client, double subTotal) {
        switch (client.getTier()) {
            case SILVER -> {
                return subTotal > 500 ? subTotal * 0.05 : 0.0;
            }
            case GOLD -> {
                return subTotal > 800 ? 0.10 : 0.0;
            }
            case PLATINUM -> {
                return subTotal >= 1200 ? subTotal * 0.15 : 0.0;

            }
            default -> {
                return 0.0;
            }
        }
    }

    // decrementer le Mountant_Restant apres le paiement
    public void decrementRemainingAmountByPayment(long orderId, double amountPaid) {
        Commande order = commandeRepository.findById(orderId).orElseThrow(()->new BusinessException("commande n'existe pas "));
        if(order.getMontantRestant()>=amountPaid){
            double newRemainingAmount= order.getMontantRestant()-amountPaid;
            order.setMontantRestant(newRemainingAmount);
            commandeRepository.save(order);
        }else {
            throw new BusinessException("Le montant saisi dépasse le montant restant.");

        }



    }


    public String confirmOrder(Long orderId) {
      Commande order= commandeRepository.findById(orderId).orElseThrow(()->new BusinessException("commande introuvable"));
      if(order.getMontantRestant()==0){
          order.setStatut(OrderStatus.CONFIRMED);
          commandeRepository.save(order);
          order.getClient().setTotalOrders(order.getClient().getTotalOrders()+1);
          order.getClient().setTotalSpent(order.getClient().getTotalSpent()+order.getMontantHT());
          for(OrderItem item: order.getItems()){
              productService.decreaseStockAfterOrder(item.getProduct(), item.getQuantite());
          }
          clientService.updateFidelite(order.getClient());
          return "commande confirmée ";
      }
       return  "le paiement n'est pas complet ";

    }

    @Transactional
    public String updateCommande(Long orderId, CommandeDTO req) {
        Commande order = commandeRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Commande introuvable"));

        if (order.getStatut() != OrderStatus.PENDING) {
            throw new BusinessException("Impossible de modifier une commande dont le statut est : " + order.getStatut());
        }

        // Mise à jour du client si besoin
        if (req.getClientId() != null) {
            Client client = clientRepository.findById(req.getClientId())
                    .orElseThrow(() -> new BusinessException("Client introuvable"));
            order.setClient(client);
        }

        // Nettoyage des anciens items avant update
        order.getItems().clear();

        double subTotal = 0.0;

        for (OrderItemDTO itemReq : req.getItems()) {

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new BusinessException("Produit introuvable"));

            if (product.getStock() < itemReq.getQuantite()) {
                throw new BusinessException("Stock insuffisant pour : " + product.getNom());
            }

            double totalLigne = product.getPrixUnitaire() * itemReq.getQuantite();
            subTotal += totalLigne;

            OrderItem itemEntity = orderItemMapper.toEntity(itemReq);
            itemEntity.setCommande(order);
            itemEntity.setTotalLigne(totalLigne);

            order.getItems().add(itemEntity);
        }

        // Recalcul discount
        double discount = calculateLoyaltyDiscount(order.getClient(), subTotal);
        if (req.getCodePromo() != null && req.getCodePromo().matches("PROMO-[A-Z0-9]{4}")) {
            discount += subTotal * 0.5;
        }

        // Recalcul montants
        double htAfterDiscount = subTotal - discount;
        double tva = htAfterDiscount * 0.20;
        double totalTTC = htAfterDiscount + tva;

        order.setSousTotalHT(subTotal);
        order.setMontantHT(htAfterDiscount);
        order.setRemise(discount);
        order.setTva(tva);
        order.setTotalTTC(totalTTC);
        order.setMontantRestant(htAfterDiscount);

        commandeRepository.save(order);

        return "Commande mise à jour avec succès";
    }












}
