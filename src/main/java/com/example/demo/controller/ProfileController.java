package com.example.demo.controller;

import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.UpdateProfileResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    @Autowired
    private UserService userService;

    // Эндпоинт 1: GET /me — текущий пользователь (создаётся при первом входе).
    @GetMapping("/me")
    public UserResponse getMe() {
        User user = userService.getOrCreateCurrentUser();
        return UserResponse.from(user);
    }

    // Эндпоинт 2: PATCH /me — обновить имя/описание.
    @PatchMapping("/me")
    public UpdateProfileResponse updateMe(@RequestBody UpdateProfileRequest req) {
        User updated = userService.updateProfile(req);
        return UpdateProfileResponse.from(updated);
    }
}
