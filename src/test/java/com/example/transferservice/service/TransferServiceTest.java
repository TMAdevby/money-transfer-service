package com.example.transferservice.service;

import com.example.transferservice.model.dto.*;
import com.example.transferservice.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock private OperationRepository operationRepository;
    @Mock private LoggingService loggingService;

    private TransferService transferService;

    @BeforeEach
    void setUp() {
        transferService = new TransferService(operationRepository, loggingService);
    }

    @Test
    void initiateTransfer_success() {
        Amount amount = new Amount(1000, "RUB");
        TransferRequest request = new TransferRequest(
                "1234567890123456", "12/25", "123",
                "6543210987654321", amount
        );
        when(operationRepository.savePendingOperation(any(), any(), anyInt(), anyString(), anyString()))
                .thenReturn("OP-123456");

        TransferResponse response = transferService.initiateTransfer(request);

        assertNotNull(response);
        assertEquals("OP-123456", response.getOperationId());
        verify(operationRepository).savePendingOperation(any(), any(), eq(1000), eq("RUB"), anyString());
    }

    @Test
    void confirmOperation_success() {
        when(operationRepository.confirmOperation("OP-123", "123456")).thenReturn(true);
        ConfirmRequest request = new ConfirmRequest("OP-123", "123456");

        TransferResponse response = transferService.confirmOperation(request);

        assertEquals("OP-123", response.getOperationId());
        verify(loggingService, never()).logError(any(), any(), any(), anyInt(), anyString(), anyString());
    }

    @Test
    void confirmOperation_invalidCode() {
        when(operationRepository.confirmOperation("OP-123", "wrong")).thenReturn(false);
        ConfirmRequest request = new ConfirmRequest("OP-123", "wrong");

        assertThrows(IllegalStateException.class, () ->
                transferService.confirmOperation(request)
        );
        verify(loggingService).logError(any(), any(), any(), anyInt(), anyString(), anyString());
    }
}