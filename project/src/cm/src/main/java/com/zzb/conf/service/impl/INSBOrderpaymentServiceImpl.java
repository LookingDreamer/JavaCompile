package com.zzb.conf.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.ldap.LdapMd5;

@Service
@Transactional
public class INSBOrderpaymentServiceImpl extends BaseServiceImpl<INSBOrderpayment> implements
		INSBOrderpaymentService {
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;

	@Override
	protected BaseDao<INSBOrderpayment> getBaseDao() {
		
		return insbOrderpaymentDao;
	}

	@Override
	public INSBOrderpayment selectBySerialNumber(String serialNumber) {
		INSBOrderpayment payment =new INSBOrderpayment();
		if (StringUtil.isEmpty(serialNumber)) {
			return null;
		}
		payment.setPaymentransaction(serialNumber);
		try{
			List<INSBOrderpayment> list = insbOrderpaymentDao.selectList(payment);
			if(list.size()>0){
				return list.get(0);
			}
		}catch(Exception e){
			return null;
		}
		
		return null;
	}
	/**
	 * 验证信息是否被篡改
	 */
	@Override
	public boolean validateOrder(INSBOrderpayment payment) {
		String md5 = LdapMd5.Md5Encode(payment.getAmount()+payment.getPayresult()+payment.getPaymentransaction());
		if(payment.getMd5().equals(md5))
			return true;
		return false;
	}

	@Override
	public List<Map> refund(Map map) {
		return insbOrderpaymentDao.refund(map);
	}

	@Override
	public Long refundCount(Map map) {
		return insbOrderpaymentDao.refundCount(map);
	}

	@Override
	public int updateRefundstatus(INSBOrderpayment orderpayment) {
		return insbOrderpaymentDao.updateRefundstatus(orderpayment);
	}

	public Map<String, Object> refundtask(String taskid) {
		return insbOrderpaymentDao.refundtask(taskid);
	}
}