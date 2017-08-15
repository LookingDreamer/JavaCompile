package com.zzb.extra.entity;
/**
 * Created by Administrator on 2016/12/5.
 */
public class ChannelTask {

    /**
     * Created by Administrator on 2016/6/15.
     */
        private long id;

        /*task表的ID*/
        private String taskId;
        private String prvId;
        private String channelId;
        private String taskState;
        private String taskStateDescription;
        private String delivery;
        private String payState;
        private String insureAmount;
        private String quoteAmount;
        private String firstPayAmount;
        private String secondPayAmount;
        private String diffAmount;
        private String mark;
        private String quoteValidTime;
        /*创建时间*/
        private String createTime;
        /*修改时间*/
        private String modifyTime;

        private String prvName;//保险公司名称
        private String carLicenseNo;//车牌号
        private String insureName;//被保险人
        private String channelUserId;//渠道用户ID


        /**备注信息/错误提醒**/
        private String msg;
        /**车辆使用性质**/
        private String carProperty;
        /**收件人手机号码**/
        private String mobile;
        /**商业险起保日期**/
        private String bizStartDate;
        /**商业险终止日期**/
        private String bizEndDate;
        /**交强险起保日期**/
        private String efcStartDate;
        /**交强险终止日期**/
        private String efcEndDate;
        /**商业险保单号**/
        private String bizPolicyNo;
        /**交强险保单号**/
        private String efcPolicyNo;
        /**交强险保额**/
        private String efcAmount;
        /**交强险保费**/
        private String efcPremium;
        /**车船税保费**/
        private String taxFee;
        /**车船税滞纳金**/
        private String taxLateFee;
        /**商业险折扣率**/
        private String bizBiscountRate;
        /**交强险折扣率**/
        private String efcBiscountRate;
        /**商业险总保费**/
        private String bizPremium;
        /**商业险不计免赔总保费**/
        private String bizNfcPremium;

        public String getBizNfcPremium() {
            return bizNfcPremium;
        }

        public void setBizNfcPremium(String bizNfcPremium) {
            this.bizNfcPremium = bizNfcPremium;
        }

        public String getBizPremium() {
            return bizPremium;
        }

        public void setBizPremium(String bizPremium) {
            this.bizPremium = bizPremium;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
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

        public String getTaskState() {
            return taskState;
        }

        public void setTaskState(String taskState) {
            this.taskState = taskState;
        }

        public String getTaskStateDescription() {
            return taskStateDescription;
        }

        public String getQuoteValidTime() {
            return quoteValidTime;
        }

        public void setQuoteValidTime(String quoteValidTime) {
            this.quoteValidTime = quoteValidTime;
        }

        public void setTaskStateDescription(String taskStateDescription) {
            this.taskStateDescription = taskStateDescription;
        }

        public String getInsureAmount() {
            return insureAmount;
        }

        public void setInsureAmount(String insureAmount) {
            this.insureAmount = insureAmount;
        }

        public String getQuoteAmount() {
            return quoteAmount;
        }

        public void setQuoteAmount(String quoteAmount) {
            this.quoteAmount = quoteAmount;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
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

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getPayState() {
            return payState;
        }

        public void setPayState(String payState) {
            this.payState = payState;
        }

        public String getDiffAmount() {
            return diffAmount;
        }

        public void setDiffAmount(String diffAmount) {
            this.diffAmount = diffAmount;
        }

        public String getFirstPayAmount() {
            return firstPayAmount;
        }

        public void setFirstPayAmount(String firstPayAmount) {
            this.firstPayAmount = firstPayAmount;
        }

        public String getSecondPayAmount() {
            return secondPayAmount;
        }

        public void setSecondPayAmount(String secondPayAmount) {
            this.secondPayAmount = secondPayAmount;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }


        public String getPrvName() {
            return prvName;
        }

        public void setPrvName(String prvName) {
            this.prvName = prvName;
        }

        public String getCarLicenseNo() {
            return carLicenseNo;
        }

        public void setCarLicenseNo(String carLicenseNo) {
            this.carLicenseNo = carLicenseNo;
        }

        public String getInsureName() {
            return insureName;
        }

        public void setInsureName(String insureName) {
            this.insureName = insureName;
        }

        public String getChannelUserId() {
            return channelUserId;
        }

        public void setChannelUserId(String channelUserId) {
            this.channelUserId = channelUserId;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCarProperty() {
            return carProperty;
        }

        public void setCarProperty(String carProperty) {
            this.carProperty = carProperty;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getBizStartDate() {
            return bizStartDate;
        }

        public void setBizStartDate(String bizStartDate) {
            this.bizStartDate = bizStartDate;
        }

        public String getBizEndDate() {
            return bizEndDate;
        }

        public void setBizEndDate(String bizEndDate) {
            this.bizEndDate = bizEndDate;
        }

        public String getEfcStartDate() {
            return efcStartDate;
        }

        public void setEfcStartDate(String efcStartDate) {
            this.efcStartDate = efcStartDate;
        }

        public String getEfcEndDate() {
            return efcEndDate;
        }

        public void setEfcEndDate(String efcEndDate) {
            this.efcEndDate = efcEndDate;
        }

        public String getBizPolicyNo() {
            return bizPolicyNo;
        }

        public void setBizPolicyNo(String bizPolicyNo) {
            this.bizPolicyNo = bizPolicyNo;
        }

        public String getEfcPolicyNo() {
            return efcPolicyNo;
        }

        public void setEfcPolicyNo(String efcPolicyNo) {
            this.efcPolicyNo = efcPolicyNo;
        }

        public String getEfcAmount() {
            return efcAmount;
        }

        public void setEfcAmount(String efcAmount) {
            this.efcAmount = efcAmount;
        }

        public String getEfcPremium() {
            return efcPremium;
        }

        public void setEfcPremium(String efcPremium) {
            this.efcPremium = efcPremium;
        }

        public String getTaxFee() {
            return taxFee;
        }

        public void setTaxFee(String taxFee) {
            this.taxFee = taxFee;
        }

        public String getTaxLateFee() {
            return taxLateFee;
        }

        public void setTaxLateFee(String taxLateFee) {
            this.taxLateFee = taxLateFee;
        }

        public String getBizBiscountRate() {
            return bizBiscountRate;
        }

        public void setBizBiscountRate(String bizBiscountRate) {
            this.bizBiscountRate = bizBiscountRate;
        }

        public String getEfcBiscountRate() {
            return efcBiscountRate;
        }

        public void setEfcBiscountRate(String efcBiscountRate) {
            this.efcBiscountRate = efcBiscountRate;
        }


}
