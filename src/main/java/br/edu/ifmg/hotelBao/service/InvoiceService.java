package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.InvoiceDTO;
import br.edu.ifmg.hotelBao.dto.InvoiceDTO.StayInfo;
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
                .orElseThrow(() -> new ResourceNotFound("Cliente com id: " + clientId + " não encontrado"));

        List<Stay> stays = stayRepository.findByClientId(clientId);

        if (stays.isEmpty()) {
            return new InvoiceDTO(false,
                    "Não existem estadias para o cliente: " + client.getName(),
                    null, null, null,
                    null, 0.0);
        }

        boolean hasIncomplete = stays.stream()
                .anyMatch(s -> s.getRoom().getDescription() == null
                        || s.getRoom().getDescription().trim().isEmpty()
                        || s.getRoom().getPrice() <= 0);
        if (hasIncomplete) {
            return new InvoiceDTO(false,
                    "Existem quartos sem descrição ou valor informados. A operação deve ser abortada.",
                    null, null, null,
                    null, 0.0);
        }

        List<StayInfo> stayInfos = stays.stream()
                .map(s -> new StayInfo(s.getRoom().getDescription(), s.getRoom().getPrice()))
                .collect(Collectors.toList());

        double total = stayInfos.stream()
                .mapToDouble(StayInfo::getPrice)
                .sum();

        return new InvoiceDTO(true,
                "NOTA FISCAL",
                client.getName(),
                client.getEmail(),
                client.getPhone(),
                stayInfos,
                total);
    }
}