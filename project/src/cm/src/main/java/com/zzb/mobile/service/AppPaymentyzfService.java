package com.zzb.mobile.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import net.sf.json.JSONObject;

import com.cninsure.core.dao.BaseService;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.mobile.model.CommonModel;

public interface AppPaymentyzfService extends BaseService<AppPaymentyzf> {

/**
 * 	翼支付签约
 */
public CommonModel Signyzf(JSONObject params);

/**
 * 获取银行卡类型接口
 */
public CommonModel getCardType();
/**
 * 获取银行接口
 */
public CommonModel getBankType();
/**
 * 获取证件类型接口
 */
public CommonModel getidCardType();
/**
 * 获取省级
 */
public CommonModel ProvinceInfo();

/**
 * 获取市级
 */
public CommonModel CityInfo(String provinceID);	
/**
 * 	查询翼支付签约信息
 */	
public CommonModel querySignInfos(String jobNum);
/**
 * 	翼支付签约成功信息
 */	
public CommonModel querySignInfos1(String jobNum);
/**
 * 	签约信息确认
 */

public CommonModel Infosyzf(JSONObject params);
/**
 * 获取支付金额
 * @param orderid 订单id
 * @return
 */
public CommonModel getPaymentAmount(String taskid,String prvid);

}
