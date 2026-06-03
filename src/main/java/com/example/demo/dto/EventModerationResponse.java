package com.example.demo.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EventModerationResponse(
        UUID eventId,
        String status,
        UUID moderatedBy,
        Instant updatedAt
) {}
