package com.example.demo.dto;

import com.example.demo.enums.EventStatus;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetEventDetails {
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
    private Boolean isFinished;
    private GetUser creator;
    private GetUser moderatedBy;
    private Instant createdAt;
    private Instant updatedAt;
}
