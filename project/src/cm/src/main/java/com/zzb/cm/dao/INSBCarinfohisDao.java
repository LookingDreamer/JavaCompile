package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBCarinfohis;

public interface INSBCarinfohisDao extends BaseDao<INSBCarinfohis> {

    public boolean updateInsureconfigsameaslastyear(String taskid, String inscomcode, String value);
    /**
     * 前端查看投保信息需显示所属性质和车辆性质
     * @param insbCarinfohis
     * @return
     */
    public INSBCarinfohis selectCarinfohis(INSBCarinfohis insbCarinfohis);
}