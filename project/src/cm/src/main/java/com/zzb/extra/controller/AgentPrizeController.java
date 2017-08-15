package com.zzb.extra.controller;

import com.cninsure.core.utils.BeanUtils;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.extra.entity.INSBMarketPrize;
import com.zzb.extra.model.MarketActivityQueryModel;
import com.zzb.extra.service.INSBAgentPrizeService;
import com.zzb.extra.service.INSBMarketActivityPrizeService;
import com.zzb.extra.service.INSBMarketActivityService;
import com.zzb.extra.service.INSBMarketPrizeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Heweicheng on 2016/5/31.
 */
@Controller
@RequestMapping("/agentPrize/*")
public class AgentPrizeController {
    @Resource
    private INSBMarketActivityService insbMarketActivityService;

    @Resource
    private INSCCodeService codeService;

    @Resource
    private INSBMarketPrizeService insbMarketPrizeService;

    @Resource
    private INSBMarketActivityPrizeService insbMarketActivityPrizeService;

    @Resource
    private INSBAgentPrizeService insbAgentPrizeService;

    /*
     *
     * 查询页面跳转和页面初始化
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView getList(Map map) {

        ModelAndView mav = new ModelAndView("extra/market/userprize/list");
        //用户奖品使用状态
        List<INSCCode> marketAgentPrizeStatusList = codeService.queryINSCCodeByCode("market", "agentPrizeStatus");
        mav.addObject("marketAgentPrizeStatusList", marketAgentPrizeStatusList);
        return mav;
    }

    @RequestMapping(value = "queryAgentPrizeList", method = RequestMethod.GET)
    @ResponseBody
    public String queryAgentPrizeList(@ModelAttribute PagingParams para,String agentname,String activityname,String prizename,String jobnum,String phone) {
        Map<String, Object> map = BeanUtils.toMap(para);
        map.put("agentname",agentname);
        map.put("activityname",activityname);
        map.put("prizename",prizename);
        map.put("phone",phone);

        map.put("jobnum",jobnum);
        return insbAgentPrizeService.queryAgentPrizeList(map);
    }
}
