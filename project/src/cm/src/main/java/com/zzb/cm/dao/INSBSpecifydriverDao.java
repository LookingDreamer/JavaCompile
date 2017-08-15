package com.zzb.cm.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBSpecifydriver;

public interface INSBSpecifydriverDao extends BaseDao<INSBSpecifydriver> {

	public List<INSBSpecifydriver> selectSpecifyDriverByTaskid(String processInstanceId);

}