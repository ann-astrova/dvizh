package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id", nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;
}
