package com.bjfu.exam.service;

import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.paper.PaperDetailDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.request.paper.*;

import java.util.List;

public interface PaperService {
    /**
     * 根据试卷代码获取试卷
     */
    PaperDTO getPaper(String code);
    /**
     * 根据创建人Id获取试卷
     */
    List<PaperDetailDTO> getAllPaperByCreatorId(Long creatorId);
    /**
     * 创建试卷(未创建题目)
     */
    PaperDetailDTO createPaper(PaperCreateRequest paperCreateRequest, Long creatorId);
    /**
     * 为试卷添加组合题目
     */
    PolymerizationProblemDTO addPolymerizationProblemInPaper(Long userId,
                                                             PolymerizationProblemAddRequest polymerizationProblemAddRequest);
    /**
     * 为组合题目添加图片
     */
    PolymerizationProblemDTO addImageInPolymerizationProblem(Long userId,
                                                             ImageInPolymerizationProblemAddRequest imageInPolymerizationProblemAddRequest);
    /**
     * 删除组合题目中的图片
     */
    PolymerizationProblemDTO deleteImageInPolymerizationProblem(Long userId,
                                                                ImageInPolymerizationProblemDeleteRequest imageInPolymerizationProblemDeleteRequest);
    /**
     * 添加题目
     */
    ProblemDTO addProblem(Long userId, ProblemAddRequest problemAddRequest);
    /**
     * 为题目添加图片
     */
    ProblemDTO addImageInProblem(Long userId, ImageInProblemAddRequest imageInProblemAddRequest);
    /**
     * 删除题目中的图片
     */
    ProblemDTO deleteImageInProblem(Long userId, ImageInProblemDeleteRequest imageInProblemDeleteRequest);
    /**
     * 删除题目
     */
    PaperDetailDTO deleteProblem(Long userId, ProblemDeleteRequest problemDeleteRequest);
    /**
     * 删除组合题目
     */
    PaperDetailDTO deletePolymerizationProblem(Long userId,
                                               PolymerizationProblemDeleteRequest polymerizationProblemDeleteRequest);
    /**
     * 删除试卷
     */
    boolean deletePaper(Long userId, Long paperId);
    /**
     * 试卷大题排序
     */
    PaperDetailDTO resortProblemsInPaper(Long userId, ProblemsInPaperResortRequest problemsInPaperResortRequest);
}
