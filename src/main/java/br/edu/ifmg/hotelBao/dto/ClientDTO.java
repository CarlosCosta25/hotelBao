package br.edu.ifmg.hotelBao.dto;

import br.edu.ifmg.hotelBao.entitie.Client;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    @JsonView({View.publicView.class, View.privateView.class})
    private Long id;
    @JsonView({View.publicView.class, View.privateView.class})
    private String name;
    @JsonView({View.publicView.class, View.privateView.class})
    private String email;
    @JsonView({View.publicView.class, View.privateView.class})
    private String phone;
    @JsonView(View.privateView.class)
    private String password;
    @JsonView(View.privateView.class)
    private String login;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.email = client.getEmail();
        this.phone = client.getPhone();
        this.password = client.getPassword();
        this.login = client.getLogin();
    }
    /*
    public ClientDTO() {
    }

    public ClientDTO(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }*/
}
