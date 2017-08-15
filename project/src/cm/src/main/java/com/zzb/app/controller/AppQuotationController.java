package com.zzb.app.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzb.app.model.bean.QuotationInfo;
import com.zzb.app.service.AppQuotationService;

@Controller
@RequestMapping("/quotation/*")
public class AppQuotationController {
    @Resource
    AppQuotationService appQuotationService;

    @RequestMapping(value = "/getQuotationInfo", method = RequestMethod.POST)
    @ResponseBody
    public String getQuotationInfo(@RequestBody QuotationInfo qi) {
        String result = "";
        result = appQuotationService.getQuotationInfo(qi.getSubtaskid(), qi.getTaskid(), qi.getInscomcode());
        return result;
    }

    @RequestMapping(value = "/getQuotationValidatedInfo", method = RequestMethod.POST)
    @ResponseBody
    public String getQuotationValidatedInfo(@RequestBody QuotationInfo qi) {
        String result = "";
        result = appQuotationService.getQuotationValidatedInfo(qi.getSubtaskid(), qi.getTaskid(), qi.getInscomcode());
        return result;
    }


    @RequestMapping(value = "/getQuotationInfoForflow", method = RequestMethod.POST)
    @ResponseBody
    public String getQuotationInfoForflow(@RequestBody QuotationInfo qi) {
        String result = "";
        result = appQuotationService.getQuotationInfoForFlow(qi.getSubtaskid(), qi.getTaskid(), qi.getInscomcode());
        return result;
    }
}
