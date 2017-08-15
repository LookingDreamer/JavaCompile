package com.zzb.mobile.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.UpdateInsureDateModel;

import javax.servlet.http.HttpServletRequest;

/*
 * 获得多方报价信息的列表，输入的参数：任务id,一次查询的数量及起始位置，命名不规范，INSBQuoteinfo ,不是驼峰命名。
 * 完成
 */
public interface INSBMultiQuoteInfoService extends BaseService<INSBQuoteinfo>{
	public String getMultiQuoteInfo(HttpServletRequest request, String processInstanceId, String inscomcode,String channelID,String channelUserId);
	public CommonModel updateInsureDate(UpdateInsureDateModel model);
}
