package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.cninsure.core.utils.DateUtil;
import com.common.CloudQueryUtil;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;

public class INSBInsuredQuoteServiceImpl {
	
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	
	/**
	 * 平台查询，针对本地数据库
	 * @param taskid 实例id
	 * @param insbcarinfo 车牌号
	 * @return
	 */
	private LastYearPolicyInfoBean queryLastInsuredInfoByCarinfo(String taskid,INSBCarinfo insbcarinfo,String areaId){
		JSONObject object = new JSONObject();
		object.put("flag", "XB");
		JSONObject inParas = new JSONObject();
		inParas.put("car.specific.license", insbcarinfo.getCarlicenseno());
		object.put("inParas", inParas);
		JSONObject carinfo = new JSONObject();
		String vehiclename = "";
		//获取车型名称
		INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
		carmodelinfo.setCarinfoid(insbcarinfo.getId());
		INSBCarmodelinfo insbCarmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
		if(null != insbCarmodelinfo){
			vehiclename = insbCarmodelinfo.getStandardfullname();
		}
		carinfo.put("engineno", insbcarinfo.getEngineno());//发动机号
		carinfo.put("vehicleframeno", insbcarinfo.getVincode());//车架号
		carinfo.put("registerdate", DateUtil.toString(insbcarinfo.getRegistdate()));//初登日期
		carinfo.put("vehiclename", vehiclename);//品牌型号
		carinfo.put("chgownerflag", "0");//是否过户车
		object.put("carinfo", carinfo);
		object.put("areaId", areaId);
		object.put("eid", taskid);

		LastYearPolicyInfoBean lastYearPolicyInfoBean = null;
		try {
			String resultJson = CloudQueryUtil.getLastYearInsurePolicy(object.toString());
			JSONObject jsonObject=JSONObject.fromObject(resultJson);
			lastYearPolicyInfoBean = (LastYearPolicyInfoBean) JSONObject.toBean(jsonObject, LastYearPolicyInfoBean.class);
		} catch (Exception e) {
			lastYearPolicyInfoBean = null;
			e.printStackTrace();
		}
		return lastYearPolicyInfoBean;
	}
	
}
