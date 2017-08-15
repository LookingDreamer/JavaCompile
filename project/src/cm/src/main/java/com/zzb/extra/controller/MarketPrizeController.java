package com.zzb.extra.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.extra.entity.INSBConditionParams;
import com.zzb.extra.entity.INSBConditions;
import com.zzb.extra.entity.INSBMarketPrize;
import com.zzb.extra.model.MarketPrizeQueryModel;
import com.zzb.extra.service.*;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.*;

/**
 * Created by heweicheng on 2016/5/30.
 */
@Controller
@RequestMapping("/marketPrize/*")
public class MarketPrizeController extends BaseController {

    @Resource
    private INSBMarketPrizeService insbMarketPrizeService;

    @Resource
    private INSCCodeService codeService;

    @Resource
    private INSBConditionsService insbConditionsService;

    @Resource
    private INSBMarketActivityService insbMarketActivityService;

    @Resource
    private INSBConditionParamsService insbConditionParamsService;

    @Resource
    private INSBMarketActivityPrizeService insbMarketActivityPrizeService;
    /*
     *
     * 查询页面跳转和页面初始化
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView getList(Map map) {
        ModelAndView mav = new ModelAndView("extra/market/prizeinfo/list");

        //营销奖品类型
        List<INSCCode> marketPrizeTypeList = codeService.queryINSCCodeByCode("market", "prizeType");
        mav.addObject("marketPrizeTypeList", marketPrizeTypeList);
        //营销奖品状态

        //条件参数<!--add refresh-->
        List<INSBConditionParams> paramList = insbConditionParamsService.queryConditionParamsByTag("03");
        mav.addObject("paramList", paramList);
        return mav;
    }

    @RequestMapping(value = "queryMarketPrizeList", method = RequestMethod.GET)
    @ResponseBody
    public String queryActivityList(@ModelAttribute PagingParams para,@ModelAttribute MarketPrizeQueryModel marketPrizeQueryModel) {
        Map<String, Object> map = BeanUtils.toMap(marketPrizeQueryModel,para);
        return insbMarketPrizeService.queryPrizeList(map);
    }


    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView editObject(String  id) {
        Map insbMarketPrize=insbMarketPrizeService.findById(id);
        ModelAndView mav = new ModelAndView("extra/market/prizeinfo/edit");
        //营销奖品类型
        List<INSCCode> marketPrizeTypeList = codeService.queryINSCCodeByCode("market", "prizeType");
        mav.addObject("marketPrizeTypeList", marketPrizeTypeList);
        mav.addObject("insbMarketPrize", insbMarketPrize);
        return mav;
    }

    /**
     *
     * 根据奖品ID查找规则列表
     *
     */
    @RequestMapping(value = "queryPrizeConditionListById", method = RequestMethod.GET)
    @ResponseBody
    public String queryPrizeConditionListById(String id,String conditionsource) {
        Map map = new HashMap<>();
        map.put("sourceid", id);
        map.put("conditionsource", conditionsource);
        //System.out.println("queryActivityConditionListById=" + insbConditionsService.queryPagingList(map));
        insbConditionsService.queryPagingList(map);
        return insbConditionsService.queryPagingList(map);
    }

    @RequestMapping(value = "saveMarketPrizeCondition", method = RequestMethod.POST)
    @ResponseBody
    public String saveMarketActivityCondition(HttpSession session,INSBConditions insbConditions) {
        try{
            INSCUser user = (INSCUser) session.getAttribute("insc_user");
            insbConditions.setOperator(user.getUsercode());
            String result = insbConditionsService.saveConditions(insbConditions);
            return result;
        }catch (Exception ex){
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    @RequestMapping(value = "deletePrizeCondition", method = RequestMethod.POST)
    @ResponseBody
    public String deletePrizeCondition(String id) {
        insbConditionsService.deleteConditionsById(id);
        return "success";
    }


    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public String  saveObject(HttpSession session,INSBMarketPrize insbMarketPrize) throws ParseException {
        try {
            Calendar ca = Calendar.getInstance();
            INSCUser user = (INSCUser) session.getAttribute("insc_user");
            if (StringUtil.isEmpty(insbMarketPrize.getId())) {
                insbMarketPrize.setId(UUIDUtils.random());
                insbMarketPrize.setCreatetime(ca.getTime());
                insbMarketPrize.setModifytime(ca.getTime());
                insbMarketPrize.setOperator(user.getUsercode());
                insbMarketPrizeService.saveObject(insbMarketPrize);
            } else {
                //启用红包，检查是否有配置规则 refreshrefresh
                /*if("1".equals(insbMarketPrize.getStatus())) {
                    INSBConditions conditions = new INSBConditions();
                    conditions.setSourceid(insbMarketPrize.getId());
                    conditions.setConditionsource("03");
                    Long counts = insbConditionsService.queryCount(conditions);
                    if (counts==0) {
                        return "fail";
                    }
                }*/
                insbMarketPrize.setModifytime(ca.getTime());
                insbMarketPrize.setOperator(user.getUsercode());
                insbMarketPrizeService.updateObject(insbMarketPrize);
            }
        }catch(Exception ex){
            return ParamUtils.resultMap(false, "系统错误");
        }
        return "success";
    }
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteObject(String id) {
        try {
            //检查奖品是否已经配置给了活动，如果是则无法删除
            Map map = new HashMap();
            map.put("id",id);
            map.put("prizeid",id);
            Long total = insbMarketActivityPrizeService.queryPagingListCount(map);
            //System.out.println("total="+totalrefresh);
            INSBConditions conditions = new INSBConditions();
            conditions.setSourceid(id);
            conditions.setConditionsource("03");
            Long counts = insbConditionsService.queryCount(conditions);
            //System.out.println("counts="+counts);
            if (total > 0) {
                return "fail";
            } else if (counts>0) {
                return "delConditionFirst";
            } else {
                insbMarketPrizeService.deleteObject((String)map.get("id"));
                return "success";
            }
        }catch (Exception ex){
            //ex.printStackTrace();
            return ParamUtils.resultMap(false, "系统错误");
        }
    }
}
