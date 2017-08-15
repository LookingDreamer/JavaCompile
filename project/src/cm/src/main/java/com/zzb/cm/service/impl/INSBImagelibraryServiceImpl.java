package com.zzb.cm.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.dao.INSBImagelibraryDao;
import com.zzb.cm.entity.INSBImagelibrary;
import com.zzb.cm.service.INSBImagelibraryService;

@Service
@Transactional
public class INSBImagelibraryServiceImpl extends BaseServiceImpl<INSBImagelibrary> implements
		INSBImagelibraryService {
	@Resource
	private INSBImagelibraryDao insbImagelibraryDao;

	@Override
	protected BaseDao<INSBImagelibrary> getBaseDao() {
		return insbImagelibraryDao;
	}

	@Override
	public Map<String,Object> queryImageByAgent(String agentCode,String carlicenseno,String insuredname,int offset,int limit) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		Map<String,Object> conditionMap = new HashMap<String,Object>();
		int index = 1;
		conditionMap.put("agentcode", agentCode);
		if(!StringUtil.isEmpty(carlicenseno)){
			conditionMap.put("carlicenseno", carlicenseno);
		}
		if(!StringUtil.isEmpty(insuredname)){
			conditionMap.put("insuredname", insuredname);
		}
		if(!StringUtil.isEmpty(offset)){
			if(!StringUtil.isEmpty(limit)){
				conditionMap.put("limit", limit);
				index = (offset/limit)+1;
			}else{
				conditionMap.put("limit", 9);
			}
			conditionMap.put("offset", offset);
			
		}else{
			conditionMap.put("offset", 0);
			conditionMap.put("limit", 9);
		}
		returnMap.put("datas", insbImagelibraryDao.selectImageByAgent(conditionMap));
		returnMap.put("count", insbImagelibraryDao.selectCount(conditionMap));
		returnMap.put("index", index);
		return returnMap;
	}

	@Override
	public Map<String, Object> queryDeatilInfo(String imageLibraryId, String agentCode,String imageTypeName,int offset,int limit) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		Map<String,Object> conditionMap = new HashMap<String,Object>();
		int index = 1;
		String code = "insuranceimage";
		conditionMap.put("agentcode", agentCode);
		conditionMap.put("imageLibraryId", imageLibraryId);
		conditionMap.put("code", code);
		if(!StringUtil.isEmpty(imageTypeName)){
			conditionMap.put("imageTypeName", imageTypeName);
		}
		if(!StringUtil.isEmpty(offset)){
			if(!StringUtil.isEmpty(limit)){
				conditionMap.put("limit", limit);
				index = (offset/limit)+1;
			}else{
				conditionMap.put("limit", 9);
			}
			conditionMap.put("offset", offset);
			
		}else{
			conditionMap.put("offset", 0);
			conditionMap.put("limit", 9);
		}
		returnMap.put("datas", insbImagelibraryDao.selectDeatilInfo(conditionMap));
		returnMap.put("count", insbImagelibraryDao.selectDeatilCount(conditionMap));
		returnMap.put("index", index);
		return returnMap;
	}

}