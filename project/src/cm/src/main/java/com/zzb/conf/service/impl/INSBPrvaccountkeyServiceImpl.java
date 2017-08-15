package com.zzb.conf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBPrvaccountkeyDao;
import com.zzb.conf.entity.INSBPrvaccountkey;
import com.zzb.conf.service.INSBPrvaccountkeyService;

@Service
@Transactional
public class INSBPrvaccountkeyServiceImpl extends BaseServiceImpl<INSBPrvaccountkey> implements
		INSBPrvaccountkeyService {
	@Resource
	private INSBPrvaccountkeyDao insbPrvaccountkeyDao;

	@Override
	protected BaseDao<INSBPrvaccountkey> getBaseDao() {
		return insbPrvaccountkeyDao;
	}

	@Override
	public List<Map<String, Object>> queryConfigInfo(String deptId, String providerid, String usetype) {
		Map<String,Object> parm = new HashMap<String, Object>();
		parm.put("deptId", deptId);
		parm.put("providerid", providerid);
		parm.put("usetype", usetype);
		return insbPrvaccountkeyDao.selectConfigInfo(parm);
	}

}