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
public class UserGetEventDetails {
    private UUID id;
    private String title;
    private String description;
    private String location;
    private GetCategory category;
    private Instant startTime;
    private Instant endTime;
    private EventStatus status;
    private int maxParticipants;
    private int currentParticipants;
    private ParticipationStatus myParticipationStatus;
    private Boolean isFinished;
    private GetUser creator;
    private Instant createdAt;
    private Instant updatedAt;
}

