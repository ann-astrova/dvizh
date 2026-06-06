package com.example.demo.service;

import com.example.demo.dto.MyAchievementsResponse;
import com.example.demo.dto.MyCreatedEventsResponse;
import com.example.demo.dto.MyEventsResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.EventParticipantsRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserAchievementsRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MeService {

    @Autowired
    private EventParticipantsRepository eventParticipantsRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserAchievementsRepository userAchievementsRepository;
    @Autowired
    private UserRepository userRepository;

    public MyEventsResponse getMyEvents(String authId) {
        User user = userRepository.findByAuthId(authId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new MyEventsResponse(
                eventParticipantsRepository.findMyEventItemsByUserId(user.getId())
        );
    }

    public MyCreatedEventsResponse getMyCreatedEvents(String authId) {
        User user = userRepository.findByAuthId(authId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new MyCreatedEventsResponse(
                eventRepository.findMyCreatedEventItemsByCreatorId(user.getId())
        );
    }

    public MyAchievementsResponse getMyAchievements(String authId) {
        User user = userRepository.findByAuthId(authId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new MyAchievementsResponse(
                userAchievementsRepository.findMyAchievementItemsByUserId(user.getId())
        );
    }
}
