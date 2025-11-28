package com.kaoutar.SmartShop.service;

import com.kaoutar.SmartShop.DTO.ClientDTO;
import com.kaoutar.SmartShop.Mapper.ClientMapper;
import com.kaoutar.SmartShop.enums.CustomerTier;
import com.kaoutar.SmartShop.enums.UserRole;
import com.kaoutar.SmartShop.model.Client;
import com.kaoutar.SmartShop.repositery.ClientRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
     @Mock
     private   ClientRepository clientRepository;

    @Mock
    private ClientMapper mapper;
     @InjectMocks
     private  ClientService clientService;
    @Test
    void createClient_EmailAlreadyUsed_ShouldThrowException() {
        ClientDTO dto = new ClientDTO();
        dto.setEmail("test@mail.com");

        when(clientRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(new Client()));

        assertThrows(IllegalArgumentException.class, () -> {
            clientService.createClient(dto);
        });
    }

    @Test
    void createClient_NewEmail_ShouldSaveClient() {
        ClientDTO dto = new ClientDTO();
        dto.setEmail("test@mail.com");
        dto.setPassword("12345");

        when(clientRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        Client entityConverted = new Client();
        entityConverted.setEmail(dto.getEmail());
        entityConverted.setPassword(dto.getPassword());

        when(mapper.toEntity(dto)).thenReturn(entityConverted);
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);
        when(mapper.toDto(any(Client.class))).thenReturn(dto);

        ClientDTO result = clientService.createClient(dto);

        assertNotNull(result);
        verify(clientRepository, times(1)).save(any(Client.class));
    }


    @Test
    void createClient_ShouldHashPassword() {
        ClientDTO dto = new ClientDTO();
        dto.setEmail("test@mail.com");
        dto.setPassword("monPass");

        when(clientRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        Client entity = new Client();
        entity.setPassword(dto.getPassword());

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);
        when(mapper.toDto(any(Client.class))).thenReturn(dto);

        clientService.createClient(dto);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientCaptor.capture());

        assertNotEquals("monPass", clientCaptor.getValue().getPassword());
        assertTrue(clientCaptor.getValue().getPassword().startsWith("$2"));
    }

    @Test
    void createClient_DefaultValues_ShouldBeSet() {
        ClientDTO dto = new ClientDTO();
        dto.setEmail("test@mail.com");
        dto.setPassword("pass");

        when(clientRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        Client entity = new Client();
        entity.setPassword(dto.getPassword());

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);
        when(mapper.toDto(any(Client.class))).thenReturn(dto);

        clientService.createClient(dto);

        ArgumentCaptor<Client> saved = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(saved.capture());

        assertEquals(UserRole.CLIENT, saved.getValue().getRole());
        assertEquals(CustomerTier.BASIC, saved.getValue().getTier());
        assertEquals(0, saved.getValue().getTotalOrders());
        assertEquals(0.0, saved.getValue().getTotalSpent());
        assertNotNull(saved.getValue().getCommandes());
        assertTrue(saved.getValue().getCommandes().isEmpty());
    }

    @Test
    void createClient_ShouldCallMapper() {
        ClientDTO dto = new ClientDTO();
        dto.setEmail("test@mail.com");

        when(clientRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        when(mapper.toEntity(dto)).thenReturn(new Client());
        when(clientRepository.save(any(Client.class))).thenReturn(new Client());
        when(mapper.toDto(any(Client.class))).thenReturn(dto);

        clientService.createClient(dto);

        verify(mapper, times(1)).toEntity(dto);
        verify(mapper, times(1)).toDto(any(Client.class));
    }
    @Test
    void getClientById_ShouldReturnClientDTO_WhenClientExists() {
        Client client = new Client();
        client.setId(1L);

        ClientDTO dto = new ClientDTO();
        dto.setId(1L);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(mapper.toDto(client)).thenReturn(dto);

        ClientDTO result = clientService.getClientById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(clientRepository).findById(1L);
        verify(mapper).toDto(client);
    }

    @Test
    void getClientById_ShouldThrowException_WhenClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            clientService.getClientById(1L);
        });

        verify(clientRepository).findById(1L);
    }
    @Test
    void getAllClients_ShouldReturnPaginatedClientDTOs() {
        Client client = new Client();
        client.setId(1L);

        ClientDTO dto = new ClientDTO();
        dto.setId(1L);

        Page<Client> pageResult = new PageImpl<>(List.of(client));

        when(clientRepository.findAll(any(Pageable.class)))
                .thenReturn(pageResult);

        when(mapper.toDto(client)).thenReturn(dto);

        Page<ClientDTO> result = clientService.getAllClients(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(dto, result.getContent().get(0));
        verify(clientRepository).findAll(any(Pageable.class));
    }
    @Test
    void getAllClients_ShouldReturnEmptyPage_WhenNoClients() {
        Page<Client> emptyPage = Page.empty();

        when(clientRepository.findAll(any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<ClientDTO> result = clientService.getAllClients(0, 10);

        assertTrue(result.isEmpty());
        verify(clientRepository).findAll(any(Pageable.class));
    }




}