// src/main/java/com/example/transferservice/model/dto/Amount.java
package com.example.transferservice.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Objects;

public class Amount {

        @Positive(message = "Сумма должна быть положительной")
        @NotNull(message = "Сумма обязательна")
        private final Integer value;

        @NotNull(message = "Валюта обязательна")
        private final String currency;

        public Amount(Integer value, String currency) {
                this.value = value;
                this.currency = currency;
        }

        public Integer getValue() { return value; }
        public String getCurrency() { return currency; }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Amount amount = (Amount) o;
                return Objects.equals(value, amount.value) && Objects.equals(currency, amount.currency);
        }

        @Override
        public int hashCode() {
                return Objects.hash(value, currency);
        }

        @Override
        public String toString() {
                return "Amount{value=" + value + ", currency='" + currency + "'}";
        }
}