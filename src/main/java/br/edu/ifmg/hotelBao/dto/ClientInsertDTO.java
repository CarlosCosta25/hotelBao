package br.edu.ifmg.hotelBao.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientInsertDTO extends ClientDTO{
    @Size(min = 2, max = 64)
    private String password;

    public ClientInsertDTO() {
        super();
    }
}
