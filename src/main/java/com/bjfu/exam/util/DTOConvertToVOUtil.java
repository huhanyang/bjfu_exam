package com.bjfu.exam.util;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.answer.PaperAnswerDetailDTO;
import com.bjfu.exam.dto.answer.ProblemAnswerDTO;
import com.bjfu.exam.dto.answer.ProblemAnswerDetailDTO;
import com.bjfu.exam.dto.export.PaperAnswerExportJobDTO;
import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.paper.PaperDetailDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.dto.user.UserDetailDTO;
import com.bjfu.exam.entity.export.PaperAnswerExportJob;
import com.bjfu.exam.vo.answer.PaperAnswerDetailVO;
import com.bjfu.exam.vo.answer.PaperAnswerVO;
import com.bjfu.exam.vo.answer.ProblemAnswerDetailVO;
import com.bjfu.exam.vo.answer.ProblemAnswerVO;
import com.bjfu.exam.vo.export.PaperAnswerExportJobVO;
import com.bjfu.exam.vo.paper.PaperDetailVO;
import com.bjfu.exam.vo.paper.PaperVO;
import com.bjfu.exam.vo.paper.PolymerizationProblemVO;
import com.bjfu.exam.vo.paper.ProblemVO;
import com.bjfu.exam.vo.user.UserDetailVO;
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

    public static UserDetailVO convertUserDTOToDetail(UserDetailDTO userDetailDTO) {
        if(userDetailDTO == null) {
            return null;
        }
        UserDetailVO userDetailVO = new UserDetailVO();
        BeanUtils.copyProperties(userDetailDTO, userDetailVO, "papers");
        userDetailVO.setPapers(userDetailDTO.getPapers().stream()
                .map(DTOConvertToVOUtil::convertPaperDTO)
                .collect(Collectors.toList()));
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

    public static PaperDetailVO convertPaperDetailDTO(PaperDetailDTO paperDetailDTO) {
        if(paperDetailDTO == null) {
            return null;
        }
        PaperDetailVO paperDetailVO = new PaperDetailVO();
        BeanUtils.copyProperties(paperDetailDTO, paperDetailVO,
                "problems", "polymerizationProblems", "paperAnswerExportJobs");
        paperDetailVO.setProblems(paperDetailDTO.getProblems().stream()
                .map(DTOConvertToVOUtil::convertProblemDTO)
                .collect(Collectors.toList()));
        paperDetailVO.setPolymerizationProblems(paperDetailDTO.getPolymerizationProblems().stream()
                .map(DTOConvertToVOUtil::convertPolymerizationProblemDTO)
                .collect(Collectors.toList()));
        paperDetailVO.setPaperAnswerExportJobs(paperDetailDTO.getPaperAnswerExportJobs().stream()
                .map(DTOConvertToVOUtil::convertPaperAnswerExportJobDTO)
                .collect(Collectors.toList()));
        return paperDetailVO;
    }

    public static ProblemVO convertProblemDTO(ProblemDTO problemDTO) {
        if(problemDTO == null) {
            return null;
        }
        ProblemVO problemVO = new ProblemVO();
        BeanUtils.copyProperties(problemDTO, problemVO);
        return problemVO;
    }

    public static PolymerizationProblemVO convertPolymerizationProblemDTO(PolymerizationProblemDTO polymerizationProblemDTO) {
        if(polymerizationProblemDTO == null) {
            return null;
        }
        PolymerizationProblemVO polymerizationProblemVO = new PolymerizationProblemVO();
        BeanUtils.copyProperties(polymerizationProblemDTO, polymerizationProblemVO, "problems");
        polymerizationProblemVO.setProblems(polymerizationProblemDTO.getProblems().stream()
                .map(DTOConvertToVOUtil::convertProblemDTO)
                .collect(Collectors.toList()));
        return polymerizationProblemVO;
    }

    public static PaperAnswerVO convertPaperAnswerDTO(PaperAnswerDTO paperAnswerDTO) {
        if(paperAnswerDTO == null) {
            return null;
        }
        PaperAnswerVO paperAnswerVO = new PaperAnswerVO();
        BeanUtils.copyProperties(paperAnswerDTO, paperAnswerVO);
        return paperAnswerVO;
    }

    public static PaperAnswerDetailVO convertPaperAnswerDetailDTO(PaperAnswerDetailDTO paperAnswerDetailDTO) {
        if(paperAnswerDetailDTO == null) {
            return null;
        }
        PaperAnswerDetailVO paperAnswerDetailVO = new PaperAnswerDetailVO();
        BeanUtils.copyProperties(paperAnswerDetailDTO, paperAnswerDetailVO,
                "user", "paper", "problemAnswers");
        paperAnswerDetailVO.setUser(convertUserDTO(paperAnswerDetailDTO.getUser()));
        paperAnswerDetailVO.setPaper(convertPaperDTO(paperAnswerDetailDTO.getPaper()));
        paperAnswerDetailVO.setProblemAnswers(paperAnswerDetailDTO.getProblemAnswers().stream()
                .map(DTOConvertToVOUtil::convertProblemAnswerDetailDTO)
                .collect(Collectors.toList()));
        return paperAnswerDetailVO;
    }

    public static ProblemAnswerVO convertProblemAnswerDTO(ProblemAnswerDTO problemAnswerDTO) {
        if(problemAnswerDTO == null) {
            return null;
        }
        ProblemAnswerVO problemAnswerVO = new ProblemAnswerVO();
        BeanUtils.copyProperties(problemAnswerDTO, problemAnswerVO);
        return problemAnswerVO;
    }

    public static ProblemAnswerDetailVO convertProblemAnswerDetailDTO(ProblemAnswerDetailDTO problemAnswerDetailDTO) {
        if(problemAnswerDetailDTO == null) {
            return null;
        }
        ProblemAnswerDetailVO problemAnswerDetailVO = new ProblemAnswerDetailVO();
        BeanUtils.copyProperties(problemAnswerDetailDTO, problemAnswerDetailVO,
                "paperAnswer", "problem");
        problemAnswerDetailVO.setPaperAnswer(convertPaperAnswerDTO(problemAnswerDetailDTO.getPaperAnswer()));
        problemAnswerDetailVO.setProblem(convertProblemDTO(problemAnswerDetailDTO.getProblem()));
        return problemAnswerDetailVO;
    }

    public static PaperAnswerExportJobVO convertPaperAnswerExportJobDTO(PaperAnswerExportJobDTO paperAnswerExportJobDTO) {
        if(paperAnswerExportJobDTO == null) {
            return null;
        }
        PaperAnswerExportJobVO paperAnswerExportJobVO = new PaperAnswerExportJobVO();
        BeanUtils.copyProperties(paperAnswerExportJobDTO, paperAnswerExportJobVO,
                "paper");
        paperAnswerExportJobVO.setPaper(convertPaperDTO(paperAnswerExportJobDTO.getPaper()));
        return paperAnswerExportJobVO;
    }

}
