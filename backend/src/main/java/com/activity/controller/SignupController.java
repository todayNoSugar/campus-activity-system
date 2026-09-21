package com.activity.controller;

import com.activity.common.RequestUser;
import com.activity.common.Result;
import com.activity.dto.AuditDTO;
import com.activity.service.SignupService;
import com.activity.vo.SignupVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 报名：学生报名/取消/我的报名，管理端审核
 */
@RestController
@RequestMapping("/api")
public class SignupController {

    @Autowired
    private SignupService signupService;

    /** 报名活动 */
    @PostMapping("/signup")
    public Result<Void> signup(@RequestParam Long activityId, HttpServletRequest request) {
        signupService.signup(RequestUser.id(request), activityId);
        return Result.ok();
    }

    /** 取消报名 */
    @DeleteMapping("/signup/{activityId}")
    public Result<Void> cancel(@PathVariable Long activityId, HttpServletRequest request) {
        signupService.cancel(RequestUser.id(request), activityId);
        return Result.ok();
    }

    /** 我的报名记录 */
    @GetMapping("/signup/my")
    public Result<IPage<SignupVO>> my(@RequestParam(defaultValue = "1") long page,
                                      @RequestParam(defaultValue = "10") long size,
                                      HttpServletRequest request) {
        return Result.ok(signupService.myPage(RequestUser.id(request), page, size));
    }

    /** 管理端：全量报名记录 */
    @GetMapping("/admin/signup/list")
    public Result<IPage<SignupVO>> adminList(@RequestParam(required = false) Long activityId,
                                             @RequestParam(required = false) Integer status,
                                             @RequestParam(required = false) String keyword,
                                             @RequestParam(defaultValue = "1") long page,
                                             @RequestParam(defaultValue = "10") long size) {
        return Result.ok(signupService.adminPage(activityId, status, keyword, page, size));
    }

    /** 审核报名（1通过 / 2驳回） */
    @PutMapping("/admin/signup/{id}/audit")
    public Result<Void> audit(@PathVariable Long id, @Valid @RequestBody AuditDTO dto) {
        signupService.audit(id, dto);
        return Result.ok();
    }
}
