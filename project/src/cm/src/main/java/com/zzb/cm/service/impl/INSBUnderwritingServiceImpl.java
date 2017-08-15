package com.zzb.cm.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBQuotetotalinfoService;

import com.zzb.mobile.service.AppPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.common.WorkFlowUtil;
import com.common.WorkflowFeedbackUtil;
import com.zzb.cm.Interface.service.CoreInterFaceService;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBUnderwritingService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;

@Service
@Transactional
public class INSBUnderwritingServiceImpl extends BaseServiceImpl implements INSBUnderwritingService {
	
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
    @Resource
	private CoreInterFaceService coreInterFaceService;
	@Resource
	private AppPermissionService appPermissionService;
	@Resource
	private InterFaceService interFaceService;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	private INSBQuotetotalinfoService quotetotalinfoService;

	@Override
	protected BaseDao getBaseDao() {
		return null;
	}


	@Override
	public String undwrtSuccess(String processInstanceId, String userid, String taskName, Map<String, Object> data,String inscomcode) {
		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(processInstanceId);
		quotetotalinfo = quotetotalinfoService.queryOne(quotetotalinfo);
		//快速续保不需要校验
		if (quotetotalinfo != null && !"1".equals(quotetotalinfo.getIsrenewal())) {
			String policyValidCheck = coreInterFaceService.validatBeforpush(processInstanceId, inscomcode);
			if (!"success".equals(policyValidCheck)) {
				return policyValidCheck;
			}
		}

		INSBQuoteinfo dataInsbQuoteinfo = insbQuoteinfoService.getByTaskidAndCompanyid(processInstanceId, inscomcode);
		interFaceService.saveHisInfo(dataInsbQuoteinfo.getWorkflowinstanceid(), "person", "D");

		//承保打单改Orderstatus为配送
		Map<String,Object> maporder = new HashMap<String,Object>();
		maporder.put("taskid", processInstanceId);
		maporder.put("inscomcode", inscomcode);              //保险公司id
		INSBOrder temp = insbOrderDao.selectOrderByTaskId(maporder);
        if (temp != null && StringUtil.isNotEmpty(temp.getId())) {
            temp.setOrderstatus("3");
            insbOrderDao.updateById(temp);
        }

		List<INSBPolicyitem> listTemp = insbPolicyitemDao.selectPolicyitemByInscomTask(maporder);
		for (INSBPolicyitem insbPolicyitem : listTemp) {
			insbPolicyitem.setPolicystatus("1");
			insbPolicyitem.setInsureddate(new Date());
			insbPolicyitemDao.updateById(insbPolicyitem);
		}

        if("人工承保".equals(taskName)){//人工承保
        	 WorkflowFeedbackUtil.setWorkflowFeedback(processInstanceId, null, "27", "Completed", taskName, WorkflowFeedbackUtil.underWrittn_complete, userid);
		}else if("承保退回".equals(taskName)){//承保退回
			WorkflowFeedbackUtil.setWorkflowFeedback(processInstanceId, null, "28", "Completed", taskName, WorkflowFeedbackUtil.underWrittn_sendback, userid);
		}else if("打单".equals(taskName)){//打单
			WorkflowFeedbackUtil.setWorkflowFeedback(processInstanceId, null, "23", "Completed", taskName, WorkflowFeedbackUtil.underWrittn_print, userid);
		}
       
		WorkFlowUtil.undwrtSuccess(processInstanceId, userid, taskName, data);
		LogUtil.info("resetPermission jobnum=" + quotetotalinfo.getAgentnum()+" processInstanceId=" +processInstanceId);
		appPermissionService.resetPermission(quotetotalinfo.getAgentnum());
        return "success";
	}

	public String undwrtSuccess(String processInstanceId, String userid, String taskCode, String inscomcode) {
		if (StringUtil.isEmpty(processInstanceId) || StringUtil.isEmpty(taskCode) || StringUtil.isEmpty(inscomcode)) {
			return "参数为空";
		}

		String taskName = null;
		Map<String,Object> data = null;

		if("27".equals(taskCode)){//人工承保
			data = new HashMap<>(1);
			data.put("result", "30");//人工承保成功
			taskName = "人工承保";
		}else if("28".equals(taskCode)){//承保退回
			data = new HashMap<>(1);
			data.put("result", "30");//承保退回成功
			taskName = "承保退回";
		}else if("23".equals(taskCode)){//打单
			taskName = "打单";//打单成功
		}

		LogUtil.info(processInstanceId+","+inscomcode+"--"+taskName+"--操作人=="+userid);
		return undwrtSuccess(processInstanceId,userid,taskName,data,inscomcode);
	}
}