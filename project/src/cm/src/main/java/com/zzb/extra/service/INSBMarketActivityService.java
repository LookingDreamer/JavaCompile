package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.controller.vo.ActivityPrizesVo;
import com.zzb.extra.entity.INSBMarketActivity;

import java.util.List;
import java.util.Map;

public interface INSBMarketActivityService extends BaseService<INSBMarketActivity> {

    public String queryTaskPrizes(ActivityPrizesVo activityPrizesVo);

    public String queryActivity(Map<String, Object> map);

    public String getList(Map map);

    public Map findById(String id);

    public void saveObject(INSBMarketActivity insbMarketActivity);

    public void updateObject(INSBMarketActivity insbMarketActivity);

    public void deleteObject(String id);

    public String queryActivityPrizeListById(String id);

    public String queryActivityConditionListById(Map<Object, Object> map);

    //验证活动是否配置奖品和规则,获取prize和conditions资料笔数
    public Map<String,Long> validatePrizeAndConditions(String id);
    //public void testTaskJob();//<!--addrefresh-->

    public String queryEffectiveActivity(Map<String, Object> map);//<!--refresh add-->
    
    public long selectMaxTmpcode();

}