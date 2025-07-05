package br.edu.ifmg.hotelBao.util;

import br.edu.ifmg.hotelBao.dto.ClientDTO;
import br.edu.ifmg.hotelBao.dto.ClientInsertDTO;
import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.entitie.Role;

public class ClientFactory {
    public static Client createClient() {
        Client client = new Client();
        client.setName("Cliente de Teste");
        client.setEmail("clienteteste@teste.com");
        client.setPhone("37 998431234");
        client.setPassword("123456");
        client.setLogin("teste");
        client.addRole(new Role(2L, "ROLE_ADMIN"));
        return client;
    }

    public static ClientDTO createClientDTO() {
        return new ClientDTO(createClient());
    }
    public static ClientInsertDTO createClientInsertDTO() {

        return new ClientInsertDTO(createClient());
    }
}
