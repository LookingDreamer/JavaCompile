package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.dao.impl.INSBServiceUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.common.HttpSender;
import com.zzb.conf.dao.INSBBusinessmanagegroupDao;
import com.zzb.conf.dao.INSBGroupdeptDao;
import com.zzb.conf.dao.INSBGroupmembersDao;
import com.zzb.conf.dao.INSBGroupprivilegDao;
import com.zzb.conf.dao.INSBGroupprovideDao;
import com.zzb.conf.dao.INSBGroupworktimeDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBTaskprivilegeDao;
import com.zzb.conf.entity.INSBBusinessmanagegroup;
import com.zzb.conf.entity.INSBGroupdept;
import com.zzb.conf.entity.INSBGroupmembers;
import com.zzb.conf.entity.INSBGroupprivileg;
import com.zzb.conf.entity.INSBGroupprovide;
import com.zzb.conf.entity.INSBGroupworktime;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBTaskprivilege;
import com.zzb.conf.service.INSBBusinessmanagegroupService;

@Service
@Transactional
public class INSBBusinessmanagegroupServiceImpl extends
		BaseServiceImpl<INSBBusinessmanagegroup> implements
		INSBBusinessmanagegroupService {
	@Resource
	private INSBBusinessmanagegroupDao businessmanagegroupDao;
	@Resource
	private INSBTaskprivilegeDao taskprivilegeDao;
	@Resource
	private INSBGroupprivilegDao groupprivilegDao;
	@Resource
	private INSBGroupprovideDao groupprovideDao;
	@Resource
	private INSBGroupdeptDao groupdeptDao;
	@Resource
	private INSBProviderDao providerDao;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSCCodeDao codeDao;
	@Resource
	private INSBGroupworktimeDao groupworktimeDao;
	@Resource
	private INSCUserDao userDao;
	@Resource
	private INSBGroupmembersDao groupmembersDao;
	@Resource
	private INSBServiceUtil serviceUtil;
	@Resource
	private INSCUserDao inscUserDao;
	
	
	
	private static String DISPATCH_HOST = "";
	static {
		// 读取相关的配置  
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("config/config");
		DISPATCH_HOST = resourceBundle.getString("dispatch.hostName");
	}
	@Override
	protected BaseDao<INSBBusinessmanagegroup> getBaseDao() {
		return businessmanagegroupDao;
	}

	@Override
	public Map<String, Object> queryByParamPage(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<INSBBusinessmanagegroup> groupList = businessmanagegroupDao.selectListByPage(map);
		
		for(INSBBusinessmanagegroup model:groupList){
			if(model.getPrivilegestate()==null){
				model.setPrivilegestatestr("关闭");
			}else if(1==model.getPrivilegestate()){
				model.setPrivilegestatestr("开启");
			}else if(2==model.getPrivilegestate()){
				model.setPrivilegestatestr("关闭");
			}
		}
		result.put("total", businessmanagegroupDao.selectCountByParam(map));
		result.put("rows", groupList);

		return result;
	}

	@Override
	public List<INSBTaskprivilege> queryTreeByPrivilegePcode(String pcode) {
		// 1：查询关系表得到权限树根节点
		List<INSBTaskprivilege> resultList = new ArrayList<INSBTaskprivilege>();
		resultList = getPrivalegTreeDataByPCode(pcode, resultList);
		// 2:循环得到所有节点
		return resultList;
	}

	/**
	 * 通过父节点得到所有数据
	 * 
	 * @param pCode
	 * @return
	 */
	private List<INSBTaskprivilege> getPrivalegTreeDataByPCode(String pcode,
			List<INSBTaskprivilege> resultList) {
		List<INSBTaskprivilege> privalegList = taskprivilegeDao
				.selectByPCode(pcode);
		if (privalegList != null) {
			for (INSBTaskprivilege model : privalegList) {
				resultList.add(model);
				getPrivalegTreeDataByPCode(model.getCode(), resultList);
			}
		}
		return resultList;
	}

	@Override
	public Map<String, Object> common2detail(String groupName, String pid,
			String groupKind) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 初始化业管组所属平台下拉框 upcomcode=1000000000
		List<INSCDept> deptList = deptDao.selectByParentDeptCode("");

		String detailPath = "zzbconf/groupmrg-detailadd";

		String groupKindStr = "";
		
		
		// 从code表中查出群组类型数据
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("codetype", "grouptype");
		List<INSCCode> codeList = codeDao.selectINSCCodeByCode(paramMap);
		for (INSCCode model : codeList) {
			if (groupKind.equals(model.getCodevalue())) {
				groupKindStr = model.getCodename();
				break;
			}
		}

		// 查询上级群组
		INSBBusinessmanagegroup bmgModelTemp = businessmanagegroupDao
				.selectById(pid);

		Map<String, String> map = new HashMap<String, String>();
		map.put("groupName", groupName);
		map.put("pid", pid);
		map.put("pgroupName", bmgModelTemp.getGroupname());
		map.put("groupKind", groupKind);
		map.put("groupKindStr", groupKindStr);

		Map<String, List<Map<String, Object>>> tempMap = getPrivilegeData(groupKind);
		
		//初始化任务类型
		List<Map<String, Object>> taskTypeList = codeDao.selectByType("tasktype");

		result.put("comm", map);
		result.put("path", detailPath);
		result.put("deptParentList", deptList);
		result.put("taskType", taskTypeList);
		result.putAll(tempMap);
		return result;
	}

	/**
	 * 通过群组类型得到权限树
	 * 
	 * @param groupKind
	 * @return
	 */
	private Map<String, List<Map<String, Object>>> getPrivilegeData(
			String groupKind) {
		Map<String, List<Map<String, Object>>> resultMap = new HashMap<String, List<Map<String, Object>>>();
		List<INSBTaskprivilege> tpList = this
				.queryTreeByPrivilegePcode(groupKind);
		List<Map<String, Object>> level1Data = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> level2Data = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> level3Data = new ArrayList<Map<String, Object>>();

		for (INSBTaskprivilege model : tpList) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			if (model.getLevel() == 1) {
				// 查询当前一级节点下有多少二级节点
				List<INSBTaskprivilege> temList = taskprivilegeDao
						.selectByPCode(model.getCode());

				tempMap.put("code", model.getCode());
				tempMap.put("pcode", model.getPcode());
				tempMap.put("name", model.getName());
				tempMap.put("rows", temList.size() + "");
				level1Data.add(tempMap);
			} else if (model.getLevel() == 2) {

				tempMap.put("code", model.getCode());
				tempMap.put("pcode", model.getPcode());
				tempMap.put("name", model.getName());
				level2Data.add(tempMap);
			} else if (model.getLevel() == 3) {
				tempMap.put("code", model.getCode());
				tempMap.put("pcode", model.getPcode());
				tempMap.put("name", model.getName());
				tempMap.put("orderD3", model.getorderflag() - 1);
				level3Data.add(tempMap);
			}
		}
		resultMap.put("data1", level1Data);
		resultMap.put("data2", level2Data);
		resultMap.put("data3", level3Data);
		return resultMap;
	}

	@Override
	public List<Map<String, String>> queryTreeByPcode(String parentcode) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		if (parentcode!=null&&!"1200000000".equals(parentcode)) {
			return list;
		}else {
			List<INSCDept> inscListDept = queryDeptListByPid(parentcode);
			for(INSCDept dept : inscListDept){
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", dept.getId());
				map.put("pId", dept.getUpcomcode());
				map.put("name", dept.getComname());
				map.put("isParent", "1".equals(dept.getChildflag())? "true" : "false");
				list.add(map);
			}
			return list;
		}
	}

	private List<INSCDept> queryDeptListByPid(String parentcode){
		if(StringUtil.isEmpty(parentcode) || "source".equalsIgnoreCase(parentcode)){
			parentcode = "";
		}
		return deptDao.selectByParentDeptCode(parentcode);
	}
	@Override
	public void saveGroupData(INSCUser user, INSBBusinessmanagegroup gropData,
			String[] pcodes, String deptids, String providerIds,
			Map<String, Object> pramMap) {

		if (gropData.getId() == null || "".equals(gropData.getId())) {
			saveGroup(user, gropData, pcodes, deptids, providerIds, pramMap);
		} else {
			updateGroupData(user, gropData, pcodes, deptids, providerIds,
					pramMap);
		}
	}

	/**
	 * 新增
	 * 
	 * @param gropData
	 * @param pcodes
	 * @param deptids
	 * @param providerIds
	 * @return
	 */
	private void saveGroup(INSCUser user, INSBBusinessmanagegroup gropData,
			String[] pcodes, String deptids, String providerIds,
			Map<String, Object> pramMap) {
		// 群组基本信息
		String groupId = null;
		String[] providerArray = null;
		String[] deptArray = null;
		try {
			gropData.setCreatetime(new Date());
			gropData.setOperator(user.getUsercode());
			gropData.setGroupnum(0);
			groupId = businessmanagegroupDao.insertReturnId(gropData);
			providerArray = providerIds.split(",");
			deptArray = deptids.split(",");
		} catch (Exception e) {
			if (!"".equals(providerIds) && providerIds != null) {
				providerArray[0] = providerIds;
			}
			if (!"".equals(deptids) && deptids != null) {
				deptArray[0] = deptids;
			}
			e.printStackTrace();
		}

		// 是否是业管组
		if ("c0004".equals(gropData.getGroupkind())) {
			// 取得时间
			String[] startworkdate = (String[]) pramMap.get("startworkdate");
			String[] endworkdate = (String[]) pramMap.get("endworkdate");
			String[] startworktime = (String[]) pramMap.get("startworktime");
			String[] endworktime = (String[]) pramMap.get("endworktime");

			// 插入时间关系表

			// 批量新增
			List<INSBGroupworktime> addList = new ArrayList<INSBGroupworktime>();
			for (int i = 0; i < startworkdate.length; i++) {
				INSBGroupworktime gwtModel = new INSBGroupworktime();
				gwtModel.setCreatetime(new Date());
				gwtModel.setOperator("1");
				gwtModel.setStartworkdate(startworkdate[i]);
				gwtModel.setEndworkdate(endworkdate[i]);
				gwtModel.setStartworktime(startworktime[i]);
				gwtModel.setEndworktime(endworktime[i]);

				addList.add(gwtModel);
				// groupworktimeDao.insert(gwtModel);
			}
			groupworktimeDao.insertInBatch(addList);

		}

		if (pcodes != null) {
			// 群组权限关系表

			// 批量新增
			List<INSBGroupprivileg> addList = new ArrayList<INSBGroupprivileg>();
			for (int i = 0; i < pcodes.length; i++) {
				INSBGroupprivileg gpModel = new INSBGroupprivileg();
				gpModel.setPrivilegcode(pcodes[i]);
				gpModel.setGroupid(groupId);
				gpModel.setCreatetime(new Date());
				gpModel.setOperator(user.getUsercode());
				gpModel.setPrivilegestate(1);

				addList.add(gpModel);
				// groupprivilegDao.insert(gpModel);
			}
			groupprivilegDao.insertInBatch(addList);
		}

		// 群组供应商关系表
		// 批量新增
		List<INSBGroupprovide> addList = new ArrayList<INSBGroupprovide>();
		for (String providerId : providerArray) {
			INSBGroupprovide gpdModel = new INSBGroupprovide();
			gpdModel.setProvideid(providerId);
			gpdModel.setGroupid(groupId);
			gpdModel.setCreatetime(new Date());
			gpdModel.setOperator("1");
			addList.add(gpdModel);
			// groupprovideDao.insert(gpdModel);
		}
		groupprovideDao.insertInBatch(addList);

		// 群组机构关系表
		// 批量新增
		List<INSBGroupdept> addDeptList = new ArrayList<INSBGroupdept>();
		if (deptArray != null) {
			for (String deptId : deptArray) {
				INSBGroupdept gdModel = new INSBGroupdept();
				gdModel.setDeptid(deptId);
				gdModel.setGroupid(groupId);
				gdModel.setCreatetime(new Date());
				gdModel.setOperator("1");
				addDeptList.add(gdModel);
				// groupdeptDao.insert(gdModel);
			}
			groupdeptDao.insertInBatch(addDeptList);
		}

	}

	/**
	 * 
	 * 修改群组信息
	 * 
	 * @param gropData
	 * @param pcodes
	 * @param deptids
	 * @param providerIds
	 * @return
	 */
	private void updateGroupData(INSCUser user,
			INSBBusinessmanagegroup gropData, String[] pcodes, String deptids,
			String providerIds, Map<String, Object> pramMap) {
		String groupId = gropData.getId();

		// 修改群组基本信息
		gropData.setModifytime(new Date());
		gropData.setOperator(user.getUsercode());
		businessmanagegroupDao.updateById(gropData);

		// 当前供应商信息
		String[] providerArray = null;
		// 当前机构信息
		String[] deptArray = null;
		try {
			providerArray = providerIds.split(",");
			deptArray = deptids.split(",");
		} catch (Exception e) {
			providerArray[0] = providerIds;
			deptArray[0] = deptids;
			e.printStackTrace();
		}

		// 更新群组供应商关系表
		updateGroupProvider(user, groupId, providerArray);

		// 更新群组权限关系表
		updateGroupPrivilege(user, groupId, pcodes);

		// 更新群组管理机构关系表
		updateGroupDepet(user, groupId, deptArray);

	}

	private void updateGroupDepet(INSCUser user, String groupId,
			String[] deptArray) {
		List<String> newDeptList = new ArrayList<String>();
		List<String> oldDeptList = new ArrayList<String>();

		// 当前机构id
		for (String deptId : deptArray) {
			newDeptList.add(deptId);
		}

		// 以前供应商id
		List<INSBGroupdept> tempDeptData = groupdeptDao
				.selectListByGruopId(groupId);
		for (INSBGroupdept model : tempDeptData) {
			oldDeptList.add(model.getDeptid());
		}
		Map<String, List<String>> resultDept = serviceUtil.updateUtil(
				newDeptList, oldDeptList);

		List<String> deleteDept = resultDept.get("delete");
		for (String id : deleteDept) {
			groupdeptDao.deleteByDeptId(id);
		}
		List<String> addDept = resultDept.get("add");
		// 批量新增
		List<INSBGroupdept> addListParam = new ArrayList<INSBGroupdept>();
		for (String id : addDept) {
			INSBGroupdept model = new INSBGroupdept();
			model.setDeptid(id);
			model.setGroupid(groupId);
			model.setCreatetime(new Date());
			model.setOperator(user.getUsercode());

			addListParam.add(model);
			// groupdeptDao.insert(model);
		}
		groupdeptDao.insertInBatch(addListParam);
	}

	/**
	 * 更新群组供应商关系表
	 */
	private void updateGroupProvider(INSCUser user, String groupId,
			String[] providerArray) {
		// 供应商更新
		List<String> newProviderList = new ArrayList<String>();
		List<String> oldProviderList = new ArrayList<String>();

		// 当前供应商id
		for (String providerId : providerArray) {
			newProviderList.add(providerId);
		}

		// 以前供应商id
		List<INSBGroupprovide> tempProviderData = groupprovideDao
				.selectListByGroupId(groupId);
		for (INSBGroupprovide model : tempProviderData) {
			oldProviderList.add(model.getProvideid());
		}

		Map<String, List<String>> resultProvider = serviceUtil.updateUtil(
				newProviderList, oldProviderList);

		List<String> deleteProvider = resultProvider.get("delete");
		for (String id : deleteProvider) {
			groupprovideDao.deleteByProviderId(id);
		}
		List<String> addProvider = resultProvider.get("add");
		// 批量新增
		List<INSBGroupprovide> addList = new ArrayList<INSBGroupprovide>();
		for (String id : addProvider) {
			INSBGroupprovide gpdModel = new INSBGroupprovide();
			gpdModel.setProvideid(id);
			gpdModel.setGroupid(groupId);
			gpdModel.setCreatetime(new Date());
			gpdModel.setOperator(user.getUsercode());
			// groupprovideDao.insert(gpdModel);
			addList.add(gpdModel);
		}
		groupprovideDao.insertInBatch(addList);
	}

	/**
	 * 更新群组权限关系表
	 */
	private void updateGroupPrivilege(INSCUser user, String groupId,
			String[] pcodes) {
		// 供应商更新
		List<String> newPrivilegeList = new ArrayList<String>();
		List<String> oldPrivilegeList = new ArrayList<String>();

		// 当前供应商id
		if (pcodes != null) {
			for (String privilegeCode : pcodes) {
				newPrivilegeList.add(privilegeCode);
			}
		}

		// 以前供应商id
		List<INSBGroupprivileg> tempPrivilegeData = groupprivilegDao
				.selectListByGruopId(groupId);
		for (INSBGroupprivileg model : tempPrivilegeData) {
			oldPrivilegeList.add(model.getPrivilegcode());
		}

		Map<String, List<String>> resultPrivilege = serviceUtil.updateUtil(
				newPrivilegeList, oldPrivilegeList);
		List<String> deletePrivilege = resultPrivilege.get("delete");
		for (String id : deletePrivilege) {
			groupprivilegDao.deleteByPrivilegeCodel(id);
		}

		List<String> addPrivilege = resultPrivilege.get("add");
		// 批量新增
		List<INSBGroupprivileg> addList = new ArrayList<INSBGroupprivileg>();
		for (String id : addPrivilege) {
			INSBGroupprivileg gpModel = new INSBGroupprivileg();
			gpModel.setPrivilegcode(id);
			gpModel.setGroupid(groupId);
			gpModel.setCreatetime(new Date());
			gpModel.setOperator(user.getUsercode());
			gpModel.setPrivilegestate(1);
			addList.add(gpModel);
			// groupprivilegDao.insert(gpModel);
		}
		groupprivilegDao.insertInBatch(addList);
	}

	@Override
	public Map<String, Object> updateGruopData(String id) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 1:群组基本信息
		INSBBusinessmanagegroup bgModel = businessmanagegroupDao.selectById(id);
		
		// 初始化任务类型
		List<Map<String, Object>> taskTypeList = codeDao.selectByType("tasktype");
		
		// 得到选中的任务类型
		List<Map<String, Object>> oldTaskTypeList = new ArrayList<Map<String, Object>>();

		
		String taskTypes = bgModel.getTasktype();
		String[] tasktypeArray = null;
		if (taskTypes != null) {
			tasktypeArray = taskTypes.split(",");
		}
		
		if (tasktypeArray != null) {
			for (Map<String, Object> map : taskTypeList) {
				for (String tasktypevalue : tasktypeArray) {
					if (tasktypevalue.equals(map.get("codevalue"))) {
						oldTaskTypeList.add(map);
					}
				}
			}
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();

		// 群组所属组织机构
		INSCDept deptModel = deptDao.selectByComcode(bgModel
				.getOrganizationid());

		// 组织机构下拉框初始化
		List<INSCDept> deptList = null;
		if(deptModel!=null){
			if (deptModel.getUpcomcode() != null) {
				deptList = deptDao.selectByParentDeptCode(deptModel.getUpcomcode());
			}
		}
		// 1.1：得到群组类型
		paramMap.put("codetype", "grouptype");
		List<INSCCode> codeList = codeDao.selectINSCCodeByCode(paramMap);
		for (INSCCode model : codeList) {
			if (model.getCodevalue().equals(bgModel.getGroupkind())) {
				bgModel.setGroupkindstr(model.getCodename());
			}
		}

		// 1.2：得到父群组名称
//		INSBBusinessmanagegroup parentModel = businessmanagegroupDao.selectById(bgModel.getPid());

		// 2:群组权限关系数据
		List<INSBGroupprivileg> groupPrivilegList = groupprivilegDao
				.selectListByGruopId(id);
		// 3:群组管理供应商信息
		List<INSBGroupprovide> groupProviderList = groupprovideDao
				.selectListByGroupId(id);
		// 4:群组管理机构信息
		List<INSBGroupdept> groupDeptList = groupdeptDao
				.selectListByGruopId(id);

		// 得到权限树
		Map<String, List<Map<String, Object>>> tempMap = getPrivilegeData(bgModel
				.getGroupkind());
		List<Map<String, Object>> tempPrivilegeList = tempMap.get("data3");
		for (Map<String, Object> map : tempPrivilegeList) {
			for (INSBGroupprivileg model : groupPrivilegList) {
				if (map.get("code").equals(model.getPrivilegcode())) {
					map.put("selected", "1");
					break;
				}
			}

		}

		// 得到供应商名称
		String providerNames = "";
		String providerIds = "";
		for (INSBGroupprovide model : groupProviderList) {
			INSBProvider pModel = providerDao.selectById(model.getProvideid());
			if (pModel != null) {
				providerNames = providerNames + pModel.getPrvname() + ",";
				providerIds = providerIds + model.getProvideid() + ",";
			}
		}

		// 得到管理机构名称
		String deptNames = "";
		String deptIds = "";
		for (INSBGroupdept model1 : groupDeptList) {
			INSCDept dModel = deptDao.selectById(model1.getDeptid());
			if (dModel != null) {
				deptNames = deptNames + dModel.getComname() + ",";
				deptIds = deptIds + model1.getDeptid() + ",";
			}

		}
		if(deptModel==null){
			result.put("groupDeptOrgName", null);
		}else {
			result.put("groupDeptOrgName", deptModel.getComname());
		}
//		result.put("pcomm", parentModel.getGroupname());
		result.put("comm", bgModel);
		result.putAll(tempMap);
		if (!"".equals(providerNames)) {
			result.put("providerNames",
					providerNames.substring(0, providerNames.length() - 1));
		} else {
			result.put("providerNames", providerNames);
		}

		if (!"".equals(deptNames)) {
			result.put("deptNames",
					deptNames.substring(0, deptNames.length() - 1));
		} else {
			result.put("deptNames", deptNames);
		}

		if (!"".equals(providerIds)) {
			result.put("providerIds",
					providerIds.substring(0, providerIds.length() - 1));
		} else {
			result.put("providerIds", providerIds);
		}

		if (!"".equals(deptIds)) {
			result.put("deptIds", deptIds.substring(0, deptIds.length() - 1));
		} else {
			result.put("deptIds", deptIds);
		}
		
		
		// 下拉框数据
		Map<String, String> groupType = new HashMap<String, String>();
		groupType.put("codetype", "grouptype");
		List<INSCCode> groupTypeList = codeDao.selectINSCCodeByCode(paramMap);

		
		result.put("groupType", groupTypeList);
		result.put("groupOrgData", deptModel);
		result.put("deptList", deptList);
		result.put("taskType", taskTypeList);
		result.put("oldtaskType", oldTaskTypeList);
		return result;
	}

	@Override
	public List<Map<String, String>> getProviderTreeList(String parentcode,
			String providerIds, String checked) {
		// 得到当前群组所有供应商
		String[] pids = null;
		try {
			pids = providerIds.split(",");
		} catch (Exception e) {
			pids[0] = providerIds;
			e.printStackTrace();
		}
		// 存储关系表中所有供应商id
		List<String> TempItemProviderIdsList = new ArrayList<String>();

		for (String ap : pids) {
			TempItemProviderIdsList.add(ap);
		}

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
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

			// 供应商关系表中所有的 id
			for (String providerId : pids) {

				// 关系表中id 等于当前初始化id
				if (providerId.equals(pro.getId())) {

					// 得到当前初始化id所有子节点
					List<INSBProvider> tempInsbListPro = providerDao
							.selectByParentProTreeCode(pro.getId());

					// 如果子节点不为空
					if (tempInsbListPro != null) {

						// 存储当前子节点子节点数据
						List<String> TempProviderIdsList = new ArrayList<String>();

						// 把子节点放到新list中
						for (INSBProvider pid : tempInsbListPro) {
							TempProviderIdsList.add(pid.getId());
						}
						// 存在 全选
						if (TempItemProviderIdsList
								.containsAll(TempProviderIdsList)) {
							map.put("checked", "true");
							map.put("openflag", "1");
							// 不全部存在 半选
						} else {
							map.put("checked", "true");
							map.put("halfCheck", "true");
							map.put("openflag", "0");
						}
					} else {
						map.put("checked", "true");
						map.put("openflag", "1");
					}
				} else {
					map.put("openflag", "0");
				}
			}
			list.add(map);
		}
		return list;
	}

	@Override
	public void deleteGroupBath(String ids) {

		String[] idArray = null;
		try {
			idArray = ids.split(",");
		} catch (Exception e1) {
			idArray[0] = ids;
			e1.printStackTrace();
		}
		for (String id : idArray) {
			// 1：群组业务权限关系表
			groupprivilegDao.deleteByGroupId(id);

			// 2：群组供应商关系表
			groupprovideDao.deleteByGroupId(id);

			// 3：群组管理机构关系表

			groupdeptDao.deleteByGroupId(id);
			
			// 4：群组基本信息
			businessmanagegroupDao.deleteById(id);
			
			// 5：群组人员关系表
			try {
				groupmembersDao.deleteByGroupId(id);
			} catch (Exception e) {
				LogUtil.warn("级联删除群组成员出错");
			}
		}

	}

	@Override
	public Map<String, Object> main2add() {
		Map<String, Object> result = new HashMap<String, Object>();

		// 下拉框数据
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("codetype", "grouptype");
		List<INSCCode> codeList = codeDao.selectINSCCodeByCode(paramMap);
		
		//初始化任务类型
		List<Map<String, Object>> taskTypeList = codeDao.selectByType("tasktype");
		
		INSBBusinessmanagegroup bgModel=new INSBBusinessmanagegroup();
		bgModel.setPrivilegestate(2);
		// 直属上级群组初始化 
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", "0");
		List<INSBBusinessmanagegroup> bmgList = businessmanagegroupDao
				.selectListByMap(map);
		
		result.put("comm", bgModel);
		result.put("taskType", taskTypeList);
		result.put("groupType", codeList);
		result.put("parentGroup", bmgList);
		return result;
	}

	@Override
	public Map<String, Object> getGroupMenberData(String groupId) {
		// 1：拿到群组信息,1.1：群组基本信息,1.2:群组关联关系
		Map<String, Object> groupMap = updateGruopData(groupId);

		INSBBusinessmanagegroup bgModel = businessmanagegroupDao
				.selectById(groupId);

		INSCDept deptModel = deptDao.selectByComcode(bgModel
				.getOrganizationid());
		if(deptModel!=null){
			INSCDept deptParentModel = deptDao.selectByComcode(deptModel.getUpcomcode());
			groupMap.put("groupDeptOrgParentName", deptParentModel.getComname());
			groupMap.put("groupDeptOrgName", deptModel.getComname());
			
		}else{
			groupMap.put("groupDeptOrgParentName", null);
			groupMap.put("groupDeptOrgName", null);
		}
		
		

		
		//
		return groupMap;
	}

	@Override
	public Map<String, Object> getGroupMemberList(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();

		List<INSBGroupmembers> gmList = groupmembersDao.selectPageByParam(map);
		for (INSBGroupmembers model : gmList) {
			if (1 == model.getGroupprivilege()) {
				model.setGroupprivilegestr("查看");
			} else if (2 == model.getGroupprivilege()) {
				model.setGroupprivilegestr("执行");
			}
		}
		result.put("total", groupmembersDao.selectPageCountByParam(map));
		result.put("rows", gmList);
		return result;
	}

	@Override
	public Map<String, Object> queryUsetListByGroupId(String groupId,
			Map<String, Object> map, String usercode, String name,String deptid) {

		Map<String, Object>  result = new HashMap<String,Object>();
		
		List<String> oldUser = new ArrayList<String>();
		List<INSBGroupmembers> memberList = groupmembersDao.selectByGroupId(groupId);
		for(INSBGroupmembers model:memberList){
			oldUser.add(model.getUsercode());
		}
		if(oldUser.isEmpty()){
			map.put("userList", null);
		}else{
			map.put("userList", oldUser);
		}
		map.put("name", name);
		map.put("usercode", usercode);
		map.put("deptid", deptid);
		
				
		result.put("total", userDao.selectUsersCountByDeptId(map));
		result.put("rows", userDao.selectUsersUseLike(map));
		return result;
	}

	@Override
	public void saveGroupUsers(String userIds, String groupId) {

		String[] idArray = null;
		try {
			idArray = userIds.split(",");
		} catch (Exception e) {
			idArray[0] = userIds;
			e.printStackTrace();
		}
		// 批量新增
		List<INSBGroupmembers> addList = new ArrayList<INSBGroupmembers>();
		for (String id : idArray) {
			INSBGroupmembers gmModel = new INSBGroupmembers();
			INSCUser user = userDao.selectById(id);
			gmModel.setGroupid(groupId);
			gmModel.setUserid(id);
			List<INSBGroupmembers> list=groupmembersDao.selectList(gmModel);
			if (list.size()==0) {
				gmModel.setCreatetime(new Date());
				gmModel.setOperator("1");
				gmModel.setUsercode(user.getUsercode());
				gmModel.setName(user.getName());
				gmModel.setUserorganization(user.getUserorganization());
				
				// 初始化群组成员权限
				gmModel.setGroupprivilege(2);
				
				addList.add(gmModel);
			}
			// groupmembersDao.insert(gmModel);
		}
		groupmembersDao.insertInBatch(addList);
		updateGroupUsersCount(groupId);
		notifyScheduler(userIds);
	}

	/**
	 * 更新群组成员数量
	 * 
	 * @param groupId
	 */
	private void updateGroupUsersCount(String groupId) {
		Map<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("groupId", groupId);

		List<INSBGroupmembers> countmembersList = groupmembersDao
				.selectByGroupId(groupId);
		int countmembers = countmembersList.size();

		INSBBusinessmanagegroup model = new INSBBusinessmanagegroup();
		model.setModifytime(new Date());
		model.setId(groupId);
		model.setGroupnum(countmembers);

		businessmanagegroupDao.updateGroupCount(model);
	}

	@Override
	public void removeGroupMember(String userIds, String groupId) {
		if (StringUtil.isEmpty(userIds) || StringUtil.isEmpty(groupId)) return;
		String[] userArray = userIds.split(",");
		for (String id : userArray) {
			if (StringUtil.isEmpty(id)) continue;
			groupmembersDao.deleteByUserId(id,groupId);
		}
		updateGroupUsersCount(groupId);
		notifyScheduler(userIds);
	}

	@Override
	public Map<String,Object> queryGroupPrivilegeByGroupId(String userId,String groupId) {
		Map<String,Object> result = new HashMap<String,Object>();
		
		Map<String,String> param = new HashMap<String,String>();
		param.put("groupid", groupId);
		param.put("userid", userId);
		Map<String,String> privalege = groupmembersDao.selectPrivilegeByGroupIdAndUserId(param);
		INSCUser user = userDao.selectById(userId);
		
		result.put("privalege", privalege);
		result.put("user", user);
		return result;
	}

	@Override  
	public void updatGroupMemberPrivilege(String id, String groupprivilege) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int privilege = Integer.parseInt(groupprivilege);
		paramMap.put("groupprivilege", privilege);
		paramMap.put("id", id);
		
		INSBGroupmembers insbGroupmembers = groupmembersDao.selectById(id);
		groupmembersDao.updataGroupPrivilegeById(paramMap);
		if(insbGroupmembers.getGroupprivilege() != privilege){
			//发给调度服务用户下线
			notifyScheduler(insbGroupmembers.getUserid());
//			String path = DISPATCH_HOST+"/worker/online";
//			String result;
//			try {
//				INSCUser inscUser = inscUserDao.selectById(insbGroupmembers.getUserid());
//				if(null!=inscUser){
//					Map<String, Object> worker = new HashMap<String,Object>();
//					List<String> usercodes = new ArrayList<String>();
//					usercodes.add(inscUser.getUsercode());
//					List<String> groups = groupmembersDao.selectGroupIdsByUserCodes4Login(usercodes);
//					worker.put("groups", groups);
//					worker.put("workerId", inscUser.getUsercode());
//					worker.put("workerName", inscUser.getUsercode());
//					worker.put("isLogin", "true");
//					LogUtil.info("\r**1===任务调度userLogoutForTask请求调度参数=" + worker);
//					result = HttpSender.doPost(path, JSONObject.fromObject(worker).toString());
//					LogUtil.info("\r**1===任务调度userLogoutForTask请求调度result="+result+"+workerId=" + inscUser.getUsercode());
//				}else{
//					LogUtil.info("\r**1===任务调度userLogoutForTask请求调度+userid=" + id);
//				}
//			} catch (Exception e) {
//				LogUtil.info("\r**2===任务调度userLogoutForTask请求调度异常失败userid=" + id, e);
//			}
		}
	}
	/**
	 * 通知调度系统
	 * @param userIds
	 */
	public void notifyScheduler(String userIds) {
		List<String> usercodes = new ArrayList<String>();
		String path = DISPATCH_HOST + "/worker/online";
		for (String userId : userIds.split(",")) {
			INSCUser inscUser = inscUserDao.selectById(userId);
			if (inscUser == null)
				continue;
			usercodes.removeAll(usercodes);//清空
			usercodes.add(inscUser.getUsercode());
			List<String> groups = groupmembersDao.selectGroupIdsByUserCodes4Login(usercodes);
			Map<String, Object> worker = new HashMap<String, Object>();
			worker.put("groups", groups);
			worker.put("workerId", inscUser.getUsercode());
			worker.put("workerName", inscUser.getUsercode());
			worker.put("maxTaskNum", inscUser.getTaskpool()>0?inscUser.getTaskpool():20);//默认20个为业管最大任务数
			LogUtil.info("业管权限调度通知参数=" + worker);
			if(null!=inscUser&&null!=inscUser.getOnlinestatus()&&"1".equals(inscUser.getOnlinestatus().toString())){
				path = DISPATCH_HOST + "/worker/online";//在线的用户用在线的接口通知调度
				worker.put("isLogin", "true");
			}else{
				path = DISPATCH_HOST + "/worker/offline";//不在线的用户用下线通知的接口通知调度
				worker.put("isLogin", "false");
			}
			String result = "";
			try {
				result = HttpSender.doPost(path, JSONObject.fromObject(worker).toString());
			} catch (Exception e) {
				LogUtil.info("业管权限调度通知参数异常失败userid=" + userId, e);
			}
			LogUtil.info("业管权限调度通知参数结果=" + result + " usercode=" + inscUser.getUsercode());
		}
	}

	@Override
	public List<INSBBusinessmanagegroup> findByTasksetid(String tasksetid) {
		return businessmanagegroupDao.selectByTasksetid(tasksetid);
	}

	@Override
	public void saveBaseGroupData(INSCUser user,
			INSBBusinessmanagegroup gropData) {
		
//		gropData.setCreatetime(new Date());
		gropData.setOperator(user.getUsercode());
		//修改
		if(gropData.getId()!=null&&!"".equals(gropData.getId())){
			gropData.setGroupnum(groupmembersDao.selectByGroupId(gropData.getId()).size());
			if(!"1".equals(String.valueOf(gropData.getPrivilegestate()))){//如果不是生效的修改则通知调度次组内的成员已经没有相应权限
				LogUtil.info(gropData.getGroupname()+"修改，且不是生效的修改则通知调度次组内的成员已经没有相应权限，id="+gropData.getId());
				List<INSBGroupmembers> insbGroupmembers = groupmembersDao.selectByGroupId(gropData.getId());
				for(INSBGroupmembers member:insbGroupmembers){
					notifyScheduler(member.getUserid());
				}
			}
			gropData.setModifytime(new Date());
			businessmanagegroupDao.updateById(gropData);
		//新增	
		}else{
			gropData.setCreatetime(new Date());
			gropData.setGroupnum(0);
			businessmanagegroupDao.insert(gropData);
		}
	}

	@Override
	public Map<String, Object> getGroupDeptidList(Map<String, Object> map,String groupId) {
		Map<String, Object> result = new HashMap<String, Object>();

		List<INSBGroupdept> gdList = groupdeptDao.selectPageByParam(map);
		result.put("total", groupdeptDao.selectListByGruopId(groupId).size());
		result.put("rows", gdList);
		return result;
	}

	@Override
	public Map<String, Object> getGroupProviderList(Map<String, Object> map,String groupId) {
		Map<String, Object> result = new HashMap<String, Object>();

		List<INSBProvider> gdList = groupprovideDao.selectprovidePageByParam(map);
		result.put("total", groupprovideDao.selectListByGroupId(groupId).size());
		result.put("rows", gdList);
		return result;
	}

	@Override
	public void removeGroupProvide(String ids, String groupid) {
		String[] provideArray = null;
		try {
			provideArray = ids.split(",");
		} catch (Exception e) {
			provideArray[0] = ids;
			e.printStackTrace();
		}
		for (String id : provideArray) {
			groupprovideDao.deleteById(id);
		}
	}

	@Override
	public void removeGroupDept(String ids, String groupid) {
		String[] deptArray = null;
		try {
			deptArray = ids.split(",");
		} catch (Exception e) {
			deptArray[0] = ids;
			e.printStackTrace();
		}
		for (String id : deptArray) {
			groupdeptDao.deleteById(id);
		}
		
	}
	
	@Override
	public List<Map<String,String>> getMembersByMember(INSCUser member){
		//组织查询入参
//		List<String> usercodeList = new ArrayList<String>();
//		usercodeList.add(member.getUsercode());
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("usercode", member.getUsercode());//要匹配的usercodeList
		List<Map<String,String>> result = groupmembersDao.getUserInfoListByGroup(params);
		if(result==null || result.size()==0){
			Map<String,String> temp = new HashMap<String, String>();
			temp.put("usercode", member.getUsercode());
			temp.put("uname", member.getName());
			result.add(temp);
		}
		for(Map<String,String> map:result){
			if(StringUtil.isEmpty(map.get("usercode"))){
				result.remove(map);
			}
		}
		return result;
		
	}
	
	public List<Map<String, Object>> queryGroupProviderByDeptId(String deptId, String groupId,String checked){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<INSBProvider> providerList = new ArrayList<INSBProvider>();
		if(deptId.equals("1200000000")||deptId==null){
			providerList = groupdeptDao.getGroupProviderByGroup(groupId);
		}else{
			providerList = groupdeptDao.getGroupProviderByDept(deptId,groupId);
		}
		for(INSBProvider provider:providerList){
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("id", provider.getPrvcode());
			map.put("name", provider.getPrvname());
			map.put("isParent","false");
			map.put("pId", "1");
			//如果前端点了全选, 返回也要全选
			if(checked !=null && !"".equals(checked)){
				map.put("checked", true);
			};
			result.add(map);
		};
		return result;
	}
	
}