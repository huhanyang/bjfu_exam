package com.bjfu.exam.core.util;

import com.bjfu.exam.api.enums.SessionKeyEnum;
import com.bjfu.exam.dto.user.UserDTO;

import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static void initSession(HttpSession session, UserDTO userDetailDTO) {
        session.setAttribute(SessionKeyEnum.LOGIN_USER_ID.name(), userDetailDTO.getId());
        session.setAttribute(SessionKeyEnum.LOGIN_USER_ACCOUNT.name(), userDetailDTO.getAccount());
        session.setAttribute(SessionKeyEnum.LOGIN_USER_TYPE.name(), userDetailDTO.getType());
    }

    public static void deleteSession(HttpSession session) {
        session.invalidate();
    }

    public static boolean existSession(HttpSession session) {
        return session.getAttribute(SessionKeyEnum.LOGIN_USER_ID.name()) != null;
    }

    public static long getUserId(HttpSession session) {
        return (long) session.getAttribute(SessionKeyEnum.LOGIN_USER_ID.name());
    }

    public static String getUserAccount(HttpSession session) {
        return (String) session.getAttribute(SessionKeyEnum.LOGIN_USER_ACCOUNT.name());
    }

}
