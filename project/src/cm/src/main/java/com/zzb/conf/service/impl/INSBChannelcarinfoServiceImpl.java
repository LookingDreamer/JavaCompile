package com.zzb.conf.service.impl;

import java.io.IOException;
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
import com.zzb.conf.dao.INSBChannelcarinfoDao;
import com.zzb.conf.entity.INSBChannelcarinfo;
import com.zzb.conf.service.INSBChannelcarinfoService;

@Service
@Transactional
public class INSBChannelcarinfoServiceImpl extends BaseServiceImpl<INSBChannelcarinfo> implements
		INSBChannelcarinfoService {
	@Resource
	private INSBChannelcarinfoDao insbChannelcarinfoDao; 

	public String initChannelCarInfo(Map<String, Object> map){
		List<INSBChannelcarinfo> channelcarList = insbChannelcarinfoDao.getChannelcarinfo(map);
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object,Object>>();
		Map<Object, Object> initMap = new HashMap<>();
		initMap.put("records", "10000");
		initMap.put("page", 1);
		initMap.put("total", insbChannelcarinfoDao.selectCount());
		for (INSBChannelcarinfo channelcarinfo : channelcarList) {
			Map<Object, Object> tempMap = new HashMap<>();
			tempMap.put("id", channelcarinfo.getId());
			tempMap.put("drivingarea", channelcarinfo.getDrivingarea());
			tempMap.put("carlicenseno", channelcarinfo.getCarlicenseno());
			tempMap.put("customername", channelcarinfo.getCustomername());
			tempMap.put("address", channelcarinfo.getAddress());
			tempMap.put("phonenumber", channelcarinfo.getPhonenumber());
			tempMap.put("vehiclebrand", channelcarinfo.getVehiclebrand());
			tempMap.put("vehicletype", channelcarinfo.getVehicletype());
			tempMap.put("registdate", channelcarinfo.getRegistdate());
			tempMap.put("engineno", channelcarinfo.getEngineno());
			tempMap.put("chassiso", channelcarinfo.getChassiso());
			tempMap.put("batchcode", channelcarinfo.getBatchcode());
			tempMap.put("channelcode", channelcarinfo.getChannelcode());
			resultList.add(tempMap);
		}
		initMap.put("rows", resultList);
		JSONObject jsonObject = JSONObject.fromObject(initMap);
		return jsonObject.toString();
	}
	
	@Override
	protected BaseDao<INSBChannelcarinfo> getBaseDao() {
		return insbChannelcarinfoDao;
	}

	@Override
	public String exportChannelCarInfo(List<String> arrayid) throws IOException {
		StringBuffer fileBuffer=new StringBuffer();
		for (String string : arrayid) {
			INSBChannelcarinfo insbChannelcarinfo=new INSBChannelcarinfo();
			String batchcode = string;
			insbChannelcarinfo.setBatchcode(batchcode);
			List<INSBChannelcarinfo> list=insbChannelcarinfoDao.selectList(insbChannelcarinfo);
			for (INSBChannelcarinfo insbChannelcarinfo2 : list) {
				fileBuffer.append(insbChannelcarinfo2.toString());
				//System.out.println(fileBuffer);
			}
		}
		return fileBuffer.toString();
	}
}