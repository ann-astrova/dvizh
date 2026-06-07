package com.example.demo.dto;

import com.example.demo.enums.ParticipationStatus;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetParticipant {
    private UUID userId;
    private String name;
    private ParticipationStatus participationStatus;
}
