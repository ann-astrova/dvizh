package com.example.demo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ConfirmAttendanceRequest(Integer rewardAmount) {

    public int resolvedRewardAmount() {
        return rewardAmount == null ? 0 : rewardAmount;
    }
}
