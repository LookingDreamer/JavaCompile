package com.zzb.cm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;
import com.common.redis.CMRedisClient;
import com.zzb.cm.dao.INSBMonitorDao;
import com.zzb.cm.entity.INSBMonitor;
import com.zzb.cm.service.INSBMonitorService;
import com.zzb.conf.entity.INSBEdiconfiguration;
import com.zzb.conf.entity.INSBElfconf;
import com.zzb.conf.service.INSBEdiconfigurationService;
import com.zzb.conf.service.INSBElfconfService;
import com.zzb.monitor.model.Monitormodel;

@Service
@Transactional
public class INSBMonitorServiceImpl extends BaseServiceImpl<INSBMonitor>
		implements INSBMonitorService {
	@Resource
	INSCDeptDao inscDeptDao;
	@Resource
	INSCDeptService inscDeptService;
	@Resource
	INSBMonitorDao insbMonitorDao;
	@Resource
	INSBElfconfService insbElfconfService;
	@Resource
	INSBEdiconfigurationService insbEdiconfigurationService;

	@Override
	protected BaseDao<INSBMonitor> getBaseDao() {
		return insbMonitorDao;
	}

	@Override
	public List<Map<String, String>> queryOrgInfo() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		INSCDept queryInscDept = new INSCDept();
		queryInscDept.setType("1");
		queryInscDept.setComgrade("01");// 平台级的
		List<INSCDept> insbListPro = inscDeptDao.selectList(queryInscDept);
		for (INSCDept pro : insbListPro) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", pro.getId());

			map.put("prvcode", pro.getComcode());
			map.put("pId", pro.getDeptinnercode());
			map.put("name", pro.getShortname());
			map.put("isParent", "false");
			list.add(map);
		}
		return list;
	}

	@Override
	public Map<String, Object> queryList(Map<String, Object> monitorModel) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<String> resultList = insbMonitorDao.queryList(monitorModel);
		List<String> resultAllList = insbMonitorDao.queryAllList(monitorModel);
		for (String tempKeyOne : resultAllList) {
			String CM_MONITOR_ROBOT = "newmonitor:robot:" + tempKeyOne + ":";
			String CM_MONITOR_ROBOT_ERRORCOUNT = CM_MONITOR_ROBOT
					+ "errorcount";
			String tempErrorcount = String.valueOf(CMRedisClient.getInstance()
					.get("cm", CM_MONITOR_ROBOT_ERRORCOUNT));
			Map<String, String> parp = new HashMap<>();
			if (StringUtil.isEmpty(tempKeyOne) || tempKeyOne.split("_").length < 2) continue;
			parp.put("monitorid", tempKeyOne.split("_")[0]);
			parp.put("orgcode", tempKeyOne.split("_")[1]);
			parp.put("quotetype", (String) monitorModel.get("quotetype"));
			if (StringUtil.isEmpty(tempErrorcount)) {
				parp.put("flag", "0");
			} else if (Integer.parseInt(tempErrorcount) >= 10) {
				parp.put("flag", "1");
			} else {
				parp.put("flag", "0");
			}
			insbMonitorDao.updateMonitorStatus(parp);
		}

		long total = insbMonitorDao.queryCountList(monitorModel);
		List<Monitormodel> resultModel = new ArrayList<Monitormodel>();
		if (resultList.size() <= 0) {
			returnMap.put("total", total);
			returnMap.put("rows", resultModel);
			return returnMap;
		}
		for (String tempKeyOne : resultList) {

			String CM_MONITOR_ROBOT = "newmonitor:robot:" + tempKeyOne + ":";
			String CM_MONITOR_ROBOT_TOTAL = CM_MONITOR_ROBOT + "total";
			String CM_MONITOR_ROBOT_SUCTOTAL = CM_MONITOR_ROBOT + "suctotal";
			String CM_MONITOR_ROBOT_AVGTIME = CM_MONITOR_ROBOT + "avgtime";
			String CM_MONITOR_ROBOT_ERRORCOUNT = CM_MONITOR_ROBOT
					+ "errorcount";

			Monitormodel tempMonitormodel = new Monitormodel();
			if (StringUtil.isEmpty(tempKeyOne) || tempKeyOne.split("_").length < 2) continue;
			String tempId = tempKeyOne.split("_")[0];
			String tempOrg = tempKeyOne.split("_")[1];
			String status = (String) monitorModel.get("status");
			INSBElfconf dataInsbElfconf = insbElfconfService.queryById(tempId);
			if (StringUtil.isEmpty(dataInsbElfconf)) {
				continue;
			}
			tempMonitormodel.setId(tempId);
			tempMonitormodel.setName(dataInsbElfconf.getElfname());
			tempMonitormodel.setOrgcode(tempOrg);
			INSCDept queryInscDept = new INSCDept();
			queryInscDept.setDeptinnercode(tempOrg);
			tempMonitormodel.setOrgname(inscDeptService.queryOne(queryInscDept)
					.getShortname());
			String tempTatol = String.valueOf(CMRedisClient.getInstance().get(
					"cm", CM_MONITOR_ROBOT_TOTAL));
			String tempSuc = String.valueOf(CMRedisClient.getInstance().get(
					"cm", CM_MONITOR_ROBOT_SUCTOTAL));
			String tempAvg = String.valueOf(CMRedisClient.getInstance().get(
					"cm", CM_MONITOR_ROBOT_AVGTIME));
			String tempErrorcount = String.valueOf(CMRedisClient.getInstance()
					.get("cm", CM_MONITOR_ROBOT_ERRORCOUNT));
			tempMonitormodel
					.setTotal(StringUtil.isNotEmpty(tempTatol) ? tempTatol
							: "0");
			tempMonitormodel.setSuc(StringUtil.isNotEmpty(tempSuc) ? tempSuc
					: "0");
			tempMonitormodel.setAvg(StringUtil.isNotEmpty(tempAvg) ? tempAvg
					: "0");
			if (!StringUtil.isEmpty(status)) {
				if ("0".equals(status)) {// 健康
					if (StringUtil.isEmpty(tempErrorcount)) {
						tempMonitormodel.setStatus("健康");
					} else if (Integer.parseInt(tempErrorcount) >= 10) {
						continue;
					} else if (Integer.parseInt(tempErrorcount) < 10) {
						tempMonitormodel.setStatus("健康");
					}
				} else if ("1".equals(status)) {// 异常
					if (StringUtil.isEmpty(tempErrorcount)) {
						continue;
					} else if (Integer.parseInt(tempErrorcount) >= 10) {
						tempMonitormodel
								.setStatus("<font style=\"color: red\">异常</font>");
					} else if (Integer.parseInt(tempErrorcount) < 10) {
						continue;
					}
				}
			} else {
				if (StringUtil.isEmpty(tempErrorcount)) {
					tempMonitormodel.setStatus("健康");
				} else if (Integer.parseInt(tempErrorcount) >= 10) {
					tempMonitormodel
							.setStatus("<font style=\"color: red\">异常</font>");
				} else if (Integer.parseInt(tempErrorcount) < 10) {
					tempMonitormodel.setStatus("健康");
				}
			}
			resultModel.add(tempMonitormodel);
		}
		returnMap.put("total", total);
		returnMap.put("rows", resultModel);
		return returnMap;
	}

	@Override
	public String queryPrvNames(Map<String, Object> data) {
		List<String> resultList = insbMonitorDao.queryPrvNames(data);
		String result = "";
		if (resultList.size() <= 0) {
			return result;
		} else {
			for (String tempPrvName : resultList) {
				result = result + "[" + tempPrvName + "]";
			}
			return result;
		}
	}

	@Override
	public Map<String, Object> queryTaskList(Map<String, Object> monitorModel) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		long total = insbMonitorDao.queryCountTaskList(monitorModel);
		List<INSBMonitor> resultList = insbMonitorDao
				.queryTaskList(monitorModel);
		List<INSBMonitor> resultList1 = new ArrayList<>();
		if (resultList.size() <= 0) {
			returnMap.put("total", total);
			returnMap.put("rows", resultList1);
			return returnMap;
		} else {
			for (INSBMonitor temp : resultList) {
				if (!StringUtil.isEmpty(temp.getStartdate())) {
					temp.setStartdateString((DateUtil.toString(
							temp.getStartdate(), DateUtil.Format_DateTime)));
				}
				if (!StringUtil.isEmpty(temp.getEnddate())) {
					temp.setEnddateString((DateUtil.toString(temp.getEnddate(),
							DateUtil.Format_DateTime)));
				}
				if ("失败".equals(temp.getTaskstatusdes())) {
					temp.setTaskstatusdes("<font style=\"color: red\">失败</font>");
				}
				resultList1.add(temp);
			}
		}
		returnMap.put("total", total);
		returnMap.put("rows", resultList1);
		return returnMap;
	}

	@Override
	public Map<String, Object> queryEdiList(Map<String, Object> monitorModel) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<String> resultList = insbMonitorDao.queryList(monitorModel);
		List<String> resultAllList = insbMonitorDao.queryAllList(monitorModel);
		for (String tempKeyOne : resultAllList) {
			String CM_MONITOR_EDI = "newmonitor:edi:" + tempKeyOne + ":";
			String CM_MONITOR_EDI_ERRORCOUNT = CM_MONITOR_EDI + "errorcount";
			String tempErrorcount = String.valueOf(CMRedisClient.getInstance()
					.get("cm", CM_MONITOR_EDI_ERRORCOUNT));
			Map<String, String> parp = new HashMap<>();
			parp.put("monitorid", tempKeyOne.split("_")[0]);
			parp.put("orgcode", tempKeyOne.split("_")[1]);
			parp.put("quotetype", (String) monitorModel.get("quotetype"));
			if (StringUtil.isEmpty(tempErrorcount)) {
				parp.put("flag", "0");
			} else if (Integer.parseInt(tempErrorcount) >= 10) {
				parp.put("flag", "1");
			} else {
				parp.put("flag", "0");
			}
			insbMonitorDao.updateMonitorStatus(parp);
		}

		long total = insbMonitorDao.queryCountList(monitorModel);
		List<Monitormodel> resultModel = new ArrayList<Monitormodel>();
		if (resultList.size() <= 0) {
			returnMap.put("total", total);
			returnMap.put("rows", resultModel);
			return returnMap;
		}
		for (String tempKeyOne : resultList) {

			String CM_MONITOR_EDI = "newmonitor:edi:" + tempKeyOne + ":";
			String CM_MONITOR_EDI_TOTAL = CM_MONITOR_EDI + "total";
			String CM_MONITOR_EDI_SUCTOTAL = CM_MONITOR_EDI + "suctotal";
			String CM_MONITOR_EDI_AVGTIME = CM_MONITOR_EDI + "avgtime";
			String CM_MONITOR_EDI_ERRORCOUNT = CM_MONITOR_EDI + "errorcount";

			Monitormodel tempMonitormodel = new Monitormodel();
			String tempId = tempKeyOne.split("_")[0];
			String tempOrg = tempKeyOne.split("_")[1];
			String status = (String) monitorModel.get("status");
			INSBEdiconfiguration dataInsbEdiconfiguration = insbEdiconfigurationService
					.queryById(tempId);
			if (StringUtil.isEmpty(dataInsbEdiconfiguration)) {
				continue;
			}
			tempMonitormodel.setId(tempId);
			tempMonitormodel.setName(dataInsbEdiconfiguration.getEdiname());
			tempMonitormodel.setOrgcode(tempOrg);
			INSCDept queryInscDept = new INSCDept();
			queryInscDept.setDeptinnercode(tempOrg);
			tempMonitormodel.setOrgname(inscDeptService.queryOne(queryInscDept)
					.getShortname());
			String tempTatol = String.valueOf(CMRedisClient.getInstance().get(
					"cm", CM_MONITOR_EDI_TOTAL));
			String tempSuc = String.valueOf(CMRedisClient.getInstance().get(
					"cm", CM_MONITOR_EDI_SUCTOTAL));
			String tempAvg = String.valueOf(CMRedisClient.getInstance().get(
					"cm", CM_MONITOR_EDI_AVGTIME));
			String tempErrorcount = String.valueOf(CMRedisClient.getInstance()
					.get("cm", CM_MONITOR_EDI_ERRORCOUNT));
			tempMonitormodel
					.setTotal(StringUtil.isNotEmpty(tempTatol) ? tempTatol
							: "0");
			tempMonitormodel.setSuc(StringUtil.isNotEmpty(tempSuc) ? tempSuc
					: "0");
			tempMonitormodel.setAvg(StringUtil.isNotEmpty(tempAvg) ? tempAvg
					: "0");
			if (!StringUtil.isEmpty(status)) {
				if ("0".equals(status)) {// 健康
					if (StringUtil.isEmpty(tempErrorcount)) {
						tempMonitormodel.setStatus("健康");
					} else if (Integer.parseInt(tempErrorcount) >= 10) {
						continue;
					} else if (Integer.parseInt(tempErrorcount) < 10) {
						tempMonitormodel.setStatus("健康");
					}
				} else if ("1".equals(status)) {// 异常
					if (StringUtil.isEmpty(tempErrorcount)) {
						continue;
					} else if (Integer.parseInt(tempErrorcount) >= 10) {
						tempMonitormodel
								.setStatus("<font style=\"color: red\">异常</font>");
					} else if (Integer.parseInt(tempErrorcount) < 10) {
						continue;
					}
				}
			} else {
				if (StringUtil.isEmpty(tempErrorcount)) {
					tempMonitormodel.setStatus("健康");
				} else if (Integer.parseInt(tempErrorcount) >= 10) {
					tempMonitormodel
							.setStatus("<font style=\"color: red\">异常</font>");
				} else if (Integer.parseInt(tempErrorcount) < 10) {
					tempMonitormodel.setStatus("健康");
				}
			}
			resultModel.add(tempMonitormodel);
		}
		returnMap.put("total", total);
		returnMap.put("rows", resultModel);
		return returnMap;
	}

	@Override
	public Map<String, Object> querytaskinfoList(
			Map<String, Object> monitorModel) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		long total = insbMonitorDao.getAllCountMonitorTaskInfo(monitorModel);
		List<INSBMonitor> resultModel = new ArrayList<INSBMonitor>();
		resultModel = insbMonitorDao.getAllMonitorTaskInfo(monitorModel);
		if (resultModel.size() <= 0) {
			returnMap.put("total", total);
			returnMap.put("rows", resultModel);
			return returnMap;
		}
		List<INSBMonitor> resultList1 = new ArrayList<>();
		for(INSBMonitor temp : resultModel){
			if (!StringUtil.isEmpty(temp.getStartdate())) {
				temp.setStartdateString((DateUtil.toString(
						temp.getStartdate(), DateUtil.Format_DateTime)));
			}
			if (!StringUtil.isEmpty(temp.getEnddate())) {
				temp.setEnddateString((DateUtil.toString(temp.getEnddate(),
						DateUtil.Format_DateTime)));
			}
			if ("失败".equals(temp.getTaskstatusdes())) {
				temp.setTaskstatusdes("<font style=\"color: red\">失败</font>");
			}
			if ("edi".equals(temp.getNoti())) {
				temp.setNoti("EDI");
			}else if ("robot".equals(temp.getNoti())){
				temp.setNoti("精灵");
			}else{
				temp.setNoti("历史数据");
			}
			resultList1.add(temp);
		}
		returnMap.put("total", total);
		returnMap.put("rows", resultList1);
		return returnMap;
	}
}