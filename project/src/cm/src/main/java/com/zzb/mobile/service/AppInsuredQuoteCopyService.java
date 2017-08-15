package com.zzb.mobile.service;

import com.zzb.extra.model.SearchProviderModelForMinizzb;
import com.zzb.mobile.CheckAddQuoteProviderRoleModel;
import com.zzb.mobile.model.*;

import java.util.List;
import java.util.Map;

public interface AppInsuredQuoteCopyService {

	/**
	 * task-122 按照任务号，提供该单所有数据接口，供前端使用
	 * 将该报价的车辆信息、关系人信息、投保公司、保险配置复制，同时跳转到车险投保首页，将这些信息带入到新的报价流程中。
	 * @param srcTaskId
	 * @param inscomcode
	 * @return
	 */
	CommonModel copy(String srcTaskId, String inscomcode);



}
 