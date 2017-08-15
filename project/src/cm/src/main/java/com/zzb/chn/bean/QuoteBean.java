package com.zzb.chn.bean;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class QuoteBean {
    private String insureAreaCode;
    private String taskId;
    private String prvId;
    private String prvName;
    private String respCode;
    private String accessToken;
    private String expiresTime;
    private String errorMsg;
    private String totalSize;
    private String pageNum;
    private String pageSize;
    private String channelId;
    private String channelSecret;
    private String callbackurl;
    private String channelUserId;
    private String taskState;
    private String taskStateDescription;
    private String payUrl;
    private String createTime;
    private String modifyTime;
    private String agreementProvCode;
    private String retUrl;
    private String remark;
    private String msgType; //作为入参：FirstPayLastInsure-insbQuotetotalinfo的SourceFrom标志支付前置流程；
    						//作为出参：errorMsg的类型，00-[成功]非工作时间提醒；01-[失败]上传影像提醒；02-[失败]北京验证码流程；
    private String innerAuthCode; //渠道访问内嵌首页的授权码
    private String innerShowTitle; //渠道内嵌页面是否显示标题 Y-是 N-否
    private String diffAmount; //核保金额-报价金额

    private List<CarModelInfoBean> carModelInfos;
    private CarInfoBean carInfo;
    private PersonBean carOwner;
    private InsureInfoBean insureInfo;
    private PersonBean applicant;
    private PersonBean insured;
    private PersonBean beneficiary;
    private List<PersonBean> beneficiaries;
    private List<PersonBean> insureds;
    private List<DriverBean> drivers;
    private List<ProviderBean> providers;
    private PersonBean personInfo;
    private DrivingInfoBean drivingInfo;
    private DeliveryBean delivery;
    private ImageInfoBean imageInfo;
    private List<ImageInfoBean> imageInfos;
    private List<InsureSupplyBean> insureSupplys;
    private String payValidTime;
    private String quoteValidTime;
    private List<AgreementAreaBean> agreementAreas;
    private DeptInfoBean deptInfo;
    private InvoiceInfoBean invoiceInfo; //发票信息
    
    /** 北京平台流程 start **/
    //0-正在获取中，1-成功，2-姓名身份证信息错误，3-手机号绑定超限，4-身份证绑定超限，5-代码异常
    private String applyStatus;
    //0-正在提交中，1-验证码提交成功，2-验证码有误重新输入，3-验证码提交失败，4-验证码超时重新申请，5-代码异常
    private String commitStatus;
    private String pinCode; //验证码
    private IDCardBean insuredIdCard; //被保人身份证信息
    /** 北京平台流程 end **/
    
    public List<InsureSupplyBean> getInsureSupplys() {
		return insureSupplys;
	}

	public void setInsureSupplys(List<InsureSupplyBean> insureSupplys) {
		this.insureSupplys = insureSupplys;
	}
	
    public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

	public String getCommitStatus() {
		return commitStatus;
	}

	public void setCommitStatus(String commitStatus) {
		this.commitStatus = commitStatus;
	}
    
    public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public IDCardBean getInsuredIdCard() {
		return insuredIdCard;
	}

	public void setInsuredIdCard(IDCardBean insuredIdCard) {
		this.insuredIdCard = insuredIdCard;
	}

	public InvoiceInfoBean getInvoiceInfo() {
		return invoiceInfo;
	}

	public void setInvoiceInfo(InvoiceInfoBean invoiceInfo) {
		this.invoiceInfo = invoiceInfo;
	}

	public DeptInfoBean getDeptInfo() {
        return deptInfo;
    }

    public void setDeptInfo(DeptInfoBean deptInfo) {
        this.deptInfo = deptInfo;
    }

    public String getRetUrl() {
        return retUrl;
    }

    public void setRetUrl(String retUrl) {
        this.retUrl = retUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getChannelSecret() {
        return channelSecret;
    }

    public void setChannelSecret(String channelSecret) {
        this.channelSecret = channelSecret;
    }

    public ImageInfoBean getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfoBean imageInfo) {
        this.imageInfo = imageInfo;
    }

    public List<ImageInfoBean> getImageInfos() {
        return imageInfos;
    }

    public void setImageInfos(List<ImageInfoBean> imageInfos) {
        this.imageInfos = imageInfos;
    }

    public DeliveryBean getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryBean delivery) {
        this.delivery = delivery;
    }

    public PersonBean getInsured() {
        return insured;
    }

    public void setInsured(PersonBean insured) {
        this.insured = insured;
    }

    public PersonBean getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(PersonBean beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId) {
        this.channelUserId = channelUserId;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public PersonBean getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonBean personInfo) {
        this.personInfo = personInfo;
    }

    public DrivingInfoBean getDrivingInfo() {
        return drivingInfo;
    }

    public void setDrivingInfo(DrivingInfoBean drivingInfo) {
        this.drivingInfo = drivingInfo;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(String expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getInsureAreaCode() {
        return insureAreaCode;
    }

    public void setInsureAreaCode(String insureAreaCode) {
        this.insureAreaCode = insureAreaCode;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getPrvId() {
        return prvId;
    }

    public void setPrvId(String prvId) {
        this.prvId = prvId;
    }

    public List<CarModelInfoBean> getCarModelInfos() {
        return carModelInfos;
    }

    public void setCarModelInfos(List<CarModelInfoBean> carModelInfos) {
        this.carModelInfos = carModelInfos;
    }

    public CarInfoBean getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfoBean carInfo) {
        this.carInfo = carInfo;
    }

    public PersonBean getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(PersonBean carOwner) {
        this.carOwner = carOwner;
    }

    public InsureInfoBean getInsureInfo() {
        return insureInfo;
    }

    public void setInsureInfo(InsureInfoBean insureInfo) {
        this.insureInfo = insureInfo;
    }

    public PersonBean getApplicant() {
        return applicant;
    }

    public void setApplicant(PersonBean applicant) {
        this.applicant = applicant;
    }

    public List<PersonBean> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<PersonBean> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public List<PersonBean> getInsureds() {
        return insureds;
    }

    public void setInsureds(List<PersonBean> insureds) {
        this.insureds = insureds;
    }

    public List<DriverBean> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<DriverBean> drivers) {
        this.drivers = drivers;
    }

    public List<ProviderBean> getProviders() {
        return providers;
    }

    public void setProviders(List<ProviderBean> providers) {
        this.providers = providers;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskStateDescription() {
        return taskStateDescription;
    }

    public void setTaskStateDescription(String taskStateDescription) {
        this.taskStateDescription = taskStateDescription;
    }

    public String getPrvName() {
        return prvName;
    }

    public void setPrvName(String prvName) {
        this.prvName = prvName;
    }

    public String getPayValidTime() {
        return payValidTime;
    }

    public void setPayValidTime(String payValidTime) {
        this.payValidTime = payValidTime;
    }

    public String getQuoteValidTime() {
        return quoteValidTime;
    }

    public void setQuoteValidTime(String quoteValidTime) {
        this.quoteValidTime = quoteValidTime;
    }

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }

    public List<AgreementAreaBean> getAgreementAreas() {
        return agreementAreas;
    }

    public void setAgreementAreas(List<AgreementAreaBean> agreementAreas) {
        this.agreementAreas = agreementAreas;
    }

    public String getAgreementProvCode() {
        return agreementProvCode;
    }

    public void setAgreementProvCode(String agreementProvCode) {
        this.agreementProvCode = agreementProvCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getInnerAuthCode() {
		return innerAuthCode;
	}

	public void setInnerAuthCode(String innerAuthCode) {
		this.innerAuthCode = innerAuthCode;
	}

	public String getInnerShowTitle() {
		return innerShowTitle;
	}

	public void setInnerShowTitle(String innerShowTitle) {
		this.innerShowTitle = innerShowTitle;
	}

	public String getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(String diffAmount) {
		this.diffAmount = diffAmount;
	}
    
}
