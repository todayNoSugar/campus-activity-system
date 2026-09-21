package com.activity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动列表/详情视图：活动信息 + 分类名 + 已报名人数 + 当前用户是否已报名
 */
@Data
public class ActivityVO {

    private Long id;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signupDeadline;

    private Integer maxPeople;
    private String intro;
    private String detail;
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 已报名人数（状态为待审核/已通过） */
    private Integer signedCount;

    /** 当前用户是否已报名 */
    private Boolean signedUp;

    /** 剩余名额 */
    public Integer getRemainCount() {
        if (maxPeople == null || maxPeople <= 0) {
            return null; // 不限制人数
        }
        int signed = signedCount == null ? 0 : signedCount;
        return Math.max(maxPeople - signed, 0);
    }
}
