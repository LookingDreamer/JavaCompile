package com.zzb.warranty.model;

import com.cninsure.core.dao.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/10.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class INSEQuote extends BaseModel implements Identifiable{
    private String carinfoid;

    @NotNull(message = "车架号不为空")
    private String vincode;
    @NotNull(message = "引擎号不为空")
    private String engineno;

    private String standardname;

    private String standardfullname;

    @NotNull(message = "需要提供车价")
    private Double carprice;

    @NotNull(message = "初登日期不为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date registerdate;

    @NotNull(message = "需要提供延保开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date startdate;

    @NotNull(message = "需要提供延保结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date enddate;
    @Null
    private Double amount;

    @NotNull(message = "需要提供延保方案")
    @Range(min = 0, max = 2, message = "延保方案的值为0(全面保修)、1(三大部件保修)、2(基础保修)")
    private Integer extendwarrantytype;
    @NotNull(message = "原厂保修期不能为空")
    private int origialwarrantyperiod;
    @NotNull(message = "车牌号不能为空")
    private String plateNumber;

    private String cifstandardname;

    public String getCifstandardname() {
        return cifstandardname;
    }

    public void setCifstandardname(String cifstandardname) {
        this.cifstandardname = cifstandardname;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int getOrigialwarrantyperiod() {
        return origialwarrantyperiod;
    }

    public void setOrigialwarrantyperiod(int origialwarrantyperiod) {
        this.origialwarrantyperiod = origialwarrantyperiod;
    }

    public Integer getExtendwarrantytype() {
        return extendwarrantytype;
    }

    public void setExtendwarrantytype(Integer extendwarrantytype) {
        this.extendwarrantytype = extendwarrantytype;
    }

    public String getVincode() {
        return vincode;
    }

    public void setVincode(String vincode) {
        this.vincode = vincode;
    }

    public String getEngineno() {
        return engineno;
    }

    public void setEngineno(String engineno) {
        this.engineno = engineno;
    }

    public String getStandardname() {
        return standardname;
    }

    public void setStandardname(String standardname) {
        this.standardname = standardname;
    }

    public Double getCarprice() {
        return carprice;
    }

    public void setCarprice(Double carprice) {
        this.carprice = carprice;
    }

    public Date getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(Date registerdate) {
        this.registerdate = registerdate;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStandardfullname() {
        return standardfullname;
    }

    public void setStandardfullname(String standardfullname) {
        this.standardfullname = standardfullname;
    }

    public String getCarinfoid() {
        return carinfoid;
    }

    public void setCarinfoid(String carinfoid) {
        this.carinfoid = carinfoid;
    }
}
