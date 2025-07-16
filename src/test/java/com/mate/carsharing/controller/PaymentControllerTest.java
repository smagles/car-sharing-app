package com.mate.carsharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.carsharing.dto.payment.PaymentRequestDto;
import com.mate.carsharing.model.Payment;
import com.mate.carsharing.model.User;
import com.mate.carsharing.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest {
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
    @DisplayName("Should create payment for authenticated user")
    void createPayment_ValidRequest_ShouldReturnCreated() throws Exception {
        PaymentRequestDto requestDto = new PaymentRequestDto(1L, Payment.PaymentType.PAYMENT);
        User alice = userRepository.findByEmail("alice@example.com")
                .orElseThrow();

        String json = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/payments")
                        .with(user(alice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    @DisplayName("Should return list of user's payments")
    void getMyPayments_ShouldReturnListOfPayments() throws Exception {
        User alice = userRepository.findByEmail("alice@example.com")
                .orElseThrow();
        mockMvc.perform(get("/payments/my")
                        .with(user(alice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]").isArray());
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @DisplayName("Should return list of payments by user ID for manager")
    void getPaymentsByUser_ShouldReturnListOfPayments() throws Exception {
        mockMvc.perform(get("/payments/user/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]").isArray());
    }

    @Test
    @DisplayName("Should return cancel message for canceled payment")
    void paymentCancel_ShouldReturnCancelMessage() throws Exception {
        mockMvc.perform(get("/payments/cancel")
                        .param("session_id", "test_session_id"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment was canceled. You can try again within 24 hours"));
    }
}
