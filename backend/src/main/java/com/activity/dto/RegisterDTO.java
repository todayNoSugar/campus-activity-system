package com.activity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterDTO {

    @NotBlank(message = "账号不能为空")
    @Size(min = 3, max = 20, message = "账号长度需在3~20位之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "账号只能包含字母、数字和下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度需在6~20位之间")
    private String password;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 20, message = "姓名长度不能超过20位")
    private String realName;

    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}
