package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.common.ModelUtil;
import com.zzb.conf.controller.vo.DeptPayTypeVo;
import com.zzb.conf.dao.INSBAgreementpaymethodDao;
import com.zzb.conf.entity.INSBAgreementpaymethod;
import com.zzb.conf.service.INSBAgreementpaymethodService;

@Service
@Transactional
public class INSBAgreementpaymethodServiceImpl extends BaseServiceImpl<INSBAgreementpaymethod> implements
		INSBAgreementpaymethodService {
	@Resource
	private INSBAgreementpaymethodDao insbAgreementpaymethodDao;
 
	@Override
	protected BaseDao<INSBAgreementpaymethod> getBaseDao() {
		return insbAgreementpaymethodDao;
	}
	
	/**
	 * 获取INSBAgreementpaymethod中的支付id
	 */
	@Override
	public List<DeptPayTypeVo> getDeptPaymethod(String deptid,
			String agreementid, String providerid) {
		INSBAgreementpaymethod agreementpaymethod=new INSBAgreementpaymethod();
		//agreementpaymethod.setDeptid(deptid);
		agreementpaymethod.setAgreementid(agreementid);
		agreementpaymethod.setProviderid(providerid);
		List<String> selectedPaymethod = new ArrayList<String>();
		List<INSBAgreementpaymethod> list=insbAgreementpaymethodDao.selectList(agreementpaymethod);
		for (INSBAgreementpaymethod paymethod : list) {
			selectedPaymethod.add(paymethod.getPaychannelid());
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("agreeid", agreementid);
		map.put("deptid", deptid);
		map.put("providerid", providerid);
		List<DeptPayTypeVo> listVo = insbAgreementpaymethodDao.getDeptPayType(map);
		for (DeptPayTypeVo deptPayTypeVo : listVo) {
			deptPayTypeVo.setAgreementid(agreementid);
			deptPayTypeVo.setDeptid(deptid);
			deptPayTypeVo.setProviderid(providerid);
			if(selectedPaymethod.contains(deptPayTypeVo.getId())){
				deptPayTypeVo.setState(true);
				deptPayTypeVo.setModifytime(ModelUtil.conbertToString(new Date()));
				deptPayTypeVo.setCheck("开启");
			}else{
				deptPayTypeVo.setState(false);
				deptPayTypeVo.setCheck("关闭");
			}
		}
		return listVo;
	}

	@Override
	public int delByAgreeid(String agreeid) {
		return insbAgreementpaymethodDao.delByAgreeid(agreeid);
	}

	
}