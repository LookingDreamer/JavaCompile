package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBMiniDateDao;
import com.zzb.extra.entity.INSBMiniDate;
/**
 * 
 * @author Shigw
 * 
 * ——————————————————————————————
 */

@Repository
public class INSBMiniDateDaoImpl extends BaseDaoImpl<INSBMiniDate> implements INSBMiniDateDao {

	@Override
	public int updateById(INSBMiniDate date) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.insert("INSBMiniDate_updateById", date);
	}


	@Override
	public int insertDate(INSBMiniDate date) {
		
		return this.sqlSessionTemplate.insert("INSBMiniDate_insert", date);
	}

	@Override
	public List<INSBMiniDate> selectByDatetype(String datetype) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList("INSBMiniDate_select", datetype);
	}

	@Override
	public List<INSBMiniDate> selectDate(Map map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList("INSBMiniDate_select", map);
	}

	@Override
	public INSBMiniDate selectDateByDatestr(Map map) {
		
		return this.sqlSessionTemplate.selectOne("INSBMiniDate_select", map);
	}

	@Override
	public List<Map<Object, Object>> queryMiniDateList(Map<String, Object> map) {
		
		return this.sqlSessionTemplate.selectList("INSBMiniDate_select", map);
	}

	@Override
	public long queryCountMiniDate(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("INSBMiniDate_selectCount", map);
	}


	@Override
	public void deleteByYear(String year) {

		this.sqlSessionTemplate.delete("INSBMiniDate_deleteByYear", year);
	}

}
