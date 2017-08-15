package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.entity.INSBCarmodelinfo;

@Repository
public class INSBCarmodelinfoDaoImpl extends BaseDaoImpl<INSBCarmodelinfo> implements
		INSBCarmodelinfoDao {

	@Override
	public void updateByCarInfoId(INSBCarmodelinfo iNSBCarmodelinfo) {
		this.sqlSessionTemplate.update(this.getSqlName("updateByCarInfoId"),iNSBCarmodelinfo);
		
	}

	@Override
	public List<Map<String, Object>> reselectCarModelInfo(
			Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("reselectCarModelInfo"),map);
	}

	@Override
	public boolean updateCarinfoidById(INSBCarmodelinfo Carmodelinfo) {
		this.sqlSessionTemplate.update(this.getSqlName("updateCarinfoidById"),Carmodelinfo);
		return true;
	}

	@Override
	public List<INSBCarmodelinfo> selectPageByStandardname(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByStandardname"), map);
	}
	
	@Override
	public long selectCountPageByStandardname(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountByStandardname"), map);
	}

	@Override
	public List<INSBCarmodelinfo> selectPageByBrandName(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByBrandName"), map);
	}

	@Override
	public long selectCountPageByBrandName(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountByBrandName"), map);

	}

	@Override
	public INSBCarmodelinfo selectByCarinfoId(String CarinfoId) {
        List<INSBCarmodelinfo> list = this.sqlSessionTemplate.selectList(this.getSqlName("selectCountByCarinfoId"), CarinfoId);
        return list!=null&&list.size()>0 ? list.get(0) : null;
	}

	@Override
	public List<INSBCarmodelinfo> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}
	
	@Override
	public List<INSBCarmodelinfo> selectAll(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPage"),params);
	}

}