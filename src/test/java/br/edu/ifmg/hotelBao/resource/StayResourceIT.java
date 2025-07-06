package br.edu.ifmg.hotelBao.resource;
import br.edu.ifmg.hotelBao.dto.StayDTO;
import br.edu.ifmg.hotelBao.util.StayFactory;
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

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StayResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private String adminToken;
    private Long existingId;
    private Long nonExistingId;
    private Long existingClientId;
    private Long nonExistingClientId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 999L;

        existingClientId = 1L;
        nonExistingClientId = 998L;

        String login = "lucas";
        String password = "123456";
        adminToken = tokenUtil.obtainAccessToken(mockMvc, login, password);
    }

    @Test
    void getAllShouldReturnPageOfStays() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay")
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void getAllShouldReturnSortedPageWhenSortByCheckIn() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay?sort=checkIn,desc")
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].checkIn").value("2024-03-10T17:00:00Z"));
        result.andExpect(jsonPath("$.content[1].checkIn").value("2024-02-28T17:00:00Z"));
    }

    @Test
    void getByIdShouldReturnStayDTOWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay/{id}", existingId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.client.id").exists()); // Verifica se o cliente foi incluído
        result.andExpect(jsonPath("$.room.id").exists());   // Verifica se o quarto foi incluído
    }

    @Test
    void getByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isNotFound());
    }

    @Test
    void insertShouldReturnCreatedWhenDataIsValid() throws Exception {
        StayDTO dto = StayFactory.createStayDTO();
        dto.setId(0L); // Garante que o ID é nulo para inserção
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                post("/stay")
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // Assert: Verifica a resposta
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.client.name").value("João Silva"));
        result.andExpect(jsonPath("$.room.description").value("Quarto Standard 101"));
    }

    @Test
    void insertShouldReturnUnprocessableEntityWhenCheckInIsInThePast() throws Exception {
        StayDTO dto = StayFactory.createStayDTO();
        dto.setId(null);
        // Força um erro de validação da anotação @FutureOrPresent
        dto.setCheckIn(Instant.now().minusSeconds(86400)); // Data de ontem
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                post("/stay")
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateShouldReturnOkAndStayDTOWhenIdExists() throws Exception {
        StayDTO dto = StayFactory.createStayDTO();
        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(
                put("/stay/{id}", existingId)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.client.name").value("João Silva"));
    }

    @Test
    void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        StayDTO dto = StayFactory.createStayDTO();
        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(
                put("/stay/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }



    @Test
    void deleteShouldReturnOkWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/stay/{id}", existingId)
                        .header("Authorization", "Bearer " + adminToken)
        );
        result.andExpect(status().isOk());
    }


    @Test
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/stay/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + adminToken)
        );
        result.andExpect(status().isNotFound());
    }

    @Test
    void getByClientShouldReturnListOfStaysWhenClientExists() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay/client/{clientId}", existingClientId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$[0].client.id").value(existingClientId));
    }

    @Test
    void getByClientShouldReturnNotFoundWhenClientDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay/client/{clientId}", nonExistingClientId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isNotFound());
    }

    @Test
    void getTotalValueByClientShouldReturnTotalValueWhenClientExists() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay/client/{clientId}/total", existingClientId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.total_estadia").exists());
        // Soma dos valores das estadias do cliente existente
        result.andExpect(jsonPath("$.total_estadia").value(950.00));
    }

    @Test
    void getTotalValueByClientShouldReturnNotFoundWhenClientDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay/client/{clientId}/total", nonExistingClientId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isNotFound());
    }

    @Test
    void getHighestValueStayByClientShouldReturnStayDTOWhenClientExists() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay/client/{clientId}/highest", existingClientId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.client.id").value(existingClientId));
        result.andExpect(jsonPath("$.id").value(5L));
        result.andExpect(jsonPath("$.room.price").value(400.00));
    }

    @Test
    void getHighestValueStayByClientShouldReturnNotFoundWhenClientDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay/client/{clientId}/highest", nonExistingClientId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isNotFound());
    }

    @Test
    void getLowestValueStayByClientShouldReturnStayDTOWhenClientExists() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay/client/{clientId}/lowest", existingClientId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.client.id").value(existingClientId));

        result.andExpect(jsonPath("$.id").value(1L));
        result.andExpect(jsonPath("$.room.price").value(150.00));
    }

    @Test
    void getLowestValueStayByClientShouldReturnNotFoundWhenClientDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/stay/client/{clientId}/lowest", nonExistingClientId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // Assert: Verifica a resposta de erro
        result.andExpect(status().isNotFound());
    }
}
