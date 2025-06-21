package br.edu.ifmg.hotelBao.dto;

import br.edu.ifmg.hotelBao.entitie.Client;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ClientDTO extends RepresentationModel<ClientDTO> {

    private Long id;
    @NotBlank(message = "Nome não pode ser vazio")
    @Size(max = 50, message = "Nome não pode ter mais de 50 caracteres")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "Nome deve conter apenas letras e espaços")
    private String name;
    @Email
    @NotBlank(message = "Email não pode ser vazio")
    private String email;
    @NotBlank(message = "Telefone não pode ser vazio")
    private String phone;
    @NotBlank(message = "Username não pode ser vazio")
    private String login;

    private Set<RoleDTO> roles = new HashSet<>();

    public ClientDTO(Long id, String name, String email, String phone, String login) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.login = login;
    }

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.email = client.getEmail();
        this.phone = client.getPhone();
        this.login = client.getLogin();
        client.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

}
