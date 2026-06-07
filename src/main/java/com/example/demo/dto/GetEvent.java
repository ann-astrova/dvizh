package com.example.demo.dto;

import com.example.demo.entity.EventParticipants;
import com.example.demo.enums.EventStatus;
import com.example.demo.enums.ParticipationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetEvent {
    private UUID id;
    private String title;
    private String location;
    private GetCategory category;
    private Instant startTime;
    private Instant endTime;
    private EventStatus status;
    private int maxParticipants;
    private int currentParticipants;
    private ParticipationStatus myParticipationStatus;
    private Boolean isCreator;
    private Boolean isFinished;

}
