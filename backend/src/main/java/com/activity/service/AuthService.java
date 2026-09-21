package com.activity.service;

import com.activity.dto.LoginDTO;
import com.activity.dto.PasswordDTO;
import com.activity.dto.PhoneDTO;
import com.activity.dto.RegisterDTO;
import com.activity.entity.SysUser;

import java.util.Map;

public interface AuthService {

    void register(RegisterDTO dto);

    Map<String, Object> login(LoginDTO dto);

    SysUser getInfo(Long userId);

    void updatePhone(Long userId, PhoneDTO dto);

    void updatePassword(Long userId, PasswordDTO dto);
}
