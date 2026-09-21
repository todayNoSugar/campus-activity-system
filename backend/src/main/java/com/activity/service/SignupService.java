package com.activity.service;

import com.activity.dto.AuditDTO;
import com.activity.vo.SignupVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface SignupService {

    /** 学生报名 */
    void signup(Long userId, Long activityId);

    /** 学生取消报名 */
    void cancel(Long userId, Long activityId);

    /** 我的报名记录 */
    IPage<SignupVO> myPage(Long userId, long page, long size);

    /** 管理端报名记录 */
    IPage<SignupVO> adminPage(Long activityId, Integer status, String keyword, long page, long size);

    /** 审核报名（通过/驳回） */
    void audit(Long signupId, AuditDTO dto);
}
