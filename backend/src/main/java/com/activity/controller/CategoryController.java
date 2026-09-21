package com.activity.controller;

import com.activity.common.Result;
import com.activity.dto.CategoryDTO;
import com.activity.entity.ActivityCategory;
import com.activity.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 活动分类：学生端查看列表，管理端增删改
 */
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/list")
    public Result<List<ActivityCategory>> list() {
        return Result.ok(categoryService.list());
    }

    @PostMapping("/admin/category")
    public Result<Void> add(@Valid @RequestBody CategoryDTO dto) {
        categoryService.add(dto);
        return Result.ok();
    }

    @PutMapping("/admin/category/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        categoryService.update(id, dto);
        return Result.ok();
    }

    @DeleteMapping("/admin/category/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.ok();
    }
}
