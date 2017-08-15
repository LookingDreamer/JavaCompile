package com.zzb.extra.controller.vo;

import java.io.Serializable;

/**
 * Created by MagicYuan on 2016/5/25.
 */
public class ConditionsVo  implements Serializable {

    /**
     * 条件项id
     */
    private String conditionid;

    /**
     * 来源id
     */
    private String sourceid;

    /**
     * 来源类型
     */
    private String conditionsource;

    public String getConditionid() {
        return conditionid;
    }

    public void setConditionid(String conditionid) {
        this.conditionid = conditionid;
    }

    public String getSourceid() {
        return sourceid;
    }

    public void setSourceid(String sourceid) {
        this.sourceid = sourceid;
    }

    public String getConditionsource() {
        return conditionsource;
    }

    public void setConditionsource(String conditionsource) {
        this.conditionsource = conditionsource;
    }

}
