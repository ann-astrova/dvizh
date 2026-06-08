package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Category;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventParticipants;
import com.example.demo.entity.User;
import com.example.demo.enums.EventStatus;
import com.example.demo.enums.ParticipationStatus;
import com.example.demo.enums.Role;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.EventParticipantsRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private EntityManager entityManager;

    @Autowired
    private EventParticipantsRepository  eventParticipantsRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    private GetCategory makeGetCategory(Category category) {
        GetCategory getCategory = new GetCategory();
        getCategory.setId(category.getId());
        getCategory.setName(category.getName());
        return getCategory;
    }

    private GetEvent makeGetEvent(Event event, User user){
        GetEvent getEvent = new GetEvent();

        getEvent.setId(event.getId());
        getEvent.setTitle(event.getTitle());
        getEvent.setLocation(event.getLocation());
        getEvent.setStartTime(event.getStartTime());
        getEvent.setEndTime(event.getEndTime());
        getEvent.setStatus(event.getStatus());
        getEvent.setMaxParticipants(event.getMaxParticipants());

        getEvent.setIsCreator(user.equals(event.getCreator()));
        getEvent.setCategory(makeGetCategory(event.getCategory()));

        Optional<EventParticipants> eventParticipants = eventParticipantsRepository.findByEventIdAndUserId(event.getId(), user.getId());
        if (eventParticipants.isPresent()){
            getEvent.setMyParticipationStatus(eventParticipants.get().getStatus());
        }
        else{
            getEvent.setMyParticipationStatus(null);
        }

        if (event.getEndTime().isBefore(Instant.now())) {
            getEvent.setIsFinished(true);
        } else {
            getEvent.setIsFinished(false);
        }

        getEvent.setCurrentParticipants(eventParticipantsRepository.countByEventIdAndStatus(event.getId(), ParticipationStatus.registered));
        return getEvent;

    }

    public GetEvents GetEvents(String authId){
        User user = userRepository.findByAuthId(authId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals(Role.admin)) {
            List<Event> events = eventRepository.findAllEventsSorted();
            List<GetEvent> getEvents = new ArrayList<>();
            for (Event event : events) {
                getEvents.add(makeGetEvent(event, user));
            }
            return new GetEvents(getEvents);

        }
        else{
            List<Event> events = eventRepository.findSomeEventsSorted();
            List<GetEvent> getEvents = new ArrayList<>();
            for (Event event : events) {
                if (event.getStatus().equals(EventStatus.approved) || event.getStatus().equals(EventStatus.pending)) {
                    getEvents.add(makeGetEvent(event, user));
                }
            }
            return new GetEvents(getEvents);
        }
    }

    private GetUser makeGetUser(User user) {
        if (user == null) {
            return null;
        }
        GetUser getUser = new GetUser();
        getUser.setId(user.getId());
        getUser.setName(user.getName());
        return getUser;
    }

    public AdminGetEventDetails AdminGetEventDetails(UUID id){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        AdminGetEventDetails getEventDetails = new AdminGetEventDetails();

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

        List<User> participants = eventParticipantsRepository.findByIdAndStatus(id, ParticipationStatus.registered);
        List<GetParticipant> getParticipants = new ArrayList<>();
        for (User participant : participants) {
            GetParticipant getParticipant = new GetParticipant();
            getParticipant.setParticipationStatus(ParticipationStatus.registered);
            getParticipant.setName(participant.getName());
            getParticipant.setUserId(participant.getId());
            getParticipants.add(getParticipant);
        }
        getEventDetails.setParticipants(getParticipants);
        return getEventDetails;
    }

    public UserGetEventDetails UserGetEventDetails(UUID id, User user){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        UserGetEventDetails getEventDetails = new UserGetEventDetails();

        getEventDetails.setId(event.getId());
        getEventDetails.setTitle(event.getTitle());
        getEventDetails.setDescription(event.getDescription());
        getEventDetails.setLocation(event.getLocation());
        getEventDetails.setStartTime(event.getStartTime());
        getEventDetails.setEndTime(event.getEndTime());
        getEventDetails.setStatus(event.getStatus());
        getEventDetails.setMaxParticipants(event.getMaxParticipants());

        Optional<EventParticipants> eventParticipants = eventParticipantsRepository.findByEventIdAndUserId(event.getId(), user.getId());
        if (eventParticipants.isPresent()){
            getEventDetails.setMyParticipationStatus(eventParticipants.get().getStatus());
        }
        else{
            getEventDetails.setMyParticipationStatus(null);
        }

        User creator = event.getCreator();
        getEventDetails.setCreator(makeGetUser(creator));

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


    public ResponseEntity<?> GetEventDetails(UUID id, String authId){
        User user = userRepository.findByAuthId(authId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals(Role.student)) {
           return ResponseEntity.ok(UserGetEventDetails(id, user));
        }
        else{
            return ResponseEntity.ok(AdminGetEventDetails(id));
        }
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

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        event.setCategory(category);

        if (creator.getRole().name().equals("ADMIN")) {
            event.setModeratedBy(creator);
            event.setStatus(EventStatus.approved);
        }
        else{
            event.setStatus(EventStatus.pending);
        }
        event = eventRepository.saveAndFlush(event);
        entityManager.refresh(event);

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
        if (newMaxParticipants != null){
            if (currentParticipants <= newMaxParticipants){
                event.setMaxParticipants(newMaxParticipants);
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Max participants cannot be less than current " + currentParticipants);
            }
        }

        if (newCategoryId != null){
            event.setCategory(categoryRepository.findById(newCategoryId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")));
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
            event = eventRepository.saveAndFlush(event);
            entityManager.refresh(event);

            PatchEventResponse patchEventResponse = new PatchEventResponse();
            patchEventResponse.setEventId(eventId);
            patchEventResponse.setUpdatedAt(event.getUpdatedAt());
            return  patchEventResponse;
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event is unavailable for patching");
        }
    }

    public DeleteEventResponse DeleteEvent(UUID eventId, String authId){
        User user = userRepository.findByAuthId(authId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        if (user.getRole().name().equals("student") && !event.getCreator().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to delete this event");
        }
        event.setStatus(EventStatus.cancelled);
        event = eventRepository.saveAndFlush(event);
        entityManager.refresh(event);

        DeleteEventResponse deleteEventResponse = new DeleteEventResponse();
        deleteEventResponse.setEventId(eventId);
        deleteEventResponse.setUpdatedAt(event.getUpdatedAt());
        deleteEventResponse.setStatus(EventStatus.cancelled);
        return  deleteEventResponse;
    }

    public SignUpOrDeleteResponse SignUp(UUID eventId, String authId, ParticipationStatus status){
        User user = userRepository.findByAuthId(authId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().name().equals("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only student can leave sign up");
        }
        if (status.equals(ParticipationStatus.registered) && eventParticipantsRepository.existsByUserIdAndEventId(user.getId(), eventId)){
            EventParticipants eventParticipants = eventParticipantsRepository.findByEventIdAndUserId(eventId, user.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sign not found"));
            eventParticipants.setStatus(status);
            eventParticipantsRepository.save(eventParticipants);
        }
        else{
            EventParticipants eventParticipants = new EventParticipants();
            eventParticipants.setStatus(status);
            eventParticipants.setUser(user);
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
            eventParticipants.setEvent(event);
            eventParticipantsRepository.save(eventParticipants);
        }
        SignUpOrDeleteResponse signUpOrDeleteResponse = new SignUpOrDeleteResponse();
        signUpOrDeleteResponse.setEventId(eventId);
        signUpOrDeleteResponse.setUpdatedAt(Instant.now());
        signUpOrDeleteResponse.setStatus(status);
        signUpOrDeleteResponse.setUserId(user.getId());
        return signUpOrDeleteResponse;
    }

    public SignUpOrDeleteResponse DeleteParticipants(UUID eventId, String authId){
        User user = userRepository.findByAuthId(authId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().name().equals("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only student can delete sign up");
        }
        EventParticipants eventParticipants = eventParticipantsRepository.findByEventIdAndUserId(eventId, user.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sign not found"));
        eventParticipantsRepository.delete(eventParticipants);
        SignUpOrDeleteResponse signUpOrDeleteResponse = new SignUpOrDeleteResponse();
        signUpOrDeleteResponse.setEventId(eventId);
        signUpOrDeleteResponse.setUserId(user.getId());
        signUpOrDeleteResponse.setStatus(ParticipationStatus.cancelled);
        signUpOrDeleteResponse.setUpdatedAt(Instant.now());
        return signUpOrDeleteResponse;
    }








}
