package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.ReportDTO;
import br.edu.ifmg.hotelBao.repository.StayRepository;
import br.edu.ifmg.hotelBao.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public ReportDTO getHighestValueStayByClient(Long clientId) {
        // Verificar se o cliente existe
        if (!clientRepository.existsById(clientId)) {
            return new ReportDTO(
                    "RELATÓRIO - ESTADIA DE MAIOR VALOR",
                    "O cliente informado não existe.",
                    0.0
            );
        }

        Double maxValue = stayRepository.findHighestValueStayByClient(clientId);

        if (maxValue == null) {
            return new ReportDTO(
                    "RELATÓRIO - ESTADIA DE MAIOR VALOR",
                    "Nenhuma estadia encontrada para o cliente informado.",
                    0.0
            );
        }



        return new ReportDTO(
                "RELATÓRIO - ESTADIA DE MAIOR VALOR",
                String.format("De frente pra praia Valor: R$ %.1f", maxValue),
                maxValue
        );
    }

    @Transactional(readOnly = true)
    public ReportDTO getLowestValueStayByClient(Long clientId) {
        // Verificar se o cliente existe
        if (!clientRepository.existsById(clientId)) {
            return new ReportDTO(
                    "RELATÓRIO - ESTADIA DE MENOR VALOR",
                    "O cliente informado não existe.",
                    0.0
            );
        }
        Double result = stayRepository.findLowestValueStayByClient(clientId);

        if (result == null) {
            return new ReportDTO(
                    "RELATÓRIO - ESTADIA DE MENOR VALOR",
                    "Nenhuma estadia encontrada para o cliente informado.",
                    0.0
            );
        }


        return new ReportDTO(
                "RELATÓRIO - ESTADIA DE MENOR VALOR",
                String.format("Simples Valor: R$ %.1f", result),
                result
        );
    }

    @Transactional(readOnly = true)
    public ReportDTO getTotalStayValueByClient(Long clientId) {
        // Verificar se o cliente existe
        if (!clientRepository.existsById(clientId)) {
            return new ReportDTO(
                    "RELATÓRIO - O TOTAL DAS ESTADIAS DO CLIENTE",
                    "O cliente informado não existe.",
                    0.0
            );
        }

        Double totalValue = stayRepository.findTotalValueByClient(clientId);

        if (totalValue == null || totalValue == 0.0) {
            return new ReportDTO(
                    "RELATÓRIO - O TOTAL DAS ESTADIAS DO CLIENTE",
                    "Nenhuma estadia encontrada para o cliente informado.",
                    0.0
            );
        }

        return new ReportDTO(
                "RELATÓRIO - O TOTAL DAS ESTADIAS DO CLIENTE É",
                String.format("R$ %.1f", totalValue),
                totalValue
        );
    }
}