package com.bjfu.exam.util;

import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.paper.PaperDetailDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.dto.user.UserDetailDTO;
import com.bjfu.exam.vo.paper.PaperDetailVO;
import com.bjfu.exam.vo.paper.PaperVO;
import com.bjfu.exam.vo.paper.PolymerizationProblemVO;
import com.bjfu.exam.vo.paper.ProblemVO;
import com.bjfu.exam.vo.user.UserDetailVO;
import com.bjfu.exam.vo.user.UserVO;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

public class DTOConvertToVOUtil {
    public static UserVO convertUserDTO(UserDTO userDTO) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO, userVO);
        return userVO;
    }
    public static UserDetailVO convertUserDTOToDetail(UserDetailDTO userDetailDTO) {
        UserDetailVO userDetailVO = new UserDetailVO();
        BeanUtils.copyProperties(userDetailDTO, userDetailVO, "papers");
        userDetailVO.setPapers(userDetailDTO.getPapers().stream()
                .map(DTOConvertToVOUtil::convertPaperDTO)
                .collect(Collectors.toList()));
        return userDetailVO;
    }
    public static PaperVO convertPaperDTO(PaperDTO paperDTO) {
        PaperVO paperVO = new PaperVO();
        BeanUtils.copyProperties(paperDTO, paperVO, "creator");
        paperVO.setCreator(convertUserDTO(paperDTO.getCreator()));
        return paperVO;
    }
    public static PaperDetailVO convertPaperDetailDTO(PaperDetailDTO paperDetailDTO) {
        PaperDetailVO paperDetailVO = new PaperDetailVO();
        BeanUtils.copyProperties(paperDetailDTO, paperDetailVO);
        paperDetailVO.setProblems(paperDetailDTO.getProblems().stream()
                .map(DTOConvertToVOUtil::convertProblemDTO)
                .collect(Collectors.toList()));
        paperDetailVO.setPolymerizationProblems(paperDetailDTO.getPolymerizationProblems().stream()
                .map(DTOConvertToVOUtil::convertPolymerizationProblemDTO)
                .collect(Collectors.toList()));
        return paperDetailVO;
    }
    public static ProblemVO convertProblemDTO(ProblemDTO problemDTO) {
        ProblemVO problemVO = new ProblemVO();
        BeanUtils.copyProperties(problemDTO, problemVO);
        return problemVO;
    }
    public static PolymerizationProblemVO convertPolymerizationProblemDTO(PolymerizationProblemDTO polymerizationProblemDTO) {
        PolymerizationProblemVO polymerizationProblemVO = new PolymerizationProblemVO();
        BeanUtils.copyProperties(polymerizationProblemDTO, polymerizationProblemVO, "problems");
        polymerizationProblemVO.setProblems(polymerizationProblemDTO.getProblems().stream()
                .map(DTOConvertToVOUtil::convertProblemDTO)
                .collect(Collectors.toList()));
        return polymerizationProblemVO;
    }
}
