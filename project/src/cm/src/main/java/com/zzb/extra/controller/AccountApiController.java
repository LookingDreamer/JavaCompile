package com.zzb.extra.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.common.PagingParams;
import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.controller.vo.AccountDetailVo;
import com.zzb.extra.controller.vo.AccountWithdrawVo;
import com.zzb.extra.model.AccountDetailsQueryModel;
import com.zzb.extra.model.AccountWithdrawQueryModel;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.extra.service.INSBAccountWithdrawService;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/account/api/*")
public class AccountApiController extends BaseController {

    @Resource
    private INSBAccountDetailsService insbAccountDetailsService;

    @Resource
    private INSBAccountWithdrawService insbAccountWithdrawService;

    /**
     * 账户流水信息查询接口
     *
     * @param para
     * @param queryModel
     * @return
     * @throws
     */
    @RequestMapping(value = "/queryDetails", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryDetails(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute AccountDetailsQueryModel queryModel) throws ControllerException {
        try {
            if (queryModel.getAgentid() == null || queryModel.getAgentid().equals("")) {
                return ParamUtils.resultMap(false, "参数不正确");
            }
            Map<String, Object> map = BeanUtils.toMap(queryModel, para);
            //System.out.println(map);
            return insbAccountDetailsService.queryPagingList(map);
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 提现任务查询接口
     *
     * @param para
     * @param queryModel
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryWithdraw", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryWithdraw(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute AccountWithdrawQueryModel queryModel) throws ControllerException {
        try {
            if (queryModel.getAgentid() == null || queryModel.getAgentid().equals("")) {
                return ParamUtils.resultMap(false, "参数不正确");
            }
            Map<String, Object> map = BeanUtils.toMap(queryModel, para);
            //System.out.println(map);
            return insbAccountWithdrawService.queryPagingList(map);
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");

        }
    }

    /**
     * 提现请求
     *
     * @param accountWithdrawVo
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "putWithdraw", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String putWithdraw(HttpSession session, @RequestBody AccountWithdrawVo accountWithdrawVo) throws ControllerException {
        try {
            return insbAccountWithdrawService.putAccountWithdraw(accountWithdrawVo);
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 账户余额查询接口
     *
     * @param agentid
     * @return
     * @throws
     */
    @RequestMapping(value = "/queryBalance", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryBalance(HttpSession session, @RequestParam String agentid) throws ControllerException {
        try {
            return insbAccountDetailsService.queryBalance(agentid);
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 探测订单状态
     *
     * @param accountDetailVo
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "detector", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String detector(HttpSession session, @RequestBody AccountDetailVo accountDetailVo) throws ControllerException {
        try {
            return insbAccountDetailsService.detector();
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 根据任务号查询已获酬金和奖券兑现
     *
     * @param taskid
     * @return
     * @throws
     */
    @RequestMapping(value = "/queryCommissionAndPrizeByTask", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryCommissionAndPrizeByTask(@RequestParam String taskid,@RequestParam String agentid) throws ControllerException {
        try {
            List<Map<String,Object>> result = insbAccountDetailsService.queryCommissionAndPrizeByTask(taskid,agentid);
            Map<String,Object> map = new HashMap<String,Object>();
            if(null != result && result.size() > 0){
                map.put("total",result.size());
                map.put("rows",result);
                map.put("success",true);
                map.put("taskid",taskid);
            }else{
                map.put("total",0);
                map.put("rows","");
                map.put("success",true);
                map.put("taskid",taskid);
            }
            return JsonUtils.serialize(map);
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }
}
