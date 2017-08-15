package com.zzb.chetong.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import net.chetong.sdk.client.ChetongHttpClient;
import net.chetong.sdk.exception.ChetongApiException;
import net.chetong.sdk.vo.AgentInfoVo;
import net.chetong.sdk.vo.CommonRequestVo;
import net.chetong.sdk.vo.CommonResponseVo;
import net.chetong.sdk.vo.CustomerInfoVo;
import net.chetong.sdk.vo.ZhangzbInsureVo;
import net.chetong.sdk.vo.ZhangzbResponseVo;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.LogUtil;
import com.zzb.chetong.entity.ChetongCarModel;
import com.zzb.chetong.service.ChetongService;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.mobile.model.CommonModel;

@Service
@Transactional
public class ChetongServiceImpl implements ChetongService {
	@Resource
	INSBPolicyitemDao policyDao;
	@Resource
	INSBCarinfoDao carinfoDao;
	@Resource
	INSBCarmodelinfohisDao carModeDao;
	@Resource
	INSBAgentDao agentDao;
	@Resource
	INSBInsuredhisDao insuredDao;
	@Resource
	INSBPersonDao personDao;

	private String env;
	private String appKey;
	private String privateKey;
	private String serviceName;

	{
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
		env = resourceBundle.getString("chetong.env");
		appKey = resourceBundle.getString("chetong.appkey");
		privateKey = resourceBundle.getString("chetong.privateKey");
		serviceName = resourceBundle.getString("chetong.serviceName");
	}
	
//	String env = "develop";
//    String appkey = "spys3ylubz59xycpa6";
//    String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMJY4fHKmE5IdVyntB9lBkroPJjJmvdewX+35YAQE0WvQY7CvlKhwJaqWQxrazpvmXM0nIgwRRbIG81sp39YbSTIi1NXAx6/U40G/CS1lQo9RHNldxGPAh60+a8NWrj3UxdvX1lWZm2mgryj7Z+4q8UzOoWyxGtXuD1Tm7pvtlUvAgMBAAECgYAwfbzid4pI/gtKcw2eR7aqOgfkl2jzD9F37Ip2yYwFdxUEadLTC0jiTt6pGqRGkFHZTOdBk8mpRYSqXNHuFEIm26Ma/SRm1Jm1wRl1n6Gob+fERVOgbpdNAhqZJmrZZxigMBakuLXUEf/Fw1VBq4U7yDtQP2ldKzi+4bUrF1wzAQJBAPPLYK7fAFn58sEwqpFpKAjWMGPWZ6HK8z77SHDvGSZCbvFa9cpcPpsg/5j1oj04MpZ9eHiFc8ZAn25iECX4qvMCQQDME8IO2elm2DAlfni/HDoaIEtRaPcK5xUFJ44sI3x3m9DvYb78WCLl/6z2ga5QwpzsCFDxM//DONYXgpuQQ8PVAkEA1Xztp3Tk6+XzbLXfSALb6hEWwvbIPWhp5mdKIotebKCUYoqS3qg0ssgMy7eeYRnLQvmQ+4lxvtRsSM9a8MV3fQJBAMlAuuFWeNxZWrG/FWiyd4IIUv6H2wi/dnVM2uIgZMC3wsYvyN6mNFRJXdMDdFpkccg7M0eLFjD5vfgVb0BH87kCQFF6vd59ANYZvYsVaa8ct2Ouu4i3xx6G0JitrpWQwSz8ReDQ35GqTdwmtMx7gxwuJk5HjNsbxagzRMfJZu4eebU=";
//    String serviceName = "net.chetong.ctapi.service.api.order.spOrderService.saveInsure";

	@Override
	public CommonModel jumpingToChetong(String ownername, String carlicenseno) {
		// 根据车牌 + 车主获取已生效的保单
		CommonModel com = new CommonModel(); 
		List<INSBPolicyitem> l = policyDao.getPolicyByOwnernameAndCarlicenseno(
				ownername, carlicenseno);
		if (l.isEmpty()) {
			com.setStatus(CommonModel.STATUS_FAIL);
			com.setMessage("暂无法查询保单信息");
		} else if (l.size() > 1) {
			com.setStatus(CommonModel.STATUS_FAIL);
			com.setMessage("获取多张保单数据");
			com.setBody(l);

		} else {
			com = jumpingToChetong(l.get(0).getId());
		}
		return com;
	}

	@Override
	public CommonModel jumpingToChetong(String policyId) {
		CommonModel com = new CommonModel();
		ChetongHttpClient client = new ChetongHttpClient(env, appKey,
				privateKey);
		ZhangzbInsureVo zhangzbInsureVo = findZhangzbInsureVo(policyId);
		JSONObject  jso = new JSONObject(zhangzbInsureVo);
		LogUtil.info(jso.toString());
		
		// 设置请求参数
		CommonRequestVo commonRequestVo;
		try {
			commonRequestVo = new CommonRequestVo(serviceName);
			commonRequestVo.setParams(zhangzbInsureVo);
			CommonResponseVo response = client.call(commonRequestVo);
			ZhangzbResponseVo zhangzbResponseVo = response
					.getBuildVo(ZhangzbResponseVo.class);
			com.setStatus(CommonModel.STATUS_SUCCESS);
			com.setBody(zhangzbResponseVo);
		} catch (ChetongApiException e) {
			com.setStatus(CommonModel.STATUS_FAIL);
			com.setMessage("车同接口调用失败");
			e.printStackTrace();
		}
		return com;
	}
	
	private String formateDate(Date date){
		String Format_DateTime = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat df = new SimpleDateFormat(Format_DateTime);
		return df.format(date);
	}
	

	private ZhangzbInsureVo findZhangzbInsureVo(String policyId) {
		ZhangzbInsureVo zhangzbInsureVo = new ZhangzbInsureVo();
		INSBPolicyitem policyItem = policyDao.selectById(policyId);
		// 保单信息
		zhangzbInsureVo.setRequestNo(String.valueOf(System.currentTimeMillis()));
		zhangzbInsureVo.setInsureNo(policyItem.getPolicyno());
		zhangzbInsureVo.setUseNum("1");
		zhangzbInsureVo.setPayTime(formateDate(policyItem
				.getModifytime()));
		zhangzbInsureVo.setValidBegin(formateDate(policyItem
				.getStartdate()));
		zhangzbInsureVo.setValidEnd(formateDate(policyItem
				.getEnddate()));
		zhangzbInsureVo.setCreateTime(formateDate(policyItem
				.getCreatetime()));

		INSBCarinfo carinfo = carinfoDao.selectCarinfoByTaskId(policyItem
				.getTaskid());
		INSBCarmodelinfohis carmodelinfohisVo = new INSBCarmodelinfohis();
		carmodelinfohisVo.setCarinfoid(carinfo.getId());
		carmodelinfohisVo.setInscomcode(policyItem.getInscomcode());
		INSBCarmodelinfohis carmodelinfohis = this.carModeDao
				.selectOne(carmodelinfohisVo);
		zhangzbInsureVo.setKeywords(carinfo.getCarlicenseno());
		zhangzbInsureVo.setInsureFee(policyItem.getAmount().toString());
		ChetongCarModel carModel = CreateChetongCar(carinfo, carmodelinfohis);
		JSONObject json = new JSONObject(carModel);
		zhangzbInsureVo.setServiceObj(json.toString());

		// 代理人信息
		INSBAgent agent = agentDao.selectByJobnum(policyItem.getAgentnum());
		AgentInfoVo agentInfoVo = new AgentInfoVo();
		agentInfoVo.setAgentName(agent.getName());
		agentInfoVo.setAgentMobile(agent.getMobile());
		zhangzbInsureVo.setAgentInfo(agentInfoVo);

		// 投保人信息
		INSBInsuredhis insueredVo = new INSBInsuredhis();
		insueredVo.setTaskid(policyItem.getTaskid());
		insueredVo.setInscomcode(policyItem.getInscomcode());
		INSBInsuredhis insured = insuredDao.selectOne(insueredVo);
		INSBPerson person = personDao.selectById(insured.getPersonid());
		CustomerInfoVo customerInfoVo = new CustomerInfoVo();
		customerInfoVo.setCustomerName(person.getName());
		// customerInfoVo.setSex(person.);
		customerInfoVo.setPhoneNum(person.getCellphone());
		customerInfoVo.setPidType(person.getIdcardtype().toString());
		customerInfoVo.setPid(person.getIdcardno());

		zhangzbInsureVo.setCustomerInfo(customerInfoVo);

		return zhangzbInsureVo;
	}

	private ChetongCarModel CreateChetongCar(INSBCarinfo carinfo,
			INSBCarmodelinfohis carmodelinfohis) {
		ChetongCarModel ctcar = new ChetongCarModel();
		ctcar.setBrandname(carmodelinfohis.getBrandname());
		ctcar.setCarlicenseno(carinfo.getCarlicenseno());
		ctcar.setCarVehicleOrigin(carmodelinfohis.getCarVehicleOrigin());
		ctcar.setDisplacement(carmodelinfohis.getDisplacement());
		ctcar.setEngineno(carinfo.getEngineno());
		ctcar.setFactoryname(carmodelinfohis.getFactoryname());
		ctcar.setFamilyname(carmodelinfohis.getFamilyname());
		ctcar.setFullweight(carmodelinfohis.getFullweight());
		ctcar.setGearbox(carmodelinfohis.getGearbox());
		ctcar.setGlassType(carmodelinfohis.getGlassType());
		ctcar.setJgVehicleType(carmodelinfohis.getJgVehicleType());
		ctcar.setListedyear(carmodelinfohis.getListedyear());
		ctcar.setLoads(carmodelinfohis.getLoads());
		ctcar.setRatedPassengerCapacity(carmodelinfohis
				.getRatedPassengerCapacity());
		ctcar.setRegistdate(formateDate(carinfo.getRegistdate()));
		ctcar.setSeat(carmodelinfohis.getSeat());
		ctcar.setStandardfullname(carmodelinfohis.getStandardfullname());
		ctcar.setUnwrtweight(carmodelinfohis.getUnwrtweight());
		ctcar.setVincode(carinfo.getVincode());
		return ctcar;
	}
}