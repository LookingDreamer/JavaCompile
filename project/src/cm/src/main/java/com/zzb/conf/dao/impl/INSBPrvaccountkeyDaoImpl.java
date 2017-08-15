package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBPrvaccountkeyDao;
import com.zzb.conf.entity.INSBPrvaccountkey;

@Repository
public class INSBPrvaccountkeyDaoImpl extends BaseDaoImpl<INSBPrvaccountkey> implements
		INSBPrvaccountkeyDao {
	@Override
	public List<INSBPrvaccountkey> selectListPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListPage"), map);
	}

	@Override
	public int selectPageCountByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectPageCountByMap"), map);
	}
	

	@Override
	public List<INSBPrvaccountkey> selectListPageNew(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListPageNew"), map);
	}

	@Override
	public int selectPageCountByMapNew(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectPageCountByMapNew"), map);
	}

	@Override
	public List<INSBPrvaccountkey> extendsPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("extendsPage"), map);
	}

	@Override
	public int extendscount(Map<String, Object> map) {
		List<Integer> cs =this.sqlSessionTemplate.selectList(this.getSqlName("extendscount"), map);
		int i =0 ;
		for (Integer j:cs) {
			i++;
		}
		return i;
	}

	@Override
	public List<Map<String, Object>> selectConfigInfo(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectConfigInfo"), map);
	}

}