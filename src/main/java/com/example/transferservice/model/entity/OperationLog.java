// src/main/java/com/example/transferservice/model/entity/OperationLog.java
package com.example.transferservice.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class OperationLog {

    private final LocalDateTime timestamp;
    private final String cardFrom;
    private final String cardTo;
    private final Integer amount;
    private final String currency;
    private final Double commission;
    private final String operationId;
    private final String status;
    private final String errorMessage;

    private OperationLog(LocalDateTime timestamp, String cardFrom, String cardTo,
                         Integer amount, String currency, Double commission,
                         String operationId, String status, String errorMessage) {
        this.timestamp = timestamp;
        this.cardFrom = cardFrom;
        this.cardTo = cardTo;
        this.amount = amount;
        this.currency = currency;
        this.commission = commission;
        this.operationId = operationId;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public static OperationLog success(String operationId, String cardFrom, String cardTo,
                                       Integer amount, String currency, Double commission) {
        return new OperationLog(LocalDateTime.now(), cardFrom, cardTo, amount, currency,
                commission, operationId, "SUCCESS", null);
    }

    public static OperationLog error(String operationId, String cardFrom, String cardTo,
                                     Integer amount, String currency, String errorMsg) {
        return new OperationLog(LocalDateTime.now(), cardFrom, cardTo, amount, currency,
                0.0, operationId, "ERROR", errorMsg);
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getCardFrom() { return cardFrom; }
    public String getCardTo() { return cardTo; }
    public Integer getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public Double getCommission() { return commission; }
    public String getOperationId() { return operationId; }
    public String getStatus() { return status; }
    public String getErrorMessage() { return errorMessage; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationLog that = (OperationLog) o;
        return Objects.equals(operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId);
    }

    @Override
    public String toString() {
        return String.format("OperationLog{date=%s time=%s from=%s to=%s amount=%d %s commission=%.2f opId=%s status=%s}",
                timestamp.toLocalDate(), timestamp.toLocalTime(), cardFrom, cardTo,
                amount, currency, commission, operationId, status);
    }
}
