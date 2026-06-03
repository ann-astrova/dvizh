package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    @Column(name = "notification_id", nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne
    private User user;

    @Column(name = "type", nullable = false, columnDefinition = "TEXT")
    private String type;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_read", nullable = false, columnDefinition = "TEXT")
    private String isRead;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMPTZ")
    @CreationTimestamp
    private Instant createdAt;
}
