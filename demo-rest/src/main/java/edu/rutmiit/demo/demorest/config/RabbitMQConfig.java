package edu.rutmiit.demo.demorest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);

    public static final String EXCHANGE_NAME = "orders-exchange";
    public static final String ROUTING_KEY_ORDER_CREATED = "order.created";
    public static final String ROUTING_KEY_ORDER_DELETED = "order.deleted";
    public static final String FANOUT_EXCHANGE = "analytics-fanout";

    @Bean
    public TopicExchange ordersExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public FanoutExchange analyticsExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.error("NACK: Message delivery failed! CorrelationData: {}, Cause: {}", correlationData, cause);
            } else {
                log.info("ACK: Message successfully delivered to exchange. CorrelationData: {}", correlationData);
            }
        });

        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("RETURN: Message returned! ReplyCode: {}, ReplyText: {}, Exchange: {}, RoutingKey: {}",
                    returned.getReplyCode(), returned.getReplyText(), returned.getExchange(), returned.getRoutingKey());
        });

        return rabbitTemplate;
    }
}