package br.edu.ifmg.hotelBao.entitie;

import br.edu.ifmg.hotelBao.dto.ClientDTO;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@TableGenerator(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String password;
    private String login;
    private String phone;
    private Instant createdAt;
    private Instant updatedAt;


    public Client() {
    }
    public Client(long id, String name, String email, String password, String login, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.login = login;
        this.phone = phone;
    }

    public Client(ClientDTO clientDTO) {
        this.id = clientDTO.getId();
        this.name = clientDTO.getName();
        this.email = clientDTO.getEmail();
        this.phone = clientDTO.getPhone();
    }


    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", login='" + login + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
