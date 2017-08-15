package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBRenewalitemDao;
import com.zzb.conf.entity.INSBRenewalitem;
import com.zzb.conf.service.INSBRenewalitemService;

import java.util.List;

@Service
@Transactional
public class INSBRenewalitemServiceImpl extends BaseServiceImpl<INSBRenewalitem> implements
		INSBRenewalitemService {
	@Resource
	private INSBRenewalitemDao renewalitemDao;

	@Override
	protected BaseDao<INSBRenewalitem> getBaseDao() {
		return renewalitemDao;
	}

    public List<INSBRenewalitem> selectAllByCodes(List<String> codes) {
        return renewalitemDao.selectAllByCodes(codes);
    }

	@Override
	public List<INSBRenewalitem> queryAll() {
		return renewalitemDao.selectAll();
	}
}