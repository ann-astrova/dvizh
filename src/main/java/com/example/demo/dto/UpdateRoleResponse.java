package com.example.demo.dto;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

// Ответ PATCH /admin/users/{id}/role (эндпоинт 3).
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateRoleResponse(UUID userId, Role role, Instant updatedAt) {
    public static UpdateRoleResponse from(User u) {
        return new UpdateRoleResponse(u.getId(), u.getRole(), u.getUpdatedAt());
    }
}
