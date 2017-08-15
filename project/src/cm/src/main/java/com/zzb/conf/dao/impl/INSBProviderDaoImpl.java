package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.model.ProOrParModel;

@Repository
public class INSBProviderDaoImpl extends BaseDaoImpl<INSBProvider> implements
		INSBProviderDao {

	@Override
	public List<Map<Object, Object>> selectProviderLitByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"),map);
	}

	@Override
	public List<INSBProvider> selectByParentProTreeCode(String parentcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByParentProTreeCode"),parentcode);
	}
	
	@Override
	public List<INSBProvider> selectByParentProTreeCodeStair() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByParentProTreeCodeStair"));
	}
	@Override
	public List<INSBProvider> selectByParentProTreeCode2(ProOrParModel parentcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByParentProTreeCode2"),parentcode);
	}

	@Override
	public int addProData(INSBProvider pro) {
		
		return this.sqlSessionTemplate.insert(this.getSqlName("insert"),pro);
	}

	@Override
	public INSBProvider queryProinfoById(String id) {
		
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectById"),id);
	}

	@Override
	public INSBProvider queryByPrvcode(String prvcode) {
		
		return this.sqlSessionTemplate.selectOne(this.getSqlName("prvcode"),prvcode);
	}

	@Override
	public int updateProById(String id) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateProById"),id);
	}

	@Override
	public int updateProByIddel(String id) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateProByIddel"),id);
	}

	@Override
	public List<INSBProvider> queryListPro(String ediid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryListPro"),ediid);
	}

	@Override
	public List<INSBProvider> selectByModifytime(String modifytime) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByModifytime"), modifytime);
	}

	@Override
	public List<Map<String, String>> selectByParentProTreeCode3(
			String parentcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("seelctListMapByPCode"), parentcode);
	}

	@Override
	public int getQuotationValidityById(String id) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getQuotationValidityById"), id);
	}
	
	/**
	 * 查询顶级 供应商
	 */
	public INSBProvider selectFatherProvider(String sonPrvId){
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectFatherProvider"), sonPrvId);
	}

	@Override
	public List<INSBProvider> selectProvider() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectProviderdata"));
	}

	@Override
	public List<Map<String, String>> selectEdiProvider(String userdept) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectEdiProvider"), userdept);
	}

	@Override
	public List<Map<String, String>> selectEdiAllProvider(String userdept) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectEdiAllProvider"), userdept);
	}

	@Override
	public List<INSBProvider> selectByInsureAreaCode(String insureAreaCode) {
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByInsureAreaCode"), insureAreaCode);
	}

	@Override
	public List<INSBProvider> selectByInsureAreaCode2(Map<String, Object> map) {

		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByInsureAreaCode2"), map);
	}

	@Override
	@Deprecated
	public List<INSBProvider> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}

	@Override
	public List<INSBProvider> selectListIn(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListIn"), params);
	}
	
	
	
	
}