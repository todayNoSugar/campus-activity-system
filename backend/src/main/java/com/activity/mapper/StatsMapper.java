package com.activity.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 后台数据概览统计
 */
public interface StatsMapper {

    @Select("SELECT COUNT(*) FROM sys_user WHERE role = 0")
    Long countStudents();

    @Select("SELECT COUNT(*) FROM activity_info")
    Long countActivities();

    @Select("SELECT COUNT(*) FROM activity_signup")
    Long countSignups();

    @Select("SELECT COUNT(*) FROM activity_info WHERE status = 1 AND start_time > NOW()")
    Long countOngoing();

    @Select("SELECT COUNT(*) FROM activity_signup WHERE status = 0")
    Long countPending();

    @Select("SELECT c.name AS name, COUNT(a.id) AS value FROM activity_category c " +
            "LEFT JOIN activity_info a ON a.category_id = c.id " +
            "GROUP BY c.id, c.name ORDER BY c.id")
    List<Map<String, Object>> categoryDistribution();
}
