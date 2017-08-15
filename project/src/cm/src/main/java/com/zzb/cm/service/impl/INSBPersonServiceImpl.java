package com.zzb.cm.service.impl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.service.INSCCodeService;
import com.common.ConstUtil;
import com.zzb.cm.controller.vo.INSBRelationPersonVO;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.dao.INSBApplicanthisDao;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBCarowneinfoDao;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBLegalrightclaimDao;
import com.zzb.cm.dao.INSBLegalrightclaimhisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBRelationpersonDao;
import com.zzb.cm.dao.INSBRelationpersonhisDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBLegalrightclaim;
import com.zzb.cm.entity.INSBLegalrightclaimhis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBRelationperson;
import com.zzb.cm.entity.INSBRelationpersonhis;
import com.zzb.cm.service.INSBPersonHelpService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.mobile.model.CommonModel;

@Service
@Transactional
public class INSBPersonServiceImpl extends BaseServiceImpl<INSBPerson>
		implements INSBPersonService {
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBInsuredDao insbInsuredDao;
	@Resource
	private INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	private INSBApplicantDao insbApplicantDao;
	@Resource
	private INSBApplicanthisDao insbApplicanthisDao;
	@Resource
	private INSBLegalrightclaimDao insbLegalrightclaimDao;
	@Resource
	private INSBLegalrightclaimhisDao insbLegalrightclaimhisDao;
	@Resource
	private INSBRelationpersonDao insbRelationpersonDao;
	@Resource
	private INSBRelationpersonhisDao insbRelationpersonhisDao;
	@Resource
	private INSBCarowneinfoDao insbCarowneinfoDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBCarinfohisDao insbCarinfohisDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBPersonHelpService insbPersonHelpService;
	@Resource
	private INSBPolicyitemService insbPolicyitemService;// 保单信息表
	@Override
	protected BaseDao<INSBPerson> getBaseDao() {
		return insbPersonDao;
	}
	/**
	 * 通过任务id和调用用途选择性查询车辆任务中关系人信息
	 */
	@Override
	public Map<String, Object> getCarTaskRelationPersonInfo(String taskId, String inscomcode, String opeType){
		INSBPerson applicant = new INSBPerson();
		INSBPerson insured = new INSBPerson();
		INSBPerson personForRight = new INSBPerson();
		INSBPerson linkPerson = new INSBPerson();
		INSBPerson carowner = new INSBPerson();
		//得到投保人信息
		INSBApplicant applicantTemp = new INSBApplicant();
		applicantTemp.setTaskid(taskId);
		applicantTemp = insbApplicantDao.selectOne(applicantTemp);
		if(applicantTemp!=null){
			if(applicantTemp.getPersonid()!=null){
				applicant= insbPersonDao.selectById(applicantTemp.getPersonid());
			}
		}
		//得到被保人信息
		INSBInsured insuredTemp = new INSBInsured();
		insuredTemp.setTaskid(taskId);
		insuredTemp = insbInsuredDao.selectOne(insuredTemp);
		if(insuredTemp!=null){
			if(insuredTemp.getPersonid()!=null){
				insured= insbPersonDao.selectById(insuredTemp.getPersonid());
			}
		}
		//得到权益索赔人信息
		INSBLegalrightclaim legalrightclaimTemp = new INSBLegalrightclaim();
		legalrightclaimTemp.setTaskid(taskId);
		legalrightclaimTemp = insbLegalrightclaimDao.selectOne(legalrightclaimTemp);
		if(legalrightclaimTemp!=null){
			if(legalrightclaimTemp.getPersonid()!=null){
				personForRight= insbPersonDao.selectById(legalrightclaimTemp.getPersonid());
			}
		}
		//得到联系人信息
		INSBRelationperson relationpersonTemp = new INSBRelationperson();
		relationpersonTemp.setTaskid(taskId);
		relationpersonTemp = insbRelationpersonDao.selectOne(relationpersonTemp);
		if(relationpersonTemp!=null){
			if(relationpersonTemp.getPersonid()!=null){
				linkPerson= insbPersonDao.selectById(relationpersonTemp.getPersonid());
			}
		}
		//得到车主信息
		INSBCarowneinfo carownerinfo = new INSBCarowneinfo();
		carownerinfo.setTaskid(taskId);
		carownerinfo = insbCarowneinfoDao.selectOne(carownerinfo);
		if(carownerinfo!=null){
			if(carownerinfo.getPersonid()!=null){
				carowner = insbPersonDao.selectById(carownerinfo.getPersonid());
			}
		}
		//判断是否为人工报价节点
		INSBOrder order = new INSBOrder();
		order.setTaskid(taskId);
		order.setPrvid(inscomcode);
		order = insbOrderDao.selectOne(order);
//		if(order==null){
			//不能查到订单表明是人工报价节点
			//重新得到投保人信息
			INSBApplicanthis applicanthis = new INSBApplicanthis();
			applicanthis.setTaskid(taskId);
			applicanthis.setInscomcode(inscomcode);
			applicanthis = insbApplicanthisDao.selectOne(applicanthis);
			if(applicanthis!=null){
				if(applicanthis.getPersonid()!=null){
					applicant= insbPersonDao.selectById(applicanthis.getPersonid());
				}
			}
			//重新得到被保人信息
			INSBInsuredhis insuredhis = new INSBInsuredhis();
			insuredhis.setTaskid(taskId);
			insuredhis.setInscomcode(inscomcode);
			insuredhis = insbInsuredhisDao.selectOne(insuredhis);
			if(insuredhis!=null){
				if(insuredhis.getPersonid()!=null){
					insured= insbPersonDao.selectById(insuredhis.getPersonid());
				}
			}
			//重新得到权益索赔人信息
			INSBLegalrightclaimhis legalrightclaimhis = new INSBLegalrightclaimhis();
			legalrightclaimhis.setTaskid(taskId);
			legalrightclaimhis.setInscomcode(inscomcode);
			legalrightclaimhis = insbLegalrightclaimhisDao.selectOne(legalrightclaimhis);
			if(legalrightclaimhis!=null){
				if(legalrightclaimhis.getPersonid()!=null){
					personForRight= insbPersonDao.selectById(legalrightclaimhis.getPersonid());
				}
			}
			//重新得到联系人信息
			INSBRelationpersonhis relationpersonhis = new INSBRelationpersonhis();
			relationpersonhis.setTaskid(taskId);
			relationpersonhis.setInscomcode(inscomcode);
			relationpersonhis = insbRelationpersonhisDao.selectOne(relationpersonhis);
			if(relationpersonhis!=null){
				if(relationpersonhis.getPersonid()!=null){
					linkPerson= insbPersonDao.selectById(relationpersonhis.getPersonid());
				}
			}
//		}
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("GenderList", inscCodeService.queryINSCCodeByCode("Gender","Gender"));//性别列表
		temp.put("CertKindList", inscCodeService.queryINSCCodeByCode("CertKinds","CertKinds"));//证件类型列表
		temp.put("taskid", taskId);
		temp.put("inscomcode", inscomcode);
		if("SHOW".equalsIgnoreCase(opeType)||"EDIT".equalsIgnoreCase(opeType)){
			if(applicant!=null){
				if(applicant.getName()!=null){
					temp.put("applicantname", applicant.getName());//投保人姓名
					if(applicant.getIdcardtype()!=null){
						temp.put("applicantidcardtype", applicant.getIdcardtype());//投保人证件类型code
						String CertKindValue = inscCodeService.transferValueToName("CertKinds","CertKinds",applicant.getIdcardtype() + "");
						if(StringUtils.isNotEmpty(CertKindValue)){
							temp.put("applicantidcardtypeValue", CertKindValue);//投保人证件类型
						}else{
							temp.put("applicantidcardtypeValue", "身份证");//投保人证件类型
						}
					}else{
						temp.put("applicantidcardtypeValue", "身份证");//投保人证件类型
					}
					if(applicant.getIdcardno()!=null){
						temp.put("applicantidcardno", applicant.getIdcardno());//投保人证件号
					}
				}
			}else{
				temp.put("applicantidcardtypeValue", "身份证");//投保人证件类型
			}
			if(insured!=null){
				if(insured.getName()!=null){
					temp.put("insuredname", insured.getName());//被保人姓名
					if(insured.getIdcardtype()!=null){
						temp.put("insuredidcardtype", insured.getIdcardtype());//被保人证件类型code
						String CertKindValue = inscCodeService.transferValueToName("CertKinds","CertKinds",insured.getIdcardtype() + "");
						if(StringUtils.isNotEmpty(CertKindValue)){
							temp.put("insuredidcardtypeValue", CertKindValue);//被保人证件类型
						}else{
							temp.put("insuredidcardtypeValue", "身份证");//被保人证件类型
						}
					}else{
						temp.put("insuredidcardtypeValue", "身份证");//被保人证件类型
					}
					if(insured.getIdcardno()!=null){
						temp.put("insuredidcardno", insured.getIdcardno());//被保人证件号
					}
					if(insured.getCellphone() != null){
						temp.put("insuredcellphone", insured.getCellphone());//被保人电话号
					}
					if(insured.getEmail() != null){
						temp.put("insuredemail", insured.getEmail());//被保人邮箱
					}
					
					temp.put("insuredsex", insured.getGender());//被保人性别
					if(insured.getNation() != null){
						temp.put("insurednation", insured.getNation());//被保人民族
					}
					if(insured.getBirthday() != null){
						temp.put("insuredbirthday", DateUtil.toString(insured.getBirthday()));//被保人出生日期
					}
					if(insured.getExpdate() != null){
						temp.put("insuredexpdate", insured.getExpdate());//被保人证件有效期
					}
                    if (insured.getExpstartdate() != null) {
                        temp.put("insuredexpstartdate", DateUtil.toString(insured.getExpstartdate()));//被保人证件有效期开始
                    }
                    if (insured.getExpenddate() != null) {
                        temp.put("insuredexpenddate", DateUtil.toString(insured.getExpenddate()));//被保人证件有效期结束
                    }
                    if(insured.getAddress() != null){
						temp.put("insuredaddress", insured.getAddress());//被保人地址
					}
					if(insured.getAuthority() != null){
						temp.put("insuredauthority", insured.getAuthority());//被保人签发机关
					}
				}
			}else{
				temp.put("insuredidcardtypeValue", "身份证");//被保人证件类型
			}
			//验证码
			if(insuredhis != null){
				temp.put("insuredpincode", insuredhis.getPincode());
			}
			if(personForRight!=null){
				if(personForRight.getName()!=null){
					temp.put("personForRightname", personForRight.getName());//权益索赔人姓名
					if(personForRight.getIdcardtype()!=null){
						temp.put("personForRightidcardtype", personForRight.getIdcardtype());//权益索赔人证件类型code
						String CertKindValue = inscCodeService.transferValueToName("CertKinds","CertKinds",personForRight.getIdcardtype() + "");
						if(StringUtils.isNotEmpty(CertKindValue)){
							temp.put("personForRightidcardtypeValue", CertKindValue);//权益索赔人证件类型
						}else{
							temp.put("personForRightidcardtypeValue", "身份证");//权益索赔人证件类型
						}
					}else{
						temp.put("personForRightidcardtypeValue", "身份证");//权益索赔人证件类型
					}
					if(personForRight.getIdcardno()!=null){
						temp.put("personForRightidcardno", personForRight.getIdcardno());//权益索赔人证件号
					}
				}
			}else{
				temp.put("personForRightidcardtypeValue", "身份证");//权益索赔人证件类型
			}
			if(linkPerson!=null){
				if(linkPerson.getName()!=null){
					temp.put("linkPersonname", linkPerson.getName());//联系人姓名
					if(linkPerson.getIdcardtype()!=null){
						temp.put("linkPersonidcardtype", linkPerson.getIdcardtype());//联系人证件类型
						String CertKindValue = inscCodeService.transferValueToName("CertKinds","CertKinds",linkPerson.getIdcardtype() + "");
						if(StringUtils.isNotEmpty(CertKindValue)){
							temp.put("linkPersonidcardtypeValue", CertKindValue);//联系人证件类型
						}else{
							temp.put("linkPersonidcardtypeValue", "身份证");//联系人证件类型
						}
					}else{
						temp.put("linkPersonidcardtypeValue", "身份证");//联系人证件类型
					}
					if(linkPerson.getIdcardno()!=null){
						temp.put("linkPersonidcardno", linkPerson.getIdcardno());//联系人证件号
					}
				}
			}else{
				temp.put("linkPersonidcardtypeValue", "身份证");//联系人证件类型
			}
			if(carowner!=null){
				if(carowner.getName()!=null){
					temp.put("carownername", carowner.getName());//车主姓名
				}
			}
			if("EDIT".equalsIgnoreCase(opeType)){
				//以下是修改弹出框需要增加的取值字段
				if(applicant!=null){
					if(applicant.getId()!=null){
						temp.put("applicantid", applicant.getId());//投保人id
					}
					if(applicant.getGender()!=null){
						temp.put("applicantgender", applicant.getGender());//投保人性别code
						temp.put("applicantGenderValue", inscCodeService.transferValueToName("Gender","Gender",applicant.getGender()+""));//投保人性别
					}
					if(applicant.getCellphone()!=null){
						temp.put("applicantcellphone", applicant.getCellphone());//投保人手机号
					}
					if(applicant.getEmail()!=null){
						temp.put("applicantEmail", applicant.getEmail());//投保人邮箱
					}
				}
				if(insured!=null){
					if(insured.getId()!=null){
						temp.put("insuredid", insured.getId());//被保人id
					}
					if(insured.getId()!=null){
					temp.put("insuredgender", insured.getGender());//被保人性别code
					temp.put("insuredGenderValue", inscCodeService.transferValueToName("Gender","Gender",insured.getGender()+""));//被保人性别
					}
					if(insured.getCellphone()!=null){
						temp.put("insuredcellphone", insured.getCellphone());//被保人手机号
					}
					if(insured.getEmail()!=null){
						temp.put("insuredEmail", insured.getEmail());//被保人邮箱
					}
				}
				if(personForRight!=null){
					if(personForRight.getId()!=null){
						temp.put("personForRightid", personForRight.getId());//权益索赔人id
					}
					if(personForRight.getGender()!=null){
					temp.put("personForRightgender", personForRight.getGender());//权益索赔人性别code
					temp.put("personForRightGenderValue", inscCodeService.transferValueToName("Gender","Gender",personForRight.getGender()+""));//权益索赔人性别
					}
					if(personForRight.getCellphone()!=null){
						temp.put("personForRightcellphone", personForRight.getCellphone());//权益索赔人手机号
					}
					if(personForRight.getEmail()!=null){
						temp.put("personForRightEmail", personForRight.getEmail());//权益索赔人邮箱
					}
				}
				if(linkPerson!=null){
					if(linkPerson.getId()!=null){
						temp.put("linkPersonid", linkPerson.getId());//联系人id
					}
					if(linkPerson.getGender()!=null){
					temp.put("linkPersongender", linkPerson.getGender());//联系人性别code
					temp.put("linkPersonGenderValue", inscCodeService.transferValueToName("Gender","Gender",linkPerson.getGender()+""));//联系人性别
					}
					if(linkPerson.getCellphone()!=null){
						temp.put("linkPersoncellphone", linkPerson.getCellphone());//联系人手机号
					}
					if(linkPerson.getEmail()!=null){
						temp.put("linkPersonEmail", linkPerson.getEmail());//联系人邮箱
					}
				}
				if(carowner!=null){
					if(carowner.getId()!=null){
						temp.put("carOwnerid", carowner.getId());//车主id
					}
					if(carowner.getGender()!=null){
						temp.put("carownergender", carowner.getGender());//车主性别code
						temp.put("carownerGenderValue", inscCodeService.transferValueToName("Gender","Gender",carowner.getGender()+""));//车主性别
					}
					if(carowner.getIdcardtype()!=null){
						temp.put("carownercardtype", carowner.getIdcardtype());//车主证件类型
						temp.put("carownercardtypeValue", inscCodeService.transferValueToName("CertKinds","CertKinds",carowner.getIdcardtype() + ""));//车主证件类型
					}
					if(carowner.getIdcardno()!=null){
						temp.put("carownercardno", carowner.getIdcardno());//车主证件号
					}
					if(carowner.getCellphone()!=null){
						temp.put("carownercellphone", carowner.getCellphone());//车主手机号
					}
					if(carowner.getEmail()!=null){
						temp.put("carownerEmail", carowner.getEmail());//车主邮箱
					}
				}
			}
		} 
		return temp;
	}
	
	/**
	 * 通过taskid查询车辆任务中单个关系人详细信息
	 */
	@Override
	public Map<String, Object> getOneOfCarTaskRelationPersonInfo(String taskid, String inscomcode, String opeType) {
		String personId = "";
		INSBPerson person = new INSBPerson();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> temp = new HashMap<String, Object>();
		//通过是否生成订单判断是否在人工报价节点
		boolean noHasOrder = false;
		INSBOrder order = new INSBOrder();
		order.setTaskid(taskid);
		order.setPrvid(inscomcode);
		order = insbOrderDao.selectOne(order);
		if(order==null){
			//不能查到订单表明是人工报价节点
			noHasOrder = true;
		}
		//2016年2月17日新要求必须查询历史表数据
		noHasOrder = true;
		//得到投保人id
		if("APPLICANT".equalsIgnoreCase(opeType)){
			temp.put("showtype", "投保人");
			INSBApplicant applicantTemp = new INSBApplicant();
			applicantTemp.setTaskid(taskid);
			applicantTemp = insbApplicantDao.selectOne(applicantTemp);
			if(applicantTemp!=null){
				if(applicantTemp.getPersonid()!=null){
					personId= applicantTemp.getPersonid();
				}
			}
			//如需查询历史中间表重新得到personid
			if(noHasOrder){
				INSBApplicanthis applicanthis = new INSBApplicanthis();
				applicanthis.setTaskid(taskid);
				applicanthis.setInscomcode(inscomcode);
				applicanthis = insbApplicanthisDao.selectOne(applicanthis);
				if(applicanthis!=null){
					personId = applicanthis.getPersonid();
				}
			}
		}
		//得到被保人id
		if("INSURED".equalsIgnoreCase(opeType)){
			temp.put("showtype", "被保人");
			INSBInsured insuredTemp = new INSBInsured();
			insuredTemp.setTaskid(taskid);
			insuredTemp = insbInsuredDao.selectOne(insuredTemp);
			if(insuredTemp!=null){
				if(insuredTemp.getPersonid()!=null){
					personId= insuredTemp.getPersonid();
				}
			}
			//如需查询历史中间表重新得到personid
			if(noHasOrder){
				INSBInsuredhis insuredhis = new INSBInsuredhis();
				insuredhis.setTaskid(taskid);
				insuredhis.setInscomcode(inscomcode);
				insuredhis = insbInsuredhisDao.selectOne(insuredhis);
				if(insuredhis!=null){
					personId = insuredhis.getPersonid();
				}
			}
		}
		//得到权益索赔人id
		if("PERSONFORRIGHT".equalsIgnoreCase(opeType)){
			temp.put("showtype", "权益索赔人");
			INSBLegalrightclaim legalrightclaimTemp = new INSBLegalrightclaim();
			legalrightclaimTemp.setTaskid(taskid);
			legalrightclaimTemp = insbLegalrightclaimDao.selectOne(legalrightclaimTemp);
			if(legalrightclaimTemp!=null){
				if(legalrightclaimTemp.getPersonid()!=null){
					personId= legalrightclaimTemp.getPersonid();
				}
			}
			//如需查询历史中间表重新得到personid
			if(noHasOrder){
				INSBLegalrightclaimhis legalrightclaimhis = new INSBLegalrightclaimhis();
				legalrightclaimhis.setTaskid(taskid);
				legalrightclaimhis.setInscomcode(inscomcode);
				legalrightclaimhis = insbLegalrightclaimhisDao.selectOne(legalrightclaimhis);
				if(legalrightclaimhis!=null){
					personId = legalrightclaimhis.getPersonid();
				}
			}
		}
		//得到联系人id
		if("LINKPERSON".equalsIgnoreCase(opeType)){
			temp.put("showtype", "联系人");
			INSBRelationperson relationpersonTemp = new INSBRelationperson();
			relationpersonTemp.setTaskid(taskid);
			relationpersonTemp = insbRelationpersonDao.selectOne(relationpersonTemp);
			if(relationpersonTemp!=null){
				if(relationpersonTemp.getPersonid()!=null){
					personId= relationpersonTemp.getPersonid();
				}
			}
			//如需查询历史中间表重新得到personid
			if(noHasOrder){
				INSBRelationpersonhis relationpersonhis = new INSBRelationpersonhis();
				relationpersonhis.setTaskid(taskid);
				relationpersonhis.setInscomcode(inscomcode);
				relationpersonhis = insbRelationpersonhisDao.selectOne(relationpersonhis);
				if(relationpersonhis!=null){
					personId = relationpersonhis.getPersonid();
				}
			}
		}
		//查询车主
		if("OWNER".equalsIgnoreCase(opeType)){
			temp.put("showtype", "车主");
			INSBCarowneinfo carownerinfo = new INSBCarowneinfo();
			carownerinfo.setTaskid(taskid);
			carownerinfo = insbCarowneinfoDao.selectOne(carownerinfo);
			if(carownerinfo!=null){
				personId = carownerinfo.getPersonid();
			}
		}
		if(!("".equals(personId))){
			person= insbPersonDao.selectById(personId);
			if(person!=null){
				if(person.getName()!=null){
					temp.put("personname", person.getName());//姓名
				}
				if(person.getEname()!=null){
					temp.put("personename", person.getEname());//英文名
				}
				temp.put("persongender", person.getGender());//性别code
				temp.put("persongenderValue", inscCodeService.transferValueToName("Gender","Gender",person.getGender()+""));//性别
				temp.put("taskid", person.getTaskid());//任务id
				temp.put("operator", person.getOperator());//操作员
				temp.put("createtime", person.getCreatetime());//创建时间
				if(StringUtils.isNotEmpty(person.getBloodtype()+"")){
					temp.put("personbloodtype", person.getBloodtype());//血型code
					temp.put("personbloodtypeValue", inscCodeService.transferValueToName("BloodType","BloodType",person.getBloodtype()+""));//血型
				}
				if(StringUtils.isNotEmpty(person.getMaritalstatus())){
					temp.put("personmaritalstatus", person.getMaritalstatus());//婚姻状态code
					temp.put("personmaritalstatusValue", inscCodeService.transferValueToName("MaritalStatus","MaritalStatus",person.getMaritalstatus()+""));//婚姻状态
				}
				if(person.getAddress()!=null){
					temp.put("personaddress", person.getAddress());//居住地
				}
				if(person.getEaddress()!=null){
					temp.put("personeaddress", person.getEaddress());//英文住址
				}
				if(StringUtils.isNotEmpty(person.getIdcardtype()+"")){
					temp.put("personidcardtype", person.getIdcardtype());//证件类型code
					temp.put("personidcardtypeValue", inscCodeService.transferValueToName("CertKinds","CertKinds",person.getIdcardtype()+""));//证件类型
				}
				if(person.getCellphone()!=null){
					temp.put("personcellphone", person.getCellphone());//电话
				}
				if(person.getIdcardno()!=null){
					temp.put("personidcardno", person.getIdcardno());//证件号
				}
				if(person.getBirthday()!=null){
					temp.put("personbirthday", sdf.format(person.getBirthday()).toString());//生日
				}
				if(StringUtils.isNotEmpty(person.getEducateinfo())){
					temp.put("personeducateinfo", person.getEducateinfo());//教育状况code
					temp.put("personeducateinfoValue",  inscCodeService.transferValueToName("EducationLevel","EducationLevel",person.getEducateinfo()+""));//教育状况
				}
				temp.put("personage", person.getAge());//年龄
				if(person.getEmail()!=null){
					temp.put("personEmail", person.getEmail());//邮箱
				}
				if(person.getZip()!=null){
					temp.put("personzip", person.getZip());//邮编
				}
				if(person.getJob()!=null){
					temp.put("personjob", person.getJob());//工作
				}
			}
		}
		return temp;
	}
	public INSBPerson editPersonInfo(INSBPerson oldPerson, INSBPerson pagePerson){
		oldPerson.setName(pagePerson.getName());
		oldPerson.setGender(pagePerson.getGender());
		oldPerson.setIdcardtype(pagePerson.getIdcardtype());
		oldPerson.setIdcardno(pagePerson.getIdcardno());
		oldPerson.setCellphone(pagePerson.getCellphone());
		oldPerson.setEmail(pagePerson.getEmail());
		return oldPerson;
	}
	/**
	 * 修改关系人信息
	 * */
	@Override
	public String editRelationPersonInfo(INSBRelationPersonVO relationPersonVO) {
		//分别得到页面传回的四个关系人对象
		INSBPerson personInsuredPage = relationPersonVO.getInsured();//被保人
		INSBPerson personApplicantPage = relationPersonVO.getApplicant();//投保人
		INSBPerson personRelationpersonPage = relationPersonVO.getLinkPerson();//联系人
		INSBPerson personLegalrightclaimPage = relationPersonVO.getPersonForRight();//权益人
		INSBPerson personCarOwnerInfoPage = relationPersonVO.getCarOwnerInfo();//车主
		//得到页面传回的其他信息
		Boolean applicantflag = relationPersonVO.getApplicantflag();//投保人是否与被保人一致
		Boolean relationpersonflag = relationPersonVO.getLinkPersonflag();//联系人是否与被保人一致
		Boolean Legalrightclaimflag = relationPersonVO.getPersonForRightflag();//权益人是否与被保人一致
		String taskIdPage = relationPersonVO.getTaskid();//工作流程号
		String inscomcodePage = relationPersonVO.getInscomcode();//供应商id
		String insuredIdPage = relationPersonVO.getInsuredid();//被保人id
		String applicantIdPage = relationPersonVO.getApplicantid();//投保人id
		String relationpersonIdPage = relationPersonVO.getLinkPersonid();//关系人id
		String legalrightclaimIdPage = relationPersonVO.getPersonForRightid();//权益人id
		String carOwnerInfoIdPage = relationPersonVO.getCarOwnerid();//车主id
		String operatorPage= relationPersonVO.getOperator();//操作人

		if(StringUtil.isEmpty(carOwnerInfoIdPage)||StringUtil.isEmpty(taskIdPage)
				||StringUtil.isEmpty(operatorPage)||StringUtil.isEmpty(inscomcodePage)){
			LogUtil.info("CM后台修改关系人,传入参数异常,部分参数为空：taskid="+taskIdPage+",inscomcode="+inscomcodePage+",操作人="+operatorPage);
			return CommonModel.STATUS_FAIL;
		}
		Date date = new Date();
		INSBPerson insbPersonCarOwnerInfo = insbPersonDao.selectById(carOwnerInfoIdPage);//车主关联的person记录
		if(StringUtil.isEmpty(insbPersonCarOwnerInfo)){
			LogUtil.info("CM后台修改关系人,车主人员信息不存在：taskid="+taskIdPage+",inscomcode="+inscomcodePage+",操作人="+operatorPage+",personId="+carOwnerInfoIdPage);
			return CommonModel.STATUS_FAIL;
		}
		INSBPerson insbPersonInsured = insbPersonDao.selectById(insuredIdPage);//被保人关联的person记录
		INSBPerson insbPersonApplicant = insbPersonDao.selectById(applicantIdPage);//投保人关联的person记录
		INSBPerson insbPersonRelationperson = insbPersonDao.selectById(relationpersonIdPage);//关系人关联的person记录
		INSBPerson insbPersonLegalrightclaim = insbPersonDao.selectById(legalrightclaimIdPage);//权益人人关联的person记录
		INSBPerson newPerson = new INSBPerson();//新建person共用对象
		//被保人
		if(!StringUtil.isEmpty(insbPersonInsured)){
			insbPersonInsured=insbPersonHelpService.updateINSBPerson(insbPersonInsured,personInsuredPage,operatorPage,date);	
		}else{
			//没有相关被保人insbperson记录，①：查找工作流程号（taskid）是否有关联被保人原始记录，如果没有则添加
			INSBInsured insbInsured = insbInsuredDao.selectInsuredByTaskId(taskIdPage);
			if(StringUtil.isEmpty(insbInsured)){
				newPerson=insbPersonHelpService.addPersonIsNull(insbPersonCarOwnerInfo, operatorPage, ConstUtil.STATUS_2);
				insbInsured= new INSBInsured();
				insbInsured.setOperator(operatorPage);
				insbInsured.setPersonid(newPerson.getId());
			}
			//②：判断被保人历史表是否有关联记录，没有添加
		    INSBInsuredhis insbInsuredhis = new INSBInsuredhis();
            insbInsuredhis.setTaskid(taskIdPage);
            insbInsuredhis.setInscomcode(inscomcodePage);
            INSBInsuredhis insuredhis = insbInsuredhisDao.selectOne(insbInsuredhis);
            if (StringUtil.isEmpty(insuredhis)) {
            	newPerson=insbPersonHelpService.addPersonHisIsNull(insbInsured,taskIdPage, inscomcodePage, ConstUtil.STATUS_2);
            }
            //最后更新创建的insbPerson为页面编辑内容
            insbPersonInsured=insbPersonHelpService.updateINSBPerson(newPerson,personInsuredPage,operatorPage,date);	
		}
		//投保人
		if(!StringUtil.isEmpty(insbPersonApplicant)){		
			//与被保人人员信息一致
			if(applicantflag){			
				insbPersonApplicant=insbPersonHelpService.updateINSBPerson(insbPersonApplicant,insbPersonInsured,operatorPage,date);
			}else{
				insbPersonApplicant=insbPersonHelpService.updateINSBPerson(insbPersonApplicant,personApplicantPage,operatorPage,date);
			}
		}else{
			INSBApplicant insbApplicant = insbApplicantDao.selectByTaskID(taskIdPage);
			if(StringUtil.isEmpty(insbApplicant)){
				newPerson=insbPersonHelpService.addPersonIsNull(insbPersonCarOwnerInfo, operatorPage, ConstUtil.STATUS_1);
				insbApplicant= new INSBApplicant();
				insbApplicant.setOperator(operatorPage);
				insbApplicant.setPersonid(newPerson.getId());
			}
			INSBApplicanthis insbApplicanthis = new INSBApplicanthis();
            insbApplicanthis.setTaskid(taskIdPage);
            insbApplicanthis.setInscomcode(inscomcodePage);
            INSBApplicanthis applicanthis = insbApplicanthisDao.selectOne(insbApplicanthis);
            if (StringUtil.isEmpty(applicanthis)) {
            	newPerson=insbPersonHelpService.addPersonHisIsNull(insbApplicant,taskIdPage, inscomcodePage, ConstUtil.STATUS_1);
            }
            //最后更新创建的insbPerson为页面编辑内容
			if(applicantflag){			
				insbPersonApplicant=insbPersonHelpService.updateINSBPerson(newPerson,insbPersonInsured,operatorPage,date);
			}else{
				insbPersonApplicant=insbPersonHelpService.updateINSBPerson(newPerson,personApplicantPage,operatorPage,date);
			}
		}
		//权益索赔人
		if(!StringUtil.isEmpty(insbPersonRelationperson)){			
			if(relationpersonflag){
				insbPersonRelationperson=insbPersonHelpService.updateINSBPerson(insbPersonRelationperson,insbPersonInsured,operatorPage,date);
			}else{
				insbPersonRelationperson=insbPersonHelpService.updateINSBPerson(insbPersonRelationperson,personRelationpersonPage,operatorPage,date);
			}
		}else{
			INSBLegalrightclaim insbLegalrightclaim = insbLegalrightclaimDao.selectByTaskID(taskIdPage);
			if(StringUtil.isEmpty(insbLegalrightclaim)){
				newPerson=insbPersonHelpService.addPersonIsNull(insbPersonCarOwnerInfo, operatorPage, ConstUtil.STATUS_3);
				insbLegalrightclaim=new INSBLegalrightclaim();
				insbLegalrightclaim.setOperator(operatorPage);
				insbLegalrightclaim.setPersonid(newPerson.getId());
			}
			INSBLegalrightclaimhis insbLegalrightclaimhis = new INSBLegalrightclaimhis();
            insbLegalrightclaimhis.setTaskid(taskIdPage);
            insbLegalrightclaimhis.setInscomcode(inscomcodePage);
            INSBLegalrightclaimhis legalrightclaimhis = insbLegalrightclaimhisDao.selectOne(insbLegalrightclaimhis);
            if (StringUtil.isEmpty(legalrightclaimhis)) {
            	newPerson=insbPersonHelpService.addPersonHisIsNull(insbLegalrightclaim,taskIdPage, inscomcodePage, ConstUtil.STATUS_3);
            }
           //最后更新创建的insbPerson为页面编辑内容
            if(relationpersonflag){
				insbPersonRelationperson=insbPersonHelpService.updateINSBPerson(newPerson,insbPersonInsured,operatorPage,date);
			}else{
				insbPersonRelationperson=insbPersonHelpService.updateINSBPerson(newPerson,personRelationpersonPage,operatorPage,date);
			}
		}
		//联系人
		if(!StringUtil.isEmpty(insbPersonLegalrightclaim)){
			if(Legalrightclaimflag){
				insbPersonLegalrightclaim=insbPersonHelpService.updateINSBPerson(insbPersonLegalrightclaim,insbPersonInsured,operatorPage,date);
			}else{
				insbPersonLegalrightclaim=insbPersonHelpService.updateINSBPerson(insbPersonLegalrightclaim,personLegalrightclaimPage,operatorPage,date);
			}
		}else{
			INSBRelationperson insbRelationperson = insbRelationpersonDao.selectByTaskID(taskIdPage);
			if(StringUtil.isEmpty(insbRelationperson)){
				newPerson=insbPersonHelpService.addPersonIsNull(insbPersonCarOwnerInfo, operatorPage, ConstUtil.STATUS_4);
				insbRelationperson=new INSBRelationperson();
				insbRelationperson.setOperator(operatorPage);
				insbRelationperson.setPersonid(newPerson.getId());
			}
			INSBRelationpersonhis insbRelationpersonhis = new INSBRelationpersonhis();
            insbRelationpersonhis.setTaskid(taskIdPage);
            insbRelationpersonhis.setInscomcode(inscomcodePage);
            INSBRelationpersonhis relationpersonhis = insbRelationpersonhisDao.selectOne(insbRelationpersonhis);
            if (StringUtil.isEmpty(relationpersonhis)) {
            	newPerson=insbPersonHelpService.addPersonHisIsNull(insbRelationperson,taskIdPage, inscomcodePage, ConstUtil.STATUS_4);
            }
            //最后更新创建的insbPerson为页面编辑内容
            if(Legalrightclaimflag){
				insbPersonLegalrightclaim=insbPersonHelpService.updateINSBPerson(newPerson,insbPersonInsured,operatorPage,date);
			}else{
				insbPersonLegalrightclaim=insbPersonHelpService.updateINSBPerson(newPerson,personLegalrightclaimPage,operatorPage,date);
			}
		}
		if(!StringUtil.isEmpty(insbPersonCarOwnerInfo)){	
			insbPersonCarOwnerInfo=insbPersonHelpService.updateINSBPerson(insbPersonCarOwnerInfo,personCarOwnerInfoPage,operatorPage,date);
		}
		//更新INSBPolicyitem表相关name值
		List<INSBPolicyitem> insbPolicyitems =insbPersonHelpService.getINSBPolicyItemList(taskIdPage,inscomcodePage);
        for (INSBPolicyitem ddInsbPolicyitem : insbPolicyitems) {
			ddInsbPolicyitem.setInsuredname(insbPersonInsured.getName());
			ddInsbPolicyitem.setApplicantname(insbPersonApplicant.getName());
			ddInsbPolicyitem.setCarownername(insbPersonCarOwnerInfo.getName());
			insbPolicyitemService.updateById(ddInsbPolicyitem);
		}
		return CommonModel.STATUS_SUCCESS;
	}
	@Override
	public void deletebyID(String id) {
		
		insbPersonDao.deletebyID(id);
		
	}
	@Override
	public String updateCarOwnerInfo(INSBPerson carowner, String taskid) {
		try {
			INSBCarowneinfo temp = new INSBCarowneinfo();
			temp.setTaskid(taskid);
			INSBCarowneinfo carowneinfo = insbCarowneinfoDao.selectOne(temp);
			INSBPerson person = new INSBPerson();
			person = insbPersonDao.selectById(carowneinfo.getPersonid());
			person.setName(carowner.getName());
			person.setEname(carowner.getEname());
			person.setGender(carowner.getGender());
			person.setBloodtype(carowner.getBloodtype());
			person.setMaritalstatus(carowner.getMaritalstatus());
			person.setAddress(carowner.getAddress());
			person.setEaddress(carowner.getEaddress());
			person.setEducateinfo(carowner.getEducateinfo());
			person.setAge(carowner.getAge());
			person.setEmail(carowner.getEmail());
			person.setZip(carowner.getZip());
			person.setJob(carowner.getJob());
			insbPersonDao.updateById(person);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
	@Override
	public List<String> getSelectDelId(String taskID) {
		return insbPersonDao.getSelectDelId(taskID);
	}
	@Override
	public INSBPerson selectCarOwnerPersonByTaskId(String taskId) {
		return insbPersonDao.selectCarOwnerPersonByTaskId(taskId);
	}
}