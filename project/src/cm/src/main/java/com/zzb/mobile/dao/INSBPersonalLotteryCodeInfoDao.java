package com.zzb.mobile.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.mobile.entity.INSBPersonalLotteryCodeInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by HaiJuan.Lei on 2016/10/10.
 *
 */
public interface INSBPersonalLotteryCodeInfoDao  extends BaseDao<INSBPersonalLotteryCodeInfo> {
    /**
     * @param codeInfo 新增一个抽奖码记录
     */
    public void insert(INSBPersonalLotteryCodeInfo codeInfo);

    /**
     * 获取抽奖码数
     * */
    int getNewLotteryCodeCnt();


    /**
     * 获取当周抽奖码数
     * */
    int getWeekLotteryCodeCnt(Map<String,Object> params);

    /**获取我的抽奖码信息*/
    List<Map<String, Object>> getMyLoteryCodeInfo(String agentCode);

    /**
     *获取抽奖码
     * */
    List<Map<String, Object>>  getAwardLotteryCodeInfo();



}
