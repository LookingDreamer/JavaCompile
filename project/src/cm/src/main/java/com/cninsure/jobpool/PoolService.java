package com.cninsure.jobpool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import com.zzb.conf.entity.INSBGroupmembers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.conf.dao.INSBBusinessmanagegroupDao;
import com.zzb.conf.dao.INSBGroupdeptDao;
import com.zzb.conf.dao.INSBGroupmembersDao;
import com.zzb.conf.dao.INSBGroupprovideDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.mobile.service.AppInsuredQuoteService;

/**
 * 任务派发逻辑
 * 
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class PoolService {

	@Resource
	private INSBQuotetotalinfoDao quotetotalinfoDao;
	@Resource
	private INSBQuoteinfoDao quoteinfoDao;
	@Resource
	private INSBGroupdeptDao groupdeptDao;
	@Resource
	private INSBGroupprovideDao groupprovideDao;	
	@Resource
	private INSBGroupmembersDao groupmembersDao;
	@Resource
	private INSCUserDao userDao;
	@Resource
	private INSBBusinessmanagegroupDao businessmanagegroupDao;
	@Resource
	private INSBWorkflowsubtrackDao workflowsubTrackDao;
	@Resource
	private INSBWorkflowsubDao workflowsubDao;
	@Resource
	private INSCCodeDao codeDao;
	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;

	/**
	 * 根据任务计算得到任务组
	 * 
	 * @param task
	 * @return
	 */
	public List<String> dispathGroupByTask(Task task) {
		LogUtil.info("*******===任务调度查找业管---查找业管组---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
		Map<String, String> tempParam2 = new HashMap<String, String>();
		tempParam2.put("codetype", "tasktype");
		tempParam2.put("codename", task.getTaskName());
		// 得到任务类型
		List<INSCCode> codeList = codeDao.selectINSCCodeByParentCode(tempParam2);
		Map<String, String> tempParam = new HashMap<String, String>();

		if(null!=task && "认证任务".equals(task.getTaskName())){
			LogUtil.info("认证任务查找处理任务组，id="+task.getProInstanceId());
			tempParam.put("tasktype", codeList.get(0).getCodevalue());
			tempParam.put("deptId", task.getPrvcode());//认证任务所属机构代码
			return businessmanagegroupDao.selectIdsByIdsAndTaskType4CertifiTask(tempParam);//认证任务直接处理完就返回组信息
		}

		List<String> groupIds = new ArrayList<String>();
		tempParam.put("inscomcode", task.getPrvcode());
		tempParam.put("taskid", task.getProInstanceId());

		List<String> deptIds= quoteinfoDao.selectDeptIdByQuotetotalIdAndComCode4Task(tempParam);
		LogUtil.info("*******===任务调度查找业管---查找业管组---通过报价表得到出单网点---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---deptids="+deptIds);

		if(deptIds==null||deptIds.isEmpty()){
			LogUtil.info("*******===任务调度查找业管---查找业管组---通过报价表得到出单网点---异常---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
			return groupIds;
		}

		String deptId  = deptIds.get(0);
		if (deptId != null) {
			List<String> TempGroupIds = groupdeptDao.selectGroupIdIdByDeptId4Task(deptId);
			/*List<String> noDeptGroupIds = groupdeptDao.selectNoDeptGroupIdId();
			if(noDeptGroupIds != null && !noDeptGroupIds.isEmpty()) {
				TempGroupIds.addAll(noDeptGroupIds);
			}*/

			LogUtil.debug("*******===任务调度查找业管---查找业管组---业管组网点关系表得到业管组id---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---groupIds="+TempGroupIds);
			
			List<String> TempGroupIds1 = new ArrayList<String>();
			String prvId = "";
			try {
				prvId = getProvId(new ArrayList<String>(),task.getPrvcode());
				LogUtil.info("*******===任务调度查找业管---查找业管组---得到订单顶级供应商---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---prvid="+prvId);
			} catch (Exception e) {
				LogUtil.info("*******===任务调度查找业管---查找业管组---得到订单顶级供应商---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---异常");
				e.printStackTrace();
			}
			if(StringUtil.isNotEmpty(prvId)){
				TempGroupIds1=  groupprovideDao.selectGroupIdByProvideid(prvId);
				/*List<String> noProvideGroupIds = groupprovideDao.selectNoProvideGroupId();
				if(noProvideGroupIds != null && !noProvideGroupIds.isEmpty()) {
					TempGroupIds1.addAll(noProvideGroupIds);
				}*/
				LogUtil.debug("*******===任务调度查找业管---查找业管组---业管组供应商关系表得到业管组id---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---groupid="+TempGroupIds1);
			}
			
			if(TempGroupIds1!=null&&!TempGroupIds1.isEmpty()){
				TempGroupIds.retainAll(TempGroupIds1);
				LogUtil.info("*******===任务调度查找业管---查找业管组---交集后的业管组---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---groupid="+TempGroupIds);
			}else{
				return null;
			}
			
			if(TempGroupIds==null||TempGroupIds.isEmpty()){
				LogUtil.info("*******===任务调度查找业管---查找业管组---没有符合条件的业管组---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
				return null;
			}
			
			if (codeList != null&&!codeList.isEmpty()) {
				Map<String, Object> tempParam1 = new HashMap<String, Object>();
				tempParam1.put("ids", TempGroupIds);
				tempParam1.put("tasktype", codeList.get(0).getCodevalue());
				tempParam1.put("deptId", deptId);
				
				groupIds = businessmanagegroupDao.selectIdsByIdsAndTaskType4Task(tempParam1);
				LogUtil.info("*******===任务调度查找业管---查找业管组---得到有权限的业管组---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---groupid="+groupIds);
			}
			
			groupIds.retainAll(TempGroupIds);
			List<String> result = new ArrayList<String>();

			for (String groupid : groupIds) {
				List<INSBGroupmembers> groupmembersList = groupmembersDao.selectByGroupId(groupid);
				if (groupmembersList != null && !groupmembersList.isEmpty()) {
					boolean hasExe = false;

					for (INSBGroupmembers groupmember : groupmembersList) {
						if (2 == groupmember.getGroupprivilege()) {
							hasExe = true;
							break;
						}
					}

					if (hasExe) {
						result.add(groupid);
					} else {
						LogUtil.info("群组"+groupid+"成员无执行权限");
					}

				} else {
					LogUtil.info("群组"+groupid+"无成员");
				}
			}

			groupIds = result;
		}
		
		return groupIds;
	}

	private String getProvId(List<String> result,String id){
		
//		INSBProvider prvModel = providerDao.selectById(id);
//		
//		if(prvModel.getPrvgrade()!=null){
//			if(prvModel.getParentcode()==null){
//				System.out.println(prvModel.getId());
//				result.add(prvModel.getId());
//			}else{
//				getProvId(result,prvModel.getParentcode());
//			}
//		}
//		return result.get(0);
		if(StringUtil.isEmpty(id)||id.length()<4){
			return id;
		}else{
			return id.substring(0,4);
		}
	}

	/**
	 * 根据业管组得到业管
	 * 
	 * @param task
	 * @return
	 */
	public INSCUser dispathUserByTask(Task task) {

		// 打单配送 业管认领不需要自动分配
		if ("配送".equals(task.getTaskName())) {
			LogUtil.info("*******===任务调度查找业管---maintaskid---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"="+task.getProInstanceId()+"---prv="+task.getPrvcode()+"---配送不需要自动分人");
			return null;
		}

		// 得到唯一的业管
		String ablityUser = getEligibilityUsers(task);
		INSCUser user = null;
		if (ablityUser != null) {
			user = userDao.selectByUserCode(ablityUser);
		}
		return user;
	}

	/**
	 * 按照任务组得到可用用户
	 * 
	 * @param task
	 * @return
	 */
	private String getEligibilityUsers(Task task) {
		
		String liaoNingUserCode = agentAreaHandler(task.getProInstanceId(),task.getSonProInstanceId(),task.getTaskcode());
		
		if(!StringUtils.isEmpty(liaoNingUserCode)){
			LogUtil.info("*******===任务调度查找业管---得到最终符合条件业管组---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---非费改订单人工规则报价处理");
			return liaoNingUserCode;
		}
		// 得到所有的群组
		List<String> groupIds = dispathGroupByTask(task);
		LogUtil.info("*******===任务调度查找业管---得到最终符合条件业管组---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---group="+groupIds);

		if (groupIds == null||groupIds.isEmpty()) {
			return null;
		}
		
		//拒绝过当前任务的业管
		List<String> refuseUsers = new ArrayList<String>();
		
		
		String[] trackArray  = getTaskTracks(task.getTaskTracks());
		if(null!=trackArray){
			for (String userCode : trackArray) {
				refuseUsers.add(userCode);
			}
			LogUtil.info("*******===任务调度查找业管---拒绝过任务的业管---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---refuseUsers="+refuseUsers);
		}
		
		//得到符合条件业管
		Map<String,Object> getUserCodes = new HashMap<String,Object>();
		
		
		if(refuseUsers.isEmpty()){
			getUserCodes.put("refuseUsers", null);
		}else{
			getUserCodes.put("refuseUsers", refuseUsers);
		}
		getUserCodes.put("groupIds", groupIds);
		
		
		List<String> eligibilityUsers = groupmembersDao.selecUserCodeByGroupIds4Task(getUserCodes);
		LogUtil.info("*******===任务调度查找业管---得到有能力的业管---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---eligibilityUsers="+eligibilityUsers);

		
		if (eligibilityUsers == null||eligibilityUsers.isEmpty()) {
			return null;
		}else{//无用逻辑，后台查询时就应该把null的usercode过滤掉就ok了。。。。。。askqvn20160603
			List<String> tempEligibilityUsers = new ArrayList<String>();
			for(String str:eligibilityUsers){
				if(null!=str){
					tempEligibilityUsers.add(str);
				}
			}
			eligibilityUsers.clear();
			eligibilityUsers.addAll(tempEligibilityUsers);

		}
		if (eligibilityUsers == null||eligibilityUsers.isEmpty()) {
			return null;
		}
		
		LogUtil.info("*******===任务调度查找业管---最终业管---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---eligibilityUsers="+eligibilityUsers);

		Map<String, Object> param = new HashMap<String, Object>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String datestr = format.format(new Date());
		
		param.put("startTime", datestr+" 00:00:00");
		param.put("endTime", datestr+" 23:59:59");
		param.put("userList", eligibilityUsers);
		
		

		/**
		 * 1:符合条件的业管 ，今天是否有处理过单子（包括处理完成的和未处理的）
		 * 1.1：都没有----给第一个业管（随机一个）
		 * 1.2：都处理过----排除当前业管未处理完成的单子量 给第一个（随机一个）
		 * 1.3：部分处理过------分给没有处理过的第一个（随机一个）
		 */
		
		List<Map<String, Object>> allSubUserWork = workflowsubTrackDao.selectUserCodesByTimeAndTaskNumAll4Task(param);
		LogUtil.info("*******===任务调度查找业管---业管工作量排序---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---allSubUserWork="+allSubUserWork);

		//1.1：都没有----给第一个业管（随机一个）
		if(allSubUserWork==null||allSubUserWork.isEmpty()){
			LogUtil.info("*******===任务调度查找业管---业管工作量为0---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
			int randomCount = eligibilityUsers.size();
			Random random = new Random();
	        int userNo=random.nextInt(randomCount);
			LogUtil.info("*******===任务调度查找业管---随机分配符合条件业管---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+ "---usercode="+eligibilityUsers.get(userNo));
	        return eligibilityUsers.get(userNo);
		}else{

			LogUtil.info("*******===任务调度查找业管---有业管有工作量---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
			List<String> tempWorkedOperator = new ArrayList<String>();
			for(Map<String, Object> map:allSubUserWork){
				tempWorkedOperator.add(map.get("operator").toString());
			}
			
			eligibilityUsers.removeAll(tempWorkedOperator);
			LogUtil.info("*******===任务调度查找业管---没有工作量业管---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---eligibilityUsers="+eligibilityUsers);

			if(eligibilityUsers.isEmpty()){
				LogUtil.info("*******===任务调度查找业管---所有业管全部有工作量---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode());
				
				//1.2：都处理过----排除当前业管未处理完成的单子量 给第一个（随机一个）
				param.put("userList", tempWorkedOperator);
				List<Map<String, Object>> subUserWork = workflowsubTrackDao.selectUserCodesByTimeAndTaskNum4Task(param);
				LogUtil.info("*******===任务调度查找业管---未完成工作业管工作量排序---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+ "---subUserWork"+subUserWork);
				
				List<String> unFinishedWorkeOperator = new ArrayList<String>();
				for(Map<String, Object> map:subUserWork){
					unFinishedWorkeOperator.add(map.get("operator").toString());
				}
				tempWorkedOperator.removeAll(unFinishedWorkeOperator);
				if(tempWorkedOperator.isEmpty()){
					LogUtil.info("*******===任务调度查找业管---所有业管都有未完成工作量---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---分给工作量最少的业管---usercode="+subUserWork.get(0).get("operator").toString());
					return subUserWork.get(0).get("operator").toString();
				}else{
					LogUtil.info("*******===任务调度查找业管---分给工作量最少的业管---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+"---usercode="+tempWorkedOperator.get(0));
					return tempWorkedOperator.get(0);
				}
			}else{
				//1.3：部分处理过------分给没有处理过的第一个（随机一个）
				int randomCount = eligibilityUsers.size();
				Random random = new Random();
		        int userNo=random.nextInt(randomCount);
				LogUtil.info("*******===任务调度查找业管---有业管未处理过订单随机分给业管---maintaskid="+task.getProInstanceId()+"---taskName="+task.getTaskName()+"---prv="+task.getPrvcode()+ "---usercode="+eligibilityUsers.get(userNo));
		        return eligibilityUsers.get(userNo);
			}
		}
	}
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
	 * 非费改地区特殊处理
	 * 
	 * @param mainTaskId 主流程id
	 * @param subTaskId  子流程id
	 * @param taskCode 结点
	 * @return
	 */
	private String agentAreaHandler(String mainTaskId,String subTaskId,String taskCode){
		
		LogUtil.info("*******===任务调度查找业管---费改区域判断---mainTaskId="+mainTaskId+"---subTaskId="+subTaskId+"---taskCode="+taskCode);

		//是不是人工规则报价
		if(StringUtils.isEmpty(taskCode)||!"7".equals(taskCode)){
			LogUtil.info("*******===任务调度查找业管---费改区域判断---不是人工规则报价不做判断---mainTaskId="+mainTaskId+"---subTaskId="+subTaskId+"---taskCode="+taskCode);
			return "";
		}
		
		//查询当前订单代理人信息
		Map<String,String> agentData = quotetotalinfoDao.selectAgentDataByTaskId(mainTaskId);
		LogUtil.info("*******===任务调度查找业管---费改区域判断---mainTaskId="+mainTaskId+"---subTaskId="+subTaskId+"---taskCode="+taskCode+"---代理人信息="+agentData);

		if(agentData.isEmpty()){
			return "";
		}
		
		//得到代理人工号
		String agentnum = agentData.get("agentnum");
		LogUtil.info("*******===任务调度查找业管---费改区域判断---mainTaskId="+mainTaskId+"---subTaskId="+subTaskId+"---taskCode="+taskCode+"---代理人工号="+agentnum);

		if(StringUtils.isEmpty(agentnum)){
			return "";
		}
		
		Map<String, Object> resultAgentData =  appInsuredQuoteService.getChangeFee(null,agentnum);
		LogUtil.info("*******===任务调度查找业管---费改区域判断---代理人判断---mainTaskId="+mainTaskId+"---subTaskId="+subTaskId+"---taskCode="+taskCode+"---代理人判断结果="+resultAgentData.toString());

		if(!(boolean)resultAgentData.get("isfeeflag")){
			return "";
		}
		
		List<INSBWorkflowsub> subModel = workflowsubDao.selectSubModelByMainInstanceId(mainTaskId);
		if(subModel==null){
			return "";
		}
		LogUtil.info("*******===任务调度查找业管---费改区域判断---所有子流程信息---mainTaskId="+mainTaskId+"---subTaskId="+subTaskId+"---taskCode="+taskCode+"---subModel="+subModel.toString());
		for(INSBWorkflowsub model:subModel){
			if(model.getOperator()!=null){
				LogUtil.info("*******===任务调度查找业管---费改区域判断---得到唯一操作人---mainTaskId="+mainTaskId+"---subTaskId="+subTaskId+"---taskCode="+taskCode+"---userCode="+model.getOperator());
				return model.getOperator();
			}
		}
		return "";
	}
}
