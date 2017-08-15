package com.zzb.mobile.controller;

import com.cninsure.core.exception.ControllerException;
import com.zzb.mobile.entity.INSBAgentMarketingNew;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.marketing.LotteryCodeInfo;
import com.zzb.mobile.model.marketing.RankingModel;
import com.zzb.mobile.service.AppMarketingActivitiesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by HaiJuan.Lei on 2016/10/8.
 * 营销活动
 */
@Controller
@RequestMapping("/mobile/marketing/*")
public class AppMarketingActivitiesController{

    @Resource
    private AppMarketingActivitiesService marketingService;

    @RequestMapping(value = "registerSuccess", method = RequestMethod.GET)
    @ResponseBody
    public CommonModel registerSuccess(@RequestParam(value="jobNum") String jobNum) throws ControllerException {
        return marketingService.registerSuccess(jobNum);
    }

    @RequestMapping(value = "getRankingList", method = RequestMethod.GET)
    @ResponseBody
    public RankingModel getRankingList(@RequestParam(value="jobNum") String jobNum) throws ControllerException {
        return marketingService.getRankingList(jobNum);
    }


    @RequestMapping(value = "getWeekRankingList", method = RequestMethod.GET)
    @ResponseBody
    public RankingModel getWeekRankingList(@RequestParam(value="jobNum") String jobNum) throws ControllerException {
        return marketingService.getWeekRankingList(jobNum);
    }

    @RequestMapping(value = "getReferrals", method = RequestMethod.GET)
    @ResponseBody
    public List<INSBAgentMarketingNew> getReferrals(@RequestParam(value="jobNum") String jobNum) throws ControllerException {
        return marketingService.getReferrals(jobNum);
    }

}
