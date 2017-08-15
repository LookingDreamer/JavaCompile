package com.zzb.cm.Interface.entity.car.model;

import java.io.Serializable;

/**
 * Created by wjl on 2015/9/18.
 */
public class SubAgentInfo extends PersonInfo implements Serializable {

    /*
    是否正式用户
    */
    private Boolean isOfficial;
    /*
    * 虚拟工号
    * */
    private String virtualWorkerID;//

    public Boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(Boolean isOfficial) {
        this.isOfficial = isOfficial;
    }

    public String getVirtualWorkerID() {
        return virtualWorkerID;
    }

    public void setVirtualWorkerID(String virtualWorkerID) {
        this.virtualWorkerID = virtualWorkerID;
    }
}
