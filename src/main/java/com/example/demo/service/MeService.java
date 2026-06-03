package com.example.demo.service;

import com.example.demo.dto.MyAchievementsResponse;
import com.example.demo.dto.MyCreatedEventsResponse;
import com.example.demo.dto.MyEventsResponse;
import com.example.demo.repository.EventParticipantsRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserAchievementsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MeService {

    private final EventParticipantsRepository eventParticipantsRepository;
    private final EventRepository eventRepository;
    private final UserAchievementsRepository userAchievementsRepository;

    public MeService(
            EventParticipantsRepository eventParticipantsRepository,
            EventRepository eventRepository,
            UserAchievementsRepository userAchievementsRepository
    ) {
        this.eventParticipantsRepository = eventParticipantsRepository;
        this.eventRepository = eventRepository;
        this.userAchievementsRepository = userAchievementsRepository;
    }

    public MyEventsResponse getMyEvents(UUID userId) {
        return new MyEventsResponse(
                eventParticipantsRepository.findMyEventItemsByUserId(userId)
        );
    }

    public MyCreatedEventsResponse getMyCreatedEvents(UUID userId) {
        return new MyCreatedEventsResponse(
                eventRepository.findMyCreatedEventItemsByCreatorId(userId)
        );
    }

    public MyAchievementsResponse getMyAchievements(UUID userId) {
        return new MyAchievementsResponse(
                userAchievementsRepository.findMyAchievementItemsByUserId(userId)
        );
    }
}
