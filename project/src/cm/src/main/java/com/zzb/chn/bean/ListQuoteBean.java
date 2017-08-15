package com.zzb.chn.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ListQuoteBean {
    private String respCode;
    private String errorMsg;
    private int totalNum;
    private List<QuoteBean> quoteBeanList;


    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<QuoteBean> getQuoteBeanList() {
        return quoteBeanList;
    }

    public void setQuoteBeanList(List<QuoteBean> quoteBeanList) {
        this.quoteBeanList = quoteBeanList;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}
