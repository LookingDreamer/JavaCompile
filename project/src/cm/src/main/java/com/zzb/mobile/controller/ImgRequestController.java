package com.zzb.mobile.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.LogUtil;
import com.common.ZxingImgUtil;
import com.google.zxing.NotFoundException;
import com.zzb.mobile.model.FileUploadBase64Param;

@Controller
@RequestMapping("/other/*")
public class ImgRequestController extends BaseController {
	
	/**
	 * 解析二维码图片请求
	 * @param file  MultipartFile
	 * @param filepath  String filepath不为空则根据路径解析，否则根据上传file解析
	 * @return
	 */
	@RequestMapping(value = "decodeBinaryImgFile1", method = RequestMethod.POST)
	@ResponseBody
	public String decodeBinaryImgFile1(HttpServletRequest request, @RequestParam("file") MultipartFile file,@RequestParam("filepath") String filepath) {
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
	 * @param filepath  String filepath不为空则根据路径解析，否则根据上传file解析@RequestParam
	 * @return
	 */
	@RequestMapping(value = "decodeBinaryImg1", method = RequestMethod.POST)
	@ResponseBody
	public String decodeBinaryImg1(HttpServletRequest request,@RequestParam("filepath") String filepath) {
		try {
			return ZxingImgUtil.decodeBinaryBitmap(filepath, null, request);
		} catch (Exception e) {
			LogUtil.info(filepath+"NotFoundException"+filepath);
			return e.getMessage();
		} 
	}
	
	
	/**
	 * 解析二维码图片请求
	 * @param param  FileUploadBase64Param
	 * @return
	 */
	@RequestMapping(value = "decodeBinaryImgFile", method = RequestMethod.POST)
	@ResponseBody
	public String decodeBinaryImgFile(HttpServletRequest request, @RequestBody FileUploadBase64Param param) {
		try {
			return ZxingImgUtil.decodeBinaryBitmapBase64(null, param.getFile(), request); 
		} catch (Exception e) {
			LogUtil.info("NotFoundException"+e.getCause());
			return e.getMessage();
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
	public String decodeBinaryImg(HttpServletRequest request,@RequestBody String filepath) {
		try {
			JSONObject json = JSON.parseObject(filepath);
			return ZxingImgUtil.decodeBinaryBitmap(json.getString("filepath"), null, request);
		} catch (Exception e) {
			LogUtil.info("NotFoundException"+filepath);
			return e.getMessage();
		} 
	}
}
