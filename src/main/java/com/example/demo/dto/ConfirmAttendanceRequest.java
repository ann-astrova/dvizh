package com.example.demo.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ConfirmAttendanceRequest(Integer rewardAmount) {

    public int resolvedRewardAmount() {
        return rewardAmount == null ? 0 : rewardAmount;
    }
}
