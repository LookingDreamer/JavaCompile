package com.zzb.conf.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.exception.DaoException;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.ldap.LdapMd5;
import com.zzb.mobile.service.InsbpaymenttransactionService;

@Repository
public class INSBOrderpaymentDaoImpl extends BaseDaoImpl<INSBOrderpayment> implements
		INSBOrderpaymentDao {
	@Resource
	private InsbpaymenttransactionService insbpaymenttransactionService;
	
	@Override
	public void insert(INSBOrderpayment arg){
		if(arg.getPaymentransaction()==null || arg.getPaymentransaction().trim().length()==0){
			String paymentransaction=insbpaymenttransactionService.getPaymenttransaction(new Date());
			arg.setPaymentransaction(paymentransaction);
		}
		String md5 = LdapMd5.Md5Encode(arg.getAmount()+arg.getPayresult()+arg.getPaymentransaction());
		arg.setMd5(md5);
		super.insert(arg);	
		
	}
	@Override 
	public int updateById(INSBOrderpayment arg){
		String md5 = LdapMd5.Md5Encode(arg.getAmount()+arg.getPayresult()+arg.getPaymentransaction());
		arg.setMd5(md5);
		return super.updateById(arg);
	}
	@Override 
	public int updateByIdSelective(INSBOrderpayment arg){
		String md5 = LdapMd5.Md5Encode(arg.getAmount()+arg.getPayresult()+arg.getPaymentransaction());
		arg.setMd5(md5);
		return super.updateById(arg);
	}
	@Override
	public void updateInBatch(List<INSBOrderpayment> args){
		for(INSBOrderpayment arg:args){
			arg.setMd5(LdapMd5.Md5Encode(arg.getAmount()+arg.getPayresult()+arg.getPaymentransaction()));
		}
		super.updateInBatch(args);
	}
	@Override
	public INSBOrderpayment selectOne(INSBOrderpayment arg) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOne"),arg);
	}
	/**
	 * 查询最新的insborderpayment表信息
	 *  时间排序
	 * @param orderpayment
	 * @return
	 */
	@Override
	public INSBOrderpayment selectOneByCode(INSBOrderpayment orderpayment) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOneByCode"),orderpayment);
	}
	
	@Override
	public INSBOrderpayment selectNewestPayInfo(INSBOrderpayment orderpayment)throws DaoException {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectNewestPayInfo"),orderpayment);
	}

	@Override
	public List<Map> refund(Map map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("refund"),map);
	}

	@Override
	public Map<String, Object> refundtask(String taskid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("refundtask"),taskid);
	}

	@Override
	public Long refundCount(Map map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("refundCount"),map);
	}

	@Override
	public int updateRefundstatus(INSBOrderpayment orderpayment) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateRefundstatus"),orderpayment);
	}
}