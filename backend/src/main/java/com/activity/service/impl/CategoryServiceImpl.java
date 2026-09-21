package com.activity.service.impl;

import com.activity.common.BusinessException;
import com.activity.dto.CategoryDTO;
import com.activity.entity.ActivityCategory;
import com.activity.entity.ActivityInfo;
import com.activity.mapper.ActivityCategoryMapper;
import com.activity.mapper.ActivityInfoMapper;
import com.activity.service.CategoryService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ActivityCategoryMapper categoryMapper;

    @Autowired
    private ActivityInfoMapper activityInfoMapper;

    @Override
    public List<ActivityCategory> list() {
        return categoryMapper.selectList(
                Wrappers.<ActivityCategory>lambdaQuery().orderByAsc(ActivityCategory::getId));
    }

    @Override
    public void add(CategoryDTO dto) {
        checkNameUnique(null, dto.getName());
        ActivityCategory category = new ActivityCategory();
        category.setName(dto.getName());
        categoryMapper.insert(category);
    }

    @Override
    public void update(Long id, CategoryDTO dto) {
        if (categoryMapper.selectById(id) == null) {
            throw new BusinessException("分类不存在");
        }
        checkNameUnique(id, dto.getName());
        ActivityCategory category = new ActivityCategory();
        category.setId(id);
        category.setName(dto.getName());
        categoryMapper.updateById(category);
    }

    @Override
    public void delete(Long id) {
        if (categoryMapper.selectById(id) == null) {
            throw new BusinessException("分类不存在");
        }
        Long count = activityInfoMapper.selectCount(
                Wrappers.<ActivityInfo>lambdaQuery().eq(ActivityInfo::getCategoryId, id));
        if (count != null && count > 0) {
            throw new BusinessException("该分类下存在活动，无法删除");
        }
        categoryMapper.deleteById(id);
    }

    private void checkNameUnique(Long excludeId, String name) {
        Long count = categoryMapper.selectCount(
                Wrappers.<ActivityCategory>lambdaQuery()
                        .eq(ActivityCategory::getName, name)
                        .ne(excludeId != null, ActivityCategory::getId, excludeId));
        if (count != null && count > 0) {
            throw new BusinessException("分类名称已存在");
        }
    }
}
