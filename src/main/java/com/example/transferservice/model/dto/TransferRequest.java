package com.example.transferservice.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Objects;

public class TransferRequest {

    @NotBlank(message = "Номер карты отправителя обязателен")
    @Pattern(regexp = "\\d{16}", message = "Номер карты должен содержать 16 цифр")
    private final String cardFromNumber;

    @NotBlank(message = "Срок действия карты обязателен")
    @Pattern(regexp = "\\d{2}/\\d{2}", message = "Формат: MM/YY")
    private final String cardFromValidTill;

    @NotBlank(message = "CVV обязателен")
    @Pattern(regexp = "\\d{3}", message = "CVV должен содержать 3 цифры")
    private final String cardFromCVV;

    @NotBlank(message = "Номер карты получателя обязателен")
    @Pattern(regexp = "\\d{16}", message = "Номер карты должен содержать 16 цифр")
    private final String cardToNumber;

    @NotNull(message = "Сумма перевода обязательна")
    @Valid
    private final Amount amount;

    public TransferRequest(String cardFromNumber, String cardFromValidTill, String cardFromCVV,
                           String cardToNumber, Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardFromValidTill = cardFromValidTill;
        this.cardFromCVV = cardFromCVV;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
    }

    public String getCardFromNumber() { return cardFromNumber; }
    public String getCardFromValidTill() { return cardFromValidTill; }
    public String getCardFromCVV() { return cardFromCVV; }
    public String getCardToNumber() { return cardToNumber; }
    public Amount getAmount() { return amount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferRequest that = (TransferRequest) o;
        return Objects.equals(cardFromNumber, that.cardFromNumber) &&
                Objects.equals(cardFromValidTill, that.cardFromValidTill) &&
                Objects.equals(cardFromCVV, that.cardFromCVV) &&
                Objects.equals(cardToNumber, that.cardToNumber) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardFromNumber, cardFromValidTill, cardFromCVV, cardToNumber, amount);
    }

    @Override
    public String toString() {
        return "TransferRequest{cardFrom='****', cardTo='****', amount=" + amount + "}";
    }
}
