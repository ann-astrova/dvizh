package com.example.demo.dto;

import com.example.demo.enums.Role;

// Тело PATCH /admin/users/{id}/role (эндпоинт 3).
public record UpdateRoleRequest(Role role) {}
