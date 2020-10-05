package com.bjfu.exam.util;

import com.bjfu.exam.dto.PaperDTO;
import com.bjfu.exam.dto.PolymerizationProblemDTO;
import com.bjfu.exam.dto.ProblemDTO;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import com.bjfu.exam.entity.paper.Problem;
import org.springframework.beans.BeanUtils;

public class EntityConvertToDTOUtil {
    public static PaperDTO convertPaper(Paper paper) {
        PaperDTO paperDTO = new PaperDTO();
        BeanUtils.copyProperties(paper, paperDTO);
        paperDTO.setCreatorName(paper.getCreator().getName());
        paperDTO.setProblemCount(paper.getPaperAnswers().size() + paper.getPolymerizationProblems().size());
        return paperDTO;
    }
    public static ProblemDTO convertProblem(Problem problem) {
        ProblemDTO problemDTO = new ProblemDTO();
        BeanUtils.copyProperties(problem, problemDTO);
        return problemDTO;
    }
    public static PolymerizationProblemDTO convertPolymerizationProblem(PolymerizationProblem polymerizationProblem) {
        PolymerizationProblemDTO polymerizationProblemDTO = new PolymerizationProblemDTO();
        BeanUtils.copyProperties(polymerizationProblem, polymerizationProblemDTO);
        return polymerizationProblemDTO;
    }
}
