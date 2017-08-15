package com.zzb.extra.model;

import com.zzb.mobile.model.policyoperat.PolicyListQueryParam;

/**
 * Created by liwucai on 2016/5/27 16:32.
 */
public class PolicyListQueryParamForMinizzb extends PolicyListQueryParam {
    private String channeluserid; //渠道用户uuid

    public String getChanneluserid() {
        return channeluserid;
    }

    public void setChanneluserid(String channeluserid) {
        this.channeluserid = channeluserid;
    }
}
