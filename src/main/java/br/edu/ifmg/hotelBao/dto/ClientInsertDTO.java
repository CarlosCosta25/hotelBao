package br.edu.ifmg.hotelBao.dto;
import br.edu.ifmg.hotelBao.entitie.Client;
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

    public ClientInsertDTO(Client client){
        super(client);
        this.password = client.getPassword();
    }
}
