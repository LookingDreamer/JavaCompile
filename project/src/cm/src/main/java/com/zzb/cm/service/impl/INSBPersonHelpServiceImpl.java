package com.zzb.cm.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.common.ConstUtil;
import com.common.ModelUtil;
import com.zzb.cm.Interface.service.InterFaceDefaultValueUtil;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBLegalrightclaimDao;
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
import com.zzb.cm.service.INSBApplicantService;
import com.zzb.cm.service.INSBApplicanthisService;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBCarinfohisService;
import com.zzb.cm.service.INSBCarmodelinfoService;
import com.zzb.cm.service.INSBCarmodelinfohisService;
import com.zzb.cm.service.INSBCarowneinfoService;
import com.zzb.cm.service.INSBInsuredService;
import com.zzb.cm.service.INSBInsuredhisService;
import com.zzb.cm.service.INSBLegalrightclaimService;
import com.zzb.cm.service.INSBLegalrightclaimhisService;
import com.zzb.cm.service.INSBPersonHelpService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.cm.service.INSBRelationpersonService;
import com.zzb.cm.service.INSBRelationpersonhisService;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.mobile.model.lastinsured.LastYearPersonBean;

import net.sf.json.JSONObject;

/**
 * 
 * 车险相关人员共用操作类，包括：人员表，投保人，被保人，关系人，权益人
 *
 */
@Service
public class INSBPersonHelpServiceImpl implements INSBPersonHelpService {
	@Resource
	private INSBCarinfoService insbCarinfoService;// 车辆信息原表
	@Resource
	private INSBCarinfohisService insbCarinfohisService;// 车辆信息历史表
	@Resource
	private INSBCarmodelinfoService insbCarmodelinfoService;// 车型信息原表
	@Resource
	private INSBCarmodelinfohisService insbCarmodelinfohisService;// 车型信息历史表
	@Resource
	private INSBPersonService insbPersonService;// 人员信息表
	@Resource
	private INSBCarowneinfoService insbCarowneinfoService;// 车主信息表
	@Resource
	private INSBApplicantService insbApplicantService;// 投保人原表
	@Resource
	private INSBApplicanthisService insbApplicanthisService;// 投保人历史表
	@Resource
	private INSBInsuredService insbInsuredService;// 被保人原表
	@Resource
	private INSBInsuredhisService insbInsuredhisService;// 被保人历史表
	@Resource
	private INSBLegalrightclaimService insbLegalrightclaimService;// 受益人原表
	@Resource
	private INSBLegalrightclaimhisService insbLegalrightclaimhisService;// 受益人历史表
	@Resource
	private INSBRelationpersonService insbRelationpersonService;//关系人
	@Resource
	private INSBRelationpersonhisService insbRelationpersonhisService;//关系人历史
	@Resource
	private INSBPolicyitemService insbPolicyitemService;// 保单信息表
	@Resource
	private INSBInsuredDao insbInsrueDao;
	@Resource
	private INSBApplicantDao insbApplicantDao;
	@Resource
	private INSBLegalrightclaimDao insbLegalrightclaimDao;

	@Override
	public INSBCarowneinfo addINSBCarowneinfo(INSBCarowneinfo insbCarowneinfo, Date modifytime, String operator,
			String taskId, String personId) {
		insbCarowneinfo.setId(UUIDUtils.create());
		insbCarowneinfo.setCreatetime(new Date());
		if(modifytime!=null)
			insbCarowneinfo.setModifytime(modifytime);
		insbCarowneinfo.setOperator(operator);
		insbCarowneinfo.setTaskid(taskId);
		insbCarowneinfo.setPersonid(personId);
		insbCarowneinfoService.insert(insbCarowneinfo);
		return insbCarowneinfo;
	}
	
	
	@Override
	public INSBPerson getINSBPersonObj(String status,JSONObject jsonObject,String taskId,String insuranceCompanyId,
			String oldPersonId,String classType, String property,boolean setDefaultValue,INSBPerson insbPerson){
		//获取历史insbperson信息
		INSBPerson oldPerson=null;
		if(!StringUtil.isEmpty(oldPersonId)){
			oldPerson=insbPersonService.queryById(oldPersonId);
		}
		if(status.equals(ConstUtil.OPER_ADD)){
			insbPerson.setId(UUIDUtils.create());
			insbPerson.setTaskid(taskId);
			insbPerson.setCreatetime(new Date());
		}else{
			insbPerson.setModifytime(new Date());
		}
		insbPerson.setOperator(ConstUtil.OPER_ADMIN);
		if (!StringUtil.isEmpty(jsonObject.get("name"))){
			insbPerson.setName(jsonObject.get("name").toString());// 姓名
		}else{
			if(!StringUtil.isEmpty(oldPerson))
				insbPerson.setName(oldPerson.getName());//回写没有值，获取历史数据
		}
		if (!StringUtil.isEmpty(jsonObject.get("enName"))){
			insbPerson.setEname(jsonObject.get("enName").toString());// 英文名
		}else{
			if(!StringUtil.isEmpty(oldPerson))
				insbPerson.setEname(oldPerson.getEname());
		}
		if (!StringUtil.isEmpty(jsonObject.get("sex"))) {
			insbPerson.setGender(Integer.valueOf(jsonObject.get("sex").toString()));// 性别
		} else {
			insbPerson.setGender(0);// 性别
		}
		if (!StringUtil.isEmpty(jsonObject.get("address"))) {
			insbPerson.setAddress(jsonObject.get("address").toString());// 居住地址
		}else{
			if(!StringUtil.isEmpty(oldPerson))
				insbPerson.setAddress(oldPerson.getAddress());
		}
		if (!StringUtil.isEmpty(jsonObject.get("postCode"))){
			insbPerson.setZip(jsonObject.get("postCode").toString());// 邮编
		}else{
			if(!StringUtil.isEmpty(oldPerson))
				insbPerson.setZip(oldPerson.getZip());
		}
		if (!StringUtil.isEmpty(jsonObject.get("birthday"))){
			insbPerson.setBirthday(DateUtil.parse(jsonObject.get("birthday").toString(), "yyyy-MM-dd"));// 出生日期
		}else{
			if(!StringUtil.isEmpty(oldPerson))
				insbPerson.setBirthday(oldPerson.getBirthday());
		}
		if (!StringUtil.isEmpty(jsonObject.get("idCardType"))) {
			insbPerson.setIdcardtype(Integer.valueOf(jsonObject.get("idCardType").toString()));// 证件类型
		}else{
			if(!StringUtil.isEmpty(oldPerson))
				insbPerson.setIdcardtype(oldPerson.getIdcardtype());
		}
		if (!StringUtil.isEmpty(jsonObject.get("idCard"))) {
			insbPerson.setIdcardno(jsonObject.get("idCard").toString());// 证件号
		}else{
			if(!StringUtil.isEmpty(oldPerson))
				insbPerson.setIdcardno(oldPerson.getIdcardno());
		}
		if (!StringUtil.isEmpty(jsonObject.get("mobile"))){
			insbPerson.setCellphone(jsonObject.get("mobile").toString());// 手机
		}else{
			if(!StringUtil.isEmpty(oldPerson))
				insbPerson.setCellphone(oldPerson.getCellphone());
		}
		if (!StringUtil.isEmpty(jsonObject.get("email"))){
			insbPerson.setEmail(jsonObject.get("email").toString());// 邮箱
		}else{
			if(!StringUtil.isEmpty(oldPerson))
				insbPerson.setEmail(oldPerson.getEmail());
		}	
		return  insbPerson;
	}

	@Override
	public void operINSBRelationpersonhis(String taskId,String insurancecompanyid,JSONObject jsonObject){
		INSBRelationpersonhis queryInsbRelationpersonhis = new INSBRelationpersonhis();
		queryInsbRelationpersonhis.setTaskid(taskId);
		queryInsbRelationpersonhis.setInscomcode(insurancecompanyid);
		queryInsbRelationpersonhis = insbRelationpersonhisService.queryOne(queryInsbRelationpersonhis);
		INSBPerson insbPerson = new INSBPerson();
		//insbPerson.setNoti("operINSBRelationpersonhis-test");
		//有关系人数据更新否则新增
		if (StringUtil.isEmpty(queryInsbRelationpersonhis)) {
			insbPerson=getINSBPersonObj(ConstUtil.OPER_ADD, jsonObject, taskId, insurancecompanyid,
					null,null, null,false,insbPerson);
			//insbPerson.setNoti("关系人1-"+insurancecompanyid);
			insbPersonService.insert(insbPerson);
			queryInsbRelationpersonhis = new INSBRelationpersonhis();
			queryInsbRelationpersonhis.setId(UUIDUtils.create());
			queryInsbRelationpersonhis.setCreatetime(new Date());
			queryInsbRelationpersonhis.setModifytime(new Date());
			queryInsbRelationpersonhis.setOperator("admin");
			queryInsbRelationpersonhis.setTaskid(taskId);
			queryInsbRelationpersonhis.setPersonid(insbPerson.getId());
			queryInsbRelationpersonhis.setInscomcode(insurancecompanyid);
			insbRelationpersonhisService.insert(queryInsbRelationpersonhis);
		} else {
			// 查询是否有关联insbPerson信息,①：没有添加，②：有更新
			insbPerson=insbPersonService.queryById(queryInsbRelationpersonhis.getPersonid());
			if(insbPerson==null){
				insbPerson = new INSBPerson();
				insbPerson=getINSBPersonObj(ConstUtil.OPER_ADD, jsonObject, taskId, insurancecompanyid,
						queryInsbRelationpersonhis.getPersonid(),null, null,false,insbPerson);
				//insbPerson.setNoti("关系人2-"+insurancecompanyid);
				insbPersonService.insert(insbPerson);
			}else{
				insbPerson=getINSBPersonObj(ConstUtil.OPER_UPDATE, jsonObject, taskId, insurancecompanyid,
						queryInsbRelationpersonhis.getPersonid(),null, null,false,insbPerson);
				//insbPerson.setNoti("关系人3-"+insurancecompanyid);
				insbPersonService.updateById(insbPerson);
			}
			queryInsbRelationpersonhis.setPersonid(insbPerson.getId());
			insbRelationpersonhisService.updateById(queryInsbRelationpersonhis);
		}
	}


	@Override
	public INSBApplicant addINSBApplicant(INSBApplicant insbApplicant, Date modifytime, String operator, String taskId,
			String personId) {
		insbApplicant.setId(UUIDUtils.create());
		insbApplicant.setTaskid(taskId);
		insbApplicant.setPersonid(personId);
		insbApplicant.setOperator(operator);
		insbApplicant.setCreatetime(new Date());
		if(modifytime!=null)
			insbApplicant.setModifytime(modifytime);
		insbApplicantService.insert(insbApplicant);
		return insbApplicant;
	}


	@Override
	public INSBApplicanthis addINSBApplicanthis(INSBApplicanthis insbApplicanthis, Date modifytime, String operator,
			String taskId, String inscomcode, String personId) {
		insbApplicanthis.setId(UUIDUtils.create());
		insbApplicanthis.setTaskid(taskId);
		insbApplicanthis.setInscomcode(inscomcode);
		insbApplicanthis.setCreatetime(new Date());
		if(modifytime!=null)
			insbApplicanthis.setModifytime(modifytime);
		insbApplicanthis.setOperator(operator);
		insbApplicanthis.setPersonid(personId);
		insbApplicanthisService.insert(insbApplicanthis);
		return insbApplicanthis;
	}


	@Override
	public INSBInsured addINSBInsured(INSBInsured insbInsured, Date modifytime, String operator, String taskId,
			String personId) {
		insbInsured.setId(UUIDUtils.create());
		insbInsured.setTaskid(taskId);
		insbInsured.setPersonid(personId);
		insbInsured.setOperator(operator);
		insbInsured.setCreatetime(new Date());
		if(modifytime!=null)
			insbInsured.setModifytime(modifytime);
		insbInsuredService.insert(insbInsured);
		return insbInsured;
	}


	@Override
	public INSBInsuredhis addINSBInsuredhis(INSBInsuredhis insbInsuredhis, Date modifytime, String operator,
			String taskId, String inscomcode, String personId) {
		insbInsuredhis.setId(UUIDUtils.create());
		insbInsuredhis.setTaskid(taskId);
		insbInsuredhis.setInscomcode(inscomcode);
		insbInsuredhis.setCreatetime(new Date());
		if(modifytime!=null)
			insbInsuredhis.setModifytime(modifytime);
		insbInsuredhis.setOperator(operator);
		insbInsuredhis.setPersonid(personId);
		insbInsuredhisService.insert(insbInsuredhis);
		return insbInsuredhis;
	}


	@Override
	public INSBLegalrightclaim addINSBLegalrightclaim(INSBLegalrightclaim insbLegalrightclaim, Date modifytime,
			String operator, String taskId, String personId) {
		insbLegalrightclaim.setId(UUIDUtils.create());
		insbLegalrightclaim.setTaskid(taskId);
		insbLegalrightclaim.setPersonid(personId);
		insbLegalrightclaim.setOperator(operator);
		insbLegalrightclaim.setCreatetime(new Date());
		if(modifytime!=null)
			insbLegalrightclaim.setModifytime(modifytime);
		insbLegalrightclaimService.insert(insbLegalrightclaim);
		return insbLegalrightclaim;
	}


	@Override
	public INSBLegalrightclaimhis addINSBLegalrightclaimhis(INSBLegalrightclaimhis insbLegalrightclaimhis,
			Date modifytime, String operator, String taskId, String inscomcode, String personId) {
		insbLegalrightclaimhis.setId(UUIDUtils.create());
		insbLegalrightclaimhis.setCreatetime(new Date());
		if(modifytime!=null)
			insbLegalrightclaimhis.setModifytime(modifytime);
		insbLegalrightclaimhis.setOperator(operator);
		insbLegalrightclaimhis.setTaskid(taskId);
		insbLegalrightclaimhis.setInscomcode(inscomcode);
		insbLegalrightclaimhis.setPersonid(personId);
		insbLegalrightclaimhisService.insert(insbLegalrightclaimhis);
		return insbLegalrightclaimhis;
	}


	@Override
	public INSBRelationperson addINSBRelationperson(INSBRelationperson insbRelationperson, Date modifytime,
			String operator, String taskId, String personId) {
		insbRelationperson.setCreatetime(new Date());
		insbRelationperson.setOperator(operator);
		insbRelationperson.setPersonid(personId);
		if(modifytime!=null)
			insbRelationperson.setModifytime(new Date());
		insbRelationperson.setTaskid(taskId);
		insbRelationpersonService.insert(insbRelationperson);			
		return insbRelationperson;
	}


	@Override
	public INSBRelationpersonhis addINSBRelationpersonhis(INSBRelationpersonhis insbRelationpersonhis, Date modifytime,
			String operator, String taskId, String inscomcode, String personId) {
		insbRelationpersonhis.setId(UUIDUtils.create());
        insbRelationpersonhis.setCreatetime(new Date());
        if(modifytime!=null)
        	insbRelationpersonhis.setModifytime(modifytime);
        insbRelationpersonhis.setPersonid(personId);
        insbRelationpersonhis.setInscomcode(inscomcode);
        insbRelationpersonhis.setTaskid(taskId);
        insbRelationpersonhis.setOperator(operator);
        insbRelationpersonhisService.insert(insbRelationpersonhis);
		return insbRelationpersonhis;
	}


	@Override
	public INSBCarinfo addINSBCarinfo(INSBCarinfo insbCarinfo, Date modifytime, String operator, String taskId,
			String personId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public INSBCarinfohis addINSBCarinfohis(INSBCarinfohis insbCarinfohis, Date modifytime, String operator,
			String taskId, String inscomcode, String personId) {
		insbCarinfohis.setId(UUIDUtils.create());
		insbCarinfohis.setCreatetime(new Date());
		if(modifytime!=null)
			insbCarinfohis.setModifytime(modifytime);
		insbCarinfohis.setOwner(personId);// 关联新增insbperson表id
		insbCarinfohis.setInscomcode(inscomcode);
		insbCarinfohis.setOperator(operator);
		insbCarinfohis.setTaskid(taskId);
		insbCarinfohisService.insert(insbCarinfohis);
		return insbCarinfohis;
	}
	@Override
	public INSBPerson repairPerson(INSBPerson newPerson,INSBPerson oldPerson,String operator){
		
		newPerson.setOperator(operator);
		newPerson.setCreatetime(new Date());
		newPerson.setTaskid(oldPerson.getTaskid());
		newPerson.setName(oldPerson.getName());
		newPerson.setGender(oldPerson.getGender());
		newPerson.setIdcardtype(oldPerson.getIdcardtype());//证件类型
		newPerson.setIdcardno(oldPerson.getIdcardno());//证件号码
		newPerson.setCellphone(oldPerson.getCellphone());
		newPerson.setEmail(oldPerson.getEmail());
		return newPerson;
	}


	@Override
	public INSBPerson addINSBPerson(INSBPerson person,String operator) {
		INSBPerson insbPerson = new INSBPerson();
		try {
		    PropertyUtils.copyProperties(insbPerson, person);
		    insbPerson.setId(UUIDUtils.create());
		    insbPersonService.insert(insbPerson);
		} catch (Exception e) {
		    LogUtil.error("taskid=" + person.getTaskid() + "创建insbPerson信息错误: " + e.getMessage());
		}
		return insbPerson;
	}


	@Override
	public INSBPerson addPersonIsNull(INSBPerson person, String operator,int status) {
		INSBPerson insbPerson=addINSBPerson(person,operator);
		switch (status) {
			case ConstUtil.STATUS_1:
				INSBApplicant insbApplicant = new INSBApplicant();
				addINSBApplicant(insbApplicant, null, operator, insbPerson.getTaskid(),insbPerson.getId());			
				break;
			case ConstUtil.STATUS_2:
				INSBInsured insbInsured = new INSBInsured();
				addINSBInsured(insbInsured, null, operator, insbPerson.getTaskid(), insbPerson.getId());
				break;
			case ConstUtil.STATUS_3:
				INSBLegalrightclaim insbLegalrightclaim = new INSBLegalrightclaim();
				addINSBLegalrightclaim(insbLegalrightclaim, null, operator, insbPerson.getTaskid(), insbPerson.getId());
				break;
			case ConstUtil.STATUS_4:
				INSBRelationperson insbRelationperson = new INSBRelationperson();	
				addINSBRelationperson(insbRelationperson, null, operator, insbPerson.getTaskid(),insbPerson.getId());
				break;
			}
       	return insbPerson;
	}
	@Override
	public INSBPerson addPersonHisIsNull(Object object,String taskid,String inscomcode, int status) {
		INSBPerson oldPerson = new INSBPerson();
		INSBPerson newPerson = new INSBPerson();
		List<INSBPolicyitem> insbPolicyitems;
		switch (status) {
			case ConstUtil.STATUS_1:
				INSBApplicant insbApplicant = (INSBApplicant)object;
                oldPerson=insbPersonService.queryById(insbApplicant.getPersonid());
                newPerson=repairPerson(newPerson,oldPerson,insbApplicant.getOperator());
        		newPerson.setId(UUIDUtils.create());
                insbPersonService.insert(newPerson);
                //插入新的数据
                INSBApplicanthis applicanthis = new INSBApplicanthis();
                try {
                	PropertyUtils.copyProperties(applicanthis, insbApplicant);
                } catch (Exception e) {
					e.printStackTrace();
					LogUtil.info("复制INSBApplicanthis表失败了" + taskid + ": " + e.getMessage());
				} 
                applicanthis=addINSBApplicanthis(applicanthis, new Date(),
                		insbApplicant.getOperator(), taskid, inscomcode, newPerson.getId());
                //更新保单表INSBPolicyitem表的Applicantid字段，为INSBApplicanthis表id
                insbPolicyitems =getINSBPolicyItemList(taskid,inscomcode);
                for (INSBPolicyitem ddInsbPolicyitem : insbPolicyitems) {
					ddInsbPolicyitem.setApplicantid(applicanthis.getId());
					insbPolicyitemService.updateById(ddInsbPolicyitem);
				}
				break;
			case ConstUtil.STATUS_2:
				//被保人
				INSBInsured insbInsured = (INSBInsured)object;
				oldPerson=insbPersonService.queryById(insbInsured.getPersonid());
		        newPerson=repairPerson(newPerson,oldPerson,insbInsured.getOperator());
				newPerson.setId(UUIDUtils.create());
		        insbPersonService.insert(newPerson);
	            //插入新的数据
				INSBInsuredhis insuredhis = new INSBInsuredhis();
				try {
					PropertyUtils.copyProperties(insuredhis, insbInsured);
				} catch (Exception e) {
					e.printStackTrace();
					LogUtil.info("复制INSBInsuredhis表失败了" + taskid + ": " + e.getMessage());
				} 
	            insuredhis=addINSBInsuredhis(insuredhis, new Date(),oldPerson.getOperator(), taskid, inscomcode, newPerson.getId());
	            //更新保单表INSBPolicyitem表的insuredid字段，为INSBInsuredhis表id
	            insbPolicyitems =getINSBPolicyItemList(taskid,inscomcode);
	            for (INSBPolicyitem ddInsbPolicyitem : insbPolicyitems) {
					ddInsbPolicyitem.setInsuredid(insuredhis.getId());
					insbPolicyitemService.updateById(ddInsbPolicyitem);
				}
				break;
			case ConstUtil.STATUS_3:
				INSBLegalrightclaim insbLegalrightclaim = (INSBLegalrightclaim)object;
                oldPerson=insbPersonService.queryById(insbLegalrightclaim.getPersonid());
                newPerson=repairPerson(newPerson,oldPerson,insbLegalrightclaim.getOperator());
        		newPerson.setId(UUIDUtils.create());
                insbPersonService.insert(newPerson);
                //插入新的数据
                INSBLegalrightclaimhis legalrightclaimhis = new INSBLegalrightclaimhis();
                try {
                	PropertyUtils.copyProperties(legalrightclaimhis, insbLegalrightclaim);
                } catch (Exception e) {
					e.printStackTrace();
					LogUtil.info("复制INSBLegalrightclaimhis表失败了" + taskid + ": " + e.getMessage());
				} 
                addINSBLegalrightclaimhis(legalrightclaimhis, new Date(),
                		insbLegalrightclaim.getOperator(), taskid, inscomcode, newPerson.getId());
				break;
			case ConstUtil.STATUS_4:
				  INSBRelationperson insbRelationperson =(INSBRelationperson)object;
				  oldPerson=insbPersonService.queryById(insbRelationperson.getPersonid());
                  newPerson=repairPerson(newPerson,oldPerson,insbRelationperson.getOperator());
          		  newPerson.setId(UUIDUtils.create());
                  insbPersonService.insert(newPerson);
                  //插入新的数据
                  INSBRelationpersonhis relationpersonhis = new INSBRelationpersonhis();
                  try {
                	  PropertyUtils.copyProperties(relationpersonhis, insbRelationperson);
                  } catch (Exception e) {
  					  e.printStackTrace();
  					  LogUtil.info("复制INSBRelationpersonhis表失败了" + taskid + ": " + e.getMessage());
  				  } 
                  addINSBRelationpersonhis(relationpersonhis, new Date(), 
                  		relationpersonhis.getOperator(), taskid, inscomcode, newPerson.getId());
				break;
			}
		return newPerson;
		
	}

	@Override
	public INSBPerson addLastYearPersonBean(INSBPerson insbPerson, LastYearPersonBean lastYearPersonBean,String operator,
			String taskid,String peopleIdcardType,Integer peopleIdcardTypeInteger) {
		insbPerson.setCreatetime(new Date());
		insbPerson.setOperator(operator);
		insbPerson.setTaskid(taskid);
		if("0".equals(peopleIdcardType) && lastYearPersonBean.getIdno().length() == 18){
			insbPerson.setGender(ModelUtil.getGenderByIdCard(lastYearPersonBean.getIdno()));
		}else{
			// 1-女,0-男
			insbPerson.setGender(1);
		}
		insbPerson.setName(lastYearPersonBean.getName());
		insbPerson.setEmail(lastYearPersonBean.getEmail());
		insbPerson.setIdcardtype(peopleIdcardTypeInteger);
		insbPerson.setIdcardno(lastYearPersonBean.getIdno());
		insbPerson.setCellphone(lastYearPersonBean.getMobile());
		insbPerson.setAddress(lastYearPersonBean.getAddrss());
		insbPersonService.insert(insbPerson);
		return insbPerson;
	}
	
	public List<INSBPolicyitem> getINSBPolicyItemList(String taskid,String inscomcode){
		INSBPolicyitem queryInsbPolicyitem = new INSBPolicyitem();
		queryInsbPolicyitem.setTaskid(taskid);
		queryInsbPolicyitem.setInscomcode(inscomcode);
		return insbPolicyitemService.queryList(queryInsbPolicyitem);
	}


	@Override
	public INSBPerson updateINSBPerson(INSBPerson updatePerson, INSBPerson oldPerson, String operator,Date modifytime) {
		updatePerson.setName(oldPerson.getName());
		updatePerson.setGender(oldPerson.getGender());
		updatePerson.setOperator(operator);
		updatePerson.setModifytime(modifytime);
		updatePerson.setIdcardno(oldPerson.getIdcardno());
		updatePerson.setCellphone(oldPerson.getCellphone());
		updatePerson.setEmail(oldPerson.getEmail());
		updatePerson.setIdcardtype(oldPerson.getIdcardtype());
		insbPersonService.updateById(updatePerson);
		return updatePerson;
	}
	@Override
	public void updateInsbPolicyitemInsured(String taskid,String insuredId,String name){
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(taskid);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemService.queryList(policyitem);
		for(INSBPolicyitem insbPolicyitem : insbPolicyitems){
			insbPolicyitem.setInsuredid(insuredId);
			insbPolicyitem.setInsuredname(name);
			insbPolicyitemService.updateById(insbPolicyitem);
		}
	}
	@Override
	public void updateInsbPolicyitemApplicant(String taskid,String applicantId,String name){
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(taskid);
		List<INSBPolicyitem> insbPolicyitems = insbPolicyitemService.queryList(policyitem);
		for(INSBPolicyitem insbPolicyitem : insbPolicyitems){
			insbPolicyitem.setApplicantid(applicantId);
			insbPolicyitem.setApplicantname(name);
			insbPolicyitemService.updateById(insbPolicyitem);
		}
	}


}
