package edu.rutmiit.demo.demorest.controllers;

import edu.rutmiit.demo.demorest.config.RabbitMQConfig;
import edu.rutmiit.demo.events.UserRatedEvent;
import edu.rutmiit.demo.grpc.AnalyticsServiceGrpc;
import edu.rutmiit.demo.grpc.UserRatingRequest;
import edu.rutmiit.demo.grpc.UserRatingResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingController {

    @GrpcClient("analytics-service")
    private AnalyticsServiceGrpc.AnalyticsServiceBlockingStub analyticsStub;

    private final RabbitTemplate rabbitTemplate;

    public RatingController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/api/users/{id}/rate")
    public String rateUser(@PathVariable Long id) {
        System.out.println("Calling gRPC Analytics Service for user " + id);

        UserRatingRequest request = UserRatingRequest.newBuilder()
                .setUserId(id)
                .setCategory("General")
                .build();

        UserRatingResponse gRpcResponse = analyticsStub.calculateUserRating(request);

        UserRatedEvent event = new UserRatedEvent(
                gRpcResponse.getUserId(),
                gRpcResponse.getRatingScore(),
                gRpcResponse.getVerdict()
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", event);

        return "Rating calculated via gRPC: " + gRpcResponse.getRatingScore() +
                " (" + gRpcResponse.getVerdict() + "). Event published.";
    }
}