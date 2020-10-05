package com.bjfu.exam.service;

import com.bjfu.exam.dto.PaperDTO;
import com.bjfu.exam.dto.PolymerizationProblemDTO;
import com.bjfu.exam.dto.ProblemDTO;
import com.bjfu.exam.request.PaperCreateRequest;
import com.bjfu.exam.request.PolymerizationProblemAddRequest;
import com.bjfu.exam.request.ProblemAddRequest;

import java.io.File;
import java.util.List;

public interface PaperService {
    /**
     * 根据试卷代码获取试卷
     */
    PaperDTO getPaper(String code);
    /**
     * 根据创建人Id获取试卷
     */
    List<PaperDTO> getAllPaperByCreatorId(Long creatorId);
    /**
     * 创建试卷(未创建题目)
     */
    PaperDTO createPaper(PaperCreateRequest paperCreateRequest, Long creatorId);
    /**
     * 为试卷添加组合题目
     */
    PolymerizationProblemDTO addPolymerizationProblemInPaper(Long userId,
                                                          PolymerizationProblemAddRequest polymerizationProblemAddRequest);
    /**
     * 为组合题目添加图片
     */
    PolymerizationProblemDTO addImageInPolymerizationProblem(Long userId, Long polymerizationProblemId, File image);
    /**
     * 添加题目
     */
    ProblemDTO addProblem(Long userId, ProblemAddRequest problemAddRequest);
    /**
     * 为题目添加图片
     */
    ProblemDTO addImageInProblem(Long userId, Long problemId, File image);
    /**
     * 删除题目
     */
    PaperDTO deleteProblem(Long userId, Long paperId, Long problemId);
    /**
     * 删除组合题目
     */
    PaperDTO deletePolymerizationProblem(Long userId, Long paperId, Long polymerizationProblemId);
    /**
     * 删除试卷
     */
    void deletePaper(Long userId, Long paperId);
}
