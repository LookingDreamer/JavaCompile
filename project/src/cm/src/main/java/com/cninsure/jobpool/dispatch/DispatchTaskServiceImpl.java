package com.cninsure.jobpool.dispatch;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.DispatchService;
import com.cninsure.jobpool.Pool;
import com.cninsure.jobpool.PoolService;
import com.cninsure.jobpool.Task;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.dao.INSCMessageDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCMessage;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCUserService;
import com.common.*;
import com.common.redis.CMRedisClient;
import com.common.redis.Constants;
import com.zzb.cm.controller.vo.NotReadMessageVo;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.service.INSBFlowerrorService;

import com.zzb.cm.service.INSBNewMessagePushService;
import com.zzb.conf.dao.*;

import com.zzb.conf.entity.INSBCertification;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBCertificationService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowsubService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 派发 控制器 对任务进行派发工作
 * 
 * @author askqvn
 *
 */

@Repository
@Transactional
//@PropertySource(value = { "classpath:config/config.properties" })
public class DispatchTaskServiceImpl implements DispatchTaskService {
	@Resource
	private PoolService poolService;
	@Resource
	private INSBWorkflowsubDao workflowsubDao;
	@Resource
	private INSBWorkflowsubtrackDao workflowsubtrackDao;
	@Resource
	private INSBWorkflowmainDao workflowmainDao;
	@Resource
	private INSBWorkflowmainService workflowmainService;
	@Resource
	private INSBWorkflowsubService workflowsubService;
	@Resource
	private INSBFlowerrorService insbflowerrorService;
	@Resource
	private INSCUserService inscUserService; 
	@Resource
	private INSCMessageDao messageDao;
	@Resource
	private INSBQuotetotalinfoDao quotetotalinfoDao;
	@Resource
	private INSBGroupmembersDao insbgroupmembersDao;
	@Resource
	private INSBWorkflowmaintrackDao workflowmaintrackDao;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private INSBCertificationService insbCertificationService;
	@Resource
	private INSBQuoteinfoDao quoteinfoDao;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private DispatchService dispatchService;
	@Resource
	private INSBNewMessagePushService insbNewMessagePushService;
	
	private static String DISPATCH_HOST = "";
	static {
		// 读取相关的配置  
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("config/config");
		DISPATCH_HOST = resourceBundle.getString("dispatch.hostName");
	}

	@Override
	public void dispatchTask(Task task){
		// 得到有能力处理的业管组
		List<String> groupIds = poolService.dispathGroupByTask(task);

		if(null!=task && "认证任务".equals(task.getTaskName())){
			if (null == groupIds || groupIds.isEmpty()) {
				LogUtil.info("认证任务自动分配到组失败，没有配置组insbCertificationid="+task.getProInstanceId());
				return;
			}else{
				task.setGroup(groupIds.get(0));
				INSBCertification insbCertification = new INSBCertification();
				insbCertification.setId(task.getProInstanceId());
				insbCertification.setDesignatedgroup(groupIds.get(0));
				insbCertificationService.updateById(insbCertification);
			}
			task.setTaskcode(TaskConst.VERIFY_0);
			dispatching(task,task.getTaskTracks(),DateUtil.getCurrentTime("YYYY-MM-dd HH:mm:ss"));
			LogUtil.info(groupIds.get(0)+"groupId认证任务自动分配到组insbCertificationid="+task.getProInstanceId());
			return;//认证任务直接处理完就返回

		}else{
			Map<String, String> tempParam = new HashMap<String, String>();
			tempParam.put("inscomcode", task.getPrvcode());
			tempParam.put("taskid", task.getProInstanceId());
			List<String> deptIds= quoteinfoDao.selectDeptIdByQuotetotalIdAndComCode4Task(tempParam);
			if (deptIds != null && !deptIds.isEmpty()) {
				String deptId  = deptIds.get(0);
				INSCDept dept = deptDao.selectByComcode(deptId);
				if (dept != null && StringUtil.isNotEmpty(dept.getDeptinnercode()) &&
						dept.getDeptinnercode().startsWith("02012") && "打单".equals(task.getTaskName())) {
					LogUtil.info("0===任务调度+maintaskid=" + task.getProInstanceId() + "+taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode() + "是山东打单任务，不进行调度");
					return;
				}
			}

			LogUtil.info("1===任务调度查找并分配业管组+开始+maintaskid=" + task.getProInstanceId() + "+taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode());
			// 默认分配到第一个业管组，若业管组数大于二并给出提示此任务存在俩个以上的业管组可处理任务
			if (null == groupIds || groupIds.isEmpty()) {//没有业管组处理问题，给出提示此任务无业管处理
				insbflowerrorService.insertInsbFlowerror(task.getProInstanceId(), task.getPrvcode(), "99", task.getTaskName()+"任务找不到业管组", " ", "NotFindGroup", "NotFindGroup", "NotFindGroup", "admin");
				LogUtil.info("3===任务没有业管组处理+maintaskid="+task.getProInstanceId()+"+taskName="+task.getTaskName()+"+prv="+task.getPrvcode());
				return ;
			}else if(groupIds.size()>1){//存在c俩个以上的业管组可处理任务
				insbflowerrorService.insertInsbFlowerror(task.getProInstanceId(), task.getPrvcode(), "98", task.getTaskName()+"任务业管组不唯一", " ", "MoreGroups", "MoreGroups", "MoreGroups", "admin");
				LogUtil.info("4===任务查找到多个业管组+maintaskid="+task.getProInstanceId()+"+taskName="+task.getTaskName()+"+prv="+task.getPrvcode()+"+groupIds"+groupIds );
			}

			// 得到第一个群组
			LogUtil.info("get===任务调度查找业管没分配到人默认第一组，得到业管组集合+maintaskid="+task.getProInstanceId()+"+taskName="+task.getTaskName()+"+prv="+task.getPrvcode()+"+groupId"+groupIds.get(0) );
			// 设置任务属性
			task.setGroup(groupIds.get(0));
			String priorUser = "";
			String tempTime = "";
			String createTime = null;

			//操作最近处理该任务的业管
			if (null !=task.getSonProInstanceId()  && !"".equals(task.getSonProInstanceId())) {
				INSBWorkflowsub subModel =  workflowsubDao.selectByInstanceId(task.getSonProInstanceId());
				List<Map<String,Object>> subtracks =  workflowsubtrackDao.selectAllTrack(task.getSonProInstanceId());
				for(Map<String,Object> sub:subtracks){//找出最近的一个人工节点operator
					if(StringUtil.isEmpty(tempTime)){
						if(null!=sub.get("createtime")){
							tempTime = sub.get("createtime").toString();
						}
						if(null!=(sub.get("operator"))&&!"".equals(sub.get("operator"))&&!"admin".equals(sub.get("operator"))){
							priorUser = sub.get("operator").toString();
						}
					}else{
						if(tempTime.compareTo(sub.get("createtime").toString())<0&&null!=(sub.get("operator"))&&!"".equals(sub.get("operator"))&&!"admin".equals(sub.get("operator"))){
							priorUser = sub.get("operator").toString();
						}
					}
					if(null!=subModel&&subModel.getTaskcode().equals(sub.get("taskcode"))){
						if(null==createTime){
							createTime = sub.get("createtime").toString();//得到阶段任务最开始创建时间
						}
					}
				}
				if(null!=subModel&&(null==subModel.getOperator()||StringUtil.isEmpty(subModel.getOperator()))
						&&StringUtil.isEmpty(subModel.getGroupid())){
					LogUtil.info("***===任务调度查找业管组成功更新子流程maintaskid="+task.getProInstanceId()+"-taskName="+task.getTaskName()+"-prv="+task.getPrvcode() );
					updateWorkFlowSub(task, "Ready", "setGroup", "admin");
					if(StringUtil.isEmpty(task.getTaskcode())){
						task.setTaskcode(subModel.getTaskcode());
					}
				}else{
					LogUtil.info(subModel+"=mainModel***===任务调度查找业管组maintaskid="+task.getProInstanceId()+"taskName="+task.getTaskName()+"prv="+task.getPrvcode()+"自动分组失败 ");
				}
			} else {
				INSBWorkflowmain mainModel =  workflowmainDao.selectByInstanceId(task.getProInstanceId());
				List<Map<String,Object>> maintracks =  workflowmaintrackDao.selectAllTrack(task.getProInstanceId());
				for(Map<String,Object> main:maintracks){//找出最近的一个人工节点operator
					if(StringUtil.isEmpty(tempTime)){
						tempTime = main.get("createtime").toString();
						if(!"".equals(main.get("operator"))&&!"admin".equals(main.get("operator"))){
							priorUser = main.get("operator").toString();
						}
					}else{
						if(tempTime.compareTo(main.get("createtime").toString())<0&&null!=(main.get("operator"))&&!"".equals(main.get("operator"))&&!"admin".equals(main.get("operator"))){
							priorUser = main.get("operator").toString();
						}
					}
					if(null!=mainModel&&mainModel.getTaskcode().equals(main.get("taskcode"))){
						if(null==createTime){
							createTime = main.get("createtime").toString();//得到阶段任务最开始创建时间
						}
					}
				}
				if(null!=mainModel&&(null==mainModel.getOperator()||StringUtil.isEmpty(mainModel.getOperator()))
						&&StringUtil.isEmpty(mainModel.getGroupid())){
					LogUtil.info("***===任务调度查找业管组成功更新主流程maintaskid="+task.getProInstanceId()+"-taskName="+task.getTaskName()+"-prv="+task.getPrvcode() );
					updateWorkFlowMain(task, "Ready", "setGroup", "admin");
					if(StringUtil.isEmpty(task.getTaskcode())){
						task.setTaskcode(mainModel.getTaskcode());
					}
				}else{
					LogUtil.info(mainModel+"=mainModel***===任务调度查找业管maintaskid="+task.getProInstanceId()+"taskName="+task.getTaskName()+"prv="+task.getPrvcode()+"自动分组失败" );
				}
			}
			if(null == createTime){
				createTime = DateUtil.getCurrentTime("YYYY-MM-dd HH:mm:ss");
			}
			Pool.addOrUpdate(task);
			LogUtil.info(priorUser+createTime+"=dis任务调度查找并分配业管组+发起请求+maintaskid=" + task.getProInstanceId() + "+taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode());
			dispatching(task,priorUser,createTime);//DateUtil.toDateTimeString(createTime)
		}
	}
	/**
	 * 加入任务到调度中心
	 * @param task
	 */
	private void dispatching(Task task,String priorUser,String createTime){
		//发给调度服务分配任务
		String path = DISPATCH_HOST+"/task/add";
		Map<String, Object> dispatch = new HashMap<String,Object>();
		if(!TaskConst.QUOTING_7.equals(task.getTaskcode())&&!TaskConst.QUOTING_8.equals(task.getTaskcode())
				&&!TaskConst.VERIFYING_18.equals(task.getTaskcode())&&!TaskConst.PAYINGSUCCESS_27.equals(task.getTaskcode())
				&&!TaskConst.UNDERWRITESUCCESS_23.equals(task.getTaskcode())&&!TaskConst.PAYINGSUCCESS_SECOND_21.equals(task.getTaskcode())
				&&!TaskConst.STARTPOST_24.equals(task.getTaskcode())&&!TaskConst.VERIFY_0.equals(task.getTaskcode())){
			LogUtil.info(task.getTaskcode()+"任务code不需要自动分配任务taskid="+task.getProInstanceId());
			return;
		}
		if(TaskConst.PAYING_20.equals(task.getTaskcode())||TaskConst.PAYINGSUCCESS_SECOND_21.equals(task.getTaskcode())){//二支任务支付任务不需要优先处理人
			LogUtil.info(task.getTaskcode()+"任务code不需要优先任务分配人taskid="+task.getProInstanceId());
			priorUser = "";
		}
		dispatch.put("assignGroupId", task.getGroup());
		//子流程任务
		if (null !=task.getSonProInstanceId()  && !"".equals(task.getSonProInstanceId())) {
			dispatch.put("taskId", task.getSonProInstanceId());
			dispatch.put("subWfId", task.getSonProInstanceId());
		}else{//主流程任务
			dispatch.put("taskId", task.getProInstanceId());
		}
		dispatch.put("mainWfId", task.getProInstanceId());
		dispatch.put("assignWorker", "");
		dispatch.put("created", createTime);
		dispatch.put("priorWorker", priorUser);
		dispatch.put("insCode", task.getPrvcode());
		dispatch.put("relativeTask", task.getTaskRelated());

		String result;
		try {
			result = HttpSender.doPost(path, JSONObject.fromObject(dispatch).toString());
			LogUtil.info(dispatch+"=dispatchparam,send===任务调度分配请求结果taskid=" + task.getProInstanceId() + "+result=" + result + "taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode());
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info(e.getMessage()+"msg,send===任务调度分配请求访问异常taskid=" + task.getProInstanceId() + "+taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode());
		}
	}
	/**
	 * 更新主流程节点
	 */
	private void updateWorkFlowMain(Task task, String taskStatus,String operatorState, String fromOperator) {
		LogUtil.info("****1===更新主流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+开始");

		INSBWorkflowmain workflowModel = workflowmainService.selectByInstanceId(task.getProInstanceId());
		LogUtil.info("****2===更新主流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+oldmainmodel="+workflowModel);

		workflowModel.setTaskstate(taskStatus);
		workflowModel.setModifytime(new Date());
		workflowModel.setOperatorstate(operatorState);
		if (null != task.getDispatchUser()&&!StringUtil.isEmpty(task.getDispatchUser())) {
			try {
					workflowModel.setOperator(task.getDispatchUser());
			} catch (Exception e) {
				LogUtil.info("****3===更新主流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+更新操作人异常=");
				workflowModel.setOperator(null);
				e.printStackTrace();
			}
			if (task.getGroup() != null&&!StringUtil.isEmpty(task.getGroup())) {
				LogUtil.info("****4===更新主流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+更新群组+group="+task.getGroup());
				workflowModel.setGroupid(task.getGroup());
			}
		} else {
			LogUtil.info("****5===更新主流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+未分配人=");
			workflowModel.setOperator(null);
			if (task.getGroup() != null&&!StringUtil.isEmpty(task.getGroup())) {
				LogUtil.info("****6===更新主流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+更新群组+group="+task.getGroup());
				workflowModel.setGroupid(task.getGroup());
			}
		}
		LogUtil.info("****7===更新主流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+newmainmodel="+workflowModel);
		
		//当前人工结点结束时不更新
		INSBWorkflowmain tModel = workflowmainDao.selectByInstanceId(workflowModel.getInstanceid());
		if(null!=tModel){
			if(!StringUtils.isEmpty(tModel.getTaskstate())){
				if(!tModel.getTaskstate().equals("Closed")&&!tModel.getTaskstate().equals("Completed")){
					workflowmainService.updateWorkFlowMainData(workflowModel, fromOperator);
				}
			}
		}
	}

	/**
	 * 更新子流程节点
	 */
	private void updateWorkFlowSub(Task task, String taskStatus,String operatorState, String fromOperator) {
		INSBWorkflowsub workflowModel = workflowsubDao.selectModelByInstanceId(task.getSonProInstanceId());
	
		if("Closed".equals(workflowModel.getTaskstate())){
			return;
		}
		
		LogUtil.info("****1===更新子流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+当前结点信息+subModel="+workflowModel);
			LogUtil.info("****2===更新子流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+开始");
			workflowModel.setModifytime(new Date());
			workflowModel.setTaskstate(taskStatus);
			workflowModel.setOperatorstate(operatorState);
			if (null != task.getDispatchUser()&&!StringUtil.isEmpty(task.getDispatchUser())) {
				try {
					if(null!=task.getDispatchUser()&&!StringUtil.isEmpty(task.getDispatchUser())){
						LogUtil.info("****3===更新子流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+已经分配人+operator="+task.getDispatchUser());
						workflowModel.setOperator(task.getDispatchUser());
					}else{
						LogUtil.info("****4===更新子流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+已经分配人+operator="+task.getDispatchUser());
						workflowModel.setOperator(null);
					}
					
				} catch (Exception e) {
					LogUtil.info("****5===更新子流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+已经分配人+异常");
					workflowModel.setOperator(null);
					e.printStackTrace();
				}
				if (null != task.getGroup()&&!StringUtil.isEmpty(task.getGroup())) {
					LogUtil.info("****6===更新子流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+分配组+group="+task.getGroup());
					workflowModel.setGroupid(task.getGroup());
				}
			} else {
				workflowModel.setOperator(null);
				if (null != task.getGroup()&&!StringUtil.isEmpty(task.getGroup())) {
					LogUtil.info("****7===更新子流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+分配组+group="+task.getGroup());
					workflowModel.setGroupid(task.getGroup());
				}
			}
			LogUtil.info(workflowModel.getOperator()+"=getDispatchUser****===8更新子流程信息+maintaskid=" + task.getProInstanceId() + "+taskname="+task.getTaskName()+"+prv="+task.getPrvcode()+"+newsubmodel="+workflowModel);
			
			//当前人工结点结束时不更新
			INSBWorkflowsub tModel = workflowsubDao.selectByInstanceId(workflowModel.getInstanceid());
			if(null!=tModel){
				if(!StringUtils.isEmpty(tModel.getTaskstate())){
					if(!tModel.getTaskstate().equals("Closed")&&!tModel.getTaskstate().equals("Completed")){
						workflowsubService.updateWorkFlowSubData(workflowModel, fromOperator);					}
				}
			}
	}

	@Override
	public void completeTask(Task task,String endTask){ 
		LogUtil.info("****1===移除任务---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---开始 " );
		if(!"certificateDel".equals(endTask)){
			if(null!=task.getSonProInstanceId()){
				Task task1 = Pool.get(null, task.getSonProInstanceId(), task.getPrvcode());
				if(null!=task1&&task.getTaskName().equals(task1.getTaskName())){//&&task.getTaskName().equals(task1.getTaskName())且任务当前状态一致
					task = task1;
					Pool.del(null, task.getSonProInstanceId(), task.getPrvcode());
					LogUtil.info("****2===移除任务---子流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---结束 " );
				}else{
					LogUtil.info("****21===移除任务---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"-状态不一致 " );
					return ;
				}
//				if(StringUtil.isEmpty(task.getGroup())){//删除任务如果组为空则查询出当前任务的业管组设置给task
//					if(null!=workflowsubDao.selectByInstanceId(task.getSonProInstanceId()))
//						task.setGroup(workflowsubDao.selectByInstanceId(task.getSonProInstanceId()).getGroupid());
//				}
			}else {
				Task task1 = Pool.get(task.getProInstanceId(), null, task.getPrvcode());
				if(null!=task1&&task.getTaskName().equals(task1.getTaskName())){//&&task.getTaskName().equals(task1.getTaskName())且任务当前状态一致
					task = task1;
					Pool.del(task.getProInstanceId(), null, task.getPrvcode());
					LogUtil.info("****3===移除任务---主流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---结束 " );
				}else{
					LogUtil.info("****11===移除任务---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"-状态不一致" );
					return ;
				}
//				if(StringUtil.isEmpty(task.getGroup())){//删除任务如果组为空则查询出当前任务的业管组设置给task
//					if(null!=workflowmainDao.selectByInstanceId(task.getProInstanceId()))
//						task.setGroup(workflowmainDao.selectByInstanceId(task.getProInstanceId()).getGroupid());
//				}
				if("报价".equals(task.getTaskName())){//如果还在主流程还在报价状态，则处理子任务必须删除(增加)
					List<INSBWorkflowsub> soninstances = workflowsubDao.selectSubModelByMainInstanceId(task.getProInstanceId());
					for(INSBWorkflowsub soninstance:soninstances){
						Task sontask = new Task();
						sontask.setProInstanceId(soninstance.getMaininstanceid());
						sontask.setSonProInstanceId(soninstance.getInstanceid());
						sontask.setGroup(soninstance.getGroupid());
						taskthreadPool4workflow.execute(new Runnable() {
							@Override
							public void run() {
								LogUtil.info("关闭流程主流程下的子任务"+sontask.getSonProInstanceId());
								completeTask(sontask, "");
							}
						});
					}
				}
			}
		}
		//发给调度服务移除任务
		String path = DISPATCH_HOST+"/task/del";
		Map<String, String> dispatch = new HashMap<String,String>();
//		if(StringUtil.isEmpty(task.getGroup())){
//			LogUtil.info("**15===任务调度没有分配到相应业管组不需通知调度删除+maintaskid=" + task.getProInstanceId()+ "+groups="+task.getGroup());
//		}else{
			//dispatch.put("assignGroupId", task.getGroup());
			//子流程任务
			if (null !=task.getSonProInstanceId()  && !"".equals(task.getSonProInstanceId())) {
				dispatch.put("taskId", task.getSonProInstanceId());
				dispatch.put("subWfId", task.getSonProInstanceId());
			}else{//主流程任务
				dispatch.put("taskId", task.getProInstanceId());
			}
			dispatch.put("mainWfId", task.getProInstanceId());
			if(StringUtil.isNotEmpty(endTask))
				dispatch.put("assignWorker", task.getDispatchUser());//如果不是改派任务则需要传任务指派人完成任务
			dispatch.put("created", DateUtil.getCurrentDate(DateUtil.Format_DateTime));
			dispatch.put("priorWorker", task.getTaskTracks());
			dispatch.put("insCode", task.getPrvcode());
			
			String result;
			try {
				result = HttpSender.doPost(path, JSONObject.fromObject(dispatch).toString());
				LogUtil.info(JSONObject.fromObject(dispatch).toString()+"param**5===任务调度结束请求结果result="+result+"+maintaskid=" + task.getProInstanceId() + "+taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode());
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.info(e.getMessage()+"msg**6===任务调度结束请求访问异常maintaskid=" + task.getProInstanceId() + "+taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode());
			}
//		}
	}
	
	@Override
	public void deleteTask(Task task) throws WorkFlowException {   
		//TODO 发给调度服务移除任务
		String path = DISPATCH_HOST+"/task/del";
		Map<String, String> dispatch = new HashMap<String,String>();
		String result;
		
		//子流程任务
		if (null !=task.getSonProInstanceId()  && !"".equals(task.getSonProInstanceId())) {
			dispatch.put("taskId", task.getSonProInstanceId());
			dispatch.put("subWfId", task.getSonProInstanceId());
			dispatch.put("mainWfId", task.getProInstanceId());
			dispatch.put("created", DateUtil.getCurrentDate(DateUtil.Format_DateTime));
			dispatch.put("insCode", task.getPrvcode());
			try {
				
				Pool.del(null, task.getSonProInstanceId(), task.getPrvcode());
				LogUtil.info("****2===移除任务---子流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---结束 " );
				
				result = HttpSender.doPost(path, JSONObject.fromObject(dispatch).toString());
				
				LogUtil.info(JSONObject.fromObject(dispatch).toString()+"param**5===任务调度结束请求结果result="+result+"+maintaskid=" + task.getProInstanceId() + "+taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode());
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.info(e.getMessage()+"msg**6===任务调度结束请求访问异常maintaskid=" + task.getProInstanceId() + "+taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode());
			}
		}else{//主流程任务
			dispatch.put("taskId", task.getProInstanceId());
			dispatch.put("created", DateUtil.getCurrentDate(DateUtil.Format_DateTime));
			dispatch.put("insCode", task.getPrvcode());
			dispatch.put("mainWfId", task.getProInstanceId());
			try {
				Pool.del(task.getProInstanceId(), null, task.getPrvcode());
				LogUtil.info("****3===移除任务---主流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---结束 " );
				
				result = HttpSender.doPost(path, JSONObject.fromObject(dispatch).toString());
				LogUtil.info(JSONObject.fromObject(dispatch).toString()+"param**5===任务调度结束请求结果result="+result+"+maintaskid=" + task.getProInstanceId() + "+taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode());
				/*INSBWorkflowmaintrackdetail lastmodel = insbWorkflowmaintrackdetailDao.copyTaskFeed4CompletedState(task.getProInstanceId());
				if(null!=lastmodel && "Proccessing".equals(lastmodel.getOperatorstate())){
					INSCUser user = inscUserService.getByUsercode(lastmodel.getOperator());
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							// 得到系统用户发送消息
							INSCUser fromUser = inscUserService.getByUsercode("admin");
							try {
								LogUtil.info("**===任务关闭业管---向打开任务业管发送消息---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
								messageManager(fromUser, user, task.getProInstanceId(),task.getTaskName(),"taskDel@"+"车牌："+(null==carinfo?"XXXX":carinfo.getCarlicenseno())+"，车主："+(null==carinfo?"XXX":carinfo.getOwnername())+" 的报价，该报价将自动关闭并移除");
								LogUtil.info("**===任务关闭业管---向打开任务业管发送xmpp消息成功,maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
					
							} catch (Exception e) {
								LogUtil.error("***===任务关闭发送xmpp消息失败--向业管发送消息异常---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
								e.printStackTrace();
								//throw e;把异常丢给外层处理
							}
							Pool.addOrUpdate(task);
						}
					});
				}*/
				List<INSBWorkflowsub> soninstances = workflowsubDao.selectSubModelByMainInstanceId(task.getProInstanceId());
				if(soninstances !=null){
					for(INSBWorkflowsub soninstance:soninstances){
						final Task sontask = new Task();
						sontask.setProInstanceId(soninstance.getMaininstanceid());
						sontask.setSonProInstanceId(soninstance.getInstanceid());
						sontask.setGroup(soninstance.getGroupid());
						sontask.setTaskName(soninstance.getTaskname());
						sontask.setPrvcode(soninstance.getProviderId());
						taskthreadPool4workflow.execute(new Runnable() {
							@Override
							public void run() {
								LogUtil.info("关闭流程主流程下的子任务"+sontask.getSonProInstanceId());
								deleteTask(sontask);
							}
						});
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.info(e.getMessage()+"msg**6===任务调度结束请求访问异常maintaskid=" + task.getProInstanceId() + "+taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode());
			}
		}
	}

	@Override
	public void userLoginForTask(String workerid){
		//发给调度服务用户上线
		String path = DISPATCH_HOST+"/worker/online";
		String result;
		try {
			Map<String, Object> worker = new HashMap<String,Object>();
			List<String> usercodes = new ArrayList<String>();
			usercodes.add(workerid);
			List<String> groups = insbgroupmembersDao.selectGroupIdsByUserCodes4Login(usercodes);
			INSCUser user = inscUserService.getByUsercode(workerid);
			if(null==groups||groups.isEmpty()){
				LogUtil.info("**0===业管没有业管组不需要通知任务调度user=" + workerid + "+groups="+groups);
			}else{
				if(null!=user){
					worker.put("groups", groups);
					worker.put("workerId", user.getUsercode());
					worker.put("workerName", user.getUsercode());
					worker.put("isLogin", "true");
					worker.put("maxTaskNum", user.getTaskpool()>0?user.getTaskpool():20);//默认20个为业管最大任务数
					//再次判断用户是否在线，不在线则通知调度用户不在线。
					if(null!=user.getOnlinestatus()&&"1".equals(user.getOnlinestatus().toString())){
						path = DISPATCH_HOST + "/worker/online";//在线的用户用在线的接口通知调度
						worker.put("isLogin", true);
					}else if(null!=user.getOnlinestatus()&&"2".equals(user.getOnlinestatus().toString())){
						path = DISPATCH_HOST + "/worker/busy";//在线的用户用在线的接口通知调度
						worker.put("isLogin", true);
					}else{
						path = DISPATCH_HOST + "/worker/offline";//不在线的用户用下线通知的接口通知调度
						worker.put("isLogin", false);
					}
					result = HttpSender.doPost(path, JSONObject.fromObject(worker).toString());
					
					LogUtil.info(path+"=path,1===任务调度userLoginForTask请求调度+workerId=" + workerid+result+"=result");
				}else{
					LogUtil.info("**1===任务调度userLoginForTask请求调度+user=null+" + workerid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("**2===任务调度userLoginForTask请求调度异常失败result=null+workerId=" + workerid);
		}
	}
	
	@Override
	public void usersLoginForTask(String[] userCodes) {
		String path = DISPATCH_HOST+"/worker/onlines";
		String result;
		try {
			List<String> usercodes = new ArrayList<String>();
			List<Map<String, Object>> requestParam = new ArrayList<Map<String, Object>>();
			Map<String, Object> param = null;
			for(int i=0;i<userCodes.length;i++){
				usercodes.removeAll(usercodes);//清空
				usercodes.add(userCodes[i]);
				List<String> groups = insbgroupmembersDao.selectGroupIdsByUserCodes4Login(usercodes);
				if(null==groups||groups.isEmpty()){
					LogUtil.info("**0===业管没有业管组不需要通知任务调度user=" + userCodes[i]);
					continue;
				}
				param = new HashMap<String, Object>();
				INSCUser user = inscUserService.getByUsercode(userCodes[i]);
				if(null!=user){
					param.put("maxTaskNum", user.getTaskpool()>0?user.getTaskpool():20);//默认20个为业管最大任务数
					param.put("groups", groups);
					//再次判断用户是否在线，不在线则通知调度用户不在线。
					if(null!=user.getOnlinestatus()&&"1".equals(user.getOnlinestatus().toString())){
						path = DISPATCH_HOST + "/worker/online";//在线的用户用在线的接口通知调度
						param.put("isLogin", true);
					}else if(null!=user.getOnlinestatus()&&"2".equals(user.getOnlinestatus().toString())){
						path = DISPATCH_HOST + "/worker/busy";//在线的用户用在线的接口通知调度
						param.put("isLogin", true);
					}else{
						path = DISPATCH_HOST + "/worker/offline";//不在线的用户用下线通知的接口通知调度
						param.put("isLogin", false);
					}
					param.put("workerId", user.getUsercode());
					requestParam.add(param);
				}else{
					LogUtil.info("**1===任务调度usersLoginForTask请求调度+user=null+" + userCodes[i]);
				}
			}
			result = HttpSender.doPost(path, JSONArray.fromObject(requestParam).toString());
			LogUtil.info(path+"**1===任务调度usersLoginForTask请求调度result="+result+"+userCodes=" + userCodes);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("**2===任务调度usersLoginForTask请求调度异常失败result=null+userCodes=" + userCodes);
		}
	}
	
	@Override
	public void userLogoutForTask(String workerid){
		//发给调度服务用户下线
		String path = DISPATCH_HOST+"/worker/offline";
		String result;
		try {
			Map<String, Object> worker = new HashMap<String,Object>();
//			List<String> usercodes = new ArrayList<String>();
//			usercodes.add(workerid);
//			List<String> groups = insbgroupmembersDao.selectGroupIdsByUserCodes4Login(usercodes);
//			if(null==groups||groups.isEmpty()){
//				LogUtil.info("**0===业管没有业管组不需要通知任务调度user=" + workerid);
//			}
//			INSCUser user = inscUserService.getByUsercode(workerid);
//			if(null!=user){
//				worker.put("groups", groups);
				worker.put("workerId", workerid);
				worker.put("workerName", workerid);
				worker.put("isLogin", "false");
				
				result = HttpSender.doPost(path, JSONObject.fromObject(worker).toString());
				
				LogUtil.info("**1===任务调度userLogoutForTask请求调度+workerId=" + workerid+"+result="+result);
//			}else{
//				LogUtil.info("**1===任务调度userLogoutForTask请求调度+user=null+" + workerid);
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("**2===任务调度userLogoutForTask请求调度异常失败result=null+workerId=" + workerid);
		}
	}
	
	@Override
	public void userBusyForTask(String usercode) {
		String path = DISPATCH_HOST+"/worker/busy";
		String result;
		try {
			Map<String, Object> worker = new HashMap<String,Object>();
			worker.put("workerId", usercode);
			worker.put("workerName", usercode);
			worker.put("isLogin", "true");
				
			result = HttpSender.doPost(path, JSONObject.fromObject(worker).toString());
				
			LogUtil.info("**1===任务调度userBusyForTask请求调度+workerId=" + usercode+"+result="+result);
			
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.info("**2===任务调度userBusyForTask请求调度异常失败result=null+workerId=" + usercode);
		}
		
	}
	
	@Override
	public void userRefuseTask(Task task){
		//打回任务，回收任务
		String exception = task.getDispatchUser();
		if(task.getSonProInstanceId()!=null&&StringUtil.isNotEmpty(task.getSonProInstanceId())){
			Task task1 = Pool.get(null, task.getSonProInstanceId(), task.getPrvcode());
			if(null!=task1){
				task = task1;
				Pool.del(null,task.getSonProInstanceId(), task.getPrvcode());
				LogUtil.info("****2===打回任务子流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---结束 " );
			}
			INSBWorkflowsub sub = workflowsubDao.selectByInstanceId(task.getSonProInstanceId());
			if(null!=sub){
				if(StringUtil.isEmpty(task.getGroup())){//删除任务如果组为空则查询出当前任务的业管组设置给task
					task.setGroup(sub.getGroupid());
				}
			}
		}else {
			Task task1 = Pool.get(task.getProInstanceId(), null, task.getPrvcode());
			if(null!=task1){
				task = task1;
				Pool.del(task.getProInstanceId(),null, task.getPrvcode());
				LogUtil.info("****3===打回任务主流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---结束 " );
			}
			INSBWorkflowmain main = workflowmainDao.selectByInstanceId(task.getProInstanceId());
			if(null!=main){
				if(StringUtil.isEmpty(task.getGroup())){//删除任务如果组为空则查询出当前任务的业管组设置给task
					task.setGroup(main.getGroupid());
				}
			}
		}
		//发给调度服务移除任务
		String path = DISPATCH_HOST+"/task/except";
		Map<String, String> dispatch = new HashMap<String,String>();
		if(StringUtil.isEmpty(task.getGroup())){
			LogUtil.info("**15===任务调度没有分配到相应业管组不需通知调度+maintaskid=" + task.getProInstanceId()+ "+groups="+task.getGroup());
		}else{
			dispatch.put("assignGroupId", task.getGroup());
			//子流程任务
			if (null !=task.getSonProInstanceId()  && !"".equals(task.getSonProInstanceId())) {
				dispatch.put("taskId", task.getSonProInstanceId());
				dispatch.put("subWfId", task.getSonProInstanceId());
			}else{//主流程任务
				dispatch.put("taskId", task.getProInstanceId());
			}
			dispatch.put("mainWfId", task.getProInstanceId());
			if(StringUtil.isNotEmpty(exception))//传任务当前操作人
				dispatch.put("exceptWorker", exception);//传任务打回人
			dispatch.put("created", DateUtil.getCurrentDate(DateUtil.Format_DateTime));
			dispatch.put("priorWorker", task.getTaskTracks());
			dispatch.put("insCode", task.getPrvcode());
			
			String result;
			try {
				result = HttpSender.doPost(path, JSONObject.fromObject(dispatch).toString());
				LogUtil.info(dispatch+"**5===打回任务，回收任务 +maintaskid=" + task.getProInstanceId() + "+result=" + result + "+prv=" + task.getPrvcode());
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.info(dispatch+"**6===任务调度结束请求访问异常maintaskid=" + task.getProInstanceId() + "+taskName=" + task.getTaskName() + "+prv=" + task.getPrvcode());
			}
		}
	}
	
	@Override
	public int taskDispatched(Task task) {
		INSCUser user = inscUserService.getByUsercode(task.getDispatchUser());
		if(user == null){
			LogUtil.info(task.getProInstanceId()+"任务无法分配，用户不存在，userocde=" + task.getDispatchUser());
			return 0;
		}
		LogUtil.info(task.getProInstanceId()+"任务分配，"+task.getSonProInstanceId()+"修改数据状态：DispatchUser=" + task.getDispatchUser());
		if(task.getProInstanceId().equals(task.getSonProInstanceId())){//当子任务号与主任务号一致，则是认证任务分配成功
			INSBCertification insbCertification = new INSBCertification();
			insbCertification.setId(task.getProInstanceId());
			insbCertification.setDesignatedoperator(task.getDispatchUser());
			insbCertificationService.updateById(insbCertification);
			LogUtil.info(task.getProInstanceId() + "=id认证任务自动分配成功userocde=" + task.getDispatchUser());
			return 1;
		}else{
			//CallBackDispatchGroup(task, user);
			// 工作流参数
			Map<String, String> param = new HashMap<String, String>();
			param.put("p1", task.getProInstanceId());
			param.put("p2", task.getPrvcode());
			param.put("p3", user.getUsercode());
			// 更新子流程
			if (null != task.getSonProInstanceId()&& !"".equals(task.getSonProInstanceId())) {
				param.put("p4", task.getSonProInstanceId());
				Task task1 = Pool.get(null, task.getSonProInstanceId(), task.getPrvcode());
				if(null!=task1) {
					task.setTaskName(task1.getTaskName());//从缓存获取任务名称
				} else {
					task1 = dispatchService.getDataFromDb(null, task.getSonProInstanceId(), task.getPrvcode());
					task.setTaskName(task1.getTaskName());
				}
				INSBWorkflowsub subModel =  workflowsubDao.selectByInstanceId(task.getSonProInstanceId());
				
				if(null==subModel.getOperator()||StringUtil.isEmpty(subModel.getOperator())){
					LogUtil.info("11任务调度查找业管---更新子流程---maintaskid="+task.getProInstanceId()+"+Operator="+subModel.getOperator()+"+getGroupid="+subModel.getGroupid()+"+DispatchUser="+task.getDispatchUser());
					updateWorkFlowSub(task, "Ready", "autoDispatch", "admin");
					if(TaskConst.QUOTING_7.equals(subModel.getTaskcode())){//查询是否存在其他没有分配到人的人工规则查询任务一起分配给当前任务分配的业管
						List<INSBWorkflowsub> subs = workflowsubDao.selectSubModelByMainInstanceId(subModel.getMaininstanceid());
						for(INSBWorkflowsub sub:subs){
							if(TaskConst.QUOTING_7.equals(sub.getTaskcode())&&(null==sub.getOperator()||StringUtil.isEmpty(sub.getOperator()))){//必须其他的子任务是人工规则报价，且没有安排处理业管
								task.setSonProInstanceId(sub.getInstanceid());
								updateWorkFlowSub(task, "Ready", "autoDispatch", "admin");
								LogUtil.info("110存在其他没有分配到人的人工规则查询任务一起分配给当前任务分配的业管。子流程号：instanceid="+sub.getInstanceid());
							}
						}
					}
				}else if(StringUtil.isNotEmpty(subModel.getOperator())&&!"admin".equals(subModel.getOperator())&&!"core".equals(subModel.getOperator())
						&&!TaskConst.CLOSE_37.equals(subModel.getTaskcode())&&!TaskConst.END_33.equals(subModel.getTaskcode())&&!TaskConst.VERIFYINGFAILED_30.equals(subModel.getTaskcode())){
					INSCUser operator = inscUserService.getByUsercode(subModel.getOperator());//当前任务处理人
					if(!"1".equals(String.valueOf(operator.getOnlinestatus())) && !"2".equals(String.valueOf(operator.getOnlinestatus()))){//如果当前处理人不在线，是1为在线，2为忙碌
						LogUtil.info("10可能需要重新调度分配maintaskid="+task.getProInstanceId()+"+Operator="+subModel.getOperator()+"+getGroupid="+subModel.getGroupid()+"+DispatchUser="+task.getDispatchUser());
						return 0;//还需要再次调度
					}else if(!task.getDispatchUser().equals(subModel.getOperator())){
						int i = CMRedisClient.getInstance().atomicIncr(Constants.CM_GLOBAL, task.getProInstanceId() + "startedRetry");
						LogUtil.info("101getDispatchUser是(%s)任务处理分配人不相同当前是subModelgetOperator(%s)，可能还需要再次分配",task.getDispatchUser(),subModel.getOperator(),i);
						if(i == 10)
							CMRedisClient.getInstance().expire(Constants.CM_GLOBAL, task.getSonProInstanceId() + "startedRetry", 30);
						if(i<=10){
							return 0;
						}else{
							return 2;
						}
					}else{
						LogUtil.info("102任务已经手动分配或已经是需要删除的订单-maintaskid="+task.getProInstanceId()+"+Operator="+subModel.getOperator()+"+getGroupid="+subModel.getGroupid()+"+instanceid="+subModel.getInstanceid());
						return 2;
					}
				}else{
					LogUtil.info("12任务已经手动分配或已经是需要删除的订单-maintaskid="+task.getProInstanceId()+"+Operator="+subModel.getOperator()+"+getGroupid="+subModel.getGroupid()+"+instanceid="+subModel.getInstanceid());
					return 2;
				}
			
				// 更新主流程
			} else {
				param.put("p4", task.getProInstanceId());
				Task task1 = Pool.get(task.getProInstanceId(), null, task.getPrvcode());
				if(null!=task1) {
					task.setTaskName(task1.getTaskName());//从缓存获取任务名称
				} else {
					task1 = dispatchService.getDataFromDb(task.getProInstanceId(), null, task.getPrvcode());
					task.setTaskName(task1.getTaskName());
				}
				INSBWorkflowmain mainModel =  workflowmainDao.selectByInstanceId(task.getProInstanceId());
				if(null==mainModel.getOperator()||StringUtil.isEmpty(mainModel.getOperator())){
					LogUtil.info("21=====任务调度查找业管---更新主流程---maintaskid="+task.getProInstanceId()+"+Operator="+mainModel.getOperator()+"+getGroupid="+mainModel.getGroupid()+"+DispatchUser="+task.getDispatchUser());
					updateWorkFlowMain(task, "Ready", "autoDispatch", "admin");
				}else if(StringUtil.isNotEmpty(mainModel.getOperator())&&!"admin".equals((mainModel.getOperator()))&&!"core".equals((mainModel.getOperator()))
						&&!TaskConst.CLOSE_37.equals(mainModel.getTaskcode())&&!TaskConst.END_33.equals(mainModel.getTaskcode())&&!TaskConst.VERIFYINGFAILED_30.equals(mainModel.getTaskcode())){
					INSCUser operator = inscUserService.getByUsercode(mainModel.getOperator());//当前任务处理人
					if(!"1".equals(String.valueOf(operator.getOnlinestatus())) && !"2".equals(String.valueOf(operator.getOnlinestatus()))){//如果当前处理人不在线，是1为在线，2为忙碌
						LogUtil.info("20可能需要重新调度分配maintaskid="+task.getProInstanceId()+"+Operator="+mainModel.getOperator()+"+getGroupid="+mainModel.getGroupid()+"+DispatchUser="+task.getDispatchUser());
						return 0;//还需要再次调度
					}else if(!task.getDispatchUser().equals(mainModel.getOperator())){
						int i = CMRedisClient.getInstance().atomicIncr(Constants.CM_GLOBAL, task.getProInstanceId() + "startedRetry");
						LogUtil.info("201getDispatchUser是(%s)任务处理分配人不相同当前是mainModelgetOperator(%s)，可能还需要再次分配,此次i=%s",task.getDispatchUser(),mainModel.getOperator(),i);
						if(i == 10)
							CMRedisClient.getInstance().expire(Constants.CM_GLOBAL, task.getProInstanceId() + "startedRetry", 30);
						if(i<=10){
							return 0;
						}else{
							return 2;
						}
					}else{
						LogUtil.info("202任务已经手动分配或已经是需要删除的订单-maintaskid="+task.getProInstanceId()+"+Operator="+mainModel.getOperator()+"+getGroupid="+mainModel.getGroupid()+"+instanceid="+mainModel.getInstanceid());
						return 2;
					}
				}else{
					LogUtil.info("22任务已经手动分配或已经是需要删除的订单状态-maintaskid="+task.getProInstanceId()+"+Operator="+mainModel.getOperator()+"+getGroupid="+mainModel.getGroupid()+"+instanceid="+mainModel.getInstanceid());
					return 2;
				}
			}
			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					//通知工作流
					try {
						LogUtil.info("3***===任务调度查找业管---通知工作流---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
						String wStr = WorkFlowUtil.claimTaskWorkflow(param.get("p1"),param.get("p2"), param.get("p3"), param.get("p4"));
						// 工作流内部异常
						@SuppressWarnings("unchecked")
						Map<String, String> wMap = JSONObject.fromObject(wStr);
						if ("fail".equals(wMap.get("message"))) {
							LogUtil.info("***4===任务调度查找业管---通知工作流失败---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---reson="+wMap.get("reason"));
							throw new WorkFlowException(wMap.get("reason"));
						}
					} catch (ParseException | IOException e) {
						LogUtil.info("***===5http异常-maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
						// http异常全部包装成工作流异常抛出
						throw new WorkFlowException(e.getMessage());
					}
				}
			});
		}
		taskthreadPool4workflow.execute(new Runnable() {
			@Override
			public void run() {
				// 得到系统用户发送消息
				INSCUser fromUser = inscUserService.getByUsercode("admin");
				try {
					LogUtil.info(task.getDispatchUser()+"**===6任务调度查找业管---向业管发送消息---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
					messageManager(fromUser, user, task.getProInstanceId(),task.getTaskName(),"taskmsg");
					LogUtil.info("**===7发送xmpp消息成功,maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
		
				} catch (Exception e) {
					LogUtil.error("***===8发送xmpp消息失败--向业管发送消息异常---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
					e.printStackTrace();
					//throw e;把异常丢给外层处理
				}
				Pool.addOrUpdate(task);
			}
		});
		LogUtil.info("***===9任务调度查找业管---向redis回写数据---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
		return 1;
	}
	private void messageManager(INSCUser fromUser, INSCUser toUser,	String MainInstanceId, String TaskName, String msgtype) {
		LogUtil.info("*******111========向业管发送任务消息=======结点名称："+TaskName);
		String taskId = MainInstanceId;
		String codeName = TaskName;
		
		
		// 查询当前代理人 拿到车牌号
		Map<String, String> agentMap =new HashMap<String,String>();
		agentMap = quotetotalinfoDao.selectAgentDataByTaskId(MainInstanceId);
		// 向业管发送任务消息
		try {
			if (!agentMap.isEmpty()) {
				String plateStr = "";
				if (agentMap.get("platenumber") != null) {
					plateStr = agentMap.get("platenumber");
				}
				// 因为taskName
				String[] arrayUser = { "人工回写", "人工核保", "支付", "二次支付确认", "人工承保",	"打单", "配送", "人工规则报价", "人工报价" };
				for (String codeTempName : arrayUser) {
					if (codeTempName.equals(codeName)) {
						// 保存业管任务消息
						saveMessageData(fromUser.getUsercode(),toUser.getUsercode(), plateStr + " " + taskId + " "+ codeName, plateStr + " " + taskId+ " " + codeName + " 请处理");
						LogUtil.info("*******222========向业管发送任务消息=======保存消息完成");
					}
				}
				sendXmppMessage4User(fromUser, toUser, msgtype);
			}
		} catch (Exception e) {
			LogUtil.info("*******333========向业管发送任务消息失败======="+e.getMessage());
			throw e;//把异常丢给外层处理
		}
	}
	private void saveMessageData(String fromUser, String toUser,String content, String title) {
		LogUtil.info("====11向业管发送消息,保存消息数据---开始---fromUser=" + fromUser+"---touser="+toUser);

		INSCMessage messageModel = new INSCMessage();
		messageModel.setCreatetime(new Date());
		messageModel.setMsgcontent(content);
		messageModel.setMsgtitle(title);
		messageModel.setOperator("admin");
		messageModel.setReceiver(toUser);
		messageModel.setSender(fromUser);
		messageModel.setSendtime(DateUtil.getCurrentDate(DateUtil.Format_DateTime));
		messageModel.setStatus(0);
		messageModel.setState(1);
		messageDao.insert(messageModel);
		LogUtil.info("====22向业管发送消息---fromUser=---保存消息数据=" + fromUser + messageModel + "---touser=" + toUser);
	}
	/**
	 * 发送任务消息到业管  
	 * 
	 * @param fromUser
	 * @param toUser
	 */
	public void sendXmppMessage4User(INSCUser fromUser, INSCUser toUser,String msgType) {
		String messageChannelConfig = ConfigUtil.getPropString("cm.message.pushchannel", "openfire");//add by hewc for websocket 20170720
		if("websocket".equals(messageChannelConfig)){
			insbNewMessagePushService.sendMessage(fromUser,toUser,msgType);
		}else { //add by hewc for websocket 20170720
			LogUtil.info(msgType + "====1向业管发送消息---fromUser=" + fromUser.getUsercode() + "---touser=" + toUser);
			String xmppUserName = toUser.getUsercode().toLowerCase() + "@" + ValidateUtil.getConfigValue("fairy.serviceName");
			LogUtil.info(msgType + "====2向业管发送消息---fromUser=" + "xmppUserName=" + xmppUserName + fromUser.getUsercode() + "---touser=" + toUser);

			NotReadMessageVo message = new NotReadMessageVo();
			message.setFromPeople(fromUser.getUsercode());
			message.setOperator(fromUser.getUsercode());
			message.setReciever(toUser.getUsercode());
			message.setDateTime(DateUtil.getCurrentDate(DateUtil.Format_DateTime));
			if (msgType.contains("@")) {
				message.setMsgtype(msgType.split("@")[0]);
				message.setContent(msgType.split("@")[1]);
			} else {
				message.setMsgtype(msgType);
				message.setContent("");
			}

			JSONObject jsonMessage = JSONObject.fromObject(message);
			XMPPUtils xmppInstance = XMPPUtils.getInstance();
			try {
				xmppInstance.sendMessage(xmppUserName, jsonMessage.toString());
				LogUtil.info("====3向业管发送消息---fromUser=" + fromUser.getUsercode() + "---touser=" + toUser + "---message=" + jsonMessage.toString());
			} catch (Exception e) {
				LogUtil.info("====4向业管发送消息异常=---fromUser=" + fromUser.getUsercode() + "---touser=" + toUser);
			}
		}
	}
	@Override
	public int reclyDispatched(Task task) {
		// TODO 回收任务。将任务关联的处理业管清空
		String opreator = task.getDispatchUser();
		task.setDispatchUser("");
		// 更新主流程
		INSBWorkflowmain mainModel =  new INSBWorkflowmain();
		mainModel.setOperator(opreator);//用需要回收任务的业管未条件去查询此时业管签收的主任务
		mainModel.setReclystate("Recly");//需要回收的任务
		List<INSBWorkflowmain> mainModels =  workflowmainDao.selectList(mainModel);
		for(INSBWorkflowmain main:mainModels){
			if(!"Completed".equals(main.getTaskstate())&&!"Closed".equals(main.getTaskstate())&&!"end".equals(main.getTaskstate())){
				 LogUtil.info("=====1任务调度回收业管签收的主任务---更新主流程---maintaskid="+main.getInstanceid());
				 task.setProInstanceId(main.getInstanceid());
				 task.setGroup(main.getGroupid());
				 updateWorkFlowMain(task, "Ready", "reclyDispatch", "admin");
				 if(StringUtil.isNotEmpty(main.getGroupid())){//组不为空则再加入调度
					 //将任务重新加入到调度中心
					 LogUtil.info("=====主任务调度回收加入任务到调度中心---maintaskid="+task.getProInstanceId());
					 if(StringUtil.isEmpty(task.getTaskcode())){
						 task.setTaskcode(main.getTaskcode());
					 }
					 dispatching(task,opreator,DateUtil.toDateTimeString(main.getCreatetime()));
				 }else{
					 LogUtil.info("=====主任务调度回收加入任务到调度中心失败，没有业管组。maintaskid="+task.getProInstanceId());
				 }
			}
		}
		// 回收更新子流程
		INSBWorkflowsub subModel = new INSBWorkflowsub();
		subModel.setOperator(opreator);//用需要回收任务的业管未条件去查询此时业管签收的子任务
		subModel.setReclystate("Recly");//需要回收的任务
		List<INSBWorkflowsub> subModels =  workflowsubDao.selectList(subModel);
		for(INSBWorkflowsub sub:subModels){
			 if(!"Completed".equals(sub.getTaskstate())&&!"Closed".equals(sub.getTaskstate())&&!"end".equals(sub.getTaskstate())){
				 LogUtil.info("=====1任务调度回收业管签收的子任务---更新子流程---maintaskid="+sub.getMaininstanceid());
				 task.setProInstanceId(sub.getMaininstanceid());
				 task.setSonProInstanceId(sub.getInstanceid());
				 task.setGroup(sub.getGroupid());
				 updateWorkFlowSub(task, "Ready", "reclyDispatch", "admin");
				 if(StringUtil.isNotEmpty(sub.getGroupid())){//组不为空则再加入调度
					//将任务重新加入到调度中心
					LogUtil.info(task.getSonProInstanceId()+"=====子任务调度回收加入任务到调度中心---maintaskid="+task.getProInstanceId());
					if(StringUtil.isEmpty(task.getTaskcode())){
						 task.setTaskcode(sub.getTaskcode());
					 }
					dispatching(task,opreator,DateUtil.toDateTimeString(sub.getCreatetime()==null?(new Date()):sub.getCreatetime()));
				 }else{
					 LogUtil.info("=====子任务调度回收加入任务到调度中心失败，没有业管组。maintaskid="+task.getProInstanceId());
				 }
			}
		}
		return 1;
	}
	
}
