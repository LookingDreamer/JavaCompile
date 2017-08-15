package com.cninsure.system.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.system.dao.INSCScheduleDao;
import com.cninsure.system.entity.INSCSchedule;

@Repository
public class INSCScheduleDaoImpl extends BaseDaoImpl<INSCSchedule> implements
		INSCScheduleDao {

	@Override
	public List<INSCSchedule> getScheduleStateListByIds(List<String> roleidList) {
		return this.sqlSessionTemplate.selectList(
				this.getSqlName("getScheduleStateListByIds"), roleidList);
	}

	@Override
	public long selectPagingCount(Map<String, Object> data) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCount"), data);
	}

	@Override
	public List<Map<Object, Object>> selectScheduleListPaging(
			Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectScheduleListPaging"),map);
	}

}