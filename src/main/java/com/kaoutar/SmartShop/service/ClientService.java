package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.DTO.ClientDTO;
import com.kaoutar.SmartShop.Mapper.ClientMapper;
import com.kaoutar.SmartShop.enums.CustomerTier;
import com.kaoutar.SmartShop.enums.UserRole;
import com.kaoutar.SmartShop.model.Client;
import com.kaoutar.SmartShop.model.User;
import com.kaoutar.SmartShop.repositery.ClientRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
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
       User Client = (User) session.getAttribute("user");

        if (Client == null) {
            throw new RuntimeException("Aucun utilisateur connecté");
        }

        Client client = clientRepository.findById(Client.getId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        return mapper.toDto(client);
    }


}
