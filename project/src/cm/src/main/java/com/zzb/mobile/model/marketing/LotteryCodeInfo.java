package com.zzb.mobile.model.marketing;

import com.zzb.mobile.model.CommonModel;

import java.util.List;
import java.util.Map;

/**
 * Created by HaiJuan.Lei on 2016/10/9.
 * 所有的抽奖码状态
 */
public class LotteryCodeInfo extends CommonModel {
    /**
     * 代理人工号
     * */
    private String agentCode;

    /**
     * 抽奖码情况
     * */
    private List<Map<String, Object>> myLotteryCode;


    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public List<Map<String, Object>> getMyLotteryCode() {
        return myLotteryCode;
    }

    public void setMyLotteryCode(List<Map<String, Object>> myLotteryCode) {
        this.myLotteryCode = myLotteryCode;
    }
}
