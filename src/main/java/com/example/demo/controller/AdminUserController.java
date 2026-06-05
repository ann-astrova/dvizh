package com.example.demo.controller;

import com.example.demo.dto.UpdateRoleRequest;
import com.example.demo.dto.UpdateRoleResponse;
import com.example.demo.dto.UserListItem;
import com.example.demo.dto.UsersListResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    // Эндпоинт 3: PATCH /admin/users/{id}/role
    @PatchMapping("/{id}/role")
    public UpdateRoleResponse updateRole(@PathVariable("id") UUID id,
                                         @RequestBody UpdateRoleRequest req) {
        User updated = userService.updateRole(id, req);
        return UpdateRoleResponse.from(updated);
    }

    // Эндпоинт 19: GET /admin/users
    @GetMapping
    public UsersListResponse getAllUsers() {
        List<UserListItem> items = userService.getAllUsers().stream()
                .map(UserListItem::from)
                .toList();
        return new UsersListResponse(items);
    }
}
