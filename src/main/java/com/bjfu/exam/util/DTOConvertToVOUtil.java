package com.bjfu.exam.util;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.paper.*;
import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.vo.answer.PaperAnswerVO;
import com.bjfu.exam.vo.paper.*;
import com.bjfu.exam.vo.user.UserVO;
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

    public static PaperVO convertPaperDTO(PaperDTO paperDTO) {
        if(paperDTO == null) {
            return null;
        }
        PaperVO paperVO = new PaperVO();
        BeanUtils.copyProperties(paperDTO, paperVO, "creator");
        paperVO.setCreator(convertUserDTO(paperDTO.getCreator()));
        return paperVO;
    }

    public static PaperDetailVO convertPaperDetailDTO(PaperDetailDTO paperDetailDTO) {
        if(paperDetailDTO == null) {
            return null;
        }
        PaperDetailVO paperDetailVO = new PaperDetailVO();
        BeanUtils.copyProperties(paperDetailDTO, paperDetailVO, "creator");
        paperDetailVO.setCreator(convertUserDTO(paperDetailDTO.getCreator()));
        return paperDetailVO;
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
        problemVO.setFatherProblem(convertProblemDTO(problemDTO.getFatherProblem()));
        problemVO.setSubProblems(problemDTO.getSubProblems().stream()
                .map(DTOConvertToVOUtil::convertProblemDTO)
                .collect(Collectors.toList()));
        return problemVO;
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
