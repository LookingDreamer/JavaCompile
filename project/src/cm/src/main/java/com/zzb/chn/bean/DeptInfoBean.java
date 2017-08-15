package com.zzb.chn.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by Administrator on 2016/11/8.
 * 平台机构信息
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class DeptInfoBean {
    private String deptcode;
    private String comname;
    private String jobnum; //出单工号

    public String getJobnum() {
		return jobnum;
	}

	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}

	public String getComname() {
        return comname;
    }

    public void setComname(String comname) {
        this.comname = comname;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }
}
