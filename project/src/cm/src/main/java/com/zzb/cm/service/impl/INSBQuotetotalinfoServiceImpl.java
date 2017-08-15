package com.zzb.cm.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.common.HttpClientUtil;
import com.zzb.cm.controller.vo.OrderListVo;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;

@Service
@Transactional
public class INSBQuotetotalinfoServiceImpl extends BaseServiceImpl<INSBQuotetotalinfo> implements
		INSBQuotetotalinfoService {
	private static String WORKFLOW = "";

	static {
		// 读取相关的配置
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("config/config");
		WORKFLOW = resourceBundle.getString("workflow.url");
	}
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBWorkflowmainDao insbWorkflowmainDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Override
	protected BaseDao<INSBQuotetotalinfo> getBaseDao() {
		return insbQuotetotalinfoDao;
	}

	/**
	 * 根据流程实例id
	 * 查询非报价期间的保险公司列表
	 */
	@Override
	public List<String> getInscomcodeListByInstanceId(String instanceId,String inscomcode) {
		//收集insccomcode的List集合
		List<String> inscomcodeList = new ArrayList<String>();
		//通过是否生成订单判断是否在人工报价节点
		INSBOrder order = new INSBOrder();
		order.setTaskid(instanceId);
		order.setPrvid(inscomcode);
		order = insbOrderDao.selectOne(order);
		if(order!=null){
			//已生成订单说明已经指定保险限公司
			inscomcodeList.add(order.getPrvid());
		}
		return inscomcodeList;
	}

	@Override
	public List<Map<String, Object>> getInscomInfo(String taskid) {
		return insbQuotetotalinfoDao.getInscomInfo(taskid);
	}
	/**
	 * 人工调整页面调整完成页面按钮
	 */
	@Override
	public String adjustcomplete(String subInstanceId,String result){
		try{
			Map<String,Object>params=new HashMap<String, Object>();
			
			INSBWorkflowmain mainModel=insbWorkflowmainDao.selectByInstanceId(subInstanceId);
			params.put("userId", mainModel.getOperator());
			params.put("processinstanceid", Integer.parseInt(mainModel.getInstanceid()));
			
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("result", result);  //报价结果，成功是3
			params.put("data", data);
			JSONObject jsonb = JSONObject.fromObject(params);
			Map<String,String> par = new HashMap<String,String>();
			par.put("datas", jsonb.toString());
			
			String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeTask", par);
			LogUtil.info(subInstanceId+"人工调整页面调整完成工作流接口completeSubTask返回结果："+message);
			return "success";
			//JSONObject jsonresult= JSONObject.fromObject(message);
			/*if(jsonresult.getString("message").equals("success")){
				return "success";
			}
			else{
				return "fail";
			}*/
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
		
	}

	/**
	 * 根据流程实例id、任务节点状态编码、登录用户的code
	 * 查询报价期间的保险公司列表
	 */
	@Override
	public List<String> getInscomcodeListByInstanceId(String instanceId,
			String taskcode, String usercode) {
		//收集insccomcode的List集合
		List<String> inscomcodeList = new ArrayList<String>();
		//查询工作流子表得到子流程id
		INSBWorkflowsub workflowsub = new INSBWorkflowsub();
		workflowsub.setMaininstanceid(instanceId);
		workflowsub.setTaskcode(taskcode);
		workflowsub.setOperator(usercode);
		List<INSBWorkflowsub> workflowsubList = insbWorkflowsubDao.selectList(workflowsub);
		//得到报价信息总表id
		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(instanceId);
		quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
		//循环子流程通过子流程id查询报价表的到保险公司code
		if(workflowsubList!=null && workflowsubList.size()>0){
			for (int i = 0; i < workflowsubList.size(); i++) {
				INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
				quoteinfo.setWorkflowinstanceid(workflowsubList.get(i).getInstanceid());
				quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
				quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
				if(quoteinfo!=null){
					inscomcodeList.add(quoteinfo.getInscomcode());
				}
			}
		}
		return inscomcodeList;
	}

	@Override
	public Map<String, Object> getAgentInfoByTaskId(String taskid) {
		
		return insbQuotetotalinfoDao.getAgentInfoByTaskId(taskid);
	}

	public String getQuotetotalinfoByUserid(Map<String, Object> map){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		if ("1".equals((String)map.get("orderstatus"))) {
			List<String> list = Arrays.asList("1","2","3","4","6","7","8","31","13","14","15");
			map.put("taskcodes", list);
		}else if ("2".equals((String)map.get("orderstatus"))) {
			List<String> list = Arrays.asList("20");
			map.put("taskcodes", list);
		}
		//List<OrderListVo> ll = insbQuotetotalinfoDao.getQuotetotalinfoByUserid(map);
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object,Object>>();
		Map<Object, Object> initMap = new HashMap<>();
		initMap.put("records", "10000");
		initMap.put("page", 1);
//		initMap.put("total", insbQuotetotalinfoDao.getCountQuotetotalinfoByUserid(map));
		initMap.put("total", 0);
//		for (OrderListVo orderListVo : ll) {
//			Map<Object, Object> tempMap = new HashMap<>();
//			tempMap.put("id", orderListVo.getId());
//			tempMap.put("platenumber", orderListVo.getPlatenumber());
//			tempMap.put("name", orderListVo.getName());
//			tempMap.put("prvshotname", orderListVo.getPrvshotname());
//			tempMap.put("createtime", format.format(orderListVo.getCreatetime()));
//			tempMap.put("taskid", orderListVo.getTaskid());
//			resultList.add(tempMap);
//		}
		initMap.put("rows", resultList);
		JSONObject jsonObject = JSONObject.fromObject(initMap);
		return jsonObject.toString();
	}

	@Override
	public String getQuotetotalinfoByParams(Map<String, Object> map) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		if ("1".equals((String)map.get("orderstatus"))) {
			List<String> list = Arrays.asList("1","2","3","4","6","7","8","31","13","14","15");
			map.put("taskcodes", list);
		}else if ("2".equals((String)map.get("orderstatus"))) {
			List<String> list = Arrays.asList("20","19","18","17","16");
			map.put("taskcodes", list);
		}
		List<OrderListVo> ll = insbQuotetotalinfoDao.getQuotetotalinfoByParams(map);
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object,Object>>();
		Map<Object, Object> initMap = new HashMap<>();
		initMap.put("records", "10000");
		initMap.put("page", 1);
		initMap.put("total", insbQuotetotalinfoDao.getCountQuotetotalinfoByParams(map));
		if ( ll != null && ll.size() > 0) {
			for (OrderListVo orderListVo : ll) {
				Map<Object, Object> tempMap = new HashMap<>();
				tempMap.put("id", orderListVo.getId());
				tempMap.put("platenumber", orderListVo.getPlatenumber());
				tempMap.put("name", orderListVo.getName());
				tempMap.put("prvshotname", orderListVo.getPrvshotname());
				tempMap.put("createtime", format.format(orderListVo.getCreatetime()));
				tempMap.put("taskid", orderListVo.getTaskid());
				resultList.add(tempMap);
			}
		}
		initMap.put("rows", resultList);
		JSONObject jsonObject = JSONObject.fromObject(initMap);
		return jsonObject.toString();
	}
	@Override
	public List<String> getInscomcodeListByInstanceId(String taskid) {
		//收集insccomcode的List集合
				List<String> inscomcodeList = new ArrayList<String>();
				//查询工作流子表得到子流程id
				INSBWorkflowsub workflowsub = new INSBWorkflowsub();
				workflowsub.setMaininstanceid(taskid);
				List<INSBWorkflowsub> workflowsubList = insbWorkflowsubDao.selectList(workflowsub);
				//得到报价信息总表id
				INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
				quotetotalinfo.setTaskid(taskid);
				quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
				//循环子流程通过子流程id查询报价表的到保险公司code
				if(workflowsubList!=null && workflowsubList.size()>0){
					for (int i = 0; i < workflowsubList.size(); i++) {
						INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
						quoteinfo.setWorkflowinstanceid(workflowsubList.get(i).getInstanceid());
						quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
						quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
						if(quoteinfo!=null){
							inscomcodeList.add(quoteinfo.getInscomcode());
						}
					}
				}
				return inscomcodeList;
	}
	@Override
	public List<Map<String,String>> getInscomcodeAndNameListByInstanceId(String taskid,String taskCode, String userCode) {
		//收集insccomcode的List集合
		List<Map<String,String>> inscomcodeList = new ArrayList<Map<String,String>>();
		//查询工作流子表得到子流程id
		INSBWorkflowsub workflowsub = new INSBWorkflowsub();
		workflowsub.setMaininstanceid(taskid);
		workflowsub.setTaskcode(taskCode);
		workflowsub.setOperator(userCode);
		List<INSBWorkflowsub> workflowsubList = insbWorkflowsubDao.selectList(workflowsub);
		//得到报价信息总表id
		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(taskid);
		quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
		Map<String,String> insbcom;
		INSBProvider provider;
		//循环子流程通过子流程id查询报价表的到保险公司code
		if(workflowsubList!=null && workflowsubList.size()>0){
			for (int i = 0; i < workflowsubList.size(); i++) {
				INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
				quoteinfo.setWorkflowinstanceid(workflowsubList.get(i).getInstanceid());
				quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
				quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
				if(quoteinfo!=null){
					insbcom = new HashMap<String,String>();
					insbcom.put("inscomcode", quoteinfo.getInscomcode());
					provider = new INSBProvider();
					provider.setPrvcode(quoteinfo.getInscomcode());
					provider = insbProviderDao.selectOne(provider);
					if(provider!=null){
						insbcom.put("inscomname", provider.getPrvshotname());//保险公司名称
					}
					inscomcodeList.add(insbcom);
				}
			}
		}
		return inscomcodeList;
	}
}