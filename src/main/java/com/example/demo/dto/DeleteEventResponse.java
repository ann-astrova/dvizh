package com.example.demo.dto;

import com.example.demo.enums.EventStatus;
import com.example.demo.enums.ParticipationStatus;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeleteEventResponse {
    private UUID eventId;
    private EventStatus status;
    private Instant updatedAt;
}
