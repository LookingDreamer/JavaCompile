package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zzb.conf.dao.INSBRuleBaseDao;
import com.zzb.conf.entity.INSBRuleBase;

@Repository
public class INSBRuleBaseDaoImpl implements INSBRuleBaseDao {

	@Autowired(required = true)
	private SqlSession sqlSessionTemplate;

	public void setSqlSession1(SqlSession sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Override
	public List<INSBRuleBase> selectListPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList("INSBRuleBase_selectPage",
				map);
	}

	@Override
	public long selectListCountPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("INSBRuleBase_selectCount",
				map);
	}

	@Override
	public List<String> selectTasksetidList(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(
				"INSBRuleBase_selectTasksetidList", map);
	}

	@Override
	public INSBRuleBase selectById(String id) {
		return this.sqlSessionTemplate.selectOne("INSBRuleBase_selectById", id);
	}

	@Override
	public Long selectCount() {
		
		return this.sqlSessionTemplate.selectOne("INSBRuleBase_selectCountlist");
	}

	@Override
	public List<Map<Object, Object>> selectRuleBaseListPaging(
			Map<String, Object> map) {
		
		return this.sqlSessionTemplate.selectList("INSBRuleBase_select",map);
	}
}