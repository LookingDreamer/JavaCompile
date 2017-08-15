package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.BeanUtils;
import com.zzb.conf.dao.INSBRiskkindDao;
import com.zzb.conf.entity.INSBRiskkind;

@Repository
public class INSBRiskkindDaoImpl extends BaseDaoImpl<INSBRiskkind> implements
		INSBRiskkindDao {

	@Override
	public Long queryListByVoCount(INSBRiskkind riskkind) {
		Map<String, Object> params = BeanUtils.toMap(riskkind);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectVoCount"),params);
	}

	@Override
	public Long queryListByVoCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectVoCount"),map);
	}
	@Override
	public List<INSBRiskkind> queryListByVo(INSBRiskkind riskkind) {
		Map<String, Object> params = BeanUtils.toMap(riskkind);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectVo"),params);
	}
	@Override
	public List<INSBRiskkind> queryByRiskkindVopage(Map<String, Object> map) {		
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectVoBypage"),map);
	}
	@Override
	public List<INSBRiskkind> selectByModifyDate(String modifytime) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByModifytime"), modifytime);
	}

	/**
	 * liuchao查询排序后的险种
	 * */
	@Override
	public List<Map<String, String>> selectOrderedRiskkindByInscomcode(
			String inscomcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOrderedRiskkindByInscomcode"), inscomcode);
	}

	@Override
	public long selectCOuntByKindCode(Map<String,String> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCOuntByKindCode"), map);
	}

	@Override
	public String selectKindNameByCode(String code) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectKindNameByCode"), code);
	}

	@Override
	public List<String> selectAllPreKind() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllPreKind"));
	}

	@Override
	public int selectCountByRiskid(String riskid) {
		
		return  this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountByRiskid"), riskid);
	}

	@Override
	public int selectCountByKindcode(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectcountByKindcode"), map);
	}

	@Override
	public INSBRiskkind notDedByPreriskkind(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("notDedByPreriskkind"), map);
	}

	@Override
	public List<INSBRiskkind> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}

} 