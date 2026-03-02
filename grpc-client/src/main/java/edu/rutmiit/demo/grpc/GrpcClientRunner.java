package edu.rutmiit.demo.grpc;

import edu.rutmiit.demo.grpc.*;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GrpcClientRunner implements CommandLineRunner {

    @GrpcClient("my-server-channel")
    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- 1. Создание пользователя ---");
        CreateUserRequest createRequest = CreateUserRequest.newBuilder()
                .setUsername("Ivan_Vasilev")
                .setEmail("ivan@test.com")
                .build();

        CreateUserResponse createResponse = userServiceStub.createUser(createRequest);
        long userId = createResponse.getUser().getId();
        System.out.println("Created User: ID=" + userId + ", Name=" + createResponse.getUser().getUsername());


        System.out.println("\n--- 2. Обновление пользователя ---");
        UpdateUserRequest updateRequest = UpdateUserRequest.newBuilder()
                .setId(userId)
                .setNewUsername("Ivan_THE_GREAT")
                .build();

        UpdateUserResponse updateResponse = userServiceStub.updateUser(updateRequest);
        System.out.println("Updated User: Name=" + updateResponse.getUser().getUsername());


        System.out.println("\n--- 3. Получение достижений (Успех) ---");
        GetUserBadgesRequest badgesRequest = GetUserBadgesRequest.newBuilder().setUserId(userId).build();
        GetUserBadgesResponse badgesResponse = userServiceStub.getUserBadges(badgesRequest);

        System.out.println("Badges for user " + userId + ":");
        badgesResponse.getBadgesList().forEach(b -> System.out.println(" - " + b.getName() + ": " + b.getDescription()));


        System.out.println("\n--- 4. Получение достижений (Ошибка 404) ---");
        long fakeId = 99999L;
        System.out.println("Requesting badges for non-existent ID: " + fakeId);

        try {
            userServiceStub.getUserBadges(GetUserBadgesRequest.newBuilder().setUserId(fakeId).build());
        } catch (StatusRuntimeException e) {
            System.err.println("!!! ОШИБКА ПОЛУЧЕНА !!!");
            System.err.println("Status Code: " + e.getStatus().getCode());
            System.err.println("Description: " + e.getStatus().getDescription());
        }

        System.out.println("\n--- Тест завершен ---");
    }
}