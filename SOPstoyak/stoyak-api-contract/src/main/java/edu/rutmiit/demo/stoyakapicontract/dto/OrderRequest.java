package edu.rutmiit.demo.stoyakapicontract.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderRequest(
        @NotNull(message = "ID клиента не может быть пустым")
        Long clientId,

        @NotNull(message = "ID очереди не может быть пустым")
        Long queueId,

        @NotNull(message = "Желаемое время не может быть пустым")
        LocalDateTime desiredTime,

        @NotNull(message = "Цена не может быть пустой")
        @Positive(message = "Цена должна быть положительной")
        BigDecimal price,

        String specialInstructions
) {}