package com.bjfu.exam.service.impl;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.enums.UserTypeEnum;
import com.bjfu.exam.repository.user.UserRepository;
import com.bjfu.exam.request.admin.AdminCreateTeacherAccountRequest;
import com.bjfu.exam.service.AdminService;
import com.bjfu.exam.util.EntityConvertToDTOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDTO createTeacherAccount(Long userId, AdminCreateTeacherAccountRequest adminCreateTeacherAccountRequest) {
        if(userRepository.existsByAccount(adminCreateTeacherAccountRequest.getAccount())) {
            return null;
        }
        User user = new User();
        user.setType(UserTypeEnum.TEACHER.getType());
        BeanUtils.copyProperties(adminCreateTeacherAccountRequest, user);
        user = userRepository.save(user);
        return EntityConvertToDTOUtil.convertUser(user);
    }
}
