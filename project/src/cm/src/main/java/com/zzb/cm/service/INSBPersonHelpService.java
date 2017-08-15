package com.zzb.cm.service;

import java.util.Date;
import java.util.List;

import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBLegalrightclaim;
import com.zzb.cm.entity.INSBLegalrightclaimhis;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBRelationperson;
import com.zzb.cm.entity.INSBRelationpersonhis;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.mobile.model.lastinsured.LastYearPersonBean;

import net.sf.json.JSONObject;

public interface INSBPersonHelpService {

	/**
	 * 新增车主信息 
	 * @param insbCarowneinfo
	 * @param modifytime 更新时间  值=null 不填
	 * @param operator 操作人
	 * @param taskId 工作流号
	 * @param personId  insbperson表id
	 * @return
	 */
	public INSBCarowneinfo addINSBCarowneinfo(INSBCarowneinfo insbCarowneinfo,Date modifytime,String operator,String taskId,String personId);
	
	/**
	 * 新增投保人
	 * @param insbApplicant
	 * @param modifytime 更新时间  值=null 不填
	 * @param operator 操作人
	 * @param taskId 工作流号
	 * @param personId  insbperson表id
	 * @return
	 */
	public INSBApplicant addINSBApplicant(INSBApplicant insbApplicant,Date modifytime,String operator,String taskId,String personId);

	/**
	 * 新增投保人历史
	 * @param insbApplicanthis
	 * @param modifytime 更新时间  值=null 不填
	 * @param operator 操作人
	 * @param taskId 工作流号
	 * @param inscomcode 供应商id
	 * @param personId  insbperson表id
	 * @return
	 */
	public INSBApplicanthis addINSBApplicanthis(INSBApplicanthis insbApplicanthis,Date modifytime,
			String operator,String taskId,String inscomcode,String personId);
	/**
	 * 新增被保人
	 * @param insbInsured
	 * @param modifytime 更新时间  值=null 不填
	 * @param operator 操作人
	 * @param taskId 工作流号
	 * @param personId  insbperson表id
	 * @return
	 */
	public INSBInsured addINSBInsured(INSBInsured insbInsured,Date modifytime,String operator,String taskId,String personId);

	/**
	 * 新增被保人历史
	 * @param insbInsuredhis
	 * @param modifytime 更新时间  值=null 不填
	 * @param operator 操作人
	 * @param taskId 工作流号
	 * @param inscomcode 供应商id
	 * @param personId  insbperson表id
	 * @return
	 */
	public INSBInsuredhis addINSBInsuredhis(INSBInsuredhis insbInsuredhis,Date modifytime,
			String operator,String taskId,String inscomcode,String personId);
	
	/**
	 * 新增权益人
	 * @param insbLegalrightclaim
	 * @param modifytime 更新时间  值=null 不填
	 * @param operator 操作人
	 * @param taskId 工作流号
	 * @param personId  insbperson表id
	 * @return
	 */
	public INSBLegalrightclaim addINSBLegalrightclaim(INSBLegalrightclaim insbLegalrightclaim,Date modifytime,String operator,
			String taskId,String personId);

	/**
	 * 新增权益人历史
	 * @param insbLegalrightclaimhis
	 * @param modifytime 更新时间  值=null 不填
	 * @param operator 操作人
	 * @param taskId 工作流号
	 * @param inscomcode 供应商id
	 * @param personId  insbperson表id
	 * @return
	 */
	public INSBLegalrightclaimhis addINSBLegalrightclaimhis(INSBLegalrightclaimhis insbLegalrightclaimhis,Date modifytime,
			String operator,String taskId,String inscomcode,String personId);
	
	/**
	 * 新增关系人
	 * @param insbRelationperson
	 * @param modifytime 更新时间  值=null 不填
	 * @param operator 操作人
	 * @param taskId 工作流号
	 * @param personId  insbperson表id
	 * @return
	 */
	public INSBRelationperson addINSBRelationperson(INSBRelationperson insbRelationperson,Date modifytime,String operator,
			String taskId,String personId);

	/**
	 * 新增关系人历史
	 * @param insbRelationpersonhis
	 * @param modifytime 更新时间  值=null 不填
	 * @param operator 操作人
	 * @param taskId 工作流号
	 * @param inscomcode 供应商id
	 * @param personId  insbperson表id
	 * @return
	 */
	public INSBRelationpersonhis addINSBRelationpersonhis(INSBRelationpersonhis insbRelationpersonhis,Date modifytime,
			String operator,String taskId,String inscomcode,String personId);
	/**
	 * 新增车辆信息
	 * @param insbCarinfo
	 * @param modifytime 更新时间  值=null 不填
	 * @param operator 操作人
	 * @param taskId 工作流号
	 * @param personId  insbperson表id
	 * @return
	 */
	public INSBCarinfo addINSBCarinfo(INSBCarinfo insbCarinfo,Date modifytime,String operator,
			String taskId,String personId);

	/**
	 * 新增车辆信息历史
	 * @param insbCarinfohis
	 * @param modifytime 更新时间  值=null 不填
	 * @param operator 操作人
	 * @param taskId 工作流号
	 * @param inscomcode 供应商id
	 * @param personId  insbperson表id
	 * @return
	 */
	public INSBCarinfohis addINSBCarinfohis(INSBCarinfohis insbCarinfohis,Date modifytime,
			String operator,String taskId,String inscomcode,String personId);

	/**
	 * 操作关系人表
	 * @param taskId 工作流程号
	 * @param insurancecompanyid 供应商id
	 * @param JSONObject
	 */
	public void operINSBRelationpersonhis(String taskId,String insurancecompanyid,JSONObject jsonObject);
	
	/**
	 * 获取新增或更新insbperson表对象
	 * @param status  新增或更新
	 * @param jsonObject json数据
	 * @param taskId  工作流程号
	 * @param insuranceCompanyId 供应商id
	 * @param oldPersonId 历史insbperson表id
	 * @param classType 类名用于拼接redis中的key
	 * @param property 属性
	 * @param setDefaultValue 是否操作address属性，删除相关redis记录
	 * @param insbPerson 
	 * @return
	 */
	public INSBPerson getINSBPersonObj(String status,JSONObject jsonObject,String taskId,String insuranceCompanyId,
			String oldPersonId,String classType, String property,boolean setDefaultValue,INSBPerson insbPerson);
	
	/**
	 * 根据人员信息，新增
	 * @param newPerson 新增对象
	 * @param oldPerson 原对象
	 * @param operator 操作人
	 * @return
	 */
	public INSBPerson repairPerson(INSBPerson newPerson,INSBPerson oldPerson,String operator);
	
	/***
	 * 根据传入人员信息，添加新的人员信息
	 * @param person 人员信息对象
	 * @param operator 操作人
	 * @return
	 */
	public INSBPerson addINSBPerson(INSBPerson person,String operator);
	
	/***
	 * 根据传入人员信息，更新人员信息
	 * @param updatePerson 更新对象
	 * @param oldPerson 原对象
	 * @param operator 操作人
	 * @param modifytime 更新时间
	 * @return updatePerson
	 */
	public INSBPerson updateINSBPerson(INSBPerson updatePerson,INSBPerson oldPerson,String operator,Date modifytime);
	
	/**
	 * 相关人员信息为null 业务操作步骤如：例如，insbInsured 被保人信息为null ①：添加insperson人员信息；②：添加insbInsured被保人信息
	 * @param person 人员信息对象
	 * @param operator 操作人
	 * @param status 操作状态  1:投保人，2：被保人，3：权益人，4：关系人
	 */
	public INSBPerson addPersonIsNull(INSBPerson person,String operator,int status);
	/**
	 * 相关历史人员信息为null 业务操作步骤如：例如，INSBInsuredhis 被保人信息为null 
	 * ①：根据personid查出对应INSBInsured表记录并查出相应的insbperson记录；②：根据之前查出insbperson记录复制并插入新的InsbPerson表记录；③：插入对应INSBInsuredhis表记录；
	 * ④：INSBPolicyitem，如果有关联id，则更新相关id。如：INSBPolicyitem的Insuredid更新为INSBInsuredhis表的id
	 * @param object 
	 * @param taskId 工作流程号
	 * @param inscomcode 供应商id
	 * @param status 操作状态  1:投保人，2：被保人，3：权益人，4：关系人
	 */
	public INSBPerson addPersonHisIsNull(Object object,String taskId,String inscomcode,int status);
	
	/**
	 * 根据大数据平台返回的insbperson人员信息数据，添加人员信息
	 * @param insbPerson
	 * @param lastYearPersonBean
	 * @param operator  操作人
	 * @param taskid 工作流程号
	 * @param peopleIdcardType  平台查询，证件信息映射
	 * 01 == 0 居民身份证; 02 == 1居民户口簿;03 == 2驾驶证;04 == 3军官证;05 == 3士兵证;
	   07 == 4中国护照;09 == 5港澳通行证;10 == 5台湾通行证;66 == 8 社会信用代码证;
	   71 == 6组织机构代码证;99 == 7	其他证件
	 * @param peopleIdcardTypeInteger 平台查询，证件信息映射int
	 * @return
	 */
	public INSBPerson addLastYearPersonBean(INSBPerson insbPerson,LastYearPersonBean lastYearPersonBean,String operator,String taskid
			,String peopleIdcardType,Integer peopleIdcardTypeInteger);
	
	/**
	 * 获取保单表信息
	 * @param taskid 工作流程号
	 * @param inscomcode 供应商id
	 * @return
	 */
	public List<INSBPolicyitem> getINSBPolicyItemList(String taskid,String inscomcode);
	/**
	 * 更新被保人保单表信息
	 * @param taskid
	 * @param insuredId
	 * @param name
	 */
	public void updateInsbPolicyitemInsured(String taskid,String insuredId,String name);
	/**
	 * 更新投保人保单表信息
	 * @param taskid
	 * @param applicantId
	 * @param name
	 */
	public void updateInsbPolicyitemApplicant(String taskid,String applicantId,String name);

}