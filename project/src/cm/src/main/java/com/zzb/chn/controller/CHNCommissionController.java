package com.zzb.chn.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.chn.service.CHNCommissionRateService;
import com.zzb.extra.controller.vo.CommissionVo;
import com.zzb.extra.controller.vo.ConditionsVo;
import com.zzb.extra.entity.INSBCommissionRate;
import com.zzb.extra.entity.INSBConditionParams;
import com.zzb.extra.entity.INSBConditions;
import com.zzb.extra.service.INSBCommissionRateService;
import com.zzb.extra.service.INSBConditionParamsService;
import com.zzb.extra.service.INSBConditionsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hwc on 2016/11/7.
 */
@Controller
@RequestMapping("/channelCommission/*")
public class CHNCommissionController extends BaseController {

    @Resource
    private CHNCommissionRateService insbCommissionRateService;

    @Resource
    private INSBConditionsService insbConditionsService;

    @Resource
    private INSBConditionParamsService insbConditionParamsService;

    @Resource
    private INSCCodeService codeService;

    @RequestMapping(value = "/commissionRate", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView commissionRate() throws ControllerException {
        ModelAndView mav = new ModelAndView("chn/commissionRate");
        //佣金类型
        List<INSCCode> commissionTypeList = codeService.queryINSCCodeByCode("market", "commissionType");
        mav.addObject("commissionTypeList", commissionTypeList);

        //应用渠道类型
        List<INSCCode> channelSourceList = codeService.queryINSCCodeByCode("market", "channelSource");
        mav.addObject("channelSourceList", channelSourceList);

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
        map.put("productcode","channel");
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
        commissionRate.setProductcode("channel");
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
        commissionRate.setProductcode("channel");
        return insbCommissionRateService.updateCommissionRateStatus(commissionRate);
    }

    /**
     * 特定时间关闭佣金配置
     *
     * @param commissionRate
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/updateCommissionRateTerminalTime", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String updateCommissionRateTerminalTime(HttpSession session, @ModelAttribute INSBCommissionRate commissionRate) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        commissionRate.setOperator(user.getUsercode());
        commissionRate.setProductcode("channel");
        return insbCommissionRateService.updateCommissionRateTerminalTime(commissionRate);
    }

    /**
     * 批量特定时间关闭佣金配置
     *
     * @param rateIds
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/batchUpdateCommissionRateTerminalTime", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String batchUpdateCommissionRateTerminalTime(HttpSession session,String rateIds,Date terminalTime) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        return insbCommissionRateService.batchUpdateCommissionRateTerminalTime(rateIds,terminalTime,user.getUsercode());
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

    /**
     * 批量修改佣金状态
     *
     * @param rateIds
     * @return
     */
    @RequestMapping(value = "/batchUpdateCommissionRateStatus", method = RequestMethod.GET)
    public Map<String, String> batchUpdateCommissionRateStatus(HttpSession session,String rateIds,String status) {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        String userCode = user.getUsercode();
        return insbCommissionRateService.batchUpdateCommissionRateStatus(rateIds, status,userCode);
    }

    /**
     * 批量复制佣金配置
     *
     * @param rateIds
     * @param agreementIds 已选择的供应商协议ID
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/batchCopyCommissionRate", method = RequestMethod.POST)
    @ResponseBody
    public String batchCopyCommissionRate(HttpSession session, @RequestParam String rateIds,@RequestParam String agreementIds) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        return insbCommissionRateService.batchCopyCommissionRate(rateIds, agreementIds, user.getUsercode());
    }

    /**
     * 批量删除佣金配置
     * @param rateIds 已选择的佣金配置ID
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/batchDeleteCommissionRate", method = RequestMethod.POST)
    @ResponseBody
    public String batchDeleteCommissionRate(HttpSession session, @RequestParam String rateIds) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        return insbCommissionRateService.batchDeleteCommissionRate(rateIds, user.getUsercode());
    }
}
