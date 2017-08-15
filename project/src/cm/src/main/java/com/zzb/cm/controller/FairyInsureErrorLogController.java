package com.zzb.cm.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.controller.BaseController;
import com.zzb.cm.entity.INSBFairyInsureErrorLog;
import com.zzb.cm.service.INSBFairyInsureErrorLogService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * author: wz
 * date: 2017/3/27.
 */
@Controller
@RequestMapping("/business/insureerror/*")
public class FairyInsureErrorLogController extends BaseController {

    @Resource
    INSBFairyInsureErrorLogService fairyInsureErrorLogService;

    @RequestMapping(value = "fairyinsureerrorspage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView queryPage() {
        ModelAndView mav = new ModelAndView("cm/fairyinsureerrorlog/fairyinsureerrorlogpage");
        return mav;
    }


    @RequestMapping(value = "fairyinsureerrors", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject query(
            @RequestParam(required = false) String taskId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order) {

        INSBFairyInsureErrorLog insureErrorLog = new INSBFairyInsureErrorLog();
        insureErrorLog.setTaskId(taskId);

        Map<String, Object> query = new HashMap<>();
        query.put("startTime", startTime);
        query.put("endTime", endTime);
        query.put("taskId", taskId);

        if (offset != null && limit != null) {
            query.put("offset", offset);
            query.put("limit", limit);
        }

        String sorting = null;
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            sorting = sort + " " + order;
        }
        if (!StringUtils.isEmpty(sorting)) {
            query.put("sorting", sorting);
        }

        JSONObject result = new JSONObject();

        Page<Map<String, Object>> page = fairyInsureErrorLogService.queryPageList(query, null);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(page.getContent());

        result.put("total", page.getTotalElements());
        result.put("rows", jsonArray);
        return result;
    }
}
