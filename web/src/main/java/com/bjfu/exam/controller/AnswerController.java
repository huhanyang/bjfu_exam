package com.bjfu.exam.controller;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.request.answer.PaperAnswerCreateRequest;
import com.bjfu.exam.request.answer.ProblemAnswerSubmitRequest;
import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.service.AnswerService;
import com.bjfu.exam.utils.DTOConvertToVOUtil;
import com.bjfu.exam.vo.answer.PaperAnswerVO;
import com.bjfu.exam.vo.paper.ProblemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @GetMapping("/getPaperAnswers")
    public BaseResult<List<PaperAnswerVO>> getPaperAnswers(HttpSession session) {
        List<PaperAnswerDTO> paperAnswerDetailDTOS =
                answerService.getPaperAnswers((Long) session.getAttribute("userId"));
        List<PaperAnswerVO> paperAnswerDetailVOS = paperAnswerDetailDTOS.stream()
                .map(DTOConvertToVOUtil::convertPaperAnswerDTO)
                .collect(Collectors.toList());
        return new BaseResult<>(ResultEnum.SUCCESS, paperAnswerDetailVOS);
    }

    @PutMapping("/createPaperAnswer")
    public BaseResult<PaperAnswerVO> createPaperAnswer(@RequestBody PaperAnswerCreateRequest paperAnswerCreateRequest,
                                                       HttpSession session) {
        PaperAnswerDTO paperAnswerDTO =
                answerService.createPaperAnswer((Long) session.getAttribute("userId"), paperAnswerCreateRequest);
        PaperAnswerVO paperAnswerVO = DTOConvertToVOUtil.convertPaperAnswerDTO(paperAnswerDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, paperAnswerVO);
    }

    @GetMapping("/getNextProblem")
    public BaseResult<ProblemVO> getNextProblem(@NotNull(message = "答卷id不能为空!") Long paperAnswerId, HttpSession session) {
        ProblemDTO problemDTO =
                answerService.getNextProblem((Long) session.getAttribute("userId"), paperAnswerId);
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, problemVO);
    }

    @PutMapping("/submitAnswer")
    public BaseResult<Void> submitAnswer(@RequestBody ProblemAnswerSubmitRequest problemAnswerSubmitRequest,
                                                    HttpSession session) {
        answerService.submitAnswer((Long) session.getAttribute("userId"), problemAnswerSubmitRequest);
        return new BaseResult<>(ResultEnum.SUCCESS);
    }


}
