package com.activity.service;

import com.activity.dto.RegisterDTO;
import com.activity.entity.SysUser;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface AdminUserService {

    IPage<SysUser> page(String keyword, long page, long size);

    void add(RegisterDTO dto);

    void updateStatus(Long id, Integer status);

    void resetPassword(Long id);
}
