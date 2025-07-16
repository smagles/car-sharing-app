package com.mate.carsharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.carsharing.dto.rental.RentalCreateRequestDto;
import com.mate.carsharing.model.User;
import com.mate.carsharing.repository.UserRepository;
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
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/database/002-add-default-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/database/003-add-default-rentals.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class RentalControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void setup(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    @DisplayName("Should create rental and return created response")
    void createRental_ValidRequest_ShouldReturnCreated() throws Exception {
        LocalDate today = LocalDate.now();
        RentalCreateRequestDto requestDto = new RentalCreateRequestDto();
        requestDto.setCarId(2L);
        requestDto.setRentalDate(today);
        requestDto.setReturnDate(today.plusDays(5));

        User bob = userRepository.findByEmail("bob@example.com")
                .orElseThrow();

        String json = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(bob))
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    @DisplayName("Should return BadRequest for invalid rental and return dates")
    void createRental_InvalidDates_ShouldReturnBadRequest() throws Exception {
        RentalCreateRequestDto requestDto = new RentalCreateRequestDto();
        requestDto.setCarId(3L);
        requestDto.setRentalDate(LocalDate.now().plusDays(1));
        requestDto.setReturnDate(LocalDate.now());

        mockMvc.perform(post("/rentals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    @DisplayName("Should return rental when returned with valid rental ID")
    void returnRental_WithValidRentalId_ShouldReturnRental() throws Exception {
        User alice = userRepository.findByEmail("alice@example.com")
                .orElseThrow();
        mockMvc.perform(post("/rentals/{id}/return", 1)
                        .with(user(alice)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    @DisplayName("Should return page of rentals for customer")
    void getRentals_ShouldReturnPageOfRentals() throws Exception {
        mockMvc.perform(get("/rentals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @DisplayName("Should return rental details for manager")
    void getRental_AsManager_ShouldReturnDetailedRental() throws Exception {
        User charlie = userRepository.findByEmail("charlie@example.com")
                .orElseThrow();

        mockMvc.perform(get("/rentals/{id}", 1)
                        .with(user(charlie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @DisplayName("Should return rentals by user ID for manager")
    void getRentalsByUserId_ShouldReturnRentalsForGivenUser() throws Exception {
        mockMvc.perform(get("/rentals/user/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @DisplayName("Should return all rentals for manager")
    void getAllRentals_ShouldReturnAllRentals() throws Exception {
        mockMvc.perform(get("/rentals/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }
}
