package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class MyCreatedEventItem {
    UUID eventId;
    String title;
    String status;
    Instant createdAt;

    public MyCreatedEventItem(UUID eventId, String title, String status, Instant createdAt) {
        this.eventId = eventId;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
    }
}
