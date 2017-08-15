package com.zzb.cm.Interface.entity.cif.model;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class IntendToPay extends BaseEntity implements Identifiable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String paymentMethod;
	String merchantId;
	String enableChanged;
	String thirdPartyMerchantId;
	String txType;
	String payeeCode;
	String paymentTarget;
	String policyno;
	String source;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getEnableChanged() {
		return enableChanged;
	}
	public void setEnableChanged(String enableChanged) {
		this.enableChanged = enableChanged;
	}
	public String getThirdPartyMerchantId() {
		return thirdPartyMerchantId;
	}
	public void setThirdPartyMerchantId(String thirdPartyMerchantId) {
		this.thirdPartyMerchantId = thirdPartyMerchantId;
	}
	public String getTxType() {
		return txType;
	}
	public void setTxType(String txType) {
		this.txType = txType;
	}
	public String getPayeeCode() {
		return payeeCode;
	}
	public void setPayeeCode(String payeeCode) {
		this.payeeCode = payeeCode;
	}
	public String getPaymentTarget() {
		return paymentTarget;
	}
	public void setPaymentTarget(String paymentTarget) {
		this.paymentTarget = paymentTarget;
	}
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	
	
	
	
	
	
}
