package com.zzb.extra.model;

import com.zzb.extra.entity.INSBMiniOrderTrace;

/**
 * Created by Hwc on 2016/12/8.
 */
public class INSBMiniOrderTraceModel extends INSBMiniOrderTrace {
    private String createtimestart;
    private String createtimeend;
    private String startdateflag;
    private String agentname;

    public String getCreatetimestart() {
        return createtimestart;
    }

    public void setCreatetimestart(String createtimestart) {
        this.createtimestart = createtimestart;
    }

    public String getStartdateflag() {
        return startdateflag;
    }

    public void setStartdateflag(String startdateflag) {
        this.startdateflag = startdateflag;
    }

    public String getCreatetimeend() {
        return createtimeend;
    }

    public void setCreatetimeend(String createtimeend) {
        this.createtimeend = createtimeend;
    }


    public String getAgentname() {
        return agentname;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }

}
