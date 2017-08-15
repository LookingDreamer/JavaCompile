package com.zzb.extra.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBAgentDeliveryaddressDao;
import com.zzb.extra.entity.INSBAgentDeliveryaddress;
import com.zzb.extra.service.INSBAgentDeliveryaddressService;
import com.zzb.extra.util.AgentDeliveryaddressUtils;

@Service
@Transactional
public class INSBAgentDeliveryaddressServiceImpl extends BaseServiceImpl<INSBAgentDeliveryaddress> implements
		INSBAgentDeliveryaddressService {
	@Resource
	private INSBAgentDeliveryaddressDao insbAgentDeliveryaddressDao;

	@Override
	protected BaseDao<INSBAgentDeliveryaddress> getBaseDao() {
		return insbAgentDeliveryaddressDao;
	}


	@Override
	public Map<String, Object> initAgentDeliveryaddressList(Map<String, Object> data) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbAgentDeliveryaddressDao.selectAgentDeliveryaddressListPaging(data);
		List<INSBAgentDeliveryaddress> rows = new ArrayList<>();
		
		Iterator<Map<Object, Object>> iterator = infoList.iterator();
		while (iterator.hasNext()) {
			INSBAgentDeliveryaddress tmpAddress =(INSBAgentDeliveryaddress)iterator.next(); 
			
			String recipientprovinceTmp = tmpAddress.getRecipientprovince();
			String recipientcityTmp = tmpAddress.getRecipientcity();
			String recipientareaTmp = tmpAddress.getRecipientarea();
			
			if(recipientprovinceTmp != null && !recipientprovinceTmp.equals("") && recipientareaTmp.contains("-")) {
				String[] buff = recipientprovinceTmp.split("-");
				tmpAddress.setRecipientprovince( buff[1].trim());
				
			}
			if(recipientprovinceTmp != null && !recipientprovinceTmp.equals("") && recipientareaTmp.contains("-")) {
				String[] buff = recipientcityTmp.split("-");
				tmpAddress.setRecipientcity( buff[1].trim());
				
			}
			if(recipientprovinceTmp != null && !recipientprovinceTmp.equals("") && recipientareaTmp.contains("-")) {
				String[] buff = recipientareaTmp.split("-");
				tmpAddress.setRecipientarea( buff[1].trim());
				
			}
			rows.add(tmpAddress);

		} 
		

		map.put("total", insbAgentDeliveryaddressDao.selectCountDeliveryaddress(data));
		map.put("rows", rows);
		return map;
	}

	@Override
	public List<Map<Object, Object>> queryAgentDeliveryAddressList(String agentid) {
		//Map<String, Object> map = new HashMap<String, Object>();
		List<Map<Object, Object>> infoList = insbAgentDeliveryaddressDao.queryAgentDeliveryAddressList(agentid);
		//map.put("rows", infoList);
		return infoList;
	}

	/**
	 * 设置用户的默认配置地址
	 * @param id 配置地址id
	 * @param agentid 代理人id
	 */
	public void setDefaultAgentDeliveryAddress(String id, String agentid){
		 insbAgentDeliveryaddressDao.updateIsdefaultByAgentid(agentid,"0");
		 insbAgentDeliveryaddressDao.updateIsdefaultById(id,"1");
	}

	@Override
	public int saveOrUpdateAgentDeliveryaddress(INSBAgentDeliveryaddress agentDeliveryaddress) {
		if ("1".equals(agentDeliveryaddress.getIsdefault())) {
			insbAgentDeliveryaddressDao.updateIsdefaultByAgentid(agentDeliveryaddress.getAgentid(), "0");
		}
		if(agentDeliveryaddress.getId() == null || agentDeliveryaddress.getId().trim().equals("")){
			agentDeliveryaddress.setCreatetime(new Date());
			return insbAgentDeliveryaddressDao.addAgentDeliveryaddress(agentDeliveryaddress);
		}else{
			INSBAgentDeliveryaddress address = insbAgentDeliveryaddressDao.selectById(agentDeliveryaddress.getId());
			if(address!=null && address.getId()!=null && address.getId().trim().length()>0){
				AgentDeliveryaddressUtils.copySouceToTarget(agentDeliveryaddress, address);
				address.setModifytime(new Date());
				return insbAgentDeliveryaddressDao.updateById(address);
			}else {
				agentDeliveryaddress.setCreatetime(new Date());
				return insbAgentDeliveryaddressDao.addAgentDeliveryaddress(agentDeliveryaddress);
			}
		}
	}
}