package com.activity.service.impl;

import com.activity.common.BusinessException;
import com.activity.common.JwtUtil;
import com.activity.dto.LoginDTO;
import com.activity.dto.PasswordDTO;
import com.activity.dto.PhoneDTO;
import com.activity.dto.RegisterDTO;
import com.activity.entity.SysUser;
import com.activity.mapper.SysUserMapper;
import com.activity.service.AuthService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void register(RegisterDTO dto) {
        Long exists = userMapper.selectCount(
                Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, dto.getUsername()));
        if (exists != null && exists > 0) {
            throw new BusinessException("该账号已被注册");
        }
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone() == null || dto.getPhone().isEmpty() ? null : dto.getPhone());
        user.setRole(0);   // 学生
        user.setStatus(1); // 正常
        userMapper.insert(user);
    }

    @Override
    public Map<String, Object> login(LoginDTO dto) {








        // 完整业务实现代码请获取完整版源码
        return null;
    }

    @Override
    public SysUser getInfo(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(401, "用户不存在");
        }
        return user;
    }

    @Override
    public void updatePhone(Long userId, PhoneDTO dto) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPhone(dto.getPhone());
        userMapper.updateById(user);
    }

    @Override
    public void updatePassword(Long userId, PasswordDTO dto) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码不正确");
        }
        SysUser update = new SysUser();
        update.setId(userId);
        update.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateById(update);
    }
}
