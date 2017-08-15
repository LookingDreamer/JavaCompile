package com.zzb.conf.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.controller.vo.DistributionVo;
import com.zzb.conf.dao.INSBDistributionDao;
import com.zzb.conf.entity.INSBDistribution;
import com.zzb.conf.service.INSBDistributionService;

@Service
@Transactional
public class INSBDistributionServiceImpl extends BaseServiceImpl<INSBDistribution> implements
		INSBDistributionService {
	@Resource
	private INSBDistributionDao insbDistributionDao;
 
	@Override
	protected BaseDao<INSBDistribution> getBaseDao() {
		return insbDistributionDao;
	}

	@Override
	public List<INSBDistribution> getAllDistribution(String deptid,
			String agreementid, String providerid) {
		INSBDistribution distribution=new INSBDistribution();
		distribution.setAgreementid(agreementid);
		distribution.setDeptid(deptid);
		distribution.setProviderid(providerid);
		List<INSBDistribution> allDistribution=insbDistributionDao.selectList(distribution);
		return allDistribution;
	}

	@Override
	public DistributionVo getDistribution(String agreementid,String providerid, String deptid) {
		INSBDistribution temp = new INSBDistribution();
		temp.setProviderid(providerid);
		temp.setAgreementid(agreementid);
		temp.setDeptid(deptid);
		List<INSBDistribution> distributionList = insbDistributionDao.selectList(temp);
		DistributionVo tempVo = new DistributionVo();
		tempVo.setAgreementid(agreementid);
		tempVo.setProviderid(providerid);
		tempVo.setDeptid(deptid);
		for (INSBDistribution distribution : distributionList) {
			if("1".equals(distribution.getDistritype())){//自取
				tempVo.setSelfdistritype("1");
				tempVo.setSelfnoti(distribution.getNoti());
			}else{//快递
				tempVo.setDistdistritype("2");
				tempVo.setDistnoti(distribution.getNoti());
				tempVo.setDistpaytype(distribution.getDistpaytype());
				tempVo.setDistrcompany(distribution.getDistrcompany());
				if(distribution.getChargefee()!=null)
				tempVo.setChargefee(distribution.getChargefee().toString());
			}
		}
		return tempVo;
	}

	@Override
	public void saveDistribution(DistributionVo distributionVo) {
		INSBDistribution temp = new INSBDistribution();
		temp.setProviderid(distributionVo.getProviderid());
		temp.setAgreementid(distributionVo.getAgreementid());
		temp.setDeptid(distributionVo.getDeptid());
		temp.setCreatetime(new Date());
		List<INSBDistribution> distributionList = insbDistributionDao.selectList(temp);
		if(distributionList!=null && distributionList.size()>0){
			for (INSBDistribution distribution : distributionList) {
				insbDistributionDao.deleteById(distribution.getId());
			}
		}
		if("1".equals(distributionVo.getSelfdistritype())){//自取
			temp.setId(UUIDUtils.create());
			temp.setDistritype("1");
			temp.setNoti(distributionVo.getSelfnoti());
			insbDistributionDao.insert(temp);
		}
		if("2".equals(distributionVo.getDistdistritype())){//快递
			temp.setId(UUIDUtils.create());
			temp.setDistritype("2");
			temp.setNoti(distributionVo.getDistnoti());
			temp.setDistpaytype(distributionVo.getDistpaytype());
			temp.setDistrcompany(distributionVo.getDistrcompany());
			if(!StringUtil.isEmpty(distributionVo.getChargefee()))
			temp.setChargefee(Double.parseDouble(distributionVo.getChargefee()));
			insbDistributionDao.insert(temp);
		}
	}
	
}