package com.zzb.cm.Interface.entity.car.model;

/**
 * 保险人使用的对象
 * Created by austinChen on 2015/10/9.
 */

public class InsurePerson extends PersonInfo {


    /**
     * 与车主关系
     */
    private String relation;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
