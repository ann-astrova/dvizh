package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "user_achievements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAchievements {

    @ManyToOne
    private User user;

    @ManyToOne
    private Achievement achievement;

    @Column(name = "awarded_at", nullable = false, columnDefinition = "TIMESTAMPTZ")
    private Instant awardedAt;
}
