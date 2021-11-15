package com.bjfu.exam.core.ao;

import com.bjfu.exam.core.dto.answer.ProblemDTO;
import com.bjfu.exam.core.params.problem.ProblemCreateProblemParams;
import com.bjfu.exam.core.params.problem.ProblemEditProblemParams;

import java.io.InputStream;
import java.util.List;

/**
 * 试卷试题相关操作
 * @author warthog
 */
public interface ProblemAO {

    ProblemDTO createProblem(ProblemCreateProblemParams params, Long operatorId);

    ProblemDTO editProblem(ProblemEditProblemParams params, Long operatorId);

    ProblemDTO addImageOnProblem(Long paperId, Long problemId, InputStream imageInputStream, Long operatorId);

    void deleteProblem(Long paperId, Long problemId, Long operatorId);

    ProblemDTO deleteImageOnProblem(Long paperId, Long problemId, Integer num, Long operatorId);

    List<ProblemDTO> getProblems(Long paperId, Long operatorId);

}
