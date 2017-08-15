package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBFilebusiness;

import java.util.List;
import java.util.Map;

public interface INSBFilebusinessDao extends BaseDao<INSBFilebusiness> {
    public void deleteByFilelibraryids(List<String> filelibraryids);
    public void deleteByFilelibraryByTadkIdImageType(Map<String,String> params);
    /**
     * 按id删除
     */
    public int deleteIn(List<String> list);
}