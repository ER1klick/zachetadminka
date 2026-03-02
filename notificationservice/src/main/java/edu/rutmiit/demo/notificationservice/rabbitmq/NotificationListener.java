package edu.rutmiit.demo.notificationservice.rabbitmq;

import edu.rutmiit.demo.events.UserRatedEvent;
import edu.rutmiit.demo.notificationservice.websocket.NotificationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    private final NotificationHandler notificationHandler;

    public NotificationListener(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.notifications.browser", durable = "true"),
            exchange = @Exchange(name = "analytics-fanout", type = "fanout")
    ))
    public void handleUserRatedEvent(UserRatedEvent event) {
        log.info("Received event from RabbitMQ: {}", event);

        String userMessage = String.format(
                "{\"type\": \"RATING_UPDATE\", \"userId\": %d, \"score\": %d, \"verdict\": \"%s\"}",
                event.userId(), event.score(), event.verdict()
        );

        notificationHandler.broadcast(userMessage);
    }
}