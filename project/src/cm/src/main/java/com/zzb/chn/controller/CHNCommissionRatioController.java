package com.zzb.chn.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.chn.entity.INSBCommissionratio;
import com.zzb.chn.service.CHNCommissionRateService;
import com.zzb.chn.service.CHNCommissionRatioService;
import com.zzb.extra.controller.vo.CommissionVo;
import com.zzb.extra.controller.vo.ConditionsVo;
import com.zzb.extra.entity.INSBCommissionRate;
import com.zzb.extra.entity.INSBConditionParams;
import com.zzb.extra.entity.INSBConditions;
import com.zzb.extra.service.INSBConditionParamsService;
import com.zzb.extra.service.INSBConditionsService;
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
@RequestMapping("/channelCommissionRatio/*")
public class CHNCommissionRatioController extends BaseController {

    @Resource
    private CHNCommissionRatioService insbCommissionRatioService;

    @Resource
    private INSBConditionsService insbConditionsService;

    @Resource
    private INSBConditionParamsService insbConditionParamsService;

    @Resource
    private INSCCodeService codeService;

    @RequestMapping(value = "/commissionRatio", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView commissionRate() throws ControllerException {
        ModelAndView mav = new ModelAndView("chn/commissionRatio");
        //佣金类型
        List<INSCCode> commissionTypeList = codeService.queryINSCCodeByCode("market", "commissionType");
        mav.addObject("commissionTypeList", commissionTypeList);


        //条件参数
        List<INSBConditionParams> paramList = insbConditionParamsService.queryConditionParamsByTag("04");
        mav.addObject("paramList", paramList);
        return mav;
    }

    @RequestMapping(value = "initChannelList", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> initChannelList(HttpSession session) throws ControllerException {
        return insbCommissionRatioService.initChannelList();
    }

    /**
     * 查询佣金配置
     *
     * @param para
     * @param commissionVo
     * @return
     * @throws com.cninsure.core.exception.ControllerException
     */
    @RequestMapping(value = "/queryCommissionRatio", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryCommissionRatio(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute CommissionVo commissionVo) throws ControllerException {
        Map<String, Object> map = BeanUtils.toMap(commissionVo, para);
        //System.out.println(map);
        return insbCommissionRatioService.queryPagingList(map);
    }

    /**
     * 新增佣金配置
     *
     * @param commissionRatio
     * @return
     * @throws com.cninsure.core.exception.ControllerException
     */
    @RequestMapping(value = "/addCommissionRatio", method = RequestMethod.POST)
    @ResponseBody
    public String addCommissionRatio(HttpSession session, @ModelAttribute INSBCommissionratio commissionRatio) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        commissionRatio.setOperator(user.getUsercode());
        return insbCommissionRatioService.addCommissionRatio(commissionRatio);
    }

    /**
     * 批量修改佣金系数状态
     *
     * @param ratioIds
     * @return
     */
    @RequestMapping(value = "/batchUpdateCommissionRatioStatus", method = RequestMethod.GET)
    public Map<String, String> batchUpdateCommissionRatioStatus(HttpSession session,String ratioIds,String status) {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        String userCode = user.getUsercode();
        return insbCommissionRatioService.batchUpdateCommissionRatioStatus(ratioIds, status, userCode);
    }

    /**
     * 复制并新增佣金配置
     *
     * @param commissionratioid
     * @return
     * @throws com.cninsure.core.exception.ControllerException
     */
    @RequestMapping(value = "/copyCommissionRatio", method = RequestMethod.POST)
    @ResponseBody
    public String copyCommissionRatio(HttpSession session, @RequestParam String commissionratioid) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        return insbCommissionRatioService.copyCommissionRatio(commissionratioid, user.getUsercode());
    }

    /**
     * 修改佣金配置
     *
     * @param commissionRatio
     * @return
     * @throws com.cninsure.core.exception.ControllerException
     */
    @RequestMapping(value = "/updateCommissionRatio", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String updateCommissionRatio(HttpSession session, @ModelAttribute INSBCommissionratio commissionRatio) throws ControllerException {
        return insbCommissionRatioService.updateCommissionRatio(commissionRatio);
    }

    /**
     * 修改佣金配置状态
     *
     * @param commissionRatio
     * @return
     * @throws com.cninsure.core.exception.ControllerException
     */
    @RequestMapping(value = "/updateCommissionRatioStatus", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String updateCommissionRatioStatus(HttpSession session, @ModelAttribute INSBCommissionratio commissionRatio) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        commissionRatio.setOperator(user.getUsercode());
        return insbCommissionRatioService.updateCommissionRatioStatus(commissionRatio);
    }

    /**
     * 删除佣金配置
     *
     * @param id
     * @return
     * @throws com.cninsure.core.exception.ControllerException
     */
    @RequestMapping(value = "/delCommissionRatio", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String delCommissionRate(HttpSession session, String id) throws ControllerException {
        return insbCommissionRatioService.delCommissionRatio(id);
    }

    /**
     * 查询配置条件项
     *
     * @param para
     * @param conditionsVo
     * @return
     * @throws com.cninsure.core.exception.ControllerException
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
     * @throws com.cninsure.core.exception.ControllerException
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
     * @throws com.cninsure.core.exception.ControllerException
     */
    @RequestMapping(value = "/delConditions", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String delConditions(HttpSession session, String id) throws ControllerException {
        return insbConditionsService.deleteConditionsById(id);
    }

    /**
     * 批量复制佣金配置
     *
     * @param ratioIds
     * @param channelIds 已选择的供应商协议ID
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/batchCopyCommissionRatio", method = RequestMethod.POST)
    @ResponseBody
    public String batchCopyCommissionRate(HttpSession session, @RequestParam String ratioIds,@RequestParam String channelIds) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        return insbCommissionRatioService.batchCopyCommissionRatio(ratioIds, channelIds, user.getUsercode());
    }

    /**
     * 特定时间关闭佣金配置
     *
     * @param commissionRatio
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/updateCommissionRatioTerminalTime", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String updateCommissionRatioTerminalTime(HttpSession session, @ModelAttribute INSBCommissionratio commissionRatio) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        commissionRatio.setOperator(user.getUsercode());
        return insbCommissionRatioService.updateCommissionRatioTerminalTime(commissionRatio);
    }

    /**
     * 批量特定时间关闭佣金配置
     *
     * @param ratioIds
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/batchUpdateCommissionRatioTerminalTime", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String batchUpdateCommissionRatioTerminalTime(HttpSession session,String ratioIds,Date terminalTime) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        return insbCommissionRatioService.batchUpdateCommissionRatioTerminalTime(ratioIds, terminalTime, user.getUsercode());
    }

    /**
     * 批量删除佣金配置
     * @param rateIds 已选择的佣金配置ID
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/batchDeleteCommissionRatio", method = RequestMethod.POST)
    @ResponseBody
    public String batchDeleteCommissionRatio(HttpSession session, @RequestParam String ratioIds) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        return insbCommissionRatioService.batchDeleteCommissionRatio(ratioIds, user.getUsercode());
    }
}
