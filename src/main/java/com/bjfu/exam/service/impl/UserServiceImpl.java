package com.bjfu.exam.service.impl;

import com.bjfu.exam.dto.UserDTO;
import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.enums.UserTypeEnum;
import com.bjfu.exam.repository.user.UserRepository;
import com.bjfu.exam.request.UserChangePasswordRequest;
import com.bjfu.exam.request.UserRegisterRequest;
import com.bjfu.exam.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO loginCheck(String account, String password) {
        Optional<User> userOptional = userRepository.findByAccount(account);
        if(userOptional.isEmpty()) {
            return null;
        } else {
            User user = userOptional.get();
            if(user.getPassword().equals(password)) {
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
                return userDTO;
            } else {
                return null;
            }
        }
    }

    @Override
    @Transactional
    public UserDTO register(UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest.getType().equals(UserTypeEnum.STUDENT.getType())
                || userRegisterRequest.getType().equals(UserTypeEnum.TEACHER.getType())) {
            if(userRepository.existsByAccount(userRegisterRequest.getAccount())) {
                return null;
            } else {
                User user = new User();
                BeanUtils.copyProperties(userRegisterRequest, user);
                user = userRepository.save(user);
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
                return userDTO;
            }
        } else {
            return null;
        }
    }

    @Override
    public UserDTO changePassword(UserChangePasswordRequest userChangePasswordRequest) {
        Optional<User> userOptional = userRepository.findByAccount(userChangePasswordRequest.getAccount());
        if(userOptional.isEmpty()) {
            return null;
        } else {
            User user = userOptional.get();
            if(user.getPassword().equals(userChangePasswordRequest.getOldPassword())) {
                user.setPassword(userChangePasswordRequest.getNewPassword());
                user = userRepository.save(user);
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(user, userDTO);
                return userDTO;
            } else {
                return null;
            }
        }
    }

}
