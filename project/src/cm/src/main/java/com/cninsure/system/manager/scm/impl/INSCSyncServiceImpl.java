package com.cninsure.system.manager.scm.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import org.springframework.stereotype.Service;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCFdcom;
import com.cninsure.system.manager.scm.INSCDeptorProviderSyncingService;
import com.cninsure.system.manager.scm.INSCSyncService;
import com.cninsure.system.manager.scr.INSCFdcomManager;


@Service
public class INSCSyncServiceImpl  implements INSCSyncService {
	
	@Resource
	INSCFdcomManager inscDeptManager;
	@Resource
	INSCDeptorProviderSyncingService inscDeptsyncingService;
	@Resource
	private IRedisClient redisClient;
	private Map<String,String> parentcodeMap = new HashMap<String,String>();
	
	//同步状态
//	private static long syncCount = -1L;
//	private static long syncProcess = -1L;
	private static final String DEPT_SYNC_COUNT = "DEPT_SYNC_COUNT";
	private static final String DEPT_SYNC_PROCESS = "DEPT_SYNC_PROCESS";
	private static final int EXPIRED_TIME =60*60;

	public long getSyncCount() {
		if (redisClient.get(Constants.CM_SYNC, DEPT_SYNC_COUNT) == null) {
			return -1;
		}
		return Long.valueOf((String) redisClient.get(Constants.CM_SYNC, DEPT_SYNC_COUNT));
	}

	public long getSyncProcess() {
		if (redisClient.get(Constants.CM_SYNC, DEPT_SYNC_PROCESS) == null) {
			return -1;
		}
		return Long.valueOf((String)redisClient.get(Constants.CM_SYNC, DEPT_SYNC_PROCESS));
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cninsure.system.service.INSCDeptService#getSyncDeptData(java.lang
	 * .String)
	 */
	@Override
	public Map<String, Object> getSyncDeptData(String operator,String comcode) {
		Date syncdate = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		redisClient.set(Constants.CM_SYNC, DEPT_SYNC_COUNT,0, EXPIRED_TIME);
		try {
			Date maxSyncdate = inscDeptsyncingService.getMaxSyncdate(Integer.valueOf(1));
			String maxSyncdateStr = null;
			if (maxSyncdate != null) {
				maxSyncdateStr = DateUtil.toDateTimeString(maxSyncdate);
			}
			List<INSCFdcom> list = null;
			if(StringUtil.isNotEmpty(comcode)){
				list = inscDeptManager.getOrganizationData(null, comcode);//根据机构编码同步单个机构
			}else{
				list = inscDeptManager.getOrganizationData(maxSyncdateStr, null);//根据同步时间同步所有机构
			}
			if (list != null && list.size() > 0) {
				if (parentcodeMap.isEmpty()) {
					List<Map<String, String>> list1 = inscDeptManager.getOrgCode();
					for (Map<String, String> m : list1) {
						parentcodeMap.put(m.get("COMCODE"), m.get("UPCOMCODE"));
					}
				}
				redisClient.set(Constants.CM_SYNC, DEPT_SYNC_COUNT,list.size(), EXPIRED_TIME);
				getSyncDataResultOfCm(list);
				map.put("success", true);
				map.put("returnMsg", "成功同步了" + list.size() + "条机构数据！");
			} else {
				map.put("success", false);
				map.put("returnMsg", "没有需要同步的机构数据！");
			}
			if(StringUtil.isEmpty(comcode)){//同步单个机构不更新同步数据时间，否则会丢失同步时间之前未同步的数据
				inscDeptsyncingService.saveOrgagentlog(operator, true, map, syncdate);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(e);
			map.put("success", false);
			map.put("returnMsg", "机构数据同步失败，数据同步时发生了异常.");
			inscDeptsyncingService.saveOrgagentlog(operator, false, map, syncdate);
		} finally {
			redisClient.expire(Constants.CM_SYNC, DEPT_SYNC_COUNT, 0);
			redisClient.expire(Constants.CM_SYNC, DEPT_SYNC_PROCESS, 0);
		}
		return map;
	}
	
	/**
	 * 将机构信息同步到表INSCDEPT中
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	private void getSyncDataResultOfCm(List<INSCFdcom> list) throws Exception {
		INSCFdcom inscFdcom;
		for (int i = 0; i < list.size(); i++) {
			redisClient.set(Constants.CM_SYNC, DEPT_SYNC_PROCESS,i + 1, EXPIRED_TIME);
			inscFdcom = list.get(i);
			if (StringUtil.isNotEmpty(inscFdcom.getComcode())) {
				INSCDept dept = inscDeptsyncingService.getDept(inscFdcom.getComcode());
				if (dept != null) {
					inscDeptsyncingService.updateDept(getDeptInfo(dept, inscFdcom));
				} else {
					inscDeptsyncingService.saveDept(getDeptInfo(new INSCDept(), inscFdcom));
				}
			}
		}
	}
	/**
	 * 获得机构信息
	 * 
	 * @param inscFdcom
	 * @param dept
	 * @return
	 */
	private INSCDept getDeptInfo(INSCDept dept, INSCFdcom inscFdcom) {
		dept.setId(inscFdcom.getComcode());
		dept.setDeptinnercode(inscFdcom.getIncomcode());
		dept.setComcode(inscFdcom.getComcode());
		if ("1000000000".equals(inscFdcom.getUpcomcode())) {
			dept.setUpcomcode("");
		} else {
			dept.setUpcomcode(inscFdcom.getUpcomcode());
		}

		dept.setComname(inscFdcom.getComname());
		dept.setShortname(inscFdcom.getShortname());
		dept.setComkind(inscFdcom.getComkind());
		dept.setComtype(inscFdcom.getComtype());
		dept.setComgrade(inscFdcom.getComgrade());
		dept.setRearcomcode(inscFdcom.getRearcomcode());
		if (StringUtil.isEmpty(dept.getProvince())) {
			dept.setProvince(inscFdcom.getProvince());
		}
		if (StringUtil.isEmpty(dept.getCity())) {
			dept.setCity(inscFdcom.getCity());
		}
		if (StringUtil.isEmpty(dept.getCounty())) {
			dept.setCounty(inscFdcom.getCounty());
		}
		if("Y".equalsIgnoreCase(inscFdcom.getEndFlag())){
			dept.setType("0"); //停业
		} else {
			dept.setType("1");
		}
		dept.setAddress(inscFdcom.getAddress());
		dept.setZipcode(inscFdcom.getZipcode());
		dept.setPhone(inscFdcom.getPhone());
		dept.setFax((inscFdcom.getFax()+"").replace("—", "-"));
		dept.setEmail(inscFdcom.getEmail());
		dept.setWebaddress(inscFdcom.getWebaddress());
		dept.setSatrapname(inscFdcom.getSatrapname());
		dept.setSatrapcode(inscFdcom.getSatrapcode());
		dept.setChildflag((StringUtil.isEmpty(inscFdcom.getSonnodeflag()) || "0".equals(inscFdcom.getSonnodeflag()))?"0":"1");
		dept.setTreelevel(inscFdcom.getLevelnum() + "");
		dept.setOperator(inscFdcom.getOperator());
		if (dept.getCreatetime() == null) {
			dept.setCreatetime(inscFdcom.getMaketime());
		}
		dept.setModifytime(inscFdcom.getModifytime());
		
		dept.setParentcodes(getParentcodesOfDept(inscFdcom.getUpcomcode()));
		
		return dept;
	}
	/**
	 * @param upcomcode
	 * @return
	 */
	private String getParentcodesOfDept(String upcomcode) {
		if ("".equals(upcomcode) || "1000000000".equals(upcomcode)) {
			return "p+1000000000";
		} else {
			return getParentcodesOfDept(parentcodeMap.get(upcomcode)) + "+" + upcomcode;
		}
	}

}