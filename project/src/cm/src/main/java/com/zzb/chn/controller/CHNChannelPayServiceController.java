package com.zzb.chn.controller;

import javax.annotation.Resource;

import com.zzb.mobile.util.PayConfigMappingMgr;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.redis.IRedisClient;
import com.zzb.chn.bean.PayInfo;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.chn.service.CHNPaymentService;
import com.zzb.chn.util.JsonUtils;

@Controller
@RequestMapping("/payService/*")
public class CHNChannelPayServiceController {
    @Resource
    private CHNPaymentService chnPaymentService;
    @Resource
    private IRedisClient redisClient;

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public Object view() {
		return PayConfigMappingMgr.getDoc();
	}

	@RequestMapping(value = "/view/{mapType}/{code}", method = RequestMethod.GET)
	@ResponseBody
	public Object viewSingle(@PathVariable("mapType") String mapType, @PathVariable("code") String code) {
		return PayConfigMappingMgr.getPayCodeByCmCode(mapType, code);
	}


	@RequestMapping(value = "/view/loadPath", method = RequestMethod.GET)
	@ResponseBody
	public Object loadPath(@RequestParam("path") String path) {
		try {
			org.springframework.core.io.Resource res = new ClassPathResource(path);
			return res.getURI().toURL().toString();
		}
		catch (Exception ex)
		{
			LogUtil.error(ex);
			return "读取路径有错误！";

		}
	}

	@RequestMapping(value="/view/{mapType}", method = RequestMethod.GET)
	@ResponseBody
	public Object viewNode(@PathVariable("mapType") String mapType) {
		return PayConfigMappingMgr.getElements(mapType);
	}

    //收银台确认支付
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @ResponseBody
    public PayInfo pay(@RequestBody PayInfo payInfo) {
        PayInfo result = new PayInfo();
		try {
			LogUtil.info("channelpay-pay:" + JsonUtils.serialize(payInfo));
			String jsonStr = String.valueOf(redisClient.get(CHNPaymentService.CHANNEL_PAYMENT_MODULE, payInfo.getBizId()));
			if (StringUtil.isEmpty(jsonStr)) {
			    result.setCode("13");
			    result.setMsg("支付请求已失效，请重新请求支付");
			    return result;
			}
			LogUtil.info("支付参数：" + jsonStr);
			QuoteBean quoteBean = JsonUtils.deserialize(jsonStr, QuoteBean.class);

			long invalidTime = Long.parseLong(quoteBean.getCreateTime());
			if ((System.currentTimeMillis() - invalidTime) > CHNPaymentService.PAYURL_VALIDTIME_SECONDS * 1000) {
			    redisClient.del(CHNPaymentService.CHANNEL_PAYMENT_MODULE, payInfo.getBizId());
			    result.setCode("13");
			    result.setMsg("支付请求已失效，请重新请求支付");
			    return result;
			}

			result = chnPaymentService.pay(payInfo, quoteBean);
		} catch (Exception e) {
			LogUtil.error("收银台确认支付pay异常：" + e.getMessage());
			e.printStackTrace();
			result.setCode("13");
		    result.setMsg("支付pay异常：" + e.getMessage());
		}
		LogUtil.info("channelpay-pay-return:" + JsonUtils.serialize(result));
		return result;
	}

	@RequestMapping(value = "/queryResult", method = RequestMethod.GET)
	@ResponseBody
	public PayInfo queryResult(@RequestParam("bizTransId") String bizTransId) {
		LogUtil.info("channelpay-queryResult:" + bizTransId);
		PayInfo result = chnPaymentService.queryResult(bizTransId);
		LogUtil.info("channelpay-queryResult-return:" + JsonUtils.serialize(result));
		return result;
	}

	@RequestMapping(value = "/queryResultP", method = RequestMethod.POST)
	@ResponseBody
	public PayInfo queryResultP(@RequestBody PayInfo payInfo) {
		LogUtil.info("channelpay-queryResultP:" + JsonUtils.serialize(payInfo));
		PayInfo result = chnPaymentService.queryResult(payInfo.getBizTransactionId());
		LogUtil.info("channelpay-queryResultP-return:" + JsonUtils.serialize(result));
		return result;
	}

	/**
	 * 渠道验证请求有效性接口
	 * 返回配置的支付方式列表
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	@ResponseBody
	public PayInfo verify(@RequestBody PayInfo payInfo) throws ControllerException {
		LogUtil.info("channelpay-verify:" + JsonUtils.serialize(payInfo));
		PayInfo result = chnPaymentService.verify(payInfo);
		LogUtil.info("channelpay-verify-return:" + JsonUtils.serialize(result));
		return result;
	}

	//旧流程备用金支付/新流程核保成功推二支
	@RequestMapping(value = "/ok", method = RequestMethod.POST)
	@ResponseBody
	public QuoteBean callOk(@RequestBody QuoteBean quoteBean) throws ControllerException {
		LogUtil.info("channelpay-callOk-in:" + JsonUtils.serialize(quoteBean));
		QuoteBean result = new QuoteBean();
		try {
			result = chnPaymentService.callOk(quoteBean);
		} catch (Exception e) {
			LogUtil.error("channelpay-callOk-异常:" + e.getMessage());
            e.printStackTrace();
            result.setRespCode("01");
            result.setErrorMsg("callOk异常：" + e.getMessage());
		}
		LogUtil.info("channelpay-callOk-return:" + JsonUtils.serialize(result) + "--" + quoteBean.getTaskId());
		return result;
	}

	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	@ResponseBody
	public String callback(@RequestBody PayInfo params) {
		LogUtil.info("channelpay-callBack:" + JsonUtils.serialize(params));
		String result = chnPaymentService.callback(params);
		LogUtil.info("channelpay-callBack-return:" + result);
		return result;
	}
	
	//新流程退款接口
	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	@ResponseBody
	public String refund(@RequestBody QuoteBean quoteBean) {
		LogUtil.info("channelpay-refund:" + JsonUtils.serialize(quoteBean));
		String result = chnPaymentService.refund(quoteBean);
		LogUtil.info("channelpay-refund-return:" + result + "--" + quoteBean.getTaskId());
		return result;
	}

	//新流程渠道垫付推核保接口
	@RequestMapping(value = "/channelPayPolicy", method = RequestMethod.POST)
	@ResponseBody
	public QuoteBean channelPayPolicy(@RequestBody QuoteBean quoteBeanIn) throws ControllerException {
		LogUtil.info("channelpay-channelPayPolicy-in:" + JsonUtils.serialize(quoteBeanIn));
		QuoteBean resultOut = new QuoteBean();
		try {
			resultOut = chnPaymentService.channelPayPolicy(quoteBeanIn);
		} catch (Exception e) {
			LogUtil.error("channelpay-channelPayPolicy-异常:" + e.getMessage());
            e.printStackTrace();
            resultOut.setRespCode("01");
            resultOut.setErrorMsg("异常：" + e.getMessage());
		}
		LogUtil.info("channelpay-channelPayPolicy-out:" + JsonUtils.serialize(resultOut));
		return resultOut;
	}
}
