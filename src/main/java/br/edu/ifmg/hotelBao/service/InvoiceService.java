package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.ClientDTO;
import br.edu.ifmg.hotelBao.dto.InvoiceDTO;
import br.edu.ifmg.hotelBao.dto.StayDTO;
import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.entitie.Stay;
import br.edu.ifmg.hotelBao.repository.ClientRepository;
import br.edu.ifmg.hotelBao.repository.StayRepository;
import br.edu.ifmg.hotelBao.service.exceptions.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StayRepository stayRepository;

    @Transactional(readOnly = true)
    public InvoiceDTO generateInvoice(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFound("Client id " + clientId + " not found"));

        List<Stay> stays = stayRepository.findByClientId(clientId);
        if (stays.isEmpty()) {
            throw new IllegalArgumentException("No stays found for client");
        }

        boolean incomplete = stays.stream()
                .anyMatch(s -> s.getRoom().getDescription() == null || s.getRoom().getPrice() <= 0);
        if (incomplete) {
            throw new IllegalArgumentException("Incomplete stay data detected");
        }

        // Convert to DTOs
        List<StayDTO> stayDTOs = stays.stream()
                .map(StayDTO::new)
                .collect(Collectors.toList());

        // Calculate total using room price from DTO
        double total = stayDTOs.stream()
                .mapToDouble(dto -> dto.getRoom().getPrice())
                .sum();

        return new InvoiceDTO(new ClientDTO(client), stayDTOs, total);
    }
}