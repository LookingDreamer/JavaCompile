package com.zzb.cm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBPlatcarpriceDao;
import com.zzb.cm.entity.INSBPlatcarprice;
import com.zzb.cm.service.INSBPlatcarpriceService;

@Service
@Transactional
public class INSBPlatcarpriceServiceImpl extends
		BaseServiceImpl<INSBPlatcarprice> implements INSBPlatcarpriceService {
	@Resource
	private INSBPlatcarpriceDao insbPlatcarpriceDao;

	@Override
	protected BaseDao<INSBPlatcarprice> getBaseDao() {
		return insbPlatcarpriceDao;
	}

	/**
	 * 通过taskid和inscomcode查询出平台车价信息List
	 */
	@Override
	public String getPlatCarPriceInfoJSONByMap(Map<String, Object> paramMap) {
		List<Map<String, Object>> platCarPriceInfoList = insbPlatcarpriceDao.getPlatCarPriceInfoByMap(paramMap);
		//组织返回数据
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("records", "10000");
		map.put("page", 1);
		map.put("total", platCarPriceInfoList.size());
		map.put("rows", platCarPriceInfoList);
		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject.toString();
	}
}