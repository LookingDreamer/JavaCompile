package com.zzb.extra.model;

import com.zzb.extra.entity.ChannelTask;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
public class MiniOrderListModel {
    private long total;
    private List<Map<Object,Object>> rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Map<Object,Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<Object,Object>> rows) {
        this.rows = rows;
    }
}
