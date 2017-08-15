package com.zzb.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import net.sf.json.JSONObject;

import com.cninsure.core.dao.BaseService;
import com.zzb.mobile.entity.AppEpayInfo;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.mobile.model.CommonModel;

public interface AppEpayInfoService extends BaseService<AppEpayInfo> {

/**
 * 查询信息
 */
	public CommonModel queryEpayInfos(String jobNum);
	
	/**
	 * 填写信息,并写到缓存里
	 */
	public CommonModel EpayInfos(JSONObject params);
	/**
	 * 获取缓存数据后删除缓存，填写并提交信息
	 */
	
	public CommonModel submitEpayInfos(JSONObject params);
	/**
	 * 获取省级列表
	 */

	public CommonModel getProvinceInfo();
	/**
	 * 获取市级列表
	 */

	public CommonModel getCityInfo(String provinceID);
	/**
	 * 获取代理人姓名
	 */

	public CommonModel writebackName(String jobNum);
}
