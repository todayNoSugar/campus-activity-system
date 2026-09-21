package com.activity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报名记录视图：报名信息 + 活动信息 + 用户信息（学生端/管理端共用）
 */
@Data
public class SignupVO {

    private Long id;
    private Long activityId;
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signupTime;

    /** 审核状态：0 待审核 1 已通过 2 已驳回 */
    private Integer status;

    private String remark;

    // 活动信息
    private String activityTitle;
    private String activityLocation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activityStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activityEndTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime activitySignupDeadline;

    private String categoryName;

    // 用户信息
    private String username;
    private String realName;
    private String phone;
}
