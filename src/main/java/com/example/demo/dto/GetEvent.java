package com.example.demo.dto;

public class GetEvent {
    private String id;
    private String title;

            "description": "Backend competition",
            "location": "Amsterdam",

            "category": {
        "id": "uuid-cat",
                "name": "IT"
    },

            "start_time": "2026-05-25T10:00:00Z",
            "end_time": "2026-05-25T18:00:00Z",

            "status": "approved",

            "max_participants": 50,
            "current_participants": 32,

            "is_finished": false,

            "creator": {
        "id": "uuid-user",
                "name": "Ivan"


    }
