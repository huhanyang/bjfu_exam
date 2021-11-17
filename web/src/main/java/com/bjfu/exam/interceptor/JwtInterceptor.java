package com.bjfu.exam.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.bjfu.exam.api.enums.ResultEnum;
import com.bjfu.exam.core.ao.UserAO;
import com.bjfu.exam.core.dto.user.UserDTO;
import com.bjfu.exam.core.exception.BizException;
import com.bjfu.exam.interceptor.annotation.HttpAuthCheck;
import com.bjfu.exam.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserAO userAO;

    /**
     * 进入controller前检查权限
     *
     * @param request  http请求
     * @param response http响应
     * @param handler  处理器
     * @return 是否可以通过拦截器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean isHandlerMethod = handler.getClass().isAssignableFrom(HandlerMethod.class);
        if (isHandlerMethod) {
            HandlerMethod handlerMethod = ((HandlerMethod) handler);
            // 获取接口上的访问权限控制注解
            HttpAuthCheck httpAuthCheck = handlerMethod.getMethodAnnotation(HttpAuthCheck.class);
            if (Objects.isNull(httpAuthCheck)) {
                // todo 抛出系统错误异常
                throw new RuntimeException();
            }
            if (!httpAuthCheck.needLogin()) {
                return true;
            }
            // 尝试从header中取token 取不到就从http参数中取
            UserDTO userInfo = Optional.ofNullable(request.getHeader("Authorization"))
                    .filter(tokenInHeader -> tokenInHeader.length() > 7) // 前缀"Bearer"判断
                    .map(tokenInHeader -> tokenInHeader.substring(7)) // 前缀"Bearer"清除
                    .map(JwtUtil::verifyToken) // jwt 解析
                    .map(claimMap -> claimMap.get("userAccount")) // 获取jwt中的用户账号信息
                    .map(Claim::asString) // 用户账号转换成字符串
                    .map(account -> userAO.getUserInfo(account)) // 获取用户信息
                    .orElseThrow(() -> new BizException(ResultEnum.TOKEN_ERROR)); // 失败则代表token错误
            // 检查用户类型
            Optional.ofNullable(httpAuthCheck.allowUserTypes())
                    .map(Arrays::stream)
                    .map(userTypeEnumStream -> userTypeEnumStream.anyMatch(userTypeEnum -> userTypeEnum.equals(userInfo.getType())))
                    .filter(aBoolean -> aBoolean)
                    .orElseThrow(() -> new BizException(ResultEnum.PERMISSION_DENIED));
            // 检查用户状态
            Optional.ofNullable(httpAuthCheck.allowUserStates())
                    .map(Arrays::stream)
                    .map(userStateEnumStream -> userStateEnumStream.anyMatch(userStateEnum -> userStateEnum.equals(userInfo.getState())))
                    .filter(aBoolean -> aBoolean)
                    .orElseThrow(() -> new BizException(ResultEnum.PERMISSION_DENIED));
            // todo context init
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // todo context clear
    }
}
