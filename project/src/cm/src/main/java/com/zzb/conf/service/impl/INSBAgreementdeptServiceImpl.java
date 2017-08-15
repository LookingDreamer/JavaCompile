package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBAgreementdeptDao;
import com.zzb.conf.dao.INSBAgreementproviderDao;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.dao.INSBChannelagreementDao;
import com.zzb.conf.entity.INSBAgreementdept;
import com.zzb.conf.entity.INSBAgreementprovider;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBChannelagreement;
import com.zzb.conf.service.INSBAgreementdeptService;

@Service
@Transactional
public class INSBAgreementdeptServiceImpl extends BaseServiceImpl<INSBAgreementdept> implements
		INSBAgreementdeptService {
	@Resource   
	private INSBAgreementdeptDao insbAgreementdeptDao;
	@Resource
	INSBChannelagreementDao channelagreementDao;
	@Resource
	private INSBChannelDao channelDao;
	@Resource
	private INSBAgreementproviderDao agreementproviderDao;
	@Override
	protected BaseDao<INSBAgreementdept> getBaseDao() {
		return insbAgreementdeptDao;
	}

	
	@Override
	public List<String> getINSBAgreementdept(String agreementId) {
		
		Map<String,String>mapParm=new HashMap<String, String>();
		mapParm.put("agreementid", agreementId);
		List<INSBAgreementdept>AgreementdeptList=insbAgreementdeptDao.getINSBAgreementdept(mapParm);
		for(INSBAgreementdept agreementdept:AgreementdeptList){
			//agreementdept.ge
		}
		
		return null;
	}


	@Override
	public List<Map<String, String>> getPrvider(INSBChannel channel) {
		Map<String,String> mapParm=new HashMap<String, String>();
		INSBChannel Tchannel= channelDao.selectById(channel.getId());
		mapParm.put("prvid", Tchannel.getDeptid());
		List<Map<String, String>>agreementdeptList=insbAgreementdeptDao.getPrvider(mapParm);
		
		return agreementdeptList;
	}
	
	
	@Override
	public List<INSBAgreementprovider> checkedPrvider(INSBChannel channel) {
//		INSBChannelagreement agreement=new INSBChannelagreement();
//		agreement.setChannelid(channel.getId());
//		INSBChannelagreement agreements=channelagreementDao.selectOne(agreement);
		List<INSBChannelagreement>agreements=channelagreementDao.getChannelagreement(channel.getId());
		INSBAgreementprovider agreementdept=new INSBAgreementprovider();
		for(INSBChannelagreement channelagreement:agreements){
			agreementdept.setAgreementid(channelagreement.getId());
		}
//		agreementdept.setAgreementid(agreements.getId());
		List<INSBAgreementprovider>checkedProvider=agreementproviderDao.selectList(agreementdept);
		return checkedProvider;
	}


	@Override
	public List<Object> getdeptname(String agreementid, String providerid) {
		List<Object> deptnameList=new ArrayList<Object>();
		Map<String,Object> deptname=new HashMap<String, Object>();
		deptname.put("agreementid", agreementid);
		deptname.put("providerid", providerid);
		Map<String,Object> map_deptname=insbAgreementdeptDao.getdeptname(deptname);
		deptnameList.add(map_deptname);
		return deptnameList;
	}


	@Override
	public int delByAgreeid(String agreeid) {
		return insbAgreementdeptDao.delByAgreeid(agreeid);
	}
	

}