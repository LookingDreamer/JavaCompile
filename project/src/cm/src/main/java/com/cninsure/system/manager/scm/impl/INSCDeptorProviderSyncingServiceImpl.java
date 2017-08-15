package com.cninsure.system.manager.scm.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cninsure.system.dao.INSBOrgagentlogDao;
import com.cninsure.system.entity.INSBOrgagentlog;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.manager.scm.INSCDeptorProviderSyncingService;
import com.cninsure.system.service.INSCDeptService;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.dao.INSBProviderDao;

@Service
public class INSCDeptorProviderSyncingServiceImpl implements INSCDeptorProviderSyncingService {
	
	@Resource
	private INSCDeptService inscDeptService;
	
	@Resource
	private INSBOrgagentlogDao insbOrgagentlogDao;
	
	@Resource
	private INSBProviderDao insbProviderDao;
	
	@Override
	public INSCDept getDept(String comcode) {
		return inscDeptService.getOrgDeptByDeptCode(comcode);
	}

	@Override
	public void saveDept(INSCDept dept) {
		inscDeptService.insert(dept);
	}

	@Override
	public void updateDept(INSCDept dept) {
		inscDeptService.updateById(dept);
	}
	/**
	 * 保存日志信息
	 * 
	 * @param operator
	 * @param result
	 * @param map
	 * @param syncdate
	 */
	public void saveOrgagentlog(String operator, boolean result,
			Map<String, Object> map, Date syncdate) {
		INSBOrgagentlog log = new INSBOrgagentlog();
		log.setCreatetime(new Date());
		log.setOperator(operator);
		log.setType(Integer.valueOf(1));
		log.setSyncdate(syncdate);
		log.setIssuccess(result ? Integer.valueOf(1) : Integer.valueOf(2));
		log.setLogcontent((map.get("returnMsg")+""));
		insbOrgagentlogDao.insert(log);
	}
	/**
	 * 保存代理人日志信息
	 * 
	 * @param operator
	 * @param result
	 * @param map
	 * @param syncdate
	 */
	public void saveOrgagentlog2(String operator, boolean result,
			Map<String, Object> map, Date syncdate) {
		INSBOrgagentlog log = new INSBOrgagentlog();
		log.setCreatetime(new Date());
		log.setOperator(operator);
		log.setType(Integer.valueOf(2));
		log.setSyncdate(syncdate);
		log.setIssuccess(result ? Integer.valueOf(1) : Integer.valueOf(2));
		log.setLogcontent((String) map.get("returnMsg"));
		insbOrgagentlogDao.insert(log);
	}
	/**
	 * 保存日志信息
	 * 
	 * @param operator
	 * @param result
	 * @param map
	 * @param syncdate
	 */
	public void saveOrgproviderlog(String operator, boolean result, Map<String, Object> map, Date syncdate) {
		INSBOrgagentlog log = new INSBOrgagentlog();
		log.setCreatetime(new Date());
		log.setOperator(operator);
		log.setType(3);//供应商同步
		log.setSyncdate(syncdate);
		log.setIssuccess(result ? Integer.valueOf(1) : Integer.valueOf(2));
		log.setLogcontent((map.get("returnMsg")+""));
		insbOrgagentlogDao.insert(log);
	}
	
	@Override
	public Date getMaxSyncdate(Integer value) {
		return insbOrgagentlogDao.getMaxSyncdate(value);
	}

	@Override
	public INSBProvider queryByPrvcode(String prvcode) {
		return insbProviderDao.queryByPrvcode(prvcode);
	}

	@Override
	public void updateById(INSBProvider pro) {
		insbProviderDao.updateById(pro);
		
	}
	@Override
	public void addProData(INSBProvider pro) {
		insbProviderDao.addProData(pro);
	}
}
