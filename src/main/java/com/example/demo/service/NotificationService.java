package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserService userService;

    // Эндпоинт 16: GET /notifications — уведомления текущего пользователя, новые сверху.
    public List<Notification> getMyNotifications() {
        User current = userService.getOrCreateCurrentUser();
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(current.getId());
    }

    // Эндпоинт 17: PATCH /notifications/{id}/read — отметить уведомление прочитанным.
    // Чужие уведомления отмечать нельзя.
    @Transactional
    public Notification markAsRead(UUID notificationId) {
        User current = userService.getOrCreateCurrentUser();
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Уведомление не найдено"));

        if (n.getUser() == null || !n.getUser().getId().equals(current.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Это не ваше уведомление");
        }

        // isRead в сущности — строка "true"/"false" (так задаёт AdminService.buildNotification).
        n.setIsRead("true");
        return notificationRepository.save(n);
    }
}
