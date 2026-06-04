package com.example.demo.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class GetCategory {
    private UUID id;
    private String name;
}
