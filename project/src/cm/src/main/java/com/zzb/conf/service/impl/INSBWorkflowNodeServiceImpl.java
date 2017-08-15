package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cninsure.core.exception.ServiceException;
import com.cninsure.core.httpclient.ClientTaskApi;
import com.cninsure.core.httpclient.IClientRuleApi;
import com.cninsure.core.utils.JsonUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.service.INSCUserService;
import com.common.WorkFlowUtil;
import com.zzb.conf.dao.INSBRuleBaseDao;
import com.zzb.conf.entity.INSBBusinessmanagegroup;
import com.zzb.conf.entity.INSBTaskset;
import com.zzb.conf.service.INSBBusinessmanagegroupService;
import com.zzb.conf.service.INSBTasksetService;
import com.zzb.conf.service.INSBWorkflowNodeService;

/**
 * @author hlj
 * @version 1.0
 * @since 16:59 2015/9/16
 */
@Service
public class INSBWorkflowNodeServiceImpl implements INSBWorkflowNodeService {
	@Resource
	private INSBTasksetService insbTasksetService;
	@Resource
	private INSBBusinessmanagegroupService insbBusinessmanagegroupService;
	@Resource
	private INSCUserService inscUserService;
	@Resource
	private INSBRuleBaseDao insbRuleBaseDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zzb.conf.service.INSBWorkflowNodeService#getPersonInfo(java.util.Map)
	 */
	@Override
	public String getPersonInfo(Map<String, Object> paramMap)
			throws ServiceException {
		String result = "{}";
		Map<String, String> map = new HashMap<String, String>();

		if (paramMap != null && !paramMap.isEmpty()) {
			String orgCode = null;
			String ruleParam = null;
			JSONObject ruleParamJsonObj = null;
			if ((paramMap.containsKey("orgCode")
					&& (orgCode = (String) paramMap.get("orgCode")) != null && StringUtils
						.isNotBlank(orgCode))
					&& (paramMap.containsKey("ruleParam")
							&& (ruleParamJsonObj = (JSONObject) paramMap
									.get("ruleParam")) != null && StringUtils
								.isNotBlank(ruleParam = ruleParamJsonObj
										.toString()))) {
				List<INSBTaskset> list = insbTasksetService
						.findListByDeptId(orgCode);
				Map<String, Object> tasksetIdRuleMap = new HashMap<String, Object>();
				if (list != null && !list.isEmpty() && list.size() > 0) {
					List<String> tasksetids = new ArrayList<String>();//
					String tasksetId = null;
					int setstatus = -1;
					for (INSBTaskset taskset : list) {
						if (taskset != null) {
							setstatus = taskset.getSetstatus();
							if (setstatus == 1) {
								tasksetId = taskset.getId();
								if (StringUtils.isNotBlank(tasksetId)) {
									tasksetids.add(tasksetId);
								}
								System.out.println("tasksetId:" + tasksetId
										+ " state:" + setstatus);
							}
						}
					}
					if (tasksetids != null && !tasksetids.isEmpty()
							&& tasksetids.size() > 0) {
						tasksetIdRuleMap.put("tasksetids", tasksetids);
						IClientRuleApi client = new ClientTaskApi();
						Map<String, String> ruleMap = new HashMap<String, String>();
						ruleMap.put("orgCode", orgCode);
						ruleMap.put("paramStr", ruleParam);
						String ruleName = null;
						try {
							ruleName = getRuleName(client
									.getRuleResult(ruleMap));
						} catch (Exception e) {
							throw new ServiceException("请确认是否启动了规则引擎调度程序。", e);
						}
						if (StringUtils.isNotBlank(ruleName)) {
							System.out.println("ruleName:" + ruleName);
							List<String> ruleNames = new ArrayList<String>();
							ruleNames.add(ruleName);
							tasksetIdRuleMap.put("ruleNames", ruleNames);
							Map<String, String> userInfoMap = getUserInfoMap(tasksetIdRuleMap);
							if (userInfoMap != null && !userInfoMap.isEmpty()) {
								result = JsonUtil.getJsonString(userInfoMap);
							}
						} else {
							throw new ServiceException("没有找到匹配的规则。");
						}
					}
				} else {
					throw new ServiceException("该机构下没有创建任务组。");
				}
			} else {
				throw new ServiceException(
						"获取人员信息的参数(orgCode,ruleParam)有问题，请重新传参。");
			}
		}
		if (map != null && !map.isEmpty()) {
			result = JsonUtil.getJsonString(map);
		}
		return result;
	}

	/**
	 * 获得规则名称
	 * 
	 * @param str
	 * @return
	 */
	public String getRuleName(String str) {
		String result = null;
		if (StringUtils.isNotBlank(str)) {
			JSONObject jobj = JSONObject.fromObject(str);
			String successKey = "ruleItem.taskDispatch.success";
			if (jobj != null && jobj.containsKey(successKey)) {
				boolean success = jobj.getBoolean(successKey);
				if (success) {
					String messageKey = "ruleItem.taskDispatch.message";
					if (jobj != null && jobj.containsKey(messageKey)) {
						String message = jobj.getString(messageKey);
						if (message != null && message.indexOf(" run") > -1) {
							result = message.substring(0,
									message.indexOf(" run"));
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 通过群组ID获得人员信息
	 * 
	 * @param groupid
	 * @return
	 */
	public Map<String, String> getUserInfoMapByGroupid(String groupid) {
		Map<String, String> userMap = null;
		if (StringUtils.isNotBlank(groupid)) {
			List<String> userList = inscUserService.findUserByGroupid(groupid);
			if (userList != null && !userList.isEmpty() && userList.size() > 0) {
				userMap = new HashMap<String, String>();
				String handleUser = null;
				int minTaskNum = -1;
				for (int i = 0; i < userList.size(); i++) {
					String userName = userList.get(i);
					if (StringUtils.isNotBlank(userName)) {
						userName = userName.trim();
						String userTaskInfo = WorkFlowUtil
								.getUserTaskInfo(userName);
						JSONObject jObj = null;
						if (StringUtils.isNotBlank(userTaskInfo)
								&& (jObj = JSONObject.fromObject(userTaskInfo)) != null
								&& jObj.containsKey("message")) {
							String message = jObj.getString("message");
							if (null != message
									&& "success".equals(message.trim())
									&& jObj.containsKey("data")) {
								JSONArray jsonArray = jObj.getJSONArray("data");
								if (jsonArray != null) {
									int taskNum = jsonArray.size();
									if (i == 0) {
										minTaskNum = taskNum;
										handleUser = userName;
									} else if (taskNum < minTaskNum) {
										minTaskNum = taskNum;
										handleUser = userName;
									}
									LogUtil.warn("userName:" + userName);
									System.out.println("userName:" + userName);
									System.out.println(userTaskInfo);
								}
							} else if (null != message
									&& "fail".equals(message.trim())
									&& jObj.containsKey("reason")) {
								String reason = jObj.getString("reason");
								LogUtil.warn(reason);
							}
						} else {
							handleUser = userName;
							break;
						}
					}
				}
				userMap.put("userName", (handleUser != null) ? handleUser : "");
			}
		}
		return userMap;
	}

	/**
	 * 通过任务组ID获得人员信息
	 * 
	 * @param tasksetid
	 * @return
	 */
	public Map<String, String> getUserInfoMapByTasksetid(String tasksetid) {
		Map<String, String> allUserInfoMap = null;
		if (StringUtils.isNotBlank(tasksetid)) {
			List<INSBBusinessmanagegroup> bmgrouplist = insbBusinessmanagegroupService
					.findByTasksetid(tasksetid);
			String groupid = "";
			Integer privilegestate = -1;
			if (bmgrouplist != null && !bmgrouplist.isEmpty()
					&& bmgrouplist.size() > 0) {
				allUserInfoMap = new HashMap<String, String>();
				Map<String, String> userInfoMap = null;
				for (INSBBusinessmanagegroup bmg : bmgrouplist) {
					if (bmg != null) {
						groupid = bmg.getId();
						privilegestate = bmg.getPrivilegestate();
						System.out.println("groupid:" + groupid
								+ " privilegestate:" + privilegestate);
						userInfoMap = getUserInfoMapByGroupid(groupid);
						if (userInfoMap != null && !userInfoMap.isEmpty()) {
							allUserInfoMap.putAll(userInfoMap);
							userInfoMap.clear();
						}
					}
				}
			}
		}
		return allUserInfoMap;
	}

	/**
	 * 通过任务组ID和规则名称获得人员信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, String> getUserInfoMap(Map<String, Object> map) {
		Map<String, String> allUserInfoMap = null;
		Map<String, String> userMap = null;
		if (map != null && !map.isEmpty()) {
			List<String> tasksetidList = insbRuleBaseDao
					.selectTasksetidList(map);
			if (tasksetidList != null && !tasksetidList.isEmpty()
					&& tasksetidList.size() > 0) {
				allUserInfoMap = new HashMap<String, String>();
				for (String tasksetid : tasksetidList) {
					System.out.println("tasksetid:" + tasksetid);
					if (StringUtils.isNotBlank(tasksetid)) {
						userMap = getUserInfoMapByTasksetid(tasksetid);
						if (userMap != null && !userMap.isEmpty()) {
							allUserInfoMap.putAll(userMap);
							userMap.clear();
						}
					}
				}
			}
		}
		return allUserInfoMap;
	}

	@Override
	public String getPersonInfo(String param) {
		String result = null;
		Map<String, Object> errMap = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(param)) {
			JSONObject jobj = JSONObject.fromObject(param);
			if(jobj.containsKey("taskid") && jobj.containsKey("subtaskid")
					&& jobj.containsKey("nodename")
					){
				Map<String, Object> map = new HashMap<String, Object>();
				
				String taskid = jobj.getString("taskid");
				String subtaskid = jobj.getString("subtaskid");
				String nodename = jobj.getString("nodename");
				if(StringUtils.isNotBlank(taskid)
				   && StringUtils.isBlank(subtaskid)
				   && StringUtils.isNotBlank(nodename)
						){//子流程
					if("精灵录单".equals(nodename)){

					}else if("人工调整".equals(nodename)){
						
					}else if("人工规则录单".equals(nodename)){
						
					}else if("人工录单".equals(nodename)){
						
					}
				}else if (StringUtils.isNotBlank(taskid)
						   && StringUtils.isNotBlank(subtaskid)
						   && StringUtils.isNotBlank(nodename)
								){
					
				}
				
				try {
					result = this.getPersonInfo(map);
				} catch (ServiceException se) {
					LogUtil.error(se);
					errMap.put("errorMsg", se.getMessage());
				}
			}
		} else {
			errMap.put("errorMsg",
					"Parameter [param]'s value is null or empty.");
		}

		if (errMap != null && !errMap.isEmpty()
				&& errMap.containsKey("errorMsg")) {
			result = JsonUtil.getJsonString(errMap);
		}
		return result;		
	}
}
