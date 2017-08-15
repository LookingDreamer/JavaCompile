package com.zzb.cm.Interface.entity.cif.model;

import java.util.Date;
import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


@SuppressWarnings("serial")
public class CarInfo extends BaseEntity implements Identifiable{
	String vehicleno;//车牌号
	String engineno;//发动机号
	String vehicleframeno;//车架号
	Date registerdate;//初登日期
	String claimno;//理赔次数
	String areaname;//行驶区域名称
	Double  price;//新车购置价
	String vehicletype;//车辆类型
	String brandmodel;//品牌型号
	String chinesebrand;//中文品牌
	String vehiclemodel;//车辆型号
	String manufacturer;//制造国
	String vehiclestate; //机动车状态
	String usingnaturecode;//使用性质编码
	String usingnature;//使用性质
	Date checkenddate;//检验有效期
	Date scrapdate;//强制报废期
	String enginetype;//G发动机型号
	String engines;//排量
	String vehiclecolor;//车身颜色
	String englishbrand;//英文品牌
	String domestic;//国产进口
	String mortgagestatus;  //抵押标记
	String mortgagesituation;//抵押情况
	Date licensedate;  //发行驶证日期
	Date insurancedate; //保险终止日期
	Date latelycheckdate; //最近定检日期
	Date platedate;      //发牌日期
	Date registrationdate;   //发登记证书日期
	Date qualifierdate;   //发合格证日期
	String carsource;   //carsource
	String authority;  //发证机关
	String changeplatetime; //补/换领号牌次数
	String changelicensetime; //补/换领行驶证次数
	String changecertificatetime;//补/换领证书次数
	String syssource;//来源
	String searchtime;//国政通查询次数
	String newvehicleflag;//是否新车
	String chgownerflag;//是否过户车
	String drivendistancerange;//行驶里程
	double depreciationPrice;
	double taxprice;//含税价
	String seatcount;//座位数
	String area;//行驶区域
//	Date createtime;
//	Date modifytime;

//	public Date getCreatetime() {
//		return createtime;
//	}
//	public void setCreatetime(Date createtime) {
//		this.createtime = createtime;
//	}
//	 
//	public Date getModifytime() {
//		return modifytime;
//	}
//	public void setModifytime(Date modifytime) {
//		this.modifytime = modifytime;
//	}
public double getTaxprice() {
		return taxprice;
	}
	public void setTaxprice(double taxprice) {
		this.taxprice = taxprice;
	}
public double getDepreciationPrice() {
		return depreciationPrice;
	}
	public void setDepreciationPrice(double depreciationPrice) {
		this.depreciationPrice = depreciationPrice;
	}
public String getDrivendistancerange() {
		return drivendistancerange;
	}
	public void setDrivendistancerange(String drivendistancerange) {
		this.drivendistancerange = drivendistancerange;
	}
public String getChgownerflag() {
		return chgownerflag;
	}
	public void setChgownerflag(String chgownerflag) {
		this.chgownerflag = chgownerflag;
	}
public String getNewvehicleflag() {
		return newvehicleflag;
	}
	public void setNewvehicleflag(String newvehicleflag) {
		this.newvehicleflag = newvehicleflag;
	}
	//	Date createtim;
//	Date modifytime;

	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public String getEngineno() {
		return engineno;
	}
	public void setEngineno(String engineno) {
		this.engineno = engineno;
	}
	public String getVehicleframeno() {
		return vehicleframeno;
	}
	public void setVehicleframeno(String vehicleframeno) {
		this.vehicleframeno = vehicleframeno;
	}
	public Date getRegisterdate() {
		return registerdate;
	}
	public void setRegisterdate(Date registerdate) {
		this.registerdate = registerdate;
	}
	public String getClaimno() {
		return claimno;
	}
	public void setClaimno(String claimno) {
		this.claimno = claimno;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getVehicletype() {
		return vehicletype;
	}
	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}
	public String getBrandmodel() {
		return brandmodel;
	}
	public void setBrandmodel(String brandmodel) {
		this.brandmodel = brandmodel;
	}
	public String getChinesebrand() {
		return chinesebrand;
	}
	public void setChinesebrand(String chinesebrand) {
		this.chinesebrand = chinesebrand;
	}
	public String getVehiclemodel() {
		return vehiclemodel;
	}
	public void setVehiclemodel(String vehiclemodel) {
		this.vehiclemodel = vehiclemodel;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getVehiclestate() {
		return vehiclestate;
	}
	public void setVehiclestate(String vehiclestate) {
		this.vehiclestate = vehiclestate;
	}
	public String getUsingnature() {
		return usingnature;
	}
	public void setUsingnature(String usingnature) {
		this.usingnature = usingnature;
	}
	public Date getCheckenddate() {
		return checkenddate;
	}
	public void setCheckenddate(Date checkenddate) {
		this.checkenddate = checkenddate;
	}
	public Date getScrapdate() {
		return scrapdate;
	}
	public void setScrapdate(Date scrapdate) {
		this.scrapdate = scrapdate;
	}
	public String getEnginetype() {
		return enginetype;
	}
	public void setEnginetype(String enginetype) {
		this.enginetype = enginetype;
	}
	public String getEngines() {
		return engines;
	}
	public void setEngines(String engines) {
		this.engines = engines;
	}
	public String getVehiclecolor() {
		return vehiclecolor;
	}
	public void setVehiclecolor(String vehiclecolor) {
		this.vehiclecolor = vehiclecolor;
	}
	public String getSeatcount() {
		return seatcount;
	}
	public void setSeatcount(String seatcount) {
		this.seatcount = seatcount;
	}
	public String getEnglishbrand() {
		return englishbrand;
	}
	public void setEnglishbrand(String englishbrand) {
		this.englishbrand = englishbrand;
	}
	public String getDomestic() {
		return domestic;
	}
	public void setDomestic(String domestic) {
		this.domestic = domestic;
	}
	public String getMortgagestatus() {
		return mortgagestatus;
	}
	public void setMortgagestatus(String mortgagestatus) {
		this.mortgagestatus = mortgagestatus;
	}
	public String getMortgagesituation() {
		return mortgagesituation;
	}
	public void setMortgagesituation(String mortgagesituation) {
		this.mortgagesituation = mortgagesituation;
	}
	public Date getLicensedate() {
		return licensedate;
	}
	public void setLicensedate(Date licensedate) {
		this.licensedate = licensedate;
	}
	public Date getInsurancedate() {
		return insurancedate;
	}
	public void setInsurancedate(Date insurancedate) {
		this.insurancedate = insurancedate;
	}
	public Date getLatelycheckdate() {
		return latelycheckdate;
	}
	public void setLatelycheckdate(Date latelycheckdate) {
		this.latelycheckdate = latelycheckdate;
	}
	public Date getPlatedate() {
		return platedate;
	}
	public void setPlatedate(Date platedate) {
		this.platedate = platedate;
	}
	public Date getRegistrationdate() {
		return registrationdate;
	}
	public void setRegistrationdate(Date registrationdate) {
		this.registrationdate = registrationdate;
	}
	public Date getQualifierdate() {
		return qualifierdate;
	}
	public void setQualifierdate(Date qualifierdate) {
		this.qualifierdate = qualifierdate;
	}
	public String getCarsource() {
		return carsource;
	}
	public void setCarsource(String carsource) {
		this.carsource = carsource;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getChangeplatetime() {
		return changeplatetime;
	}
	public void setChangeplatetime(String changeplatetime) {
		this.changeplatetime = changeplatetime;
	}
	public String getChangelicensetime() {
		return changelicensetime;
	}
	public void setChangelicensetime(String changelicensetime) {
		this.changelicensetime = changelicensetime;
	}
	public String getChangecertificatetime() {
		return changecertificatetime;
	}
	public void setChangecertificatetime(String changecertificatetime) {
		this.changecertificatetime = changecertificatetime;
	}
	public String getSyssource() {
		return syssource;
	}
	public void setSyssource(String syssource) {
		this.syssource = syssource;
	}
	public String getSearchtime() {
		return searchtime;
	}
	public void setSearchtime(String searchtime) {
		this.searchtime = searchtime;
	}
//	public Date getCreatetim() {
//		return createtim;
//	}
//	public void setCreatetim(Date createtim) {
//		this.createtim = createtim;
//	}
//	public Date getModifytime() {
//		return modifytime;
//	}
//	public void setModifytime(Date modifytime) {
//		this.modifytime = modifytime;
//	}
	@Override
	public String toString() {
		return "CarInfo [ vehicleno=" + vehicleno
				+ ", engineno=" + engineno + ", vehicleframeno="
				+ vehicleframeno + ", registerdate=" + registerdate
				+ ", claimno=" + claimno + ", area=" + area + ", areaname="
				+ areaname + ", price=" + price + ", vehicletype="
				+ vehicletype + ", brandmodel=" + brandmodel
				+ ", chinesebrand=" + chinesebrand + ", vehiclemodel="
				+ vehiclemodel + ", manufacturer=" + manufacturer
				+ ", vehiclestate=" + vehiclestate + ", usingnature="
				+ usingnature + ", checkenddate=" + checkenddate
				+ ", scrapdate=" + scrapdate + ", enginetype=" + enginetype
				+ ", engines=" + engines + ", vehiclecolor=" + vehiclecolor
				+ ", seatcount=" + seatcount + ", englishbrand=" + englishbrand
				+ ", domestic=" + domestic + ", mortgagestatus="
				+ mortgagestatus + ", mortgagesituation=" + mortgagesituation
				+ ", licensedate=" + licensedate + ", insurancedate="
				+ insurancedate + ", latelycheckdate=" + latelycheckdate
				+ ", platedate=" + platedate + ", registrationdate="
				+ registrationdate + ", qualifierdate=" + qualifierdate
				+ ", carsource=" + carsource + ", authority=" + authority
				+ ", changeplatetime=" + changeplatetime
				+ ", changelicensetime=" + changelicensetime
				+ ", changecertificatetime=" + changecertificatetime
				+ ", syssource=" + syssource + ", searchtime=" + searchtime
				+ ", newvehicleflag=" + newvehicleflag + ", chgownerflag="
				+ chgownerflag + ", drivendistancerange=" + drivendistancerange
				+ ", depreciationPrice=" + depreciationPrice + ", taxprice= "
				+ "taxprice ]";
	}
	public String getUsingnaturecode() {
		return usingnaturecode;
	}
	public void setUsingnaturecode(String usingnaturecode) {
		this.usingnaturecode = usingnaturecode;
	}
	
}
