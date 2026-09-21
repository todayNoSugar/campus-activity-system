package com.activity.mapper;

import com.activity.entity.ActivityInfo;
import com.activity.vo.ActivityVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {

    /**
     * 分页查询活动（学生端：onlyPublished=true 只看已发布；管理端：查看全部）
     * signedUp：仅当传入当前 userId 时有效
     */
    @Select("<script>" +
            "SELECT a.*, c.name AS categoryName, " +
            "  (SELECT COUNT(*) FROM activity_signup s WHERE s.activity_id = a.id AND s.status IN (0,1)) AS signedCount, " +
            "  (SELECT COUNT(*) FROM activity_signup s2 WHERE s2.activity_id = a.id AND s2.user_id = #{userId}) > 0 AS signedUp " +
            "FROM activity_info a " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "WHERE 1 = 1 " +
            "<if test='onlyPublished'>AND a.status = 1 </if>" +
            "<if test='keyword != null and keyword != \"\"'>AND a.title LIKE CONCAT('%', #{keyword}, '%') </if>" +
            "<if test='categoryId != null'>AND a.category_id = #{categoryId} </if>" +
            "ORDER BY a.create_time DESC" +
            "</script>")
    IPage<ActivityVO> selectActivityPage(IPage<ActivityVO> page,
                                         @Param("keyword") String keyword,
                                         @Param("categoryId") Long categoryId,
                                         @Param("onlyPublished") boolean onlyPublished,
                                         @Param("userId") Long userId);

    /**
     * 活动详情（含分类名、已报名人数、当前用户是否已报名）
     */
    @Select("<script>" +
            "SELECT a.*, c.name AS categoryName, " +
            "  (SELECT COUNT(*) FROM activity_signup s WHERE s.activity_id = a.id AND s.status IN (0,1)) AS signedCount, " +
            "  (SELECT COUNT(*) FROM activity_signup s2 WHERE s2.activity_id = a.id AND s2.user_id = #{userId}) > 0 AS signedUp " +
            "FROM activity_info a " +
            "LEFT JOIN activity_category c ON a.category_id = c.id " +
            "WHERE a.id = #{id}" +
            "</script>")
    ActivityVO selectActivityDetail(@Param("id") Long id, @Param("userId") Long userId);
}
