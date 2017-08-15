package com.zzb.conf.service.impl;

import javax.annotation.Resource;

import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.dao.INSBRenewalitemDao;
import com.zzb.conf.entity.INSBRenewalitem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBRenewalconfigitemDao;
import com.zzb.conf.entity.INSBRenewalconfigitem;
import com.zzb.conf.service.INSBRenewalconfigitemService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class INSBRenewalconfigitemServiceImpl extends BaseServiceImpl<INSBRenewalconfigitem> implements
		INSBRenewalconfigitemService {
	@Resource
	private INSBRenewalconfigitemDao renewalconfigitemDao;
    @Resource
    private INSBRenewalitemDao renewalitemDao;

	@Override
	protected BaseDao<INSBRenewalconfigitem> getBaseDao() {
		return renewalconfigitemDao;
	}

    public int deleteByAgreementid(String agreementid) {
        return renewalconfigitemDao.deleteByAgreementid(agreementid);
    }

    public int save(String agreementid, List<String> configItemcodes, INSCUser operator) {
        if (configItemcodes == null || configItemcodes.size() == 0) {
            return 0;
        }

        List<INSBRenewalitem> renewalitems = renewalitemDao.selectAllByCodes(configItemcodes);
        if (renewalitems == null || renewalitems.size() == 0) {
            return 0;
        }

        List<INSBRenewalconfigitem> batch = new ArrayList<>();

        for (String code : configItemcodes) {
            for (INSBRenewalitem renewalitem : renewalitems) {
                if (renewalitem.getItemcode().equals(code)) {
                    INSBRenewalconfigitem renewalconfigitem = new INSBRenewalconfigitem();
                    renewalconfigitem.setAgreementid(agreementid);
                    renewalconfigitem.setItemcode(renewalitem.getItemcode());
                    renewalconfigitem.setItemname(renewalitem.getItemname());
                    renewalconfigitem.setItemorder(renewalitem.getItemorder());
                    renewalconfigitem.setIsrequired(renewalitem.getIsrequired());
                    renewalconfigitem.setOperator(operator.getUsercode());
                    renewalconfigitem.setCreatetime(new Date());
                    renewalconfigitem.setModifytime(new Date());
                    renewalconfigitem.setNoti(renewalitem.getItemname());
                    batch.add(renewalconfigitem);
                }
            }
        }

        if (!batch.isEmpty()) {
            renewalconfigitemDao.insertInBatch(batch);
            return batch.size();
        }

        return 0;
    }
}