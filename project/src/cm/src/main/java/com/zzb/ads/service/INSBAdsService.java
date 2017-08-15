package com.zzb.ads.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.ads.entity.INSBAds;

import java.util.List;
import java.util.Map;

public interface INSBAdsService extends BaseService<INSBAds> {

    /**
     * 保存促销信息
     * @param taskid
     * @param agreementids
     * @return
     */
    int saveAds(String taskid, List<String> agreementids);

    /**
     * 获取促销信息
     * @param taskid
     * @return
     */
    Map<String, List<Map>> getAds (String taskid);

    /**
     * 删除促销信息
     * @param taskid
     * @param agreementid
     * @return
     */
    int deleteAds(String taskid, String agreementid);
}