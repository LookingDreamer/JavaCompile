package com.zzb.extra.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.HttpSender;
import com.common.PagingParams;
import com.zzb.chn.util.JsonUtils;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.extra.dao.INSBAgentTaskDao;
import com.zzb.extra.model.MiniOrderListMappingModel;
import com.zzb.extra.model.MiniOrderQueryModel;
import com.zzb.extra.util.ParamUtils;

/**
 * 
 * @author Shigw
 *
 */
@Controller
@RequestMapping("/queryRefund/*")
public class QueryRefundController extends BaseController {

	@Resource
	private INSCCodeService inscCodeService;

	@Resource
	private INSBAgentTaskDao insbAgentTaskDao;

	@Resource
	private INSBAgentService agentService;

	private static Map<String, String> httpHead = new HashMap<>();

	static {
		httpHead.put("innerPipe", "zheshiyigemimi!");
		httpHead.put("Content-Type", "application/json;charset=utf-8");
		httpHead.put("channelId", "nqd_minizzb2016");
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView getList(Map<String, Object> map, HttpSession session) {

		ModelAndView mav = new ModelAndView("extra/queryRefund");

		List<INSCCode> orderStatusList = inscCodeService.queryINSCCodeByCode("orderStatus", "orderStatus");

		mav.addObject("orderStatusList", orderStatusList);

		return mav;
	}

	/**
	 * 退款查询接口
	 * 
	 * @param para
	 * 
	 * @return
	 */
	@RequestMapping(value = "queryRefundList", method = RequestMethod.GET)
	@ResponseBody
	public String queryRefundList(@ModelAttribute PagingParams para, @ModelAttribute MiniOrderQueryModel mom) {

		try {
//			参数设置
			INSCCode inscCode = new INSCCode();
			mom.setPageNum(para.getOffset() / para.getLimit() + 1 + "");
			mom.setPageSize(para.getLimit()+"");
//			定义返回结果
			List<Map<Object, Object>> rowsResult = new ArrayList<>();
			long total = 0;
			String name = mom.getName();
			String phone = mom.getPhoneMini();
			Map<String, Object> tmpMap = new HashMap<>();
			tmpMap.put("name", name);
			tmpMap.put("phone", phone);
//			设想到名字可以重复
			if (name != null && !name.equals("") || phone != null && !phone.equals("")) {

				List<Map<Object, Object>> resultLists = insbAgentTaskDao.selectIdByName(tmpMap);
				for (Map<Object, Object> paraMap : resultLists) {
					String id = (String) paraMap.get("id");
					mom.setChannelUserId(id);
					Map<String, Object> map = BeanUtils.toMap(mom);
//					调用接口，调试数据
					MiniOrderListMappingModel orderListMappingModel = JsonUtils.deserialize(this.getRequestData(map, inscCode),
							MiniOrderListMappingModel.class);
					List<Map<Object, Object>> rows = orderListMappingModel.getBody().getRows();
					long total1 = orderListMappingModel.getBody().getTotal();
					total += total1;
					this.getParam(rows, rowsResult);
				}
			} else {
				Map<String, Object> map = BeanUtils.toMap(mom);
//				调用接口，调试数据
				MiniOrderListMappingModel orderListMappingModel = JsonUtils.deserialize(this.getRequestData(map, inscCode),
						MiniOrderListMappingModel.class);
				List<Map<Object, Object>> rows = orderListMappingModel.getBody().getRows();
				long total1 = orderListMappingModel.getBody().getTotal();
				total += total1;
				this.getParam(rows, rowsResult);
			}
			return ParamUtils.resultMap(total, rowsResult);
		} catch (Exception e) {
			LogUtil.error("错误信息", e);
			return ParamUtils.resultMap(false, "系统错误");
		}
	}
	/**
	 * 查询结果，调用接口
	 * @param param
	 * @param inscCode
	 * @return
	 * @throws Exception
	 */
	private String getRequestData(Map<String, Object> param, INSCCode inscCode) throws Exception {
//		参数设置
		inscCode.setParentcode("ChannelForMini");
		inscCode.setCodetype("channelurl");
		inscCode.setCodevalue("01");
//		获得参数值
		inscCode = inscCodeService.queryOne(inscCode);
		String url = "";
		String signUrl = "";
		if (null != inscCode) {
			url = inscCode.getProp2() + "/channel/miniOrderQuery";
			signUrl = inscCode.getProp2() + "/channel/getSignForInner";
		}
		param.put("refundOrder", "refundOrder");
		String json = JsonUtils.serialize(param);
		String sign = HttpSender.doPost(signUrl, json, httpHead, "UTF-8");
		httpHead.put("sign", sign);
		String res = HttpSender.doPost(url, json, httpHead, "UTF-8");
		return res;
	}
	/**
	 * 格式返回
	 * @param res
	 * @return
	 */
	private void getParam(List<Map<Object, Object>> res, List<Map<Object, Object>> rowsResult) {
		for (Map<Object, Object> map2 : res) {
			String insureMoney = null;
			String firstPayAmount = (String) map2.get("firstPayAmount");
			String quoteAmount = (String) map2.get("quoteAmount");
			
			if (firstPayAmount != null && !firstPayAmount.equals("")) {
				insureMoney = firstPayAmount;
				map2.put("insureMoney", insureMoney);
			} else {
				insureMoney = quoteAmount;
				map2.put("insureMoney", insureMoney);
			}
			String taskid = (String) map2.get("taskId");

			Map<String, Object> map3 = insbAgentTaskDao.selectNameByTaskId(taskid);
			
			if (map3 != null && map3.size() > 0) {
				String resultName = (String) map3.get("name");
				String resultPhone = (String) map3.get("phone");
				map2.put("name", resultName);
				map2.put("phoneMini", resultPhone);
			}
			rowsResult.add(map2);
		}
	}

}
