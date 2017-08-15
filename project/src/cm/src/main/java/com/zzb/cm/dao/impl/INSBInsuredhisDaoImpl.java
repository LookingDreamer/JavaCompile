package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.entity.INSBInsuredhis;

import java.util.List;

@Repository
public class INSBInsuredhisDaoImpl extends BaseDaoImpl<INSBInsuredhis> implements
		INSBInsuredhisDao {

	@Override
	public int deleteByObj(INSBInsuredhis insbInsuredhis) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByObj"), insbInsuredhis);
	}

    public List<INSBInsuredhis> selectByTaskid(String taskid) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectByTaskid"), taskid);
    }
}