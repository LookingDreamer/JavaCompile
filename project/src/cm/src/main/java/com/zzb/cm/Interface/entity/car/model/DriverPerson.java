package com.zzb.cm.Interface.entity.car.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.zzb.cm.Interface.entity.model.LicenseType;

/**
 * 驾驶人列表
 * Created by austinChen on 2015/10/9.
 */
public class DriverPerson extends InsurePerson {

    /*证件类型*/
    private LicenseType licenseType;
    /*证件描述*/
    private String licenseDesc;
    /*证件号码*/
    private String licenseNo;
    /*证件状态*/
    private String licenseState;
    /*证件登记日期*/
    private Date firstRegDate;
    /*是否主驾驶员*/
    private Boolean isPrimary;
    /*驾驶习惯*/
    private Map<String,String> habits=new HashMap<String,String>();

    public Date getFirstRegDate() {
        return firstRegDate;
    }

    public void setFirstRegDate(Date firstRegDate) {
        this.firstRegDate = firstRegDate;
    }

    public Map<String, String> getHabits() {
        return habits;
    }

    public void setHabits(Map<String, String> habits) {
        this.habits = habits;
    }

    public Boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getLicenseDesc() {
        return licenseDesc;
    }

    public void setLicenseDesc(String licenseDesc) {
        this.licenseDesc = licenseDesc;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getLicenseState() {
        return licenseState;
    }

    public void setLicenseState(String licenseState) {
        this.licenseState = licenseState;
    }

	public LicenseType getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(LicenseType licenseType) {
		this.licenseType = licenseType;
	}

}
