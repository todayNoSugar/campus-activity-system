package com.activity.service;

import com.activity.dto.ActivityDTO;
import com.activity.vo.ActivityVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface ActivityService {

    /** 学生端：已发布活动分页 */
    IPage<ActivityVO> studentList(String keyword, Long categoryId, long page, long size, Long userId);

    /** 活动详情 */
    ActivityVO detail(Long id, Long userId);

    /** 管理端：全量活动分页 */
    IPage<ActivityVO> adminList(String keyword, Long categoryId, long page, long size);

    void create(ActivityDTO dto);

    void update(Long id, ActivityDTO dto);

    void updateStatus(Long id, Integer status);

    void delete(Long id);
}
