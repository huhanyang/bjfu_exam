package com.bjfu.exam.controller;

import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.enums.ResponseBodyEnum;
import com.bjfu.exam.request.PaperCreateRequest;
import com.bjfu.exam.service.PaperService;
import com.bjfu.exam.vo.PaperVO;
import com.bjfu.exam.vo.ResponseBody;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController("/paper")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @GetMapping("/get")
    public ResponseBody<PaperVO> getPaper(String code) {
        if(StringUtils.isEmpty(code)) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        Paper paper = paperService.getPaper(code);
        if(paper != null) {
            PaperVO paperVO = new PaperVO();
            BeanUtils.copyProperties(paper, paperVO);
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperVO);
        }
        return new ResponseBody<>(ResponseBodyEnum.FIND_FAILED);
    }

    @PostMapping("/create")
    public ResponseBody<PaperVO> createPaper(PaperCreateRequest paperCreateRequest, HttpSession session) {
        if(paperCreateRequest.isComplete()) {
            Paper paper = paperService.createPaper(paperCreateRequest, (Long) session.getAttribute("userId"));
            if(paper == null) {
                return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
            }
            PaperVO paperVO = new PaperVO();
            BeanUtils.copyProperties(paper, paperVO);
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperVO);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }

    @DeleteMapping("/delete")
    public ResponseBody<Void> deletePaper(Long paperId, HttpSession session) {
        if(paperId == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(session.getAttribute("userId") == null) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        paperService.deletePaper(userId, paperId);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
    }

}
