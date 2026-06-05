package com.example.demo.dto;

import java.util.List;

// Обёртка ответа GET /admin/users: { "users": [...] }.
public record UsersListResponse(List<UserListItem> users) {}
