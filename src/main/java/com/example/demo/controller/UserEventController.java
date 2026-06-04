package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.security.DemoUserDetails;
import com.example.demo.service.AdminService;
import com.example.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/events")
public class UserEventController {
    @Autowired
    private EventService eventService;

    @GetMapping("")
    public ResponseEntity<?> getEvents() {
        GetEvents getEvents = eventService.GetEvents();
        return ResponseEntity.ok().body(getEvents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventDetail(@PathVariable UUID id) {
        GetEventDetails getEventDetails = eventService.GetEventDetails(id);
        return ResponseEntity.ok().body(getEventDetails);
    }

    @PostMapping("")
    public ResponseEntity<?> postEvent(@RequestBody CreateEventRequest createEventRequest,
                                       @AuthenticationPrincipal DemoUserDetails userDetails) {
        CreateEventResponse  createEventResponse = eventService.CreateEvent(createEventRequest, userDetails.getAuthId());
        return new ResponseEntity<>(createEventResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchEvent(@RequestBody PatchEventRequest patchEventRequest,
                                       @AuthenticationPrincipal DemoUserDetails userDetails, @PathVariable UUID id) {
        PatchEventResponse patchEventResponse = eventService.PatchEvent(patchEventRequest, userDetails.getAuthId(), id);
        return ResponseEntity.ok().body(patchEventResponse);
    }





}
