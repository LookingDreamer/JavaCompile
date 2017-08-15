package com.zzb.ads.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.ads.entity.INSBAds;

import java.util.List;
import java.util.Map;

public interface INSBAdsDao extends BaseDao<INSBAds> {
    /**
     * 获取促销信息
     * @param taskid
     * @return
     */
    List<Map<String, String>> getAds (String taskid);

    /**
     * 删除促销信息
     * @param taskid
     * @param agreementid
     * @return
     */
    int deleteAds(String taskid, String agreementid);
}