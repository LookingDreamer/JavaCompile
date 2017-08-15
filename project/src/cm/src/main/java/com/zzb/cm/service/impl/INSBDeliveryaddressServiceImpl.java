package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBDeliveryaddressDao;
import com.zzb.cm.entity.INSBDeliveryaddress;
import com.zzb.cm.service.INSBDeliveryaddressService;

@Service
@Transactional
public class INSBDeliveryaddressServiceImpl extends BaseServiceImpl<INSBDeliveryaddress> implements
		INSBDeliveryaddressService {
	@Resource
	private INSBDeliveryaddressDao insbDeliveryaddressDao;

	@Override
	protected BaseDao<INSBDeliveryaddress> getBaseDao() {
		return insbDeliveryaddressDao;
	}

}