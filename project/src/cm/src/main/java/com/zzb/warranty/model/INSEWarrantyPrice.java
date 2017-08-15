package com.zzb.warranty.model;

import com.cninsure.core.dao.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/1/16.
 */
public class INSEWarrantyPrice implements Identifiable{
    @JsonIgnore
    private String id;
    private Double downPrice;
    private Double upPrice;
    private Double warrantyPrice;
    private int warrantyPlan;
    @JsonProperty("isImported")
    private boolean isImported;
    private int warrantyPeriod;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String s) {
        this.id = s;
    }

    public Double getDownPrice() {
        return downPrice;
    }

    public void setDownPrice(Double downPrice) {
        this.downPrice = downPrice;
    }

    public Double getUpPrice() {
        return upPrice;
    }

    public void setUpPrice(Double upPrice) {
        this.upPrice = upPrice;
    }

    public Double getWarrantyPrice() {
        return warrantyPrice;
    }

    public void setWarrantyPrice(Double warrantyPrice) {
        this.warrantyPrice = warrantyPrice;
    }

    public int getWarrantyPlan() {
        return warrantyPlan;
    }

    public void setWarrantyPlan(int warrantyPlan) {
        this.warrantyPlan = warrantyPlan;
    }

    @JsonIgnore
    public boolean isImported() {
        return isImported;
    }

    public void setImported(boolean isImported) {
        this.isImported = isImported;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
}
