package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.dao.INSBBaseDataDao;
import com.zzb.conf.entity.INSBBaseData;


@Repository
public class INSBBaseDataDaoImpl extends BaseDaoImpl<INSBBaseData> implements INSBBaseDataDao {

	@Override
	public List<INSBBaseData> selectBaseDatasByModel(INSBBaseData model,int page, int rows) {
		Map<String,Object> paramMap = new HashMap<String,Object>(); 
		paramMap.put("startRow", (page-1)*rows+1);
		paramMap.put("endRow", page*rows);
		paramMap.put("model", model);
		
		return this.sqlSessionTemplate.selectList("com.zzb.conf.entity.baseData.INSCBaseData_select", paramMap);
	}

	@Override
	public List<INSBBaseData> selectBaseDatas(int page,int rows) {
		Map<String,Integer> paramMap = new HashMap<String,Integer>();
		paramMap.put("startRow", (page-1)*rows);
		paramMap.put("endRow", page*rows);
		List<INSBBaseData> result = this.sqlSessionTemplate.selectList("com.zzb.conf.entity.INSCBaseData_select", paramMap);
		for(INSBBaseData model:result){
			System.out.println("===model==="+model);
		}
		return result;
	}

	@Override
	public int insertBaseDatas(INSBBaseData model) {
		model.setId(UUIDUtils.random());
		return this.sqlSessionTemplate.insert("com.zzb.conf.entity.INSCBaseData_insert",model);
	}

	@Override
	public int deleteBaseDatas(String id) {
		return this.sqlSessionTemplate.delete("com.zzb.conf.entity.INSCBaseData_delete",id);
	}

	@Override
	public int updateBaseDatas(INSBBaseData model) {
		return this.sqlSessionTemplate.update("com.zzb.conf.entity.INSCBaseData_updateById", model);
	}


}
