package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.dao.INSBAgreementscopeDao;
import com.zzb.conf.dao.INSBAgreementzoneDao;
import com.zzb.conf.entity.INSBAgreementzone;
import com.zzb.conf.service.INSBAgreementareaService;
import com.zzb.conf.service.INSBAgreementzoneService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsbSaveScopeParam;

@Service
@Transactional
public class INSBAgreementzoneServiceImpl extends BaseServiceImpl<INSBAgreementzone> implements
		INSBAgreementzoneService {
	@Resource 
	private INSBAgreementzoneDao insbAgreementzoneDao;
	@Resource 
	private INSBAgreementscopeDao insbAgreementscopeDao;

	@Override
	protected BaseDao<INSBAgreementzone> getBaseDao() {
		return insbAgreementzoneDao;
	}

	@Override
	public void savezones(INSCUser operator, InsbSaveScopeParam model) {
		//协议只能关联一个省里的网点，要先删除以前关联的网点
		List<String> listcityList=insbAgreementzoneDao.getCityByAgreementId(model.getAgreementid());
		for (String city : listcityList) {
			insbAgreementscopeDao.deletebyagreementid(model.getAgreementid(), city);
		}
		insbAgreementzoneDao.deleteGuanlian(model.getAgreementid());
		List<String> listciytid=(List<String>) model.getCitys();
		for (String cityid : listciytid) {
			INSBAgreementzone sAgreementzone=new INSBAgreementzone();
			sAgreementzone.setAgreementid(model.getAgreementid());
			sAgreementzone.setProvince(model.getProvince());
			sAgreementzone.setCity(cityid);
			sAgreementzone.setOperator(operator.getName());
			sAgreementzone.setCreatetime(new Date());
			insbAgreementzoneDao.insert(sAgreementzone);
		}
	}

	@Override
	public CommonModel getZone(String agreementid) {
		CommonModel model=new CommonModel();
		try {
			List<Map<String, Object>> list= insbAgreementzoneDao.getProvinces(agreementid);
			for (Map<String, Object> province : list) {
				Map<String, String> map= new HashMap<String, String>();
				map.put("agreementid", agreementid);
				map.put("comcode", (String) province.get("comcode"));
				province.put("city", insbAgreementzoneDao.getCityByProvince(map));
			}
			model.setBody(list);
			model.setMessage("查询成功！");
			model.setStatus("success");
			return model;
		} catch (Exception e) {
			model.setMessage("查询失败，请重试！");
			model.setStatus("fail");
			return model;
		}
	}

}