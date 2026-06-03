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

    private final EventRepository eventRepository; // event repository
    private final UserRepository userRepository; // user repository

    public AdminService(EventRepository eventRepository, UserRepository userRepository) { // constructor
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public EventModerationResponse approve(UUID eventId, UUID adminUserId) {
        // checking that the action is allowed
        Event event = eventRepository.findById(eventId) // find event by id
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getStatus() != EventStatus.pending) { // check if event is pending
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending events can be approved");
        }

        User admin = userRepository.findById(adminUserId) // find admin by id
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));

        // change event and save it
        event.setStatus(EventStatus.approved);
        event.setModeratedBy(admin);
        Event saved = eventRepository.save(event);

        // return response
        return new EventModerationResponse(
                saved.getId(),
                saved.getStatus().name(),
                saved.getModeratedBy().getId(),
                saved.getUpdatedAt()
        );
    }

    public EventModerationResponse reject(UUID eventId, UUID adminUserId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getStatus() != EventStatus.pending) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending events can be rejected");
        }

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));

        event.setStatus(EventStatus.rejected);
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
