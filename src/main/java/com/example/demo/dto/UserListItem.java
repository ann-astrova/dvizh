package com.example.demo.dto;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

// Один пользователь в списке GET /admin/users (эндпоинт 19).
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserListItem(UUID userId, String name, String email, Role role) {
    public static UserListItem from(User u) {
        return new UserListItem(u.getId(), u.getName(), u.getEmail(), u.getRole());
    }
}
