package com.zzb.conf.service.impl;

import com.zzb.conf.service.INSBAutoconfigshowService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBElfconfDao;
import com.zzb.conf.entity.INSBElfconf;
import com.zzb.conf.service.INSBElfconfService;
import com.zzb.model.elfAbilityModel;

@Service
@Transactional
public class INSBElfconfServiceImpl extends BaseServiceImpl<INSBElfconf> implements
		INSBElfconfService {
	@Resource
	private INSBElfconfDao insbElfconfDao;

	@Resource
	private INSBAutoconfigshowService insbAutoconfigshowService;

	@Override
	protected BaseDao<INSBElfconf> getBaseDao() {
		return insbElfconfDao;
	}

	@Override
	public Map<String, Object> initELFConfList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbElfconfDao.selectELFConfListPaging(data);
		map.put("total", insbElfconfDao.selectCount());
		map.put("rows", infoList);
		return map;
	}
	/** 
	 * 根据精灵id获取能力列表
	 * @see com.zzb.conf.service.INSBElfconfService#abilityListByelfid(java.util.Map, java.lang.String)
	 */
	@Override
	public Map<String, Object> abilityListByelfid(Map<String, Object> data,String elfid) {
		data.put("elfid", elfid);
		List<Map<Object, Object>> infoList = insbElfconfDao.abilityListByelfidPaging(data);
//		01：报价配置 :02：核保配置:03：续保配置.04:承保配置
		String[] nl = {"报价","核保","续保","承保","平台"};
		String[] num = {"01","02","03","04","05"};
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < infoList.size(); i++) {
			StringBuilder sb = new StringBuilder();
			elfAbilityModel mo = (elfAbilityModel) infoList.get(i);
			String  abilitys = mo.getAbility();
			for(int j = 0;j < 5;j++){
				if(abilitys != null && abilitys.contains(num[j])){
					sb.append(nl[j]+"<input type=\"checkbox\"  checked=\"checked\" >");
				}else{
					sb.append(nl[j]+"<input type=\"checkbox\">");
				}
			}
			mo.setAbility(sb.toString());
		}
		map.put("total", insbElfconfDao.abilityListByelfidPagingCount(data));
		map.put("rows", infoList);
		return map;
	}

	@Override
	public List<INSBElfconf> queryElfAll() {
		return insbElfconfDao.queryElfAll();
	}

	@Override
	public int deleteById(String id) {
		int deleted = super.deleteById(id);
		if (deleted > 0) {
			insbAutoconfigshowService.deleteElfAutoConfigShowByElfId(id);
		}
		return deleted;
	}
}