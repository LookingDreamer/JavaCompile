package com.cninsure.jobpool;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.common.*;
import com.zzb.cm.service.INSBNewMessagePushService;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCMessageDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.entity.INSCMessage;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.controller.vo.NotReadMessageVo;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.conf.dao.INSBGroupmembersDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowsubService;

/**
 * 派发 控制器 对任务进行派发工作
 * 
 * @author hxx  
 *
 */

@Repository
@Transactional
@PropertySource(value = { "classpath:config/config.properties" })
public class DispatchServiceImpl implements DispatchService {
	
	@Resource
	private PoolService poolService;
	@Resource
	private INSCCodeDao codeDao;
	@Resource
	private INSCUserDao userDao;
	@Resource
	private INSBWorkflowmainService workflowmainService;
	@Resource
	private INSBWorkflowsubService workflowsubService;
	@Resource
	private DispatchTaskService dispatchTaskService;//akka任务调度中心cm通知服务
	@Resource
	private INSBWorkflowsubDao workflowsubDao;
	@Resource
	private INSBWorkflowmainDao workflowmainDao;
	@Resource
	private INSBGroupmembersDao groupmembersDao;
	@Resource
	private INSCMessageDao messageDao;
	@Resource
	private INSBQuotetotalinfoDao quotetotalinfoDao;
	@Resource
	private INSBNewMessagePushService insbNewMessagePushService; //add by hewc for websocket 20170720
	
	/**
	 * 所有task 分配调度
	 * 
	 * 执行嵌套事务
	 * 
	 * @throws WorkFlowException
	 * @throws java.text.ParseException
	 */
	@Override
	public void dispatchAll(Task paramTask) {
		LogUtil.info("*******===任务调度查找业管---maintaskid=" + paramTask.getProInstanceId() + "---taskname="+paramTask.getTaskName()+"---prv="+paramTask.getPrvcode()+"---start");
		dispatchUser(paramTask);
		LogUtil.info("*******===任务调度查找业管---maintaskid=" + paramTask.getProInstanceId() + "---taskname="+paramTask.getTaskName()+"---prv="+paramTask.getPrvcode()+"---分配人结束");
	}

	/**
	 * 1：为任务分配人 1.1：成功 1.1.2 更新cm流程信息 1.1.3 回调工作流
	 *
	 * 1.2：失败
	 * 
	 * @param task
	 * @return
	 * @throws WorkFlowException
	 * @throws java.text.ParseException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private int dispatchUser(Task task)throws WorkFlowException {
		LogUtil.info("*******===任务调度查找业管---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---开始");
		
		INSCUser user = poolService.dispathUserByTask(task);
		LogUtil.info("*******===任务调度查找业管---得到业管---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---usercode="+user);

		// 得到派发人
		if (user != null) {
			// 得到派发信息
			task.setDispatchUser(user.getUsercode());
			String tempTrack = user.getUsercode()+"_"+task.getTaskTracks();
			task.setTaskTracks(tempTrack);
			task = calculateWeight(task);
			
			// 回写组信息
			CallBackDispatchGroup(task, user);

			// 工作流参数
			Map<String, String> param = new HashMap<String, String>();
			param.put("p1", task.getProInstanceId());
			param.put("p2", task.getPrvcode());
			param.put("p3", user.getUsercode());

			// 更新子流程
			if (task.getSonProInstanceId() != null&& !"".equals(task.getSonProInstanceId())) {
				param.put("p4", task.getSonProInstanceId());
				
				INSBWorkflowsub subModel =  workflowsubDao.selectByInstanceId(task.getSonProInstanceId());
				if(StringUtil.isEmpty(subModel.getOperator())){
					if(subModel.getOperatorstate()==null){
						LogUtil.info("=====任务调度查找业管---更新子流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
						updateWorkFlowSub(task, "Ready", "autoDispatch", "admin");
					}else if(!subModel.getOperatorstate().equals("pauseTask")){
						LogUtil.info("=====任务调度查找业管---更新子流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
						updateWorkFlowSub(task, "Ready", "autoDispatch", "admin");
					}
					
				}else{
					LogUtil.info("=====任务调度查找业管---更新子流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---任务已经手动分配人");
					return 0;
				}
			
				// 更新主流程
			} else {
				param.put("p4", task.getProInstanceId());
				INSBWorkflowmain mainModel =  workflowmainDao.selectByInstanceId(task.getProInstanceId());
				if(mainModel.getOperator()==null){
					LogUtil.info("=====任务调度查找业管---更新主流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
					updateWorkFlowMain(task, "Ready", "autoDispatch", "admin");
				}else{
					LogUtil.info("=====任务调度查找业管---更新主流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---任务已经手动分配人");
					return 0;
				}
			}
			
			//通知工作流
			try {
				LogUtil.info("***===任务调度查找业管---通知工作流---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
				String wStr = WorkFlowUtil.claimTaskWorkflow(param.get("p1"),param.get("p2"), param.get("p3"), param.get("p4"));
				// 工作流内部异常
				@SuppressWarnings("unchecked")
				Map<String, String> wMap = JSONObject.fromObject(wStr);
				if ("fail".equals(wMap.get("message"))) {
					LogUtil.info("***===任务调度查找业管---通知工作流失败---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---reson="+wMap.get("reason"));
					throw new WorkFlowException(wMap.get("reason"));
				}
			} catch (ParseException | IOException e) {
				LogUtil.info("***===任务调度查找业管---通知工作流失败---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---http异常");
				// http异常全部包装成工作流异常抛出
				throw new WorkFlowException(e.getMessage());
			}

			// 得到系统用户发送消息
			INSCUser fromUser = userDao.selectByUserCode("admin");
			try {
				LogUtil.error("**===任务调度查找业管---向业管发送消息---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
				messageManagerByTask(fromUser, user, task);
				LogUtil.error("**===任务调度查找业管---向业管发送消息---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---发送xmpp消息成功");

			} catch (Exception e) {
				LogUtil.error("***===任务调度查找业管---向业管发送消息异常---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---发送xmpp消息失败");
//				e.printStackTrace();
			}
			Pool.addOrUpdate(task);
			LogUtil.info("***===任务调度查找业管---向redis回写数据---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());

		} else {
			
			// 默认第一个业管组
			dispatchGroup(task);
			
			// 更新子流程
			if (task.getSonProInstanceId() != null&& !"".equals(task.getSonProInstanceId())) {
							
				INSBWorkflowsub subModel =  workflowsubDao.selectByInstanceId(task.getSonProInstanceId());
				if(null==subModel.getOperator()||StringUtil.isEmpty(subModel.getOperator())){
					LogUtil.info("***===任务调度查找业管---更新子流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode() );
					updateWorkFlowSub(task, "Ready", null, "admin");
				}else{
					LogUtil.info("***===任务调度查找业管---更新子流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---已经分人放弃自动分组 ");
					return 0;
				}
			// 更新主流程
			} else {
							
				INSBWorkflowmain mainModel =  workflowmainDao.selectByInstanceId(task.getProInstanceId());
				if(null==mainModel.getOperator()||StringUtil.isEmpty(mainModel.getOperator())){
					LogUtil.info("***===任务调度查找业管---更新主流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode() );
					updateWorkFlowMain(task, "Ready", null, "admin");
				}else{
					LogUtil.info("***===任务调度查找业管---更新主流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---已经分人放弃自动分组 " );
					return 0;
				}
			}
			Pool.addOrUpdate(task);
			LogUtil.info("**===任务调度查找业管---结束---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode() );
		}
		return 1;
	}

	/**
	 * 没分配到人，默认第一组
	 * 
	 * @param task
	 * @return
	 */
	private Task dispatchGroup(Task task) {
		LogUtil.info("***===任务调度查找业管---没分配到人默认第一组---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode() );
		// 得到有能力处理的业管组
		List<String> groupIds = poolService.dispathGroupByTask(task);
		LogUtil.info("***===任务调度查找业管---没分配到人默认第一组---得到业管组集合---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---groupIds"+groupIds );
		if (groupIds == null || groupIds.isEmpty()) {
			return null;
		}
		// 得到第一个群组
		LogUtil.info("***===任务调度查找业管---没分配到人默认第一组---得到业管组集合---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---groupId"+groupIds.get(0) );
		// 设置任务属性
		task.setGroup(groupIds.get(0));
		// 计算权重
		task = calculateWeight(task);
		return task;
	}

	/**
	 * 分配到人回写任务组
	 * 
	 * @param user
	 * @return
	 */
	private Task CallBackDispatchGroup(Task task, INSCUser user) {
		LogUtil.info("***===任务调度查找业管---回写业管组---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---开始 " );
		List<String> paramLIst = new ArrayList<String>();
		paramLIst.add(user.getUsercode());
		List<String> groupIds = groupmembersDao.selectGroupIdsByUserCodes4Task(paramLIst);
		LogUtil.info("***===任务调度查找业管---得到业管所属业管组---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---当前业管所属群组--groupids="+groupIds );

		// 得到第一个群组
		LogUtil.info("***===任务调度查找业管---得到业管所属业管组---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---当前业管所属群组第一个--groupid="+groupIds.get(0) );
			// 设置任务属性
		task.setGroup(groupIds.get(0));
		return task;
	}

	/**
	 * 回收
	 * 
	 * @param task
	 * @param recycleUser
	 * @return
	 */
	public Task recycle(Task task, INSCUser recycleUser) {
		String dispatch = task.getDispatchUser();
		if (dispatch == null) {
			return task;
		}else{
			task.setDispatchUser(null);
		}
		task = calculateWeight(task);
		return task;
	}

	/**
	 * 
	 * 节点完成出池
	 * 
	 * @param task
	 */
	@Override
	public void removTaskFromPool(Task task) {
		LogUtil.info("****===移除任务---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---开始 " );

		if(task.getSonProInstanceId()!=null){
			Pool.del(null,task.getSonProInstanceId(), task.getPrvcode());
			LogUtil.info("****===移除任务---子流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---结束 " );
		}else {
			Pool.del(task.getProInstanceId(),null, task.getPrvcode());
			LogUtil.info("****===移除任务---主流程---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---结束 " );
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RedisException.class })
	public void pauseTask(String instanceId, String providerId,int instanceType, String fromOperator) throws RedisException {
		LogUtil.info("****===暂停任务---maintaskid=" + instanceId + "---providerId="+providerId+"---开始");
		Task task = null;
		if (instanceType == 1) {
			task = Pool.get(instanceId, null, providerId);
			if (null==task) {
				LogUtil.info("****===暂停任务---maintaskid=" + instanceId + "---providerId="+providerId+"---redis异常");
				task = getDataFromDb(instanceId, null, providerId);
				task.setDispatchflag(false);
				Pool.addOrUpdate( task);
			} else{
				task.setDispatchflag(false);
			}
			
			INSBWorkflowmain mainModel = workflowmainService.selectByInstanceId(instanceId);
			if(null==mainModel.getOperator()||StringUtils.isEmpty(mainModel.getOperator())){
				LogUtil.info("****===暂停任务---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---主流程暂停");
				updateWorkFlowMain(task, "Ready", "pauseTask", fromOperator);
			}

		} else {
			task = Pool.get( null, instanceId, providerId);
			if (null==task) {
				LogUtil.info("****===暂停任务---maintaskid=" + instanceId + "---providerId="+providerId+"---redis异常");
				task = getDataFromDb(null, instanceId, providerId);
				task.setDispatchflag(false);
				Pool.addOrUpdate(task);
			} else{
				task.setDispatchflag(false);
			}
			INSBWorkflowsub subModel = workflowsubDao.selectByInstanceId(instanceId);
			if(null==subModel.getOperator()||StringUtils.isEmpty(subModel.getOperator())){
				LogUtil.info("****===暂停任务---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---子流程暂停");
				updateWorkFlowSub(task, "Ready", "pauseTask", fromOperator);
			}
			
		}
		task.setDispatchUser("");
		dispatchTaskService.completeTask(task,"");//暂停任务，先统一处理调度中心删除任务，待重启任务再加入任务
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RedisException.class })
	public void restartTask(String instanceId, String providerId,String userCode, int instanceType, String fromOperator)throws WorkFlowException, RedisException {
		LogUtil.info("****===重启任务---maintaskid=" + instanceId + "---providerId="+providerId+"---开始");
		// 得到的task
		Task myTask = new Task();
		if (instanceType == 1) {
			myTask = Pool.get( instanceId, null, providerId);
			if (myTask == null) {
				LogUtil.info("****===重启任务---maintaskid=" + instanceId + "---providerId="+providerId+"---redis查询数据为空");
				myTask = getDataFromDb(instanceId, null, providerId);
				Pool.addOrUpdate(myTask);
			}
			updateWorkFlowMain(myTask, "Ready", "restartTask", fromOperator);
			LogUtil.info("****===重启任务---maintaskid=" + instanceId + "---providerId="+providerId+"---结束---开始重新自动分人");
			dispatchTaskService.dispatchTask(myTask);
			
		} else {
			myTask = Pool.get( null, instanceId, providerId);
			if (myTask == null) {
				LogUtil.info("****===重启任务---maintaskid=" + instanceId + "---providerId="+providerId+"---redis查询数据为空");
				myTask = getDataFromDb(null, instanceId, providerId);
				Pool.addOrUpdate(myTask);

			}
			myTask.setDispatchflag(true);
			updateWorkFlowSub(myTask, "Ready", "restartTask", fromOperator);
			LogUtil.info("****===重启任务---maintaskid=" + instanceId + "---providerId="+providerId+"---结束---开始重新自动分人");
			dispatchTaskService.dispatchTask(myTask);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {WorkFlowException.class, RedisException.class })
	public void reassignment(String instanceId, String providerId,int instanceType, INSCUser fromUser, INSCUser toUser)throws WorkFlowException, RedisException {
		
		LogUtil.info("****===改派任务---maintaskid=" + instanceId + "---providerId="+providerId+"---开始");
		// 查询一个任务进行操作
		Map<String, String> param = new HashMap<String, String>();
		// 得到的task
		Task myTask = null;
		if (instanceType == 1) {
			myTask = Pool.get( instanceId, null, providerId);

			if (myTask == null) {
				LogUtil.info("****===改派任务---maintaskid=" + instanceId + "---providerId="+providerId+"---redis查询数据为空");
				myTask = getDataFromDb(instanceId, null, providerId);
				String tracks = toUser.getUsercode()+"_"+myTask.getTaskTracks();
				myTask.setDispatchUser(toUser.getUsercode());
				myTask.setTaskTracks(tracks);
				Pool.addOrUpdate(myTask);
			}else{
				dispatchUserByManual(myTask, fromUser, toUser);
			}
			INSBWorkflowmain mainModel = workflowmainDao.selectByInstanceId(instanceId);
			String oldUser = mainModel.getOperator();
			if(TaskConst.END_33.equals(mainModel.getTaskcode())||TaskConst.CLOSE_37.equals(mainModel.getTaskcode())){
				LogUtil.info("****===改派任务---maintaskid=" + instanceId + "---taskcode="+mainModel.getTaskcode()+"任务已结束无需操作。");
				return;
			}
			param.put("p1", myTask.getProInstanceId());
			param.put("p2", myTask.getPrvcode());
			param.put("p3", oldUser);
			param.put("p4", instanceId);
			param.put("p5", toUser.getUsercode());
			// cm后台更新数据库
			updateWorkFlowMain(myTask, "Reserved", "reassignment",fromUser.getUsercode());
			LogUtil.info("****===改派任务---maintaskid=" + instanceId + "---providerId="+providerId+"---更新CM后台主流程完成");
		} else {
			myTask = Pool.get( null, instanceId, providerId);
			if (myTask == null) {
				LogUtil.info("****===改派任务---maintaskid=" + instanceId + "---providerId="+providerId+"---redis查询数据为空");
				myTask = getDataFromDb(null, instanceId, providerId);
				String tracks = toUser.getUsercode()+"_"+myTask.getTaskTracks();
				myTask.setDispatchUser(toUser.getUsercode());
				myTask.setTaskTracks(tracks);
				Pool.addOrUpdate(myTask);
			}else{
				dispatchUserByManual(myTask, fromUser, toUser);
			}
			INSBWorkflowsub subModel = workflowsubDao.selectByInstanceId(instanceId);
			String oldUser = subModel.getOperator();
			if(TaskConst.END_33.equals(subModel.getTaskcode())||TaskConst.CLOSE_37.equals(subModel.getTaskcode())){
				LogUtil.info("****===改派任务(子任务)---instanceId=" + instanceId + "---taskcode="+subModel.getTaskcode()+"任务已结束无需操作。");
				return;
			}
			
			param.put("p1", myTask.getProInstanceId());
			param.put("p2", myTask.getPrvcode());
			param.put("p3", oldUser);
			param.put("p4", instanceId);
			param.put("p5", toUser.getUsercode());
			// 更新cm后台工作流子流程状态信息
			updateWorkFlowSub(myTask, "Reserved", "reassignment",fromUser.getUsercode());
			LogUtil.info("****===改派任务---maintaskid=" + instanceId + "---providerId="+providerId+"---更新CM后台子流程完成");
		}
		// 通知工作流改派任务
		try {
			LogUtil.info("****===改派任务---maintaskid=" + instanceId + "---providerId="+providerId+"---通知工作流----START");

			String wStr = WorkFlowUtil.delegateTaskWorkflow(param.get("p1"),param.get("p2"), param.get("p3"), param.get("p4"),param.get("p5"));
			@SuppressWarnings("unchecked")
			Map<String, String> wMap = JSONObject.fromObject(wStr);
			if ("fail".equals(wMap.get("message"))) {
				LogUtil.info("****===改派任务---maintaskid=" + instanceId + "---providerId="+providerId+"---通知工作流----异常"+wMap.get("reason"));
				throw new WorkFlowException(wMap.get("reason"));
			}
		} catch (ParseException | IOException | WorkFlowException e) {
			LogUtil.info("****===改派任务---maintaskid=" + instanceId + "---providerId="+providerId+"---通知工作流----http异常"+e.getMessage());
			throw new WorkFlowException(e.getMessage());
		}
		boolean taskFlage = Pool.addOrUpdate( myTask);
		if (!taskFlage) {
			LogUtil.info("****===改派任务---maintaskid=" + instanceId + "---providerId="+providerId+"---");
			throw new RedisException("请重试");
		}
		LogUtil.info("****===改派任务---maintaskid=" + instanceId + "---providerId="+providerId+"---通知工作流----向业管发送消息");
		messageManagerByTask(fromUser, toUser, myTask);
		myTask.setDispatchUser("");
		dispatchTaskService.completeTask(myTask, "");//改派任务，统一处理从调度中心删除任务
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {WorkFlowException.class, RedisException.class })
	public void reassignment4NoRedis(String instanceId, String providerId,int instanceType, INSCUser fromUser, INSCUser toUser)throws WorkFlowException, RedisException {
		LogUtil.info("****===改派未入池任务---maintaskid=" + instanceId + "---providerId="+providerId+"---开始");
		// 查询一个任务进行操作
		Map<String, String> param = new HashMap<String, String>();
		if (instanceType == 1) {

			INSBWorkflowmain mainModel = workflowmainDao.selectByInstanceId(instanceId);
			String oldUser = mainModel.getOperator();
			param.put("p1", instanceId);
			param.put("p2", providerId);
			param.put("p3", oldUser);
			param.put("p4", instanceId);
			param.put("p5", toUser.getUsercode());
			param.put("p6", mainModel.getTaskname());
			// cm后台更新数据库
			workflowmainService.updateWorkFlowMainData(mainModel,fromUser.getUsercode());
			LogUtil.info("****===改派未入池任务---maintaskid=" + instanceId + "---providerId="+providerId+"---更新CM主流程信息");


		} else {

			INSBWorkflowsub subModel = workflowsubDao.selectByInstanceId(instanceId);
			LogUtil.info("****===改派未入池任务---maintaskid=" + instanceId + "---providerId="+providerId+"---通过主流程查询子流程信息---subModel="+subModel);

			if(subModel.getTaskcode()!=null){
				Map<String,String> tempResult = codeDao.selectWorkflowNodeNameByCode(subModel.getTaskcode());
				subModel.setTaskname(tempResult.get("codename"));
			}
			String oldUser = subModel.getOperator();
			param.put("p1", subModel.getMaininstanceid());
			param.put("p2", providerId);
			param.put("p3", oldUser);
			param.put("p4", instanceId);
			param.put("p5", toUser.getUsercode());
			param.put("p6", subModel.getTaskname());
			// 更新cm后台工作流子流程状态信息

			subModel.setOperator(toUser.getUsercode());
			subModel.setModifytime(new Date());
			subModel.setTaskstate("Reserved");
			subModel.setOperatorstate("reassignment");
			subModel.setTaskname(null);
			workflowsubService.updateWorkFlowSubData(subModel,fromUser.getUsercode());
			LogUtil.info("****===改派未入池任务---maintaskid=" + instanceId + "---providerId="+providerId+"---更新CM子流程信息");

		}
		// 通知工作流改派任务
		try {

			LogUtil.info("****===改派未入池任务---maintaskid=" + instanceId + "---providerId="+providerId+"---通知工作流");
			String wStr = WorkFlowUtil.delegateTaskWorkflow(param.get("p1"),param.get("p2"), param.get("p3"), param.get("p4"),param.get("p5"));
			@SuppressWarnings("unchecked")
			Map<String, String> wMap = JSONObject.fromObject(wStr);
			if ("fail".equals(wMap.get("message"))) {
				LogUtil.info("****===改派未入池任务---maintaskid=" + instanceId + "---providerId="+providerId+"---通知工作流---工作流内部异常");
				throw new WorkFlowException(wMap.get("reason"));
			}
		} catch (ParseException | IOException | WorkFlowException e) {
			LogUtil.info("****===改派未入池任务---maintaskid=" + instanceId + "---providerId="+providerId+"---通知工作流---http调用异常");
			throw new WorkFlowException(e.getMessage());
		}
		// 发送消息
		try {
			LogUtil.info("****===改派未入池任务---maintaskid=" + instanceId + "---providerId="+providerId+"---向业管发消息---toUser="+toUser);
			messageManager(fromUser, toUser, param.get("p1"),param.get("p6"));
		} catch (Exception e) {
			LogUtil.info("****===改派未入池任务---maintaskid=" + instanceId + "---providerId="+providerId+"---向业管发消息---xmpp发送消息异常");
//			e.printStackTrace();
		}
	}

	/**
	 * 手动改派任务
	 * 
	 * @param task
	 * @param fromUser
	 * @param toUser
	 * @return 返回最后一次任务处理人
	 */
	private String dispatchUserByManual(Task task, INSCUser fromUser,INSCUser toUser) {
		String oldUser = null;
		// 得到轨迹列表
		task.setTaskTracks(toUser.getUsercode()+"_"+task.getTaskTracks());
		task.setDispatchUser(toUser.getUsercode());
		if(task.getTaskTracks()==null||"".equals(task.getTaskTracks())){
			
			return oldUser;
		}
		LogUtil.info("===手动指派任务任务轨迹信息:" + task.getTaskTracks()=="00");
		oldUser = getTaskTracks(task.getTaskTracks())[0];
		LogUtil.info("========手动指派任务当前任务信息:" + task.toString() + "========");
		return oldUser;
	}

	/**
	 * 
	 * 拒绝任务操作redis
	 * 
	 * 
	 * @param task
	 * @param user
	 * @return
	 */
	private void RefuseTask2redis(Task task, INSCUser user) {
		LogUtil.info("****===拒绝任务任务---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---拒绝人---user="+user);
		task.setDispatchUser(null);
		LogUtil.info("========拒绝任务后任务信息修改改为：" + task.toString());
	}

	private void messageManagerByTask(INSCUser fromUser, INSCUser toUser,Task task) {
		messageManager(fromUser, toUser, task.getProInstanceId(),task.getTaskName());
	}

	private void messageManager(INSCUser fromUser, INSCUser toUser,	String MainInstanceId, String TaskName) {
		LogUtil.info("*******========向业管发送任务消息=======结点名称："+TaskName);
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
				String[] arrayUser = { "人工回写", "人工核保", "支付", "二次支付确认", "人工承保",	"打单", "配送", "人工规则报价", "人工报价" };
				for (String codeTempName : arrayUser) {
					if (codeTempName.equals(codeName)) {
						// 保存业管任务消息
						saveMessageData(fromUser.getUsercode(),toUser.getUsercode(), plateStr + " " + taskId + " "+ codeName, plateStr + " " + taskId+ " " + codeName + " 请处理");
						LogUtil.info("*******========向业管发送任务消息=======保存消息完成");
					}
				}
				sendXmppMessage4User(fromUser, toUser);
			}
		} catch (Exception e) {
			LogUtil.info("*******========向业管发送任务消息失败======="+e.getMessage());
//			e.printStackTrace();
		}
	}

	/**
	 * 发送任务消息到业管  
	 * 
	 * @param fromUser
	 * @param toUser
	 */
	private void sendXmppMessage4User(INSCUser fromUser, INSCUser toUser) {
		String messageChannelConfig = ConfigUtil.getPropString("cm.message.pushchannel", "openfire");//add by hewc for websocket 20170720
		if("websocket".equals(messageChannelConfig)){
			insbNewMessagePushService.sendMessage(fromUser,toUser);
		}else { //add by hewc for websocket 20170720
			LogUtil.info("====向业管发送消息---fromUser=" + fromUser.getUsercode() + "---touser=" + toUser);
//		String xmppUserName = (String) com.common.RedisClient.get(toUser.getUsercode().toLowerCase() + "_online");
			String xmppUserName = toUser.getUsercode().toLowerCase() + "@" + ValidateUtil.getConfigValue("fairy.serviceName");
			LogUtil.info("====向业管发送消息---fromUser=" + fromUser.getUsercode() + "---touser=" + toUser + "---xmppUserName=" + xmppUserName);

			NotReadMessageVo message = new NotReadMessageVo();
			message.setFromPeople(fromUser.getUsercode());
			message.setOperator(fromUser.getUsercode());
			message.setReciever(toUser.getUsercode());
			message.setDateTime(getNowDate());
			message.setMsgtype("taskmsg");
			message.setContent("");

			JSONObject jsonMessage = JSONObject.fromObject(message);
			LogUtil.info("====向业管发送消息---fromUser=" + fromUser.getUsercode() + "---touser=" + toUser + "---message=" + jsonMessage.toString());
			XMPPUtils xmppInstance = XMPPUtils.getInstance();
			try {
				xmppInstance.sendMessage(xmppUserName, jsonMessage.toString());
			} catch (Exception e) {
				LogUtil.warn("====向业管发送消息---fromUser=" + fromUser.getUsercode() + "---touser=" + toUser + "---消息异常=");
			}
		}
	}

	private void saveMessageData(String fromUser, String toUser,String content, String title) {
		LogUtil.info("====向业管发送消息---fromUser=" + fromUser+"---touser="+toUser+"---保存消息数据---开始");

		INSCMessage messageModel = new INSCMessage();
		messageModel.setCreatetime(new Date());
		messageModel.setMsgcontent(content);
		messageModel.setMsgtitle(title);
		messageModel.setOperator("admin");
		messageModel.setReceiver(toUser);
		messageModel.setSender(fromUser);
		messageModel.setSendtime(getNowDate());
		messageModel.setStatus(0);
		messageModel.setState(1);
		messageDao.insert(messageModel);
		LogUtil.info("====向业管发送消息---fromUser=" + fromUser+"---touser="+toUser+"---保存消息数据="+messageModel);

	}

	/**
	 * 时间格式化4xmpp
	 * 
	 * @return
	 */
	private String getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {WorkFlowException.class, RedisException.class })
	public void refuseTask(String instanceId, String providerId,int instanceType, INSCUser user) throws WorkFlowException,RedisException {
		LogUtil.info("****===拒绝任务---maintaskid=" + instanceId + "---providerId="+providerId+"---拒绝人---user="+user+"---开始");

		Map<String, String> param = new HashMap<String, String>();
		Task myTask = null;
		if (instanceType == 1) {
			myTask = Pool.get( instanceId, null, providerId);//TODO
			if (myTask == null) {
				LogUtil.info("****instanceType == 1===拒绝任务---maintaskid=" + instanceId + "---providerId="+providerId+"---redis查询数据为空");
				myTask = getDataFromDb(instanceId, null, providerId);
				myTask.setDispatchUser(null);
				Pool.addOrUpdate(myTask);
			}else{
				RefuseTask2redis(myTask, user);
			}
			
			param.put("p1", myTask.getProInstanceId());
			param.put("p2", myTask.getPrvcode());
			param.put("p3", user.getUsercode());
			param.put("p4", instanceId);
			// 更新cm后台工作流状态信息
			updateWorkFlowMain(myTask, "Ready", "refuseTask",user.getUsercode());
			LogUtil.info("****instanceType == 1===拒绝任务---maintaskid=" + instanceId + "---providerId="+providerId+"---更新CM系统主流程数据");

		} else {
			myTask = Pool.get( null, instanceId, providerId);//TODO

			if (myTask == null) {
				LogUtil.info("****===拒绝任务---maintaskid=" + instanceId + "---providerId="+providerId+"---redis查询数据为空");
				myTask = getDataFromDb(null, instanceId, providerId);
				myTask.setDispatchUser(null);
				Pool.addOrUpdate(myTask);
			}
			RefuseTask2redis(myTask, user);
			param.put("p1", myTask.getSonProInstanceId());
			param.put("p2", myTask.getPrvcode());
			param.put("p3", user.getUsercode());
			param.put("p4", instanceId);
			// 更新cm后台工作流子流程状态信息
			updateWorkFlowSub(myTask, "Ready", "refuseTask", user.getUsercode());
			LogUtil.info("****===拒绝任务---maintaskid=" + instanceId + "---providerId="+providerId+"---更新CM系统子流程数据");

		}

		myTask.setDispatchUser(user.getUsercode());
		dispatchTaskService.userRefuseTask(myTask);//拒绝任务，从调度中心回收任务

		if (myTask != null) {
			myTask.setDispatchUser("");//为重启调度作准备。清空此次回退任务的业管信息
			// 调用工作流接口 更新状态
			try {
				LogUtil.info("****===拒绝任务---maintaskid=" + instanceId + "---providerId="+providerId+"---通知工作流");
				String wStr = WorkFlowUtil.releaseTaskWorkflow(param.get("p1"),param.get("p2"), param.get("p3"), param.get("p4"));
				@SuppressWarnings("unchecked")
				Map<String, String> wMap = JSONObject.fromObject(wStr);
				if ("fail".equals(wMap.get("message"))) {
					LogUtil.info("****===拒绝任务---maintaskid=" + instanceId + "---providerId="+providerId+"---工作流内部异常---message="+wMap.get("reason"));
					throw new WorkFlowException(wMap.get("reason"));
				}
			} catch (ParseException | IOException | WorkFlowException e) {
				LogUtil.info("****===拒绝任务---maintaskid=" + instanceId + "---providerId="+providerId+"---http异常---message="+e.getMessage());
				throw new WorkFlowException(e.getMessage());
			}
			
			boolean taskFlage = Pool.addOrUpdate(myTask);
			if (!taskFlage) {
				LogUtil.info("****===拒绝任务---maintaskid=" + instanceId + "---providerId="+providerId+"---状态错误请重试");
				throw new RedisException("请重试");
			}

			if (!"20".equals(myTask.getTaskcode())) {
				LogUtil.info("****===拒绝任务---maintaskid=" + instanceId + "---providerId=" + providerId + "---开启任务自动调度");
				dispatchTaskService.dispatchTask(myTask);//重启调度，准备自动分配任务
			} else {
				LogUtil.info("****===拒绝任务---maintaskid=" + instanceId + "---providerId=" + providerId + "---支付任务，不开启任务自动调度");
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {WorkFlowException.class, RedisException.class })
	public String getTask(String instanceId, String providerId,int instanceType, INSCUser fromUser, INSCUser toUser)throws WorkFlowException, RedisException {

		
		LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---开始---userCode="+fromUser.getUsercode()+"==="+toUser.getUsercode());
		Map<String, String> param = new HashMap<String, String>();
		Task myTask = null;
		if (instanceType == 1) {

			
			myTask = Pool.get( instanceId, null, providerId);//TODO
			if (myTask == null) {
				LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---redis查询数据为空");
				myTask = getDataFromDb(instanceId, null, providerId);
				myTask.setDispatchUser(toUser.getUsercode());
				myTask.setTaskTracks(toUser.getUsercode());
				Pool.addOrUpdate(myTask);
			}else{
				dispatchUserByManual(myTask, fromUser, toUser);
				Pool.addOrUpdate(myTask);
			}
			param.put("p1", myTask.getProInstanceId());
			param.put("p2", myTask.getPrvcode());
			param.put("p3", toUser.getUsercode());
			param.put("p4", instanceId);
			// 更新cm后台工作流状态信息
			updateWorkFlowMain(myTask, "Ready", "getTask",fromUser.getUsercode());
			LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---更新CM主流程信息");
		} else {
			myTask = Pool.get( null, instanceId, providerId);//TODO
			if (myTask == null) {
				LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---redis查询数据为空");
				myTask = getDataFromDb(null, instanceId, providerId);
				myTask.setDispatchUser(toUser.getUsercode());
				myTask.setTaskTracks(toUser.getUsercode());
				Pool.addOrUpdate(myTask);

			}else{
				dispatchUserByManual(myTask, fromUser, toUser);
			}
			LogUtil.info("\n========手动指派任务子流程id:" + instanceId + "========");

			param.put("p1", myTask.getProInstanceId());
			param.put("p2", myTask.getPrvcode());
			param.put("p3", toUser.getUsercode());
			param.put("p4", instanceId);
			// 更新cm后台工作流子流程状态信息
			updateWorkFlowSub(myTask, "Ready", "getTask",fromUser.getUsercode());
			LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---更新CM子流程信息");
		}

		// 重新放入任务池
			try {
				LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---调用工作流---开始");
				String wStr = WorkFlowUtil.claimTaskWorkflow(param.get("p1"),param.get("p2"), param.get("p3"), param.get("p4"));
				@SuppressWarnings("unchecked")
				Map<String, String> wMap = JSONObject.fromObject(wStr);
				if ("fail".equals(wMap.get("message"))) {
					LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---工作流内部异常");
					throw new WorkFlowException(wMap.get("reason"));
				}
			} catch (ParseException | IOException | WorkFlowException e) {
				LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---http异常");
				throw new WorkFlowException(e.getMessage());
			}
			boolean taskFlage = Pool.addOrUpdate( myTask);
			if (!taskFlage) {
				LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---状态异常请重试");
				throw new RedisException("请重试");
			}
			try {
				LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---向业管发送信息");
				messageManagerByTask(fromUser, toUser, myTask);
			} catch (Exception e) {
				LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---xmpp发送消息异常");
				e.printStackTrace();
			}
		LogUtil.info("****===认领任务---maintaskid=" + instanceId + "---providerId="+providerId+"---结束");
		myTask.setDispatchUser("");
		dispatchTaskService.completeTask(myTask, "");//改派任务，统一处理从调度中心删除任务
		return "getTask";
	}

	/**
	 * 权重轮询 TODO
	 * 
	 * @param task
	 * @return
	 */
	private Task calculateWeight(Task task) {
		// 权重信息表
		// Map<String, Integer> weightMap = new HashMap<String, Integer>();
		return task;
	}

	/**
	 * 更新主流程节点
	 */
	private void updateWorkFlowMain(Task task, String taskStatus,String operatorState, String fromOperator) {
		LogUtil.info("****===更新主流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---开始");

		INSBWorkflowmain workflowModel = workflowmainService.selectByInstanceId(task.getProInstanceId());
		LogUtil.info("****===更新主流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---oldmainmodel="+workflowModel);

		workflowModel.setTaskstate(taskStatus);
		workflowModel.setModifytime(new Date());
		workflowModel.setOperatorstate(operatorState);
		if (task.getDispatchUser() != null&&!StringUtil.isEmpty(task.getDispatchUser())) {
			try {
				workflowModel.setOperator(task.getDispatchUser());
			} catch (Exception e) {
				LogUtil.info("****===更新主流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---更新操作人异常=");
				workflowModel.setOperator(null);
				e.printStackTrace();
			}
			if (task.getGroup() != null&&!StringUtil.isEmpty(task.getGroup())) {
				LogUtil.info("****===更新主流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---更新群组---group="+task.getGroup());
				workflowModel.setGroupid(task.getGroup());
			}
		} else {
			LogUtil.info("****===更新主流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---未分配人=");
			workflowModel.setOperator(null);
			if (task.getGroup() != null&&!StringUtil.isEmpty(task.getGroup())) {
				LogUtil.info("****===更新主流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---更新群组---group="+task.getGroup());
				workflowModel.setGroupid(task.getGroup());
			}
		}

		LogUtil.info("****===更新主流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---newmainmodel="+workflowModel);
		
		//当前人工结点结束时不更新
		INSBWorkflowmain tModel = workflowmainDao.selectByInstanceId(workflowModel.getInstanceid());
		if(tModel!=null){
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
	private int updateWorkFlowSub(Task task, String taskStatus,String operatorState, String fromOperator) {
		INSBWorkflowsub workflowModel = workflowsubDao.selectModelByInstanceId(task.getSonProInstanceId());
		LogUtil.info("****===更新子流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---当前结点信息---subModel="+workflowModel);

		task.setTaskcode(workflowModel.getTaskcode());
	
		if("Closed".equals(workflowModel.getTaskstate())){
			return 0;
		}

		LogUtil.info("****===更新子流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---开始");
		workflowModel.setModifytime(new Date());
		workflowModel.setTaskstate(taskStatus);
		workflowModel.setOperatorstate(operatorState);
		if (null !=task.getDispatchUser()  && !StringUtil.isEmpty(task.getDispatchUser())) {
			try {
				if(task.getDispatchUser()!=null&&!StringUtil.isEmpty(task.getDispatchUser())){
					LogUtil.info("****===更新子流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---已经分配人---operator="+task.getDispatchUser());
					workflowModel.setOperator(task.getDispatchUser());
				}else{
					LogUtil.info("****===更新子流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---已经分配人---operator="+task.getDispatchUser());
					workflowModel.setOperator(null);
				}

			} catch (Exception e) {
				LogUtil.info("****===更新子流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---已经分配人---异常");
				workflowModel.setOperator(null);
				e.printStackTrace();
			}
			if (task.getGroup() != null&&!StringUtil.isEmpty(task.getGroup())) {
				LogUtil.info("****===更新子流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---分配组---group="+task.getGroup());
				workflowModel.setGroupid(task.getGroup());
			}
		} else {
			workflowModel.setOperator(null);
			if (task.getGroup() != null&&!StringUtil.isEmpty(task.getGroup())) {
				LogUtil.info("****===更新子流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---分配组---group="+task.getGroup());
				workflowModel.setGroupid(task.getGroup());
			}
		}

		LogUtil.info("****===更新子流程信息---maintaskid=" + task.getProInstanceId() + "---taskname="+task.getTaskName()+"---prv="+task.getPrvcode()+"---newsubmodel="+workflowModel);

		//当前人工结点结束时不更新
		INSBWorkflowsub tModel = workflowsubDao.selectByInstanceId(workflowModel.getInstanceid());
		if(tModel!=null){
			if(!StringUtils.isEmpty(tModel.getTaskstate())){
				if(!tModel.getTaskstate().equals("Closed")&&!tModel.getTaskstate().equals("Completed")){
					workflowsubService.updateWorkFlowSubData(workflowModel, fromOperator);					}
			}
		}
		return 1;
	}
	
	
	

	@Override
	public void completedAllTaskByCore(String mainInstanceId,String instanceid) {
		LogUtil.info("===核心反向关闭流程---maintaskid="+mainInstanceId+"---subtaskid="+instanceid);
		closeSubTask(instanceid,"end");
		closeMainTask(mainInstanceId);
		List<String> subList = workflowsubDao.selectInstanceIdByMainInstanceId(mainInstanceId);
		if(null!=subList&&!subList.isEmpty()){
			subList.remove(instanceid);
			for(String str:subList){
				closeSubTask(str,"Closed");
			}
		}
	}
	
	/**
	 * 反向关闭主流程
	 * @param instanceid
	 */
	private void closeMainTask(String instanceid){
		INSBWorkflowmain mainModel = workflowmainDao.selectByInstanceId(instanceid);
			mainModel.setCreatetime(new Date());
			mainModel.setOperator("core");
			mainModel.setTaskcode("33");
			mainModel.setTaskname("结束");
			mainModel.setTaskstate("end");
			mainModel.setNoti("核心系统反向关闭");
			LogUtil.info("===核心反向关闭流程---taskid="+instanceid+"---type=main"+"---mainModel="+mainModel);
		workflowmainService.updateWorkFlowMainData(mainModel, "core");
	}
	/**
	 * 反向关闭子流程
	 * @param subinstanceid
	 * @param status
	 */
	public void closeSubTask(String subinstanceid,String status){
		INSBWorkflowsub subModel = workflowsubDao.selectByInstanceId(subinstanceid);
		subModel.setCreatetime(new Date());
		subModel.setOperator("core");
		if("end".equals(status)){
			subModel.setTaskcode("33");
			subModel.setTaskname("结束");
			subModel.setTaskstate("end");
		}else if("Completed".equals(status)){
			subModel.setTaskstate("Completed");
		}else if("Closed".equals(status)){
			subModel.setTaskcode("37");
			subModel.setTaskname("关闭");
			subModel.setTaskstate("Closed");
		}else{
			subModel.setTaskcode("37");
			subModel.setTaskname("关闭");
			subModel.setTaskstate("Closed");
		}
		
		subModel.setNoti("核心系统反向关闭");
		LogUtil.info("===核心反向关闭流程---taskid="+subinstanceid+"---type=main"+"---mainModel="+subModel);
		workflowsubService.updateWorkFlowSubData(subModel, "core");
	}
	
	/**
	 * 
	 * 解析订单轨迹
	 * @param tracks
	 * @return
	 */
	private String[] getTaskTracks(String tracks){
		if(null!=tracks&&tracks.length()>0){
			 try {
				return tracks.split("_");
			} catch (Exception e) {
				e.printStackTrace();
				return new String[]{tracks};
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 当redis为空时查询数据库
	 * 
	 * @param mainId 主流程
	 * @param subId 子流程
	 * @param prv 供应商
	 */
	public Task getDataFromDb(String mainId,String subId, String prv){
		LogUtil.info("****===redis异常---查询cm数据库---maintaskid=" + mainId + "---subId="+subId+"---providerId="+prv);

		Task task = new Task();
		task.setPrvcode(prv);
		
		//子流程
		if(subId!=null){
			INSBWorkflowsub subModel = workflowsubDao.selectByInstanceId(subId);
			task.setProInstanceId(subModel.getMaininstanceid());
			task.setSonProInstanceId(subId);
			task.setTaskcode(subModel.getTaskcode());
			if(subModel.getOperator()!=null){
				task.setDispatchUser(subModel.getOperator());
				task.setTaskTracks(subModel.getOperator());
			}
			if(subModel.getGroupid()!=null){
				task.setGroup(subModel.getGroupid());
			}
			Map<String,String> codeMap = codeDao.selectWorkflowNodeNameByCode(subModel.getTaskcode());
			task.setTaskName(codeMap.get("codename"));
			LogUtil.info("****===redis异常---查询cm数据库子流程 " + task);

		}else{
			INSBWorkflowmain mainModel = workflowmainDao.selectByInstanceId(mainId);
			
			task.setProInstanceId(mainModel.getInstanceid());
			task.setTaskcode(mainModel.getTaskcode());
			if(mainModel.getOperator()!=null){
				task.setDispatchUser(mainModel.getOperator());
				task.setTaskTracks(mainModel.getOperator());
			}
			if(mainModel.getGroupid()!=null){
				task.setGroup(mainModel.getGroupid());
			}
			Map<String,String> codeMap = codeDao.selectWorkflowNodeNameByCode(mainModel.getTaskcode());
			task.setTaskName(codeMap.get("codename"));
			LogUtil.info("****===redis异常---查询cm数据库主流程 " + task);
		}
		return task;
	}
}
