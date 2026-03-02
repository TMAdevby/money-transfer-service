package com.example.transferservice.service;

import com.example.transferservice.model.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class TransferIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired private TestRestTemplate restTemplate;

    @Test
    void transfer_validationError_sameCard() {
        Amount amount = new Amount(1000, "RUB");
        TransferRequest request = new TransferRequest(
                "1111222233334444", "12/26", "999",
                "1111222233334444", amount
        );

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/transfer/transfer", request, ErrorResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getId());
    }

    @Test
    void transfer_validationError_invalidCardNumber() {
        Amount amount = new Amount(1000, "RUB");
        TransferRequest request = new TransferRequest(
                "123", "12/26", "999",
                "5555666677778888", amount
        );

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/transfer/transfer", request, ErrorResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
