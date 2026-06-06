package com.example.demo.controller;

import com.example.demo.dto.AttendanceConfirmationResponse;
import com.example.demo.dto.ConfirmAttendanceRequest;
import com.example.demo.dto.EventModerationResponse;
import com.example.demo.security.DemoUserDetails;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminEventController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/events/{id}/approve")
    public EventModerationResponse approve(
            @PathVariable UUID id,
            @AuthenticationPrincipal DemoUserDetails userDetails
    ) {
        return adminService.approve(id, userDetails.getAuthId());
    }

    @PostMapping("/events/{id}/reject")
    public EventModerationResponse reject(
            @PathVariable UUID id,
            @AuthenticationPrincipal DemoUserDetails userDetails
    ) {
        return adminService.reject(id, userDetails.getAuthId());
    }

    @PostMapping("/events/{id}/attendance/{userId}")
    public AttendanceConfirmationResponse confirmAttendance(
            @PathVariable UUID id,
            @PathVariable UUID userId,
            @RequestBody(required = false) ConfirmAttendanceRequest request,
            @AuthenticationPrincipal DemoUserDetails userDetails
    ) {
        return adminService.confirmAttendance(id, userId, userDetails.getAuthId(), request);
    }
}
