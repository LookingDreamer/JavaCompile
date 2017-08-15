package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBFlowerrorDao;
import com.zzb.cm.entity.INSBFlowerror;

@Repository
public class INSBFlowerrorDaoImpl extends BaseDaoImpl<INSBFlowerror> implements
		INSBFlowerrorDao {

	@Override
	public List<Map<Object, Object>> selectErrorListPaging(
			Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectErrorListPaging"),map); 

	}

	@Override
	public long selectPagingCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectErrorCountPaging"), map);
	}

	@Override
	public List<INSBFlowerror> selectflowcode() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectflowcode"));
	}

	@Override
	public int selectMydataOneGZ(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectMydataOne_GZ"), map);
	}
	@Override
	public int selectMydataOneRG(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectMydataOne_RG"), map);
	}
	
	@Override
	public void updateMydataGZ(Map<String, String> map) {
		 this.sqlSessionTemplate.selectOne(this.getSqlName("updateMydata_GZ"), map);
	}
	@Override
	public void updateMydataRG(Map<String, String> map) {
		 this.sqlSessionTemplate.selectOne(this.getSqlName("updateMydata_RG"), map);
	}
	
	@Override
	public int selectMydataOneFfg(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectMydataOneFfg"), map);
	}

	@Override
	public List<INSBFlowerror> selectOneTipGuizeshow(INSBFlowerror insbFlowerror) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOneTipGuizeshow"), insbFlowerror);
	}
}