package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Category;
import com.example.demo.entity.Event;
import com.example.demo.entity.User;
import com.example.demo.enums.EventStatus;
import com.example.demo.enums.ParticipationStatus;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.EventParticipantsRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EventService {
    @Autowired
    private EventParticipantsRepository  eventParticipantsRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    private GetUser makeGetUser(User user) {
        GetUser getUser = new GetUser();
        getUser.setId(user.getId());
        getUser.setName(user.getName());
        return getUser;
    }

    private GetCategory makeGetCategory(Category category) {
        GetCategory getCategory = new GetCategory();
        getCategory.setId(category.getId());
        getCategory.setName(category.getName());
        return getCategory;
    }

    private GetEvent makeGetEvent(Event event){
        GetEvent getEvent = new GetEvent();

        getEvent.setId(event.getId());
        getEvent.setTitle(event.getTitle());
        getEvent.setDescription(event.getDescription());
        getEvent.setLocation(event.getLocation());
        getEvent.setStartTime(event.getStartTime());
        getEvent.setEndTime(event.getEndTime());
        getEvent.setStatus(event.getStatus());
        getEvent.setMaxParticipants(event.getMaxParticipants());

        User user = event.getCreator();
        getEvent.setCreator(makeGetUser(user));

        getEvent.setCategory(makeGetCategory(event.getCategory()));

        if (event.getEndTime().isBefore(Instant.now())) {
            getEvent.setIsFinished(true);
        } else {
            getEvent.setIsFinished(false);
        }

        getEvent.setCurrentParticipants(eventParticipantsRepository.countByEventIdAndStatus(event.getId(), ParticipationStatus.registered));
        return getEvent;

    }

    public GetEvents GetEvents(){
        List<Event> events = eventRepository.findAll();
        List<GetEvent> getEvents = new ArrayList<>();
        for (Event event : events) {
            getEvents.add(makeGetEvent(event));
        }
        return new GetEvents(getEvents);
    }


    public GetEventDetails GetEventDetails(UUID id){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        GetEventDetails getEventDetails = new GetEventDetails();
        GetCategory getCategory = new GetCategory();

        getEventDetails.setId(event.getId());
        getEventDetails.setTitle(event.getTitle());
        getEventDetails.setDescription(event.getDescription());
        getEventDetails.setLocation(event.getLocation());
        getEventDetails.setStartTime(event.getStartTime());
        getEventDetails.setEndTime(event.getEndTime());
        getEventDetails.setStatus(event.getStatus());
        getEventDetails.setMaxParticipants(event.getMaxParticipants());

        User creator = event.getCreator();
        getEventDetails.setCreator(makeGetUser(creator));

        User moderator = event.getModeratedBy();
        getEventDetails.setModeratedBy(makeGetUser(moderator));

        Category category = event.getCategory();
        getEventDetails.setCategory(makeGetCategory(category));

        if (event.getEndTime().isBefore(Instant.now())) {
            getEventDetails.setIsFinished(true);
        } else {
            getEventDetails.setIsFinished(false);
        }

        getEventDetails.setCurrentParticipants(eventParticipantsRepository.countByEventIdAndStatus(event.getId(), ParticipationStatus.registered));
        getEventDetails.setCreatedAt(event.getCreatedAt());
        getEventDetails.setUpdatedAt(event.getUpdatedAt());
        return getEventDetails;
    }

    public CreateEventResponse CreateEvent(CreateEventRequest request, String authId){
        User creator = userRepository.findByAuthId(authId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time cannot be after end time");
        }
        Event event = new Event();
        event.setCreator(creator);
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setLocation(request.getLocation());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setMaxParticipants(request.getMaxParticipants());

        Category category = categoryRepository.findById(request.getCategoryId());
        event.setCategory(category);

        if (creator.getRole().name().equals("ADMIN")) {
            event.setModeratedBy(creator);
            event.setStatus(EventStatus.approved);
        }
        else{
            event.setStatus(EventStatus.pending);
        }
        eventRepository.save(event);

        CreateEventResponse createEventResponse = new CreateEventResponse();
        createEventResponse.setEventId(event.getId());
        createEventResponse.setCreatedAt(event.getCreatedAt());
        createEventResponse.setStatus(event.getStatus());
        return createEventResponse;
    }

    private Event PatchingEvent(PatchEventRequest request, UUID eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        int currentParticipants =  eventParticipantsRepository.countByEventIdAndStatus(event.getId(), ParticipationStatus.registered);
        String newTitle = request.getTitle();
        String newDescription = request.getDescription();
        String newLocation = request.getLocation();
        Instant newStartTime = request.getStartTime();
        Instant newEndTime = request.getEndTime();
        UUID newCategoryId = request.getCategoryId();
        Integer newMaxParticipants = request.getMaxParticipants();
        if (newStartTime != null && newEndTime != null && newStartTime.isAfter(newEndTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time cannot be after end time");
        }
        else if (newStartTime == null && newEndTime != null && event.getStartTime().isAfter(newEndTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time cannot be after end time");
        }
        else if (newStartTime != null && newEndTime == null && newStartTime.isAfter(event.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time cannot be after end time");
        }


        if (newTitle != null){
            event.setTitle(newTitle);
        }
        if (newDescription != null){
            event.setDescription(newDescription);
        }
        if (newLocation != null){
            event.setLocation(newLocation);
        }
        if (newStartTime != null){
            event.setStartTime(newStartTime);
        }
        if (newEndTime != null){
            event.setEndTime(newEndTime);
        }
        if (newMaxParticipants != null && currentParticipants <= newMaxParticipants){
            event.setMaxParticipants(newMaxParticipants);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Max participants cannot be less than current " + currentParticipants);
        }

        if (newCategoryId != null){
            event.setCategory(categoryRepository.findById(newCategoryId));
        }
        eventRepository.save(event);
        return event;

    }

    public PatchEventResponse PatchEvent(PatchEventRequest request, String authId, UUID eventId){
        User creator = userRepository.findByAuthId(authId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));;
        if ((event.getStartTime().plus(Duration.ofHours(3)).isAfter(Instant.now()))){
            if (creator.getRole().name().equals("student")
                    && eventRepository.existsByCreatorIdAndIdAndStatus(creator.getId(), eventId, EventStatus.pending)) {
                event = PatchingEvent(request, eventId);
            }
            else if (creator.getRole().name().equals("admin")){
                event = PatchingEvent(request, eventId);
            }
            PatchEventResponse patchEventResponse = new PatchEventResponse();
            patchEventResponse.setEventId(eventId);
            patchEventResponse.setUpdatedAt(event.getUpdatedAt());
            return  patchEventResponse;
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event is unavailable for patching");
        }
    }






}
