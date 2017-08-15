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
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.service.INSBCarmodelinfoService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.service.INSBCarinfoManagerService;

@Service
@Transactional
public class INSBCarinfoManagerServiceImpl extends BaseServiceImpl<INSBCarinfo> implements INSBCarinfoManagerService {
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBCarmodelinfoService insbCarmodelinfoService;

	@Override
	protected BaseDao<INSBCarinfo> getBaseDao() {
		return insbCarinfoDao;
	}

	@Override
	public Map<String, Object> initCarList(Map<String, Object> map) {
		List<INSBCarinfo> carList = insbCarinfoDao.getCarinfos(map);
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object, Object>>();
		Map<String, Object> initMap = new HashMap<String, Object>();
		long total = insbCarinfoDao.selectPagingCount(map);
		initMap.put("total", total);
		for (int i = 0; i < carList.size(); i++) {
			Map<Object, Object> tempMap = new HashMap<Object, Object>();
			INSBCarinfo tempCar = new INSBCarinfo();
			tempCar = carList.get(i);
			tempMap.put("id", tempCar.getId());
			tempMap.put("rownum", i + 1);
			tempMap.put("carlicenseno", tempCar.getCarlicenseno());
			tempMap.put("vincode", tempCar.getVincode());
			tempMap.put("ownername", tempCar.getOwnername());
			tempMap.put("idcardno", tempCar.getEngineno());
			tempMap.put("phonenumber", tempCar.getPhonenumber());
			tempMap.put("operating1", "<a href=\"javascript:updateuser(\'" + tempCar.getId() + "\');\" >编辑</a>");
			tempMap.put("operating2", "<a href=\"javascript:deleteuser(\'" + tempCar.getId() + "\');\" >删除</a>");
			resultList.add(tempMap);
		}
		initMap.put("rows", resultList);
		return initMap;
	}

	@Override
	public String showCarinfoList(Map<String, Object> map) {

		List<INSBCarinfo> infoList = insbCarinfoDao.selectCarinfoList(map);
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> initMap = new HashMap<Object, Object>();
		initMap.put("records", "10000");
		initMap.put("page", 1);
		initMap.put("total", insbCarinfoDao.selectCount());

		for (int i = 0; i < infoList.size(); i++) {
			INSBCarinfo tempInfo = infoList.get(i);
			Map<Object, Object> tempMap = new HashMap<Object, Object>();
			tempMap.put("id", tempInfo.getId());
			tempMap.put("carlicenseno", tempInfo.getCarlicenseno());
			tempMap.put("vincode", tempInfo.getVincode());
			tempMap.put("ownername", tempInfo.getOwnername());
			tempMap.put("idcardno", tempInfo.getCarid());
			tempMap.put("phonenumber", tempInfo.getPhonenumber());
			tempMap.put("operating1", "<a href=\"javascript:updatecarinfo(\'" + tempInfo.getId() + "\');\" >更新</a>");
			tempMap.put("operating2", "<a href=\"javascript:deletecarinfo(\'" + tempInfo.getId() + "\');\" >删除</a>");
			resultList.add(tempMap);
		}
		initMap.put("rows", resultList);
		JSONObject jsonObject = JSONObject.fromObject(initMap);
		return jsonObject.toString();
	}

	// 车辆明细
	@Override
	public String initCarInfoList(INSBCarinfo car) {
		List<INSBCarinfo> carList = insbCarinfoDao.selectList(car);
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> initMap = new HashMap<Object, Object>();
		initMap.put("records", "10000");
		initMap.put("page", 1);
		initMap.put("total", insbCarinfoDao.selectCount());
		for (int i = 0; i < carList.size(); i++) {
			Map<Object, Object> tempMap = new HashMap<Object, Object>();
			INSBCarinfo tempCar = new INSBCarinfo();
			tempCar = carList.get(i);
			tempMap.put("id", tempCar.getId());
			tempMap.put("carlicenseno", tempCar.getCarlicenseno());
			tempMap.put("vincode", tempCar.getVincode());
			tempMap.put("ownername", tempCar.getOwnername());
			tempMap.put("idcardno", tempCar.getOwner());
			tempMap.put("phonenumber", tempCar.getPhonenumber());
			tempMap.put("operating1", "<a href=\"javascript:updateuser(\'" + tempCar.getId() + "\');\" >编辑</a>");
			tempMap.put("operating2", "<a href=\"javascript:deleteuser(\'" + tempCar.getId() + "\');\" >删除</a>");
			resultList.add(tempMap);
		}
		initMap.put("rows", resultList);
		JSONObject jsonObject = JSONObject.fromObject(initMap);
		return jsonObject.toString();
	}

	// 查询车型信息列表
	@Override
	public List<Map<String, String>> querycarmodellist(int offset, int limit) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<INSBCarmodelinfo> carModelList = insbCarmodelinfoService.queryAll(offset, limit);

		for (int i = 0; i < carModelList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("carmodelid", carModelList.get(i).getId());
			map.put("standardfullname", carModelList.get(i).getStandardfullname());

			list.add(map);
		}

		return list;
	}

	// 通过id查询车辆信息
	public List<Map<String, String>> querycarinfobyid(String id) {

		INSBCarinfo carinfoTemp = new INSBCarinfo();
		carinfoTemp.setId(id);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(carinfoTemp);
		return null;
	}
}
