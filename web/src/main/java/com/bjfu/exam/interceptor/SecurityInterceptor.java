package com.bjfu.exam.interceptor;

import com.bjfu.exam.api.enums.ResultEnum;
import com.bjfu.exam.interceptor.annotation.HttpAuthCheck;
import com.bjfu.exam.utils.ResponseUtil;
import com.bjfu.exam.utils.SessionUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 权限控制拦截器
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    // todo isAdmin 要同时具备 isLogin的功能

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        boolean isHandlerMethod = handler.getClass().isAssignableFrom(HandlerMethod.class);
        if(isHandlerMethod) {
            HandlerMethod handlerMethod= ((HandlerMethod)handler);
            boolean requireLogin = handlerMethod.getMethodAnnotation(HttpAuthCheck.class) != null;
            boolean requireTeacher = handlerMethod.getMethodAnnotation(RequireTeacher.class) != null;
            boolean requireStudent = handlerMethod.getMethodAnnotation(RequireStudent.class) != null;
            boolean requireAdmin = handlerMethod.getMethodAnnotation(RequireAdmin.class) != null;
            if(requireLogin) {
                boolean isLogin = SessionUtil.existSession(session);
                if(!isLogin) {
                    ResponseUtil.writeResultToResponse(ResultEnum.NEED_TO_LOGIN, response);
                    return false;
                }
            }
            if(requireStudent) {
                boolean isStudent = SessionUtil.isStudent(session);
                if(!isStudent) {
                    ResponseUtil.writeResultToResponse(ResultEnum.REQUIRE_STUDENT, response);
                    return false;
                }
            } else if(requireTeacher) {
                boolean isTeacher = SessionUtil.isTeacher(session);
                if(!isTeacher) {
                    ResponseUtil.writeResultToResponse(ResultEnum.REQUIRE_TEACHER, response);
                    return false;
                }
            } else if(requireAdmin) {
                boolean isAdmin = SessionUtil.isAdmin(session);
                if(!isAdmin) {
                    ResponseUtil.writeResultToResponse(ResultEnum.REQUIRE_ADMIN, response);
                    return false;
                }
            }
        }
        return true;
    }
}
