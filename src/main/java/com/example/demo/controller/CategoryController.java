package com.example.demo.controller;

import com.example.demo.dto.GetCategories;
import com.example.demo.dto.GetEvents;
import com.example.demo.service.CategoryService;
import com.example.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<?> getCategories() {
        GetCategories getCategories = categoryService.getCategories();
        return ResponseEntity.ok().body(getCategories);
    }
}
