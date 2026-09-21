package com.activity.mapper;

import com.activity.entity.ActivitySignup;
import com.activity.vo.SignupVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ActivitySignupMapper extends BaseMapper<ActivitySignup> {

    /**
     * 学生端：我的报名记录（含活动信息）
     */
    @Select("SELECT s.*, a.title AS activityTitle, a.location AS activityLocation, " +
            "a.start_time AS activityStartTime, a.end_time AS activityEndTime, " +
            "a.signup_deadline AS activitySignupDeadline, c.name AS categoryName " +
            "FROM activity_signup s " +
            "JOIN activity_info a ON s.activity_id = a.id " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "WHERE s.user_id = #{userId} " +
            "ORDER BY s.signup_time DESC")
    IPage<SignupVO> selectMyPage(IPage<SignupVO> page, @Param("userId") Long userId);

    /**
     * 管理端：全量报名记录（含活动、用户信息），支持按活动/状态/关键字筛选
     */
    @Select("<script>" +
            "SELECT s.*, a.title AS activityTitle, a.location AS activityLocation, " +
            "a.start_time AS activityStartTime, a.end_time AS activityEndTime, " +
            "a.signup_deadline AS activitySignupDeadline, " +
            "u.username, u.real_name AS realName, u.phone " +
            "FROM activity_signup s " +
            "JOIN activity_info a ON s.activity_id = a.id " +
            "JOIN sys_user u ON s.user_id = u.id " +
            "WHERE 1 = 1 " +
            "<if test='activityId != null'>AND s.activity_id = #{activityId} </if>" +
            "<if test='status != null'>AND s.status = #{status} </if>" +
            "<if test='keyword != null and keyword != \"\"'>AND (a.title LIKE CONCAT('%', #{keyword}, '%') " +
            "OR u.real_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR u.username LIKE CONCAT('%', #{keyword}, '%')) </if>" +
            "ORDER BY s.signup_time DESC" +
            "</script>")
    IPage<SignupVO> selectAdminPage(IPage<SignupVO> page,
                                    @Param("activityId") Long activityId,
                                    @Param("status") Integer status,
                                    @Param("keyword") String keyword);

    /**
     * 统计某活动已占用名额（待审核 + 已通过）
     */
    @Select("SELECT COUNT(*) FROM activity_signup WHERE activity_id = #{activityId} AND status IN (0, 1)")
    Long countOccupied(@Param("activityId") Long activityId);
}
