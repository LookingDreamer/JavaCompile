package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBWorktimearea;

public interface INSBWorktimeareaDao extends BaseDao<INSBWorktimearea> {

	List<INSBWorktimearea> selectByWorktimeId(Map<String, String> map);

}