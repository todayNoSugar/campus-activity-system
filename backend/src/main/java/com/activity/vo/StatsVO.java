package com.activity.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 后台数据概览
 */
@Data
public class StatsVO {

    /** 学生总数 */
    private Long totalUsers;

    /** 活动总数 */
    private Long totalActivities;

    /** 报名总数 */
    private Long totalSignups;

    /** 进行中活动数（已发布且未开始/进行中） */
    private Long ongoingActivities;

    /** 待审核报名数 */
    private Long pendingSignups;

    /** 各分类活动数量分布 */
    private List<Map<String, Object>> categoryDistribution;
}
