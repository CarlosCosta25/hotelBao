package br.edu.ifmg.hotelBao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO extends RepresentationModel<ReportDTO> {
    private String title;
    private String message;
    private Double value;
}