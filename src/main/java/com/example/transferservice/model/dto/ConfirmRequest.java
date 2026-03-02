// src/main/java/com/example/transferservice/model/dto/ConfirmRequest.java
package com.example.transferservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

public class ConfirmRequest {

    @NotBlank(message = "operationId обязателен")
    private final String operationId;

    @NotBlank(message = "Код подтверждения обязателен")
    private final String code;

    public ConfirmRequest(String operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }

    public String getOperationId() { return operationId; }
    public String getCode() { return code; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmRequest that = (ConfirmRequest) o;
        return Objects.equals(operationId, that.operationId) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId, code);
    }

    @Override
    public String toString() {
        return "ConfirmRequest{operationId='" + operationId + "', code='***'}";
    }
}
