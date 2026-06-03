package com.example.demo.repository;

import com.example.demo.dto.MyEventItem;
import com.example.demo.entity.EventParticipants;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventParticipantsRepository extends Repository<EventParticipants, UUID> {

    @Query("""
            SELECT new com.example.demo.dto.MyEventItem(
                e.id, e.title, e.status.name(), ep.status.name())
            FROM EventParticipants ep
            JOIN ep.event e
            WHERE ep.user.id = :userId
            """)
    List<MyEventItem> findMyEventItemsByUserId(@Param("userId") UUID userId);
}
