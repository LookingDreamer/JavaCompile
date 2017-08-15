package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import com.cninsure.core.exception.DaoException;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.dao.INSBOrderManageDao;

@Repository
public class INSBOrderManageDaoImpl extends BaseDaoImpl<INSBOrder> implements
		INSBOrderManageDao {

	@Override
	public long queryOrderPagingListCount(Map<String, Object> map)throws DaoException {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.queryOrderPagingListCount", map);
	}

	@Override
	public List<Map<Object, Object>> queryOrderPagingList(
			Map<String, Object> map) throws DaoException{
		return this.sqlSessionTemplate.selectList("policyandordermanage.queryOrderPagingList", map);
	}

	@Override
	public List<Map<Object, Object>> queryPolicyInfoByOrderId(String orderid) {
		return this.sqlSessionTemplate.selectList("policyandordermanage.queryPolicyInfoByOrderId", orderid);
	}

}
