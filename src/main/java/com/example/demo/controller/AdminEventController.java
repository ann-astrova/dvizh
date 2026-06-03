package com.example.demo.controller;

import com.example.demo.config.AppUserPrincipal;
import com.example.demo.dto.AttendanceConfirmationResponse;
import com.example.demo.dto.EventModerationResponse;
import com.example.demo.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('admin')")
public class AdminEventController {

    private final AdminService adminService;

    public AdminEventController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/events/{id}/approve")
    public EventModerationResponse approve(
            @PathVariable UUID id,
            @AuthenticationPrincipal AppUserPrincipal currentAdmin
    ) {
        return adminService.approve(id, currentAdmin.userId());
    }

    @PostMapping("/events/{id}/reject")
    public EventModerationResponse reject(@PathVariable UUID id) {
        // TODO: adminService.reject(id, currentAdmin.userId())
        return new EventModerationResponse(
                id,
                "rejected",
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                Instant.parse("2025-03-16T16:20:00Z")
        );
    }

    @PostMapping("/events/{id}/attendance/{userId}")
    public AttendanceConfirmationResponse confirmAttendance(
            @PathVariable UUID id,
            @PathVariable UUID userId
    ) {
        // TODO: attendanceService.confirm(id, userId, currentAdmin.userId())
        return new AttendanceConfirmationResponse(
                id,
                userId,
                "attended",
                Instant.parse("2025-03-16T18:00:00Z")
        );
    }
}
