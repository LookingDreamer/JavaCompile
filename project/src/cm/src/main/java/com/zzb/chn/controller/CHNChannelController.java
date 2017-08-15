package com.zzb.chn.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.redis.IRedisClient;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.chn.service.CHNChannelQuoteService;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.chn.service.CHNCommissionRateService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.chn.util.RSACoderUtil;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.model.WorkFlow4TaskModel;

@Controller
@RequestMapping("/channelService/*")
public class CHNChannelController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CHNChannelService chnService;
    @Resource
    private INSBQuotetotalinfoService insbQuotetotalinfoService;
    @Resource
    private INSBWorkflowsubDao insbWorkflowsubDao;
    @Resource
    private CHNChannelQuoteService channelchnQuoteService;
    @Resource
    private CHNCommissionRateService chnCommissionRateService;
    @Resource
    private IRedisClient redisClient;

    //获取调用接口的凭证
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean getToken(@RequestBody QuoteBean quoteBean) {
        QuoteBean result = new QuoteBean();
        LogUtil.info("channel-getToken:" + JsonUtils.serialize(quoteBean));
        String id = quoteBean.getChannelId();
        String pass = quoteBean.getChannelSecret();
        if (StringUtil.isEmpty(id) || StringUtil.isEmpty(pass)) {
            result.setRespCode("01");
            result.setErrorMsg("渠道ID和密码不能为空!");
        } else {
            try {
                result = chnService.getToken(id, pass);
            } catch (Exception e) {
                LogUtil.info("channel-getToken-get:" + e.getMessage());
                e.printStackTrace();
                result.setRespCode("01");
                result.setErrorMsg("getToken异常 " + e.getMessage());
            }
        }
        LogUtil.info("channel-getToken-return:" + JsonUtils.serialize(result));
        return result;
    }

    //查询开通的供应商
    @RequestMapping(value = "/getProviders", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean getProviders(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) {
        QuoteBean result = new QuoteBean();
        LogUtil.info("channel-getProviders:" + channelId + " " + JsonUtils.serialize(quoteBean));
        quoteBean.setChannelId(channelId);
        String city = quoteBean.getInsureAreaCode();
        if (StringUtil.isEmpty(channelId) && StringUtil.isEmpty(city)) {
            result.setRespCode("01");
            result.setErrorMsg("渠道ID和地区代码不能为空!");
        } else {
            try {
                result = chnService.getProviders(channelId, city);
            } catch (Exception e) {
                LogUtil.info("getProviders时发生异常" + e.getMessage());
                e.printStackTrace();
                result.setRespCode("01");
                result.setErrorMsg("getProviders异常 " + e.getMessage());
            }
        }
        LogUtil.info("channel-getProviders-return:" + JsonUtils.serialize(result));
        return result;
    }

    //查询车型接口
    @RequestMapping(value = "/queryCarModelInfos", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean queryCarModelInfos(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) {
        LogUtil.info("channel-queryCarModelInfos:" + channelId + " " + JsonUtils.serialize(quoteBean));
        quoteBean.setChannelId(channelId);
        QuoteBean result = new QuoteBean();

        try {
	        String vehicleName = quoteBean.getCarInfo().getVehicleName();
	        if (StringUtil.isEmpty(vehicleName) || (StringUtil.isEmpty(quoteBean.getPageSize()) || StringUtil.isEmpty(quoteBean.getPageNum()))) {
	            result.setRespCode("01");
	            result.setErrorMsg("车型品牌名称和分页参数不能为空");
	        } else if ( !channelchnQuoteService.hasInterfacePower("9", null, channelId) ) { //9-车型数据查询权限
	            result.setRespCode("01");
	            result.setErrorMsg("没有调用该接口的权限");
	        } else {
	            result = chnService.queryCarModelInfos(quoteBean);
	        }
        } catch (Exception e) {
            LogUtil.error("queryCarModelInfos异常：" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("queryCarModelInfos异常：" + e.getMessage());
        }
        LogUtil.info("channel-queryCarModelInfos-return:" + JsonUtils.serialize(result));
        return result;
    }

    //图像识别
    @RequestMapping(value = "/recognizeImage", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean recognizeImage(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) {
        QuoteBean result = new QuoteBean();
        LogUtil.info("channel-recognizeImage:" + channelId + " " + JsonUtils.serialize(quoteBean));
        quoteBean.setChannelId(channelId);
        try {
            result = chnService.recognizeImage(request, quoteBean);
        } catch (Exception e) {
            LogUtil.info("recognizeImage时发生异常" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("recognizeImage异常 " + e.getMessage());
        }
        LogUtil.info("channel-recognizeImage-return:" + JsonUtils.serialize(result));
        return result;
    }

    //上传影像
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean uploadImage(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) {
        QuoteBean result = new QuoteBean();
        LogUtil.info("channel-uploadImage-in:" + channelId + " " + quoteBean.getTaskId());
        LogUtil.debug("channel-uploadImage-in:" + channelId + " " + JsonUtils.serialize(quoteBean));
        quoteBean.setChannelId(channelId);
        try {
            result = chnService.uploadImage(request, quoteBean);
        } catch (Exception e) {
            LogUtil.error("uploadImage时发生异常：" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("uploadImage异常：" + e.getMessage());
        }
        LogUtil.info("channel-uploadImage-return:" + JsonUtils.serialize(result));
        return result;
    }

    /**
     * 9  提交报价任务
     *
     * @param quoteBean 主任务号 供应商
     * @return 提交报价任务
     * @throws ControllerException
     */
    @RequestMapping(value = "/submitQuote", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean submitQuote(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId)
            throws ControllerException {
        QuoteBean result = new QuoteBean();
        quoteBean.setChannelId(channelId);
        LogUtil.info("channel-submitQuote:" + channelId + " " + JsonUtils.serialize(quoteBean));
        try {
            result = channelchnQuoteService.submitQuote(quoteBean);
        } catch (Exception e) {
            LogUtil.info("submitQuote时发生异常" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("submitQuote异常 " + e.getMessage());
        }
        LogUtil.info("channel-submitQuote-return:" + JsonUtils.serialize(result));
        return result;
    }

    /**
     * 10  提交核保任务
     *
     * @param quoteBean 主任务号 供应商
     * @return 提交核保
     * @throws ControllerException
     */
    @RequestMapping(value = "/submitInsure", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean submitInsure(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId)
            throws ControllerException {
        QuoteBean result = new QuoteBean();
        quoteBean.setChannelId(channelId);
        LogUtil.info("channel-submitInsure:" + channelId + " " + JsonUtils.serialize(quoteBean));
        try {
            result = channelchnQuoteService.submitInsure(quoteBean);
        } catch (Exception e) {
            LogUtil.error("提交核保异常" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("submitInsure异常" + e.getMessage());
        }
        LogUtil.info("channel-submitInsure-return:" + JsonUtils.serialize(result) + "-" + quoteBean.getTaskId() + "-" + quoteBean.getPrvId());
        return result;
    }

    /*
     *8 修改报价/核保 任务
     * @param QuoteBean
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/updateTask", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean updateTask(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId)
            throws ControllerException {
        QuoteBean result = new QuoteBean();
        LogUtil.info("channel-updateTask:" + channelId + " " + JsonUtils.serialize(quoteBean));
        quoteBean.setChannelId(channelId);
        try {
            result = channelchnQuoteService.updateTask(quoteBean);
        } catch (Exception e) {
            LogUtil.info("updateTask时发生异常" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("updateTask异常 " + e.getMessage());
        }
        LogUtil.info("channel-updateTask-return:" + JsonUtils.serialize(result));
        return result;
    }

    /**
     * 12 任务查询
     *
     * @param quoteBean 主任务号 供应商
     * @return 任务的查询结果
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryTask", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean queryTask(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) throws ControllerException {
        QuoteBean result = new QuoteBean();
        LogUtil.info("channel-queryTask:" + channelId + " " + JsonUtils.serialize(quoteBean));
        quoteBean.setChannelId(channelId);
        try {
            result = chnService.getTaskQueryByTaskIdAndDeptId(quoteBean.getTaskId(), quoteBean.getPrvId(), false);
        } catch (Exception e) {
            LogUtil.error("queryTask时发生异常" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("queryTask异常：" + e.getMessage());
        }
        return result;
    }

    /**
     * 5 创建报价
     *
     * @param quoteBean 需提供车信息，车主信息
     * @return taskid 主任务号 ..凭主任务号调用修改接口 提供完整数据
     * @throws ControllerException
     */
    @RequestMapping(value = "/createTaskA", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean chnCreateTaskA(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) throws ControllerException {
        QuoteBean result = new QuoteBean();
        LogUtil.info("channel-createTaskA:" + channelId + " " + JsonUtils.serialize(quoteBean));
        quoteBean.setChannelId(channelId);
        try {
            result = channelchnQuoteService.chnCreateTaskA(quoteBean);
        } catch (Exception e) {
            LogUtil.error("createTaskA异常：" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("createTaskA异常：" + e.getMessage());
            result.setTaskId(null);
        }
        LogUtil.info("channel-createTaskA-return:" + JsonUtils.serialize(result));
        return result;
    }

    /**
     * 6  创建报价(标准接口)
     *
     * @param quoteBean 需提供车信息，车主信息，投保地区，供应商信息，险种信息
     * @return taskid 主任务号
     * @throws ControllerException
     */
    @RequestMapping(value = "/createTaskB", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean chnCreateTaskB(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) throws ControllerException {
        QuoteBean result = new QuoteBean();
        LogUtil.info("channel-createTaskB:" + channelId + " " + JsonUtils.serialize(quoteBean));
        quoteBean.setChannelId(channelId);
        if ( StringUtil.isEmpty(quoteBean.getChannelUserId()) ) {
        	quoteBean.setChannelUserId("渠道接口");
        }
        try {
            result = channelchnQuoteService.createTaskB(quoteBean);
        } catch (Exception e) {
        	LogUtil.error("createTaskB异常：" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("createTaskB异常：" + e.getMessage());
            result.setTaskId(null);
        }
        LogUtil.info("channel-createTaskB-return:" + JsonUtils.serialize(result));
        return result;
    }

    //过滤器错误时跳转接口
    @RequestMapping(value = "/error", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean error(@RequestParam("status") String errorCode, @RequestParam("msg") String msg) {
        QuoteBean bean = new QuoteBean();
        if (errorCode.equals("404")) {
            bean.setRespCode("01");
            bean.setErrorMsg("token 超时");
        } else if (errorCode.equals("403")) {
            bean.setRespCode("01");
            bean.setErrorMsg("非法请求！");
        }
        if (StringUtils.isNoneEmpty(msg)) {
            bean.setErrorMsg(msg);
        }

        return bean;
    }

    //取得支付请求url
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean pay(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) {
        LogUtil.info("channel-pay-in:" + channelId + " " + JsonUtils.serialize(quoteBean));
        quoteBean.setChannelId(channelId);
        QuoteBean result = new QuoteBean();
        
        try {
			result = channelchnQuoteService.pay(quoteBean);
		} catch (Exception e) {
			LogUtil.error("pay异常：" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("pay异常：" + e.getMessage());
            result.setPayUrl(null);
		}
        
        LogUtil.info("channel-pay-return:" + JsonUtils.serialize(result) + "-" + quoteBean.getTaskId() + "-" + quoteBean.getPrvId());
        return result;
    }

    //获取签名，测试用
    @RequestMapping(value = "/getSign", method = RequestMethod.GET)
    @ResponseBody
    public String getSign(@RequestParam("nonce") String nonce) {
        try {
            return RSACoderUtil.sign(nonce);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //查询渠道调用接口的凭证，测试用
    @RequestMapping(value = "/queryToken", method = RequestMethod.GET)
    @ResponseBody
    public String queryToken(@RequestParam("channelId") String channelId) {
        try {
            return (String) redisClient.get(CHNChannelService.CHANNEL_MODULE, channelId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //回调接口，测试用
    @RequestMapping(value = "/cb", method = RequestMethod.POST)
    @ResponseBody
    public String callback(@RequestBody WorkFlow4TaskModel model) {
        try {
            chnService.callback(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    //查询任务列表
    @RequestMapping(value = "/queryList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryList(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) {
    	LogUtil.info("channel-queryList-in:" + channelId + " " + JsonUtils.serialize(quoteBean));
    	quoteBean.setChannelId(channelId);
        String result = chnService.queryList(quoteBean, channelId);
        LogUtil.info("channel-queryList-out:" + channelId + " " + result);        
        return result;
    }

    //比queryTask更详细的出参信息
    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public QuoteBean queryOrderDetail(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) {
        return chnService.query(quoteBean, channelId);
    }

    /**
     * 获取投保地区接口
     */
    @RequestMapping(value = "/getAgreementAreas", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean getAgreementAreas(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) {
        LogUtil.info("channel-getAgreementAreas-in:" + channelId + " " + JsonUtils.serialize(quoteBean));
        QuoteBean resultOut = new QuoteBean();
        try {
            quoteBean.setChannelId(channelId);
            if (StringUtil.isEmpty(channelId)) {
            	resultOut.setRespCode("01");
            	resultOut.setErrorMsg("渠道ID不能为空!");
                return resultOut;
            }
            resultOut = chnService.getAgreementAreas(quoteBean);
        } catch (Exception e) {
            LogUtil.error("channel-getAgreementAreas-异常:" + e.getMessage());
            e.printStackTrace();
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("异常：" + e.getMessage());
        }

        LogUtil.info("channel-getAgreementAreas-return:" + channelId + " " + JsonUtils.serialize(resultOut));
        return resultOut;
    }

    /**
     * 渠道获取访问内嵌首页的授权码
     * @param quoteBeanIn
     * @return
     */
    @RequestMapping(value = "/getInnerAuthCode", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean getInnerToken(@RequestBody QuoteBean quoteBeanIn, @RequestHeader(value = "channelId") String channelId) {
        QuoteBean resultOut = new QuoteBean();
        quoteBeanIn.setChannelId(channelId);
        LogUtil.info("channel-getInnerToken-in:" + JsonUtils.serialize(quoteBeanIn));
        
        try {
        	resultOut = chnService.getInnerToken(quoteBeanIn);
        } catch (Exception e) {
            LogUtil.error("channel-getInnerToken:" + e.getMessage());
            e.printStackTrace();
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("异常：" + e.getMessage());
        }
        
        LogUtil.info("channel-getInnerToken-return:" + JsonUtils.serialize(resultOut));
        return resultOut;
    }
    
    /**
     * 渠道内嵌首页验证授权码
     * @param quoteBeanIn
     * @return
     */
    @RequestMapping(value = "/verifyInnerAuthCode", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean verifyInnerToken(@RequestBody QuoteBean quoteBeanIn, HttpServletRequest request) {
        QuoteBean resultOut = new QuoteBean();
        LogUtil.info("channel-verifyInnerToken-in:" + JsonUtils.serialize(quoteBeanIn));
        
        try {
        	resultOut = chnService.verifyInnerToken(quoteBeanIn, request);
        } catch (Exception e) {
            LogUtil.error("channel-verifyInnerToken:" + e.getMessage());
            e.printStackTrace();
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("异常：" + e.getMessage());
        }
        
        LogUtil.info("channel-verifyInnerToken-return:" + JsonUtils.serialize(resultOut));
        return resultOut;
    }

    /**
     * 验证单个报价公司修改保险，险配置别信息验证，调用规则验证
     * 修改保险配置，调用工作流
     * @param quoteBeanIn
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/checkRestartConf", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean checkRestartConf(@RequestBody QuoteBean quoteBeanIn) {
    	QuoteBean resultOut = new QuoteBean();
        LogUtil.info("channel-checkRestartConf-in:" + JsonUtils.serialize(quoteBeanIn));
        
        try {
            /* 记录chn推cm的时间点开始*/
            try {
                Map<String, String> phaseTime = new HashMap<>();
                phaseTime.put("channelId", quoteBeanIn.getChannelId());
                phaseTime.put("taskId", quoteBeanIn.getTaskId());
                phaseTime.put("prvId", quoteBeanIn.getPrvId()==null?"":quoteBeanIn.getPrvId());
                phaseTime.put("taskState", quoteBeanIn.getTaskState()==null?"":quoteBeanIn.getTaskState());
                phaseTime.put("phaseType", "submitHumanQuoteChnCallCM");
                phaseTime.put("phaseSeq", "4");
                chnService.recordPhaseTime(phaseTime);
            }catch (Exception e){
                LogUtil.info("记录chn推cm的时间点异常:"+quoteBeanIn.getTaskId()+e.getMessage());
            }
             /* 记录chn推cm的时间点结束*/
        	resultOut = channelchnQuoteService.submitSingleQuote(quoteBeanIn);
        } catch (Exception e) {
            LogUtil.error("channel-checkRestartConf-error:" + e.getMessage());
            e.printStackTrace();
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("异常：" + e.getMessage());
        }
        
        LogUtil.info("channel-checkRestartConf-return:" + JsonUtils.serialize(resultOut));
        return resultOut;
    }

    /**
     * 查询可以获得的佣金 传参taskid/providercode httpheader channelId
     * @param agentTaskModel
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryTaskCommission", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryTaskCommission(@RequestBody AgentTaskModel agentTaskModel, @RequestHeader(value = "channelId") String channelId)throws ControllerException{
        agentTaskModel.setChannelid(channelId);
        return chnCommissionRateService.queryTaskCommission(agentTaskModel);
    }

    /**
     * 查询任务佣金率 传参taskid/providercode httpheader channelId
     * @param agentTaskModel
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryTaskCommissionRate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryTaskCommissionRate(@RequestBody AgentTaskModel agentTaskModel, @RequestHeader(value = "channelId") String channelId)throws ControllerException{
        agentTaskModel.setChannelid(channelId);
        return chnCommissionRateService.queryTaskCommissionRate(agentTaskModel);
    }

    /**
     * 查询任务佣金率 传参taskid httpheader channelId
     * @param agentTaskModel
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/completeTaskCommission", method = RequestMethod.POST)
    @ResponseBody
    public String completeTaskCommission(@RequestBody AgentTaskModel agentTaskModel, @RequestHeader(value = "channelId") String channelId)throws ControllerException{
        agentTaskModel.setChannelid(channelId);
        return chnCommissionRateService.completeTaskCommission(agentTaskModel.getTaskid());
    }

    /**
     * 查询佣金信息 传参taskid/providerid
     * @param params
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryCommissionInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryCommissionInfo(@RequestBody Map<String,Object> params, @RequestHeader(value = "channelId") String channelId)throws ControllerException{
        if("N".equals(params.getOrDefault("queryAllChannelData", "N").toString())){
            params.put("channelid",channelId);
        }
        return chnCommissionRateService.queryCommissionInfo(params);
    }

    /**
     * 查询佣金信息 传参taskid/providerid
     * @param params
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryCommissionRateRule", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryCommissionRateRule(@RequestBody Map<String,Object> params, @RequestHeader(value = "channelId") String channelId)throws ControllerException{
        params.put("channelid",channelId);
        return chnCommissionRateService.queryCommissionRateRule(params);
    }

    /**
     * 查询接口权限信息 interfaceId  httpheader channelId & innerPipe=zheshiyigemimi!
     * @param params
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryInterfacePower", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryInterfacePower(@RequestBody Map<String,Object> params, @RequestHeader(value = "channelId") String channelId) throws ControllerException{
        String interfaceId = params.getOrDefault("interfaceId", "00").toString();
        Map<String,Object> result = new HashMap<>();
        if (!channelchnQuoteService.hasInterfacePower(interfaceId, null, channelId)) {
            result.put("respCode", "01");
            result.put("errMsg", "没有调用该接口的权限!");
        }else{
            result.put("respCode", "00");
            result.put("errMsg", "有调用该接口的权限!");
        }
        return JsonUtils.serialize(result);
    }

    /**
     * 平台查询接口信息 interfaceId  httpheader channelId & innerPipe=zheshiyigemimi!
     * @param params taskId / prvId
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryPlatInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryPlatInfo(@RequestBody Map<String,Object> params, @RequestHeader(value = "channelId") String channelId) throws ControllerException{
        String taskId = params.getOrDefault("taskId", "").toString();
        String prvId = params.getOrDefault("prvId", "").toString();
        return chnService.queryPlatInfo(taskId,prvId,channelId,"oldflow");
    }

    /**
     * 平台查询接口信息 interfaceId  httpheader channelId & innerPipe=zheshiyigemimi!
     * @param params taskId / prvId
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "/queryPlatInfoNewFlow", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryPlatInfoNewFlow(@RequestBody Map<String,Object> params, @RequestHeader(value = "channelId") String channelId) throws ControllerException{
        String taskId = params.getOrDefault("taskId", "").toString();
        String prvId = params.getOrDefault("prvId", "").toString();
        return chnService.queryPlatInfo(taskId,prvId,channelId,"newflow");
    }
    
    /**
	 * 北京平台，核保完成支付前，验证被保人身份证信息以及获取验证码
	 */
	@RequestMapping(value="checkIDCardAndGetPin", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public QuoteBean checkIDCardAndGetPin(@RequestBody QuoteBean quoteBeanIn, @RequestHeader(value="channelId") String channelId) {
		QuoteBean resultOut = new QuoteBean();
        quoteBeanIn.setChannelId(channelId);
        LogUtil.info("channel-checkIDCardAndGetPin-in:" + JsonUtils.serialize(quoteBeanIn));
        
        try {
        	resultOut = chnService.checkIDCardAndGetPin(quoteBeanIn);
        } catch (Exception e) {
            LogUtil.error("channel-checkIDCardAndGetPin-exception:" + e.getMessage());
            e.printStackTrace();
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("异常：" + e.getMessage());
        }
        
        LogUtil.info("channel-checkIDCardAndGetPin-return:" + JsonUtils.serialize(resultOut));
        return resultOut;
	}
    
    /**
	 * 北京平台，录入验证码提交
	 * @return 失败，可重新输入验证码再次提交；若成功，就可选择支付方式，进行支付
	 */
	@RequestMapping(value="commitPinCode", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public QuoteBean commitPinCode(@RequestBody QuoteBean quoteBeanIn, @RequestHeader(value="channelId") String channelId) {
		QuoteBean resultOut = new QuoteBean();
        quoteBeanIn.setChannelId(channelId);
        LogUtil.info("channel-commitPinCode-in:" + JsonUtils.serialize(quoteBeanIn));
        
        try {
        	resultOut = chnService.commitPinCode(quoteBeanIn);
        } catch (Exception e) {
            LogUtil.error("channel-commitPinCode-exception:" + e.getMessage());
            e.printStackTrace();
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("异常：" + e.getMessage());
        }
        
        LogUtil.info("channel-commitPinCode-return:" + JsonUtils.serialize(resultOut));
        return resultOut;
	}
    
    /**
	 * 北京平台，获取身份证信息验证的结果，验证码结果
	 */
	@RequestMapping(value="getPinCodeBJ", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public QuoteBean getPinCodeBJ(@RequestBody QuoteBean quoteBeanIn, @RequestHeader(value="channelId") String channelId) {
		QuoteBean resultOut = new QuoteBean();
        quoteBeanIn.setChannelId(channelId);
        LogUtil.info("channel-getPinCodeBJ-in:" + JsonUtils.serialize(quoteBeanIn));
        
        try {
        	resultOut = chnService.getPinCodeBJ(quoteBeanIn);
        } catch (Exception e) {
            LogUtil.error("channel-getPinCodeBJ-exception:" + e.getMessage());
            e.printStackTrace();
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("异常：" + e.getMessage());
        }
        
        LogUtil.info("channel-getPinCodeBJ-return:" + JsonUtils.serialize(resultOut));
        return resultOut;
	}
    
    /**
	 * 北京平台，重新发起申请验证码
	 */
	@RequestMapping(value="reapplyPin", produces="application/json;charset=UTF-8", method=RequestMethod.POST)
	@ResponseBody
	public QuoteBean reapplyPin(@RequestBody QuoteBean quoteBeanIn, @RequestHeader(value="channelId") String channelId) {
		QuoteBean resultOut = new QuoteBean();
        quoteBeanIn.setChannelId(channelId);
        LogUtil.info("channel-reapplyPin-in:" + JsonUtils.serialize(quoteBeanIn));
        
        try {
        	resultOut = chnService.reapplyPin(quoteBeanIn);
        } catch (Exception e) {
            LogUtil.error("channel-reapplyPin-exception:" + e.getMessage());
            e.printStackTrace();
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("异常：" + e.getMessage());
        }
        
        LogUtil.info("channel-reapplyPin-return:" + JsonUtils.serialize(resultOut));
        return resultOut;
	}

    /**
     * 掌中保内嵌H5页面获取投保地区接口
     */
    @RequestMapping(value = "/getAgreementAreasForChn", produces="application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public QuoteBean getAgreementAreasForChn(@RequestBody QuoteBean quoteBean, @RequestHeader(value = "channelId") String channelId) {
        LogUtil.info("channel-getAgreementAreasForChn-in:" + channelId + " " + JsonUtils.serialize(quoteBean));
        QuoteBean resultOut = new QuoteBean();
        try {
            quoteBean.setChannelId(channelId);
            if (StringUtil.isEmpty(channelId)) {
                resultOut.setRespCode("01");
                resultOut.setErrorMsg("渠道ID不能为空!");
                return resultOut;
            }
            resultOut = chnService.getAgreementAreasForChn(quoteBean);
        } catch (Exception e) {
            LogUtil.error("channel-getAgreementAreasForChn-异常:" + e.getMessage());
            e.printStackTrace();
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("异常：" + e.getMessage());
        }

        LogUtil.info("channel-getAgreementAreasForChn-return:" + channelId + " " + JsonUtils.serialize(resultOut));
        return resultOut;
    }
	
}
