package com.example.demo.repository;

import com.example.demo.entity.Category;
import com.example.demo.entity.EventParticipants;
import org.springframework.data.repository.Repository;

import java.util.UUID;

public interface CategoryRepository extends Repository<Category, UUID> {

    Category findById(UUID id);
}
