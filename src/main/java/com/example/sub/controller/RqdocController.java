package com.example.sub.controller;

import com.example.sub.domain.dto.rqdoc.*;
import com.example.sub.domain.dto.user.SignUpDto;
import com.example.sub.service.rqdoc.RqdocService;
import com.example.sub.service.user.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RqdocController {

    final private RqdocService rqdocService;

    public RqdocController(RqdocService rqdocService) {
        this.rqdocService = rqdocService;
    }

    //신청
    @RequestMapping(value = "/subscription/apply", method = RequestMethod.POST)
    public void apply(@RequestBody RqdocApplyDto rqdocApplyDto) throws Exception {
        rqdocService.apply(rqdocApplyDto);
    }

    @RequestMapping(value = "/subscription/{email}", method = RequestMethod.GET)
    public List<RqdocGetDto> getListByEmail (@PathVariable String email) {
        return rqdocService.getListByEmail(email);
    }

    @RequestMapping(value = "/subscription/pay", method = RequestMethod.POST)
    public void pay(@RequestBody PayDto payDto) throws Exception {
        rqdocService.pay(payDto);
    }

    @RequestMapping(value = "/manage/rqdoc/progrscd", method = RequestMethod.POST)
    public void updateProgrsCd(@RequestBody RqdocProgrsDto rqdocProgrsDto) throws Exception {
        rqdocService.updateProgrsCd(rqdocProgrsDto);
    }

    @RequestMapping(value = "/manage/rqdocs", method = RequestMethod.POST)
    public List<RqdocManageGetResDto> getRqdocsByProgsCd (@RequestBody RqdocManageGetReqDto rqdocManageGetReqDto) {
        return rqdocService.getRqdocsByProgsCd(rqdocManageGetReqDto);
    }

    @RequestMapping(value = "/manage/rqdoc/counts", method = RequestMethod.GET)
    public RqdocCountsDto getCounts () {
        return rqdocService.getCounts();
    }

}
