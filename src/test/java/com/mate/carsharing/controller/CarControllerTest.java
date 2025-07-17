package com.mate.carsharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.carsharing.dto.car.CarCreateRequestDto;
import com.mate.carsharing.dto.car.CarDto;
import com.mate.carsharing.dto.car.CarUpdateRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/database/001-add-default-cars.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class CarControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext){
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).apply(springSecurity()).build();
    }

    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @Test
    @DisplayName("Should create a car and return the created car")
    void createCar_ValidRequest_ShouldReturnCreatedCar() throws Exception {
        CarCreateRequestDto request = new CarCreateRequestDto("Model S", "Tesla", "SEDAN", 5, BigDecimal.valueOf(200));

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/cars").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();

        CarDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CarDto.class);

        assertNotNull(actual);
        assertEquals("Tesla", actual.getBrand());
        assertEquals("Model S", actual.getModel());
    }

    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    @Test
    @DisplayName("Should return Forbidden when CUSTOMER tries to create a car")
    void createCar_WithUserRole_ShouldReturnForbidden() throws Exception {
        CarCreateRequestDto request = new CarCreateRequestDto("Tesla", "Model S", "SEDAN", 5, BigDecimal.valueOf(200));

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/cars").content(jsonRequest).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    @Test
    @DisplayName("Should return car by valid ID")
    void getCarById_WithValidId_ShouldReturnCar() throws Exception {
        mockMvc.perform(get("/cars/{id}", 1L)).andExpect(status().isOk());
    }

    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @Test
    @DisplayName("Should update a car and return updated car")
    void updateCar_ValidRequest_ShouldReturnUpdatedCar() throws Exception {
        CarUpdateRequestDto request = new CarUpdateRequestDto("Model X", "Tesla", "SEDAN", 5, BigDecimal.valueOf(200));

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/cars/{id}", 1L).content(jsonRequest).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        CarDto updated = objectMapper.readValue(result.getResponse().getContentAsString(), CarDto.class);
        assertEquals("Model X", updated.getModel());
    }

    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @Test
    @DisplayName("Should delete a car by valid ID and return NoContent")
    void deleteCar_ValidId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/cars/{id}", 1L)).andExpect(status().isNoContent());
    }

    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @Test
    @DisplayName("Should return NotFound when trying to delete non-existing car")
    void deleteCar_InvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/cars/{id}", 9999L)).andExpect(status().isNotFound());
    }
}
