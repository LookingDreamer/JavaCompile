package com.zzb.cm.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsuranceImageBean;
import com.zzb.mobile.service.AppInsuredQuoteService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.cm.entity.INSBUserremark;
import com.zzb.cm.entity.INSBUserremarkhis;
import com.zzb.cm.service.INSBUserremarkService;
import com.zzb.cm.service.INSBUserremarkhisService;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBUsercomment;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;

/**
 * 备注公共页面
 */
@Controller
@RequestMapping("/common/remarksinfo/*")
public class INSBCommonRemarksInfoController extends BaseController {
	@Resource
	private AppInsuredQuoteDao appInsuredQuoteDao;
	@Resource
	private INSBUserremarkService insbUserremarkService;
	@Resource
	private INSBUserremarkhisService insbUserremarkhisService;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	private INSBWorkflowmaintrackService insbWorkflowmaintrackService;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	private AppInsuredQuoteService insuredQuoteService;
	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;

	// 跳出修改用户备注或添加操作员备注弹出框
	@RequestMapping(value = "showeditremark", method = RequestMethod.GET)
	public ModelAndView showEditRemark(HttpSession session, String mark, String instanceId,String inscomcode,String subInstanceId
			,String num) {
		ModelAndView mav = new ModelAndView();
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		if ("editUserRemark".equals(mark)) {//跳转修改用户备注
			//获取备注类型和备注内容类型
			List<INSCCode> commenttypeList = inscCodeService.queryINSCCodeByCode("commenttype", "commenttype");
			mav.addObject("commenttypeList",commenttypeList);
			INSBUsercomment usercomment = null;
			//获取轨迹信息和用户备注信息
			if(subInstanceId==null || "".equals(subInstanceId)){
				INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintrackService.getMainTrackByInscomcode(instanceId);
				//用户备注信息，第二个参数（1主流程轨迹、 2子流程轨迹）
				usercomment = insbUsercommentService.selectUserCommentByTrackid(workflowmaintrack.getId(), 1);
				mav.addObject("mainOrSub",1);
				mav.addObject("trackid",workflowmaintrack.getId());
			}else{
				INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(instanceId, inscomcode);
				usercomment = insbUsercommentService.selectUserCommentByTrackid(workflowsubtrack.getId(), 2);
				mav.addObject("mainOrSub",2);
				mav.addObject("trackid",workflowsubtrack.getId());
				mav.addObject("taskcode",workflowsubtrack.getTaskcode());
			}
			mav.addObject("usercomment",usercomment);
			if(usercomment!=null)
			mav.addObject("commentcontenttypeList",insbUsercommentService.getCommentcontenttypeListByCommenttype
					(usercomment.getCommenttype()!=null?usercomment.getCommenttype().toString():""));

			CommonModel cm = insuredQuoteService.selectNeedUploadImage_ByParentcode(instanceId);
			// 查询备注要上传的文件codetype
			List<String> listCodeType = appInsuredQuoteService.queryUserCommentUploadFile_codeType(instanceId, inscomcode);
			List<InsuranceImageBean> commentUpFile = appInsuredQuoteDao.selectBackNeedUploadImageByCodeType(listCodeType);
            //初始化
			mav.addObject("allList", cm.getBody());
			mav.addObject("addList", commentUpFile);

			mav.setViewName("cm/common/editUserRemark");
		} else if ("addOperatorRemark".equals(mark)) {//跳转添加操作员备注
			mav.addObject("loginName", loginUser.getName());
			mav.addObject("loginUserName", loginUser.getUsername());
			//获取轨迹信息和用户备注信息
			if(subInstanceId==null || "".equals(subInstanceId)){
				INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintrackService.getMainTrackByInscomcode(instanceId);
				mav.addObject("trackid",workflowmaintrack.getId());
				mav.addObject("mainOrSub",1);
			}else{
				INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackService.getSubTrackByInscomcode(instanceId, inscomcode);
				mav.addObject("trackid",workflowsubtrack.getId());
				mav.addObject("mainOrSub",2);
			}
			mav.setViewName("cm/common/addOperatorRemark");
		}
		mav.addObject("num", num);//选项卡下标
		mav.addObject("instanceId", instanceId);//主流程实例id
		return mav;
	}

	// 查新备注信息弃用
	@RequestMapping(value = "getremarkinfo", method = RequestMethod.GET)
	@ResponseBody
	public String getRemarkinfo(INSBUserremark userremark) {
		INSBUserremark ruserremark = insbUserremarkService
				.getRemarkByTaskId(userremark.getTaskid());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("remarkinfo", ruserremark);
		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject.toString();
	}

	// 添加新的用户备注信息弃用
	@RequestMapping(value = "adduserremark", method = RequestMethod.GET)
	public String addUserRemark(INSBUserremarkhis userremarkhis) {
		userremarkhis.setOperator("king");
		INSBUserremarkhis tmp1 =new INSBUserremarkhis();
		INSBUserremarkhis tmp2 =null;
		tmp1.setTaskid(userremarkhis.getTaskid());
		tmp1.setInscomcode(userremarkhis.getInscomcode());
		tmp2 = insbUserremarkhisService.queryOne(tmp1);
		if (tmp2 == null) {
			userremarkhis.setCreatetime(new Date());
			insbUserremarkhisService.insert(userremarkhis);
			return "success";
		} else {
			userremarkhis.setModifytime(new Date());
			insbUserremarkhisService.updateById(userremarkhis);
			return "success";
		}
	}

	/**
	 * 级联备注内容类型
	 */
	@RequestMapping(value = "changecommentcontenttype", method = RequestMethod.GET)
	@ResponseBody
	public String changeCommentcontenttype(@RequestParam String commenttypeValue) {
		List<INSCCode> commentcontenttypeList = insbUsercommentService.getCommentcontenttypeListByCommenttype(commenttypeValue);
		JSONArray result = JSONArray.fromObject(commentcontenttypeList);
		return result.toString();
	}
	
	/**
	 * 修改用户备注
	 */
	@RequestMapping(value = "editusercomment", method = RequestMethod.POST)
	@ResponseBody
	public String editUserComment(HttpSession session, INSBUsercomment usercomment) {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		usercomment.setOperator(loginUser.getUsercode());
		return insbUsercommentService.editUserComment(usercomment);
	}
	
	/**
	 * 添加操作人备注
	 */
	@RequestMapping(value = "addoperatorcomment", method = RequestMethod.POST)
	@ResponseBody
	public String addOperatorComment(HttpSession session, INSBOperatorcomment operatorcomment) {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		operatorcomment.setOperator(loginUser.getUsercode());
		return insbOperatorcommentService.addOperatorComment(operatorcomment);
	}
}
