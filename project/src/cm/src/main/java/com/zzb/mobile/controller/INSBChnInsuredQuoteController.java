package com.zzb.mobile.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.common.JsonUtil;
import com.zzb.extra.model.SearchProviderModelForMinizzb;
import com.zzb.mobile.model.*;
import com.zzb.mobile.service.AppInsuredQuoteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/mobile/insured/quote/chn/*")
public class INSBChnInsuredQuoteController extends BaseController{

	@Resource
	private AppInsuredQuoteService insuredQuoteService;
	/**
	 * 1.根据区域查询协议应用范围，获取该区域协议
	 * 2.根据协议id获取供应商列表
	 * 3.根据代理人供应商关系表查询供应商列表，和2结果取交集
	 * 4.根据交集查询供应商表，获取供应商列表
	 * 5.根据供应商id查询协议表，获取该供应商对应的协议列表
	 * 6.根据协议id，查询出单网点表，获取分公司和网点id,对应的机构id，以及地址信息
	 * @param model
	 * @return
	 * @throws com.cninsure.core.exception.ControllerException
	 */
	@RequestMapping(value = "/searchProviderForChn", method = RequestMethod.POST)
	@ResponseBody
	public ExtendCommonModel searchProvider(@RequestBody SearchProviderModelForMinizzb model)throws ControllerException{
		LogUtil.info("查询供应商信息：" + JsonUtil.serialize(model));
		return insuredQuoteService.searchProviderForMinizzb(model);
	}
}
