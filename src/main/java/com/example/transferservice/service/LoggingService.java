package com.example.transferservice.service;

import com.example.transferservice.model.entity.OperationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    private static final Logger operationLogger = LoggerFactory.getLogger("OPERATION_LOG");

    public void logSuccess(String operationId, String cardFrom, String cardTo,
                           Integer amount, String currency, Double commission) {
        OperationLog log = OperationLog.success(operationId, maskCard(cardFrom),
                maskCard(cardTo), amount, currency, commission);
        operationLogger.info("{}", formatLog(log));
    }

    public void logError(String operationId, String cardFrom, String cardTo,
                         Integer amount, String currency, String error) {
        OperationLog log = OperationLog.error(operationId, maskCard(cardFrom),
                maskCard(cardTo), amount, currency, error);
        operationLogger.warn("{}", formatLog(log));
    }

    private String maskCard(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "****";
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }

    private String formatLog(OperationLog log) {
        return String.format("DATE=%s TIME=%s FROM=%s TO=%s AMOUNT=%d %s COMMISSION=%.2f OP_ID=%s STATUS=%s ERROR=%s",
                log.getTimestamp().toLocalDate(),
                log.getTimestamp().toLocalTime(),
                log.getCardFrom(),
                log.getCardTo(),
                log.getAmount(),
                log.getCurrency(),
                log.getCommission(),
                log.getOperationId(),
                log.getStatus(),
                log.getErrorMessage() != null ? log.getErrorMessage() : "NONE"
        );
    }
}
