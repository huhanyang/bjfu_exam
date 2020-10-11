package com.bjfu.exam.util;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.paper.PaperDetailDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.dto.user.UserDetailDTO;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.entity.user.User;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

public class EntityConvertToDTOUtil {
    public static UserDTO convertUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static UserDetailDTO convertUserToDetail(User user) {
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        BeanUtils.copyProperties(user, userDetailDTO, "papers");
        userDetailDTO.setPapers(user.getPapers().stream()
        .map(EntityConvertToDTOUtil::convertPaper)
        .collect(Collectors.toList()));
        return userDetailDTO;
    }

    public static PaperDTO convertPaper(Paper paper) {
        PaperDTO paperDTO = new PaperDTO();
        BeanUtils.copyProperties(paper, paperDTO, "creator");
        paperDTO.setCreator(convertUser(paper.getCreator()));
        return paperDTO;
    }

    public static PaperDetailDTO convertPaperToDetail(Paper paper) {
        PaperDetailDTO paperDetailDTO = new PaperDetailDTO();
        BeanUtils.copyProperties(paper, paperDetailDTO,
                "creator", "problems", "polymerizationProblems");
        paperDetailDTO.setCreator(convertUser(paper.getCreator()));
        paperDetailDTO.setProblems(paper.getProblems().stream()
                .filter((problem) -> problem.getPolymerizationProblem() == null)
                .map(EntityConvertToDTOUtil::convertProblem)
                .collect(Collectors.toList()));
        paperDetailDTO.setPolymerizationProblems(paper.getPolymerizationProblems().stream()
                .map(EntityConvertToDTOUtil::convertPolymerizationProblem)
                .collect(Collectors.toList()));
        return paperDetailDTO;
    }

    public static ProblemDTO convertProblem(Problem problem) {
        ProblemDTO problemDTO = new ProblemDTO();
        BeanUtils.copyProperties(problem, problemDTO);
        return problemDTO;
    }

    public static PolymerizationProblemDTO convertPolymerizationProblem(PolymerizationProblem polymerizationProblem) {
        PolymerizationProblemDTO polymerizationProblemDTO = new PolymerizationProblemDTO();
        BeanUtils.copyProperties(polymerizationProblem, polymerizationProblemDTO, "problems");
        polymerizationProblemDTO.setProblems(polymerizationProblem.getProblems().stream()
                .map(EntityConvertToDTOUtil::convertProblem)
                .collect(Collectors.toList()));
        return polymerizationProblemDTO;
    }
}
