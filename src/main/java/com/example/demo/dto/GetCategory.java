package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GetCategory {
    private UUID id;
    private String name;
}
