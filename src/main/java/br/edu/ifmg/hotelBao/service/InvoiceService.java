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
                .orElseThrow(() -> new ResourceNotFound("Cliente com id " + clientId + " não encontrado"));

        List<Stay> stays = stayRepository.findByClientId(clientId);
        if (stays.isEmpty()) {
            throw new ResourceNotFound("Não há estadias para o cliente com id " + clientId);
        }

        boolean incomplete = stays.stream()
                .anyMatch(s -> s.getRoom().getDescription() == null || s.getRoom().getPrice() <= 0);
        if (incomplete) {
            throw new ResourceNotFound("Existem estadias com dados incompletos para o cliente com id " + clientId);
        }

        List<StayDTO> stayDTOs = stays.stream()
                .map(StayDTO::new)
                .collect(Collectors.toList());

        double total = stayDTOs.stream()
                .mapToDouble(dto -> dto.getRoom().getPrice())
                .sum();

        return new InvoiceDTO(new ClientDTO(client), stayDTOs, total);
    }
}
