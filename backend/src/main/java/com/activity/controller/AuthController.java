package com.activity.controller;

import com.activity.common.RequestUser;
import com.activity.common.Result;
import com.activity.dto.LoginDTO;
import com.activity.dto.PasswordDTO;
import com.activity.dto.PhoneDTO;
import com.activity.dto.RegisterDTO;
import com.activity.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * 认证接口：注册 / 登录 / 当前用户信息 / 个人中心
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        authService.register(dto);
        return Result.ok();
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        return Result.ok(authService.login(dto));
    }

    @GetMapping("/info")
    public Result<?> info(HttpServletRequest request) {
        return Result.ok(authService.getInfo(RequestUser.id(request)));
    }

    @PutMapping("/phone")
    public Result<Void> updatePhone(HttpServletRequest request, @Valid @RequestBody PhoneDTO dto) {
        authService.updatePhone(RequestUser.id(request), dto);
        return Result.ok();
    }

    @PutMapping("/password")
    public Result<Void> updatePassword(HttpServletRequest request, @Valid @RequestBody PasswordDTO dto) {
        authService.updatePassword(RequestUser.id(request), dto);
        return Result.ok();
    }
}
