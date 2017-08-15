package com.zzb.conf.service.impl;

import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.dao.impl.INSBServiceUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.conf.dao.INSBBusinessmanagegroupDao;
import com.zzb.conf.dao.INSBGroupmembersDao;
import com.zzb.conf.dao.INSBGrouptasksetDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBRuleBaseDao;
import com.zzb.conf.dao.INSBTasksetDao;
import com.zzb.conf.dao.INSBTasksetrulebseDao;
import com.zzb.conf.dao.INSBTasksetscopeDao;
import com.zzb.conf.entity.INSBBusinessmanagegroup;
import com.zzb.conf.entity.INSBGrouptaskset;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRuleBase;
import com.zzb.conf.entity.INSBTaskset;
import com.zzb.conf.entity.INSBTasksetrulebse;
import com.zzb.conf.entity.INSBTasksetscope;
import com.zzb.conf.service.INSBTasksetService;

@Service
@Transactional
public class INSBTasksetServiceImpl extends BaseServiceImpl<INSBTaskset>
		implements INSBTasksetService {
    public static final String ONLINE_USERS = "online_users";
    @Resource
	private INSBTasksetDao dao;
	@Resource
	private INSBBusinessmanagegroupDao businessmanagegroupDao;
	@Resource
	private INSBGrouptasksetDao grouptasksetDao;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSBTasksetrulebseDao tasksetrulebseDao;
	@Resource
	private INSBRuleBaseDao ruleBaseDao;
	@Resource
	private INSBServiceUtil serviceUtil;
	@Resource
	private INSCCodeDao codeDao;
	@Resource
	private INSBProviderDao providerDao;
	@Resource
	private INSBTasksetscopeDao tasksetscopeDao;
	@Resource
	private INSBQuoteinfoDao quoteinfoDao;
	@Resource
	private INSBQuotetotalinfoDao quotetotalinfoDao;
	@Resource
	private INSBGroupmembersDao groupmembersDao;
	@Resource
	private INSCUserDao userDao;

	@Resource
	private IRedisClient redisClient;

	@Override
	protected BaseDao<INSBTaskset> getBaseDao() {
		return dao;
	}

	@Override
	public Map<String, Object> queryByParamPage(String tasksetkind,
			String tasksetname, String setstatus, Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();

		int taskSetKind = 0;
		if (tasksetkind != null && !"".equals(tasksetkind)) {
			taskSetKind = Integer.parseInt(tasksetkind);
		}

		switch (taskSetKind) {
		case 0:
			break;
		case 1:
			// 任务组名称
			map.put("setname", tasksetname);
			break;

		case 2:
			// 任务组id
			map.put("tasksetcode", tasksetname);

			break;
		case 3:
			// 任务规则名称
			map.put("rulename", tasksetname);

			break;
		case 4:
			// 任务规则描述
			map.put("rulepostil", tasksetname);

			break;
		case 5:
			// 业管组名称---查询关系表
			map.put("groupname", tasksetname);

			break;
		case 6:
			// 业管组ID
			map.put("groupcode", tasksetname);

			break;
		default:
			break;
		}

		// taskset sql
		if (taskSetKind == 0 || taskSetKind == 1 || taskSetKind == 2) {
			List<INSBTaskset> taskSetList = dao.selectListByPage(map);
			for (INSBTaskset model : taskSetList) {

				// 群组名称
				String tempGroupName = "";
				List<INSBGrouptaskset> gtsList = grouptasksetDao
						.selectByTaskSetId(model.getId());
				if (gtsList != null && !gtsList.isEmpty()) {
					for (INSBGrouptaskset model1 : gtsList) {
						tempGroupName = tempGroupName + model1.getGroupname()
								+ "<br/>";
					}
					model.setGroupname(tempGroupName.substring(0,
							tempGroupName.length() - 1));
				}

				// 规则名称
				String tempRuleName = "";
				List<INSBTasksetrulebse> tuList = tasksetrulebseDao
						.selectByTaskSetId(model.getId());
				if (tuList != null && !tuList.isEmpty()) {
					for (INSBTasksetrulebse model2 : tuList) {
						INSBRuleBase ruleModel = ruleBaseDao.selectById(model2
								.getRulebaseid());

						tempRuleName = tempRuleName + ruleModel.getRuleName()
								+ "<br/>";
					}
					model.setRulebasename(tempRuleName);
				}

				if (1 == model.getSetstatus()) {
					model.setSetstatusstr("启用");
				} else {
					model.setSetstatusstr("停用");
				}
			}
			result.put("total", dao.selectListCountByPage(map));
			result.put("rows", taskSetList);
			return result;

			// 查询任务组规则关系表
		} else if (taskSetKind == 3 || taskSetKind == 4) {

			return getDataFromTasksetRulebase(map);

			// 查询任务组群组关系表
		} else {
			List<INSBGrouptaskset> list = grouptasksetDao
					.selectPageByParam(map);
			for (INSBGrouptaskset model : list) {
				if (1 == model.getSetstatus()) {
					model.setSetstatusstr("启用");
				} else {
					model.setSetstatusstr("停用");
				}
			}
			result.put("total", grouptasksetDao.selectPageCountByParam(map));
			result.put("rows", list);
			return result;
		}

	}

	/**
	 * 查询任务组规则关系表 得到分页数据
	 * 
	 * 1：大方向查询。得到任务规则关系表中 任务组id 不进行分页 2：细化查询。根据任务id进行细化查询 需要进行分页
	 * 
	 * @param map
	 *            查询分页参数 任务规则名称，任务描述
	 * @return
	 */
	private Map<String, Object> getDataFromTasksetRulebase(
			Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 得到任务组id list 直接作为条件进行任务组分页查询
		List<String> TasksetIdlist = tasksetrulebseDao
				.selectListPage4Taskset(map);

		map.put("listId", TasksetIdlist);

		result.put("total", grouptasksetDao.selectPageCountByParam(map));
		// result.put("rows", list);
		return result;
	}

	@Override
	public void saveOrUpdate(INSCUser user, INSBTaskset set) {
		// 修改
		if (set.getId() != null && !"".equals(set.getId())) {
			dao.updateById(set);
		} else {
			set.setCreatetime(new Date());
			set.setOperator(user.getUsercode());
			dao.insert(set);
		}
	}

	@Override
	public void deleteByIds(String ids) {
		String[] idArray = null;
		try {
			idArray = ids.split(",");
		} catch (Exception e) {
			idArray[0] = ids;
			e.printStackTrace();
		}
		for (String id : idArray) {
			dao.deleteById(id);
		}
	}

	@Override
	public void updateStatusByIds(String ids) {
		String[] idArray = null;
		try {
			idArray = ids.split(",");
		} catch (Exception e) {
			idArray[0] = ids;
			e.printStackTrace();
		}
		for (String id : idArray) {
			INSBTaskset model = dao.selectById(id);
			if (model.getSetstatus() == 1) {
				model.setSetstatus(2);
			} else {
				model.setSetstatus(1);
			}
			dao.updateById(model);
		}
	}

	@Override
	public void saveTasksetGroup(INSCUser user, String groupIds,
			String tasksetId) {
		List<String> newPrivilegeList = new ArrayList<String>();
		List<String> oldPrivilegeList = new ArrayList<String>();

		String[] groupIdArray = null;
		try {
			groupIdArray = groupIds.split(",");
		} catch (Exception e) {
			groupIdArray[0] = groupIds;
			e.printStackTrace();
		}

		// 查出以前所有业管组
		List<INSBGrouptaskset> tasksetGroupList = grouptasksetDao
				.selectByTaskSetId(tasksetId);
		for (INSBGrouptaskset model : tasksetGroupList) {
			oldPrivilegeList.add(model.getGroupid());
		}

		// 得到新业管组
		for (String groupid : groupIdArray) {
			if (!"".equals(groupid)) {
				newPrivilegeList.add(groupid);
			}

		}
		Map<String, List<String>> resultList = serviceUtil.updateUtil(
				newPrivilegeList, oldPrivilegeList);

		// 新增关系表数据
		List<String> groupAddId = resultList.get("add");
		// 批量新增
		List<INSBGrouptaskset> addList = new ArrayList<INSBGrouptaskset>();
		for (String gid : groupAddId) {
			INSBBusinessmanagegroup model = businessmanagegroupDao
					.selectById(gid);
			INSBGrouptaskset gtsModel = new INSBGrouptaskset();
			gtsModel.setCreatetime(new Date());
			gtsModel.setGroupcode(model.getGroupcode());
			gtsModel.setGroupid(gid);
			gtsModel.setGroupname(model.getGroupname());
			gtsModel.setTasksetid(tasksetId);
			gtsModel.setOperator(user.getUsercode());
			addList.add(gtsModel);
			// grouptasksetDao.insert(gtsModel);
		}
		grouptasksetDao.insertInBatch(addList);

		// 删除关系表数据
		List<String> deleteIds = resultList.get("delete");
		for (String id : deleteIds) {
			grouptasksetDao.deleteByGroupId(id);
		}

	}

	@Override
	public Map<String, Object> queryData2groupList(String tasksetId) {
		String groupIds = "";
		Map<String, Object> result = new HashMap<String, Object>();
		INSBTaskset tasksetModel = dao.selectById(tasksetId);
		List<INSBGrouptaskset> groupList = grouptasksetDao
				.selectByTaskSetId(tasksetId);
		if (groupList != null && !groupList.isEmpty()) {
			for (INSBGrouptaskset model : groupList) {
				groupIds = groupIds + model.getGroupid() + ",";
			}
			result.put("groupIds", groupIds.substring(0, groupIds.length() - 1));
		} else {
			result.put("groupIds", "");
		}

		result.put("model", tasksetModel);

		return result;
	}

	@Override
	public List<INSBTaskset> findListByDeptId(String deptid) {
		List<INSBTaskset> list = null;
		if (StringUtils.isNotBlank(deptid)) {
			list = dao.selectListByDeptId(deptid);
		}
		return list;
	}

	@Override
	public Map<String, Object> queryData2ruleList(String tasksetId) {
		String rulebaseIds = "";
		Map<String, Object> result = new HashMap<String, Object>();
		INSBTaskset tasksetModel = dao.selectById(tasksetId);
		List<INSBTasksetrulebse> tasksetList = tasksetrulebseDao
				.selectByTaskSetId(tasksetId);
		if (tasksetList != null && !tasksetList.isEmpty()) {
			for (INSBTasksetrulebse model : tasksetList) {
				rulebaseIds = rulebaseIds + model.getRulebaseid() + ",";
			}
			result.put("rulebaseIds",
					rulebaseIds.substring(0, rulebaseIds.length() - 1));
		} else {
			result.put("rulebaseIds", "");
		}

		result.put("model", tasksetModel);

		return result;
	}

	@Override
	public Map<String, Object> initMian2addData() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<INSCDept> deptList = deptDao.selectByParentDeptCode("");
		result.put("deptParentList", deptList);
		return result;
	}

	@Override
	public Map<String, Object> initMain2editData(String id) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 组织机构父下拉列表
		List<INSCDept> deptParentList = deptDao
				.selectByParentDeptCode("");
		// 得到选中的任务类型
		List<Map<String, Object>> oldTaskTypeList = new ArrayList<Map<String, Object>>();

		// 初始化任务类型
		List<Map<String, Object>> taskTypeList = codeDao
				.selectByType("tasktype");
		
		// 组织机构下拉列表
		List<INSCDept> deptList = new ArrayList<INSCDept>();
		
		// 初始化下拉选中值
		INSCDept deptModel = new INSCDept();
		
		// 基本信息
		INSBTaskset setModel = new INSBTaskset();

		if (id!=null) {
			// 基本信息
			setModel = dao.selectById(id);

			INSBProvider providerModel = providerDao.queryByPrvcode(setModel
					.getProviderid());
			String taskTypes = setModel.getTasktype();
			String[] tasktypeArray = null;
			if (taskTypes != null) {
				tasktypeArray = taskTypes.split(",");
			}

			// 初始化下拉选中值
			deptModel = deptDao.selectByComcode(setModel.getDeptid());

			// 组织机构下拉列表
			deptList = deptDao.selectByParentDeptCode(deptModel
					.getUpcomcode());

			if (tasktypeArray != null) {
				for (Map<String, Object> map : taskTypeList) {
					for (String tasktypevalue : tasktypeArray) {
						if (tasktypevalue.equals(map.get("codevalue"))) {
							oldTaskTypeList.add(map);
						}
					}
				}
			}
			if (providerModel != null) {
				result.put("providerName", providerModel.getPrvname());
			}
		}
		result.put("deptParentList", deptParentList);
		result.put("deptList", deptList);
		result.put("deptModel", deptModel);
		result.put("setModel", setModel);
		result.put("taskType", taskTypeList);
		result.put("oldtaskType", oldTaskTypeList);

		return result;
	}

	@Override
	public void saveTasksetRuleBase(INSCUser user, String ruleIds,
			String tasksetId) {

		List<String> newPrivilegeList = new ArrayList<String>();
		List<String> oldPrivilegeList = new ArrayList<String>();

		String[] ruleIdArray = null;
		try {
			ruleIdArray = ruleIds.split(",");
		} catch (Exception e) {
			ruleIdArray[0] = ruleIds;
			e.printStackTrace();
		}

		// 查出以前所有规则
		List<INSBTasksetrulebse> tasksetRuleBaseList = tasksetrulebseDao
				.selectByTaskSetId(tasksetId);
		for (INSBTasksetrulebse model : tasksetRuleBaseList) {
			oldPrivilegeList.add(model.getRulebaseid());
		}

		// 当前选中规则
		for (String ruleid : ruleIdArray) {
			if (!"".equals(ruleid)) {
				newPrivilegeList.add(ruleid);
			}
		}

		Map<String, List<String>> resultList = serviceUtil.updateUtil(
				newPrivilegeList, oldPrivilegeList);

		// 新增关系表数据
		List<String> ruleAddId = resultList.get("add");
		// 批量新增
		List<INSBTasksetrulebse> addList = new ArrayList<INSBTasksetrulebse>();
		for (String ruleid : ruleAddId) {

			INSBTasksetrulebse model = new INSBTasksetrulebse();
			model.setRulebaseid(ruleid);
			model.setTasksetid(tasksetId);
			model.setCreatetime(new Date());
			model.setOperator(user.getUsercode());
			addList.add(model);
			// tasksetrulebseDao.insert(model);
		}
		tasksetrulebseDao.insertInBatch(addList);

		// 删除关系表数据
		List<String> deleteIds = resultList.get("delete");
		for (String id : deleteIds) {
			tasksetrulebseDao.deleteUnionByRuleId(id);
		}

	}

	@Override
	public Map<String, String> selectOnlineUser4WorkFlow(
			Map<String, String> param) {

		Map<String, String> result = new HashMap<String, String>();

		// //拿到当前任务
		// Task task = new Task();
		// task.setProInstanceId(param.get(""));
		// task.setPrvcode(param.get(""));
		// task.setCreatetime(new Date());
		//

		// 通过也管组 和当前在线用户得到当前能够处理任务业管 TODO
		// result.put("usercode", task.getDispatch().getUser().toString());

		List<String> onlineUserCodes = new ArrayList<String>();
		String onlineUsers = (String) redisClient.get(Constants.CM_ZZB, ONLINE_USERS);
		@SuppressWarnings("unchecked")
		Map<String, Object> aa = JSONObject.fromObject(onlineUsers);
		for (String usercode : aa.keySet()) {
			onlineUserCodes.add(usercode);
		}

		result.put("usercode", onlineUserCodes.get(0));
		return result;
	}

	@Override
	public List<Map<String, String>> getProviderTreeList(String id,
			String parentcode, String providerId, String checked) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (id != null && id != "") {
			INSBTaskset setModel = dao.selectById(id);
			if (providerId != null && providerId != "") {
				setModel.setProviderid(providerId);
				dao.updateById(setModel);
			} else {
				providerId = setModel.getProviderid();
			}
		}
		// 存储关系表中所有供应商id

		if (parentcode == null) {
			parentcode = "";
		}
		List<INSBProvider> insbListPro = providerDao
				.selectByParentProTreeCode(parentcode);

		for (INSBProvider pro : insbListPro) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", pro.getPrvcode());
			map.put("prvId", pro.getId());
			map.put("pId", pro.getParentcode());
			map.put("name", pro.getPrvname());
			map.put("isParent", "1".equals(pro.getChildflag()) ? "true"
					: "false");

			// 关系表中id 等于当前初始化id
			if (providerId != null) {
				if (providerId.equals(pro.getId())) {
					map.put("checked", "true");
					map.put("openflag", "1");
				} else {
					map.put("openflag", "0");
				}
			}
			list.add(map);
		}
		return list;
	}

	@Override
	public List<Map<String, String>> getdeptbypid(String pid) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		List<INSCDept> tempresult = deptDao.selectByParentDeptCode(pid);
		for (INSCDept model : tempresult) {
			Map<String, String> temp = new HashMap<String, String>();
			temp.put("code", model.getComcode());
			temp.put("name", model.getComname());
			result.add(temp);
		}
		return result;
	}

	@Override
	public void saveTaskSetScop(INSCUser user, String tsksetId, String deptId) {

		INSBTasksetscope taskscopModel = new INSBTasksetscope();
		taskscopModel.setDeptid(deptId);
		taskscopModel.setTaksetid(tsksetId);
		taskscopModel.setOperator(user.getUsercode());
		taskscopModel.setCreatetime(new Date());

		tasksetscopeDao.insert(taskscopModel);
	}

	@Override
	public Map<String, Object> getScopListByTaskSetId(Map<String, Object> map,
			String tasksetId) {

		Map<String, Object> result = new HashMap<String, Object>();

		map.put("taksetid", tasksetId);

		List<Map<String, Object>> rows = tasksetscopeDao
				.selectScopListByTaskSetId(map);
		long total = tasksetscopeDao.selectScopListCountByTaskSetId(tasksetId);
		result.put("total", total);
		result.put("rows", rows);

		return result;
	}

	@Override
	public void removeTaskSetScop(String deptIds) {
		String[] deptIdArray = null;
		try {
			deptIdArray = deptIds.split(",");
		} catch (Exception e) {
			deptIdArray[0] = deptIds;
			e.printStackTrace();
		}
		tasksetscopeDao.deleteBatchByDeptIds(deptIdArray);
	}

	
	/**
	 * @param providerId 供应商
	 * @param taskType 任务类型
	 * @return 业管信息
	 */
	private Map<String,String> getUserData(String deptId,String taskType) {
		Set<String> uiserCodes = new HashSet<String>();
		
		Map<String,String>  result=new HashMap<String,String>();
		
		//存储符合条件所有业管
		List<String> users = new ArrayList<String>();
		
		//通过网点得到任务组
		List<String> taskSetIds =  tasksetscopeDao.selectTaskSetIdByDeptId(deptId);
		
		List<String> taskSetIdList = dao.selectIdByTaskSetType(taskType);
		taskSetIds.retainAll(taskSetIdList);
		
		
		//当前任务组不为空
		if (taskSetIds != null&&!taskSetIds.isEmpty()) {
			for(String tasksetid:taskSetIds){
				
				//得到群组
				List<INSBGrouptaskset> gtasksetList = grouptasksetDao.selectByTaskSetId(tasksetid);
				if (gtasksetList != null&&!gtasksetList.isEmpty()) {
					for (INSBGrouptaskset gtaskset : gtasksetList) {
						List<String> temp = groupmembersDao.selectUserCodeByGroupId(gtaskset.getGroupid());
						users.addAll(temp);
						
						Set<String> userIds = new HashSet<String>();
						for(String code:temp){
							userIds.add(code);
						}
						
						uiserCodes.addAll(userIds);
					}
				}
			}
				//得到在线用户全部信息 
				Map<String,Map<String,String>> onLineUser = getOnlineUsers();
				for(String userCode :users){
					if(onLineUser.get(userCode)!=null){
						result = onLineUser.get(userCode);
						break;
					}
				}
		}
		return result;
	}

	private Map<String,Map<String,String>> getOnlineUsers() {
		Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
		List<String> onlineUserCodes = new ArrayList<String>();
		String onlineUserStr = (String) redisClient.get(Constants.CM_ZZB, ONLINE_USERS);
		Map<String, Object> tempMap = JSONObject.fromObject(onlineUserStr);
		
		for (String usercode : tempMap.keySet()) {
			Map<String, String> userData = JSONObject.fromObject(tempMap.get(usercode));
			result.put(usercode, userData);
		}
		return result;
	}

	@Override
	public Map<String,String> getRenewalUserData(String providerId) {
		return getUserData(providerId,"f");
	}

	@Override
	public Map<String,String> getPlatformData(String providerId) {
		return getUserData(providerId,"e");
	}
}