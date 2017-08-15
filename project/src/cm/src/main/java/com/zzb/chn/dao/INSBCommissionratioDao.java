package com.zzb.chn.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.chn.entity.INSBCommissionratio;

import java.util.List;
import java.util.Map;

public interface INSBCommissionratioDao extends BaseDao<INSBCommissionratio> {
    public long queryPagingListCount(Map<String, Object> map);

    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);

    public List<INSBCommissionratio> queryCommissionRatioByChannel(String channelid);

    public INSBCommissionratio queryCommissionRatioById(String id);

    public List<Map<String, String>> initChannelList();

    public int insertCommissionRatio(INSBCommissionratio commissionRatio);

    public int updateCommissionRatio(INSBCommissionratio commissionRatio);

    public int updateCommissionRatioStatus(INSBCommissionratio commissionRatio);

    public int delCommissionRatio(String id);

    public int updateTerminalTimeByNoti(INSBCommissionratio commissionRatio);

    public List<Map<Object, Object>> queryCommissionRatioChannel();

    public int cleanCommissionRatioStatus(String channelid);
}