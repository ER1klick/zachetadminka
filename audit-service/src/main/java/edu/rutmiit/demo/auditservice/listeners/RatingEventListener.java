package edu.rutmiit.demo.auditservice.listeners;

import edu.rutmiit.demo.events.UserRatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RatingEventListener {

    private static final Logger log = LoggerFactory.getLogger(RatingEventListener.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "q.audit.analytics", durable = "true"),
            exchange = @Exchange(name = "analytics-fanout", type = "fanout")
    ))
    public void handleUserRated(UserRatedEvent event) {
        log.info("AUDIT: Received rating event. User {} got score {} (Verdict: {})",
                event.userId(), event.score(), event.verdict());
    }
}