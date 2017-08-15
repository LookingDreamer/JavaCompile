package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBBatchDao;
import com.zzb.conf.entity.INSBBatch;
import com.zzb.conf.entity.INSBChannelcarinfo;
import com.zzb.conf.service.INSBBatchService;

@Service
@Transactional
public class INSBBatchServiceImpl extends BaseServiceImpl<INSBBatch> implements
		INSBBatchService {
	@Resource
	private INSBBatchDao insbBatchDao;

	@Override
	protected BaseDao<INSBBatch> getBaseDao() {
		return insbBatchDao;
	}

	@Override
	public String initInsbBatch(INSBBatch insbBatch) {
		List<INSBBatch> insbBatchList = insbBatchDao.selectList(insbBatch);
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object,Object>>();
		Map<Object, Object> initMap = new HashMap<>();
		initMap.put("records", "10000");
		initMap.put("page", 1);
		initMap.put("total", insbBatchDao.selectCount());
		for (INSBBatch insbBatch1 : insbBatchList) {
			Map<Object, Object> tempMap = new HashMap<>();
			tempMap.put("batchcode", insbBatch1.getBatchacode());
			tempMap.put("id", insbBatch1.getId());
			tempMap.put("operator", insbBatch1.getOperator());
			tempMap.put("channelcode", insbBatch1.getChannelcode());
			tempMap.put("createtime", insbBatch1.getCreatetime().toLocaleString());
			tempMap.put("modifytime", insbBatch1.getModifytime().toLocaleString());
			tempMap.put("notice", insbBatch1.getNoti());
			resultList.add(tempMap);
		}
		initMap.put("rows", resultList);
		JSONObject jsonObject = JSONObject.fromObject(initMap);
		return jsonObject.toString();
	}

}