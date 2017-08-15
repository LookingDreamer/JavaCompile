package com.zzb.mobile.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.mobile.entity.INSBAgentMarketingNew;
import com.zzb.mobile.entity.Insbpaymenttransaction;
import com.zzb.mobile.model.marketing.PersonalRankInfoModel;
import com.zzb.mobile.model.marketing.RankingModel;

import java.util.List;

/**
 * Created by HaiJuan.Lei on 2016/10/9.
 * 活动新注册代理人信息
 */
public interface INSBAgentMarketingNewDao extends BaseDao<INSBAgentMarketingNew> {
    /**
     * @param agentInfo 新增一个推荐人
     */
    public void insert(INSBAgentMarketingNew agentInfo);

    /**
     * @param agentCode 代理人工号
     * @return 获取代理人的推荐列表，按照推荐时间倒序
     */
    public List<INSBAgentMarketingNew> getReferrals(String agentCode);

    public void update(INSBAgentMarketingNew insbAgentMarketingNew);

    List<INSBAgentMarketingNew> getAgentInfo(String agentCode);

    /**
     * 是否对应的被推荐人已经发了红包了
     * */
    int isSendRedPaper(String agentCode);

}
