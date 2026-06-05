package com.example.demo.dto;

import com.example.demo.entity.Notification;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

// Один элемент списка GET /notifications (эндпоинт 16).
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NotificationItem(
        UUID id,
        String message,
        boolean isRead,
        Instant createdAt
) {
    public static NotificationItem from(Notification n) {
        // isRead в сущности хранится как строка "true"/"false" — приводим к boolean для JSON.
        boolean read = "true".equalsIgnoreCase(n.getIsRead());
        return new NotificationItem(n.getId(), n.getMessage(), read, n.getCreatedAt());
    }
}
