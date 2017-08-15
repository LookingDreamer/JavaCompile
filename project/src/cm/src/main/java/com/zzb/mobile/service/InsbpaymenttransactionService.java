package com.zzb.mobile.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import net.sf.json.JSONObject;

import com.cninsure.core.dao.BaseService;
import com.zzb.mobile.entity.AppEpayInfo;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.mobile.entity.Insbpaymenttransaction;
import com.zzb.mobile.model.CommonModel;

public interface InsbpaymenttransactionService extends BaseService<Insbpaymenttransaction> {

	
	public String getPaymenttransaction(Date date);
	
}
