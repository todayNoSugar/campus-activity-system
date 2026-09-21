package com.activity.controller;

import com.activity.common.RequestUser;
import com.activity.common.Result;
import com.activity.dto.ActivityDTO;
import com.activity.service.ActivityService;
import com.activity.vo.ActivityVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 活动：学生端浏览/详情，管理端增删改查
 */
@RestController
@RequestMapping("/api")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /** 学生端：已发布活动分页（支持名称模糊搜索、分类筛选） */
    @GetMapping("/activity/list")
    public Result<IPage<ActivityVO>> studentList(@RequestParam(required = false) String keyword,
                                                 @RequestParam(required = false) Long categoryId,
                                                 @RequestParam(defaultValue = "1") long page,
                                                 @RequestParam(defaultValue = "9") long size,
                                                 HttpServletRequest request) {
        return Result.ok(activityService.studentList(keyword, categoryId, page, size,
                RequestUser.id(request)));
    }

    /** 活动详情 */
    @GetMapping("/activity/{id}")
    public Result<ActivityVO> detail(@PathVariable Long id, HttpServletRequest request) {
        return Result.ok(activityService.detail(id, RequestUser.id(request)));
    }

    /** 管理端：全量活动分页 */
    @GetMapping("/admin/activity/list")
    public Result<IPage<ActivityVO>> adminList(@RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) Long categoryId,
                                               @RequestParam(defaultValue = "1") long page,
                                               @RequestParam(defaultValue = "10") long size) {
        return Result.ok(activityService.adminList(keyword, categoryId, page, size));
    }

    @PostMapping("/admin/activity")
    public Result<Void> create(@Valid @RequestBody ActivityDTO dto) {
        activityService.create(dto);
        return Result.ok();
    }

    @PutMapping("/admin/activity/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ActivityDTO dto) {
        activityService.update(id, dto);
        return Result.ok();
    }

    @PutMapping("/admin/activity/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        activityService.updateStatus(id, status);
        return Result.ok();
    }

    @DeleteMapping("/admin/activity/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        activityService.delete(id);
        return Result.ok();
    }
}
