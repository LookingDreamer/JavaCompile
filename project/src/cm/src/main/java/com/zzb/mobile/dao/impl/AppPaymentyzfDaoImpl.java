package com.zzb.mobile.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.mobile.dao.AppPaymentyzfDao;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.model.AppPaymentyzfRModel;

@Repository
public class AppPaymentyzfDaoImpl extends BaseDaoImpl<AppPaymentyzf> implements AppPaymentyzfDao {

	@Override
	public void insert(AppPaymentyzf yzf){
		
		this.sqlSessionTemplate.insert(this.getSqlName("insert"), yzf);
	}

	@Override
	public  List<Map<String,Object>> getBankType(){
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("getBankType"));
	}
	@Override
	public  List<Map<String,Object>> getidCardType(){
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("getidCardType"));
	}
	@Override
	public List<AppPaymentyzfRModel> querySignInfos(Map<String,Object> map){
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("querySignInfos"),map);
	}
	@Override
	public  List<Map<String,Object>> getProvince(){
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("getProvince"));
	}
	@Override
	public  List<Map<String,Object>> getCity(String provinceID){
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("getCity"),provinceID);
	}

	@Override
	public Map<String, Object> queryFromInsccodeByCode(Map<String, Object> map) {
		
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryFromInsccodeByCode"),map);
	}

	@Override
	public Map queryFromInsbregionByCode(Map<String, Object> map) {
		
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryFromInsbregionByCode"),map);
	}

	@Override
	public List<Map<String, Object>> getBankCardType() {
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("getBankCardType"));
	}

}
