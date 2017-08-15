package com.zzb.cm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBSupplementDao;
import com.zzb.cm.entity.INSBSupplement;
import com.zzb.cm.service.INSBSupplementService;
import com.zzb.conf.component.SupplementCache;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBSupplementServiceImpl extends BaseServiceImpl<INSBSupplement> implements
		INSBSupplementService {
	@Resource
	private INSBSupplementDao insbSupplementDao;
	@Resource
	private SupplementCache supplementCache;

	@Override
	protected BaseDao<INSBSupplement> getBaseDao() {
		return insbSupplementDao;
	}
	public List<INSBSupplement> getSupplementsBytaskid(String taskid,String inscom) {
		//获取补充获取规则报价补充项
			INSBSupplement insbsupplement = new INSBSupplement();
			insbsupplement.setTaskid(taskid);
			insbsupplement.setProviderid(inscom);
			List<INSBSupplement> allsupplements = insbSupplementDao.selectList(insbsupplement);
			List<INSBSupplement> supplements = new ArrayList<INSBSupplement>();
			StringBuffer keyset = new StringBuffer("");
			for(INSBSupplement supplement:allsupplements){
				if(keyset.toString().contains(supplement.getKeyid())){
					//已包含在补充项内，不用处理
				}else{
					List<Map<String, String>> matedataMaplist = new ArrayList<Map<String, String>>();
					if(null!=supplement.getMetadataValue()&&supplement.getMetadataValue().contains(":")){
						HashMap<String,String> matedataMap;
						JSONObject json=JSONObject.fromObject(supplement.getMetadataValue());
						Iterator iterator = json.entrySet().iterator();
				        while(iterator.hasNext()) {
				           	Map.Entry entry = (Entry) iterator.next();
				           	matedataMap = new HashMap<>();
				           	matedataMap.put("key", entry.getKey().toString());
				           	matedataMap.put("value", entry.getValue().toString());
				           	matedataMaplist.add(matedataMap);
				        }
				        supplement.setMetadataValueMapList(matedataMaplist); 
					}
					supplements.add(supplement);
					keyset.append(supplement.getKeyid());
				}
			}
			return supplements;
		}
	public List<INSBSupplement> getSupplementsBytaskid(String taskid,List<String> incomes) { 
	//获取补充获取规则报价补充项
		INSBSupplement insbsupplement = new INSBSupplement();
		insbsupplement.setTaskid(taskid);
		if(null!=incomes&&incomes.size()>0)
			insbsupplement.setProviderids(incomes);
		List<INSBSupplement> allsupplements = insbSupplementDao.selectList(insbsupplement);
		if(null==allsupplements||allsupplements.size()<=0){//平台查询没有保存规则项信息，初始化规则信息
			supplementCache.saveTaskidSupplementDatas(taskid);
			allsupplements = insbSupplementDao.selectList(insbsupplement);//重新查找所有补充项值
		}
		List<INSBSupplement> supplements = new ArrayList<INSBSupplement>();
		StringBuffer keyset = new StringBuffer("");
		for(INSBSupplement supplement:allsupplements){
			if(keyset.toString().contains(supplement.getKeyid())){
				//已包含在补充项内，不用处理
			}else{
				List<Map<String, String>> matedataMaplist = new ArrayList<Map<String, String>>();
				if(null!=supplement.getMetadataValue()&&supplement.getMetadataValue().contains(":")){
					HashMap<String,String> matedataMap;
					JSONObject json=JSONObject.fromObject(supplement.getMetadataValue());
					Iterator iterator = json.entrySet().iterator();
			        while(iterator.hasNext()) {
			           	Map.Entry entry = (Entry) iterator.next();
			           	matedataMap = new HashMap<>();
			           	matedataMap.put("key", entry.getKey().toString());
			           	matedataMap.put("value", entry.getValue().toString());
			           	matedataMaplist.add(matedataMap);
			        }
			        supplement.setMetadataValueMapList(matedataMaplist); 
				}
				supplements.add(supplement);
				keyset.append(supplement.getKeyid());
			}
		}
		return supplements;
	}
	@Override
	public void updateBykeyidandproviderValue(INSBSupplement model) {
		insbSupplementDao.updateBykeyidandproviderValue(model);
	}
}