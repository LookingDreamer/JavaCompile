package com.zzb.extra.model;

/**
 * Created by Administrator on 2016/12/5.
 */
public class MiniOrderListMappingModel extends MiniOrderQueryModel {
    private long code;
    private MiniOrderListModel body;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public MiniOrderListModel getBody() {
        return body;
    }

    public void setBody(MiniOrderListModel body) {
        this.body = body;
    }
}
