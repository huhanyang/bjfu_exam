package com.bjfu.exam.util;

import com.bjfu.exam.dto.user.UserDetailDTO;

import javax.servlet.http.HttpSession;

public class SessionUtil {
    public static boolean existSession(HttpSession session) {
        return session.getAttribute("userId") != null;
    }
    public static void initSession(HttpSession session, UserDetailDTO userDetailDTO) {
        session.setAttribute("userId", userDetailDTO.getId());
        session.setAttribute("account", userDetailDTO.getAccount());
        session.setAttribute("type", userDetailDTO.getType());
    }
    public static void deleteSession(HttpSession session) {
        session.invalidate();
    }
}
