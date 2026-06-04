package com.example.demo.dto;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class CreateEventRequest {
    private UUID categoryId;
    private String title;
    private String description;
    private String location;
    private Instant startTime;
    private Instant endTime;
    private int maxParticipants;

}
