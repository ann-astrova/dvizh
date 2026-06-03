package com.example.demo.repository;

import com.example.demo.dto.MyCreatedEventItem;
import com.example.demo.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("""
            SELECT new com.example.demo.dto.MyCreatedEventItem(
                e.id, e.title, e.status.name(), e.createdAt)
            FROM Event e
            WHERE e.creator.id = :userId
            """)
    List<MyCreatedEventItem> findMyCreatedEventItemsByCreatorId(@Param("userId") UUID userId);
}
