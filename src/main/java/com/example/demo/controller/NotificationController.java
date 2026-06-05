package com.example.demo.controller;

import com.example.demo.dto.MarkNotificationReadResponse;
import com.example.demo.dto.NotificationItem;
import com.example.demo.dto.NotificationsResponse;
import com.example.demo.entity.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Эндпоинт 16: GET /notifications
    @GetMapping
    public NotificationsResponse getNotifications() {
        List<NotificationItem> items = notificationService.getMyNotifications().stream()
                .map(NotificationItem::from)
                .toList();
        return new NotificationsResponse(items);
    }

    // Эндпоинт 17: PATCH /notifications/{id}/read
    @PatchMapping("/{id}/read")
    public MarkNotificationReadResponse markRead(@PathVariable("id") UUID id) {
        Notification n = notificationService.markAsRead(id);
        // read_at в сущности не хранится — отдаём момент обработки запроса.
        return MarkNotificationReadResponse.from(n, Instant.now());
    }
}
