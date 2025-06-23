package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.InvoiceDTO;
import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.entitie.Stay;
import br.edu.ifmg.hotelBao.repository.ClientRepository;
import br.edu.ifmg.hotelBao.repository.StayRepository;
import br.edu.ifmg.hotelBao.service.exceptions.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StayRepository stayRepository;

    @Transactional(readOnly = true)
    public String generateInvoice(Long clientId) {
        // Buscar cliente
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFound("Cliente com id: " + clientId + " não encontrado"));

        // Buscar todas as estadias do cliente
        List<Stay> stays = stayRepository.findAll().stream()
                .filter(stay -> stay.getClient().getId() == clientId)
                .toList();

        // Validações
        if (stays.isEmpty()) {
            return generateErrorMessage("Não existem estadias para o cliente: " + client.getName());
        }

        // Verificar se há quartos sem descrição ou preço
        boolean hasIncompleteRooms = stays.stream()
                .anyMatch(stay -> stay.getRoom().getDescription() == null ||
                        stay.getRoom().getDescription().trim().isEmpty() ||
                        stay.getRoom().getPrice() <= 0);

        if (hasIncompleteRooms) {
            return generateErrorMessage("Existem quartos sem descrição ou valor informados. A operação deve ser abortada.");
        }

        // Gerar nota fiscal
        return generateInvoiceText(client, stays);
    }

    private String generateInvoiceText(Client client, List<Stay> stays) {
        StringBuilder invoice = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Cabeçalho
        invoice.append("==========================\n");
        invoice.append("NOTA FISCAL\n");
        invoice.append("==========================\n");
        invoice.append("Nome: ").append(client.getName()).append("\n");
        invoice.append("Email: ").append(client.getEmail()).append("\n");
        invoice.append("Telefone: ").append(client.getPhone() != null ? client.getPhone() : "Não informado").append("\n");
        invoice.append("==========================\n");
        invoice.append("===ESTADIAS===\n");
        invoice.append("==========================\n");

        // Listar estadias
        double total = 0.0;
        for (Stay stay : stays) {
            String roomDescription = stay.getRoom().getDescription();
            double roomPrice = stay.getRoom().getPrice();

            invoice.append("Quarto: ").append(roomDescription)
                    .append("        Valor: ").append(String.format("%.1f", roomPrice)).append("\n");

            total += roomPrice;
        }

        // Total
        invoice.append("==========================\n");
        invoice.append("Total: R$ ").append(String.format("%.1f", total)).append("\n");
        invoice.append("==========================\n");

        return invoice.toString();
    }

    private String generateErrorMessage(String message) {
        return "ERRO: " + message + "\n" +
                "A impressão da nota fiscal não pode ser concluída.";
    }
}