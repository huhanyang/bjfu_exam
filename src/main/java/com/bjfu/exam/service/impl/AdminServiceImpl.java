package com.bjfu.exam.service.impl;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.enums.UserStateEnum;
import com.bjfu.exam.enums.UserTypeEnum;
import com.bjfu.exam.exception.BadParamException;
import com.bjfu.exam.repository.user.UserRepository;
import com.bjfu.exam.request.admin.AdminCreateTeacherAccountRequest;
import com.bjfu.exam.service.AdminService;
import com.bjfu.exam.util.EntityConvertToDTOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDTO createTeacherAccount(AdminCreateTeacherAccountRequest adminCreateTeacherAccountRequest) {
        if(userRepository.existsByAccount(adminCreateTeacherAccountRequest.getAccount())) {
            return null;
        }
        User user = new User();
        user.setType(UserTypeEnum.TEACHER.getType());
        user.setState(UserStateEnum.ACTIVE.getType());
        BeanUtils.copyProperties(adminCreateTeacherAccountRequest, user);
        user = userRepository.save(user);
        return EntityConvertToDTOUtil.convertUser(user);
    }

    @Override
    public List<UserDTO> getAllTeacherAccounts() {
        List<User> teachers = userRepository.findAllByType(UserTypeEnum.TEACHER.getType());
        return teachers.stream()
                .map(EntityConvertToDTOUtil::convertUser)
                .collect(Collectors.toList());
    }

    @Override
    public void banTeacherAccount(Long teacherAccountId) {
        Optional<User> userOptional = userRepository.findByIdForUpdate(teacherAccountId);
        if(userOptional.isEmpty() || !userOptional.get().getType().equals(UserTypeEnum.TEACHER.getType())) {
            throw new BadParamException(ResultEnum.TEACHER_NOT_EXIST);
        }
        User teacher = userOptional.get();
        teacher.setState(UserStateEnum.BANNED.getType());
        userRepository.save(teacher);
    }

    @Override
    public void activeTeacherAccount(Long teacherAccountId) {
        Optional<User> userOptional = userRepository.findByIdForUpdate(teacherAccountId);
        if(userOptional.isEmpty() || !userOptional.get().getType().equals(UserTypeEnum.TEACHER.getType())) {
            throw new BadParamException(ResultEnum.TEACHER_NOT_EXIST);
        }
        User teacher = userOptional.get();
        teacher.setState(UserStateEnum.ACTIVE.getType());
        userRepository.save(teacher);
    }
}
