package com.bjfu.exam.util;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.answer.PaperAnswerDetailDTO;
import com.bjfu.exam.dto.answer.ProblemAnswerDTO;
import com.bjfu.exam.dto.answer.ProblemAnswerDetailDTO;
import com.bjfu.exam.dto.export.PaperAnswerExportJobDTO;
import com.bjfu.exam.dto.paper.*;
import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.dto.user.UserDetailDTO;
import com.bjfu.exam.entity.answer.PaperAnswer;
import com.bjfu.exam.entity.answer.ProblemAnswer;
import com.bjfu.exam.entity.export.PaperAnswerExportJob;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.entity.user.User;
import org.springframework.beans.BeanUtils;

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

    public static UserDetailDTO convertUserToDetail(User user) {
        if(user == null) {
            return null;
        }
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        BeanUtils.copyProperties(user, userDetailDTO,
                "papers", "paperAnswers");
        userDetailDTO.setPapers(user.getPapers().stream()
        .map(EntityConvertToDTOUtil::convertPaper)
        .collect(Collectors.toList()));
        userDetailDTO.setPaperAnswers(user.getPaperAnswers().stream()
                .map(EntityConvertToDTOUtil::convertPaperAnswer)
                .collect(Collectors.toList()));
        return userDetailDTO;
    }

    public static PaperDTO convertPaper(Paper paper) {
        if(paper == null) {
            return null;
        }
        PaperDTO paperDTO = new PaperDTO();
        BeanUtils.copyProperties(paper, paperDTO, "creator");
        paperDTO.setCreator(convertUser(paper.getCreator()));
        return paperDTO;
    }

    public static PaperDetailDTO convertPaperToDetail(Paper paper) {
        if(paper == null) {
            return null;
        }
        PaperDetailDTO paperDetailDTO = new PaperDetailDTO();
        BeanUtils.copyProperties(paper, paperDetailDTO,
                "creator", "problems", "polymerizationProblems", "paperAnswerExportJobs");
        paperDetailDTO.setCreator(convertUser(paper.getCreator()));
        paperDetailDTO.setProblems(paper.getProblems().stream()
                .filter((problem) -> problem.getPolymerizationProblem() == null)
                .map(EntityConvertToDTOUtil::convertProblem)
                .collect(Collectors.toList()));
        paperDetailDTO.setPolymerizationProblems(paper.getPolymerizationProblems().stream()
                .map(EntityConvertToDTOUtil::convertPolymerizationProblemDetail)
                .collect(Collectors.toList()));
        paperDetailDTO.setPaperAnswerExportJobs(paper.getPaperAnswerExportJobs().stream()
                .map(EntityConvertToDTOUtil::convertPaperAnswerExportJob)
                .collect(Collectors.toList()));
        return paperDetailDTO;
    }

    public static ProblemDTO convertProblem(Problem problem) {
        if(problem == null) {
            return null;
        }
        ProblemDTO problemDTO = new ProblemDTO();
        BeanUtils.copyProperties(problem, problemDTO, "polymerizationProblem");
        problemDTO.setPolymerizationProblem(convertPolymerizationProblem(problem.getPolymerizationProblem()));
        return problemDTO;
    }

    public static PolymerizationProblemDTO convertPolymerizationProblem(PolymerizationProblem polymerizationProblem) {
        if(polymerizationProblem == null) {
            return null;
        }
        PolymerizationProblemDTO polymerizationProblemDTO = new PolymerizationProblemDTO();
        BeanUtils.copyProperties(polymerizationProblem, polymerizationProblemDTO);
        return polymerizationProblemDTO;
    }

    public static PolymerizationProblemDetailDTO convertPolymerizationProblemDetail(PolymerizationProblem polymerizationProblem) {
        if(polymerizationProblem == null) {
            return null;
        }
        PolymerizationProblemDetailDTO polymerizationProblemDetailDTO = new PolymerizationProblemDetailDTO();
        BeanUtils.copyProperties(polymerizationProblem, polymerizationProblemDetailDTO, "problems");
        polymerizationProblemDetailDTO.setProblems(polymerizationProblem.getProblems().stream()
                .map(EntityConvertToDTOUtil::convertProblem)
                .collect(Collectors.toList()));
        return polymerizationProblemDetailDTO;
    }

    public static PaperAnswerDTO convertPaperAnswer(PaperAnswer paperAnswer) {
        if(paperAnswer == null) {
            return null;
        }
        PaperAnswerDTO paperAnswerDTO = new PaperAnswerDTO();
        BeanUtils.copyProperties(paperAnswer, paperAnswerDTO);
        return paperAnswerDTO;
    }

    public static PaperAnswerDetailDTO convertPaperAnswerToDetail(PaperAnswer paperAnswer) {
        if(paperAnswer == null) {
            return null;
        }
        PaperAnswerDetailDTO paperAnswerDetailDTO = new PaperAnswerDetailDTO();
        BeanUtils.copyProperties(paperAnswer, paperAnswerDetailDTO,
                "user", "paper", "problemAnswers");
        paperAnswerDetailDTO.setUser(convertUser(paperAnswer.getUser()));
        paperAnswerDetailDTO.setPaper(convertPaper(paperAnswer.getPaper()));
        paperAnswerDetailDTO.setProblemAnswers(paperAnswer.getProblemAnswers().stream()
                .map(EntityConvertToDTOUtil::convertProblemAnswerToDetail)
                .collect(Collectors.toList()));
        return paperAnswerDetailDTO;
    }

    public static ProblemAnswerDTO convertProblemAnswer(ProblemAnswer problemAnswer) {
        if(problemAnswer == null) {
            return null;
        }
        ProblemAnswerDTO problemAnswerDTO = new ProblemAnswerDTO();
        BeanUtils.copyProperties(problemAnswer, problemAnswerDTO);
        return problemAnswerDTO;
    }

    public static ProblemAnswerDetailDTO convertProblemAnswerToDetail(ProblemAnswer problemAnswer) {
        if(problemAnswer == null) {
            return null;
        }
        ProblemAnswerDetailDTO problemAnswerDetailDTO = new ProblemAnswerDetailDTO();
        BeanUtils.copyProperties(problemAnswer, problemAnswerDetailDTO,
                "paperAnswer", "problem");
        problemAnswerDetailDTO.setPaperAnswer(convertPaperAnswer(problemAnswer.getPaperAnswer()));
        problemAnswerDetailDTO.setProblem(convertProblem(problemAnswer.getProblem()));
        return problemAnswerDetailDTO;
    }

    public static PaperAnswerExportJobDTO convertPaperAnswerExportJob(PaperAnswerExportJob paperAnswerExportJob) {
        if(paperAnswerExportJob == null) {
            return null;
        }
        PaperAnswerExportJobDTO paperAnswerExportJobDTO = new PaperAnswerExportJobDTO();
        BeanUtils.copyProperties(paperAnswerExportJob, paperAnswerExportJobDTO,
                "paper");
        paperAnswerExportJobDTO.setPaper(convertPaper(paperAnswerExportJob.getPaper()));
        return paperAnswerExportJobDTO;
    }
}
