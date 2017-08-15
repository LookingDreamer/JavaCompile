package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.common.TaskConst;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBRealtimetaskDao;
import com.zzb.conf.entity.INSBRealtimetask;
import com.zzb.conf.service.INSBRealtimetaskService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBRealtimetaskServiceImpl extends BaseServiceImpl<INSBRealtimetask> implements
		INSBRealtimetaskService {
	@Resource
	private INSBRealtimetaskDao insbRealtimetaskDao;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Override
	protected BaseDao<INSBRealtimetask> getBaseDao() {
		return insbRealtimetaskDao;
	}
	@Override
	public void addRealtimetask(INSBWorkflowmain workflowmain) {
		LogUtil.info("start INSBRealtimetask addRealtimetask " + workflowmain.toString());
		if (workflowmain.getTaskcode() == null || "2".equals(workflowmain.getTaskcode()) || "15".equals(workflowmain.getTaskcode())) return;
		try {
			if (StringUtil.isEmpty(workflowmain.getInstanceid())) {
				LogUtil.info("INSBRealtimetask Instanceid is null : " + workflowmain.toString());
				return;
			}
			Map<String,Object> queryMap = new HashMap();
			queryMap.put("maininstanceid", workflowmain.getInstanceid());
			if (StringUtil.isNotEmpty(workflowmain.getProviderId())) {
				queryMap.put("inscomcode", workflowmain.getProviderId());
			}

			List<Map<String,String>> list = insbQuoteinfoDao.selectTaskInfo(queryMap);
			if (list.isEmpty()) {
				LogUtil.info("INSBRealtimetask list is null " + workflowmain.toString());
				return ;
			}
			Map<String,String> map = list.get(0);
			if (map == null) {
				LogUtil.info("INSBRealtimetask map is null " + workflowmain.toString());
				return;
			}
			INSBRealtimetask insbRealtimetask = new INSBRealtimetask();
			insbRealtimetask.setCreatetime(new Date());
			insbRealtimetask.setModifytime(new Date());
			insbRealtimetask.setOperator("admin");
			insbRealtimetask.setMaininstanceid(workflowmain.getInstanceid());
			insbRealtimetask.setSubinstanceid(map.get("subinstanceid"));
			insbRealtimetask.setTaskcreatetime(new Date());
			insbRealtimetask.setTasktype(workflowmain.getTaskcode());
			String codename = inscCodeDao.getcodeNameBycodeValue(workflowmain.getTaskcode());
			if (StringUtil.isNotEmpty(codename) && !codename.equalsIgnoreCase(workflowmain.getTaskname())) {
				insbRealtimetask.setTasktypename(codename);
			} else {
				insbRealtimetask.setTasktypename(workflowmain.getTaskname());
			}

			insertRealtimetask(insbRealtimetask, map);
			LogUtil.info("INSBRealtimetask insertRealtimetask success " + workflowmain.toString());
		} catch (Exception e) {
			LogUtil.info("INSBRealtimetask error" + workflowmain.toString());
			e.printStackTrace();
		}
	}
	@Override
	public void addRealtimetask(INSBWorkflowsub workflowsub) {
		LogUtil.info("start INSBRealtimetask addRealtimetask " + workflowsub.toString());
		try {
			if (StringUtil.isEmpty(workflowsub.getMaininstanceid())) {
				LogUtil.info("INSBRealtimetask Maininstanceid is null");
				return;
			}
			Map<String,Object> queryMap = new HashMap();
			queryMap.put("maininstanceid", workflowsub.getMaininstanceid());

			if (StringUtil.isNotEmpty(workflowsub.getProviderId())) {
				queryMap.put("inscomcode", workflowsub.getProviderId());
			}
			//核保退回修改后提交核保没有推工作流，获取保险公司
			else if (StringUtil.isEmpty(workflowsub.getProviderId()) && StringUtil.isNotEmpty(workflowsub.getInstanceid())) {
				//获取供应商id和名称
				INSBQuoteinfo quoteinfo = insbQuoteinfoDao.queryQuoteinfoByWorkflowinstanceid(workflowsub.getInstanceid());
				if (quoteinfo != null && StringUtil.isNotEmpty(quoteinfo.getInscomcode())) {
					queryMap.put("inscomcode", quoteinfo.getInscomcode());
				}
			}

			List<Map<String,String>> list = insbQuoteinfoDao.selectTaskInfo(queryMap);
			if (list.isEmpty()) {
				LogUtil.info("INSBRealtimetask list is null " + workflowsub.toString());
				return ;
			}
			Map<String,String> map = list.get(0);
			if (map == null) {
				LogUtil.info("INSBRealtimetask map is null " + workflowsub.toString());
				return;
			}
			INSBRealtimetask insbRealtimetask = new INSBRealtimetask();
			insbRealtimetask.setCreatetime(new Date());
			insbRealtimetask.setModifytime(new Date());
			insbRealtimetask.setOperator("admin");
			insbRealtimetask.setMaininstanceid(workflowsub.getMaininstanceid());
			insbRealtimetask.setSubinstanceid(workflowsub.getInstanceid());
			insbRealtimetask.setTaskcreatetime(new Date());
			insbRealtimetask.setTasktype(workflowsub.getTaskcode());
			String codename = inscCodeDao.getcodeNameBycodeValue(workflowsub.getTaskcode());
			if (StringUtil.isNotEmpty(codename) && !codename.equalsIgnoreCase(workflowsub.getTaskname())) {
				insbRealtimetask.setTasktypename(codename);
			} else {
				insbRealtimetask.setTasktypename(workflowsub.getTaskname());
			}
			insertRealtimetask(insbRealtimetask, map);
			LogUtil.info("INSBRealtimetask insertRealtimetask success " + workflowsub.toString());
		} catch (Exception e) {
			LogUtil.info("INSBRealtimetask error" + workflowsub.toString());
			e.printStackTrace();
		}
	}

	private void insertRealtimetask(INSBRealtimetask insbRealtimetask, Map<String,String> map) {
		insbRealtimetask.setAgentName(map.get("agentName"));
		insbRealtimetask.setAgentNum(map.get("agentNum"));
//		insbRealtimetask.setApplicantcardno(map.get("applicantcardno"));
//		insbRealtimetask.setApplicantid(map.get("applicantid"));
//		insbRealtimetask.setApplicantname(map.get("applicantname"));
		insbRealtimetask.setCarlicenseno(map.get("carlicenseno"));
		insbRealtimetask.setChannelcode(map.get("channelcode"));
		insbRealtimetask.setChannelname(map.get("channelname"));
		insbRealtimetask.setDatasourcesfrom(map.get("datasourcesfrom"));
		insbRealtimetask.setDeptcode(map.get("deptcode"));
		insbRealtimetask.setDeptinnercode(map.get("deptinnercode"));
		insbRealtimetask.setDeptname(map.get("deptname"));
		insbRealtimetask.setInscomcode(map.get("inscomcode"));
		insbRealtimetask.setInscomname(map.get("inscomname"));
		insbRealtimetask.setInsuredcardno(map.get("insuredcardno"));
		insbRealtimetask.setInsuredid(map.get("insuredid"));
		insbRealtimetask.setInsuredname(map.get("insuredname"));
		//insbRealtimetask.setOperatorname(map.get("operatorname"));
		//insbRealtimetask.setOperatorcode(map.get("operatorcode"));
		//insbRealtimetask.setOperatorstate(map.get("operatorstate"));
		LogUtil.info("INSBRealtimetask insertRealtimetask object " + JSONObject.fromObject(insbRealtimetask).toString());
		insbRealtimetaskDao.insert(insbRealtimetask);
	}

	@Override
	public void deleteRealtimetask(INSBWorkflowmain workflowmain) {
		LogUtil.info("start INSBRealtimetask deleteRealtimetask " + workflowmain.toString());

		try {
			if (StringUtil.isEmpty(workflowmain.getInstanceid())) {
				LogUtil.info("INSBRealtimetask Maininstanceid is null");
				return;
			}
			Map<String,Object> queryMap = new HashMap<String,Object>(3);
			queryMap.put("maininstanceid", workflowmain.getInstanceid());
			if (!TaskConst.CLOSE_37.equals(workflowmain.getTaskcode()) && !TaskConst.END_33.equals(workflowmain.getTaskcode()) 
					&& !TaskConst.VERIFYINGFAILED_30.equals(workflowmain.getTaskcode()) && !TaskConst.CANCEL_34.equals(workflowmain.getTaskcode())) {//如果不是结束或者关闭需要对应的任务类型才关闭删除任务记录
				queryMap.put("tasktype", workflowmain.getTaskcode());
			}
			if (StringUtil.isNotEmpty(workflowmain.getProviderId())) {
				queryMap.put("inscomcode", workflowmain.getProviderId());
			}

			insbRealtimetaskDao.deleteRealtimetask(queryMap);
			LogUtil.info("INSBRealtimetask deleteRealtimetask success " + queryMap);
		} catch (Exception e) {
			LogUtil.info("INSBRealtimetask deleteRealtimetask error" + workflowmain.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void deleteRealtimetask(INSBWorkflowsub workflowsub) {
		LogUtil.info("start INSBRealtimetask deleteRealtimetask " + workflowsub.toString());
		if (workflowsub.getTaskcode() == null || "33".equals(workflowsub.getTaskcode())) return;
		try {
			if (StringUtil.isEmpty(workflowsub.getMaininstanceid())) {
				LogUtil.info("INSBRealtimetask Maininstanceid is null");
				return;
			}
			Map<String,Object> queryMap = new HashMap<String,Object>(5);
			queryMap.put("maininstanceid", workflowsub.getMaininstanceid());
			queryMap.put("subinstanceid", workflowsub.getInstanceid());
			if (!TaskConst.CLOSE_37.equals(workflowsub.getTaskcode()) && !TaskConst.END_33.equals(workflowsub.getTaskcode()) 
					&& !TaskConst.VERIFYINGFAILED_30.equals(workflowsub.getTaskcode()) && !TaskConst.CANCEL_34.equals(workflowsub.getTaskcode())) {//如果不是结束或者关闭需要对应的任务类型才关闭删除任务记录
				queryMap.put("tasktype", workflowsub.getTaskcode());
			}
			if (StringUtil.isNotEmpty(workflowsub.getProviderId())) {
				queryMap.put("inscomcode", workflowsub.getProviderId());
			}
			insbRealtimetaskDao.deleteRealtimetask(queryMap);
			LogUtil.info("INSBRealtimetask deleteRealtimetask success " + queryMap);
		} catch (Exception e) {
			LogUtil.info("INSBRealtimetask deleteRealtimetask error" + workflowsub.toString());
			e.printStackTrace();
		}
	}
}