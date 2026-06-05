package com.example.demo.dto;

import com.example.demo.enums.ParticipationStatus;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpOrDeleteResponse {
    private UUID eventId;
    private UUID userId;
    private ParticipationStatus status;
    private Instant updatedAt;
}
