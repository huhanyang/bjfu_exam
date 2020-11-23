package com.bjfu.exam.util;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.enums.SessionKeyEnum;
import com.bjfu.exam.enums.UserTypeEnum;

import javax.servlet.http.HttpSession;

public class SessionUtil {

    public static void initSession(HttpSession session, UserDTO userDetailDTO) {
        session.setAttribute(SessionKeyEnum.ACCOUNT_ID.getKey(), userDetailDTO.getId());
        session.setAttribute(SessionKeyEnum.ACCOUNT_NUMBER.getKey(), userDetailDTO.getAccount());
        session.setAttribute(SessionKeyEnum.ACCOUNT_TYPE.getKey(), userDetailDTO.getType());
    }

    public static void deleteSession(HttpSession session) {
        session.invalidate();
    }

    public static boolean existSession(HttpSession session) {
        return session.getAttribute(SessionKeyEnum.ACCOUNT_ID.getKey()) != null;
    }

    public static boolean isTeacher(HttpSession session) {
        Integer type = (Integer) session.getAttribute(SessionKeyEnum.ACCOUNT_TYPE.getKey());
        return type.equals(UserTypeEnum.TEACHER.getType());
    }

    public static boolean isStudent(HttpSession session) {
        Integer type = (Integer) session.getAttribute(SessionKeyEnum.ACCOUNT_TYPE.getKey());
        return type.equals(UserTypeEnum.STUDENT.getType());
    }

    public static boolean isAdmin(HttpSession session) {
        Integer type = (Integer) session.getAttribute(SessionKeyEnum.ACCOUNT_TYPE.getKey());
        return type.equals(UserTypeEnum.ADMIN.getType());
    }

    public static long getUserId(HttpSession session) {
        return (long) session.getAttribute(SessionKeyEnum.ACCOUNT_ID.getKey());
    }

}
