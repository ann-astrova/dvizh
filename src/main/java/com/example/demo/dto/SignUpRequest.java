package com.example.demo.dto;

import com.example.demo.enums.ParticipationStatus;
import lombok.Data;

@Data
public class SignUpRequest {
    private ParticipationStatus status;
}
