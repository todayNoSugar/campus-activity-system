package com.activity.service;

import com.activity.dto.CategoryDTO;
import com.activity.entity.ActivityCategory;

import java.util.List;

public interface CategoryService {

    List<ActivityCategory> list();

    void add(CategoryDTO dto);

    void update(Long id, CategoryDTO dto);

    void delete(Long id);
}
