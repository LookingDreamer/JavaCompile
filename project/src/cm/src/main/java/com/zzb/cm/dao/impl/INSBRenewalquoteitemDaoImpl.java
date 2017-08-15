package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBRenewalquoteitemDao;
import com.zzb.cm.entity.INSBRenewalquoteitem;

import java.util.HashMap;
import java.util.Map;

@Repository
public class INSBRenewalquoteitemDaoImpl extends BaseDaoImpl<INSBRenewalquoteitem> implements
		INSBRenewalquoteitemDao {

    public int deleteByTask(String taskid, String inscomcode) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("taskid", taskid);
        params.put("inscomcode", inscomcode);
        return this.sqlSessionTemplate.delete(this.getSqlName("deleteByTask"), params);
    }
}