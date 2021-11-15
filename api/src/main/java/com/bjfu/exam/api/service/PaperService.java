package com.bjfu.exam.api.service;

import com.bjfu.exam.core.dto.paper.PaperDTO;
import com.bjfu.exam.core.dto.paper.PaperDetailDTO;
import com.bjfu.exam.core.dto.paper.PaperWithProblemsDTO;
import com.bjfu.exam.core.dto.paper.ProblemDTO;

import java.io.IOException;
import java.util.List;

/**
 * 试卷相关
 */
public interface PaperService {
    /**
     * 根据试卷代码获取试卷
     */
    PaperDetailDTO getPaperByCode(String code);
    /**
     * 根据试卷id获取试卷详情
     */
    PaperWithProblemsDTO getPaperDetail(Long paperId, Long userId);
    /**
     * 根据创建人Id获取试卷
     */
    List<PaperDTO> getAllPaperByCreatorId(Long creatorId);
    /**
     * 创建试卷(未创建题目)
     */
    PaperDetailDTO createPaper(PaperCreateRequest paperCreateRequest, Long creatorId);
    /**
     * 编辑试卷标题与介绍
     * @param paperEditRequest 请求
     * @param creatorId 创建者id
     * @return 修改后的试卷
     */
    PaperDetailDTO editPaper(PaperEditRequest paperEditRequest, Long creatorId);
    /**
     * 添加题目
     */
    ProblemDTO addProblem(Long userId, ProblemAddRequest problemAddRequest);
    /**
     * 编辑题目信息
     * @param problemEditRequest 请求
     * @param creatorId 创建者id
     * @return 修改后的试卷
     */
    PaperDetailDTO editProblem(ProblemEditRequest problemEditRequest, Long creatorId);
    /**
     * 为题目添加图片
     */
    ProblemDTO addImageInProblem(Long userId, ImageInProblemAddRequest imageInProblemAddRequest) throws IOException;
    /**
     * 删除题目中的图片
     */
    ProblemDTO deleteImageInProblem(Long userId, ImageInProblemDeleteRequest imageInProblemDeleteRequest);
    /**
     * 删除题目
     */
    void deleteProblem(Long userId, ProblemDeleteRequest problemDeleteRequest);
    /**
     * 软删除试卷
     */
    void deletePaper(Long userId, Long paperId);
    /**
     * 改变试卷的状态
     */
    PaperDTO changePaperState(Long userId, PaperStateChangeRequest paperStateChangeRequest);
}
