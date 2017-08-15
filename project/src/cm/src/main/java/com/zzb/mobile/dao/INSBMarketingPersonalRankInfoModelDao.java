package com.zzb.mobile.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.mobile.entity.INSBPersonalLotteryCodeInfo;
import com.zzb.mobile.model.marketing.PersonalRankInfoModel;

import java.util.List;
import java.util.Map;

/**
 * Created by HaiJuan.Lei on 2016/10/9.
 *
 */
public interface INSBMarketingPersonalRankInfoModelDao extends BaseDao<INSBPersonalLotteryCodeInfo> {

    /**
     * @return 获取本周的个人排名信息对象
     */
    PersonalRankInfoModel getWeekMylotteryInfo(Map<String,Object> params);

    /**
     * @return 获取历史总的个人排名信息对象
     */
    PersonalRankInfoModel getNewMylotteryInfo(String agentCode);


    /**
     * @return 获取当前最新的排行榜
     */
    List<PersonalRankInfoModel>  getNewRank(int limit);

    /**
     * @return 获取当前周最新的排行榜
     */
    List<PersonalRankInfoModel>  getWeekRank(Map<String,Object> params);
}
