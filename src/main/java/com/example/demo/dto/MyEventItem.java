package com.example.demo.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MyEventItem(
        UUID eventId,
        String title,
        String status,
        String participationStatus
) {}
