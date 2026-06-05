package com.example.demo.dto;

import com.example.demo.entity.User;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

// Ответ PATCH /me (эндпоинт 2).
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateProfileResponse(
        UUID userId, String name, String description, Instant updatedAt
) {
    public static UpdateProfileResponse from(User u) {
        return new UpdateProfileResponse(u.getId(), u.getName(), u.getDescription(), u.getUpdatedAt());
    }
}
