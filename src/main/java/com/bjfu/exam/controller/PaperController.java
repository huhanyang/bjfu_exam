package com.bjfu.exam.controller;

import com.bjfu.exam.dto.PaperDTO;
import com.bjfu.exam.enums.ResponseBodyEnum;
import com.bjfu.exam.request.PaperCreateRequest;
import com.bjfu.exam.service.PaperService;
import com.bjfu.exam.vo.PaperVO;
import com.bjfu.exam.vo.ResponseBody;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/paper")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @GetMapping("/getByCode")
    public ResponseBody<PaperVO> getPaper(String code) {
        if(StringUtils.isEmpty(code)) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        PaperDTO paperDTO = paperService.getPaper(code);
        if(paperDTO != null) {
            PaperVO paperVO = new PaperVO();
            BeanUtils.copyProperties(paperDTO, paperVO);
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperVO);
        }
        return new ResponseBody<>(ResponseBodyEnum.FIND_FAILED);
    }
    @GetMapping("/get")
    public ResponseBody<List<PaperVO>> getMyPaper(HttpSession session) {
        if(session.getAttribute("userId") == null) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        List<PaperDTO> paperDTOS = paperService.getAllPaperByCreatorId((Long) session.getAttribute("userId"));
        List<PaperVO> paperVOS = new ArrayList<>(paperDTOS.size());
        paperDTOS.forEach( paperDTO -> {
            PaperVO paperVO = new PaperVO();
            BeanUtils.copyProperties(paperDTO, paperVO);
            paperVOS.add(paperVO);
        });
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperVOS);
    }

    @PostMapping("/create")
    public ResponseBody<PaperVO> createPaper(PaperCreateRequest paperCreateRequest, HttpSession session) {
        if(paperCreateRequest.isComplete()) {
            if(session.getAttribute("userId") == null) {
                return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
            }
            PaperDTO paperDTO = paperService.createPaper(paperCreateRequest, (Long) session.getAttribute("userId"));
            if(paperDTO == null) {
                return new ResponseBody<>(ResponseBodyEnum.UNKNOWN_WRONG);
            }
            PaperVO paperVO = new PaperVO();
            BeanUtils.copyProperties(paperDTO, paperVO);
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperVO);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }

    @DeleteMapping("/delete")
    public ResponseBody<Void> deletePaper(Long paperId, HttpSession session) {
        if(session.getAttribute("userId") == null) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        if(paperId == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        Long userId = (Long) session.getAttribute("userId");
        paperService.deletePaper(userId, paperId);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
    }

}
