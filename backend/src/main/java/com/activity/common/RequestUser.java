package com.activity.common;

import javax.servlet.http.HttpServletRequest;

/**
 * 从请求中读取当前登录用户信息（由 AuthInterceptor 写入）
 */
public class RequestUser {

    public static Long id(HttpServletRequest request) {
        return (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    }

    public static String username(HttpServletRequest request) {
        return (String) request.getAttribute(AuthInterceptor.ATTR_USERNAME);
    }

    public static Integer role(HttpServletRequest request) {
        return (Integer) request.getAttribute(AuthInterceptor.ATTR_ROLE);
    }
}
