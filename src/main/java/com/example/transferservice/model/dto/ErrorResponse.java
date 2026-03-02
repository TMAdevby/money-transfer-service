// src/main/java/com/example/transferservice/model/dto/ErrorResponse.java
package com.example.transferservice.model.dto;

import java.util.Objects;

public class ErrorResponse {

    private final String message;
    private final Integer id;

    public ErrorResponse(String message, Integer id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() { return message; }
    public Integer getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return Objects.equals(message, that.message) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, id);
    }

    @Override
    public String toString() {
        return "ErrorResponse{message='" + message + "', id=" + id + "}";
    }
}