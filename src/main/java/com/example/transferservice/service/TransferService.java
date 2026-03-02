package com.example.transferservice.service;

import com.example.transferservice.model.dto.*;
import com.example.transferservice.repository.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import java.security.SecureRandom;

@Service
@Validated
public class TransferService {

    private static final SecureRandom random = new SecureRandom();
    private final OperationRepository operationRepository;
    private final LoggingService loggingService;

    public TransferService(OperationRepository operationRepository, LoggingService loggingService) {
        this.operationRepository = operationRepository;
        this.loggingService = loggingService;
    }

    public TransferResponse initiateTransfer(@Valid TransferRequest request) {
        if (request.getCardFromNumber().equals(request.getCardToNumber())) {
            operationRepository.logValidationError(
                    request.getCardFromNumber(), request.getCardToNumber(),
                    request.getAmount().getValue(), request.getAmount().getCurrency(),
                    "Нельзя переводить на ту же карту"
            );
            throw new IllegalArgumentException("Перевод на ту же карту запрещён");
        }

        String confirmationCode = String.format("%06d", random.nextInt(1_000_000));

        String operationId = operationRepository.savePendingOperation(
                request.getCardFromNumber(),
                request.getCardToNumber(),
                request.getAmount().getValue(),
                request.getAmount().getCurrency(),
                confirmationCode
        );

        System.out.println("[DEV] Confirmation code for " + operationId + ": " + confirmationCode);

        return new TransferResponse(operationId);
    }

    public TransferResponse confirmOperation(@Valid ConfirmRequest request) {
        boolean confirmed = operationRepository.confirmOperation(
                request.getOperationId(),
                request.getCode()
        );

        if (!confirmed) {
            loggingService.logError(
                    request.getOperationId(), "****", "****", 0, "RUB",
                    "Confirmation failed: invalid code or expired operation"
            );
            throw new IllegalStateException("Неверный код или истёк срок операции");
        }

        return new TransferResponse(request.getOperationId());
    }
}
