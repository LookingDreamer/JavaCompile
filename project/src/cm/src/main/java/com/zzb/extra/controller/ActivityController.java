package com.zzb.extra.controller;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
import com.zzb.extra.entity.INSBMarketActivity;
import com.zzb.extra.entity.INSBMarketActivityPrize;
import com.zzb.extra.entity.INSBMarketPrize;
import com.zzb.extra.model.MarketActivityQueryModel;
import com.zzb.extra.service.INSBConditionParamsService;
import com.zzb.extra.service.INSBConditionsService;
import com.zzb.extra.service.INSBMarketActivityPrizeService;
import com.zzb.extra.service.INSBMarketActivityService;
import com.zzb.extra.service.INSBMarketPrizeService;
import com.zzb.extra.util.ParamUtils;

@Controller
@RequestMapping("/activity/*")
public class ActivityController extends BaseController {
    @Resource
    private INSBMarketActivityService insbMarketActivityService;

    @Resource
    private INSCCodeService codeService;

    @Resource
    private INSBMarketPrizeService insbMarketPrizeService;

    @Resource
    private INSBMarketActivityPrizeService insbMarketActivityPrizeService;

    @Resource
    private INSBConditionsService insbConditionsService;

    @Resource
    private INSBConditionParamsService insbConditionParamsService;

    /*
     *
     * 查询页面跳转和页面初始化
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView getList(Map map) {
        ModelAndView mav = new ModelAndView("extra/market/activityinfo/list");

        //营销活动类型
        List<INSCCode> marketActivityTypeList = codeService.queryINSCCodeByCode("market", "activityType");
        mav.addObject("marketActivityTypeList", marketActivityTypeList);

        //活动奖品列表
        List<INSBMarketPrize> marketPrizeList = insbMarketPrizeService.getPrizeList(map);
        mav.addObject("marketPrizeList", marketPrizeList);

        //条件参数
        List<INSBConditionParams> paramList = insbConditionParamsService.queryConditionParamsByTag("01");
        mav.addObject("paramList", paramList);
        return mav;
    }

    /**
     *
     * @param type 根据活动类型查询对应的活动信息
     * @return
     */
    @RequestMapping(value = "getAllList", method = RequestMethod.GET)
    @ResponseBody
    public String getAllList(String type) {
        Map<String, Object> map = new HashMap();
        map.put("type",type);
        return insbMarketActivityService.getList(map);
    }

    /**
     *
     *
     * @return
     */
    @RequestMapping(value = "queryActivityList", method = RequestMethod.GET)
    @ResponseBody
    public String queryActivityList(@ModelAttribute PagingParams para,@ModelAttribute MarketActivityQueryModel marketActivityQueryModel) {
        Map<String, Object> map = BeanUtils.toMap(para,marketActivityQueryModel);
        return insbMarketActivityService.queryActivity(map);
    }


    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView editObject(String  id) {
        Map insbMarketActivity=insbMarketActivityService.findById(id);
        ModelAndView mav = new ModelAndView("extra/market/activityinfo/edit");
        //营销活动类型<refresh>
        List<INSCCode> marketActivityTypeList = codeService.queryINSCCodeByCode("market", "activityType");
        mav.addObject("marketActivityTypeList", marketActivityTypeList);
        mav.addObject("insbMarketActivity", insbMarketActivity);
        return mav;
    }


    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public String  saveObject(HttpSession session,INSBMarketActivity insbMarketActivity) throws ParseException {
        try {
            Calendar ca = Calendar.getInstance();
            INSCUser user = (INSCUser) session.getAttribute("insc_user");
            
            Long tmpcode = insbMarketActivityService.selectMaxTmpcode();
            
            if(tmpcode == null) {
            	tmpcode = 1L;
            } else {
            	tmpcode++;
            }
            if (StringUtil.isEmpty(insbMarketActivity.getId())) {
                insbMarketActivity.setId(UUIDUtils.random());
                insbMarketActivity.setTmpcode(tmpcode.intValue());
                insbMarketActivity.setActivitycode("hd"+ StringUtil.leftPad(String.valueOf(tmpcode.intValue()), '0', 7));
                
                insbMarketActivity.setCreatetime(ca.getTime());
                insbMarketActivity.setOperator(user.getUsercode());
                insbMarketActivityService.saveObject(insbMarketActivity);
            } else {
                //检查交易活动开启时是否已经配置奖品和规则
                //"1"活动开启,"01"交易活动<!--add-->
                if("1".equals(insbMarketActivity.getStatus())&&"01".equals(insbMarketActivity.getActivitytype())) {
                    Map<String,Long> prizeAndConditions = insbMarketActivityService.validatePrizeAndConditions(insbMarketActivity.getId());
                    if (prizeAndConditions.get("activityPrizeCount")==0||prizeAndConditions.get("activityConditionsCount")==0) {
                        return "fail";
                    }
                }
                insbMarketActivity.setModifytime(ca.getTime());
                insbMarketActivity.setOperator(user.getUsercode());
                insbMarketActivityService.updateObject(insbMarketActivity);
            }
        }catch(Exception ex){
           return "fail";
        }
        return "success";
    }
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteObject(String id) {
        try {
            //检查活动是否已经配置奖品和规则，如果已经配置，提示不能直接删除
            Map<String, Long> prizeAndConditions = insbMarketActivityService.validatePrizeAndConditions(id);
            if (prizeAndConditions.get("activityPrizeCount") > 0 || prizeAndConditions.get("activityConditionsCount") > 0) {
                return "fail";
            } else {
                insbMarketActivityService.deleteObject(id);
                return "success";
            }
        }catch (Exception ex){
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     *
     * 根据活动ID查找奖品列表
     *
     */
    @RequestMapping(value = "queryActivityPrizeListById", method = RequestMethod.GET)
    @ResponseBody
    public String queryActivityPrizeListById(String id) {
        return insbMarketActivityService.queryActivityPrizeListById(id);
    }

    /**
     *
     * 根据活动ID查找规则列表
     *
     */
    @RequestMapping(value = "queryActivityConditionListById", method = RequestMethod.GET)
    @ResponseBody
    public String queryActivityConditionListById(String id,String conditionsource) {
        Map map = new HashMap<>();
        map.put("sourceid", id);
        map.put("conditionsource", conditionsource);
        insbConditionsService.queryPagingList(map);
        return insbConditionsService.queryPagingList(map);
    }

    @RequestMapping(value = "saveMarketActivityPrize", method = RequestMethod.POST)
    @ResponseBody
    public String saveMarketActivityPrize(HttpSession session,INSBMarketActivityPrize insbMarketActivityPrize) {
        try{
            Calendar ca = Calendar.getInstance();
            INSCUser user = (INSCUser) session.getAttribute("insc_user");
            if (StringUtil.isEmpty(insbMarketActivityPrize.getId())) {
                insbMarketActivityPrize.setId(UUIDUtils.random());
                insbMarketActivityPrize.setCreatetime(ca.getTime());
                insbMarketActivityPrize.setOperator(user.getUsercode());
                insbMarketActivityPrizeService.saveObject(insbMarketActivityPrize);
            } else {
                insbMarketActivityPrize.setModifytime(ca.getTime());
                insbMarketActivityPrize.setOperator(user.getUsercode());
                insbMarketActivityPrizeService.updateObject(insbMarketActivityPrize);
            }
            return "success";
        }catch (Exception ex){
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    @RequestMapping(value = "saveMarketActivityCondition", method = RequestMethod.POST)
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

    @RequestMapping(value = "deleteActivityPrize", method = RequestMethod.POST)
    @ResponseBody
    public String deleteActivityPrize(String id) {
        insbMarketActivityPrizeService.deleteObject(id);
        return "success";
    }

    @RequestMapping(value = "deleteActivityCondition", method = RequestMethod.POST)
    @ResponseBody
    public String deleteActivityCondition(String id) {
        insbConditionsService.deleteConditionsById(id);
        return "success";
    }





}
