package com.bjfu.exam.controller;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.answer.PaperAnswerDetailDTO;
import com.bjfu.exam.dto.answer.ProblemAnswerDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.request.answer.PaperAnswerCreateRequest;
import com.bjfu.exam.request.answer.ProblemAnswerSubmitRequest;
import com.bjfu.exam.security.annotation.RequireStudent;
import com.bjfu.exam.service.AnswerService;
import com.bjfu.exam.util.DTOConvertToVOUtil;
import com.bjfu.exam.util.SessionUtil;
import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.vo.answer.PaperAnswerDetailVO;
import com.bjfu.exam.vo.answer.PaperAnswerVO;
import com.bjfu.exam.vo.answer.ProblemAnswerVO;
import com.bjfu.exam.vo.paper.ProblemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @GetMapping("/getPaperAnswers")
    @RequireStudent
    public BaseResult<List<PaperAnswerDetailVO>> getPaperAnswers(HttpSession session) {
        List<PaperAnswerDetailDTO> paperAnswerDetailDTOS =
                answerService.getPaperAnswers((Long) session.getAttribute("userId"));
        List<PaperAnswerDetailVO> paperAnswerDetailVOS = paperAnswerDetailDTOS.stream()
                .map(DTOConvertToVOUtil::convertPaperAnswerDetailDTO)
                .collect(Collectors.toList());
        return new BaseResult<>(ResultEnum.SUCCESS, paperAnswerDetailVOS);
    }

    @GetMapping("/getPaperAnswerDetail")
    @RequireStudent
    public BaseResult<PaperAnswerDetailVO> getPaperAnswerDetail(Long paperAnswerId, HttpSession session) {
        if(paperAnswerId == null) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        PaperAnswerDetailDTO paperAnswerDetailDTO =
                answerService.getPaperAnswerDetail((Long) session.getAttribute("userId"), paperAnswerId);
        PaperAnswerDetailVO paperAnswerDetailVO = DTOConvertToVOUtil.convertPaperAnswerDetailDTO(paperAnswerDetailDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, paperAnswerDetailVO);
    }

    @PutMapping("/createPaperAnswer")
    @RequireStudent
    public BaseResult<PaperAnswerVO> createPaperAnswer(@RequestBody PaperAnswerCreateRequest paperAnswerCreateRequest,
                                                       HttpSession session) {
        if(!paperAnswerCreateRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        PaperAnswerDTO paperAnswerDTO =
                answerService.createPaperAnswer((Long) session.getAttribute("userId"), paperAnswerCreateRequest);
        PaperAnswerVO paperAnswerVO = DTOConvertToVOUtil.convertPaperAnswerDTO(paperAnswerDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, paperAnswerVO);
    }

    @GetMapping("/getNextProblem")
    @RequireStudent
    public BaseResult<ProblemVO> getNextProblem(Long paperAnswerId, HttpSession session) {
        if(paperAnswerId == null) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        ProblemDTO problemDTO =
                answerService.getNextProblem((Long) session.getAttribute("userId"), paperAnswerId);
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, problemVO);
    }

    @PutMapping("/submitAnswer")
    @RequireStudent
    public BaseResult<ProblemAnswerVO> submitAnswer(@RequestBody ProblemAnswerSubmitRequest problemAnswerSubmitRequest,
                                                    HttpSession session) {
        if(!problemAnswerSubmitRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        ProblemAnswerDTO problemAnswerDTO =
                answerService.submitAnswer((Long) session.getAttribute("userId"), problemAnswerSubmitRequest);
        ProblemAnswerVO problemAnswerVO = DTOConvertToVOUtil.convertProblemAnswerDTO(problemAnswerDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, problemAnswerVO);
    }


}
