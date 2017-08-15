package com.zzb.extra.model;

/**
 * Created by heweicheng on 2016/5/26.
 */
public class MarketActivityQueryModel {

    private String activityname;

    private String activitytype;

    private String effectivetime;

    private String terminaltime;

    private String status;//refresh
    
    private String activitycode;
    
    

    public String getActivitycode() {
		return activitycode;
	}

	public void setActivitycode(String activitycode) {
		this.activitycode = activitycode;
	}

	public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public String getActivitytype() {
        return activitytype;
    }

    public void setActivitytype(String activitytype) {
        this.activitytype = activitytype;
    }

    public String getEffectivetime() {
        return effectivetime;
    }

    public void setEffectivetime(String effectivetime) {
        this.effectivetime = effectivetime;
    }

    public String getTerminaltime() {
        return terminaltime;
    }

    public void setTerminaltime(String terminaltime) {
        this.terminaltime = terminaltime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
