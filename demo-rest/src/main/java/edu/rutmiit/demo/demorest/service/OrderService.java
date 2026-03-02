package edu.rutmiit.demo.demorest.service;

import edu.rutmiit.demo.demorest.config.RabbitMQConfig;
import edu.rutmiit.demo.demorest.storage.InMemoryStorage;
import edu.rutmiit.demo.stoyakapicontract.dto.*;
import edu.rutmiit.demo.stoyakapicontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.events.OrderCreatedEvent;
import edu.rutmiit.demo.events.OrderDeletedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// ------------------------------------

import java.time.LocalDateTime;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final InMemoryStorage storage;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(InMemoryStorage storage, RabbitTemplate rabbitTemplate) {
        this.storage = storage;
        this.rabbitTemplate = rabbitTemplate;
    }

    public OrderResponse createOrder(OrderRequest request) {
        if (!storage.users.containsKey(request.clientId())) {
            throw new ResourceNotFoundException("Client", request.clientId());
        }
        QueueResponse queue = storage.queues.get(request.queueId());
        if (queue == null) {
            throw new ResourceNotFoundException("Queue", request.queueId());
        }

        long newId = storage.orderSequence.incrementAndGet();
        LocalDateTime creationTime = LocalDateTime.now();
        OrderResponse newOrder = new OrderResponse(newId, request.clientId(), null, queue, request.desiredTime(), "new", request.price(), request.specialInstructions(), creationTime, null);
        storage.orders.put(newId, newOrder);

        OrderCreatedEvent event = new OrderCreatedEvent(
                newId,
                request.clientId(),
                request.price(),
                creationTime
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_ORDER_CREATED,
                event
        );
        log.info("Published OrderCreatedEvent for orderId: {}", newId);

        return newOrder;
    }

    public void deleteOrder(Long orderId) {
        OrderResponse order = findOrderById(orderId);
        storage.orders.remove(orderId);
        log.info("Order with id {} was deleted from storage.", orderId);

        OrderDeletedEvent event = new OrderDeletedEvent(orderId);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_ORDER_DELETED,
                event
        );
        log.info("Published OrderDeletedEvent for orderId: {}", orderId);
    }

    public OrderResponse findOrderById(Long id) {
        OrderResponse order = storage.orders.get(id);
        if (order == null) { throw new ResourceNotFoundException("Order", id); }
        return order;
    }

    public StatusResponse cancelOrder(Long id) {
        findOrderById(id);
        return new StatusResponse("success", "Заявка с ID " + id + " была отменена.");
    }
}