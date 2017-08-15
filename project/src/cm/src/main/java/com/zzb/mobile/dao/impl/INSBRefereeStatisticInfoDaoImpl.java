package com.zzb.mobile.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.mobile.dao.INSBRefereeStatisticInfoDao;
import com.zzb.mobile.entity.INSBRefereeStatisticInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by HaiJuan.Lei on 2016/10/19.
 *
 */
@Repository
public class INSBRefereeStatisticInfoDaoImpl extends BaseDaoImpl<INSBRefereeStatisticInfo> implements INSBRefereeStatisticInfoDao {



    @Override
    public void insert(INSBRefereeStatisticInfo agentInfo){
        super.insert(agentInfo);
    }


    @Override
    public void updateInfo(INSBRefereeStatisticInfo agentInfo) {
        this.sqlSessionTemplate.update(this.getSqlName("update"), agentInfo);
    }

    @Override
    public INSBRefereeStatisticInfo findByAgentCode(String agentCode) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("findByAgentCode"), agentCode);
    }

    @Override
    public List<INSBRefereeStatisticInfo> getRankInfo(int limit) {
        return  this.sqlSessionTemplate.selectList(this.getSqlName("getRankInfo"), limit);
    }

    @Override
    public INSBRefereeStatisticInfo getMyRankInfo(String agentCode) {
        return  this.sqlSessionTemplate.selectOne(this.getSqlName("getMyRankInfo"), agentCode);
    }

    @Override
    public List<INSBRefereeStatisticInfo> getWeekRankInfo(Map<String,Object> weekRankMap) {
        return  this.sqlSessionTemplate.selectList(this.getSqlName("getWeekRankInfo"), weekRankMap);
    }

    @Override
    public INSBRefereeStatisticInfo getMyWeekRankInfo(Map<String,Object> myWeekRankMap) {
        return  this.sqlSessionTemplate.selectOne(this.getSqlName("getMyWeekRankInfo"), myWeekRankMap);
    }

}
