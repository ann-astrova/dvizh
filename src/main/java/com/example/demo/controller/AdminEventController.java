package com.example.demo.controller;

import com.example.demo.dto.AttendanceConfirmationResponse;
import com.example.demo.dto.EventModerationResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('admin')") // check if user is admin
public class AdminEventController {

    @PostMapping("/events/{id}/approve")
    public EventModerationResponse approve(@PathVariable UUID id) { // event id
        // TODO
        return new EventModerationResponse(
                id,
                "approved",
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                Instant.parse("2025-03-16T16:20:00Z")
        );
    }

    @PostMapping("/events/{id}/reject")
    public EventModerationResponse reject(@PathVariable UUID id) { // event id
        // TODO
        return new EventModerationResponse(
                id,
                "rejected",
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                Instant.parse("2025-03-16T16:20:00Z")
        );
    }

    @PostMapping("/events/{id}/attendance/{userId}")
    public AttendanceConfirmationResponse confirmAttendance(
            @PathVariable UUID id, // event id
            @PathVariable UUID userId // user id
    ) {
        // TODO
        return new AttendanceConfirmationResponse(
                id,
                userId,
                "attended",
                Instant.parse("2025-03-16T18:00:00Z")
        );
    }
}
