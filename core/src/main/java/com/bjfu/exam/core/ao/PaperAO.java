package com.bjfu.exam.core.ao;

import com.bjfu.exam.api.bo.Page;
import com.bjfu.exam.api.enums.PaperStateEnum;
import com.bjfu.exam.core.dto.paper.PaperDTO;
import com.bjfu.exam.core.dto.paper.PaperDetailsDTO;
import com.bjfu.exam.core.exception.BizException;
import com.bjfu.exam.core.params.paper.PaperCreatePaperParams;
import com.bjfu.exam.core.params.paper.PaperEditPaperParams;
import com.bjfu.exam.core.params.paper.PaperListCreatedPapersParams;

/**
 * 试卷相关操作
 * @author warthog
 */
public interface PaperAO {

    /**
     * 创建试卷
     * @param params 参数
     * @param operatorId 操作人id
     * @return 创建好的试卷
     * @throws BizException USER_NOT_EXIST
     */
    PaperDTO createPaper(PaperCreatePaperParams params, Long operatorId);

    /**
     * 编辑试卷信息
     * @param params 参数
     * @param operatorId 操作人id
     * @return 编辑后的试卷
     * @throws BizException PAPER_NOT_EXIST
     * @throws BizException NOT_PAPER_CREATOR
     * @throws BizException PAPER_STATE_IS_NOT_CREATING
     */
    PaperDTO editPaper(PaperEditPaperParams params, Long operatorId);

    /**
     * 改变试卷的状态
     * @param paperId 试卷id
     * @param newState 新的状态
     * @param operatorId 操作人id
     * @return 修改状态后的试卷
     * @throws BizException PAPER_NOT_EXIST
     * @throws BizException NOT_PAPER_CREATOR
     * @throws BizException PAPER_STATE_CHANGE_NOT_ALLOW
     */
    PaperDTO changePaperState(Long paperId, PaperStateEnum newState, Long operatorId);

    /**
     * 根据试卷代码获取试卷
     * @param code 试卷代码
     * @return 对应的试卷
     * @throws BizException PAPER_NOT_EXIST
     */
    PaperDTO getPaperByCode(String code);

    /**
     * 根据试卷id获取试卷详情
     * @param paperId 试卷id
     * @param operatorId 操作人id
     * @return 试卷详情
     * @throws BizException PAPER_NOT_EXIST
     * @throws BizException NOT_PAPER_CREATOR
     */
    PaperDetailsDTO getCreatedPaperDetails(Long paperId, Long operatorId);

    /**
     * 根据试卷id获取试卷
     * @param paperId 试卷id
     * @param operatorId 操作人id
     * @return 试卷
     * @throws BizException PAPER_NOT_EXIST
     * @throws BizException NOT_PAPER_CREATOR
     */
    PaperDTO getCreatedPaper(Long paperId, Long operatorId);
    /**
     * 根据创建人Id获取试卷
     * @param params 参数
     * @param creatorId 创建人id
     * @return 分页的试卷
     * @throws BizException USER_NOT_EXIST
     */
    Page<PaperDTO> listCreatedPapers(PaperListCreatedPapersParams params, Long creatorId);
}
