package com.zzb.ads.service.impl;

import javax.annotation.Resource;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.ads.util.AdsUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.ads.dao.INSBAdsDao;
import com.zzb.ads.entity.INSBAds;
import com.zzb.ads.service.INSBAdsService;

import java.util.*;

@Service
@Transactional
public class INSBAdsServiceImpl extends BaseServiceImpl<INSBAds> implements
		INSBAdsService {
	@Resource
	private INSBAdsDao insbAdsDao;

	@Override
	protected BaseDao<INSBAds> getBaseDao() {
		return insbAdsDao;
	}

	@Override
	public int saveAds(String taskid, List<String> agreementids) {
		if (StringUtil.isEmpty(taskid)) return 0;
		if (agreementids == null || agreementids.isEmpty()) return 0;
		StringBuffer agreements = new StringBuffer();
		for (String agreementid : agreementids) {
			if (StringUtil.isEmpty(agreementid)) continue;
			agreements.append(agreementid + ",");
		}
		List<INSBAds> adses= new ArrayList<INSBAds>();
		JSONObject adsResult =  AdsUtil.getAgreementAds (agreements.toString());
		if (adsResult == null) return 0;
		for (String agreementid : agreementids) {
			if (StringUtil.isEmpty(agreementid)) continue;
			System.out.println(JSONObject.fromObject(adsResult).toString());
			JSONArray adsResultJSONArray = adsResult.getJSONArray(agreementid);
			if (adsResultJSONArray == null || adsResultJSONArray.isEmpty()) continue;
			Iterator<Object> it = adsResultJSONArray.iterator();
			while (it.hasNext()) {
				JSONObject ob = (JSONObject) it.next();
				System.out.println("111" + JSONObject.fromObject(ob).toString());
				if (ob != null) {
					//"createTime":"2017-07-01 10:16:07","description":"佛山人保由即日起，商业险和交强险按净保费结算，
					// 净保费实收佣金结算方法如下：<br /><br />方法一：（单面保费&divide;1.06）*净保费佣金率<br />例：
					// 单面保费（掌中保前端显示项为商业险保费）为3196.82元，佣金率假设为20%，则实收佣金为<br />3196.
					// 82&divide;1.06*20%&asymp;603.17<br /><br />方法二：实收佣金=发票金额*净保费佣金率<br />
					// 例：净保费（发票显示金额一栏）为3015.87元，佣金率假设为20%，则实收佣金为<br />
					// 3015.87*20%&asymp;603.17<br /><br />方法三：实收佣金率=净保费佣金率&divide;1.06<br /><br />
					// 小提示：上述方式仅提供参考，佣金率可能与实际有小许差异，最终以财务结算为准。
					// <br />技能马上get&radic;","endTime":"2099-01-01","id":"2c9080845cf3a0b4015cfbef0f3520e2",
					// "name":"净保费结算","rowNum":0,"startTime":"2017-07-01","status":"1","title":"净保费结算"}
					INSBAds ads = new INSBAds();
					ads.setOperator("admin");
					ads.setCreatetime(new Date());
					ads.setTaskid(taskid);
					ads.setAgreementid(agreementid);
					ads.setName(ob.getString("name"));
					ads.setTitle(ob.getString("title"));
					ads.setDescription(ob.getString("description"));
					ads.setAdscreateTime(DateUtil.parseDateTime(ob.getString("createTime")));
					ads.setStartTime(DateUtil.parse(ob.getString("startTime")));
					ads.setEndTime(DateUtil.parse(ob.getString("endTime")));
					ads.setStatus(ob.getString("status"));
					adses.add(ads);
				}
			}
		}
		insbAdsDao.insertInBatch(adses);
		return 1;
	}

	@Override
	public Map<String, List<Map>> getAds(String taskid) {
		Map<String, List<Map>> map = new HashMap<String, List<Map>>();

		List<Map<String, String>> insbAdsList = insbAdsDao.getAds(taskid);
		if (insbAdsList == null || insbAdsList.isEmpty()) return map;

		for (Map<String, String> ads : insbAdsList) {
			if (ads == null || StringUtil.isEmpty(ads.get("agreementid"))) continue;
			String agreementid = ads.get("agreementid");
			if (map.containsKey(agreementid)) {
				List adsList = map.get(agreementid);
				adsList.add(ads);
			} else {
				List adsList = new ArrayList<>();
				adsList.add(ads);
				map.put(ads.get("agreementid"),adsList);
			}
		}

		return map;
	}

	@Override
	public int deleteAds(String taskid, String agreementid) {
		if (StringUtil.isEmpty(taskid) || StringUtil.isEmpty(agreementid)) return 0;
		return insbAdsDao.deleteAds(taskid, agreementid);
	}
}