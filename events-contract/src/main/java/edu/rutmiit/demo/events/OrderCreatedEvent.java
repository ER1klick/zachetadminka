package edu.rutmiit.demo.events;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreatedEvent(
        Long orderId,
        Long clientId,
        BigDecimal price,
        LocalDateTime createdAt
) implements Serializable {
}