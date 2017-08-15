package com.zzb.conf.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBRenewalitemDao;
import com.zzb.conf.entity.INSBRenewalitem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBRenewalitemDaoImpl extends BaseDaoImpl<INSBRenewalitem> implements
		INSBRenewalitemDao {

    public List<INSBRenewalitem> selectAllByCodes(List<String> codes) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("itemcodes", codes);
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllByCodes"), params);
    }

	@Override
	public List<INSBRenewalitem> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}
}