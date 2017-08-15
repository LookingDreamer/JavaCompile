package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBInsuresupplyparam;

import java.util.List;

public interface INSBInsuresupplyparamDao extends BaseDao<INSBInsuresupplyparam> {

    public int deleteByTask(String taskid, String inscomcode);

    public List<INSBInsuresupplyparam> getByTask(String taskid, String inscomcode);
}