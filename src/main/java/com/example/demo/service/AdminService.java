package com.example.demo.service;

import com.example.demo.dto.EventModerationResponse;
import com.example.demo.entity.Event;
import com.example.demo.entity.User;
import com.example.demo.enums.EventStatus;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AdminService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public AdminService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public EventModerationResponse approve(UUID eventId, UUID adminUserId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getStatus() != EventStatus.pending) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending events can be approved");
        }

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));

        event.setStatus(EventStatus.approved);
        event.setModeratedBy(admin);
        Event saved = eventRepository.save(event);

        return new EventModerationResponse(
                saved.getId(),
                saved.getStatus().name(),
                saved.getModeratedBy().getId(),
                saved.getUpdatedAt()
        );
    }
}
