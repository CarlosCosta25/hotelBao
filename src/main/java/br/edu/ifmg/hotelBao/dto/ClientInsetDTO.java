package br.edu.ifmg.hotelBao.dto;
import br.edu.ifmg.hotelBao.entitie.Client;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
public class ClientInsetDTO extends ClientDTO{
    @NotBlank
    private String password;
    @NotBlank
    private String login;

}
