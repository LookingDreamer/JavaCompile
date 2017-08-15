package com.zzb.cm.Interface.entity.car.model; /**
 *
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 车辆信息对象
 *
 * @author austinChen
 *         created at 2015年6月12日 下午5:11:52
 */
public class CarInfo implements ToMapInterface, Serializable {


	private String id;
	/*新车标志*/
    private Boolean isNew;
    /*未上牌标记*/
    private Boolean noLicenseFlag;
    /*车牌号*/
    private String plateNum;
    /*号牌颜色*/
    private Integer plateColor;
    /*号牌种类*/
    private Integer plateType;
    /*玻璃类型*/
    private Integer glassType;
    /*车架号*/
    private String vin;
    /*发动机号*/
    private String engineNum;
    /*交管车辆类型*/
    private Integer jgVehicleType;
    /*使用性质*/
    private Integer useProps;
    /*车辆用户类型*/
    private Integer carUserType;
    /*初登日期*/
    private Date firstRegDate;
    /*检验有效日期*/
    private Date ineffectualDate;
    /*强制有效期*/
    private Date rejectDate;
    /*最近定检日期*/
    private Date lastCheckDate;

    /*是过户车*/
    private Boolean isTransfer;
    /*过户转移登记日期*/
    private Date transferDate;
    /*车型品牌名称*/
    private String carBrandName;
    /*车系*/
    private String familyName;
    /*车型名称*/
    private String carModelName;
    /*车型信息*/
    private Map<String, Object> carModelMisc = new HashMap<String, Object>();
    /*人保车型名称*/
    private String rbCarModelName;
    /*精友车型名称*/
    private String jyCarModelName;
    /*内部车型code*/
    private String bwCode;
    /*精友车型code*/
    private String jyCode;
    /*人保车型code*/
    private String rbCode;
    /*是否人保code匹配*/
    private Boolean isRbMatch;
    /*是否精友code匹配*/
    private Boolean isJyMatch;
    /*保险公司车型Code*/
    private String insuranceCode;
    /**
     * 不含税价
     */
    private BigDecimal price;

    /**
     * 新车购置价
     */
    private BigDecimal taxPrice;
    /*含税类比价*/
    private BigDecimal taxAnalogyPrice;
    /*不含税类比价*/
    private BigDecimal analogyPrice;

    /**
     * 排气量
     */
    private BigDecimal displacement;

    /**
     * 载重量
     */
    private BigDecimal modelLoad;

    /**
     * 车身自重
     */
    private BigDecimal fullLoad;

    /**
     * 营业转非营业
     */
    private Boolean isChangeKind;

    /**
     * 平均行驶里程
     */
    private BigDecimal avgMileage;
    /*约定行驶区域*/
    private Integer driverArea;
    /*产地类型*/
    private Integer carVehicleOrigin;
    /*是否贷款车*/
    private Boolean isLoansCar;
    /*车辆用途*/
    private Integer carVehicularApplications;
    /*是否军牌或外地车*/
    private Boolean isFieldCar;
    /*车身颜色*/
    private Integer carBodyColor;
    /*座位数*/
    private Integer seatCnt;
    /*核定载客人数*/
    private Integer ratedPassengerCapacity;
    /*准牵引总质量*/
    private BigDecimal haulage;
    /*杂项*/
    private String misc;
    /*是否车贷投保多年标志*/
    private Boolean loanManyYearsFlag;

    /*车价选择，0-最低，1-最高，2-自定义*/
    private String carPriceType;
    /*自定义车价，如果车价选择为2的时候该字段为自定义的车价，其它的车价选择，该字段为0*/
    private BigDecimal definedCarPrice;

    private Date created;
    private Date updated;
    
    private String listedYear;//年款

    private String syvehicletypename;//商业险车辆名称

    /**
     * 保险配置是否与上年一致(1：是，其他值：否)
     */
    private String insureconfigsameaslastyear;

    public String getInsureconfigsameaslastyear() {
        return insureconfigsameaslastyear;
    }

    public void setInsureconfigsameaslastyear(String insureconfigsameaslastyear) {
        this.insureconfigsameaslastyear = insureconfigsameaslastyear;
    }

    public String getSyvehicletypename() {
		return syvehicletypename;
	}

	public void setSyvehicletypename(String syvehicletypename) {
		this.syvehicletypename = syvehicletypename;
	}
	/**
	 * 机动车编码
	 */
	private String syvehicletypecode;
	
	public String getSyvehicletypecode() {
		return syvehicletypecode;
	}

	public void setSyvehicletypecode(String syvehicletypecode) {
		this.syvehicletypecode = syvehicletypecode;
	}

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -1944220078598289985L;


    @Override
    public Map<String, Object> toMap() {
        return null;
    }

    public BigDecimal getAvgMileage() {
        return avgMileage;
    }

    public void setAvgMileage(BigDecimal avgMileage) {
        this.avgMileage = avgMileage;
    }

    public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public String getBwCode() {
        return bwCode;
    }

    public void setBwCode(String bwCode) {
        this.bwCode = bwCode;
    }

    public Integer getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(Integer plateColor) {
		this.plateColor = plateColor;
	}

	public Integer getPlateType() {
		return plateType;
	}

	public void setPlateType(Integer plateType) {
		this.plateType = plateType;
	}

	public Integer getGlassType() {
		return glassType;
	}

	public void setGlassType(Integer glassType) {
		this.glassType = glassType;
	}

	public Integer getJgVehicleType() {
		return jgVehicleType;
	}

	public void setJgVehicleType(Integer jgVehicleType) {
		this.jgVehicleType = jgVehicleType;
	}

	public Integer getUseProps() {
		return useProps;
	}

	public void setUseProps(Integer useProps) {
		this.useProps = useProps;
	}

	public Integer getCarUserType() {
		return carUserType;
	}

	public void setCarUserType(Integer carUserType) {
		this.carUserType = carUserType;
	}

	public String getCarBrandName() {
		return carBrandName;
	}

	public void setCarBrandName(String carBrandName) {
		this.carBrandName = carBrandName;
	}

	public Integer getDriverArea() {
		return driverArea;
	}

	public void setDriverArea(Integer driverArea) {
		this.driverArea = driverArea;
	}

	public Integer getCarVehicleOrigin() {
		return carVehicleOrigin;
	}

	public void setCarVehicleOrigin(Integer carVehicleOrigin) {
		this.carVehicleOrigin = carVehicleOrigin;
	}

	public Integer getCarVehicularApplications() {
		return carVehicularApplications;
	}

	public void setCarVehicularApplications(Integer carVehicularApplications) {
		this.carVehicularApplications = carVehicularApplications;
	}

	public Integer getCarBodyColor() {
		return carBodyColor;
	}

	public void setCarBodyColor(Integer carBodyColor) {
		this.carBodyColor = carBodyColor;
	}

	public Boolean getLoanManyYearsFlag() {
		return loanManyYearsFlag;
	}

	public void setLoanManyYearsFlag(Boolean loanManyYearsFlag) {
		this.loanManyYearsFlag = loanManyYearsFlag;
	}

	public Map<String, Object> getCarModelMisc() {
        return carModelMisc;
    }

    public void setCarModelMisc(Map<String, Object> carModelMisc) {
        this.carModelMisc = carModelMisc;
    }

    public String getCarModelName() {
        return carModelName;
    }

    public void setCarModelName(String carModelName) {
        this.carModelName = carModelName;
    }

    public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyname) {
		this.familyName = familyname;
	}

	public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public BigDecimal getDisplacement() {
        return displacement;
    }

    public void setDisplacement(BigDecimal displacement) {
        this.displacement = displacement;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public Date getFirstRegDate() {
        return firstRegDate;
    }

    public void setFirstRegDate(Date firstRegDate) {
        this.firstRegDate = firstRegDate;
    }

    public BigDecimal getFullLoad() {
        return fullLoad;
    }

    public void setFullLoad(BigDecimal fullLoad) {
        this.fullLoad = fullLoad;
    }

    public BigDecimal getHaulage() {
        return haulage;
    }

    public void setHaulage(BigDecimal haulage) {
        this.haulage = haulage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getIneffectualDate() {
        return ineffectualDate;
    }

    public void setIneffectualDate(Date ineffectualDate) {
        this.ineffectualDate = ineffectualDate;
    }

    public String getInsuranceCode() {
        return insuranceCode;
    }

    public void setInsuranceCode(String insuranceCode) {
        this.insuranceCode = insuranceCode;
    }

    public Boolean getIsChangeKind() {
        return isChangeKind;
    }

    public void setIsChangeKind(Boolean isChangeKind) {
        this.isChangeKind = isChangeKind;
    }

    public Boolean getIsFieldCar() {
        return isFieldCar;
    }

    public void setIsFieldCar(Boolean isFieldCar) {
        this.isFieldCar = isFieldCar;
    }

    public Boolean getIsJyMatch() {
        return isJyMatch;
    }

    public void setIsJyMatch(Boolean isJyMatch) {
        this.isJyMatch = isJyMatch;
    }

    public Boolean getIsLoansCar() {
        return isLoansCar;
    }

    public void setIsLoansCar(Boolean isLoansCar) {
        this.isLoansCar = isLoansCar;
    }

    public Boolean getIsRbMatch() {
        return isRbMatch;
    }

    public void setIsRbMatch(Boolean isRbMatch) {
        this.isRbMatch = isRbMatch;
    }

    public Boolean getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(Boolean isTransfer) {
        this.isTransfer = isTransfer;
    }


    public String getJyCarModelName() {
        return jyCarModelName;
    }

    public void setJyCarModelName(String jyCarModelName) {
        this.jyCarModelName = jyCarModelName;
    }

    public String getJyCode() {
        return jyCode;
    }

    public void setJyCode(String jyCode) {
        this.jyCode = jyCode;
    }

    public Date getLastCheckDate() {
        return lastCheckDate;
    }

    public void setLastCheckDate(Date lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    public Boolean getIsLoanManyYearsFlag() {
        return loanManyYearsFlag;
    }

    public void setIsLoanManyYearsFlag(Boolean loanManyYearsFlag) {
        this.loanManyYearsFlag = loanManyYearsFlag;
    }

    public String getMisc() {
		return misc;
	}

	public void setMisc(String misc) {
		this.misc = misc;
	}

	public BigDecimal getModelLoad() {
        return modelLoad;
    }

    public void setModelLoad(BigDecimal modelLoad) {
        this.modelLoad = modelLoad;
    }

    public Boolean getNoLicenseFlag() {
        return noLicenseFlag;
    }

    public void setNoLicenseFlag(Boolean noLicenseFlag) {
        this.noLicenseFlag = noLicenseFlag;
    }


    public String getPlateNum() {
		return plateNum;
	}

	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}

	public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getRatedPassengerCapacity() {
		return ratedPassengerCapacity;
	}

	public void setRatedPassengerCapacity(Integer ratedPassengerCapacity) {
		this.ratedPassengerCapacity = ratedPassengerCapacity;
	}

	public String getRbCarModelName() {
        return rbCarModelName;
    }

    public void setRbCarModelName(String rbCarModelName) {
        this.rbCarModelName = rbCarModelName;
    }

    public String getRbCode() {
        return rbCode;
    }

    public void setRbCode(String rbCode) {
        this.rbCode = rbCode;
    }

    public Date getRejectDate() {
        return rejectDate;
    }

    public void setRejectDate(Date rejectDate) {
        this.rejectDate = rejectDate;
    }

    public Integer getSeatCnt() {
        return seatCnt;
    }

    public void setSeatCnt(Integer seatCnt) {
        this.seatCnt = seatCnt;
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

	public BigDecimal getTaxAnalogyPrice() {
		return taxAnalogyPrice;
	}

	public void setTaxAnalogyPrice(BigDecimal taxAnalogyPrice) {
		this.taxAnalogyPrice = taxAnalogyPrice;
	}

	public BigDecimal getAnalogyPrice() {
		return analogyPrice;
	}

	public void setAnalogyPrice(BigDecimal analogyPrice) {
		this.analogyPrice = analogyPrice;
	}

	public String getCarPriceType() {
		return carPriceType;
	}

	public void setCarPriceType(String carPriceType) {
		this.carPriceType = carPriceType;
	}

	public BigDecimal getDefinedCarPrice() {
		return definedCarPrice;
	}

	public void setDefinedCarPrice(BigDecimal definedCarPrice) {
		this.definedCarPrice = definedCarPrice;
	}

	public String getListedYear() {
		return listedYear;
	}

	public void setListedYear(String listedYear) {
		this.listedYear = listedYear;
	}
    
}
