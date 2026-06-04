package com.example.demo.controller;

import com.example.demo.dto.MyAchievementsResponse;
import com.example.demo.dto.MyCreatedEventsResponse;
import com.example.demo.dto.MyEventsResponse;
import com.example.demo.service.MeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class MeController {

    @Autowired
    private MeService meService;

    @GetMapping("/events")
    public MyEventsResponse getMyEvents(@AuthenticationPrincipal AppUserPrincipal currentUser) {
        return meService.getMyEvents(currentUser.userId());
    }

    @GetMapping("/created-events")
    public MyCreatedEventsResponse getMyCreatedEvents(@AuthenticationPrincipal AppUserPrincipal currentUser) {
        return meService.getMyCreatedEvents(currentUser.userId());
    }

    @GetMapping("/achievements")
    public MyAchievementsResponse getMyAchievements(@AuthenticationPrincipal AppUserPrincipal currentUser) {
        return meService.getMyAchievements(currentUser.userId());
    }
}
