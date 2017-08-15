package com.zzb.conf.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.StringUtil;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.common.TaskConst;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.conf.dao.INSBWorkflowmaintrackDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackdetailDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackdetailDao;
import com.zzb.conf.entity.INSBWorkflowmaintrackdetail;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.entity.INSBWorkflowsubtrackdetail;
import com.zzb.conf.service.INSBWorkflowsubtrackService;
import com.zzb.model.WorkFlow4TaskModel;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBWorkflowsubtrackServiceImpl extends
		BaseServiceImpl<INSBWorkflowsubtrack> implements
		INSBWorkflowsubtrackService {

	@Resource
	private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;
	@Resource
	private INSBWorkflowsubtrackdetailDao workflowsubtrackdetailDao;
	@Resource
	private INSBWorkflowmaintrackdetailDao workflowmaintrackdetailDao;
	@Resource
	private INSBWorkflowmaintrackDao workflowmaintrackDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private CHNChannelService channelService;
	
	
	@Override
	protected BaseDao<INSBWorkflowsubtrack> getBaseDao() {
		return insbWorkflowsubtrackDao;
	}

	@Override
	public Map<String, Object> getMyTaskLastNode(String maininstanceid,
			String instanceid) {
		Map<String, Object> map = null;
		List<Map<String, Object>> trackList = insbWorkflowsubtrackDao
				.selectAllTrack(maininstanceid);
		if (trackList.size() > 1) {
			map = trackList.get(1);
		} else {
			List<Map<String, Object>> mainTrackList = workflowmaintrackDao
					.selectAllTrack(instanceid);
			if (mainTrackList.size() > 0) {
				map = mainTrackList.get(0);
			}
		}
		return map;
	}

	@Override
	public INSBWorkflowsubtrack getSubTrack(String maininstanceid,
			String subinstanceid, String taskcode) {
		INSBWorkflowsubtrack temp = new INSBWorkflowsubtrack();
		temp.setMaininstanceid(maininstanceid);
		temp.setInstanceid(subinstanceid);
		temp.setTaskcode(taskcode);
		return insbWorkflowsubtrackDao.selectOne(temp);
	}

	@Override
	public INSBWorkflowsubtrack getSubTrackByInscomcode(String maininstanceid,
			String inscomcode) {
		// 得到子流程id
		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();// 报价总信息
		quotetotalinfo.setTaskid(maininstanceid);
		quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
		INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
		quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
		quoteinfo.setInscomcode(inscomcode);
		quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);// 报价信息
		INSBWorkflowsub workflowsub = new INSBWorkflowsub();
		workflowsub.setInstanceid(quoteinfo.getWorkflowinstanceid());
		workflowsub = insbWorkflowsubDao.selectOne(workflowsub);// 子流程信息
		INSBWorkflowsubtrack subtrack = new INSBWorkflowsubtrack();
		subtrack.setInstanceid(workflowsub.getInstanceid());
		subtrack.setTaskcode(workflowsub.getTaskcode());
		return insbWorkflowsubtrackDao.selectOne(subtrack);// 子流程轨迹信息
	}

	@Override
	public List<Map<String, Object>> getQuoteInfo(String taskid) {
		List<Map<String, Object>> quoteInfo = insbWorkflowsubtrackDao
				.getQuoteInfo(taskid);
		for (Map<String, Object> map : quoteInfo) {
			if ("3".equals(map.get("taskcode"))) {
				map.put("taskcode", "EDI");
			} else if ("4".equals(map.get("taskcode"))) {
				map.put("taskcode", "精灵");
			} else if ("8".equals(map.get("taskcode"))
					|| "7".equals(map.get("taskcode"))
					|| "6".equals(map.get("taskcode"))) {
				map.put("taskcode", map.get("name"));
			} else {
				map.put("taskcode", "规则");
			}
		}
		return quoteInfo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean addTracks4EndOrClose(Map<String, String> param) {
		if (param.isEmpty()) {
			return false;
		}

        String opt = param.get("operator");
        final String operator = StringUtil.isEmpty(opt) ? "admin" : opt;

        if (StringUtil.isEmpty(opt)) {
            LogUtil.error(param.get("mainTaskId")+","+param.get("subTaskId")+"更新trackdetail时操作人为空");
        }

		// 主流程关闭
		if (StringUtils.isEmpty(param.get("subTaskId"))&& StringUtils.isNoneEmpty(param.get("mainTaskId"))) {
			closeMainTask(param.get("mainTaskId"), param.get("noti"), operator);
			List<String> subList = insbWorkflowsubDao.selectInstanceIdByMainInstanceId(param.get("mainTaskId"));
			if (null != subList && !subList.isEmpty()) {
				for (String str : subList) {
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							closeSubTask(param.get("mainTaskId"), str, param.get("noti"), operator);
						}
					});
				}
			}
			return true;
		} else {
			// 子流程关闭
			String mainInstanceId = "";
			if (StringUtils.isEmpty(param.get("mainTaskId"))) {
				mainInstanceId = insbWorkflowsubDao.selectMainInstanceIdBySubInstanceId(param.get("subTaskId"));
				closeSubTask(mainInstanceId, param.get("subTaskId"), param.get("noti"), operator);
			} else {
				closeSubTask(param.get("mainTaskId"), param.get("subTaskId"), param.get("noti"), operator);
			}
			return true;
		}
	}

	/**
	 * 关闭主流程
	 * 
	 * @param instanceid
	 */
	private boolean closeMainTask(String instanceid, String noti, String operator) {
		// 新增
		INSBWorkflowmaintrackdetail mainTrackModel = new INSBWorkflowmaintrackdetail();

		mainTrackModel.setCreatetime(new Date());
		mainTrackModel.setTaskcode("37");
		mainTrackModel.setTaskstate("Closed");
		mainTrackModel.setNoti(noti);
		mainTrackModel.setInstanceid(instanceid);
		mainTrackModel.setOperator(operator);
		mainTrackModel.setTaskfeedback(noti);

		workflowmaintrackdetailDao.insert(mainTrackModel);
		
		//todo,不需要工作流返回，直接关闭主流程任务。因为之前已经把轨迹写进去了，如果不关闭，工作流返回信息有错误就导致数据不一致。
		
		try {
			WorkFlow4TaskModel dataModel = new WorkFlow4TaskModel();
			dataModel.setMainInstanceId(mainTrackModel.getInstanceid());
			dataModel.setTaskCode(mainTrackModel.getTaskcode());
			dataModel.setTaskName("关闭");
			dataModel.setTaskStatus(mainTrackModel.getTaskstate());
			dataModel.setDataFlag(2);
			channelService.callback(dataModel);
		} catch (Exception e) {
			LogUtil.error("关闭操作推送渠道错误");
		}
		return true;
	}

	public boolean closeSubTask(String mainInstanceId, String subinstanceid, String noti, String operator) {

		INSBWorkflowsubtrackdetail subTrackModel = new INSBWorkflowsubtrackdetail();
		subTrackModel.setTaskcode("37");
		subTrackModel.setTaskstate("Closed");
		subTrackModel.setInstanceid(subinstanceid);
		subTrackModel.setMaininstanceid(mainInstanceId);

		INSBWorkflowsubtrackdetail workflowsubtrackdetail = workflowsubtrackdetailDao.selectOne(subTrackModel);

		if (workflowsubtrackdetail == null) {
			subTrackModel.setCreatetime(new Date());
			subTrackModel.setOperator(operator);
			subTrackModel.setNoti(noti);
			subTrackModel.setTaskfeedback(noti);

			workflowsubtrackdetailDao.insert(subTrackModel);
		} else {
			workflowsubtrackdetail.setNoti(noti);
			workflowsubtrackdetail.setTaskfeedback(noti);
			workflowsubtrackdetailDao.updateById(workflowsubtrackdetail);
		}

		//修改子流程状态为关闭------------------ 
		INSBWorkflowsub workflowsub = new INSBWorkflowsub();
		workflowsub.setInstanceid(subinstanceid);
		workflowsub = insbWorkflowsubDao.selectOne(workflowsub);
		if(null!=workflowsub&&!TaskConst.CLOSE_37.equals(workflowsub.getTaskstate())&&!TaskConst.END_33.equals(workflowsub.getTaskstate())){//如果sub状态不是结束或关闭才修改 
			workflowsub.setTaskcode("37");
			workflowsub.setTaskstate("Closed");
			workflowsub.setOperator(operator);
			insbWorkflowsubDao.updateById(workflowsub);
		}
		LogUtil.info("修改子流程状态为关闭mainInstanceId:subinstanceid-noti="+mainInstanceId+":"+subinstanceid+"-"+noti);
		//修改子流程状态为关闭------------------
		
		try {
			WorkFlow4TaskModel dataModel = new WorkFlow4TaskModel();
			dataModel.setMainInstanceId(subTrackModel.getMaininstanceid());
			dataModel.setSubInstanceId(subTrackModel.getInstanceid());
			dataModel.setTaskCode(subTrackModel.getTaskcode());
			dataModel.setTaskName("关闭");
			dataModel.setTaskStatus(subTrackModel.getTaskstate());
			dataModel.setDataFlag(2);
			
			channelService.callback(dataModel);
		} catch (Exception e) {
			LogUtil.error("关闭操作推送渠道错误mainInstanceId:subinstanceid="+mainInstanceId+"-"+subinstanceid);
		}
		
		return true;

	}

	@Override
	public List<INSBWorkflowsubtrack> selectByTaskcodeList(Map<String, Object> map){
		return this.insbWorkflowsubtrackDao.selectByTaskcodeList(map);
	}
	
	@Override
	public void addTrackdetail(INSBWorkflowsubtrack param,String operator){
		INSBWorkflowsubtrackdetail model =new INSBWorkflowsubtrackdetail();
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("subTaskId", param.getInstanceid());
			INSBWorkflowsubtrackdetail lastmodel = workflowsubtrackdetailDao.selectLastModelBySubInstanceId(params);
			if(null!=lastmodel && "Proccessing".equals(lastmodel.getOperatorstate())){
				LogUtil.debug(param.getTaskcode()+"任务已经被打开="+param.getMaininstanceid());
				return ;
			}
			PropertyUtils.copyProperties(model, param);
			model.setCreatetime(new Date());//设置打开时间
			model.setTaskstate("Proccess");
			model.setOperatorstate("Proccessing");
			model.setId(null);
			model.setModifytime(null);
			model.setOperator(operator);
			model.setFromoperator(operator);
	        if(StringUtil.isEmpty(model.getOperator())){//如果操作人为空，则设置为系统
	        	model.setOperator("admin");
	        }
	        if(StringUtil.isEmpty(model.getOperator())){//如果为空，则设置为系统
	        	model.setFromoperator("admin");
	        }
			workflowsubtrackdetailDao.insert(model);
		} catch (Exception e) {
			LogUtil.error("新增打开任务轨迹组装参数异常="+param.getMaininstanceid());
			e.printStackTrace();
		}
	}
}