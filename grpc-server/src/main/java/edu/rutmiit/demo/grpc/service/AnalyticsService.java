package edu.rutmiit.demo.grpc.service;

import edu.rutmiit.demo.grpc.AnalyticsServiceGrpc;
import edu.rutmiit.demo.grpc.UserRatingRequest;
import edu.rutmiit.demo.grpc.UserRatingResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Random;

@GrpcService
public class AnalyticsService extends AnalyticsServiceGrpc.AnalyticsServiceImplBase {

    @Override
    public void calculateUserRating(UserRatingRequest request, StreamObserver<UserRatingResponse> responseObserver) {
        long userId = request.getUserId();
        String category = request.getCategory();

        System.out.println("Received rating request for User ID: " + userId + ", Category: " + category);

        int score = new Random().nextInt(101);

        String verdict;
        if (score > 80) {
            verdict = "EXCELLENT";
        } else if (score > 50) {
            verdict = "GOOD";
        } else {
            verdict = "BAD";
        }

        UserRatingResponse response = UserRatingResponse.newBuilder()
                .setUserId(userId)
                .setRatingScore(score)
                .setVerdict(verdict)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}