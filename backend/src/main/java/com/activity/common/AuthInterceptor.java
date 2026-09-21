package com.activity.common;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录认证拦截器：校验 JWT，将 userId/username/role 写入 request
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String ATTR_USER_ID = "userId";
    public static final String ATTR_USERNAME = "username";
    public static final String ATTR_ROLE = "role";

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // CORS 预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            writeUnauthorized(response, "未登录或登录已过期");
            return false;
        }
        try {
            Claims claims = jwtUtil.parse(auth.substring(7));
            Integer role = claims.get("role", Integer.class);
            request.setAttribute(ATTR_USER_ID, Long.valueOf(claims.getSubject()));
            request.setAttribute(ATTR_USERNAME, claims.get("username", String.class));
            request.setAttribute(ATTR_ROLE, role);
            // 管理员接口权限校验
            if (request.getRequestURI().startsWith("/api/admin/") && !Integer.valueOf(1).equals(role)) {
                writeForbidden(response);
                return false;
            }
            return true;
        } catch (Exception e) {
            writeUnauthorized(response, "登录状态无效，请重新登录");
            return false;
        }
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"" + message + "\"}");
    }

    private void writeForbidden(HttpServletResponse response) throws Exception {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":403,\"message\":\"无权限访问\"}");
    }
}
