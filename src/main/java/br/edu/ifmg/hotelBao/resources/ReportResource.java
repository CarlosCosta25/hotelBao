package br.edu.ifmg.hotelBao.resources;

import br.edu.ifmg.hotelBao.dto.ReportDTO;
import br.edu.ifmg.hotelBao.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping("/report")
@Tag(name = "Report", description = "Controller/Resource for reports")
public class ReportResource {

    @Autowired private ReportService reportService;

    @GetMapping("/client/{clientId}/highest-stay")
    @Operation(summary = "Get highest value stay by client", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ReportDTO> getHighestValueStayByClient(@PathVariable Long clientId) {
        ReportDTO report = reportService.getHighestValueStayByClient(clientId);

        // Se o cliente não existe, retorna 404
        if ("O cliente informado não existe.".equals(report.getMessage())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(report);
        }

        // Não há estadias? continua 200 OK mas com mensagem informativa
        report.add(linkTo(methodOn(ReportResource.class)
                .getHighestValueStayByClient(clientId)).withSelfRel());
        report.add(linkTo(methodOn(ReportResource.class)
                .getLowestValueStayByClient(clientId)).withRel("lowest-stay"));
        report.add(linkTo(methodOn(ReportResource.class)
                .getTotalStayValueByClient(clientId)).withRel("total-value"));

        return ResponseEntity.ok(report);
    }

    @GetMapping("/client/{clientId}/lowest-stay")
    public ResponseEntity<ReportDTO> getLowestValueStayByClient(@PathVariable Long clientId) {
        ReportDTO report = reportService.getLowestValueStayByClient(clientId);
        if ("O cliente informado não existe.".equals(report.getMessage())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(report);
        }
        report.add(linkTo(methodOn(ReportResource.class)
                .getLowestValueStayByClient(clientId)).withSelfRel());
        report.add(linkTo(methodOn(ReportResource.class)
                .getHighestValueStayByClient(clientId)).withRel("highest-stay"));
        report.add(linkTo(methodOn(ReportResource.class)
                .getTotalStayValueByClient(clientId)).withRel("total-value"));
        return ResponseEntity.ok(report);
    }

    @GetMapping("/client/{clientId}/total-value")
    public ResponseEntity<ReportDTO> getTotalStayValueByClient(@PathVariable Long clientId) {
        ReportDTO report = reportService.getTotalStayValueByClient(clientId);
        if ("O cliente informado não existe.".equals(report.getMessage())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(report);
        }
        report.add(linkTo(methodOn(ReportResource.class)
                .getTotalStayValueByClient(clientId)).withSelfRel());
        report.add(linkTo(methodOn(ReportResource.class)
                .getHighestValueStayByClient(clientId)).withRel("highest-stay"));
        report.add(linkTo(methodOn(ReportResource.class)
                .getLowestValueStayByClient(clientId)).withRel("lowest-stay"));
        return ResponseEntity.ok(report);
    }
}
