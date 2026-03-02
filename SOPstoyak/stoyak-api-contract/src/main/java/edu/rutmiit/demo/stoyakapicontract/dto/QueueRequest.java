package edu.rutmiit.demo.stoyakapicontract.dto;

import jakarta.validation.constraints.NotBlank;

public record QueueRequest(
        @NotBlank(message = "Название не может быть пустым")
        String name,

        @NotBlank(message = "Адрес не может быть пустым")
        String address,

        String description,
        String workingHours
) {}