package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBSupplement;

public interface INSBSupplementDao extends BaseDao<INSBSupplement> {

	public void updateBykeyidandproviderValue(INSBSupplement model); 

}