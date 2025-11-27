package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.DTO.ClientDTO;
import com.kaoutar.SmartShop.Mapper.ClientMapper;
import com.kaoutar.SmartShop.enums.CustomerTier;
import com.kaoutar.SmartShop.enums.UserRole;
import com.kaoutar.SmartShop.exception.BusinessException;
import com.kaoutar.SmartShop.model.Client;
import com.kaoutar.SmartShop.model.User;
import com.kaoutar.SmartShop.repositery.ClientRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
@Transactional
@AllArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private  final ClientMapper mapper;


    public ClientDTO createClient(ClientDTO request) {
        Optional<Client> clientExiste = clientRepository.findByEmail(request.getEmail());
        if (clientExiste.isPresent()) {
            throw new IllegalArgumentException("Email déjà utilisé !");
        }

        Client client = mapper.toEntity(request);
        String hashedPass= BCrypt.hashpw(client.getPassword(),BCrypt.gensalt());
        client.setPassword(hashedPass);
        client.setRole(UserRole.CLIENT);
        client.setTier(CustomerTier.BASIC);
        client.setTotalOrders(0);
        client.setTotalSpent(0.0);
        client.setCommandes(new ArrayList<>());

        return mapper.toDto(clientRepository.save(client));
    }


    public ClientDTO getProfile(HttpSession session) {
        Long clientId = (Long) session.getAttribute("userId");


        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        return mapper.toDto(client);
    }


    public ClientDTO updateProfile(ClientDTO request, HttpSession session) {
        Long clientId = (Long) session.getAttribute("userId");


        Client clientExiste = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        if (request.getNom() != null) clientExiste.setNom(request.getNom());
        if (request.getEmail() != null) clientExiste.setEmail(request.getEmail());
        if (request.getPassword() != null) {
            String hashedPass = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
            clientExiste.setPassword(hashedPass);
        }


        Client clientMisAJour = clientRepository.save(clientExiste);

        return mapper.toDto(clientMisAJour);
    }






}




