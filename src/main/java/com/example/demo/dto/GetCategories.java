package com.example.demo.dto;

import com.example.demo.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCategories {
    private List<GetCategory> categories;
}
