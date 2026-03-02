package com.example.transferservice.model.dto;

import java.util.Objects;

public class TransferResponse {

    private final String operationId;

    public TransferResponse(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() { return operationId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferResponse that = (TransferResponse) o;
        return Objects.equals(operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId);
    }

    @Override
    public String toString() {
        return "TransferResponse{operationId='" + operationId + "'}";
    }
}
