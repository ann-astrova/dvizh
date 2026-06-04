package com.example.demo.service;

import com.example.demo.dto.GetCategories;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    public GetCategories getCategories(){
        GetCategories getCategories = new GetCategories();
        getCategories.setCategories(categoryRepository.findAllCategory());
        return getCategories;
    }
}
