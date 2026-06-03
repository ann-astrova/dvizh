package com.example.demo.service;

import com.example.demo.dto.AttendanceConfirmationResponse;
import com.example.demo.dto.ConfirmAttendanceRequest;
import com.example.demo.dto.EventModerationResponse;
import com.example.demo.entity.Achievement;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventParticipants;
import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.entity.UserAchievements;
import com.example.demo.enums.EventStatus;
import com.example.demo.enums.ParticipationStatus;
import com.example.demo.repository.AchievementRepository;
import com.example.demo.repository.EventParticipantsRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserAchievementsRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
public class AdminService { // admin service

    // repositories
    private final EventRepository eventRepository;
    private final EventParticipantsRepository eventParticipantsRepository;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementsRepository userAchievementsRepository;
    private final NotificationRepository notificationRepository;
    private final EntityManager entityManager;

    public AdminService( // constructor
            EventRepository eventRepository,
            EventParticipantsRepository eventParticipantsRepository,
            UserRepository userRepository,
            AchievementRepository achievementRepository,
            UserAchievementsRepository userAchievementsRepository,
            NotificationRepository notificationRepository,
            EntityManager entityManager
    ) {
        this.eventRepository = eventRepository;
        this.eventParticipantsRepository = eventParticipantsRepository;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.userAchievementsRepository = userAchievementsRepository;
        this.notificationRepository = notificationRepository;
        this.entityManager = entityManager;
    }

    // approve event
    @Transactional
    public EventModerationResponse approve(UUID eventId, UUID adminUserId) {
        // checking that the action is allowed
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getStatus() != EventStatus.pending) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending events can be approved");
        }

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));

        // approve event
        event.setStatus(EventStatus.approved);
        event.setModeratedBy(admin);
        Event saved = eventRepository.save(event);

        // check and grant achievements
        checkAndGrantCreatedEventAchievements(saved.getCreator());

        // return response
        return new EventModerationResponse(
                saved.getId(),
                saved.getStatus().name(),
                saved.getModeratedBy().getId(),
                saved.getUpdatedAt()
        );
    }

    // reject event
    public EventModerationResponse reject(UUID eventId, UUID adminUserId) {
        // checking that the action is allowed
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getStatus() != EventStatus.pending) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending events can be rejected");
        }

        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));

        // reject event
        event.setStatus(EventStatus.rejected);
        event.setModeratedBy(admin);
        Event saved = eventRepository.save(event);

        // return response
        return new EventModerationResponse(
                saved.getId(),
                saved.getStatus().name(),
                saved.getModeratedBy().getId(),
                saved.getUpdatedAt()
        );
    }

    // confirm attendance
    @Transactional // transaction to make a transaction in db
    public AttendanceConfirmationResponse confirmAttendance(
            UUID eventId,
            UUID userId,
            UUID adminUserId,
            ConfirmAttendanceRequest request
    ) {
        // checking that the action is allowed
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getStatus() != EventStatus.approved) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Attendance can only be confirmed for approved events"
            );
        }

        User admin = userRepository.findById(adminUserId) // find admin by id
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin user not found"));

        User student = userRepository.findById(userId) // find user by id
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        EventParticipants participation = eventParticipantsRepository // find participation by event id and user id
                .findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User is not registered for this event"
                ));

        ParticipationStatus currentStatus = participation.getStatus();
        if (currentStatus == ParticipationStatus.attended) { // check if attendance is already confirmed
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attendance already confirmed");
        }
        if (currentStatus != ParticipationStatus.registered && currentStatus != ParticipationStatus.planned) { // check if attendance is planned or registered
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Attendance can only be confirmed from planned or registered status"
            );
        }

        int rewardAmount = resolveParticipationReward(request);
        if (rewardAmount < 0) { // check if reward amount is not negative
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reward_amount must not be negative");
        }

        // confirm attendance
        Instant attendedAt = Instant.now();
        int updated = eventParticipantsRepository.markAsAttended(
                eventId,
                userId,
                admin,
                attendedAt,
                ParticipationStatus.attended
        );
        if (updated == 0) { // check if participation record is found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participation record not found");
        }

        // add reward to user balance
        if (rewardAmount > 0) { // check if reward amount is positive
            student.setBalance(student.getBalance() + rewardAmount);
            userRepository.save(student);
            notificationRepository.save(buildNotification( // build notification
                    student,
                    "currency",
                    "Начислено " + rewardAmount + " монет за посещение мероприятия"
            ));
        }

        // check and grant achievements
        checkAndGrantAttendanceAchievements(student);

        // return response
        return new AttendanceConfirmationResponse(
                eventId,
                userId,
                ParticipationStatus.attended.name(),
                attendedAt
        );
    }

    // resolve participation reward
    private static int resolveParticipationReward(ConfirmAttendanceRequest request) {
        if (request == null) {
            return 0;
        }
        return request.resolvedRewardAmount();
    }

    //TODO: переделать логику сверки с ачивками - должно брать условие ачивки из бд и сверять с ним, а не быть захардкожено
    private void checkAndGrantAttendanceAchievements(User user) {
        // count attended events
        long attendedCount = eventParticipantsRepository.countByUserIdAndStatus(
                user.getId(),
                ParticipationStatus.attended
        );

        if (attendedCount == 1) {
            tryAwardAchievementByTitle(user, "Первое мероприятие");
        }
        if (attendedCount >= 5) {
            tryAwardAchievementByTitle(user, "5 мероприятий");
        }
    }

    //TODO: переделать логику сверки с ачивками - должно брать условие ачивки из бд и сверять с ним, а не быть захардкожено
    private void checkAndGrantCreatedEventAchievements(User creator) {
        long approvedCreatedCount = eventRepository.countByCreatorIdAndStatus( // count approved created events
                creator.getId(),
                EventStatus.approved
        );

        if (approvedCreatedCount == 1) {
            tryAwardAchievementByTitle(creator, "Создал мероприятие");
        }
    }

    // try to award achievement by title
    private void tryAwardAchievementByTitle(User user, String achievementTitle) {
        try {
            // find achievement by title
            achievementRepository.findByTitle(achievementTitle)
                    .ifPresent(achievement -> awardAchievement(user, achievement.getId()));
        } catch (RuntimeException ignored) {
            // нет ачивки в БД или сбой при выдаче - не откатываем approve / confirmAttendance
        }
    }

    // award achievement
    private void awardAchievement(User user, UUID achievementId) {
        try {
            achievementRepository.findById(achievementId).ifPresent(achievement -> {
                // check if achievement is already awarded
                if (userAchievementsRepository.existsByUserIdAndAchievementId(user.getId(), achievementId)) {
                    return;
                }

                // award achievement
                UserAchievements userAchievement = new UserAchievements();
                userAchievement.setUser(user);
                userAchievement.setAchievement(achievement);
                userAchievement.setAwardedAt(Instant.now());
                entityManager.persist(userAchievement);

                // add reward to user balance
                user.setBalance(user.getBalance() + achievement.getRewardAmount());
                userRepository.save(user);

                // build notification
                notificationRepository.save(buildNotification(
                        user,
                        "achievement",
                        "Получено достижение: " + achievement.getTitle()
                ));
                // build notification
                notificationRepository.save(buildNotification(
                        user,
                        "currency",
                        "Начислено " + achievement.getRewardAmount() + " монет за достижение"
                ));
            });
        } catch (RuntimeException ignored) {
            // нет ачивки в БД или сбой при выдаче — не откатываем основную транзакцию
        }
    }

    // build notification
    private Notification buildNotification(User user, String type, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setMessage(message);
        notification.setIsRead("false");
        return notification; // return notification
    }
}
