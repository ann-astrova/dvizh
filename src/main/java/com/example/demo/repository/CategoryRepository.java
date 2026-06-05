package com.example.demo.repository;

import com.example.demo.dto.GetCategory;
import com.example.demo.dto.MyCreatedEventItem;
import com.example.demo.entity.Category;
import com.example.demo.entity.EventParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {



    @Query("""
            SELECT new com.example.demo.dto.GetCategory(c.id, c.name)
            FROM Category c""")
    List<GetCategory> findAllCategory();
}
