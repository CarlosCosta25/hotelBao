package br.edu.ifmg.hotelBao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private boolean success;
    private String message;
    private String clientName;
    private String email;
    private String phone;
    private List<StayInfo> stays;
    private double total;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StayInfo {
        private String description;
        private double price;
    }
}