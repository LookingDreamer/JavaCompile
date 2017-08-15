package com.zzb.mobile.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.mobile.dao.INSBMarketingPersonalRankInfoModelDao;
import com.zzb.mobile.entity.INSBPersonalLotteryCodeInfo;
import com.zzb.mobile.model.marketing.PersonalRankInfoModel;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HaiJuan.Lei on 2016/10/9.
 *
 */
@Repository
public class INSBMarketingPersonalRankInfoModelDaoImpl extends BaseDaoImpl<INSBPersonalLotteryCodeInfo>
        implements INSBMarketingPersonalRankInfoModelDao {

    @Override
    public List<PersonalRankInfoModel> getNewRank(int limit) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("getNewRank"),limit);
    }

    @Override
    public List<PersonalRankInfoModel> getWeekRank(Map<String,Object> params) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("getWeekRank"),params);
    }



    @Override
    public PersonalRankInfoModel getWeekMylotteryInfo(Map<String,Object> params) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("getWeekMylotteryInfo"), params);
    }

    @Override
    public PersonalRankInfoModel getNewMylotteryInfo(String agentCode) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("getNewMylotteryInfo"),agentCode);
    }
}
