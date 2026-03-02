package com.example.transferservice.controller;

import com.example.transferservice.model.dto.*;
import com.example.transferservice.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transfer")
@Validated
@Tag(name = "Transfer API", description = "API для перевода денег между картами")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @Operation(summary = "Перевод денег с карты на карту", description = "Call to send money between cards")
    @ApiResponse(responseCode = "200", description = "Success transfer")
    @ApiResponse(responseCode = "400", description = "Error input data")
    @ApiResponse(responseCode = "500", description = "Error transfer")
    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestBody @Valid TransferRequest request) {
        try {
            TransferResponse response = transferService.initiateTransfer(request);
            // ✅ Возвращаем успех (тип TransferResponse)
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // ✅ Возвращаем ошибку (тип ErrorResponse) — теперь это разрешено
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage(), 400));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Ошибка обработки перевода", 500));
        }
    }

    @Operation(summary = "Confirm operation", description = "Confirming operation with code")
    @ApiResponse(responseCode = "200", description = "Success confirmation")
    @ApiResponse(responseCode = "400", description = "Error input data")
    @ApiResponse(responseCode = "500", description = "Error confirmation")
    @PostMapping("/confirmOperation")
    public ResponseEntity<?> confirmOperation(@RequestBody @Valid ConfirmRequest request) {
        try {
            TransferResponse response = transferService.confirmOperation(request);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage(), 400));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("Ошибка подтверждения", 500));
        }
    }
}
