package com.zzb.conf.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.controller.vo.DeptPayTypeVo;
import com.zzb.conf.dao.INSBPaychannelmanagerDao;
import com.zzb.conf.entity.INSBPaychannelmanager;

@Repository
public class INSBPaychannelmanagerDaoImpl extends BaseDaoImpl<INSBPaychannelmanager> implements
		INSBPaychannelmanagerDao {

	@Resource
	private INSCDeptDao deptDao;
	
	@Override
	public Long deleteByExceptIds(List<String> ids,String agreementid){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		params.put("agreementid",agreementid);
		return (long) this.sqlSessionTemplate.delete(this.getSqlName("deleteByExceptIds"),params);
	}

	@Override
	public List<String> selectPaychannelIdByAgreementId(String Depcode,String prvid) {
		List<String> result = new ArrayList<String>();
		
//		Map<String, Object> map = new HashMap<String,Object>();
//		map.put("deptcode", Depcode);
//		map.put("providerid",prvid);
//		result = this.sqlSessionTemplate.selectList(this.getSqlName("selectPaychannelIdByAgreementId"),map);


		String deptId = getPingTaiDeptId(Depcode);
		Map<String, Object> map1 = new HashMap<String,Object>();
		map1.put("ptdeptcode", deptId);
		map1.put("deptcode", Depcode);
		map1.put("providerid",prvid);
		List<String> result2 = this.sqlSessionTemplate.selectList(this.getSqlName("selectPaychannelIdByAgreementId"),map1);

		result.addAll(result2);

		return result;
	}

	@Override
	public List<INSBPaychannelmanager> selectPagePaywayList(
			Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPagePaywayList"),map);
	}
	
	@Override
	public void deleteByLogicId(Map<String, String> param) {
		
		int result = this.sqlSessionTemplate.delete(this.getSqlName("deleteByLogicId"), param);
		if(result==0){
			String deptId = getPingTaiDeptId(param.get("deptid"));
			param.put("deptid", deptId);
			this.sqlSessionTemplate.delete(this.getSqlName("deleteByLogicId"), param);
		}
	}

	@Override
	public INSBPaychannelmanager queryManager(Map<String, String> param) {
		INSBPaychannelmanager result = new INSBPaychannelmanager();
		result =  this.sqlSessionTemplate.selectOne(this.getSqlName("queryManager"),param);
		if(result==null){
			String deptId = getPingTaiDeptId(param.get("deptid"));
			param.put("deptid", deptId);
			result = this.sqlSessionTemplate.selectOne(this.getSqlName("queryManager"),param);
		}
		return result;
	}

	@Override
	public List<DeptPayTypeVo> getDeptPayType(Map<String, Object> map) {
		List<DeptPayTypeVo> result = new ArrayList<DeptPayTypeVo>();
		result =  this.sqlSessionTemplate.selectList(this.getSqlName("getDeptPayType"),map);
		if(result.isEmpty()){
			String deptId = getPingTaiDeptId(map.get("deptid").toString());
			map.put("deptid", deptId);
			result =  this.sqlSessionTemplate.selectList(this.getSqlName("getDeptPayType"),map);
		}
		return result;
	}

	@Override
	public List<INSBPaychannelmanager> queryListByParam(
			Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryListByParam"),param);
	}

	@Override
	public Long selectDataSize(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectDataSize"),param);
	}
	
	private String getPingTaiDeptId(String deptId){
		String result="";
		INSCDept deptModel = deptDao.selectById(deptId);
		if(deptModel==null){
			return result;
		}
		
		String tempInnerCode = deptModel.getDeptinnercode();
		if(StringUtils.isEmpty(tempInnerCode)){
			return result;
		}
		if(tempInnerCode.length()==5){
			result = deptId;
		}else if(tempInnerCode.length()<5){
			result = deptId;
		}else if(tempInnerCode.length()>5){
			String pingTaiInnerCode = tempInnerCode.substring(0, 5);
			result =  deptDao.seleDeptIdByInnerCode(pingTaiInnerCode);
		}
		return result;
	}

}