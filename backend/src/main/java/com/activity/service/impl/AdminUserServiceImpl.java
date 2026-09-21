package com.activity.service.impl;

import com.activity.common.BusinessException;
import com.activity.dto.RegisterDTO;
import com.activity.entity.SysUser;
import com.activity.mapper.SysUserMapper;
import com.activity.service.AdminUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    /** 管理员重置/新建学生账号的默认密码 */
    public static final String DEFAULT_PASSWORD = "123456";

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public IPage<SysUser> page(String keyword, long page, long size) {
        // 完整业务实现代码请获取完整版源码
        return null;
    }

    @Override
    public void add(RegisterDTO dto) {
        Long exists = userMapper.selectCount(
                Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, dto.getUsername()));
        if (exists != null && exists > 0) {
            throw new BusinessException("该账号已被注册");
        }
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(
                dto.getPassword() == null || dto.getPassword().isEmpty()
                        ? DEFAULT_PASSWORD : dto.getPassword()));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone() == null || dto.getPhone().isEmpty() ? null : dto.getPhone());
        user.setRole(0);
        user.setStatus(1);
        userMapper.insert(user);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        SysUser update = new SysUser();
        update.setId(id);
        update.setStatus(status);
        userMapper.updateById(update);
    }

    @Override
    public void resetPassword(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        SysUser update = new SysUser();
        update.setId(id);
        update.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        userMapper.updateById(update);
    }
}
