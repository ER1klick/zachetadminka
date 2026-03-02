package edu.rutmiit.demo.stoyakapicontract.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "orders", itemRelation = "order")
@JsonRootName(value = "order")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse extends RepresentationModel<OrderResponse> {

    private final Long orderId;
    private final Long clientId;
    private final Long runnerId;
    private final QueueResponse queue;
    private final LocalDateTime desiredTime;
    private final String status;
    private final BigDecimal price;
    private final String specialInstructions;
    private final LocalDateTime createdAt;
    private final LocalDateTime completedAt;

    public OrderResponse(Long orderId, Long clientId, Long runnerId, QueueResponse queue, LocalDateTime desiredTime, String status, BigDecimal price, String specialInstructions, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.runnerId = runnerId;
        this.queue = queue;
        this.desiredTime = desiredTime;
        this.status = status;
        this.price = price;
        this.specialInstructions = specialInstructions;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getRunnerId() {
        return runnerId;
    }

    public QueueResponse getQueue() {
        return queue;
    }

    public LocalDateTime getDesiredTime() {
        return desiredTime;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(clientId, that.clientId) && Objects.equals(runnerId, that.runnerId) && Objects.equals(queue, that.queue) && Objects.equals(desiredTime, that.desiredTime) && Objects.equals(status, that.status) && Objects.equals(price, that.price) && Objects.equals(specialInstructions, that.specialInstructions) && Objects.equals(createdAt, that.createdAt) && Objects.equals(completedAt, that.completedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderId, clientId, runnerId, queue, desiredTime, status, price, specialInstructions, createdAt, completedAt);
    }
}