package com.example.demo.config;

import java.util.List;

public final class AchievementDefinitions {

    private AchievementDefinitions() {}

    public record Entry(String title, String description, int rewardAmount) {}

    public static final Entry FIRST_ATTENDANCE = new Entry(
            "Первое мероприятие",
            "Посетил первое мероприятие",
            10
    );

    public static final Entry FIVE_ATTENDANCES = new Entry(
            "5 мероприятий",
            "Посетил пять мероприятий",
            50
    );

    public static final Entry FIRST_CREATED_EVENT = new Entry(
            "Создал мероприятие",
            "Создал первое одобренное мероприятие",
            15
    );

    public static List<Entry> all() {
        return List.of(FIRST_ATTENDANCE, FIVE_ATTENDANCES, FIRST_CREATED_EVENT);
    }
}
