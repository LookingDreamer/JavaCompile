package com.zzb.mobile.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.LogUtil;
import com.common.ZxingImgUtil;
import com.google.zxing.NotFoundException;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.IsuredQuotationInfo;
import com.zzb.mobile.model.WorkFlowRuleInfo;
import com.zzb.mobile.service.AppOtherRequestService;

@Controller
@RequestMapping("/mobile/other/*")
public class AppOtherRequestController extends BaseController {

	@Resource
	private AppOtherRequestService appOtherRequestService;

	@RequestMapping(value = "insuredquotationinfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel insuredQuotationInfo(@RequestBody IsuredQuotationInfo model) {
		return appOtherRequestService.queryInsuredQuoteInfo(model);
	}
	/**
	 * 工作流调用规则接口
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveworkflowruleinfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel saveWorkFlowRuleInfo(@RequestBody WorkFlowRuleInfo model) {
		return appOtherRequestService.saveWorkFlowRuleInfo(model);
	}
	
	/**
	 * 解析二维码图片请求
	 * @param file  MultipartFile
	 * @param filepath  String filepath不为空则根据路径解析，否则根据上传file解析
	 * @return
	 */
	@RequestMapping(value = "decodeBinaryImgFile", method = RequestMethod.POST)
	@ResponseBody
	public String decodeBinaryImgFile(HttpServletRequest request, @RequestParam("file") MultipartFile file,@RequestParam("filepath") String filepath) {
		try {
			return ZxingImgUtil.decodeBinaryBitmap(filepath, file, request);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			LogUtil.info(file.getName()+"NotFoundException"+filepath);
			return "NotFoundException";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.info(file.getName()+"IOException"+filepath);
			return "IOException";
		}
	}
	
	/**
	 * 解析二维码图片请求
	 * @param file  MultipartFile
	 * @param filepath  String filepath不为空则根据路径解析，否则根据上传file解析
	 * @return
	 */
	@RequestMapping(value = "decodeBinaryImg", method = RequestMethod.POST)
	@ResponseBody
	public String decodeBinaryImg(HttpServletRequest request,@RequestParam("filepath") String filepath) {
		try {
			return ZxingImgUtil.decodeBinaryBitmap(filepath, null, request);
		} catch (Exception e) {
			LogUtil.info(filepath+"NotFoundException"+filepath);
			return e.getMessage();
		} 
	}
}
