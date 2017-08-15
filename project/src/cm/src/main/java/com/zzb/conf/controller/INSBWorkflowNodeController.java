package com.zzb.conf.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ServiceException;
import com.cninsure.core.utils.JsonUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.conf.service.INSBWorkflowNodeService;

@Controller
@RequestMapping("/flowNodeApi/*")
public class INSBWorkflowNodeController {

	@Resource
	private INSBWorkflowNodeService insbWorkflowNodeService;

	/**
	 * 获得流程人员信息
	 * 
	 * @param taskParams
	 * @return
	 */
	@RequestMapping(value = "getPersonInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getPersonInfo(
			@RequestParam(value = "taskParams", defaultValue = "") String taskParams) {
		String result = null;
		Map<String, Object> errMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(taskParams)) {
			Map<String, Object> map = JsonUtil.parseJSONToMap(taskParams);
			try {
				result = insbWorkflowNodeService.getPersonInfo(map);
			} catch (ServiceException se) {
				LogUtil.error(se);
				errMap.put("errorMsg", se.getMessage());
			}
		} else {
			errMap.put("errorMsg",
					"Parameter [taskParams]'s value is null or empty.");
		}

		if (errMap != null && !errMap.isEmpty()
				&& errMap.containsKey("errorMsg")) {
			result = JsonUtil.getJsonString(errMap);
		}
		return result;
	}
	
	@RequestMapping(value = "getHandler", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getHandler(@RequestParam(value = "param", defaultValue = "") String param) {
		return insbWorkflowNodeService.getPersonInfo(param);
	}
	

}
