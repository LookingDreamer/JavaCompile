package com.zzb.cm.Interface.entity.car.model;

import java.io.Serializable;

/**
 * 代理人信息
 * Created by austinChen on 2015/9/18.
 */

public class AgentInfo extends PersonInfo implements Serializable {

    /*代理人工号*/
    private String workerID;
    /*从业资格证*/
    private String certNumber;
    /*机构代码*/
    private String orgCode;
    /*机构名称*/
    private String orgName;
    /*团队代码*/
    private String teamCode;
    /*团队名称*/
    private String teamName;
    /*所属平台代码*/
    private String platformCode;
    /*所属平台名称*/
    private String platformName;

    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getWorkerID() {
        return workerID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }
}
