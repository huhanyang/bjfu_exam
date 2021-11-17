package com.bjfu.exam.controller;

import com.bjfu.exam.api.bo.Page;
import com.bjfu.exam.core.ao.PaperAO;
import com.bjfu.exam.core.dto.paper.PaperDTO;
import com.bjfu.exam.core.dto.paper.PaperDetailsDTO;
import com.bjfu.exam.core.exception.BizException;
import com.bjfu.exam.core.params.paper.PaperCreatePaperParams;
import com.bjfu.exam.core.params.paper.PaperEditPaperParams;
import com.bjfu.exam.core.params.paper.PaperListCreatedPapersParams;
import com.bjfu.exam.utils.SessionUtil;
import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.request.paper.*;
import com.bjfu.exam.utils.DTOConvertToVOUtil;
import com.bjfu.exam.vo.paper.PaperDetailsVO;
import com.bjfu.exam.vo.paper.PaperVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/paper")
public class PaperController {

    @Autowired
    private PaperAO paperAO;

    /**
     * 学生获取试卷信息用的接口
     * @param code 试卷代码
     * @return 试卷信息
     * @throws BizException PAPER_NOT_EXIST
     */
    @GetMapping("/getByCode")
    public BaseResult<PaperVO> getPaper(@NotBlank(message = "试卷代码不能为空!") String code) {
        PaperDTO paperDTO = paperAO.getPaperByCode(code);
        PaperVO paperVO = DTOConvertToVOUtil.convertPaperDTO(paperDTO);
        return BaseResult.success(paperVO);
    }

    /**
     * 教师获取试卷详情信息用的接口
     * @param paperId 试卷id
     * @return 试卷详情信息
     * @throws BizException USER_NOT_EXIST
     * @throws BizException PAPER_NOT_EXIST
     * @throws BizException NOT_PAPER_CREATOR
     */
    @GetMapping("/getCreatedPaperDetails")
    public BaseResult<PaperDetailsVO> getCreatedPaperDetails(@NotNull(message = "试卷id不能为空!") Long paperId,
                                                             HttpSession session) {
        PaperDetailsDTO paperDetails = paperAO.getCreatedPaperDetails(paperId, SessionUtil.getUserId(session));
        return BaseResult.success(DTOConvertToVOUtil.convertPaperDetailsDTO(paperDetails));
    }
    /**
     * 教师获取试卷信息用的接口
     * @param paperId 试卷id
     * @return 试卷信息
     * @throws BizException USER_NOT_EXIST
     * @throws BizException PAPER_NOT_EXIST
     * @throws BizException NOT_PAPER_CREATOR
     */
    @GetMapping("/getCreatedPaper")
    public BaseResult<PaperVO> getCreatedPaper(@NotNull(message = "试卷id不能为空!") Long paperId,
                                               HttpSession session) {
        PaperDTO paper = paperAO.getCreatedPaper(paperId, SessionUtil.getUserId(session));
        return BaseResult.success(DTOConvertToVOUtil.convertPaperDTO(paper));
    }

    @PostMapping("/createPaper")
    public BaseResult<PaperVO> createPaper(@Validated @RequestBody PaperCreatePaperRequest request,
                                           HttpSession session) {
        PaperCreatePaperParams params = new PaperCreatePaperParams();
        BeanUtils.copyProperties(request, params);
        PaperDTO paper = paperAO.createPaper(params, SessionUtil.getUserId(session));
        return BaseResult.success(DTOConvertToVOUtil.convertPaperDTO(paper));
    }

    @PostMapping("/editPaper")
    public BaseResult<PaperVO> editPaper(@Validated @RequestBody PaperEditPaperRequest request,
                                         HttpSession session) {
        PaperEditPaperParams params = new PaperEditPaperParams();
        BeanUtils.copyProperties(request, params);
        PaperDTO paper = paperAO.editPaper(params, SessionUtil.getUserId(session));
        return BaseResult.success(DTOConvertToVOUtil.convertPaperDTO(paper));
    }

    @PostMapping("/changePaperState")
    public BaseResult<PaperVO> changePaperState(@Validated @RequestBody PaperChangePaperStateRequest request,
                                                HttpSession session) {
        PaperDTO paper = paperAO.changePaperState(request.getPaperId(), request.getNewState(), SessionUtil.getUserId(session));
        return BaseResult.success(DTOConvertToVOUtil.convertPaperDTO(paper));
    }

    @GetMapping("/listCreatedPapers")
    public BaseResult<Page<PaperVO>> listCreatedPapers(@Validated PaperListCreatedPapersRequest request,
                                                       HttpSession session) {
        PaperListCreatedPapersParams params = new PaperListCreatedPapersParams();
        BeanUtils.copyProperties(request, params);
        Page<PaperDTO> paperDTOPage = paperAO.listCreatedPapers(params, SessionUtil.getUserId(session));
        return BaseResult.success(paperDTOPage.map(DTOConvertToVOUtil::convertPaperDTO));
    }



//
//    @PutMapping("/addProblem")
//    @RequireTeacher
//    public BaseResult<ProblemVO> addProblem(@Validated @RequestBody ProblemAddRequest problemAddRequest,
//                                            HttpSession session) {
//        Long userId = getUserId(session);
//        ProblemDTO problemDTO = paperService.addProblem(userId, problemAddRequest);
//        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
//        return new BaseResult<>(ResultEnum.SUCCESS, problemVO);
//    }
//
//    @PostMapping("/editProblem")
//    @RequireTeacher
//    public BaseResult<PaperDetailsVO> editProblem(@Validated @RequestBody ProblemEditRequest problemEditRequest,
//                                                 HttpSession session) {
//        PaperDetailDTO paperDetailDTO = paperService.editProblem(problemEditRequest, getUserId(session));
//        PaperDetailsVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
//        return new BaseResult<>(ResultEnum.SUCCESS, paperDetailVO);
//    }
//
//    @PutMapping("/addImageInProblem")
//    @RequireTeacher
//    public BaseResult<ProblemVO> addImageInProblem(ImageInProblemAddRequest imageInProblemAddRequest,
//                                                   HttpSession session) throws IOException {
//        Long userId = getUserId(session);
//        ProblemDTO problemDTO = paperService.addImageInProblem(userId, imageInProblemAddRequest);
//        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
//        return new BaseResult<>(ResultEnum.SUCCESS, problemVO);
//    }
//
//    @DeleteMapping("/deleteImageInProblem")
//    @RequireTeacher
//    public BaseResult<ProblemVO> deleteImageInProblem(ImageInProblemDeleteRequest imageInProblemDeleteRequest,
//                                                      HttpSession session) {
//        Long userId = getUserId(session);
//        ProblemDTO problemDTO = paperService.deleteImageInProblem(userId, imageInProblemDeleteRequest);
//        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
//        return new BaseResult<>(ResultEnum.SUCCESS, problemVO);
//    }
//
//    @DeleteMapping("/deleteProblem")
//    @RequireTeacher
//    public BaseResult<PaperDetailsVO> deleteProblem(ProblemDeleteRequest problemDeleteRequest,
//                                                   HttpSession session) {
//        Long userId = getUserId(session);
//        paperService.deleteProblem(userId, problemDeleteRequest);
//        return new BaseResult<>(ResultEnum.SUCCESS);
//    }
//
//    @DeleteMapping("/delete")
//    @RequireTeacher
//    public BaseResult<Void> deletePaper(@NotNull(message = "试卷id不能为空!") Long paperId,
//                                        HttpSession session) {
//        Long userId = getUserId(session);
//        paperService.deletePaper(userId, paperId);
//        return new BaseResult<>(ResultEnum.SUCCESS);
//    }



}
