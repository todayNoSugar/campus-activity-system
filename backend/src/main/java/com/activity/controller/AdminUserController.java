package com.activity.controller;

import com.activity.common.Result;
import com.activity.dto.RegisterDTO;
import com.activity.entity.SysUser;
import com.activity.service.AdminUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 管理端：用户管理（学生账号）
 */
@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @GetMapping("/list")
    public Result<IPage<SysUser>> list(@RequestParam(required = false) String keyword,
                                       @RequestParam(defaultValue = "1") long page,
                                       @RequestParam(defaultValue = "10") long size) {
        return Result.ok(adminUserService.page(keyword, page, size));
    }

    @PostMapping
    public Result<Void> add(@Valid @RequestBody RegisterDTO dto) {
        adminUserService.add(dto);
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        adminUserService.updateStatus(id, status);
        return Result.ok();
    }

    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        adminUserService.resetPassword(id);
        return Result.ok();
    }
}
