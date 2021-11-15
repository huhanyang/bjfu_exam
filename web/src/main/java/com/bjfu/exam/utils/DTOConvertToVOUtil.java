package com.bjfu.exam.utils;


import com.bjfu.exam.core.dto.paper.PaperDTO;
import com.bjfu.exam.core.dto.paper.PaperDetailsDTO;
import com.bjfu.exam.core.dto.user.UserDTO;
import com.bjfu.exam.vo.user.UserDetailVO;
import com.bjfu.exam.vo.user.UserVO;
import com.bjfu.exam.vo.answer.PaperAnswerVO;
import com.bjfu.exam.vo.paper.*;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

public class DTOConvertToVOUtil {
    
    private DTOConvertToVOUtil(){}

    public static UserVO convertUserDTO(UserDTO userDTO) {
        if(userDTO == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO, userVO);
        return userVO;
    }

    public static UserDetailVO convertToUserDetailVO(UserDTO userDTO) {
        if(userDTO == null) {
            return null;
        }
        UserDetailVO userDetailVO = new UserDetailVO();
        BeanUtils.copyProperties(userDTO, userDetailVO);
        return userDetailVO;
    }

    public static PaperVO convertPaperDTO(PaperDTO paperDTO) {
        if(paperDTO == null) {
            return null;
        }
        PaperVO paperVO = new PaperVO();
        BeanUtils.copyProperties(paperDTO, paperVO, "creator");
        paperVO.setCreator(convertUserDTO(paperDTO.getCreator()));
        return paperVO;
    }

    public static PaperDetailsVO convertPaperDetailsDTO(PaperDetailsDTO paperDetailDTO) {
        if(paperDetailDTO == null) {
            return null;
        }
        PaperDetailsVO paperDetailsVO = new PaperDetailsVO();
        BeanUtils.copyProperties(paperDetailDTO, paperDetailsVO, "creator");
        paperDetailsVO.setCreator(convertUserDTO(paperDetailDTO.getCreator()));
        return paperDetailsVO;
    }

    public static PaperWithProblemsVO convertPaperWithProblemsDTO(PaperWithProblemsDTO paperWithProblemsDTO) {
        if(paperWithProblemsDTO == null) {
            return null;
        }
        PaperWithProblemsVO paperWithProblemsVO = new PaperWithProblemsVO();
        BeanUtils.copyProperties(paperWithProblemsDTO, paperWithProblemsVO, "creator", "problems");
        paperWithProblemsVO.setCreator(convertUserDTO(paperWithProblemsDTO.getCreator()));
        paperWithProblemsVO.setProblems(paperWithProblemsDTO.getProblems().stream()
                .map(DTOConvertToVOUtil::convertProblemDTO)
                .collect(Collectors.toList()));
        return paperWithProblemsVO;
    }

    public static ProblemVO convertProblemDTO(ProblemDTO problemDTO) {
        if(problemDTO == null) {
            return null;
        }
        ProblemVO problemVO = new ProblemVO();
        BeanUtils.copyProperties(problemDTO, problemVO, "fatherProblem", "subProblems");
        problemVO.setFatherProblem(convertProblemInfoDTO(problemDTO.getFatherProblem()));
        problemVO.setSubProblems(problemDTO.getSubProblems().stream()
                .map(DTOConvertToVOUtil::convertProblemDTO)
                .collect(Collectors.toList()));
        return problemVO;
    }

    public static ProblemInfoVO convertProblemInfoDTO(ProblemInfoDTO problemInfoDTO) {
        if(problemInfoDTO == null) {
            return null;
        }
        ProblemInfoVO problemInfoVO = new ProblemInfoVO();
        BeanUtils.copyProperties(problemInfoDTO, problemInfoVO, "fatherProblem");
        problemInfoVO.setFatherProblem(convertProblemInfoDTO(problemInfoDTO.getFatherProblem()));
        return problemInfoVO;
    }

    public static PaperAnswerVO convertPaperAnswerDTO(PaperAnswerDTO paperAnswerDTO) {
        if(paperAnswerDTO == null) {
            return null;
        }
        PaperAnswerVO paperAnswerVO = new PaperAnswerVO();
        BeanUtils.copyProperties(paperAnswerDTO, paperAnswerVO);
        return paperAnswerVO;
    }

}
