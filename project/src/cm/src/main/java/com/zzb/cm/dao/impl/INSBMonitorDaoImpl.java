package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBMonitorDao;
import com.zzb.cm.entity.INSBMonitor;

@Repository
public class INSBMonitorDaoImpl extends BaseDaoImpl<INSBMonitor> implements
		INSBMonitorDao {
	@Override
	public List<String> queryList(Map<String, Object> parp) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectRobotInfo"),parp);
	}
	@Override
	public long queryCountList(Map<String, Object> parp) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountRobotInfo"),parp);
	}
	@Override
	public List<String> queryPrvNames(Map<String, Object> parp) {
		 return this.sqlSessionTemplate.selectList(this.getSqlName("abilityListByelfidPaging"),parp);
	}
	@Override
	public List<INSBMonitor> queryTaskList(Map<String, Object> parp) {
		 return this.sqlSessionTemplate.selectList(this.getSqlName("selectRobotTaskInfo"),parp);
	}
	@Override
	public long queryCountTaskList(Map<String, Object> parp) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectRobotTaskInfoCount"),parp);
	}
	@Override
	public List<String> queryAllList(Map<String, Object> parp) {
		 return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllRobotInfo"),parp);
	}
	@Override
	public void updateMonitorStatus(Map<String, String> parp) {
		 this.sqlSessionTemplate.update(this.getSqlName("updateMonitorStatus"),parp);
	}
	@Override
	public List<Map<String,Object>> getAllMonitorInfo(Map<String, Object> parp){
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllMonitorInfo"),parp);
	}
	@Override
	public List<INSBMonitor> getAllMonitorTaskInfo(Map<String, Object> parp) {
		 return this.sqlSessionTemplate.selectList(this.getSqlName("selectMonitorTaskInfo"),parp);
	}
	@Override
	public long getAllCountMonitorTaskInfo(Map<String, Object> parp) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountMonitorTaskInfo"),parp);
	}
}