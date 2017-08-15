package com.zzb.conf.service.impl;

import java.util.*;

import javax.annotation.Resource;

import com.cninsure.core.utils.StringUtil;
import com.zzb.mobile.model.CommonModel;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBRegionDao;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.service.INSBRegionService;

@Service
@Transactional
public class INSBRegionServiceImpl extends BaseServiceImpl<INSBRegion> implements
		INSBRegionService {
	@Resource
	private INSBRegionDao insbRegionDao;

	@Override
	protected BaseDao<INSBRegion> getBaseDao() {
		return insbRegionDao;
	}

	@Override
	public Map initList(String parentcode) {
		List<Map<String, String>> list = insbRegionDao.initList(parentcode);
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		for (Map<String, String> m : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("area_code", m.get("comcode"));
			map.put("area_name", m.get("comcodename"));
			result.add(map);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		return map;
	}  

	@Override
	public Map getAllCity(String parentcode) {
		List<Map<String, String>> list = insbRegionDao.getAllCity(parentcode);
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		for (Map<String, String> m : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("area_code", m.get("comcode"));
			map.put("area_name", m.get("comcodename"));
			result.add(map);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		return map;
	}
	
	@Override
	public Map getAllCountry(String parentcode) {
		List<Map<String, String>> list = insbRegionDao.getAllCountry(parentcode);
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		for (Map<String, String> m : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("area_code", m.get("comcode"));
			map.put("area_name", m.get("comcodename"));
			result.add(map);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		return map;
	}
	
	@Override
	public Map initListfromSD(Map<String, String> para) {
		List<Map<String, String>> list = insbRegionDao.initListfromSD(para);
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		for (Map<String, String> m : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("area_code", m.get("comcode"));
			map.put("area_name", m.get("comcodename"));
			result.add(map);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		return map;
	}

	@Override
	public List<Map<String, String>> getRegisterProvince() {
		List<Map<String, String>> list = insbRegionDao.getRegisterProvince();
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		for (Map<String, String> m : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("area_code", m.get("comcode"));
			map.put("area_name", m.get("comcodename"));
			result.add(map);
		}
		return result;
	}

	@Override
	public int updateRegion(INSBRegion insbRegion, String operator) {
		if (insbRegion == null) return 0;
		INSBRegion region = insbRegionDao.selectById(insbRegion.getId());
		if (region == null) return 0;
		region.setRegister(insbRegion.getRegister());
		region.setShortname(insbRegion.getShortname());
		region.setDeptid(insbRegion.getDeptid());
		region.setOperator(operator);
		region.setModifytime(new Date());
		return insbRegionDao.updateById(region);
	}

	@Override
	public List<Map<String, Object>> initRegion(String parentcode) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (StringUtil.isEmpty(parentcode)) {
			parentcode = "0";
		}

		INSBRegion regionvo = new INSBRegion();
		regionvo.setParentid(parentcode);
		List<Map<String, String>> list = insbRegionDao.initList(parentcode);

		for(Map<String, String> map : list){

			Map<String,Object> tempMap = new HashMap<String,Object>();
			String setDistrict = " " + (String) map.get("comcodename");
			tempMap.put("name", map.get("comcodename"));
			tempMap.put("id", map.get("id"));
			tempMap.put("pid", map.get("parentid"));
			tempMap.put("register", map.get("register"));
			tempMap.put("deptid", map.get("deptid"));
			tempMap.put("shortname", map.get("shortname"));

			INSBRegion r = new INSBRegion();
			r.setParentid(map.get("id"));
			long count = insbRegionDao.selectCount(r);
			tempMap.put("isParent", count > 0 ? "true" : "false");
			String parentid = (String)map.get("parentid");
			int i = 3;
			while (true) {
				if (StringUtil.isEmpty(parentid) || "0".equals(parentid) || i == 0) {
					break;
				}
				i--;
				INSBRegion parentRegion = insbRegionDao.selectById(parentid);
				if (parentRegion != null && StringUtil.isNotEmpty(parentRegion.getComcodename())) {
					setDistrict =  " " + parentRegion.getComcodename() + setDistrict;
				}
				parentid = parentRegion.getParentid();
			}

			tempMap.put("setDistrict", "设置地区：" + setDistrict);

			resultList.add(tempMap);
		}
		return resultList;
	}
}