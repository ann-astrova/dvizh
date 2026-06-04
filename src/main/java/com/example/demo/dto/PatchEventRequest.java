package com.example.demo.dto;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PatchEventRequest {
    private String title;
    private String description;
    private String location;
    private Instant startTime;
    private Instant endTime;
    private Integer maxParticipants;
    private UUID categoryId;

}
