package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.timer.SchedulerService;
import com.cninsure.system.entity.INSCUser;
import com.common.HttpClientUtil;
import com.common.TaskConst;
import com.common.WorkflowFeedbackUtil;
import com.common.redis.CMRedisClient;
import com.zzb.cm.dao.INSBLoopunderwritingDao;
import com.zzb.cm.dao.INSBLoopunderwritingdetailDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBLoopunderwriting;
import com.zzb.cm.entity.INSBLoopunderwritingdetail;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBLoopunderwritingService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackdetailDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.entity.INSBOperatorcomment;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
import com.zzb.conf.entity.INSBWorkflowmaintrackdetail;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.entity.baseData.INSBQueryhistoryinfo;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBOperatorcommentService;
import com.zzb.conf.service.INSBWorkflowmaintrackService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.conf.service.INSBWorkflowsubtrackService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBWorkflowmaintrackServiceImpl extends BaseServiceImpl<INSBWorkflowmaintrack>
		implements INSBWorkflowmaintrackService {
	private static String WORKFLOWURL = "";

	static {
		// 读取相关的配置
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
		WORKFLOWURL = resourceBundle.getString("workflow.url");
	}

	@Resource
	private INSBWorkflowmaintrackDao insbWorkflowmaintrackDao;
	@Resource
	private INSBWorkflowmaintrackdetailDao insbWorkflowmaintrackdetailDao;
	@Resource
	private INSBWorkflowmainDao insbWorkflowmainDao;
	@Resource
	private INSBWorkflowsubtrackDao workflowsubtrackDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBQuotetotalinfoDao quotetotalinfoDao;
	@Resource
	private INSBQuoteinfoDao quoteinfoDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	public Scheduler scheduler;
	@Resource
	private INSBLoopunderwritingDao loopunderwritingDao;
	@Resource
	private INSBLoopunderwritingdetailDao loopunderwritingdetailDao;
	@Resource
	private INSBWorkflowsubService workflowsubService;
	@Resource
	private INSBWorkflowsubtrackService insbWorkflowsubtrackService;
	@Resource
	private INSBOperatorcommentService insbOperatorcommentService;
	@Resource
	private INSBLoopunderwritingService loopunderwritingService;
	@Resource
	private INSBAutoconfigshowService insbAutoconfigShowService;
	@Resource
	private SchedulerService schedule;
	
	@Override
	protected BaseDao<INSBWorkflowmaintrack> getBaseDao() {
		return insbWorkflowmaintrackDao;
	}
	/**
	 * 
	 * @param taskId 任务跟踪号，主任务号  必填
	 * @param taskStatu 任务当前状态=="支付成功"
	 * @param closeReason 关闭流程原因=="全额退款"
	 * @param operator 操作人=="admin"
	 * @return jsonStr == {"message":"关闭成功","status":"success"}
	 */
	public String chnRefundThenCloseflow(String taskId,String taskStatu,String closeReason,String operator){
		if(StringUtil.isEmpty(operator)){
			operator = "admin";
		}
		if(StringUtil.isEmpty(closeReason)){
			closeReason = "全额退款";
		}
		if(StringUtil.isEmpty(taskStatu)){
			taskStatu = WorkflowFeedbackUtil.payment_complete;
		}
		HashMap<String, String> model = new HashMap<String, String>();
		try{
//			String str = null;
			List<INSBWorkflowsub> subList = workflowsubService.selectSubModelByMainInstanceId(taskId);
			if (subList != null) {
				for (INSBWorkflowsub sub : subList) {
					if(TaskConst.PAYING_20.endsWith(sub.getTaskcode())){
						workflowsubService.updateWorkFlowSubData(sub.getMaininstanceid(), sub.getInstanceid(), "37", "Closed", "支付", closeReason, operator);
					}
				}
			}
//			JSONObject result = JSONObject.fromObject(str);
//			if("success".equals(result.getString("status"))){
				model.put("message","关闭成功");
				model.put("status","success");
//			}else{
//				model.put("message","关闭失败");
//				model.put("status","fail");
//			}
			return JSONObject.fromObject(model).toString();
		}catch(Exception e ){
			e.printStackTrace();
			model.put("message","关闭失败");
			model.put("status","fail");
			return JSONObject.fromObject(model).toString();
		}
	}
	@Override
	public Map<String, Object> getMyTaskLastNode(String instanceid) {
		List<Map<String, Object>> trackList = insbWorkflowmaintrackDao.selectAllTrack(instanceid);
		Map<String, Object> map = null;
		if (trackList.size() > 1) {
			map = trackList.get(1);
		}
		// String lastNodeName = (String)map.get("taskname");
		// String lastNodeStatus = (String)map.get("taskstate");
		// return lastNodeName+"("+lastNodeStatus+")";
		return map;
	}

	/**
	 * 通过主流程实例id和任务状态查询任务轨迹
	 */
	@Override
	public INSBWorkflowmaintrack getMainTrack(String maininstanceid, String taskcode) {
		INSBWorkflowmaintrack temp = new INSBWorkflowmaintrack();
		temp.setInstanceid(maininstanceid);
		temp.setTaskcode(taskcode);
		return insbWorkflowmaintrackDao.selectOne(temp);
	}

	/**
	 * 通过主流程实例id获取当前状态的任务轨迹
	 */
	@Override
	public INSBWorkflowmaintrack getMainTrackByInscomcode(String maininstanceid) {
		INSBWorkflowmain workflowmain = new INSBWorkflowmain();
		workflowmain.setInstanceid(maininstanceid);
		workflowmain = insbWorkflowmainDao.selectOne(workflowmain);
		INSBWorkflowmaintrack workflowmaintrack = new INSBWorkflowmaintrack();
		workflowmaintrack.setInstanceid(maininstanceid);
		workflowmaintrack.setTaskcode(workflowmain.getTaskcode());
		return insbWorkflowmaintrackDao.selectOne(workflowmaintrack);
	}

	@Override
	public long countDayTask(Map<String, Object> map) {
		return insbWorkflowmaintrackDao.countDayTask(map);
	}

	@Override
	public long countMonthTask(Map<String, Object> map) {
		return insbWorkflowmaintrackDao.countMonthTask(map);
	}

	@Override
	public Map<String, Object> getUserInfo(String taskid) {
		return insbWorkflowmaintrackDao.getUserInfo(taskid);
	}

	@Override
	public List<Map<String, String>> getWorkflowStatusByInstanceId(String mianInstanceId, String subInstanceId) {
		// 返回值
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		List<Map<String, String>> totalList = new ArrayList<Map<String, String>>();

		List<Map<String, String>> mainStatus = insbWorkflowmaintrackDao.selectByInstanceId4h5(mianInstanceId);

		if (subInstanceId != null) {
			Map<String, String> param = new HashMap<String, String>();
			param.put("instanceid", subInstanceId);
			param.put("maininstanceid", mianInstanceId);
			List<Map<String, String>> subStatus = workflowsubtrackDao.selectByInstanceId4h5(param);
			mainStatus.addAll(subStatus);

		}

		// 排序
		Set<String> mySort = new TreeSet<String>();
		for (Map<String, String> map : mainStatus) {
			if (!map.get("status").equals("99")) {
				mySort.add(map.get("status"));
			}
		}

		// 排序list
		for (String sortStr : mySort) {
			for (Map<String, String> map : mainStatus) {
				if (sortStr.equals(map.get("status"))) {
					totalList.add(map);
					break;
				}

			}
		}

		// 去掉排序字段
		for (Map<String, String> map : totalList) {
			Map<String, String> newMap = new HashMap<String, String>();
			int le = map.get("status").length();
			String statusName = map.get("status").substring(2, le);
			newMap.put("statusName", statusName);
			newMap.put("time", map.get("createtime"));

			result.add(newMap);
		}

		return result;
	}

	/**
	 * liuchao 人工核保页面核保轮询操作调用接口
	 * 点击轮序按钮，调用工作流进入轮询节点，工作流调用成功，会调用轮询接口，页面直接回到我的任务页面，轮询接口返回结果后重新调用工作流接口
	 */
	@Override
	public Map<String, Object> loopUnderwriting(String maininstanceId, String subInstanceId, String inscomcode, String userCode) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			// 根据子流程查询此子流程是否在轮询节点，18人工核保节点
			INSBWorkflowsub worksub = new INSBWorkflowsub();
			worksub.setInstanceid(subInstanceId);
			worksub = insbWorkflowsubDao.selectOne(worksub);
			if (worksub != null) {
				if ("18".equals(worksub.getTaskcode())) {
					Map<String, Object> params = new HashMap<>();
					params.put("userid", userCode);
					params.put("processinstanceid", Integer.parseInt(subInstanceId));// 子流程节点
					params.put("taskName", "人工核保");
					Map<String, Object> data = new HashMap<>();
					data.put("result", "1"); 
					data.put("underwriteway", "4"); // 人工核保流向轮询节点标记
					params.put("data", data);
					JSONObject jsonb = JSONObject.fromObject(params);
					Map<String, String> param = new HashMap<>();
					param.put("datas", jsonb.toString());

					WorkflowFeedbackUtil.setWorkflowFeedback(maininstanceId, subInstanceId, worksub.getTaskcode(), "Completed",
							(String) params.get("taskName"), WorkflowFeedbackUtil.underWriting_loop, userCode);

					LogUtil.info("workflowToLoopNode start...maininstanceId=" + maininstanceId
							+ ",subInstanceId=" + subInstanceId + ",inscomcode=" + inscomcode + ",userCode=" + userCode);
					LogUtil.info("推送工作流传递参数：" + jsonb.toString());

					String message = HttpClientUtil.doGet(WORKFLOWURL + "/process/completeSubTask", param);

					LogUtil.info("%s推送轮询节点返回数据：%s",maininstanceId,message);

//					JSONObject jo = JSONObject.fromObject(message);
//					if ("success".equals(jo.getString("message"))) {
						CMRedisClient.getInstance().set("TaskCurrentCode", maininstanceId, "18", 20 * 60);
						//推工作流会导致自动能力重新暂存，所以不推了，直接修改流程轨迹
						// underwritingToLoop(subInstanceId, userCode);

						LogUtil.info("%s,%s,%sworkflowToLoopNode success...",maininstanceId, subInstanceId, inscomcode);
						result.put("status", "success");
						result.put("msg", "成功发起核保轮询！");
						// 发起核保回写轮询
	//					return toLoopUnderwriting(maininstanceId, subInstanceId, inscomcode, userCode);
					/*} else {
						LogUtil.error(maininstanceId+","+subInstanceId+","+inscomcode+" workflowToLoopNode fail...");
						result.put("status", "fail");
						result.put("msg", "推送流程失败，发起核保轮询失败！");
					}*/
				} else {
					// 工作流已经不在人工核保节点
					LogUtil.error(maininstanceId+","+subInstanceId+","+inscomcode+"此子流程不在人工核保节点...");
					result.put("status", "fail");
					result.put("msg", "此任务不在人工核保节点！");
				}
			} else {
				// 工作流不存在
				LogUtil.error(maininstanceId+","+subInstanceId+","+inscomcode+"此任务不存在...");
				result.put("status", "fail");
				result.put("msg", "此任务不存在！");
			}
		} catch (Exception e) {
			LogUtil.error(maininstanceId+","+subInstanceId+","+inscomcode+" workflowToLoopNode fail...");
			result.put("status", "fail");
			result.put("msg", "系统内部出错，发起核保轮询失败！");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * liuchao 人工核保推送到核保轮询节点后，工作流回调接口
	 * 每5分钟调用核保回写一次，一共调用三次（间隔时间和调用次数可以配置），每次使用流程中最新的精灵或EDI核保途径发起核保回写，
	 * 根据最后核保轮询回写结果，调用工作流对应接口
	 */
	@Override
	public Map<String, Object> toLoopUnderwriting(String maininstanceId, String subInstanceId, String inscomcode, String operator) {
		Map<String, Object> result = new HashMap<>(2);
		// 默认间隔5分钟，轮询3次
		int intervalTime = 5;
		int totalCount = 3;
		try {
			// 读取相关的配置
			ResourceBundle resourceBundle = ResourceBundle.getBundle("config/underwritingloop");
			String totalCountStr = resourceBundle.getString("underwritingloop.count");
			String intervalTimeStr = resourceBundle.getString("underwritingloop.interval");
			// 定时任务间隔单位分钟
			if (!StringUtils.isEmpty(intervalTimeStr)) {
				int intervalTimeTemp = Integer.parseInt(intervalTimeStr);
				if (intervalTimeTemp >= 0) {
					intervalTime = intervalTimeTemp;
				}
			}
			// 定时任务循环次数
			if (!StringUtils.isEmpty(totalCountStr)) {
				int totalCountTemp = Integer.parseInt(totalCountStr);
				if (totalCountTemp > 0) {
					totalCount = totalCountTemp;
				}
			}
			LogUtil.info("===读取配置文件中核保回写参数:轮询次数" + totalCount + ";时间间隔" + intervalTime + "分钟===");
		} catch (NumberFormatException e1) {
			intervalTime = 5;
			totalCount = 3;
			LogUtil.info("===读取配置文件中核保回写轮询次数和时间间隔异常！使用默认间隔5分钟，轮询3次===");
			e1.printStackTrace();
		}

		String loopid = null;

		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("instanceid", subInstanceId);
			map.put("taskcode", "38");
			INSBWorkflowsubtrack workflowsubtrack = workflowsubtrackDao.selectByInstanceIdTaskCode(map);
			if (workflowsubtrack != null) {
				if (StringUtil.isEmpty(maininstanceId)) {
					maininstanceId = workflowsubtrack.getMaininstanceid();
				}
				if (StringUtil.isEmpty(inscomcode)) {
					INSBQuoteinfo quoteinfo = quoteinfoDao.queryQuoteinfoByWorkflowinstanceid(subInstanceId);
					if (quoteinfo != null) {
						inscomcode = quoteinfo.getInscomcode();
					}
				}

				INSBLoopunderwriting loopunderwriting = new INSBLoopunderwriting();
				loopunderwriting.setTasktype("insure"); //核保轮询
				loopunderwriting.setOperator("admin");
				loopunderwriting.setCreatetime(new Date());
				loopunderwriting.setModifytime(new Date());
				loopunderwriting.setNoti("工作流轮询回调当前状态：" + workflowsubtrack.getTaskstate());
				loopunderwriting.setTaskid(maininstanceId);
				loopunderwriting.setInscomcode(inscomcode);
				loopunderwriting.setTaskcreatetime(workflowsubtrack.getCreatetime());
				loopunderwriting.setLoopstatus("start");

				loopunderwritingDao.insert(loopunderwriting);
				loopid = loopunderwriting.getId();
			} else {
				LogUtil.error(maininstanceId + "," + subInstanceId + "当前不在轮询状态");
			}
		} catch (Exception e) {
			LogUtil.error(maininstanceId + "," + subInstanceId + "记录轮询数据出错：" + e.getMessage());
			e.printStackTrace();
		}

		boolean stoploop = false;
		String msg = "";

		try {
			// 获得按创建时间倒序排序的核保节点轨迹列表，下标为0的是最后核保途径
			List<String> underwritingList = insbOrderDao.getUnderwritingTrackStr(subInstanceId);

			//检查当前是否具体对应的自动能力
			List<String> quotetypes = searchQuoteTypes(subInstanceId, inscomcode);
			if (quotetypes != null) {
				if (quotetypes.isEmpty()) {
					stoploop = true;
					msg = "无自动核保能力";
					LogUtil.info(maininstanceId + "," + subInstanceId + "," + inscomcode + msg);
				} else if (underwritingList != null && !underwritingList.isEmpty()) {
					String quoteType = underwritingList.get(0);
					if ("16".equals(quoteType) && !quotetypes.contains("01")) {
						stoploop = true;
						msg = "无EDI核保能力";
						LogUtil.info(maininstanceId + "," + subInstanceId + "," + inscomcode + msg);
					} else if ("17".equals(quoteType) && !quotetypes.contains("02")) {
						stoploop = true;
						msg = "无精灵核保能力";
						LogUtil.info(maininstanceId + "," + subInstanceId + "," + inscomcode + msg);
					}
				}
			} else {
				//不阻止后面的处理
				LogUtil.info(maininstanceId + "," + subInstanceId + "," + inscomcode + "核保自动能力查询接口返回空");
			}

			if (!stoploop) {
				if (underwritingList != null && !underwritingList.isEmpty()) {
					// 创建定时器，异步开启核保轮询动作
					// 定时器唯一标示名
					String schedulerKey = "loopUnderWritingJob" + subInstanceId;
					LogUtil.info("===核保轮询定时任务开始创建===maininstanceId=" + maininstanceId + ",subInstanceId=" + subInstanceId
							+ ",inscomcode=" + inscomcode + ",intervalTime=" + intervalTime + ",totalCount=" + totalCount + ",schedulerKey=" + schedulerKey);

					/*// 定时任务数据
					JobDataMap jobMap = new JobDataMap();
					jobMap.put("maininstanceId", maininstanceId);
					jobMap.put("subInstanceId", subInstanceId);
					jobMap.put("inscomcode", inscomcode);
					jobMap.put("underwritingList", underwritingList);
					jobMap.put("loopid", loopid);

					// 创建定时任务内容
					JobDetail job = JobBuilder.newJob(LoopUnderWritingJob.class).withIdentity(schedulerKey).setJobData(jobMap).build();
					LogUtil.info("===成功创建核保轮询定时器任务job===");
					// 创建触发器
					Trigger trigger = TriggerBuilder.newTrigger().withIdentity(schedulerKey)
							.withSchedule(simpleSchedule().withIntervalInSeconds(intervalTime * 60).withRepeatCount(totalCount - 1))
							.build();
					LogUtil.info("===成功创建核保轮询定时任务触发器===");
					// 组装各个组件job，Trigger
					scheduler.scheduleJob(job, trigger);
					LogUtil.info("===成功组装核保轮询定时器任务===");
					// 开始定时器
					scheduler.start();
					LogUtil.info("===成功开启核保回写定时器任务===");*/
					
					// 通过redis处理核保轮询  超时任务
					String taskName = "核保轮询";
					StringBuffer jobName = new StringBuffer(subInstanceId);
					jobName.append('_');
					jobName.append(taskName);
					jobName.append('_');
					jobName.append(inscomcode);
					jobName.append('_');
					jobName.append(loopid);
					jobName.append('_');
					jobName.append(totalCount);
					String value = maininstanceId + "_" + inscomcode;
					long timeout = 120000; //第一次执行轮询时间2分钟之后
					Date executeTime = new Date(new Date().getTime() + timeout); //执行时间
					schedule.dealLoopOverTime(jobName.toString(), taskName, executeTime, value, String.valueOf(timeout), null, 0);

					result.put("status", "success");
					result.put("msg", "成功发起核保轮询！");
				} else {
					stoploop = true;
					msg = "无自动核保能力";
					LogUtil.error("===没有查询到精灵或EDI核保轨迹，发起核保轮询任务失败===maininstanceId=" +
							maininstanceId + ",subInstanceId=" + subInstanceId + ",inscomcode=" + inscomcode);
				}
			}
		} catch (Exception e) {
			stoploop = true;
			msg = "创建轮询任务出错";
			LogUtil.error("===maininstanceId="+maininstanceId+",subInstanceId="+subInstanceId+",inscomcode="+inscomcode+
					"创建核保轮询定时任务时出错："+e.getMessage());
			e.printStackTrace();
		}

		//停止轮询
		if (stoploop) {
			result.put("status", "fail");
			result.put("msg", "发起核保轮询失败！" + msg);

			//更新状态
			updateLoopUnderWriting(loopid, "fail");
			//推工作流转人工
			try {
				//loopToUnderwriting(subInstanceId, operator, WorkflowFeedbackUtil.loop_fail);
				loopUnderwritingWorkFlowToNext(subInstanceId, "1", maininstanceId, inscomcode);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return result;
	}

	private List<String> searchQuoteTypes(String subInstanceId, String inscomcode) {
		try {
			INSBQuoteinfo insbQuoteinfo = quoteinfoDao.queryQuoteinfoByWorkflowinstanceid(subInstanceId);
			if (insbQuoteinfo == null) return null;

			Map<String, Object> deptmap = new HashMap<String, Object>();
			deptmap.put("providerid", StringUtil.isNotEmpty(inscomcode) ? inscomcode : insbQuoteinfo.getInscomcode());
			deptmap.put("conftype", "02");
			List<String> tempDeptIds = insbAutoconfigShowService.getParentDeptIds4Show(insbQuoteinfo.getDeptcode());
			deptmap.put("deptList", tempDeptIds);
			return insbAutoconfigShowService.queryByProId(deptmap);
		} catch (Exception e) {
			LogUtil.error(subInstanceId+","+inscomcode+"查询核保自动能力时出错："+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private void updateLoopUnderWriting(String loopId, String loopstatus) {
		if (StringUtil.isEmpty(loopId)) return;

		try {
			INSBLoopunderwriting loopunderwriting = loopunderwritingService.queryById(loopId);
			if (loopunderwriting == null) return;

			loopunderwriting.setLoopstatus(loopstatus);
			loopunderwritingDao.updateById(loopunderwriting);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, Object> loopWorkFlowToNext(String maininstanceId, String subInstanceId, String inscomcode, String result, String msg) {
		return loopUnderwritingWorkFlowToNext(subInstanceId, result, maininstanceId, inscomcode);
	}

	public Map<String, Object> updateLoopUnderWritingDetail(String maininstanceId, String subInstanceId, String inscomcode, String loopResult, String msg) {
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			INSBLoopunderwriting queryloopunderwriting = new INSBLoopunderwriting();
			queryloopunderwriting.setTaskid(maininstanceId);
			queryloopunderwriting.setInscomcode(inscomcode);
			INSBLoopunderwriting loopunderwriting = loopunderwritingDao.selectOne(queryloopunderwriting);
			if (loopunderwriting == null) {
				LogUtil.error(maininstanceId + "," + inscomcode + "无loopunderwriting数据");
				result.put("status", "fail");
				result.put("msg", "无loopunderwriting数据");
				return result;
			}

			if (!"success".equals(loopResult)) {
				if ("stop".equals(loopResult)) {
					loopunderwriting.setLoopstatus("fail");
					loopunderwritingDao.updateById(loopunderwriting);
				}

				if (StringUtil.isEmpty(msg)) {
					INSBWorkflowsubtrack queryInsbWorkflowsubtrack = new INSBWorkflowsubtrack();
					queryInsbWorkflowsubtrack.setMaininstanceid(maininstanceId);
					queryInsbWorkflowsubtrack.setInstanceid(subInstanceId);
					queryInsbWorkflowsubtrack.setTaskcode("38");
					List<INSBWorkflowsubtrack> insbWorkflowsubtracks = insbWorkflowsubtrackService.queryList(queryInsbWorkflowsubtrack);

					String trackId = null;
					if (insbWorkflowsubtracks.size() > 0) {
						trackId = insbWorkflowsubtracks.get(0).getId();
					} else {
						LogUtil.error(maininstanceId + "," + subInstanceId + "在workflowsubtrack无轮询状态");
					}

					if (StringUtil.isNotEmpty(trackId)) {
						INSBOperatorcomment queryInsbOperatorcomment = new INSBOperatorcomment();
						queryInsbOperatorcomment.setTrackid(trackId);
						queryInsbOperatorcomment.setTracktype(2);
						List<INSBOperatorcomment> insbOperatorcomments = insbOperatorcommentService.queryList(queryInsbOperatorcomment);

						if (insbOperatorcomments.size() > 0) {
							msg = insbOperatorcomments.get(0).getCommentcontent();
						} else {
							LogUtil.error(maininstanceId + "," + subInstanceId + "," + trackId + "无备注数据");
						}
					}
				}
			} else {
				loopunderwriting.setLoopstatus(loopResult);
				loopunderwritingDao.updateById(loopunderwriting);
			}

			List<INSBLoopunderwritingdetail> loopunderwritingdetailList = loopunderwritingdetailDao.queryByLoopId(loopunderwriting.getId());

			if (loopunderwritingdetailList != null && !loopunderwritingdetailList.isEmpty()) {
				for (INSBLoopunderwritingdetail loopunderwritingdetail : loopunderwritingdetailList) {
					LogUtil.info(maininstanceId + "," + subInstanceId + "检查轮询结果详情:" + loopunderwritingdetail.getId() + "=" + loopunderwritingdetail.getLoopresult());

					if ("start".equals(loopunderwritingdetail.getLoopresult())) {
						loopunderwritingdetail.setLoopresult(!"stop".equals(loopResult) ? loopResult : "fail");
						loopunderwritingdetail.setMsg(msg);
						loopunderwritingdetailDao.updateById(loopunderwritingdetail);

						LogUtil.info(maininstanceId + "," + subInstanceId + "更新轮询结果详情:" + loopunderwritingdetail.getId() + "=" + msg);
						break;
					}
				}
			} else {
				LogUtil.error(maininstanceId + "," + subInstanceId + "," + loopunderwriting.getId() + "无轮询结果详情");
			}

			result.put("status", "success");
			result.put("msg", "轮询结果详情更新成功");
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", "轮询结果详情更新出错");
			LogUtil.error(maininstanceId + "," + subInstanceId + "," + loopResult + "轮询结果更新出错：" + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	public Map<String, Object> logLoopUnderWritingDetail(String loopId, String startTime, String loopResult, String msg) {
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			INSBLoopunderwritingdetail loopunderwritingdetail = new INSBLoopunderwritingdetail();
			loopunderwritingdetail.setOperator("admin");
			loopunderwritingdetail.setCreatetime(new Date());
			loopunderwritingdetail.setModifytime(new Date());
			loopunderwritingdetail.setLoopid(loopId);
			loopunderwritingdetail.setStarttime(DateUtil.parseDateTime(startTime));
			loopunderwritingdetail.setLoopresult(loopResult);

			String times = "", m = "";

			if (StringUtil.isNotEmpty(msg)) {
				String[] data = msg.split("#");
				if (data != null) {
					if (data.length == 1) {
						times = data[0];
					} else if (data.length > 1) {
						times = data[0];
						m = data[1];
					}
				}
			}

			loopunderwritingdetail.setNoti(times);
			loopunderwritingdetail.setMsg(m);

			loopunderwritingdetailDao.insert(loopunderwritingdetail);
			result.put("status", "success");
			result.put("msg", "记录轮询日志详情成功！");
		} catch (Exception e) {
			result.put("status", "fail");
			result.put("msg", "记录轮询日志详情失败！");
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * liuchao 核保回写轮询后处理回写结果接口，精灵或EDI核保回写后调用
	 * 
	 * @param subInstanceId
	 *            子流程id
	 * @param result
	 *            核保回写结果(success或fail)
	 */
	@Override
	public Map<String, Object> loopUWResultHandler(String subInstanceId, String result, String maininstanceId, String inscomcode, String msg) {
		LogUtil.info("===精灵或EDI核保轮询回写后调用接口的入参:sunInstanceid="+subInstanceId+",result="+result+",maininstanceId="+maininstanceId);
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 根据子流程查询此子流程是否在轮询节点，38轮询节点
		/*INSBWorkflowsub worksub = new INSBWorkflowsub();
		worksub.setInstanceid(subInstanceId);
		worksub = insbWorkflowsubDao.selectOne(worksub);
		if (worksub != null) {
			LogUtil.info("===任务现在的节点===subInstanceId="+subInstanceId+",taskcode="+worksub.getTaskcode()+",maininstanceId="+maininstanceId);

			if ("38".equals(worksub.getTaskcode())) {*/
				String jobName = "loopUnderWritingJob" + subInstanceId;

				// 当前子流程处于轮询节点
				if ("success".equals(result)) {
					// 核保回写成功
					LogUtil.info("===轮询成功===subInstanceId=" + subInstanceId + ",maininstanceId="+maininstanceId);
					//更新轮询结果
					updateLoopUnderWritingDetail(maininstanceId, subInstanceId, inscomcode, "success", msg);

					try {
						// 工作流推送
						//Map<String, Object> r = loopUnderwritingWorkFlowToNext(subInstanceId, "3", maininstanceId, inscomcode);
						//LogUtil.info("===已经完成工作流推送===subInstanceId=" + subInstanceId + ",maininstanceId="+maininstanceId+"："+ JsonUtil.serialize(r));

						deleteSchedulerJob(jobName);

						LogUtil.info("===结束核保轮询任务正常完成！===");
						resultMap.put("status", "success");
						resultMap.put("msg", "核保回写结果处理完毕！");
					} catch (SchedulerException e) {
						LogUtil.info("===结束核保轮询任务时移除定时任务异常！===");
						e.printStackTrace();
						resultMap.put("status", "fail");
						resultMap.put("msg", "移除定时任务时异常！");
					} catch (Exception e) {
						LogUtil.info("===结束核保轮询任务时异常！===");
						e.printStackTrace();
						resultMap.put("status", "fail");
						resultMap.put("msg", "核保轮询成功后，结束核保轮询任务时异常！");
					}
				} else {
					boolean isJobExist = true;
					if ("logDetect".equals(result)) {
						//isJobExist = isSchedulerJobExist(jobName);
						LogUtil.error(maininstanceId+"检查定时任务"+jobName+"是否存在："+isJobExist);

						if (isJobExist) {
							updateLoopUnderWritingDetail(maininstanceId, subInstanceId, inscomcode, "fail", null);
							resultMap.put("status", "success");
							resultMap.put("msg", "核保回写结果处理完毕！");
							return resultMap;
						}
					}

					// 核保回写失败
					LogUtil.info("===轮询失败===subInstanceId=" + subInstanceId + ",maininstanceId="+maininstanceId);
					//更新轮询结果
					updateLoopUnderWritingDetail(maininstanceId, subInstanceId, inscomcode, "stop", msg);

					try {
						// 工作流推送
						//Map<String, Object> r = loopUnderwritingWorkFlowToNext(subInstanceId, "1", maininstanceId, inscomcode);
						//LogUtil.info("===已经完成工作流推送===subInstanceId=" + subInstanceId + ",maininstanceId="+maininstanceId+"："+ JsonUtil.serialize(r));

						deleteSchedulerJob(jobName);

						LogUtil.info("===结束核保轮询任务正常完成！===");
						resultMap.put("status", "success");
						resultMap.put("msg", "核保回写结果处理完毕！");
					} catch (SchedulerException e) {
						LogUtil.info("===结束核保轮询任务时移除定时任务异常！===");
						e.printStackTrace();
						resultMap.put("status", "fail");
						resultMap.put("msg", "移除定时任务时异常！");
					} catch (Exception e) {
						LogUtil.info("===结束核保轮询任务时异常！===");
						e.printStackTrace();
						resultMap.put("status", "fail");
						resultMap.put("msg", "核保轮询失败后，结束核保轮询任务时异常！");
					}

					if (!isJobExist) {
						resultMap.put("status", "stopped");
					}
				}
			/*} else {
				// 当前子流程没有处于轮询节点
				LogUtil.info("===核保回写后处理时，查询到子流程记录不在轮询节点===subInstanceId=" + subInstanceId + ",maininstanceId="+maininstanceId);
				resultMap.put("status", "success");
				resultMap.put("msg", "核保回写结果处理完毕！");
			}
		} else {
			// 没有查询到子流程记录
			LogUtil.info("===核保回写后处理时没有查询到子流程记录===subInstanceId=" + subInstanceId + ",maininstanceId="+maininstanceId);
			resultMap.put("status", "fail");
			resultMap.put("msg", "核保回写结果处理:没有查询到子流程记录！");
		}*/
		return resultMap;
	}

	// 查询指定定时任务是否结束
	private boolean isSchedulerJobExist(String jobName) {
		JobKey jobKey = JobKey.jobKey(jobName);
		try {
			return scheduler.checkExists(jobKey);
		} catch (Exception e) {
			LogUtil.error("检查定时任务"+jobName+"是否存在时出错："+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	// 移除定时任务
	public void deleteSchedulerJob(String jobName) throws SchedulerException {
		TriggerKey triggerKey = new TriggerKey(jobName);
		scheduler.pauseTrigger(triggerKey);// 停止触发器
		LogUtil.info("===停止触发器完成===jobName=" + jobName);

		boolean removeTriRst = scheduler.unscheduleJob(triggerKey);// 移除触发器
		LogUtil.info("===移除触发器完成===jobName=" + jobName + ",removeTriRst=" + removeTriRst);

		JobKey jobKey = JobKey.jobKey(jobName);
		boolean removeJobRst = scheduler.deleteJob(jobKey);
		LogUtil.info("===移除job完成===jobName=" + jobName + ",removeJobRst=" + removeJobRst);
	}

	private void underwritingToLoop(String subInstanceId, String operator) {
		workflowsubService.updateWorkFlowSubData(null, subInstanceId, "18", "Completed", "人工核保", WorkflowFeedbackUtil.underWriting_loop, operator);
		workflowsubService.updateWorkFlowSubData(null, subInstanceId, "38", "Reserved", "轮询", "", operator);
	}

	private void loopToUnderwriting(String subInstanceId, String operator, String taskfeedback) {
		workflowsubService.updateWorkFlowSubData(null, subInstanceId, "38", "Completed", "轮询", taskfeedback, "admin");
		workflowsubService.updateWorkFlowSubData(null, subInstanceId, "18", "Reserved", "人工核保", "", operator);

		/*if (StringUtil.isNotEmpty(operator) && StringUtil.isNotEmpty(subInstanceId)) {
			INSBWorkflowsub sub = new INSBWorkflowsub();
			sub.setInstanceid(subInstanceId);
			sub = workflowsubService.queryOne(sub);
			if (sub != null && "18".equals(sub.getTaskcode()) && !"Completed".equals(sub.getTaskstate())) {
				sub.setOperator(operator);
				workflowsubService.updateById(sub);
			}
		}*/
	}

	/**
	 * liuchao 核保回写后推送工作流
	 */
	public Map<String, Object> loopUnderwritingWorkFlowToNext(String subInstanceId, String result, String maininstanceId, String inscomcode) {
		Map<String, Object> temp = new HashMap<String, Object>();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userid", "admin");// 轮询节点写死
		params.put("processinstanceid", Integer.parseInt(subInstanceId));// 子流程节点
		params.put("taskName", "核保查询");
		Map<String, Object> data = new HashMap<String, Object>();
		// 人工核保流向轮询节点标记
		data.put("result", result);// 1失败=转人工，3成功=转支付
		if("1".equals(result))
			data.put("underwriteway", "3");
		params.put("data", data);
		JSONObject jsonb = JSONObject.fromObject(params);
		Map<String, String> param = new HashMap<String, String>();
		param.put("datas", jsonb.toString());

		LogUtil.info("开始推送工作流从轮询节点向下流转...参数：" + jsonb.toString());
		String message = HttpClientUtil.doGet(WORKFLOWURL + "/process/completeSubTask", param);
		LogUtil.info("推送工作流从轮询节点向下流转完成...返回数据：" + message);

		JSONObject jo = JSONObject.fromObject(message);
		if ("success".equals(jo.getString("message"))) {
			LogUtil.info("===轮询节点推送工作流成功===subInstanceId=" + subInstanceId + ",result=" + result);
			temp.put("status", "success");
			temp.put("msg", "轮询节点推送工作流成功！");
		} else {
			LogUtil.info("===轮询节点推送工作流失败===subInstanceId=" + subInstanceId + ",result=" + result);
			temp.put("status", "fail");
			temp.put("msg", "轮询节点推送工作流失败！");
			throw new RuntimeException("轮询节点推送工作流失败!result=" + result);
		}

		return temp;
	}

	// ***********************************************************************************
	final String totalWorkflowNode = "1,2,3,4,6,7,8,13,14,31,17,18,19,16,20,27,28,23,25,26,24,33";
	private boolean isShowNode(String curNode) {
		StringTokenizer st = new StringTokenizer(totalWorkflowNode, ",");
		while (st.hasMoreTokens()) {
			if( st.nextToken().equals(curNode) ) {
				return true;
			}
		}
		return false;
	}
	
	private Map<String, String> OrganizeWorkFlowInfo(String maininstanceid, String subinstanceid, String maintaskstate, 
			String subtaskstate, String maintaskcode, String subtaskcode, boolean secflag) {
		String closeNode = "";
		if("Closed".equals(subtaskstate)) {
			closeNode = "1";
		}
		String taskcode = subtaskcode;
		if( "end".equals(maintaskstate) ) {
			taskcode = maintaskcode;
		} else if( "end".equals(subtaskstate) ) {
			taskcode = maintaskcode;
		} else if( "Closed".equals(subtaskstate) ) {
			List<INSBWorkflowsubtrack> subtrackList = workflowsubtrackDao.getWorkflowsubBySubId(subinstanceid);
			for( INSBWorkflowsubtrack insbWorkflowsubtrack : subtrackList ) {
				taskcode = insbWorkflowsubtrack.getTaskcode();
				break;
			}
		}
		if( !isShowNode(taskcode) ) {
			List<INSBWorkflowsubtrack> subtrackList = workflowsubtrackDao.getWorkflowsubBySubId(subinstanceid);
			for( INSBWorkflowsubtrack insbWorkflowsubtrack : subtrackList ) {
				taskcode = insbWorkflowsubtrack.getTaskcode();
				if( isShowNode(taskcode) ) {
					break;
				}
			}
		}
		List<Map<String, String>> temp = GetWorkFlowInfos(secflag, 0);
		Map<String, String> map = new HashMap<String, String>();
		int flag = 0;
		int closeNum = 0;
		for (int i = 0; i < temp.size(); i++) {
			if (temp.get(i).get("taskCode").contains(taskcode)) {
				if( "1".equals(closeNode) ) {
					closeNum = i;
					flag = i + 1;
				} else {
					flag = i;
				}
				break;
			}
		}
		map.put("flag", String.valueOf(flag));
		map.put("closeNum", String.valueOf(closeNum));
		return map;
	}

	private List<Map<String, String>> GetWorkFlowInfosXB(boolean flag, int closeNum) {
		List<Map<String, String>> temp = new ArrayList<Map<String, String>>();
		Map<String, String> map = new TreeMap<String, String>();
		Map<String, String> map2 = new TreeMap<String, String>();
		Map<String, String> map3 = new TreeMap<String, String>();
		Map<String, String> map4 = new TreeMap<String, String>();
		Map<String, String> map6 = new TreeMap<String, String>();
		Map<String, String> map7 = new TreeMap<String, String>();
		Map<String, String> map8 = new TreeMap<String, String>();
		Map<String, String> map9 = new TreeMap<String, String>();
		map.put("taskCode", "1");
		map.put("taskName", "信息录入");
		map2.put("taskCode", "15");
		map2.put("taskName", "快速续保");
		map3.put("taskCode", "17,18,19,16");
		if( closeNum == 1 ) {
			map3.put("taskName", "关闭");
		} else {
			map3.put("taskName", "核保");
		}
		if( closeNum == 2 ) {
			map4.put("taskCode", "33");
			map4.put("taskName", "关闭");
		} else {
			map4.put("taskCode", "20");
			map4.put("taskName", "支付");
		}
		map6.put("taskCode", "27");
		map6.put("taskName", "承保");
		map7.put("taskCode", "28,23,25,26,27");
		map7.put("taskName", "打单");
		map8.put("taskCode", "24");
		map8.put("taskName", "配送");
		map9.put("taskCode", "33");
		map9.put("taskName", "完成");
		temp.add(map);
		temp.add(map2);
		if( closeNum == 1 ) {
			temp.add(map3);
		} else if( closeNum == 2 ) {
			temp.add(map3);
			temp.add(map4);
		} else {
			temp.add(map3);
			temp.add(map4);
			if (flag) {
				Map<String, String> map5 = new HashMap<String, String>();
				map5.put("taskCode", "21");
				map5.put("taskName", "二次支付");
				temp.add(map5);
			}
			temp.add(map6);
			temp.add(map7);
			temp.add(map8);
			temp.add(map9);
		}
		return temp;
	}
	
	private List<Map<String, String>> GetWorkFlowInfos(boolean flag, int closeNum) {
		List<Map<String, String>> temp = new ArrayList<Map<String, String>>();
		Map<String, String> map = new TreeMap<String, String>();
		Map<String, String> map2 = new TreeMap<String, String>();
		Map<String, String> map3 = new TreeMap<String, String>();
		Map<String, String> map4 = new TreeMap<String, String>();
		Map<String, String> map6 = new TreeMap<String, String>();
		Map<String, String> map7 = new TreeMap<String, String>();
		Map<String, String> map8 = new TreeMap<String, String>();
		Map<String, String> map9 = new TreeMap<String, String>();
		map.put("taskCode", "1");
		map.put("taskName", "信息录入");
		map2.put("taskCode", "2,3,4,6,7,8,13,14,31");
		map2.put("taskName", "报价");
		if( closeNum == 1 ) {
			map3.put("taskCode", "33");
			map3.put("taskName", "关闭");
		} else {
			map3.put("taskCode", "17,18,19,16");
			map3.put("taskName", "核保");
		}
		if( closeNum == 2 ) {
			map4.put("taskCode", "33");
			map4.put("taskName", "关闭");
		} else {
			map4.put("taskCode", "20");
			map4.put("taskName", "支付");
		}
		map6.put("taskCode", "27");
		map6.put("taskName", "承保");
		map7.put("taskCode", "28,23,25,26");
		map7.put("taskName", "打单");
		map8.put("taskCode", "24");
		map8.put("taskName", "配送");
		map9.put("taskCode", "33");
		map9.put("taskName", "完成");
		temp.add(map);
		temp.add(map2);
		if( closeNum == 1 ) {
			temp.add(map3);
		} else if( closeNum == 2 ) {
			temp.add(map3);
			temp.add(map4);
		} else {
			temp.add(map3);
			temp.add(map4);
			if (flag) {
				Map<String, String> map5 = new HashMap<String, String>();
				map5.put("taskCode", "21");
				map5.put("taskName", "二次支付");
				temp.add(map5);
			}
			temp.add(map6);
			temp.add(map7);
			temp.add(map8);
			temp.add(map9);
		}
		return temp;
	}

	private boolean isThereSecondPayment(Map<String, Object> taskMap) {
		String instanceid = (String) taskMap.get("instanceid");
		String maininstanceid = (String) taskMap.get("maininstanceid");
		String taskcode = (String) taskMap.get("taskcode");
		String inscomcode = (String) taskMap.get("inscomcode");
		if (instanceid.equals(maininstanceid) && (taskcode.equals("27") || taskcode.equals("24"))) {
			// instanceid =
			// workflowsubService.getInstanceidByMaininstanceId(maininstanceid);
			INSBQuotetotalinfo temp = new INSBQuotetotalinfo();
			temp.setTaskid(maininstanceid);
			temp = quotetotalinfoDao.selectOne(temp);
			INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
			quoteinfo.setInscomcode(inscomcode);
			quoteinfo.setQuotetotalinfoid(temp.getId());
			quoteinfo = quoteinfoDao.selectOne(quoteinfo);
			instanceid = quoteinfo.getWorkflowinstanceid();
		}
		return workflowsubtrackDao.isThereSecondPayment(maininstanceid, instanceid);
	}

	/**
	 * 查询我的历史任务信息
	 */
	@Override
	public Map<String, Object> queryMyHistorytask(INSBQueryhistoryinfo insbqueryhistoryinfo, INSCUser inscuser) {
		Map<String, Object> result = new HashMap<String, Object>();
		Integer lim = insbqueryhistoryinfo.getLimit();
		Map<String, Object> params = BeanUtils.toMap(insbqueryhistoryinfo);
		params.put("loginUserName", inscuser.getUsercode());// 登陆者id
		params.put("offset", (insbqueryhistoryinfo.getCurrentpage() - 1) * lim);// 偏移量
		List<Map<String, Object>> lists = insbWorkflowmaintrackDao.queryMyHistorytask(params);
		for (Map<String, Object> taskMap : lists) {
			// 非认证任务展示流程图
			if (!"999".equals((String) taskMap.get("maintaskcode"))) {
				boolean flag = isThereSecondPayment(taskMap);
//				taskMap.put("workFlowIndex", OrganizeWorkFlowInfo((String) taskMap.get("taskcode"), flag));
				Map<String, String> map = OrganizeWorkFlowInfo((String) taskMap.get("maininstanceid"), 
						(String) taskMap.get("instanceid"), (String) taskMap.get("maintaskstate"), 
						(String) taskMap.get("taskstate"), (String) taskMap.get("maintaskcode"), 
						(String) taskMap.get("taskcode"), flag);
				
				taskMap.put("workFlowIndex", map.get("flag"));
				if ("15".equals((String) taskMap.get("taskcode"))) {
					taskMap.put("workflowinfoList", GetWorkFlowInfosXB(flag, Integer.parseInt(map.get("closeNum"))));
				} else {
					taskMap.put("workflowinfoList", GetWorkFlowInfos(flag, Integer.parseInt(map.get("closeNum"))));
				}
			}
			// 获取查看详情信息
			String taskid = (String) taskMap.get("maininstanceid");
			String inscomcode = (String) taskMap.get("inscomcode");
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("taskid", taskid);
			map.put("inscomcode", inscomcode);
			INSBOrder order = insbOrderDao.selectOrderByTaskId(map);
			Map<String,Object> mapid = new HashMap<String,Object>();
			if( order != null ) {
				// 获取页面传参的id
				String orderid =order.getId();
				mapid.put("orderid", orderid);
			}
			List<INSBPolicyitem> insbPolicyitemList = insbPolicyitemDao.selectPolicyitemByInscomTask(map);
			if( insbPolicyitemList != null && insbPolicyitemList.size() > 0 ) {
				// 获取页面传参的pid
			String pid = insbPolicyitemList.get(0).getId();
			mapid.put("pid", pid);
			}
			taskMap.putAll(mapid);
		}	
		long totalSize= insbWorkflowmaintrackDao.queryMyHistorytasknum(params);
		result.put("orderManageList", lists);
		result.put("currentPage", insbqueryhistoryinfo.getCurrentpage());
		result.put("totalPages", totalSize % lim == 0 ? totalSize / lim : (totalSize / lim + 1));
		result.put("totalSize", totalSize);
		//System.out.println(totalSize);
		return result;
	}
	
	@Override
	public void addTrackdetail(INSBWorkflowmaintrack param,String operator){
		INSBWorkflowmaintrackdetail model =new INSBWorkflowmaintrackdetail();
		try {
			INSBWorkflowmaintrackdetail lastmodel = insbWorkflowmaintrackdetailDao.copyTaskFeed4CompletedState(param.getInstanceid());
			if(null!=lastmodel && "Proccessing".equals(lastmodel.getOperatorstate())){
				LogUtil.debug(param.getTaskcode()+"任务已经被打开="+param.getInstanceid());
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
	        if(StringUtil.isEmpty(operator)){//如果操作人为空，则设置为系统
	        	model.setOperator("admin");
	        }
	        if(StringUtil.isEmpty(model.getOperator())){//如果为空，则设置为系统
	        	model.setFromoperator("admin");
	        }
	        insbWorkflowmaintrackdetailDao.insert(model);
		} catch (Exception e) {
			LogUtil.error("新增打开任务轨迹组装参数异常="+param.getInstanceid());
			e.printStackTrace();
		}
	}
}