package com.zzb.mobile.service.impl;

import java.util.*;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppInsuredHomePageService;
import com.zzb.mobile.service.INSBMyOrderService;

@Service
@Transactional
public class AppInsuredHomePageServiceImpl implements AppInsuredHomePageService{
    @Resource
	private INSBPolicyitemDao iNSBPolicyitemDao;
    @Resource
    private INSBAgentService insbAgentService;
    @Resource
    private INSBMyOrderService insbMyOrderService;
    @Resource
    private INSBAgentDao insbAgentDao;
    @Resource
    private INSBWorkflowmainDao insbWorkflowmainDao;
    @Resource
    private INSBWorkflowsubDao insbWorkflowsubDao;
    @Resource
    private AppInsuredQuoteDao appInsuredQuoteDao;
    
	@Override
	public CommonModel getTobePolicyNum(String jobNum) {
		CommonModel model=new CommonModel();
		try {
			long num = getgetTobeinsuredNum(jobNum);//appInsuredQuoteDao.getTobeinsuredNum(queryParams);
			model.setBody("count:"+num);//待投保总共条数
			model.setMessage("查询成功");
			model.setStatus("success");  
		} catch (Exception e) {
			e.printStackTrace();
			model.setMessage("查询失败");
			model.setStatus("fail");
		}
	    return model;
	}

	private long getgetTobeinsuredNum(String jobNum) {
		Map<String,Object> queryParams = new HashMap<String,Object>();
		List<Map<String,Object>> resultlist = null;
		long totalnum = 0;

		queryParams.put("orderstatus",null);
		queryParams.replace("limit",null);
		queryParams.replace("offset",null);
		queryParams.put("agentnum", jobNum);          //代理人工号
		INSBAgent agent = new INSBAgent();
		agent.setJobnum(jobNum);
		agent = insbAgentDao.selectOne(agent);
		String shareid = null;
		if(agent!=null){
			shareid = agent.getId();
			queryParams.put("shareagentnum", shareid);
		}

		resultlist =  insbWorkflowmainDao.getMyOrderListNew(queryParams);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> result : resultlist){
			if("closed".equalsIgnoreCase(result.get("taskstate").toString()))continue;
			String processInstanceId = (String) result.get("processInstanceId");
			List<Map<String,Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
			if(subQuoteInfoList==null||(subQuoteInfoList.size()==1&&StringUtil.isEmpty(subQuoteInfoList.get(0)))){
				subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
			}
			for (int i=subQuoteInfoList.size()-1;i>=0;i--) {
				Map<String, Object> map =subQuoteInfoList.get(i);
                boolean needtoremove = false;

				if(StringUtil.isEmpty(map.get("inscomcode")) || StringUtil.isEmpty(map.get("prvshotname"))
						||StringUtil.isEmpty(map.get("taskcode"))) {
                    LogUtil.info("报价"+processInstanceId+","+map.get("inscomcode")+","+map.get("prvshotname")+","+map.get("taskcode")+"存在空数据，从待投保列表移除");
                    needtoremove = true;
                }

				//去掉过期的单子 2016-03-30
				if(insbMyOrderService.removeOverTimeData2(result.get("processInstanceId"),map.get("inscomcode"))){
                    needtoremove = true;
				}

                if (needtoremove) {
                    subQuoteInfoList.remove(i);
                }

			}
			result.put("quoteInfoList", subQuoteInfoList);
			int flag20;
			boolean isAllClosed = true;//子流程全部关闭
			if(subQuoteInfoList!=null && subQuoteInfoList.size()>0){
				List<String> lastNode = Arrays.asList(new String[]{"25","26","27","28","23","24","21","20"});
				flag20=0;
				for (Map<String, Object> map : subQuoteInfoList) {
					if(map!=null){
						if(lastNode.contains((String) map.get("taskcode"))){
							flag20=1;
						}
						if (!"37".equalsIgnoreCase((String) map.get("taskcode")) && !"30".equalsIgnoreCase((String) map.get("taskcode"))) {
							isAllClosed = false;
						}
					}
				}
				if(flag20!=1 && !isAllClosed){
					list.add(result);
				}
			}
		}

		return list.size();//待投保总共条数
	}

	public CommonModel getTobePaymentOrderNum(String jobNum) {
		CommonModel model = new CommonModel();
		try {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			// 组织查询条件
			queryParams.put("agentnum", jobNum); // 代理人工号
			INSBAgent agent = new INSBAgent();
			agent.setJobnum(jobNum);
			agent = insbAgentDao.selectOne(agent);
			if (agent != null) {
				String shareid = agent.getId();
				queryParams.put("shareagentnum", shareid);
			}
			List<Map<String, Object>> resultlist = appInsuredQuoteDao.getTobePaymentOrderNum(queryParams);
			for (int j = resultlist.size() - 1; j >= 0; j--) {
				Map<String, Object> result = resultlist.get(j);
				// 去掉过期的单子 2016-03-30
				if (insbMyOrderService.removeOverTimeData(result.get("taskid"),result.get("inscomcode"))) {
					resultlist.remove(result);
				}
			}
			model.setBody("count:" + resultlist.size());
			model.setMessage("查询成功");
			model.setStatus("success");
		} catch (Exception e) {
			e.printStackTrace();
			model.setMessage("查询失败");
			model.setStatus("fail");
		}
		return model;
	}

	@Override
	public CommonModel renewalRemindNum(String jobNum) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		CommonModel model=new CommonModel();
		INSBAgent insbAgent = new INSBAgent();
		insbAgent.setJobnum(jobNum);
		insbAgent = insbAgentService.queryOne(insbAgent);
		if(insbAgent==null){
			model.setStatus("fail");
			model.setMessage("代理人不存在");
			return model;
		}
		if(StringUtil.isEmpty(insbAgent.getRenewaltime())){
			model.setStatus("fail");
			model.setMessage("未设置续保提醒时间");
			return model;
		}
		Date renewalDate=new Date();//该续保时间
		Date currDate=new Date();//当前时间
		renewalDate.setMonth(currDate.getMonth()+insbAgent.getRenewaltime()); 
		long count=0;
		Map<String, Object> pramMap = new HashMap<String,Object>();
		pramMap.put("nowdate", currDate);
		pramMap.put("agentnum", jobNum);
		List<INSBPolicyitem> list = iNSBPolicyitemDao.selectByEndDate(pramMap);
		HashMap<String,INSBPolicyitem> insbPolicyitemMap = new HashMap<String, INSBPolicyitem>();
		INSBPolicyitem insbPolicyitem2=null;
		for (INSBPolicyitem insbPolicyitem : list) {
			insbPolicyitem2 = insbPolicyitemMap.get(insbPolicyitem.getTaskid());
			if(insbPolicyitem2==null||(insbPolicyitem2!=null&&insbPolicyitem2.getEnddate().getTime()<insbPolicyitem.getEnddate().getTime())){
				insbPolicyitemMap.put(insbPolicyitem.getTaskid(), insbPolicyitem);
			}
		}
		for (Entry<String, INSBPolicyitem> entry : insbPolicyitemMap.entrySet()) {
			INSBPolicyitem value = entry.getValue();
			if(value.getEnddate().getTime()<renewalDate.getTime()){
				count++;
			}
		}
		map.put("count", count);
		model.setBody(JSONObject.fromObject(map).toString());
		model.setStatus("success");
		model.setMessage("您总共有"+count+"个保单需要续保");

		return model;
		
	}
}
