package com.activity.service.impl;

import com.activity.common.BusinessException;
import com.activity.dto.AuditDTO;
import com.activity.entity.ActivityInfo;
import com.activity.entity.ActivitySignup;
import com.activity.mapper.ActivityInfoMapper;
import com.activity.mapper.ActivitySignupMapper;
import com.activity.service.SignupService;
import com.activity.vo.SignupVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SignupServiceImpl implements SignupService {

    @Autowired
    private ActivitySignupMapper signupMapper;

    @Autowired
    private ActivityInfoMapper activityInfoMapper;

    @Override
    public void signup(Long userId, Long activityId) {
        ActivityInfo activity = activityInfoMapper.selectById(activityId);
        if (activity == null || activity.getStatus() == null || activity.getStatus() != 1) {
            throw new BusinessException("活动不存在或已下架");
        }
        if (LocalDateTime.now().isAfter(activity.getSignupDeadline())) {
            throw new BusinessException("报名已截止");
        }
        Long occupied = signupMapper.countOccupied(activityId);
        if (occupied != null && activity.getMaxPeople() != null
                && occupied >= activity.getMaxPeople()) {
            throw new BusinessException("名额已满");
        }
        Long exists = signupMapper.selectCount(
                Wrappers.<ActivitySignup>lambdaQuery()
                        .eq(ActivitySignup::getActivityId, activityId)
                        .eq(ActivitySignup::getUserId, userId));
        if (exists != null && exists > 0) {
            throw new BusinessException("请勿重复报名");
        }
        ActivitySignup signup = new ActivitySignup();
        signup.setActivityId(activityId);
        signup.setUserId(userId);
        signup.setStatus(0); // 待审核
        signupMapper.insert(signup);
    }

    @Override
    public void cancel(Long userId, Long activityId) {
        ActivitySignup signup = signupMapper.selectOne(
                Wrappers.<ActivitySignup>lambdaQuery()
                        .eq(ActivitySignup::getActivityId, activityId)
                        .eq(ActivitySignup::getUserId, userId));
        if (signup == null) {
            throw new BusinessException("报名记录不存在");
        }
        ActivityInfo activity = activityInfoMapper.selectById(activityId);
        if (activity != null && LocalDateTime.now().isAfter(activity.getStartTime())) {
            throw new BusinessException("活动已开始，无法取消报名");
        }
        signupMapper.deleteById(signup.getId());
    }

    @Override
    public IPage<SignupVO> myPage(Long userId, long page, long size) {
        return signupMapper.selectMyPage(new Page<>(page, size), userId);
    }

    @Override
    public IPage<SignupVO> adminPage(Long activityId, Integer status, String keyword, long page, long size) {
        return signupMapper.selectAdminPage(new Page<>(page, size), activityId, status, keyword);
    }

    @Override
    public void audit(Long signupId, AuditDTO dto) {
        ActivitySignup signup = signupMapper.selectById(signupId);
        if (signup == null) {
            throw new BusinessException("报名记录不存在");
        }
        ActivitySignup update = new ActivitySignup();
        update.setId(signupId);
        update.setStatus(dto.getStatus());
        update.setRemark(dto.getRemark());
        signupMapper.updateById(update);
    }
}
