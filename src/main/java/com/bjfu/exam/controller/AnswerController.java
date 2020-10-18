package com.bjfu.exam.controller;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.answer.PaperAnswerDetailDTO;
import com.bjfu.exam.dto.answer.ProblemAnswerDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.enums.ResponseBodyEnum;
import com.bjfu.exam.request.answer.PaperAnswerCreateRequest;
import com.bjfu.exam.request.answer.ProblemAnswerSubmitRequest;
import com.bjfu.exam.service.AnswerService;
import com.bjfu.exam.util.DTOConvertToVOUtil;
import com.bjfu.exam.util.SessionUtil;
import com.bjfu.exam.vo.ResponseBody;
import com.bjfu.exam.vo.answer.PaperAnswerDetailVO;
import com.bjfu.exam.vo.answer.PaperAnswerVO;
import com.bjfu.exam.vo.answer.ProblemAnswerVO;
import com.bjfu.exam.vo.paper.PolymerizationProblemVO;
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
    public ResponseBody<List<PaperAnswerVO>> getPaperAnswers(HttpSession session) {
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        List<PaperAnswerDTO> paperAnswerDTOS =
                answerService.getPaperAnswers((Long) session.getAttribute("userId"));
        List<PaperAnswerVO> paperAnswerVOS = paperAnswerDTOS.stream()
                .map(DTOConvertToVOUtil::convertPaperAnswerDTO)
                .collect(Collectors.toList());
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperAnswerVOS);
    }

    @GetMapping("/getPaperAnswerDetail")
    public ResponseBody<PaperAnswerDetailVO> getPaperAnswerDetail(Long paperAnswerId, HttpSession session) {
        if(paperAnswerId == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        PaperAnswerDetailDTO paperAnswerDetailDTO =
                answerService.getPaperAnswerDetail((Long) session.getAttribute("userId"), paperAnswerId);
        PaperAnswerDetailVO paperAnswerDetailVO = DTOConvertToVOUtil.convertPaperAnswerDetailDTO(paperAnswerDetailDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperAnswerDetailVO);
    }

    @PutMapping("/createPaperAnswer")
    public ResponseBody<PaperAnswerVO> createPaperAnswer(@RequestBody PaperAnswerCreateRequest paperAnswerCreateRequest,
                                                         HttpSession session) {
        if(!paperAnswerCreateRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        PaperAnswerDTO paperAnswerDTO =
                answerService.createPaperAnswer((Long) session.getAttribute("userId"), paperAnswerCreateRequest);
        PaperAnswerVO paperAnswerVO = DTOConvertToVOUtil.convertPaperAnswerDTO(paperAnswerDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperAnswerVO);
    }

    @GetMapping("/getProblem")
    public ResponseBody<ProblemVO> getProblem(Long paperAnswerId, HttpSession session) {
        if(paperAnswerId == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        ProblemDTO problemDTO =
                answerService.getProblem((Long) session.getAttribute("userId"), paperAnswerId);
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, problemVO);
    }

    @GetMapping("/getPolymerizationProblem")
    public ResponseBody<PolymerizationProblemVO> getPolymerizationProblem(Long paperAnswerId,
                                                                          HttpSession session) {
        if(paperAnswerId == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        PolymerizationProblemDTO polymerizationProblemDTO =
                answerService.getPolymerizationProblem((Long) session.getAttribute("userId"), paperAnswerId);
        PolymerizationProblemVO polymerizationProblemVO =
                DTOConvertToVOUtil.convertPolymerizationProblemDTO(polymerizationProblemDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, polymerizationProblemVO);
    }

    @PutMapping("/submitAnswer")
    public ResponseBody<ProblemAnswerVO> submitAnswer(@RequestBody ProblemAnswerSubmitRequest problemAnswerSubmitRequest,
                                                      HttpSession session) {
        if(!problemAnswerSubmitRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        ProblemAnswerDTO problemAnswerDTO =
                answerService.submitAnswer((Long) session.getAttribute("userId"), problemAnswerSubmitRequest);
        ProblemAnswerVO problemAnswerVO = DTOConvertToVOUtil.convertProblemAnswerDTO(problemAnswerDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, problemAnswerVO);
    }


}