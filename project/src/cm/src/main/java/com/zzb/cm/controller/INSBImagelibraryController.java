package com.zzb.cm.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.service.INSBFilelibraryService;
import com.zzb.cm.service.INSBImagelibraryService;

@Controller
@RequestMapping("/imagemanagement/*")
public class INSBImagelibraryController extends BaseController {
	@Resource
	private INSBFilelibraryService insbFilelibraryService;
	@Resource
	private INSBImagelibraryService insbImagelibraryService;

	/**
	 * 跳转到影像管理页面
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "imagemanagement", method = RequestMethod.GET)
	public ModelAndView imageManagement(HttpSession session) throws ControllerException{
		ModelAndView mav = new ModelAndView("cm/imagemanagement/imagemanagement");
		return mav;
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = "loaddata", method = RequestMethod.POST)
	@ResponseBody
	public String loadData(HttpSession session,@RequestParam(value="carlicenseno",required=false) String carlicenseno,
			@RequestParam(value="insuredname",required=false) String insuredname,
			@RequestParam(value="offset",required=false) int offset,
			@RequestParam(value="limit",required=false) int limit) throws ControllerException{
		INSCUser loginUser = (INSCUser)session.getAttribute("insc_user");
		Map<String, Object> retunMap = new HashMap<String, Object>();
		retunMap = insbImagelibraryService.queryImageByAgent(loginUser.getUsercode(),carlicenseno,insuredname,offset,limit);
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("returndatas", retunMap);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}
	
	@RequestMapping(value = "loaddetailinfo", method = RequestMethod.POST)
	@ResponseBody
	public String loadDetailInfo(HttpSession session,@RequestParam(value="imagelibraryid") String imagelibraryid,
			@RequestParam(value="imagetypename",required=false) String imagetypename,
			@RequestParam(value="offset",required=false) int offset,
			@RequestParam(value="limit",required=false) int limit) throws ControllerException{
		INSCUser loginUser = (INSCUser)session.getAttribute("insc_user");
		Map<String, Object> retunMap = new HashMap<String, Object>();
		retunMap = insbImagelibraryService.queryDeatilInfo(imagelibraryid, loginUser.getUsercode(),imagetypename,offset,limit);
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("returndatas", retunMap);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}
}
