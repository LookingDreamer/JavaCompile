package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.model.PolicyDetailedModel;

public interface INSBPolicyManageDao extends BaseDao<INSBPolicyitem>{

	public long queryPagingListCount(Map<String, Object> map);
	public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);
	//根据代理人工号查询代理人信息
	public INSBAgent selectAgentByNum(String agentNum);
	public INSBPerson selectPolicyHolderInfo(String taskid);
	public INSBPerson selectInsuredInfo(String taskid);
	public INSBCarinfo selectCarInfoByTaskId(String taskid);
	public INSBCarinfohis selectCarInfoByhis(String taskid,String proid);
	public List<PolicyDetailedModel> queryImgInfo(String id);
	public INSBPolicyitem selectPolcyByOIdAndType(Map<String, Object> map);

	public INSBPolicyitem selectPolcyByOIdAndType2(Map<String, Object> map);
	public INSBPolicyitem selectPolcyByOIdAndType2his(Map<String, String> maphis);
	public INSBPolicyitem selectPolcyBypId(String poid);

	public INSBPolicyitem selectRecognizeeByorderid(Map<String, Object> map);
	public INSBPolicyitem selectRecognizeeByorderidhis(Map<String, String> maphis);
	public INSBPolicyitem selectRecognizeeBypoid(String poid);

	public INSBPolicyitem selectInsureByorderid(Map<String, Object> map);
	public INSBPolicyitem selectInsureByorderidhis(Map<String, String> maphis);
	public INSBPolicyitem selectInsureBypoid(String poid);

	public INSBPolicyitem selectContactsByTask(Map<String, String> maphis);
	public INSBPolicyitem selectContactsByOrderid(Map<String, Object> map);
	public INSBPolicyitem selectContactsBypoid(String poid);
}
