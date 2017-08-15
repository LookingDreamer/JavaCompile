package com.zzb.mobile.model;


public class InsuredInfoModel {
	private PassiveInsurePersionBean passiveInsurePersion;//被保人
	private InsurePersionBean insurePersion;//投保人
	private CarowerinfoBean carowerinfo;//车主
	private BusinessInsureddateBean businessInsureddate;//商业险
	private TrafficInsureddateBean trafficInsureddate;//交强险
	private String selectdrivers;//是否指定驾驶员 0是，1否
	private String  email; //邮箱
    private String  tel; //联系电话
    private String  lastComId; //上家商业承保公司ID
    private String  drivingRegion;//行驶区域, 1:出入境、2:境内、3:省内、4:场内、5:固定线路
	public PassiveInsurePersionBean getPassiveInsurePersion() {
		return passiveInsurePersion;
	}
	public void setPassiveInsurePersion(
			PassiveInsurePersionBean passiveInsurePersion) {
		this.passiveInsurePersion = passiveInsurePersion;
	}
	public InsurePersionBean getInsurePersion() {
		return insurePersion;
	}
	public void setInsurePersion(InsurePersionBean insurePersion) {
		this.insurePersion = insurePersion;
	}
	public CarowerinfoBean getCarowerinfo() {
		return carowerinfo;
	}
	public void setCarowerinfo(CarowerinfoBean carowerinfo) {
		this.carowerinfo = carowerinfo;
	}
	public BusinessInsureddateBean getBusinessInsureddate() {
		return businessInsureddate;
	}
	public void setBusinessInsureddate(BusinessInsureddateBean businessInsureddate) {
		this.businessInsureddate = businessInsureddate;
	}
	public TrafficInsureddateBean getTrafficInsureddate() {
		return trafficInsureddate;
	}
	public void setTrafficInsureddate(TrafficInsureddateBean trafficInsureddate) {
		this.trafficInsureddate = trafficInsureddate;
	}
	public String getSelectdrivers() {
		return selectdrivers;
	}
	public void setSelectdrivers(String selectdrivers) {
		this.selectdrivers = selectdrivers;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getLastComId() {
		return lastComId;
	}
	public void setLastComId(String lastComId) {
		this.lastComId = lastComId;
	}
	public String getDrivingRegion() {
		return drivingRegion;
	}
	public void setDrivingRegion(String drivingRegion) {
		this.drivingRegion = drivingRegion;
	}
    
    
}
