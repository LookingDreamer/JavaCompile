package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBInsuredhis;

import java.util.List;

public interface INSBInsuredhisDao extends BaseDao<INSBInsuredhis> {
	public int deleteByObj(INSBInsuredhis insbInsuredhis);

    public List<INSBInsuredhis> selectByTaskid(String taskid);
}