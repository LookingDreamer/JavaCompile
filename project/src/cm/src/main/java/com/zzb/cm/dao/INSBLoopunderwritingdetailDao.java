package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBLoopunderwritingdetail;

import java.util.List;

public interface INSBLoopunderwritingdetailDao extends BaseDao<INSBLoopunderwritingdetail> {

    public List<INSBLoopunderwritingdetail> queryByLoopId(String loopId);
}