package br.edu.ifmg.hotelBao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private String invoiceText;
    private boolean success;
    private String message;

    public InvoiceDTO(String invoiceText) {
        this.invoiceText = invoiceText;
        this.success = true;
    }

    public InvoiceDTO(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}