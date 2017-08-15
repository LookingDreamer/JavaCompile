package com.zzb.model;

import com.cninsure.system.entity.INSCDept;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;

public class PolicyDetailedModel {
	/**
	 * 机构出单网点
	 */
	private INSCDept inscdept;
	/**
	 * 保单表
	 */
	private INSBPolicyitem policyitem;
	/**
	 * 交强险保单
	 */
	private INSBPolicyitem jqPolicyitem;
	/**
	 * 商业险保单
	 */
	private INSBPolicyitem insbPolicyitem;
	/**
	 * 受益人
	 */
	private INSBPolicyitem benefitPolicyitem;
	
	/**
	 * 被保人表
	 */
	private INSBPolicyitem recognizeePolicyitem;
	
	/**
	 * 投保人
	 */
	private INSBPolicyitem insurePolicyitem;

	/**
	 * 联系人
	 */
	private INSBPolicyitem contactsPolicyitem;
	/**
	 * 订单
	 */
	private INSBOrder insbOrder;
	/**
	 * 代理人
	 */
	private INSBAgent agent;
	/**
	 * 车辆
	 */
	private INSBCarinfohis carinfohis;
	/**
	 * 供应商
	 */
	private INSBProvider insbProvider;
	/**
	 * 云查询保险公司
	 */
	private String cloudinscompany;
	
	/**
	 * 影像信息路径
	 */
	private String filepath;
	
	/**
	 * 影像信息名称
	 */
	private String codename;
	
	/**
	 * 投保地区（省）
	 */
	private String province;
	/**
	 * 投保地区（市）
	 */
	private String city;
	/**
	 * pictureId
	 * @return
	 */
	private String pictureId;
	/**
	 * 配送信息
	 * @return
	 */
	private INSBOrderdelivery  insbOrderdelivery;
	/**
	 * 物流公司
	 */
	private String codenamestyle;
	/**
	 * 收件人收件地址
	 */
	private String orderdeliveryaddress;
	/**
	 * 自取网点
	 */
	private String addresscodename;
	
	private String address;
	
	private String smallfilepath;

	private String miniagentcode;

	public String getMiniagentcode() {
		return miniagentcode;
	}

	public void setMiniagentcode(String miniagentcode) {
		this.miniagentcode = miniagentcode;
	}

	public String getSmallfilepath() {
		return smallfilepath;
	}
	public void setSmallfilepath(String smallfilepath) {
		this.smallfilepath = smallfilepath;
	}
	
	public String getOrderdeliveryaddress() {
		return orderdeliveryaddress;
	}
	public void setOrderdeliveryaddress(String orderdeliveryaddress) {
		this.orderdeliveryaddress = orderdeliveryaddress;
	}
	public String getAddresscodename() {
		return addresscodename;
	}
	public void setAddresscodename(String addresscodename) {
		this.addresscodename = addresscodename;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCodenamestyle() {
		return codenamestyle;
	}
	public void setCodenamestyle(String codenamestyle) {
		this.codenamestyle = codenamestyle;
	}
	public INSBOrderdelivery getInsbOrderdelivery() {
		return insbOrderdelivery;
	}

	public void setInsbOrderdelivery(INSBOrderdelivery insbOrderdelivery) {
		this.insbOrderdelivery = insbOrderdelivery;
	}

	public String getPictureId() {
		return pictureId;
	}

	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}

	public String getCloudinscompany() {
		return cloudinscompany;
	}

	public void setCloudinscompany(String cloudinscompany) {
		this.cloudinscompany = cloudinscompany;
	}


	public INSBPolicyitem getJqPolicyitem() {
		return jqPolicyitem;
	}

	public void setJqPolicyitem(INSBPolicyitem jqPolicyitem) {
		this.jqPolicyitem = jqPolicyitem;
	}
	
	public INSBProvider getInsbProvider() {
		return insbProvider;
	}

	public void setInsbProvider(INSBProvider insbProvider) {
		this.insbProvider = insbProvider;
	}

	public INSBPolicyitem getInsbPolicyitem() {
		return insbPolicyitem;
	}

	public void setInsbPolicyitem(INSBPolicyitem insbPolicyitem) {
		this.insbPolicyitem = insbPolicyitem;
	}

	public INSBOrder getInsbOrder() {
		return insbOrder;
	}

	public void setInsbOrder(INSBOrder insbOrder) {
		this.insbOrder = insbOrder;
	}
	
	public INSBAgent getAgent() {
		return agent;
	}

	public void setAgent(INSBAgent agent) {
		this.agent = agent;
	}


	public INSBCarinfohis getCarinfohis() {
		return carinfohis;
	}

	public void setCarinfohis(INSBCarinfohis carinfohis) {
		this.carinfohis = carinfohis;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getCodename() {
		return codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public INSBPolicyitem getRecognizeePolicyitem() {
		return recognizeePolicyitem;
	}

	public void setRecognizeePolicyitem(INSBPolicyitem recognizeePolicyitem) {
		this.recognizeePolicyitem = recognizeePolicyitem;
	}

	public INSBPolicyitem getInsurePolicyitem() {
		return insurePolicyitem;
	}

	public void setInsurePolicyitem(INSBPolicyitem insurePolicyitem) {
		this.insurePolicyitem = insurePolicyitem;
	}

	public INSBPolicyitem getPolicyitem() {
		return policyitem;
	}

	public void setPolicyitem(INSBPolicyitem policyitem) {
		this.policyitem = policyitem;
	}

	public INSCDept getInscdept() {
		return inscdept;
	}

	public void setInscdept(INSCDept inscdept) {
		this.inscdept = inscdept;
	}

	public INSBPolicyitem getContactsPolicyitem() {
		return contactsPolicyitem;
	}

	public void setContactsPolicyitem(INSBPolicyitem contactsPolicyitem) {
		this.contactsPolicyitem = contactsPolicyitem;
	}

	public INSBPolicyitem getBenefitPolicyitem() {
		return benefitPolicyitem;
	}

	public void setBenefitPolicyitem(INSBPolicyitem benefitPolicyitem) {
		this.benefitPolicyitem = benefitPolicyitem;
	}
}
