package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.common.WorkflowFeedbackUtil;
import com.zzb.cm.service.INSBWorkflowDataService;
import com.lzgapi.order.service.LzgDataTransferService;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import com.zzb.conf.service.INSBRealtimetaskService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.Task;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCUserService;
import com.common.TaskConst;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.conf.component.MessageManager;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackdetailDao;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrackdetail;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.model.WorkFlow4TaskModel;

@Service
@Transactional
public class INSBWorkflowsubServiceImpl extends
		BaseServiceImpl<INSBWorkflowsub> implements INSBWorkflowsubService {
	
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBQuoteinfoDao quoteinfoDao;
	@Resource
	private INSBQuotetotalinfoDao quotetotalinfoDao;
	@Resource
	private INSBWorkflowsubtrackdetailDao workflowsubtrackdetailDao;
	@Resource
	private INSCUserService inscUserService; 
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private MessageManager messageManager;
	@Resource
	private CHNChannelService channelService;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private INSBRealtimetaskService insbRealtimetaskService;
	@Resource
	private DispatchTaskService dispatchTaskService;
	@Resource
	private INSBWorkflowDataService workflowDataService;
	@Resource
	private LzgDataTransferService lzgDataTransferService;

	@Override
	protected BaseDao<INSBWorkflowsub> getBaseDao() {
		return insbWorkflowsubDao;
	}

	public void updateWorkFlowSubData(String mainTaskId, String subTaskId, String taskCode, String taskState, String taskName, String taskFeedBack, String operator) {
		INSBWorkflowsub workflowsubModel = new INSBWorkflowsub();
		workflowsubModel.setMaininstanceid(mainTaskId);
		workflowsubModel.setInstanceid(subTaskId);
		workflowsubModel.setTaskcode(taskCode);
		workflowsubModel.setTaskstate(taskState);
		workflowsubModel.setTaskname(taskName);
		workflowsubModel.setTaskfeedback(taskFeedBack);
		workflowsubModel.setOperator(operator);

		try {
			updateWorkFlowSubData(workflowsubModel, operator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateWorkFlowSubData(INSBWorkflowsub workflowModel,String fromOperator) {
		LogUtil.info(workflowModel.getMaininstanceid()+","+workflowModel.getInstanceid()+"更新子工作流状态："+
				workflowModel.getTaskcode()+","+workflowModel.getTaskstate()+":"+workflowModel.getTaskfeedback()+ " Operator： " + workflowModel.getOperator());

		try {
			String mainInstancId = null;
			if (workflowModel.getMaininstanceid() == null) {
				mainInstancId = insbWorkflowsubDao.selectMainInstanceIdBySubInstanceId(workflowModel.getInstanceid());
				workflowModel.setMaininstanceid(mainInstancId);
			} else {
				mainInstancId = workflowModel.getMaininstanceid();
			}
			LogUtil.info("向代理人发送消息-----开始");
			messageManager.sendMessage4Agent("admin", mainInstancId,workflowModel.getInstanceid(),workflowModel.getTaskcode());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 判断表中是否存在
		Map<String, String> oldMap = insbWorkflowsubDao.selectByInstanceId4Task(workflowModel.getInstanceid());
		if (oldMap != null && !oldMap.isEmpty()) {
			if ("Closed".equalsIgnoreCase(oldMap.get("taskstate"))) {
				LogUtil.error(workflowModel.getMaininstanceid()+","+workflowModel.getInstanceid()+"流程轨迹已关闭，本次不再处理");
				return;
			}
		}

		//获取备注、操作人
		if (StringUtil.isEmpty(workflowModel.getTaskfeedback())) {
			Map<String, String> rdValue = WorkflowFeedbackUtil.getWorkflowFeedback(workflowModel.getMaininstanceid(), workflowModel.getInstanceid(),
					workflowModel.getTaskcode(), workflowModel.getTaskstate(), workflowModel.getTaskname());
			if (rdValue != null && !rdValue.isEmpty()) {
				if (StringUtil.isNotEmpty(rdValue.get("tfb"))) {
					workflowModel.setTaskfeedback(rdValue.get("tfb"));
				}
				if (StringUtil.isNotEmpty(rdValue.get("opt"))) {
					workflowModel.setOperator(rdValue.get("opt"));
				}
			}
		}

		// 更新节点
		if (oldMap != null && !oldMap.isEmpty()) {
			if (workflowModel.getMaininstanceid() == null) {
				String mainInstanceId = insbWorkflowsubDao.selectMainInstanceIdBySubInstanceId(workflowModel.getInstanceid());
				workflowModel.setMaininstanceid(mainInstanceId);
			}

			if ("Completed".equals(workflowModel.getTaskstate())) {
				if (StringUtil.isEmpty(workflowModel.getOperator())) {
					workflowModel.setOperator(oldMap.get("operator"));
				}
				workflowModel.setGroupid(oldMap.get("groupid"));
			}

			String operator1 = workflowModel.getOperator();
			String groupid1 = workflowModel.getGroupid();

			// 更新轨迹流程，并获取trackid
			updateTrackTable(workflowModel);
			String operator2 = workflowModel.getOperator();
			String groupid2 = workflowModel.getGroupid();

			//人工任务设置操作人
			if (workflowDataService.isManWork(workflowModel.getTaskcode())) {
				workflowModel.setOperator(operator1);
				workflowModel.setGroupid(groupid1);
			}
			workflowModel.setId(oldMap.get("id"));
			//如果是完成节点，当前节点taskcode已不是要完成的节点的taskcode则不更新workflowsub表处理
			if ("Completed".equals(workflowModel.getTaskstate()) && !workflowModel.getTaskcode().equals(oldMap.get("taskcode"))) {
				LogUtil.error("Completed,taskid=%s,subinstanceid=%s,不更新insbWorkflowsub数据taskcode=%s不一致出错：当前是%s" ,
						workflowModel.getMaininstanceid(), workflowModel.getInstanceid(), workflowModel.getTaskcode(), oldMap.get("taskcode"));
			}else{
				insbWorkflowsubDao.updateById(workflowModel);
			}

			LogUtil.info(workflowModel.getMaininstanceid()+","+workflowModel.getInstanceid()+"更新子流程状态成功："+
					workflowModel.getTaskcode()+","+workflowModel.getTaskstate()+":"+workflowModel.getTaskfeedback()+";map="+oldMap);
			workflowModel.setOperator(operator2);
			workflowModel.setGroupid(groupid2);

		} else {
			// 新增节点
			workflowModel.setCreatetime(new Date());

			String operator1 = workflowModel.getOperator();
			String groupid1 = workflowModel.getGroupid();

			// 更新轨迹流程，并获取trackid
			updateTrackTable(workflowModel);
			String operator2 = workflowModel.getOperator();
			String groupid2 = workflowModel.getGroupid();

			//人工任务设置操作人
			if (workflowDataService.isManWork(workflowModel.getTaskcode())) {
				workflowModel.setOperator(operator1);
				workflowModel.setGroupid(groupid1);
			}

			workflowModel.setId(null);
			insbWorkflowsubDao.insert(workflowModel);

			workflowModel.setOperator(operator2);
			workflowModel.setGroupid(groupid2);
		}

		workflowModel.setFromoperator(fromOperator);

		if (StringUtil.isEmpty(workflowModel.getOperator())) {
			try {
				INSBWorkflowsub flow = new INSBWorkflowsub();
				flow.setInstanceid(workflowModel.getInstanceid());
				flow = insbWorkflowsubDao.selectOne(flow);
				if (flow != null && StringUtil.isNotEmpty(flow.getOperator())) {
					workflowModel.setOperator(flow.getOperator());
				}
			} catch (Exception e) {
				workflowModel.setOperator("admin");
				LogUtil.error("查询" + workflowModel.getMaininstanceid() + "," + workflowModel.getInstanceid() + "的insbWorkflowsub数据出错：" + e.getMessage());
				e.printStackTrace();
			}
		}

		//若setGroup修改设置组信息不新增trackdetail数据
		if(!"setGroup".equals(workflowModel.getOperatorstate())) {
			workflowsubtrackdetailDao.myInsert(workflowModel);
		}
        WorkflowFeedbackUtil.delWorkflowFeedback(workflowModel.getMaininstanceid(), workflowModel.getInstanceid(),
                workflowModel.getTaskcode(), workflowModel.getTaskstate(), workflowModel.getTaskname());

		if ("Completed".equals(workflowModel.getTaskstate())) {
			//更新最上面的一条信息的modifytime
			try {
				Map<String, String> param1 = new HashMap<String, String>();
				param1.put("instanceid", workflowModel.getInstanceid());
				param1.put("taskcode", workflowModel.getTaskcode());
				if(!"admin".equals(workflowModel.getOperator())&&StringUtil.isNotEmpty(workflowModel.getOperator())){
					param1.put("operator", workflowModel.getOperator());
				}

				List<INSBWorkflowsubtrackdetail> detailModels = workflowsubtrackdetailDao.selectUpdatableData4RunQian(param1);
				if (detailModels != null && detailModels.size() > 1) {
					//第一个数据的taskstate应该是Completed
					if ("Completed".equalsIgnoreCase(detailModels.get(0).getTaskstate())) {
						INSBWorkflowsubtrackdetail detailModel = detailModels.get(1);
						if (!"Completed".equalsIgnoreCase(detailModel.getTaskstate())) {
							detailModel.setModifytime(detailModels.get(0).getCreatetime());
							workflowsubtrackdetailDao.updateById(detailModel);
						} else {
							LogUtil.error("%s轨迹数据不正确2：%s", workflowModel.getInstanceid(), detailModel.getTaskstate());
						}
					} else {
						LogUtil.error("%s轨迹数据不正确1：%s", workflowModel.getInstanceid(), detailModels.get(0).getTaskstate());
					}
				} else {
					LogUtil.error("%s轨迹数据不正确", workflowModel.getInstanceid());
				}

			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.info(workflowModel.getTaskcode()+"=taskcode更新Completed信息的modifytime报错。taskid="+workflowModel.getMaininstanceid());
			}
		}
		
		if("20".equals(workflowModel.getTaskcode())){//核保完成 关闭其他人工任务
			LogUtil.info("---支付 关闭其他人工任务---- workflowModel:"+workflowModel);
			changeSubInstance2PusePay(workflowModel);
		}

		if("16".equals(workflowModel.getTaskcode()) || "17".equals(workflowModel.getTaskcode()) || "18".equals(workflowModel.getTaskcode()) ||
                "40".equals(workflowModel.getTaskcode()) || "41".equals(workflowModel.getTaskcode())) {
			INSBWorkflowsub sub = new INSBWorkflowsub();
			sub.setInstanceid(workflowModel.getInstanceid());
			sub = insbWorkflowsubDao.selectOne(sub);

            if (sub != null) {
                if ("Reserved".equals(workflowModel.getTaskstate()) || "Ready".equals(workflowModel.getTaskstate())) {
                    if (sub.getFirstunderwritingtime() == null) {
                        sub.setFirstunderwritingtime(new Date());
                        insbWorkflowsubDao.updateById(sub);
                        LogUtil.info(workflowModel.getMaininstanceid() + "," + workflowModel.getInstanceid() + "更新首次核保时间：" +
                                workflowModel.getTaskcode() + "," + workflowModel.getTaskstate());
                    }
                } else if ("Completed".equals(workflowModel.getTaskstate())) {
                    sub.setLastunderwritingovertime(new Date());
					insbWorkflowsubDao.updateById(sub);
					LogUtil.info(workflowModel.getMaininstanceid() + "," + workflowModel.getInstanceid() + "更新核保完成时间：" +
                            workflowModel.getTaskcode() + "," + workflowModel.getTaskstate());
                }
            }
		}

		//更新车险任务查询实时表
		if (StringUtil.isNotEmpty(workflowModel.getTaskname())) {
			if ("Ready".equalsIgnoreCase(workflowModel.getTaskstate()) || "Reserved".equalsIgnoreCase(workflowModel.getTaskstate())) {
				insbRealtimetaskService.addRealtimetask(workflowModel);
			} else if ("Closed".equalsIgnoreCase(workflowModel.getTaskstate()) || "Completed".equalsIgnoreCase(workflowModel.getTaskstate()) || "End".equalsIgnoreCase(workflowModel.getTaskstate())) {
				insbRealtimetaskService.deleteRealtimetask(workflowModel);
			} else {
				LogUtil.info("INSBRealtimetask error Taskstate : " + workflowModel.getTaskstate());
			}
		}  else {
			LogUtil.info("INSBRealtimetask error Taskname : " + workflowModel.toString());
		}

		if(workflowModel.getTaskcode().equals("37")){
			WorkFlow4TaskModel dataModel = new WorkFlow4TaskModel();
			dataModel.setMainInstanceId(workflowModel.getMaininstanceid());
			dataModel.setSubInstanceId(workflowModel.getInstanceid());
			dataModel.setTaskCode(workflowModel.getTaskcode());
			dataModel.setTaskName(workflowModel.getTaskname());
			dataModel.setTaskStatus(workflowModel.getTaskstate());
			dataModel.setDataFlag(2);

			taskthreadPool4workflow.execute(new Runnable() {
				@Override
				public void run() {
					try {
						channelService.callback(dataModel);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	/**
	 * 更新轨迹表信息
	 */
	private void updateTrackTable(INSBWorkflowsub subModel) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("instanceid", subModel.getInstanceid());
		map.put("taskcode", subModel.getTaskcode());
		INSBWorkflowsubtrack workflowsubtrack = insbWorkflowsubtrackDao.selectByInstanceIdTaskCode(map);

		if (workflowsubtrack != null && (!"Completed".equals(workflowsubtrack.getTaskstate()) ||
				("Completed".equals(workflowsubtrack.getTaskstate()) && "Completed".equals(subModel.getTaskstate())))) {
			if (subModel.getMaininstanceid() == null) {
				String mainInstanceId = insbWorkflowsubDao.selectMainInstanceIdBySubInstanceId(subModel.getInstanceid());
				subModel.setMaininstanceid(mainInstanceId);
			}
			subModel.setId(workflowsubtrack.getId());
			subModel.setModifytime(new Date());

			//如果工作流通知的不是当前最新的状态，这里的id就不是最新的，就会出现问题了
			subModel.setWfsubtrackid(workflowsubtrack.getId());
			
			insbWorkflowsubtrackDao.updateySubTable(subModel);

		} else {
			subModel.setCreatetime(new Date());
			subModel.setId(null);
			if(null==subModel.getOperator() || StringUtil.isEmpty(subModel.getOperator())){
				try {
					INSBWorkflowsub flow = new INSBWorkflowsub();
					flow.setInstanceid(subModel.getInstanceid());
					flow = insbWorkflowsubDao.selectOne(flow);
					if(flow != null && null!=flow.getOperator() && StringUtil.isNotEmpty(flow.getOperator())) {
						subModel.setOperator(flow.getOperator());
					}
				} catch (Exception e) {
					LogUtil.error("查询" + subModel.getMaininstanceid() + "," + subModel.getInstanceid() + "的insbWorkflowsub数据出错：" + e.getMessage());
					e.printStackTrace();
				}
			}
			if(StringUtil.isEmpty(subModel.getOperator())){
				subModel.setOperator("admin");
            } 
			if(StringUtil.isEmpty(subModel.getFromoperator())){
				subModel.setFromoperator("admin");
            }

			insbWorkflowsubtrackDao.insertBySubTable(subModel);
			subModel.setWfsubtrackid(subModel.getId());
		}
	}

	/**
	 * 判断当前流程为支付 则其他子流程状态改为暂停支付
	 */
	private int changeSubInstance2PusePay(INSBWorkflowsub ParamSubModel) {
		INSBWorkflowsub tempSubModel = ParamSubModel;
		String mainInstanceId = null;
		if (tempSubModel.getMaininstanceid() == null) {
			mainInstanceId = insbWorkflowsubDao.selectMainInstanceIdBySubInstanceId(tempSubModel.getInstanceid());
			if(mainInstanceId==null){
				return 0;
			}
			tempSubModel.setMaininstanceid(mainInstanceId);
		}else{
			mainInstanceId = tempSubModel.getMaininstanceid();
		}
		List<INSBWorkflowsub> subList = insbWorkflowsubDao.selectSubInsIdExc(mainInstanceId);
		
		if (subList == null||subList.size()<=0) {
			return 0;
		}
		subList.remove(tempSubModel);
		
		LogUtil.info("---支付 关闭其他人工任务---- subList:"+subList);
		if (subList.size()<=0) {
			return 0;
		}
		for (int i = 0; i < subList.size(); i++) {
			INSBWorkflowsub newModel = subList.get(i);
			String taskcodebak = newModel.getTaskcode();
			String idbak = newModel.getId();
			
			if (("Completed".equals(tempSubModel.getTaskstate()) && !WorkflowFeedbackUtil.payment_reunderWriting.equals(tempSubModel.getTaskfeedback()) 
					&& !WorkflowFeedbackUtil.quote_cancel.equals(tempSubModel.getTaskfeedback())&& 
					!"30".equals(taskcodebak) && !"33".equals(taskcodebak) && !"37".equals(taskcodebak)) ||
					(("Reserved".equals(tempSubModel.getTaskstate()) || "Ready".equals(tempSubModel.getTaskstate())) &&
					("7".equals(taskcodebak) || "8".equals(taskcodebak) || "51".equals(taskcodebak) || "52".equals(taskcodebak)))) {
				newModel.setTaskcode("30");
				newModel.setTaskname("拒绝承保");
				newModel.setTaskstate("Closed");
				newModel.setOperator("admin");
				newModel.setCreatetime(new Date());
				newModel.setModifytime(new Date());
				newModel.setTaskfeedback("自动关闭#其他任务核保完成");

				insbRealtimetaskService.deleteRealtimetask(newModel);
				
				//查询车信息和车主信息
				INSBCarinfo carinfo = insbCarinfoDao.selectCarinfoByTaskId(newModel.getMaininstanceid());
				//通知正在处理的业管此任务或已被代理人关闭
				Map<String, String> param = new HashMap<String, String>();
				param.put("subTaskId", newModel.getInstanceid());
				INSBWorkflowsubtrackdetail lastmodel = workflowsubtrackdetailDao.selectLastModelBySubInstanceId(param);
				if(null!=lastmodel && "Proccessing".equals(lastmodel.getOperatorstate())){
					INSCUser user = inscUserService.getByUsercode(lastmodel.getOperator());
					taskthreadPool4workflow.execute(new Runnable() {
						@Override
						public void run() {
							// 得到系统用户发送消息
							INSCUser fromUser = inscUserService.getByUsercode("admin");
							try {
								LogUtil.info("===任务关闭任务调度查找业管---向业管发送消息---maintaskid="+newModel.getMaininstanceid()+"=taskCode="+lastmodel.getTaskcode());
								dispatchTaskService.sendXmppMessage4User(fromUser, user, "taskDel@"+"车牌："+(null==carinfo?"XXXX":carinfo.getCarlicenseno())+"，车主："+(null==carinfo?"XXX":carinfo.getOwnername())+" 的报价，该报价将自动关闭并移除");
								LogUtil.info("**===任务关闭发送xmpp消息成功,maintaskid="+newModel.getMaininstanceid()+"=taskCode="+lastmodel.getTaskcode());
					
							} catch (Exception e) {
								LogUtil.error("***===任务关闭发送xmpp消息失败--向业管发送消息异常---maintaskid="+newModel.getMaininstanceid()+"=taskCode="+lastmodel.getTaskcode());
								e.printStackTrace();
								//throw e;把异常丢给外层处理
							}
						}
					});
				}
				//通知调度删除任务
		        taskthreadPool4workflow.execute(new Runnable() {
					@Override
					public void run() {
						Task task = new Task();
						//子流程任务
						task.setProInstanceId(newModel.getMaininstanceid());
						task.setSonProInstanceId(newModel.getInstanceid());
						
						LogUtil.info("maininstanceId=" + newModel.getMaininstanceid() + "," + "subinstanceId=" + newModel.getInstanceid() + "," +
								"在changeSubInstance2PusePay中通知调度删除任务");
						dispatchTaskService.deleteTask(task);

					}
				});

				insbWorkflowsubtrackDao.insertBySubTable(newModel);

				newModel.setWfsubtrackid(newModel.getId());
				newModel.setId(idbak);
				insbWorkflowsubDao.updateById(newModel);

				//先Completed
				newModel.setTaskcode(taskcodebak);
				newModel.setTaskstate("Completed");
				workflowsubtrackdetailDao.myInsert(newModel);

				//再insert
				newModel.setTaskcode("30");
				newModel.setTaskstate("Closed");
				workflowsubtrackdetailDao.myInsert(newModel);

				WorkFlow4TaskModel dataModel = new WorkFlow4TaskModel();
				dataModel.setMainInstanceId(newModel.getMaininstanceid());
				dataModel.setSubInstanceId(newModel.getInstanceid());
				dataModel.setTaskCode(newModel.getTaskcode());
				dataModel.setTaskName(newModel.getTaskname());
				dataModel.setTaskStatus(newModel.getTaskstate());
				dataModel.setDataFlag(2);
				taskthreadPool4workflow.execute(new Runnable() {
					@Override
					public void run() {
						try {
							channelService.callback(dataModel);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				//子任务关闭状态推送资料到懒掌柜 add by hewc 20170723
				lzgDataTransferService.pushOrderToLzg(newModel.getMaininstanceid(),newModel.getInstanceid(),newModel.getProviderId());
			}
		}
		LogUtil.info("---支付 关闭其他人工任务----完成");
		return 1;
	}

	@Override
	public void deleteWorkFlowSubData(String instanceId) {
		insbWorkflowsubDao.deleteByInstanceId(instanceId);
	}

	@Override
	public List<String> queryInstanceIdsByMainInstanceId(String instanceId) {
		return insbWorkflowsubDao.selectInstanceIdByMainInstanceId(instanceId);
	}

	/**
	 * 车险任务查看任务流程
	 */
	public Map<String, Object> showWorkflowTrack(String instanceId,
			String inscomcode) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("instanceId", instanceId);
		params.put("inscomcode", inscomcode);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("instanceId", instanceId);// 流程号
		INSBProvider provider = insbProviderDao.queryByPrvcode(inscomcode);
		if (provider != null) {
			result.put("inscomName", provider.getPrvshotname());// 保险公司名称
		} else {
			result.put("inscomName", "未知保险公司");// 保险公司名称
		}
		// result.put("workflowTrackList", temp);//信息列表
		 List<Map<String, Object>> trackOld= insbWorkflowsubDao.showWorkflowTrack(params);
		 List<Map<String, Object>> trackNew = new ArrayList<Map<String,Object>>();
		 int i=0;
		 boolean flag=false;//记录是否支付
		 for (Map<String, Object> map : trackOld) { 
			String taskcode=(String)map.get("taskcode");
			String opPerson=(String)map.get("opPerson");
			//除非关闭或者打单配送可以因为admin自动生成，其他任务目标人未知的不显示轨迹
			if(opPerson.contains("目标人：未知")&&!TaskConst.CLOSE_37.equals(taskcode)
					&&!TaskConst.END_33.equals(taskcode)&&!TaskConst.UNDERWRITESUCCESS_23.equals(taskcode)
					&&!TaskConst.VERIFYINGFAILED_30.equals(taskcode)&&!TaskConst.STARTPOST_24.equals(taskcode)){
				continue;
			}
			if(i == 0 && ("37".equals(taskcode) || "30".equals(taskcode))){ 
				trackNew.add(map);
				i++;
			}else{
				// 如果还出现关闭节点继续加载轨迹内容，否则结束
				if (i > 0 && ("37".equals(taskcode) || "39".equals(taskcode) || "30".equals(taskcode))) {
					trackNew.add(map);
					continue;
				}else if(i == 0){
					if(TaskConst.PAYING_20.equals(taskcode)){
						flag=true;
					}
					//支付后不在显示“快速续保”
					if(flag&&TaskConst.QUOTING_CONTINUE_15.equals(taskcode)){
						continue;
					}
					trackNew.add(map);
					continue;
				}else{
					break;
				}
			}
		 }
		result.put("workflowTrackList",trackNew);// 信息列表
		return result;
	}

	@Override
	public Map<String, String> getDataByMainInstanceId(String instanceId) {
		Map<String, String> result = new HashMap<String, String>();
		String id = quotetotalinfoDao.selectByTaskId4TaskSet(instanceId);
		if (id != null) {
			List<Map<String, String>> tempResult = quoteinfoDao
					.selectSubInstanceIdProviderIdByTotalId(id);
			if (tempResult != null) {
				for (Map<String, String> map : tempResult) {
					result.put(map.get("workflowinstanceid"),
							map.get("inscomcode"));
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> getMediumPayInfo(Map<String, Object> map) {
		return insbWorkflowsubDao.getMediumPayInfo(map);
	}

	@Override
	public String getInstanceidByMaininstanceId(String maininstanceid) {
		return insbWorkflowsubDao.getInstanceidByMaininstanceId(maininstanceid);
	}

	@Override
	public String getTransformTaskInstanceid(Map<String, String> map) {
		return insbWorkflowsubDao.getTransformTaskInstanceid(map);
	}
	
	@Override
	public List<INSBWorkflowsub> selectSubModelByMainInstanceId(String mainInstanceId) {
		return insbWorkflowsubDao.selectSubModelByMainInstanceId(mainInstanceId);
	}

	public List<Map<String, Object>> selectSubModelInfoByMainInstanceId(String mainInstanceId) {
		return insbWorkflowsubDao.selectSubModelInfoByMainInstanceId(mainInstanceId);
	}
}