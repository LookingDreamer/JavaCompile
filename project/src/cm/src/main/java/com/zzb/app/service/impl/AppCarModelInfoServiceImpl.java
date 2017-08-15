package com.zzb.app.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.httpclient.ClientOfferApi;
import com.cninsure.core.httpclient.IClientRuleApi;
import com.cninsure.core.utils.JsonUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.app.service.AppCarModelInfoService;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.conf.service.INSBAgreementService;

@Service
@Transactional
public class AppCarModelInfoServiceImpl implements AppCarModelInfoService {
	@Resource
	private INSBAgreementService insbAgreementService;
	@Resource
	private INSBQuoteinfoService insbQuoteinfoService;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;

	@Override
	public String getCarModelInfo(String taskId, String inscomcode) {
		String result = "";
		Map<String, Object> map = new HashMap<String, Object>(1);

		INSBQuotetotalinfo qti = new INSBQuotetotalinfo();
		qti.setTaskid(taskId);
		INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao
				.selectOne(qti);
		String quoteTotalInfoId = null;
		if (quotetotalinfo != null) {
			quoteTotalInfoId = quotetotalinfo.getId();
		}
		if (StringUtil.isNotEmpty(inscomcode)) {
			inscomcode = inscomcode.trim();
			INSBQuoteinfo quoteinfo = insbQuoteinfoService.getQuoteinfo(
					quoteTotalInfoId, inscomcode);
			String agreementtrule = null;
			if (StringUtil.isNotEmpty(quoteinfo.getAgreementid())) {
				agreementtrule = insbAgreementService.queryById(
						quoteinfo.getAgreementid()).getAgreementtrule();
				if (StringUtils.isNotBlank(agreementtrule)) {
					map.put("ruleItem.ruleID", agreementtrule);
					result = getTestResult(map, "carModelRule");
				}
			}
		}
		return result;
	}

	public String getTestResult(Map<String, Object> parammap, String methodName) {
		String jstr = JsonUtil.getJsonStringDate(parammap);
		Map<String, String> map = new HashMap<String, String>();
		map.put("methodName", methodName);
		map.put("paramStr", jstr);
		IClientRuleApi client = new ClientOfferApi();
		String ruleResult = null;
		try {
			ruleResult = client.getRuleResult(map);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(e);
		}
		return ruleResult;
	}
}
