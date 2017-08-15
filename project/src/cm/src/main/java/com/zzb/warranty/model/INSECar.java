package com.zzb.warranty.model;

import com.cninsure.core.dao.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class INSECar extends BaseModel implements Identifiable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    private String taskid;

    /**
     * 已选择保险配置
     */
    private String selectconfig;

    /**
     * 已选择保险公司
     */
    private String selectins;

    private INSEPerson owner;

    /**
     * 车主id
     */
    private String ownerid;

    /**
     * 车主姓名
     */
    private String ownername;

    /**
     * 联系方式
     */
    private String phonenumber;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 车牌号
     */
    private String carlicenseno;

    /**
     * 车辆识别代号
     */
    private String vincode;

    /**
     * 车型信息描述
     */
    private String standardfullname;

    /**
     *
     */
    private String engineno;

    /**
     * 车辆初始登记日期
     */
    private Date registdate;

    /**
     * 注册许可证
     */
    private String registlicense;

    /**
     * 过户日期
     */
    private Date transferdate;

    /**
     * 过户车
     */
    private String isTransfercar;

    /**
     * 是否指定驾驶人
     */
    private String Specifydriver;

    /**
     * 行驶区域
     */
    private String drivingarea;

    /**
     * 平均行驶里程
     */
    private String mileage;

    /**
     * 事故次数
     */
    private Integer accidentnum;

    /**
     * 车辆编号
     */
    private Integer carid;

    /**
     * 使用年限
     */
    private Integer useyears;

    /**
     * 签署时间
     */
    private Date signdate;

    /**
     * 固定停放地点
     */
    private String parksite;

    /**
     * 上年商业承保公司
     */
    private String preinscode;

    /**
     * 任务状态
     */
    private String taskstatus;

    /**
     * 所属性质
     */
    private String property;

    /**
     * 车辆性质
     */
    private String carproperty;

    /**
     * 起保日期
     */
    private Date businessstartdate;

    /**
     * 终止日期
     */
    private Date businessenddate;

    /**
     *
     */
    private Date Strongstartdate;

    /**
     *
     */
    private Date Strongenddate;

    /**
     *
     */
    private String cloudinscompany;

    /**
     *
     */
    private String islicense;

    /**
     * 保险公司代码
     */
    private String inscomcode;

    /**
     * 号牌颜色
     */
    private Integer platecolor;

    /**
     * 号牌种类
     */
    private Integer plateType;

    /**
     * 检验有效日期
     */
    private Date ineffectualDate;

    /**
     * 强制有效期
     */
    private Date rejectDate;

    /**
     * 最近定检日期
     */
    private Date lastCheckDate;

    /**
     * 上否贷款车
     */
    private String isLoansCar;

    /**
     * 车辆用途
     */
    private Integer carVehicularApplications;

    /**
     * 是否军牌外地车
     */
    private String isFieldCar;

    /**
     * 车身颜色
     */
    private Integer carBodyColor;

    /**
     * 上否车贷投保多年标志
     */
    private String loanManyYearsFlag;

    /**
     * 新车标致
     */
    private String isNew;

    /**
     * 车辆折旧价
     */
    private Double depprice;
    /**
     * 车辆浮动价
     */
    private Double floatprice;
    /**
     * 是高危限制车型 0不是  1是
     */
    private String isdanger;
    /**
     * 是易盗车型 0不是 1 是
     */
    private String isrob;
    /**
     * 是管控车型 0不是 1 是
     */
    private String isspecial;
    /**
     * 车辆浮动价上限
     */
    private Double maxprice;
    /**
     * 车辆浮动价上限比率
     */
    private Double maxpricerate;
    /**
     * 车辆浮动价下限
     */
    private Double minprice;
    /**
     * 车辆浮动价下限比率
     */
    private Double minpricerate;
    /**
     * 新车购置价
     */
    private Double taxprice;
    /**
     * 行驶证图片路径
     */
    private String drivinglicenseurl;
    /**
     * 车辆照片（平台提供）
     */
    private String brandimg;
    /**
     * 平台查询传过来的车辆品牌
     */
    private String cifstandardname;
    /**
     * 保险配置是否与上年一致(1：是，其他值：否)
     */
    private String insureconfigsameaslastyear;

    private Integer origialwarrantyperiod;

    public String getCifstandardname() {
        return cifstandardname;
    }

    public void setCifstandardname(String cifstandardname) {
        this.cifstandardname = cifstandardname;
    }

    public String getBrandimg() {
        return brandimg;
    }

    public void setBrandimg(String brandimg) {
        this.brandimg = brandimg;
    }
    public String getDrivinglicenseurl() {
        return drivinglicenseurl;
    }

    public void setDrivinglicenseurl(String drivinglicenseurl) {
        this.drivinglicenseurl = drivinglicenseurl;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getSelectconfig() {
        return selectconfig;
    }

    public void setSelectconfig(String selectconfig) {
        this.selectconfig = selectconfig;
    }

    public String getSelectins() {
        return selectins;
    }

    public void setSelectins(String selectins) {
        this.selectins = selectins;
    }

    public INSEPerson getOwner() {
        return owner;
    }

    public void setOwner(INSEPerson owner) {
        this.owner = owner;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarlicenseno() {
        return carlicenseno;
    }

    public void setCarlicenseno(String carlicenseno) {
        this.carlicenseno = carlicenseno;
    }

    public String getVincode() {
        return vincode;
    }

    public void setVincode(String vincode) {
        this.vincode = vincode;
    }

    public String getStandardfullname() {
        return standardfullname;
    }

    public void setStandardfullname(String standardfullname) {
        this.standardfullname = standardfullname;
    }

    public String getEngineno() {
        return engineno;
    }

    public void setEngineno(String engineno) {
        this.engineno = engineno;
    }

    public Date getRegistdate() {
        return registdate;
    }

    public void setRegistdate(Date registdate) {
        this.registdate = registdate;
    }

    public String getRegistlicense() {
        return registlicense;
    }

    public void setRegistlicense(String registlicense) {
        this.registlicense = registlicense;
    }

    public Date getTransferdate() {
        return transferdate;
    }

    public void setTransferdate(Date transferdate) {
        this.transferdate = transferdate;
    }

    public String getIsTransfercar() {
        return isTransfercar;
    }

    public void setIsTransfercar(String isTransfercar) {
        this.isTransfercar = isTransfercar;
    }

    public String getSpecifydriver() {
        return Specifydriver;
    }

    public void setSpecifydriver(String Specifydriver) {
        this.Specifydriver = Specifydriver;
    }

    public String getDrivingarea() {
        return drivingarea;
    }

    public void setDrivingarea(String drivingarea) {
        this.drivingarea = drivingarea;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public Integer getAccidentnum() {
        return accidentnum;
    }

    public void setAccidentnum(Integer accidentnum) {
        this.accidentnum = accidentnum;
    }

    public Integer getCarid() {
        return carid;
    }

    public void setCarid(Integer carid) {
        this.carid = carid;
    }

    public Integer getUseyears() {
        return useyears;
    }

    public void setUseyears(Integer useyears) {
        this.useyears = useyears;
    }

    public Date getSigndate() {
        return signdate;
    }

    public void setSigndate(Date signdate) {
        this.signdate = signdate;
    }

    public String getParksite() {
        return parksite;
    }

    public void setParksite(String parksite) {
        this.parksite = parksite;
    }

    public String getPreinscode() {
        return preinscode;
    }

    public void setPreinscode(String preinscode) {
        this.preinscode = preinscode;
    }

    public String getTaskstatus() {
        return taskstatus;
    }

    public void setTaskstatus(String taskstatus) {
        this.taskstatus = taskstatus;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getCarproperty() {
        return carproperty;
    }

    public void setCarproperty(String carproperty) {
        this.carproperty = carproperty;
    }

    public Date getBusinessstartdate() {
        return businessstartdate;
    }

    public void setBusinessstartdate(Date businessstartdate) {
        this.businessstartdate = businessstartdate;
    }

    public Date getBusinessenddate() {
        return businessenddate;
    }

    public void setBusinessenddate(Date businessenddate) {
        this.businessenddate = businessenddate;
    }

    public Date getStrongstartdate() {
        return Strongstartdate;
    }

    public void setStrongstartdate(Date Strongstartdate) {
        this.Strongstartdate = Strongstartdate;
    }

    public Date getStrongenddate() {
        return Strongenddate;
    }

    public void setStrongenddate(Date Strongenddate) {
        this.Strongenddate = Strongenddate;
    }

    public String getCloudinscompany() {
        return cloudinscompany;
    }

    public void setCloudinscompany(String cloudinscompany) {
        this.cloudinscompany = cloudinscompany;
    }

    public String getIslicense() {
        return islicense;
    }

    public void setIslicense(String islicense) {
        this.islicense = islicense;
    }

    public String getInscomcode() {
        return inscomcode;
    }

    public void setInscomcode(String inscomcode) {
        this.inscomcode = inscomcode;
    }

    public Integer getPlatecolor() {
        return platecolor;
    }

    public void setPlatecolor(Integer platecolor) {
        this.platecolor = platecolor;
    }

    public Integer getPlateType() {
        return plateType;
    }

    public void setPlateType(Integer plateType) {
        this.plateType = plateType;
    }

    public Date getIneffectualDate() {
        return ineffectualDate;
    }

    public void setIneffectualDate(Date ineffectualDate) {
        this.ineffectualDate = ineffectualDate;
    }

    public Date getRejectDate() {
        return rejectDate;
    }

    public void setRejectDate(Date rejectDate) {
        this.rejectDate = rejectDate;
    }

    public Date getLastCheckDate() {
        return lastCheckDate;
    }

    public void setLastCheckDate(Date lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    public String getIsLoansCar() {
        return isLoansCar;
    }

    public void setIsLoansCar(String isLoansCar) {
        this.isLoansCar = isLoansCar;
    }

    public Integer getCarVehicularApplications() {
        return carVehicularApplications;
    }

    public void setCarVehicularApplications(Integer carVehicularApplications) {
        this.carVehicularApplications = carVehicularApplications;
    }

    public String getIsFieldCar() {
        return isFieldCar;
    }

    public void setIsFieldCar(String isFieldCar) {
        this.isFieldCar = isFieldCar;
    }

    public Integer getCarBodyColor() {
        return carBodyColor;
    }

    public void setCarBodyColor(Integer carBodyColor) {
        this.carBodyColor = carBodyColor;
    }

    public String getLoanManyYearsFlag() {
        return loanManyYearsFlag;
    }

    public void setLoanManyYearsFlag(String loanManyYearsFlag) {
        this.loanManyYearsFlag = loanManyYearsFlag;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public Double getDepprice() {
        return depprice;
    }

    public void setDepprice(Double depprice) {
        this.depprice = depprice;
    }

    public Double getFloatprice() {
        return floatprice;
    }

    public void setFloatprice(Double floatprice) {
        this.floatprice = floatprice;
    }

    public String getIsdanger() {
        return isdanger;
    }

    public void setIsdanger(String isdanger) {
        this.isdanger = isdanger;
    }

    public String getIsrob() {
        return isrob;
    }

    public void setIsrob(String isrob) {
        this.isrob = isrob;
    }

    public String getIsspecial() {
        return isspecial;
    }

    public void setIsspecial(String isspecial) {
        this.isspecial = isspecial;
    }

    public Double getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(Double maxprice) {
        this.maxprice = maxprice;
    }

    public Double getMaxpricerate() {
        return maxpricerate;
    }

    public void setMaxpricerate(Double maxpricerate) {
        this.maxpricerate = maxpricerate;
    }

    public Double getMinprice() {
        return minprice;
    }

    public void setMinprice(Double minprice) {
        this.minprice = minprice;
    }

    public Double getMinpricerate() {
        return minpricerate;
    }

    public void setMinpricerate(Double minpricerate) {
        this.minpricerate = minpricerate;
    }

    public Double getTaxprice() {
        return taxprice;
    }

    public void setTaxprice(Double taxprice) {
        this.taxprice = taxprice;
    }

    public String getInsureconfigsameaslastyear() {
        return insureconfigsameaslastyear;
    }

    public void setInsureconfigsameaslastyear(String insureconfigsameaslastyear) {
        this.insureconfigsameaslastyear = insureconfigsameaslastyear;
    }

    public Integer getOrigialwarrantyperiod() {
        return origialwarrantyperiod;
    }

    public void setOrigialwarrantyperiod(Integer origialwarrantyperiod) {
        this.origialwarrantyperiod = origialwarrantyperiod;
    }
}