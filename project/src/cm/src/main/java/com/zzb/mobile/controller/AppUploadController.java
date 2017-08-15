package com.zzb.mobile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cninsure.core.controller.BaseController;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.service.INSBRegionService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.FileUploadModel;
import com.zzb.mobile.service.AppUploadImageService;

@Controller
@RequestMapping("/mobile/other/*")
public class AppUploadController extends BaseController {

	@Resource
	private INSBFilelibraryService inscFilelibraryService;
	@Resource
	private INSBRegionService regionservice;
	@Resource
	private AppUploadImageService appUploadImageService;

	/**
	 * app通用图片上传接口
	 * 
	 * @param file
	 *            文件
	 * @param filetype
	 *            字典表中的codetype
	 * @param taskid
	 *            流程id
	 * @param filedescribe
	 *            文 件描述（可以不传）
	 * @return 成功/失败
	 */
	@RequestMapping(value = "fileupload", method = RequestMethod.POST)
	@ResponseBody
	public String fileUpload(@RequestBody FileUploadModel model) {
		Map<String, Object> resultMap = inscFilelibraryService.appUpload(model);
		JSONObject jsonObject = JSONObject.fromObject(resultMap);
		return jsonObject.toString();
	}

	/**
	 * 根据 parentid 获取 下级区域 参数为0为查询全部
	 * 
	 * @param session
	 * @param riskimg
	 * @return
	 */
	@RequestMapping(value = "/getregionsbyparentid", method = RequestMethod.GET)
	@ResponseBody
	public String getregionsbyparentid(@RequestParam(value="parentid") String parentid) {
		INSBRegion regionvo = new INSBRegion();
		regionvo.setParentid(parentid);
		List<INSBRegion> region = regionservice.queryList(regionvo);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("region", region);
		JSONObject jsonObject = JSONObject.fromObject(resultMap);
		return jsonObject.toString();
	}

	/**
	 * 
	 * @param file
	 *            影像
	 * @param operator
	 *            当前登陆人名称
	 * @param filecodevalue
	 *            改影像对应code表value
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/imageupload", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel imageUpload(
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "operator", required = false) String operator,
			@RequestParam(value = "filecodevalue") String filecodevalue,
			@RequestParam(value = "processinstanceid") String processinstanceid,
			HttpServletRequest request) {
		String path = request.getSession().getServletContext()
				.getRealPath("/static/upload/appimage");
		return appUploadImageService.imageUpload(file, operator, path,
				filecodevalue,processinstanceid);
	}
}
