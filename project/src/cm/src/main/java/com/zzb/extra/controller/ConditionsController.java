package com.zzb.extra.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.extra.controller.vo.ConditionParamsVo;
import com.zzb.extra.entity.INSBConditionParams;
import com.zzb.extra.entity.INSBConditions;
import com.zzb.extra.model.AccountDetailsQueryModel;
import com.zzb.extra.service.INSBConditionParamsService;
import com.zzb.extra.service.INSBConditionsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/conditions/*")
public class ConditionsController extends BaseController {
    @Resource
    private INSBConditionParamsService insbConditionParamsService;

    @Resource
    private INSCCodeService codeService;

    @RequestMapping(value = "/queryParams", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView queryParams() throws ControllerException {
        ModelAndView mav = new ModelAndView("extra/conditionParamsList");
        //参数适用模块
        List<INSCCode> paramTagList = codeService.queryINSCCodeByCode("market", "conditionsource");
        mav.addObject("paramTagList", paramTagList);

        return mav;
    }

    /**
     * 查询条件参数列表
     *
     * @param para
     * @param queryModel
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryParamsList", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryParamsList(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute ConditionParamsVo queryModel) throws ControllerException {
        Map<String, Object> map = BeanUtils.toMap(queryModel, para);
        System.out.println(map);
        return insbConditionParamsService.queryPagingList(map);

    }

    /**
     * 保存条件参数信息
     *
     * @param params
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/saveConditionParam", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String saveConditionParam(HttpSession session, @ModelAttribute INSBConditionParams params) throws ControllerException {
        INSCUser user = (INSCUser) session.getAttribute("insc_user");
        params.setOperator(user.getUsercode());
        return insbConditionParamsService.saveConditionParam(params);
    }

    /**
     * 删除条件参数信息
     *
     * @param id
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/delConditionParam", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String delConditionParam(HttpSession session, String id) throws ControllerException {
        INSCUser operator = (INSCUser) session.getAttribute("insc_user");
        LogUtil.info("条件参数信息删除id为%s,操作人:%s", id, operator.getUsercode());
        return insbConditionParamsService.deleteConditionParam(id);
    }
}
