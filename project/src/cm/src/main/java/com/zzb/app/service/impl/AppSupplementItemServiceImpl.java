package com.zzb.app.service.impl;

import com.common.redis.CMRedisClient;
import com.common.redis.IRedisClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.exception.ServiceException;
import com.zzb.app.model.AppSupplementItemModel;
import com.zzb.app.service.AppSupplementItemService;
import com.zzb.conf.dao.INSBAgreementDao;

@Service
@Transactional
public class AppSupplementItemServiceImpl implements AppSupplementItemService {
	public static final String SUPPLEMENT_MODULE = "cm:zzb:supplement:rule_id";
	@Resource
	private INSBAgreementDao insbAgreementDao;

	@Resource
	private IRedisClient redisClient;

	public static void main(String[] args) {
		String str = (String) CMRedisClient.getInstance()
				.get(SUPPLEMENT_MODULE, 144+"");// 158
		System.out.println("zzbSupplementItem:");
		System.out.println(str);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zzb.app.service.impl.AppSupplementItemService#getSupplierSupplementItem
	 * (java.lang.String)
	 */
	@Override
	public String getSupplierSupplementItem(String supplierId, String deptid)
			throws ServiceException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> agreementdruleList = insbAgreementDao
				.selectAgreementtruleListByProviderid(supplierId, deptid);
		System.out.println("zzbSupplementItem:");
		String supplementItem = null;
		JSONArray itemArr = null;
		for (String ruleId : agreementdruleList) {
			if (StringUtils.isNotBlank(ruleId)) {
				supplementItem = (String) redisClient.get(SUPPLEMENT_MODULE, ruleId);
				System.out.println(supplementItem);
				if (StringUtils.isNotBlank(supplementItem)) {
					itemArr = JSONArray.fromObject(supplementItem);
					if (itemArr != null && !itemArr.isEmpty()) {
						List<AppSupplementItemModel> itemList = getRuleResult(itemArr);
						if (itemList != null && !itemList.isEmpty()
								&& itemList.size() > 0) {
							sortList(itemList);
							resultMap.put("success", true);
							resultMap.put("resultVal", itemList);
						}

					}

				}
			}

		}

		if (resultMap.isEmpty() || resultMap.size() < 1) {
			resultMap.put("success", false);
			resultMap.put("resultVal", "生成供应商补充信息失败");
		}

		return JSONObject.fromObject(resultMap).toString();
	}

	private void sortList(List<AppSupplementItemModel> itemList) {
		Collections.sort(itemList, new Comparator<AppSupplementItemModel>() {
			public int compare(AppSupplementItemModel arg0,
					AppSupplementItemModel arg1) {
				return arg0.getCnName().compareTo(arg1.getCnName());
			}
		});
	}

	public List<AppSupplementItemModel> getRuleResult(JSONArray itemArr) {
		List<AppSupplementItemModel> result = null;
		if (itemArr != null) {
			result = new ArrayList<AppSupplementItemModel>();
			AppSupplementItemModel model = null;
			for (int i = 0; i < itemArr.size(); i++) {
				JSONObject jentity = itemArr.getJSONObject(i);
				if (jentity != null) {
					model = new AppSupplementItemModel();
					model.setValScope(jentity.getString("valScope"));
					model.setDefaultVal(jentity.getString("defaultVal"));
					String cnName = jentity.getString("cnName");
					if (StringUtils.isNotBlank(cnName)) {
						cnName = cnName.substring(cnName.lastIndexOf(".") + 1);
					} else {
						cnName = "";
					}
					model.setCnName(cnName);

					String enName = jentity.getString("enName");
					if (StringUtils.isNotBlank(enName)) {
						enName = enName.substring(enName.lastIndexOf(".") + 1);
					} else {
						enName = "";
					}
					model.setEnName(enName);
					model.setType(jentity.getString("type"));
					result.add(model);
				}
			}
		}

		return result;
	}

}
