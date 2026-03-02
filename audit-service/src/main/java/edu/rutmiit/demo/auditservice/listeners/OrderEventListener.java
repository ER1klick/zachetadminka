package edu.rutmiit.demo.auditservice.listeners;

import com.rabbitmq.client.Channel;
import edu.rutmiit.demo.events.OrderCreatedEvent;
import edu.rutmiit.demo.events.OrderDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private static final String EXCHANGE_NAME = "orders-exchange";
    private static final String QUEUE_NAME = "audit-queue";
    private static final String DLX_NAME = "orders-exchange.dlx";
    private static final String DLQ_NAME = QUEUE_NAME + ".dlq";

    // --- СЛУШАТЕЛЬ ТОЛЬКО ДЛЯ СОБЫТИЙ СОЗДАНИЯ ---
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = QUEUE_NAME, durable = "true",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = DLX_NAME),
                            @Argument(name = "x-dead-letter-routing-key", value = "dlq.audit")
                    }),
            exchange = @Exchange(name = EXCHANGE_NAME, type = "topic", durable = "true"),
            key = "order.created" // <-- Слушает только ключ 'order.created'
    ))
    public void handleOrderCreatedEvent(@Payload OrderCreatedEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("Received OrderCreatedEvent: {}", event);

            // Имитируем ошибку для тестирования DLQ
            if (event.price() != null && event.price().compareTo(new BigDecimal("999")) == 0) {
                throw new RuntimeException("Simulating processing error for DLQ test with price 999");
            }

            log.info("AUDIT LOG: Order with id {} was created.", event.orderId());
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Failed to process OrderCreatedEvent: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    // --- СЛУШАТЕЛЬ ТОЛЬКО ДЛЯ СОБЫТИЙ УДАЛЕНИЯ ---
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = QUEUE_NAME, durable = "true", // Та же самая очередь
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = DLX_NAME),
                            @Argument(name = "x-dead-letter-routing-key", value = "dlq.audit")
                    }),
            exchange = @Exchange(name = EXCHANGE_NAME, type = "topic", durable = "true"),
            key = "order.deleted" // <-- Слушает только ключ 'order.deleted'
    ))
    public void handleOrderDeletedEvent(@Payload OrderDeletedEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("Received OrderDeletedEvent: {}", event);
            log.info("AUDIT LOG: Order with id {} was deleted.", event.orderId());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process OrderDeletedEvent: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    // --- СЛУШАТЕЛЬ ДЛЯ ОЧЕРЕДИ МЕРТВЫХ ПИСЕМ (DLQ) ---
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = DLQ_NAME, durable = "true"),
            exchange = @Exchange(name = DLX_NAME, type = "topic", durable = "true"),
            key = "dlq.audit"
    ))
    public void handleDlqMessages(Object failedMessage) {
        log.error("!!! MESSAGE LANDED IN DLQ: {}", failedMessage);
    }
}