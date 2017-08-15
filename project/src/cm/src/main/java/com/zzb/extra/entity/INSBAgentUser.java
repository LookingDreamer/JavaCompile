package com.zzb.extra.entity;

import com.zzb.conf.entity.INSBAgent;

/**
 * Created by liwucai on 2016/5/18 15:48.
 */
public class INSBAgentUser extends INSBAgent {
    /**
     * 投保地区
     */
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
