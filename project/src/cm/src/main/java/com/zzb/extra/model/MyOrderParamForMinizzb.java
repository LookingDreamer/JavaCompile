package com.zzb.extra.model;

import com.zzb.mobile.model.MyOrderParam;

public class MyOrderParamForMinizzb extends MyOrderParam {
    private String channeluserid; //渠道用户uuid

    public String getChanneluserid() {
        return channeluserid;
    }

    public void setChanneluserid(String channeluserid) {
        this.channeluserid = channeluserid;
    }
}
