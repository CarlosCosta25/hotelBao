package br.edu.ifmg.hotelBao.resource;

import br.edu.ifmg.hotelBao.dto.ClientDTO;
import br.edu.ifmg.hotelBao.dto.ClientInsertDTO;
import br.edu.ifmg.hotelBao.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ifmg.hotelBao.util.ClientFactory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClientResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private String adminToken;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 999L;
        String login = "lucas";
        String password = "123456";

        adminToken = tokenUtil.obtainAccessToken(mockMvc, login, password);
    }


    @Test
    void getAllShouldReturnPage() throws Exception {
        // Act: Executa a requisição GET
        ResultActions result = mockMvc.perform(
                get("/user")
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void getAllShouldReturnSortedPageWhenSortByName() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/user?page=0&size=10&sort=name,asc")
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].name").value("João Silva"));
        result.andExpect(jsonPath("$.content[1].name").value("Maria Santos"));
    }


    @Test
    void getByIdShouldReturnClientDTOWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/user/{id}", existingId) // Usa o 'existingId' definido no setUp
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value("João Silva"));
    }

    @Test
    void getByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/user/{id}", nonExistingId) // Usa o 'nonExistingId' definido no setUp
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    void insertShouldReturnCreatedAndClientDTOWhenDataIsValid() throws Exception {
        ClientInsertDTO dto = ClientFactory.createClientInsertDTO();
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                post("/user")
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists()); // Verifica se um ID foi gerado
        result.andExpect(jsonPath("$.name").value(dto.getName()));
        result.andExpect(jsonPath("$.email").value(dto.getEmail()));
    }

    @Test
    void insertShouldReturnUnprocessableEntityWhenDataIsInvalid() throws Exception {
        ClientInsertDTO dto = ClientFactory.createClientInsertDTO();
        dto.setName("");
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                post("/user")
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateShouldReturnOkAndClientDTOWhenIdExistsAndDataIsValid() throws Exception {
        ClientDTO dto = ClientFactory.createClientDTO();
        String jsonBody = objectMapper.writeValueAsString(dto);

        // Act: Executa a requisição PUT
        ResultActions result = mockMvc.perform(
                put("/user/{id}", existingId) // Usa o 'existingId'
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(dto.getName()));
        result.andExpect(jsonPath("$.email").value(dto.getEmail()));
    }

    @Test
    void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ClientDTO dto = ClientFactory.createClientDTO();
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                put("/user/{id}", nonExistingId) // Usa o 'nonExistingId'
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    void updateShouldReturnUnprocessableEntityWhenNameIsBlank() throws Exception {
        ClientDTO dto = ClientFactory.createClientDTO();
        dto.setName(""); // Forçando o erro de validação
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                put("/user/{id}", existingId)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateShouldReturnUnprocessableEntityWhenEmailIsInvalid() throws Exception {
        ClientDTO dto = ClientFactory.createClientDTO();
        dto.setEmail("email-invalido.com"); // Forçando o erro de validação
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                put("/user/{id}", existingId)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteShouldReturnOkWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/user/{id}", existingId) // Usa o 'existingId'
                        .header("Authorization", "Bearer " + adminToken)
        );
        result.andExpect(status().isOk());
    }


    @Test
    void signUpShouldReturnCreatedWhenDataIsValid() throws Exception {
        ClientInsertDTO dto = ClientFactory.createClientInsertDTO();
        dto.setEmail("novousuario@teste.com");
        dto.setLogin("novo_usuario");
        String jsonBody = objectMapper.writeValueAsString(dto);

        // Act: Executa a requisição POST para /signup (sem token de autenticação)
        ResultActions result = mockMvc.perform(
                post("/user/signup")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists()); // Verifica se um ID foi gerado
        result.andExpect(jsonPath("$.name").value(dto.getName()));
        result.andExpect(jsonPath("$.email").value("novousuario@teste.com"));
        result.andExpect(jsonPath("$.roles[0].authority").value("ROLE_CLIENT")); // Verifica se o cliente foi criado com a role padrão
    }

    @Test
    void signUpShouldReturnUnprocessableEntityWhenDataIsInvalid() throws Exception {
        ClientInsertDTO dto = ClientFactory.createClientInsertDTO();
        dto.setName(""); // Forçando um erro de validação
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                post("/user/signup")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isUnprocessableEntity());
    }
}
