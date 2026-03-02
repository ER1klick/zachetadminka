package edu.rutmiit.demo.events;

import java.io.Serializable;

public record OrderDeletedEvent(
        Long orderId
) implements Serializable {
}