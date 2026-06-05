package com.example.demo.dto;

// Тело PATCH /me (эндпоинт 2).
public record UpdateProfileRequest(String name, String description) {}
