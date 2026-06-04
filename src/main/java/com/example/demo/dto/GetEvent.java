package com.example.demo.dto;

import com.example.demo.enums.EventStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class GetEvent {
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
}
