package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.core.exception.DaoException;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.conf.dao.INSBPolicyManageDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.model.PolicyDetailedModel;

@Repository
public class INSBPolicyManageDaoImpl extends BaseDaoImpl<INSBPolicyitem> implements INSBPolicyManageDao {
	
	@Override
	public List<Map<Object, Object>> queryPagingList (Map<String, Object> map) throws DaoException{
		return this.sqlSessionTemplate.selectList("policyandordermanage.queryPolicyPagingList", map);
	}

	@Override
	public long queryPagingListCount(Map<String, Object> map) throws DaoException {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.queryPolicyPagingListCount", map);
	}

	@Override
	public INSBAgent selectAgentByNum(String agentNum) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectAgentByNum", agentNum);
	}

	@Override
	public INSBPerson selectPolicyHolderInfo(String taskid) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectPolicyHolderInfo", taskid);
	}

	@Override
	public INSBPerson selectInsuredInfo(String taskid) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectInsuredInfo", taskid);
	}

	@Override
	public INSBPolicyitem selectPolcyByOIdAndType(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectPolcyByOIdAndType", map);
	}
	
	@Override
	public INSBPolicyitem selectPolcyByOIdAndType2(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectPolcyByOIdAndType2", map);
	}
	
	@Override
	public INSBPolicyitem selectPolcyByOIdAndType2his(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectPolcyByOIdAndType2his", map);
	}

	@Override
	public INSBCarinfo selectCarInfoByTaskId(String taskid) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectCarInfoByTaskId", taskid);
	}
	
	@Override
	public INSBCarinfohis selectCarInfoByhis(String taskid, String proid) {
		Map<String, String> parm = new HashMap<String, String>();
		parm.put("taskid", taskid);
		parm.put("proid", proid);
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectCarInfoByhis", parm);
	}

	@Override
	public List<PolicyDetailedModel> queryImgInfo(String id) {
		return this.sqlSessionTemplate.selectList("policyandordermanage.queryImgInfo",id);
	}
	@Override
	public INSBPolicyitem selectRecognizeeByorderid(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectRecognizeeByorderid", map);
	}
	@Override
	public INSBPolicyitem selectRecognizeeByorderidhis(Map<String, String> maphis) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectRecognizeeByorderidhis", maphis);
	}
	@Override
	public INSBPolicyitem selectInsureByorderid(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectInsureByorderid", map);
	}
	
	@Override
	public INSBPolicyitem selectInsureByorderidhis(Map<String, String> maphis) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectInsureByorderidhis", maphis);
	}

	@Override
	public INSBPolicyitem selectRecognizeeBypoid(String poid) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectRecognizeeBypoid", poid);
	}

	@Override
	public INSBPolicyitem selectInsureBypoid(String poid) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectInsureBypoid", poid);
	}

	@Override
	public INSBPolicyitem selectPolcyBypId(String poid) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectPolcyBypId", poid);
	}

	public INSBPolicyitem selectContactsByTask(Map<String, String> maphis) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectContactsByTask", maphis);
	}

	public INSBPolicyitem selectContactsByOrderid(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectContactsByOrderid", map);
	}

	public INSBPolicyitem selectContactsBypoid(String poid) {
		return this.sqlSessionTemplate.selectOne("policyandordermanage.selectContactsBypoid", poid);
	}
}
