package com.example.demo.dto;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

// Ответ GET /me (эндпоинт 1).
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserResponse(
        UUID userId,
        String email,
        String name,
        String description,
        Role role,
        int balance,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserResponse from(User u) {
        return new UserResponse(
                u.getId(), u.getEmail(), u.getName(), u.getDescription(),
                u.getRole(), u.getBalance(), u.getCreatedAt(), u.getUpdatedAt()
        );
    }
}
