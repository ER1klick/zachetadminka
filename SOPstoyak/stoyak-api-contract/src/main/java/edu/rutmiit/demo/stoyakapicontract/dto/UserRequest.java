package edu.rutmiit.demo.stoyakapicontract.dto;

import edu.rutmiit.demo.stoyakapicontract.Enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotBlank(message = "Имя пользователя не может быть пустым")
        String username,

        @NotBlank(message = "Пароль не может быть пустым")
        String password,

        @Email(message = "Неверный формат email")
        @NotBlank(message = "Email не может быть пустым")
        String email,

        @NotBlank(message = "Номер телефона не может быть пустым")
        String phoneNumber,

        String firstName,
        String lastName,

        @NotNull(message = "Тип пользователя должен быть указан")
        UserType userType // ENUM: 'client', 'runner'
) {}