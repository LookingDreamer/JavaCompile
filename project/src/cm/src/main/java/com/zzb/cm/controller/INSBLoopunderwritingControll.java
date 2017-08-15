package com.zzb.cm.controller;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.cm.entity.INSBLoopunderwriting;
import com.zzb.cm.entity.INSBLoopunderwritingdetail;
import com.zzb.cm.service.INSBLoopunderwritingService;
import com.zzb.cm.service.INSBLoopunderwritingdetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/business/loop/*")
public class INSBLoopunderwritingControll {
	@Resource
	private INSCDeptService inscDeptService;
	@Resource
	private INSBLoopunderwritingService loopunderwritingService;
	@Resource
	private INSBLoopunderwritingdetailService loopunderwritingdetailService;

	/**
	 * 跳转到列表页面
	 *
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "loopunderwritinglist", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/loopunderwriting/loopunderwriting");
		return mav;
	}

	@RequestMapping(value = "showloopunderwritinglist", method = RequestMethod.GET)
	@ResponseBody
	public String showloopunderwritinglist(HttpSession session, @ModelAttribute PagingParams pagingParams,
										   String insuredname, String carlicenseno, String mainInstanceId, String taskcreatetimeup, String taskcreatetimedown) throws ControllerException{
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		INSCDept d = inscDeptService.getOrgDeptByDeptCode(loginUser.getUserorganization());

		//组织查询参数
		Map<String, Object> paramMap = BeanUtils.toMap(pagingParams);
		paramMap.put("insuredname", insuredname);//被保人姓名
		paramMap.put("platenumber", carlicenseno);//车牌号
		paramMap.put("taskid", mainInstanceId);//流程id
		paramMap.put("taskcreatetimeup", taskcreatetimeup);//任务创建时间上限
		paramMap.put("taskcreatetimedown", taskcreatetimedown);//任务创建时间下限
		paramMap.put("userorganization", d.getDeptinnercode());//树形结构code

		return loopunderwritingService.searchList(paramMap);
	}

	@RequestMapping(value = "showdetail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView showdetail(String loopid, String platenumber, String insuredname){
		ModelAndView mav = new ModelAndView("cm/loopunderwriting/showDetail");

		Map<String, Object> data = new HashMap<>(4);

		if (StringUtil.isNotEmpty(loopid)) {
			INSBLoopunderwriting loopunderwriting = loopunderwritingService.queryById(loopid);
			data.put("loopObj", loopunderwriting);

			INSBLoopunderwritingdetail loopunderwritingdetail = new INSBLoopunderwritingdetail();
			loopunderwritingdetail.setLoopid(loopid);
			List<INSBLoopunderwritingdetail> loopunderwritingdetailList = loopunderwritingdetailService.queryList(loopunderwritingdetail);

			if (loopunderwritingdetailList != null && !loopunderwritingdetailList.isEmpty()) {
				Collections.reverse(loopunderwritingdetailList);
			}

			data.put("detailList", loopunderwritingdetailList);
		}

		data.put("platenumber", platenumber);
		data.put("insuredname", insuredname);
		mav.addObject("data", data);
		return mav;
	}
}
