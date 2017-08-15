package com.zzb.mobile.controller;

import com.zzb.conf.service.INSBOrderdeliveryService;
import com.zzb.mobile.model.CommonModel;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by @Author Chen Haoming on 2016/6/29.
 */
@Controller
@RequestMapping("/mobile/basedata/myDelivery/*")
public class AppMyDeliveryController {

    @Resource
    private INSBOrderdeliveryService insbOrderdeliveryService;

    @RequestMapping(value = "getDeliveryDetail", method = RequestMethod.GET)
    @ResponseBody
    public CommonModel getDelivery(@RequestParam String taskId, @RequestParam String insComCode) {
        CommonModel commonModel = new CommonModel();
        Map<String, Object> orderDeliveryDetail = insbOrderdeliveryService.getOrderDeliveryDetail(taskId, insComCode);
        commonModel.setStatus(orderDeliveryDetail.isEmpty() ? CommonModel.STATUS_FAIL : CommonModel.STATUS_SUCCESS);
        if (orderDeliveryDetail.isEmpty()) {
            commonModel.setStatus(CommonModel.STATUS_FAIL);
            commonModel.setMessage("没有该任务的配送信息！");
        } else {
            commonModel.setStatus(CommonModel.STATUS_SUCCESS);
            commonModel.setMessage("查询配送信息成功！");
            commonModel.setBody(orderDeliveryDetail);
        }
        return commonModel;
    }
}
