package com.zzb.cm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.service.INSBCommonWorkflowTrackservice;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
@Service
@Transactional
public class INSBCommonWorkflowTrackserviceImpl extends BaseServiceImpl<INSBWorkflowmaintrack> implements
		INSBCommonWorkflowTrackservice {
	@Resource
    private INSBWorkflowmaintrackDao insbWorkflowmaintrackDao;
	@Resource
	private INSBWorkflowmainDao insbWorkflowmainDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	
	@Override
	protected BaseDao<INSBWorkflowmaintrack> getBaseDao() {
		return insbWorkflowmaintrackDao;
	}
	
	/**
	 * 工作流程图获得信息方法 （适用子流程）
	 * */
	@Override
	public List<Map<String, Object>> getWorkFlowTrack(String instanceid,
			String incomcode) {
		//1:信息录入（1）2:报价（3、4、6、7、8、32）16：核保（16、17、18）20：支付（20）21：二次支付（21）25：承保（25、26、27）23：打单（23）24：配送（24）
		List<Map<String, Object>> workflowResult = new ArrayList<Map<String,Object>>();
		Map<String, Object> transAcceptance = null;//承保临时存放
		List<Map<String, Object>> workflowMainTrack = insbWorkflowmainDao.getMainWorkflowViewInfo(instanceid);
		if(workflowMainTrack!=null && workflowMainTrack.size()>0){
			Object taskcode = "";
			for (int i = 0; i < workflowMainTrack.size(); i++) {
				if(workflowMainTrack.get(i)!=null){
					taskcode = workflowMainTrack.get(i).get("taskcode");
					if(taskcode!=null){
						if("25".equals(taskcode)||"26".equals(taskcode)||"27".equals(taskcode)){//承保
							workflowMainTrack.get(i).replace("taskcode", "25");//承保统一用25
							transAcceptance = workflowMainTrack.get(i);
						}else{//其他
							workflowResult.add(workflowMainTrack.get(i));
						}
					}
				}
			}
			if(transAcceptance!=null){
				workflowResult.add(transAcceptance);
			}
		}
		List<String> codeList = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("instanceId", instanceid);
		List<Map<String, Object>> workflowSubTrack = null;
		List<String> taskcodeDate = null;
		//报价节点信息
		codeList = Arrays.asList(new String[]{"3","4","6","7","8","32"});
		params.put("tasktype", codeList);
		workflowSubTrack = insbWorkflowsubDao.getSubWorkflowViewInfo(params);
		if(workflowSubTrack!=null && workflowSubTrack.size()>0){
			Map<String, Object> quoteInfo = new HashMap<String, Object>();
			quoteInfo.put("taskcode", "2");
			quoteInfo.put("taskName", "报价");
			quoteInfo.put("workflowInfo", getQuoteInfoForView(workflowSubTrack));
			taskcodeDate = insbWorkflowsubDao.getInTaskcodeDate(params);
			if(taskcodeDate!=null && taskcodeDate.size()>0){
				quoteInfo.put("createtime", taskcodeDate.get(0));
			}
			workflowResult.add(quoteInfo);
		}
		//核保节点
		codeList = Arrays.asList(new String[]{"16","17","18","40","41"});//增加自动核保code40,41
		params.put("tasktype", codeList);
		workflowSubTrack = insbWorkflowsubDao.getSubWorkflowViewInfo(params);
		if(workflowSubTrack!=null && workflowSubTrack.size()>0){
			Map<String, Object> heBaoInfo = new HashMap<String, Object>();
			heBaoInfo.put("taskcode", "16");
			heBaoInfo.put("taskName", "核保");
			heBaoInfo.put("workflowInfo", getQuoteInfoForView(workflowSubTrack));
			taskcodeDate = insbWorkflowsubDao.getInTaskcodeDate(params);
			if(taskcodeDate!=null && taskcodeDate.size()>0){
				heBaoInfo.put("createtime", taskcodeDate.get(0));
			}
			workflowResult.add(heBaoInfo);
		}
		//支付节点
		codeList = Arrays.asList(new String[]{"20"});
		params.put("tasktype", codeList);
		workflowSubTrack = insbWorkflowsubDao.getSubWorkflowViewInfo(params);
		if(workflowSubTrack!=null && workflowSubTrack.size()>0){
			Map<String, Object> zhiFuInfo = new HashMap<String, Object>();
			zhiFuInfo.put("taskcode", "20");
			zhiFuInfo.put("taskName", "支付");
			zhiFuInfo.put("workflowInfo", getQuoteInfoForView(workflowSubTrack));
			taskcodeDate = insbWorkflowsubDao.getInTaskcodeDate(params);
			if(taskcodeDate!=null && taskcodeDate.size()>0){
				zhiFuInfo.put("createtime", taskcodeDate.get(0));
			}
			workflowResult.add(zhiFuInfo);
		}
		//支付节点
		codeList = Arrays.asList(new String[]{"21"});
		params.put("tasktype", codeList);
		workflowSubTrack = insbWorkflowsubDao.getSubWorkflowViewInfo(params);
		if(workflowSubTrack!=null && workflowSubTrack.size()>0){
			Map<String, Object> erCiZhiFuInfo = new HashMap<String, Object>();
			erCiZhiFuInfo.put("taskcode", "21");
			erCiZhiFuInfo.put("taskName", "二次支付");
			erCiZhiFuInfo.put("workflowInfo", getQuoteInfoForView(workflowSubTrack));
			taskcodeDate = insbWorkflowsubDao.getInTaskcodeDate(params);
			if(taskcodeDate!=null && taskcodeDate.size()>0){
				erCiZhiFuInfo.put("createtime", taskcodeDate.get(0));
			}
			workflowResult.add(erCiZhiFuInfo);
		}
		return workflowResult;
	}
	//组织流程示意图中报价阶段信息
	public String getQuoteInfoForView(List<Map<String, Object>> workflowSubTrack){
		String result = "";
		Object inscomname = "";
		Object quoteway = "";
		String[] temp = null;
		if(workflowSubTrack!=null && workflowSubTrack.size()>0){
			for (int i = 0; i < workflowSubTrack.size(); i++) {
				if(workflowSubTrack.get(i)!=null){
					inscomname = workflowSubTrack.get(i).get("inscomname");
					quoteway = workflowSubTrack.get(i).get("quoteway");
					if(inscomname!=null && !("".equals(inscomname))){
						result += (String)inscomname;
						if(quoteway!=null && !("".equals(quoteway))){
							temp = ((String)quoteway).split(",");
							if(temp.length>0){
								result += "("+temp[(temp.length-1)]+");<br/>";
							}
						}else{
							result += ";<br/>";
						}
					}
				}
			}
			if(result.length()>0 && result.endsWith(";<br/>")){
				result = result.substring(0, result.length()-6);
			}
		}
		return result;
	}

	/**
	 * 工作流程图获得信息方法 
	 * */
	@Override
	public List<Map<String, Object>> getWorkFlowTrack(String instanceid) {
		return getWorkFlowTrack(instanceid, null);
	}
}
