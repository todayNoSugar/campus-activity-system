package com.activity.service.impl;

import com.activity.common.BusinessException;
import com.activity.dto.ActivityDTO;
import com.activity.entity.ActivityInfo;
import com.activity.entity.ActivitySignup;
import com.activity.mapper.ActivityInfoMapper;
import com.activity.mapper.ActivitySignupMapper;
import com.activity.service.ActivityService;
import com.activity.vo.ActivityVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityInfoMapper activityInfoMapper;

    @Autowired
    private ActivitySignupMapper signupMapper;

    @Override
    public IPage<ActivityVO> studentList(String keyword, Long categoryId, long page, long size, Long userId) {
        return activityInfoMapper.selectActivityPage(
                new Page<>(page, size), keyword, categoryId, true, userId);
    }

    @Override
    public ActivityVO detail(Long id, Long userId) {
        // 完整业务实现代码请获取完整版源码
        return null;
    }

    @Override
    public IPage<ActivityVO> adminList(String keyword, Long categoryId, long page, long size) {
        return activityInfoMapper.selectActivityPage(
                new Page<>(page, size), keyword, categoryId, false, null);
    }

    @Override
    public void create(ActivityDTO dto) {
        checkTime(dto);
        ActivityInfo info = new ActivityInfo();
        BeanUtils.copyProperties(dto, info);
        info.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
        activityInfoMapper.insert(info);
    }

    @Override
    public void update(Long id, ActivityDTO dto) {
        if (activityInfoMapper.selectById(id) == null) {
            throw new BusinessException("活动不存在");
        }
        checkTime(dto);
        ActivityInfo info = new ActivityInfo();
        BeanUtils.copyProperties(dto, info);
        info.setId(id);
        if (dto.getStatus() == null) {
            info.setStatus(null); // 保持原发布状态
        }
        activityInfoMapper.updateById(info);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if (activityInfoMapper.selectById(id) == null) {
            throw new BusinessException("活动不存在");
        }
        ActivityInfo info = new ActivityInfo();
        info.setId(id);
        info.setStatus(status);
        activityInfoMapper.updateById(info);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (activityInfoMapper.selectById(id) == null) {
            throw new BusinessException("活动不存在");
        }
        // 级联删除该活动的报名记录
        signupMapper.delete(Wrappers.<ActivitySignup>lambdaQuery()
                .eq(ActivitySignup::getActivityId, id));
        activityInfoMapper.deleteById(id);
    }

    private void checkTime(ActivityDTO dto) {
        if (dto.getSignupDeadline().isAfter(dto.getStartTime())) {
            throw new BusinessException("报名截止时间必须早于活动开始时间");
        }
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new BusinessException("活动开始时间必须早于结束时间");
        }
    }
}
