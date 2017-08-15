package com.zzb.app.controller;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.common.HttpClientUtil;

//public class AppInsurPolicyanceControllerTest2 {
//
//	private static final String url = "http://localhost:8080/cm/cpmap/access";
//	private static final String cloud = "http://localhost:8080/cm/cloudquery/";
//	
//	@Test
//	public void testQueryProvider() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=PROVIDER;CmdId=search";
//		String MSM_PARAM = "zone=;channel=;jobId=8899;comCode=";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testQueryTaskId() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=Create";
//		String MSM_PARAM = "";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testQueryAgentPowerById() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=ATM;CmdId=GetFunctionAPI";
//		String MSM_PARAM = "functional_id=1f3ea330caded0a8ee4045abda620f5d";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testBindingTaskIdAndAgentId() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=Binding;CmdVer=100;Token=test";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;accountId=1f3ea330caded0a8ee4045abda620f5d";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testBindingTaskIdAndProviderIds() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=SupplierIdList";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;content=<supplierIdList><row>100613</row><row>100812</row></supplierIdList>";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testSaveInsuranceArea() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=InsureAddress;CmdVer=100;Token=test";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;content=<insureAddress><provinceName>广东</provinceName><provinceCode>440000</provinceCode><cityName>广州市</cityName><cityCode>440100</cityCode></insureAddress>";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testSaveInsuranceDate() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=InsureDate";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;content=<insureDate><trafficInsEffectDate>2015-09-17 00:00:00</trafficInsEffectDate><trafficInsInvalidDate>2016-09-16 23:59:59</trafficInsInvalidDate><businessInsEffectDate>2015-09-17 00:00:00</businessInsEffectDate><businessInsInvalidDate>2016-09-16 23:59:59</businessInsInvalidDate></insureDate>";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testSaveInsurancePeopleInfo() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=insuredInfo";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;content=<insuredInfo><personInfo><name>被保险人</name><certificateType>1</certificateType><certNumber>530500198308172309</certNumber><tel>15226008691</tel><email>mytest@qq.com</email></personInfo></insuredInfo>";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testSaveInsuranceAppPeopleInfo() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=insuredInfo";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;content=<applicant><personInfo><name>投保人</name><certificateType>99</certificateType><certNumber>123456789</certNumber><tel>15226008691</tel><email>mytest@qq.com</email></personInfo></applicant>";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//	
//	@Test
//	public void testSaveInsuranceContactsInfo() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=ContactsInfo";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;content=<contactsInfo><personInfo><name>投保人</name><certNumber>123456789</certNumber><certificateType>99</certificateType><personName>投保人</personName><mobile>15226008691</mobile><email>mytest@qq.com</email></personInfo></contactsInfo>";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testSaveDriverOwerInfo() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=ownerInfo";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;content=<ownerInfo><personInfo><name>车主姓名</name><certificateType>1</certificateType><certNumber>512081198308154062</certNumber><tel>15226008691</tel><email>mytest@qq.com</email></personInfo></ownerInfo>";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	
//	@Test
//	public void testSaveOtherCarInfo() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=RecentInsure";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;content=<recentInsure><lastComId>422</lastComId><avkt>小于3万公里</avkt><drivingRegion>1</drivingRegion></recentInsure>";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testSaveBasicCarInfo() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=vehicleInfo";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;content=<vehicleInfo><modelId>1973271</modelId><owner>车主姓名</owner><modelCode>雅阁HG7203BB轿车</modelCode><totalVehicleWeight>1510</totalVehicleWeight><plateNumber>粤A88999</plateNumber><firstRegisterDate>2014-09-16</firstRegisterDate><vin>12345678909876543</vin>"
//					+"<engineNo>67896789</engineNo><newVehicleFlag>false</newVehicleFlag><chgOwnerFlag >true</chgOwnerFlag><chgOwnerDate>2015-01-16</chgOwnerDate><institutionType>1</institutionType><useProperty>1</useProperty><licenseModelCode>雅阁HG7203BB轿车</licenseModelCode><modelDesc>12131313113</modelDesc><ratedPassengerCapacity>5"
//					+"</ratedPassengerCapacity><displacement>1.997</displacement><wholeWeight>1510</wholeWeight><tonnage>10</tonnage><rulePriceProvideType>2</rulePriceProvideType><userReplacementValue>120000</userReplacementValue></vehicleInfo>";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testUpdateDriverInfo() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=ownerInfo;CmdVer=100;Token=test";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;content=<drivers><row><name>被保险人</name><gender>2</gender><birthday>1983-08-17</birthday><driverTypeCode>A1</driverTypeCode><licenseNo>530500198308172309</licenseNo><licensedDate>2014-09-16</licensedDate></row></drivers>";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testUpdateInsurConfigure() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=InsureConfig";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019;content=<insureConfig><ensureItemList><row><unit></unit><code>VehicleDemageIns</code><name>车辆损失险</name><coverage>1</coverage><selectedOption>1</selectedOption></row><row><unit></unit><code>NcfVehicleDemageIns</code><name>车辆损失险不计免赔</name><coverage>1</coverage><selectedOption>1</selectedOption></row><row><unit></unit><code>ThirdPartyIns</code><name>第三者责任险</name><coverage>200000</coverage>"
//					+"<selectedOption>200000</selectedOption></row><row><unit></unit><code>NcfThirdPartyIns</code><name>第三者责任险不计免赔</name><coverage>1</coverage><selectedOption>1</selectedOption></row><row><unit></unit><code>TheftIns</code><name>全车盗抢险</name><coverage>2000</coverage><selectedOption>2</selectedOption></row><row><unit></unit><code>NcfTheftIns</code><name>全车盗抢险不计免赔</name><coverage>1</coverage><selectedOption>1</selectedOption></row><row><unit></unit><code>DriverIns</code>"
//					+"<name>司机责任险</name><coverage>10000</coverage><selectedOption>10000</selectedOption></row><row><unit></unit><code>NcfDriverIns</code><name>司机责任险不计免赔</name><coverage>1</coverage><selectedOption>1</selectedOption></row><row><unit>/座</unit><code>PassengerIns</code><name>乘客责任险</name><coverage>10000</coverage><selectedOption>10000</selectedOption></row><row><unit></unit><code>NcfPassengerIns</code><name>乘客责任险不计免赔</name><coverage>1</coverage><selectedOption>1"
//					+"</selectedOption></row><row><unit></unit><code>CombustionIns</code><name>自燃损失险</name><coverage>1</coverage><selectedOption>1</selectedOption></row><row><unit></unit><code>NcfCombustionIns</code><name>自燃损失险不计免赔</name><coverage>1</coverage><selectedOption>1</selectedOption></row><row><unit></unit><code>GlassIns</code><name>玻璃单独破碎险</name><coverage>2</coverage><selectedOption>2</selectedOption></row><row><unit></unit><code>VehicleCompulsoryIns</code><name>交强险</name>"
//					+"<coverage>1</coverage><selectedOption>1</selectedOption></row><row><unit></unit><code>VehicleTax</code><name>车船税</name><coverage>1</coverage><selectedOption>1</selectedOption></row></ensureItemList><remarks><remark><code>insureConfigRemark001</code><value>啊啊啊啊啊</value></remark></remarks></insureConfig>";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testUpdateQuoteStatus() {
//		String MSM_HEADER = "CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=Save";
//		String MSM_PARAM = "multiQuoteId=cfd09df69ae540a48a514efe1f410019";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("MSM_HEADER", MSM_HEADER);
//		map.put("MSM_PARAM", MSM_PARAM);
//		String result = HttpClientUtil.doPost(url, map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//
//	@Test
//	public void testQueryByNumber() {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("type", "1");
//		map.put("insureno", "10604003900046192452");
//		String result = HttpClientUtil.doPost(cloud+"querybynumber", map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//	
//	@Test
//	public void testQueryByCarInfo() {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("framenumber", "LGBK22E729Y087069");
//		map.put("enginenumber", "502482C");
//		String result = HttpClientUtil.doPost(cloud+"querybycar", map);
//		System.out.println("result = " + result);
//		assertNotNull(result);
//	}
//}
