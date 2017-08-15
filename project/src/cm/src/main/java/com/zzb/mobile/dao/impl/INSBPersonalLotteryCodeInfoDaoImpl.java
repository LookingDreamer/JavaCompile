package com.zzb.mobile.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.mobile.dao.INSBPersonalLotteryCodeInfoDao;
import com.zzb.mobile.entity.INSBPersonalLotteryCodeInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by HaiJuan.Lei on 2016/10/10.
 *
 */
@Repository
public class INSBPersonalLotteryCodeInfoDaoImpl  extends BaseDaoImpl<INSBPersonalLotteryCodeInfo> implements INSBPersonalLotteryCodeInfoDao {
    @Override
    public void insert(INSBPersonalLotteryCodeInfo codeInfo){
        super.insert(codeInfo);
    }

    @Override
    public int getNewLotteryCodeCnt() {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("getNewLotteryCodeCnt"));
    }

    @Override
    public int getWeekLotteryCodeCnt( Map<String,Object> params) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("getWeekLotteryCodeCnt"),params);
    }

    @Override
    public List<Map<String, Object>> getMyLoteryCodeInfo(String agentCode) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("getMyLoteryCodeInfo"), agentCode);
    }

    @Override
    public List<Map<String, Object>> getAwardLotteryCodeInfo() {
        return this.sqlSessionTemplate.selectList(this.getSqlName("getAwardLotteryCodeInfo"));
    }


}
