package com.example.demo.dto;

import com.example.demo.entity.Notification;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

// Ответ PATCH /notifications/{id}/read (эндпоинт 17).
// read_at в сущности Notification отсутствует, поэтому в ответ его не кладём
// (можно добавить поле read_at в Notification — это зона владельца сущности).
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MarkNotificationReadResponse(
        UUID id,
        boolean isRead,
        Instant readAt
) {
    public static MarkNotificationReadResponse from(Notification n, Instant readAt) {
        boolean read = "true".equalsIgnoreCase(n.getIsRead());
        return new MarkNotificationReadResponse(n.getId(), read, readAt);
    }
}
