package br.edu.ifmg.hotelBao.resource;

import br.edu.ifmg.hotelBao.dto.RoomDTO;
import br.edu.ifmg.hotelBao.util.RoomFactory;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RoomResourceIT {

    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper não é usado aqui, mas será útil para os próximos endpoints (POST, PUT)
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private Long existingId;
    private Long nonExistingId;
    // Adicione a variável para o token
    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 999L;

        String login = "lucas";
        String password = "123456";
        adminToken = tokenUtil.obtainAccessToken(mockMvc, login, password);
    }


    @Test
    void getAllShouldReturnPageOfRooms() throws Exception{
        ResultActions result = mockMvc.perform(
                get("/room")
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void getAllShouldReturnSortedPageWhenSortByPrice() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/room?sort=price,asc")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].price").value(150.0));
        result.andExpect(jsonPath("$.content[1].price").value(250.00));
    }

    @Test
    void getByIdShouldReturnRoomDTOWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/room/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.description").value("Quarto Standard 101"));
    }

    @Test
    void getByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/room/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    void insertShouldReturnCreatedAndRoomDTOWhenDataIsValid() throws Exception {
        RoomDTO dto = RoomFactory.createRoomDTO();
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                post("/room")
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.description").value("Quarto de Teste"));
        result.andExpect(jsonPath("$.price").value(100.0));
    }

    @Test
    void insertShouldReturnUnprocessableEntityWhenPriceIsInvalid() throws Exception {
        RoomDTO dto = RoomFactory.createRoomDTO();
        dto.setPrice(-50.0);
        String jsonBody = objectMapper.writeValueAsString(dto);


        ResultActions result = mockMvc.perform(
                post("/room")
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateShouldReturnOkAndRoomDTOWhenIdExists() throws Exception {
        RoomDTO dto = RoomFactory.createRoomDTO();
        String jsonBody = objectMapper.writeValueAsString(dto);


        ResultActions result = mockMvc.perform(
                put("/room/{id}", existingId)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );


        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.description").value("Quarto de Teste"));
        result.andExpect(jsonPath("$.price").value(100.0));
    }


    @Test
    void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        RoomDTO dto = RoomFactory.createRoomDTO();
        String jsonBody = objectMapper.writeValueAsString(dto);


        ResultActions result = mockMvc.perform(
                put("/room/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + adminToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    void updateShouldReturnUnprocessableEntityWhenPriceIsInvalid() throws Exception {
        RoomDTO dto = RoomFactory.createRoomDTO();
        dto.setPrice(0.0); // Força um erro de validação da anotação @Positive
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                put("/room/{id}", existingId)
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
                delete("/room/{id}", existingId)
                        .header("Authorization", "Bearer " + adminToken)
        );

        result.andExpect(status().isOk());
    }

    @Test
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/room/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + adminToken)
        );

        result.andExpect(status().isNotFound());
    }


}