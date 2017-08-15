package com.zzb.extra.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.common.PagingParams;
import com.zzb.extra.dao.INSBMiniPermissionDao;
/**
 * 
 * @author shiguiwu
 * @date 2017年3月2日
 */
@Controller
@RequestMapping("/miniOrderQuery/*")
public class MiniOrderQueryController extends BaseController {
	
	@Resource
	private INSBMiniPermissionDao insbMiniPermission;
	
	/**
	 * 
	 * @param agentid
	 * @param para
	 * @return
	 */
	@RequestMapping(value = "/getMiniOrderQueryList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getMiniOrderQueryList(String agentid, @ModelAttribute PagingParams para) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			Map<String, Object> param = BeanUtils.toMap(para);
			param.put("agentid", agentid);
			List<Map<Object, Object>> rows = insbMiniPermission.queryMiniOrderList(param);
			result.put("rows",rows);
			result.put("total", insbMiniPermission.queryAgentOrderByUseridCount(param));
		} catch (Exception e) {
			// TODO: handle exception
			result.put("status", "失败");
			result.put("rows", "");
			LogUtil.info("错误信息==>"+e.getMessage());
		}
		return result;
	}
}
