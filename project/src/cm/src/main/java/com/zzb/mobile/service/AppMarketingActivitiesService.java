package com.zzb.mobile.service;

import com.zzb.mobile.entity.INSBAgentMarketingNew;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.marketing.LotteryCodeInfo;
import com.zzb.mobile.model.marketing.RankingModel;

import java.util.Date;
import java.util.List;

/**
 * Created by HaiJuan.Lei on 2016/10/9.
 * 定义营销方案的服务
 */
public interface AppMarketingActivitiesService {

    /**
     * 参与营销活动P
     * */
    public CommonModel isCanParticipateMarketing(String agentCode,String referee,Date tempAgentRegTime);
    /**
     * 车险掌中保注册成功的时候调用此接口，进行营销方案
     * */
    public CommonModel registerSuccess(String agentCode);

    /**
     * 获取历史的推荐人数排行榜情况
     * */
    public RankingModel getRankingList(String agentCode);





    /**
     * 获取当周的推荐人数排行榜情况
     * */
    public RankingModel getWeekRankingList(String agentCode);

    /**
     * 获取我的推荐码的状态信息情况
     * */
//    public LotteryCodeInfo getMyLotteryCodeInfo(String agentCode);

    /**
     * 获取中奖纪录信息列表
     * */
//    public LotteryCodeInfo getAwardInfo(String agentCode);


    /**
     * @param agentCode 代理人工号
     * @return 获取代理人的推荐列表，按照推荐时间倒序
     */
     List<INSBAgentMarketingNew> getReferrals(String agentCode);

    /**
     * @param agentCode 被推荐代理人工号
     * @param referee 推荐人工号
     * @return 更新新注册的代理人对应的推荐人的红包状态
     */
    void updateAgentRedPacketStatus(String agentCode, String referee);


}
