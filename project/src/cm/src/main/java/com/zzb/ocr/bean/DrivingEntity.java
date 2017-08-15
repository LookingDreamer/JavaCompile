package com.zzb.ocr.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by chengxt1202 on 15/11/21. 
 */
@XmlRootElement(name = "data")
public class DrivingEntity {

	private String cardno;
	private String name;
	private String address;
	private String vehicleType;
	private String useCharacte;
	private String model;
	private String vin;
	private String enginePN;
	private String registerDate;
	private String issuedate;

	public String getIssuedate() {
		return issuedate;
	}

	public void setIssuedate(String issuedate) {
		this.issuedate = issuedate;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@XmlElement(name = "vehicletype")
	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	@XmlElement(name = "usecharacte")
	public String getUseCharacte() {
		return useCharacte;
	}

	public void setUseCharacte(String useCharacte) {
		this.useCharacte = useCharacte;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	@XmlElement(name = "enginepn")
	public String getEnginePN() {
		return enginePN;
	}

	public void setEnginePN(String enginePN) {
		this.enginePN = enginePN;
	}

	@XmlElement(name = "registerdate")
	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
}
