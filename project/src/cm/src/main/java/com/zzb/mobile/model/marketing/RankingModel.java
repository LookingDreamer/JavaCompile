package com.zzb.mobile.model.marketing;

import com.zzb.mobile.model.CommonModel;

import java.util.List;

/**
 * Created by HaiJuan.Lei on 2016/10/9.
 * 总的推荐人数排行版情况
 */
public class RankingModel extends CommonModel {
    /**
     * 累计发放有效抽奖码数
     * */
    private int lotteryCodeCnt;

    /**
     * 我的排名情况
     * */
    private PersonalRankInfoModel myLotteryInfo;

    /**
     * 前一百名排名情况
     * */
    private List<PersonalRankInfoModel> rankList;


    public int getLotteryCodeCnt() {
        return lotteryCodeCnt;
    }

    public void setLotteryCodeCnt(int lotteryCodeCnt) {
        this.lotteryCodeCnt = lotteryCodeCnt;
    }

    public PersonalRankInfoModel getMyLotteryInfo() {
        return myLotteryInfo;
    }

    public void setMyLotteryInfo(PersonalRankInfoModel myLotteryInfo) {
        this.myLotteryInfo = myLotteryInfo;
    }

    public List<PersonalRankInfoModel> getRankList() {
        return rankList;
    }

    public void setRankList(List<PersonalRankInfoModel> rankList) {
        this.rankList = rankList;
    }
}
