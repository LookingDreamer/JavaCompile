package com.zzb.conf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.dao.INSBRuleBaseDao;
import com.zzb.conf.dao.INSBRuleEngineDao;
import com.zzb.conf.dao.INSBTasksetrulebseDao;
import com.zzb.conf.dao.impl.INSBRuleEngineDaoImpl;
import com.zzb.conf.entity.INSBRuleBase;
import com.zzb.conf.entity.INSBTasksetrulebse;
import com.zzb.conf.service.INSBBusinessmanagegroupService;
import com.zzb.conf.service.INSBRulebseService;
import com.zzb.conf.service.INSBTasksetService;

@Service
@Transactional
public class INSBRulebseServiceImpl implements INSBRulebseService {

	@Resource
	private INSBRuleBaseDao dao;

	@Resource
	private INSBBusinessmanagegroupService businessmanagegroupService;

	@Resource
	private INSBTasksetService tasksetService;

	@Resource
	private INSCDeptDao deptDao;

	@Resource
	private INSBTasksetrulebseDao tasksetrulebseDao;
	@Resource
	private INSBRuleEngineDao insbRuleEngineDao;
	
	@Override
	public Map<String, Object> getListPageByParam(int type,
			Map<String, Object> tempMap, Map<String, Object> map) {
		if (type == 1) {
			return getData1(tempMap, map);
		} else {
			return getData2(tempMap, map);
		}
	}

	/**
	 * 任务组分配规则部分 按条件查询
	 * 
	 * 参数简单
	 * 
	 * @param tempMap
	 * @param map
	 * @return
	 */
	private Map<String, Object> getData1(Map<String, Object> tempMap,
			Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();

		map.put("ruleName", tempMap.get("ruleName"));
		map.put("rulePostil", tempMap.get("rulePostil"));

		List<INSBRuleBase> rbList = dao.selectListPage(map);
		for (INSBRuleBase model : rbList) {
			String tempType = "";
			try {
				tempType = model.getRuleGroup().split("|")[0];
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ("dispatch".equals(tempType)) {
				model.setRuleType("调度规则");
			} else if ("weight".equals(tempType)) {
				model.setRuleType("权重规则");
			}

		}
		result.put("total", dao.selectListCountPage(map));
		result.put("rows", rbList);
		return result;
	}

	/**
	 * 规则模块按条件查询
	 * 
	 * 参数复杂
	 * 
	 * @param tempMap
	 * @param map
	 * @return
	 */
	private Map<String, Object> getData2(Map<String, Object> tempMap,
			Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 规则状态 默认为0
		int ruletype = (int) tempMap.get("ruletype");
		String deptId = (String) tempMap.get("deptId");
		int param2 = (int) tempMap.get("param2");
		String param3 = (String) tempMap.get("param3");
		int rulebaseStatus = (int) tempMap.get("rulebaseStatus");

		// 规则分类 权重/调度 默认值为0
		if (ruletype == 1) {
			map.put("ruleGroup", "dispatch");
		} else if (ruletype == 2) {
			map.put("ruleGroup", "weight");
		}

		// 组织机构 结构：10000.123333.222222.111111.0 默认值为空
		if (deptId != null && !"".equals(deptId)) {
			String[] deptIdArray = deptId.split("\\.");
			int count = 0;
			for (String id : deptIdArray) {
				if ("0".equals(id)) {
					count++;
				}
			}
			if (count < 5) {
				map.put("ruleDept", deptId);
			} else {
				deptId = null;
			}
		} else {
			deptId = null;
		}

		// 规则名称/规则描述
		if (param2 == 1) {
			map.put("ruleName", param3);
		} else if (param2 == 2) {
			map.put("rulePostil", param3);
		}

		// 查询已经关联所有规则信息
		List<String> rbList = tasksetrulebseDao.selectList4Rule();

		System.out.println(rbList);
		List<INSBRuleBase> resultList =null;
		// 已关联，从关系表中检索数据
		if (rulebaseStatus == 1) {
			map.put("ruleList", rbList);
			// 作为参数查询规则信息表 in查询
			map.put("p1", 1);
			
			// 未关联
		} else if (rulebaseStatus == 2) {
			map.put("ruleList", rbList);
			// 作为参数查询规则信息表 not in查询
			map.put("p1", 2);
		}
		resultList = dao.selectListPage(map);
		for (INSBRuleBase model : resultList) {
			for(String rid:rbList){
				if(Integer.parseInt(rid)==model.getId()){
					model.setRuleState("已关联");
					break;
				}else{
					model.setRuleState("未关联");
				}
			}
			String tempType = "";
			try {
				tempType = model.getRuleGroup().split("\\|")[0];
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ("dispatch".equals(tempType)) {
				model.setRuleType("调度规则");
			} else if ("weight".equals(tempType)) {
				model.setRuleType("权重规则");
			}
		}
		result.put("total", dao.selectListCountPage(map));
		result.put("rows", resultList);
		return result;
	}

	@Override
	public Map<String, Object> getParentDeptData() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<INSCDept> deptList = deptDao.selectByParentDeptCode("1000000000");
		result.put("deptParentList", deptList);
		return result;
	}

	@Override
	public Map<String, Object> initTasksetId(String ruleid) {
		String tasksetIds = "";
		Map<String, Object> result = new HashMap<String,Object>();
		List<INSBTasksetrulebse> tasksetIdList =  tasksetrulebseDao.selectByRuleId(ruleid);
		if(tasksetIdList!=null&&tasksetIdList.size()>=1){
			for(INSBTasksetrulebse model:tasksetIdList){
				tasksetIds = tasksetIds+model.getTasksetid()+",";
			}
			result.put("tasksetIds", tasksetIds.subSequence(0, tasksetIds.length()-1));
		}else{
			result.put("tasksetIds","");
		}
		return result;
	}
	/** 
	 * 初始化规则信息列表
	 * @see com.zzb.conf.service.INSBRulebseService#initRuleBase(java.util.Map)
	 */
	@Override
	public Map<String, Object> initRuleBase(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = dao.selectRuleBaseListPaging(data);
		map.put("total", dao.selectCount());
		map.put("rows", infoList);
		return map;
	}

	/** 
	 * 根据id查找规则表
	 * @see com.zzb.conf.service.INSBRulebseService#selectById(java.lang.String)
	 */
	@Override
	public INSBRuleBase selectById(String id) {
		return dao.selectById(id);
	}

	@Override
	public String selectByagreementrule(String agreementrule) {
		return insbRuleEngineDao.getAgreementrulename(agreementrule);
	}
}