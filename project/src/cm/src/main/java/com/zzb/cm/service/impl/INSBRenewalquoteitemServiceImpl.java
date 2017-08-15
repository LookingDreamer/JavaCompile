package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBRenewalquoteitemDao;
import com.zzb.cm.entity.INSBRenewalquoteitem;
import com.zzb.cm.service.INSBRenewalquoteitemService;

import java.util.List;

@Service
@Transactional
public class INSBRenewalquoteitemServiceImpl extends BaseServiceImpl<INSBRenewalquoteitem> implements
		INSBRenewalquoteitemService {
	@Resource
	private INSBRenewalquoteitemDao renewalquoteitemDao;

	@Override
	protected BaseDao<INSBRenewalquoteitem> getBaseDao() {
		return renewalquoteitemDao;
	}

    public int deleteByTask(String taskid, String inscomcode) {
        return renewalquoteitemDao.deleteByTask(taskid, inscomcode);
    }

    public int save(List<INSBRenewalquoteitem> quoteitemList) {
        if (quoteitemList == null || quoteitemList.isEmpty()) return 0;

        renewalquoteitemDao.insertInBatch(quoteitemList);

        return quoteitemList.size();
    }
}