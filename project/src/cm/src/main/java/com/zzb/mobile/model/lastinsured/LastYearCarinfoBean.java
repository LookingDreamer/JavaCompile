package com.zzb.mobile.model.lastinsured;

import java.io.Serializable;

public class LastYearCarinfoBean implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//车辆相关信息(carinfo)
		String vehicleno;//车牌号
//		String vehiclecode;//品牌型号(carmodel)
//		String displacement;//排气量 
//		String seatcount;//核定载人数 (座位数)?seat
//		double price;//车价选择 （新车购置价）
		String usingnature;// 车辆性质 (使用性质) 传的编码
//		String modelLoads;//核定载质量 
//		String fullWeight;//整备质量 
		String engineno;//发动机号
		String vehicleframeno;//车辆识别代号 (车架号)
		String chgownerflag;//是否过户车 过户日期 (无)
		String registerdate;//车辆初登日期 
		String area;//行驶区域 影像信息 (无)
		String vehicletype;//所属性质  0	个人用车1	企业用车2	机关团体用车 
//		Date startdate;//商业险，
//		Date enddate;
//		//交强险保险期间
//		Date jqstartdate;
//		Date jqenddate;
		String brandmodel;//车型名称
		/**
		 * 车辆照片
		 */
		private String brandimg;

		public String getBrandmodel() {
			return brandmodel;
		}
		public void setBrandmodel(String brandmodel) {
			this.brandmodel = brandmodel;
		}
		public String getBrandimg() {
			return brandimg;
		}
		public void setBrandimg(String brandimg) {
			this.brandimg = brandimg;
		}
		public String getVehicleno() {
			return vehicleno;
		}
		public String getVehicletype() {
			return vehicletype;
		}
		public void setVehicletype(String vehicletype) {
			this.vehicletype = vehicletype==null?null:vehicletype.trim();
		}
		public void setVehicleno(String vehicleno) {
			this.vehicleno = vehicleno==null?null:vehicleno.trim();
		}
//		public String getVehiclecode() {
//			return vehiclecode;
//		}
//		public void setVehiclecode(String vehiclecode) {
//			this.vehiclecode = vehiclecode;
//		}
//		public String getDisplacement() {
//			return displacement;
//		}
//		public void setDisplacement(String displacement) {
//			this.displacement = displacement;
//		}
//		public String getSeatcount() {
//			return seatcount;
//		}
//		public void setSeatcount(String seatcount) {
//			this.seatcount = seatcount;
//		}
//		public Double getPrice() {
//			return price;
//		}
//		public void setPrice(Double price) {
//			this.price = price;
//		}
		public String getUsingnature() {
			return usingnature;
		}
		public void setUsingnature(String usingnature) {
			this.usingnature = usingnature==null?null:usingnature.trim();
		}
//		public String getModelLoads() {
//			return modelLoads;
//		}
//		public void setModelLoads(String modelLoads) {
//			this.modelLoads = modelLoads;
//		}
//		public String getFullWeight() {
//			return fullWeight;
//		}
//		public void setFullWeight(String fullWeight) {
//			this.fullWeight = fullWeight;
//		}
		public String getEngineno() {
			return engineno;
		}
		public void setEngineno(String engineno) {
			this.engineno = engineno==null?null:engineno.trim();
		}
		public String getVehicleframeno() {
			return vehicleframeno;
		}
		public void setVehicleframeno(String vehicleframeno) {
			this.vehicleframeno = vehicleframeno==null?null:vehicleframeno.trim();
		}
		public String getChgownerflag() {
			return chgownerflag;
		}
		public void setChgownerflag(String chgownerflag) {
			this.chgownerflag = chgownerflag==null?null:chgownerflag.trim();
		}
		public String getRegisterdate() {
			return registerdate;
		}
		public void setRegisterdate(String registerdate) {
			this.registerdate = registerdate==null?null:registerdate.trim();
		}
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area==null?null:area.trim();
		}
//		public Date getStartdate() {
//			return startdate;
//		}
//		public void setStartdate(Date startdate) {
//			this.startdate = startdate;
//		}
//		public Date getEnddate() {
//			return enddate;
//		}
//		public void setEnddate(Date enddate) {
//			this.enddate = enddate;
//		}
//		public Date getJqstartdate() {
//			return jqstartdate;
//		}
//		public void setJqstartdate(Date jqstartdate) {
//			this.jqstartdate = jqstartdate;
//		}
//		public Date getJqenddate() {
//			return jqenddate;
//		}
//		public void setJqenddate(Date jqenddate) {
//			this.jqenddate = jqenddate;
//		}
}
