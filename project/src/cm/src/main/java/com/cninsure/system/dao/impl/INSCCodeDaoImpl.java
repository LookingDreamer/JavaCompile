package com.cninsure.system.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
import com.zzb.model.AppCodeModel;

@Repository
public class INSCCodeDaoImpl extends BaseDaoImpl<INSCCode> implements
		INSCCodeDao {
	@Override
	public void insert(INSCCode insccode){
		
		this.sqlSessionTemplate.insert(this.getSqlName("insert"),insccode);
	}
	
	@Override
	public List<INSCCode> selectINSCCodeByCode(Map<String, String> para) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"), para);
	}

	public List<INSCCode> selectINSCCodeByParentCode(Map<String,String> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"), map);
	}

	@Override
	public Map<String, String> selectWorkflowNodeNameByCode(String codevalue) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectWorkflowNodeNameByValue"), codevalue);
	}

	@Override
	public String selectByOrderStatus(String orderStatus) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("seelctByOrderStatus"), orderStatus);
	}

	@Override
	public List<AppCodeModel> selectByTypes(List<String> types) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByTypes"), types);
	}
	
	@Override
	public  List<Map<String, Object>>  selectByType(String codetype) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByType"), codetype);
	}

	@Override
	public String selectCodeValueByCodeName(Map<String,String> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByCodeName"), map);
	}

	@Override
	public List<INSCCode> selectBank(Map<String, String> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectBank"), map);
	}

	@Override
	public INSCCode selectBankList(String codevalue) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("codetype", "bankcard");
		map.put("parentcode", "bankcard");
		map.put("codevalue", codevalue);
		return selectINSCCodeByCode(map).get(0);
	}

	@Override
	public List<INSCCode> selectMyTaskCode(Map<String, String> para) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectMyTaskCode"), para);
	}

	@Override
	public INSCCode transferCodeToName(Map<String, String> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("select"), para);
	}

	/**
	 * 车险任务管理页面任务类型下拉框选项  liuchao
	 */
	@Override
	public List<Map<String, String>> getWorkFlowNodesForCarTaskQuery() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getWorkFlowNodesForCarTaskQuert"));
	}

	/**
	 * 查询物流公司
	 */
	@Override
	public String getCodenameByLogisticscompany(String logisticscompany) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getCodenameByLogisticscompany"),logisticscompany);
	}
	@Override
	public String getcodeNameBycodeValue(String forname) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getcodeNameBycodeValue"),forname);
	}
	
	@Override
	public List<Map<String, Object>> queryAllErrorCode(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryAllErrorCode"), map);
	}
	
	@Override
	public int queryAllErrorCodeCount() {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryAllErrorCodeCount"));
	}

	@Override
	public Map<String, String> getCodeNamesMap(String codeType, String parentCode) {
		if (StringUtil.isEmpty(codeType) && StringUtil.isEmpty(parentCode)) {
			return null;
		}
		Map<String, String> codeNames = null;
		Map<String, String> queryCode = new HashMap<String, String>(2);
		if (StringUtil.isNotEmpty(codeType)) {
			queryCode.put("codetype", codeType);
		}
		if (StringUtil.isNotEmpty(parentCode)) {
			queryCode.put("parentcode", parentCode);
		}
		List<INSCCode> codeList = selectINSCCodeByCode(queryCode);//获取对应的码表集合
		if (codeList != null && codeList.size() > 0) {
			codeNames = new HashMap<String, String>();
			for (INSCCode code : codeList) {
				if (code != null) {
					codeNames.put(code.getCodevalue(), code.getCodename());
				}
			}
		}
		return codeNames;
	}
}