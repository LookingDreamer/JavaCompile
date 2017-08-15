package com.zzb.extra.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.extra.controller.vo.CommissionVo;
import com.zzb.extra.controller.vo.ConditionsVo;
import com.zzb.extra.entity.INSBCommissionRate;
import com.zzb.extra.entity.INSBConditionParams;
import com.zzb.extra.entity.INSBConditions;
import com.zzb.extra.model.AccountDetailsQueryModel;
import com.zzb.extra.service.INSBCommissionRateService;
import com.zzb.extra.service.INSBConditionParamsService;
import com.zzb.extra.service.INSBConditionsService;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by MagicYuan on 2016/5/23.
 */
@Controller
@RequestMapping("/market/*")
public class MarketController extends BaseController {

    @Resource
    private INSBCommissionRateService insbCommissionRateService;

    @Resource
    private INSBConditionsService insbConditionsService;

    @Resource
    private INSBConditionParamsService insbConditionParamsService;

    @Resource
    private INSCCodeService codeService;

    @RequestMapping(value = "/commissionRate", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView commissionRate() throws ControllerException {
        ModelAndView mav = new ModelAndView("extra/commissionRate");
        //佣金类型
        List<INSCCode> commissionTypeList = codeService.queryINSCCodeByCode("market", "commissionType");
        mav.addObject("commissionTypeList", commissionTypeList);

        //条件参数
        List<INSBConditionParams> paramList = insbConditionParamsService.queryConditionParamsByTag("02");
        mav.addObject("paramList", paramList);
        return mav;
    }

    @RequestMapping(value = "initAgreementList", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> initAgreementList(HttpSession session) throws ControllerException {
        return insbCommissionRateService.initAgreementList();
    }

    /**
     * 查询佣金配置
     *
     * @param para
     * @param commissionVo
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryCommissionRate", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryCommissionRate(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute CommissionVo commissionVo) throws ControllerException {
        Map<String, Object> map = BeanUtils.toMap(commissionVo, para);
        System.out.println(map);
        map.put("productcode","minizzb");
        return insbCommissionRateService.queryPagingList(map);
    }

    /**
     * 新增佣金配置
     *
     * @param commissionRate
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/addCommissionRate", method = RequestMethod.POST)
    @ResponseBody
    public String addCommissionRate(HttpSession session, @ModelAttribute INSBCommissionRate commissionRate) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        commissionRate.setOperator(user.getUsercode());
        commissionRate.setProductcode("minizzb");
        commissionRate.setChannelsource("minizzb");
        System.out.println("=============="+commissionRate.getChannelsource());
        return insbCommissionRateService.addCommissionRate(commissionRate);
    }

    /**
     * 复制并新增佣金配置
     *
     * @param commissionRateId
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/copyCommissionRate", method = RequestMethod.POST)
    @ResponseBody
    public String copyCommissionRate(HttpSession session, @RequestParam String commissionRateId) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        return insbCommissionRateService.copyCommissionRate(commissionRateId, user.getUsercode());
    }

    /**
     * 修改佣金配置
     *
     * @param commissionRate
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/updateCommissionRate", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String updateCommissionRate(HttpSession session, @ModelAttribute INSBCommissionRate commissionRate) throws ControllerException {
        commissionRate.setChannelsource("minizzb");
        return insbCommissionRateService.updateCommissionRate(commissionRate);
    }

    /**
     * 修改佣金配置状态
     *
     * @param commissionRate
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/updateCommissionRateStatus", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String updateCommissionRateStatus(HttpSession session, @ModelAttribute INSBCommissionRate commissionRate) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        commissionRate.setOperator(user.getUsercode());
        commissionRate.setProductcode("minizzb");
        commissionRate.setChannelsource("minizzb");
        return insbCommissionRateService.updateCommissionRateStatus(commissionRate);
    }

    /**
     * 删除佣金配置
     *
     * @param id
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/delCommissionRate", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String delCommissionRate(HttpSession session, String id) throws ControllerException {
        return insbCommissionRateService.delCommissionRate(id);
    }

    /**
     * 查询配置条件项
     *
     * @param para
     * @param conditionsVo
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryConditions", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryConditions(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute ConditionsVo conditionsVo) throws ControllerException {
        Map<String, Object> map = BeanUtils.toMap(conditionsVo, para);
        System.out.println(map);
        return insbConditionsService.queryPagingList(map);
    }

    /**
     * 保存配置条件项
     *
     * @param conditionsVo
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/saveConditions", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String saveConditions(HttpSession session, @ModelAttribute INSBConditions conditionsVo) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        conditionsVo.setOperator(user.getUsercode());
        return insbConditionsService.saveConditions(conditionsVo);
    }

    /**
     * 删除配置条件项
     *
     * @param id
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/delConditions", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String delConditions(HttpSession session, String id) throws ControllerException {
        return insbConditionsService.deleteConditionsById(id);
    }
}
