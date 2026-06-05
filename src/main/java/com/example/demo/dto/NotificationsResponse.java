package com.example.demo.dto;

import java.util.List;

// Обёртка ответа GET /notifications: { "notifications": [...] }.
public record NotificationsResponse(List<NotificationItem> notifications) {}
