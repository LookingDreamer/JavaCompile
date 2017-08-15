package com.zzb.mobile.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.mobile.entity.INSBRefereeStatisticInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by HaiJuan.Lei on 2016/10/19.
 *
 */
public interface INSBRefereeStatisticInfoDao extends BaseDao<INSBRefereeStatisticInfo> {


    /**
     * @param agentInfo 新增一个推荐人记录
     */
    public void insert(INSBRefereeStatisticInfo agentInfo);

    void updateInfo(INSBRefereeStatisticInfo agentInfo);

    public INSBRefereeStatisticInfo findByAgentCode(String agentCode);

    /**
     * @param  limit 只提取指定条数的记录
     * 获取排名在前 limit 的排名记录
     * */
    List<INSBRefereeStatisticInfo> getRankInfo(int limit);

    /**
     * 获取我的历史排名
     * */
    INSBRefereeStatisticInfo getMyRankInfo(String agentCode);

    /**
     * 获取周排名
     * */
    List<INSBRefereeStatisticInfo> getWeekRankInfo(Map<String,Object> weekRankMap);

    /**
     * 获取我的周排名
     * */
    INSBRefereeStatisticInfo getMyWeekRankInfo(Map<String,Object> myWeekRankMap);





}
