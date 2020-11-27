package com.bjfu.exam.util;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.paper.*;
import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.entity.answer.PaperAnswer;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.enums.PaperAnswerStateEnum;
import com.bjfu.exam.enums.PaperStateEnum;
import com.bjfu.exam.vo.paper.ProblemInfoVO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class EntityConvertToDTOUtil {

    public static UserDTO convertUser(User user) {
        if(user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static PaperDTO convertPaper(Paper paper) {
        if(paper == null || paper.getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            return null;
        }
        PaperDTO paperDTO = new PaperDTO();
        BeanUtils.copyProperties(paper, paperDTO, "creator");
        paperDTO.setCreator(convertUser(paper.getCreator()));
        List<PaperAnswer> paperAnswers = paper.getPaperAnswers();
        int paperAnswerCount = paperAnswers.size();
        long finishedPaperAnswerCount = paperAnswers.stream()
                .filter(paperAnswer -> paperAnswer.getState().equals(PaperAnswerStateEnum.FINISH.getState()))
                .count();
        paperDTO.setPaperAnswerCount(paperAnswerCount);
        paperDTO.setFinishedPaperAnswerCount((int) finishedPaperAnswerCount);
        return paperDTO;
    }

    public static PaperDetailDTO convertPaperToDetail(Paper paper) {
        if(paper == null || paper.getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            return null;
        }
        PaperDetailDTO paperDetailDTO = new PaperDetailDTO();
        BeanUtils.copyProperties(paper, paperDetailDTO, "creator");
        paperDetailDTO.setCreator(convertUser(paper.getCreator()));
        return paperDetailDTO;
    }

    public static PaperWithProblemsDTO convertPaperToWithProblems(Paper paper) {
        if(paper == null || paper.getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            return null;
        }
        PaperWithProblemsDTO paperWithProblemsDTO = new PaperWithProblemsDTO();
        BeanUtils.copyProperties(paper, paperWithProblemsDTO,
                "creator", "problems");
        paperWithProblemsDTO.setCreator(convertUser(paper.getCreator()));
        paperWithProblemsDTO.setProblems(paper.getProblems().stream()
                .filter((problem) -> problem.getFatherProblem() == null)
                .map(EntityConvertToDTOUtil::convertProblem)
                .collect(Collectors.toList()));
        return paperWithProblemsDTO;
    }

    public static ProblemDTO convertProblem(Problem problem) {
        if(problem == null) {
            return null;
        }
        ProblemDTO problemDTO = new ProblemDTO();
        BeanUtils.copyProperties(problem, problemDTO, "fatherProblem", "subProblems");
        problemDTO.setFatherProblem(convertProblemToInfo(problem.getFatherProblem()));
        problemDTO.setSubProblems(problem.getSubProblems().stream()
                .map(EntityConvertToDTOUtil::convertProblem)
                .collect(Collectors.toList()));
        return problemDTO;
    }

    public static ProblemInfoDTO convertProblemToInfo(Problem problem) {
        if(problem == null) {
            return null;
        }
        ProblemInfoDTO problemInfoDTO = new ProblemInfoDTO();
        BeanUtils.copyProperties(problem, problemInfoDTO, "fatherProblem");
        problemInfoDTO.setFatherProblem(convertProblemToInfo(problem.getFatherProblem()));
        return problemInfoDTO;
    }

    public static PaperAnswerDTO convertPaperAnswer(PaperAnswer paperAnswer) {
        if(paperAnswer == null) {
            return null;
        }
        PaperAnswerDTO paperAnswerDTO = new PaperAnswerDTO();
        BeanUtils.copyProperties(paperAnswer, paperAnswerDTO);
        paperAnswerDTO.setPaperTitle(paperAnswer.getPaper().getTitle());
        return paperAnswerDTO;
    }
}
