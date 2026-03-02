package com.example.transferservice.repository;

import com.example.transferservice.service.LoggingService;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Repository
public class OperationRepository {

    private final Map<String, PendingOperation> pendingOperations = new ConcurrentHashMap<>();
    private final LoggingService loggingService;

    public OperationRepository(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    public String savePendingOperation(String cardFrom, String cardTo,
                                       Integer amount, String currency, String confirmationCode) {
        String operationId = generateOperationId();
        pendingOperations.put(operationId, new PendingOperation(
                cardFrom, cardTo, amount, currency, confirmationCode, System.currentTimeMillis()
        ));
        return operationId;
    }

    public boolean confirmOperation(String operationId, String code) {
        PendingOperation operation = pendingOperations.get(operationId);
        if (operation == null) return false;

        if (System.currentTimeMillis() - operation.getCreatedAt() > TimeUnit.MINUTES.toMillis(10)) {
            pendingOperations.remove(operationId);
            return false;
        }

        if (!operation.getConfirmationCode().equals(code)) {
            return false;
        }

        pendingOperations.remove(operationId);

        Double commission = calculateCommission(operation.getAmount());
        loggingService.logSuccess(operationId, operation.getCardFrom(), operation.getCardTo(),
                operation.getAmount(), operation.getCurrency(), commission);
        return true;
    }

    public void logValidationError(String cardFrom, String cardTo, Integer amount,
                                   String currency, String error) {
        loggingService.logError("VALIDATION_ERROR", cardFrom, cardTo, amount, currency, error);
    }

    private String generateOperationId() {
        return "OP-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 10000);
    }

    private Double calculateCommission(Integer amount) {
        return amount * 0.015;
    }

    private static class PendingOperation {
        private final String cardFrom;
        private final String cardTo;
        private final Integer amount;
        private final String currency;
        private final String confirmationCode;
        private final long createdAt;

        public PendingOperation(String cardFrom, String cardTo, Integer amount,
                                String currency, String confirmationCode, long createdAt) {
            this.cardFrom = cardFrom;
            this.cardTo = cardTo;
            this.amount = amount;
            this.currency = currency;
            this.confirmationCode = confirmationCode;
            this.createdAt = createdAt;
        }

        public String getCardFrom() { return cardFrom; }
        public String getCardTo() { return cardTo; }
        public Integer getAmount() { return amount; }
        public String getCurrency() { return currency; }
        public String getConfirmationCode() { return confirmationCode; }
        public long getCreatedAt() { return createdAt; }
    }
}
