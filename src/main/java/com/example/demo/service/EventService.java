package com.example.demo.service;

import com.example.demo.dto.GetCategory;
import com.example.demo.dto.GetEvent;
import com.example.demo.dto.GetEvents;
import com.example.demo.dto.GetUser;
import com.example.demo.entity.Category;
import com.example.demo.entity.Event;
import com.example.demo.entity.User;
import com.example.demo.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public GetEvents GetEvents(){
        List<Event> events = eventRepository.findAll();
        GetEvents getEvents = new GetEvents();
        for (int i = 0; i < events.size(); i++){
            Event event = events.get(i);
            GetEvent getEvent = new GetEvent();
            GetUser getUser = new GetUser();
            GetCategory getCategory = new GetCategory();
            getEvent.setId(event.getId());
            getEvent.setTitle(event.getTitle());
            getEvent.setDescription(event.getDescription());
            getEvent.setLocation(event.getLocation());
            getEvent.setStartTime(event.getStartTime());
            getEvent.setEndTime(event.getEndTime());
            getEvent.setStatus(event.getStatus());
            getEvent.setMaxParticipants(event.getMaxParticipants());

            User user = event.getCreator();
            getUser.setId(user.getId());
            getUser.setName(user.getName());
            getEvent.setCreator(getUser);

            Category category = event.getCategory();
            getCategory.setId(category.getId());
            getEvent.setCategory(getCategory);
            getEvent.setCategory(getCategory);

            if (event.getEndTime().isBefore(Instant.now())){
                getEvent.setIsFinished(true);
            }
            else{
                getEvent.setIsFinished(false);
            }

        }
        return;
    }
}
