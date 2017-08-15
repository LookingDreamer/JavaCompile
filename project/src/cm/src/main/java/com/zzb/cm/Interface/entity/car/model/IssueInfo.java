package com.zzb.cm.Interface.entity.car.model;

/**
 * 出单信息
 * Created by austinChen on 2015/10/9.
 */
public class IssueInfo {

    /*机构代码*/
    private String orgCode;
    /*机构名称*/
    private String orgName;
    /*地址*/
    private String address;
    /*电话*/
    private String phone;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
