package com.zzb.mobile.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zzb.cm.dao.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.common.ModelUtil;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBSpecifydriver;
import com.zzb.cm.entity.INSBSpecifydriverhis;
import com.zzb.cm.entity.INSBSupplementaryinfo;
import com.zzb.cm.entity.entityutil.EntityTransformUtil;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppProposalInfoService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class AppProposalInfoServiceImpl implements AppProposalInfoService {
	@Resource
	private INSBCarinfoDao carinfoDao;
	@Resource
	private INSBCarinfohisDao carinfohisDao;
	@Resource
	private INSBCarmodelinfoDao carmodelinfoDao;
	@Resource
	private INSBCarmodelinfohisDao carmodelinfohisDao;
	@Resource
	private INSBInsuredDao insuredDao;
	@Resource
	private INSBInsuredhisDao insuredhisDao;
	@Resource
	private INSBPersonDao personDao;
	@Resource
	private INSBCarowneinfoDao carowneinfoDao;
	@Resource
	private INSBApplicantDao applicantDao;
	@Resource
	private INSBApplicanthisDao applicanthisDao;
	@Resource
	private INSBOrderDao orderDao;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSBPolicyitemDao policyitemDao;
	@Resource
	private INSBSpecifydriverDao specifydriverDao;
	@Resource
	private INSBSpecifydriverhisDao specifydriverhisDao;
	@Resource
	private INSBSupplementaryinfoDao supplementaryinfoDao;
	@Resource
	private INSBInsuresupplyparamDao insbInsuresupplyparamDao;

	/**
	 * 获取投保基础信息接口
	 */
	@Override
	public String getProposalInfo(String processInstanceId,String inscomcode) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> carInfoMap = new HashMap<String, Object>();
		Map<String, Object> carOwnerInfoMap = new HashMap<String, Object>();
		Map<String, Object> insuredInfoMap = new HashMap<String, Object>();
		Map<String, Object> applicantMap = new HashMap<String, Object>();
		CommonModel model = new CommonModel();
		try{
			// 被保险人信息
            insuredInfoMap.put("name", "");
            insuredInfoMap.put("gender", "");
            insuredInfoMap.put("idcardno", "");
            insuredInfoMap.put("idcardtype", "");
            insuredInfoMap.put("cellphone", "");
            insuredInfoMap.put("email", "");

			INSBInsuredhis insuredhis = selectInsuredhis(processInstanceId,inscomcode);
			String insuredid = null;
			if(insuredhis == null){
				 INSBInsured insbInsured = insuredDao.selectInsuredByTaskId(processInstanceId);
				 if(insbInsured != null){
					 insuredid = insbInsured.getPersonid();
				 }
			}else{
				insuredid = insuredhis.getPersonid();
			}
            if (StringUtil.isNotEmpty(insuredid)) {
                INSBPerson insured = personDao.selectById(insuredid);
                if (insured != null) {
                    insuredInfoMap.put("name", insured.getName());
                    insuredInfoMap.put("gender", insured.getGender());
                    insuredInfoMap.put("idcardno", insured.getIdcardno());
                    insuredInfoMap.put("idcardtype", insured.getIdcardtype());
                    insuredInfoMap.put("cellphone", insured.getCellphone());
                    insuredInfoMap.put("email", insured.getEmail());
                }
            }

			// 组织车辆信息
			INSBCarinfohis carinfohis = selectCarinfohis(processInstanceId,inscomcode);
			
			INSBCarinfo carinfo = carinfoDao.selectCarinfoByTaskId(processInstanceId);
			String carid = null;
			if(carinfo != null){
				carid = carinfo.getId();
			}
			String specialDriverid = null;
			if(carinfohis == null){
				specialDriverid = organizeCarinfo(carInfoMap, carinfo,inscomcode);
			}else{
				INSBCarinfo carinfotemp = EntityTransformUtil.carinfohis2Carinfo(carinfohis);
				specialDriverid = organizeCarinfo(carInfoMap, carinfotemp,inscomcode);
				//存入所属性质和车辆性质
				carInfoMap.put("property", carinfohis.getProperty());
				carInfoMap.put("carProperty", carinfohis.getCarproperty());
			}
			// 车辆发动机号、VIN码隐藏
			carInfoMap.put("engineno", ModelUtil.hiddenEngineNo((String)carInfoMap.get("engineno")));
			carInfoMap.put("vincode", ModelUtil.hiddenVin((String)carInfoMap.get("vincode")));
			
			INSBCarmodelinfohis carmodelinfohis = selectCarmodelhis(inscomcode,carid);
			if(carmodelinfohis == null){
				INSBCarmodelinfo carmodelinfo = selectcarmodel(carid);
				if(carmodelinfo !=null){
					carInfoMap.put("seat", carmodelinfo.getSeat());
					carInfoMap.put("unwrtweight", carmodelinfo.getUnwrtweight());
					carInfoMap.put("fullweight", carmodelinfo.getFullweight());
					carInfoMap.put("brandname", carmodelinfo.getBrandname());// 品牌名称
					carInfoMap.put("displacement", carmodelinfo.getDisplacement());//排气量
				}else{
					carInfoMap.put("seat", "");
					carInfoMap.put("unwrtweight", "");
					carInfoMap.put("fullweight", "");
					carInfoMap.put("brandname", "");// 品牌名称
					carInfoMap.put("displacement", "");//排气量
				}
			}else{
				carInfoMap.put("seat", carmodelinfohis.getSeat());
				carInfoMap.put("unwrtweight", carmodelinfohis.getUnwrtweight());
				carInfoMap.put("fullweight", carmodelinfohis.getFullweight());
				carInfoMap.put("brandname", carmodelinfohis.getBrandname());// 品牌名称
				carInfoMap.put("displacement", carmodelinfohis.getDisplacement());//排气量
			}

			// 车主信息
            carOwnerInfoMap.put("name","");
            carOwnerInfoMap.put("idcardType", "");
            carOwnerInfoMap.put("idcardno", "");
            carOwnerInfoMap.put("isSameWithInsured", false);

            INSBCarowneinfo carowneinfo = carowneinfoDao.selectByTaskId(processInstanceId);
            if (carowneinfo != null) {
                INSBPerson carowner = personDao.selectById(carowneinfo.getPersonid());
                if (carowner != null) {
                    carOwnerInfoMap.put("name", carowner.getName());
                    carOwnerInfoMap.put("idcardType", carowner.getIdcardtype());
                    carOwnerInfoMap.put("idcardno", carowner.getIdcardno());

                    if (!StringUtil.isEmpty(insuredid) && insuredid.equals(carowner.getId())) {
                        carOwnerInfoMap.put("isSameWithInsured", true);
                    }
                }
            }

			// 投保人信息
            applicantMap.put("name", "");
            applicantMap.put("gender", "");
            applicantMap.put("idcardno", "");
            applicantMap.put("idcardtype", "");
            applicantMap.put("isSameWithInsured", false);

			INSBApplicanthis applicanthis = selectAppicanthis(processInstanceId, inscomcode);
			String applicantid = null;
			if(applicanthis == null){
				INSBApplicant applicant = applicantDao.selectByTaskID(processInstanceId);
                if (applicant != null) {
                    applicantid = applicant.getPersonid();
                }
			}else{
				applicantid = applicanthis.getPersonid();
			}
            if (StringUtil.isNotEmpty(applicantid)) {
                INSBPerson applicantMan = personDao.selectById(applicantid);
                if (applicantMan != null) {
                    applicantMap.put("name", applicantMan.getName());
                    applicantMap.put("gender", applicantMan.getGender());
                    applicantMap.put("idcardno", applicantMan.getIdcardno());
                    applicantMap.put("idcardtype", applicantMan.getIdcardtype());
                }
                if (!StringUtil.isEmpty(insuredid) && insuredid.equals(applicantid)) {
                    applicantMap.put("isSameWithInsured", true);
                }
            }

			// 指定驾驶人
			Map<String, Object> specialDriverMap = new HashMap<String, Object>();
			if(specialDriverid == null){
				specialDriverMap.put("isSameWithInsured", false);
			}else{
				specialDriverMap.put("isSameWithInsured", true);
			}
			List<Map<String,Object>> drivers = new ArrayList<Map<String,Object>>();
            List<INSBSpecifydriverhis> specifyDriverhis = selectSpecifyDriverhis(processInstanceId,inscomcode);
			if(specifyDriverhis == null || specifyDriverhis.size()<1){
				List<INSBSpecifydriver> specifyDriver = selectSpecifyDriver(processInstanceId);
				for (INSBSpecifydriver insbSpecifydriver : specifyDriver) {
					INSBPerson driver = personDao.selectById(insbSpecifydriver.getPersonid());
					drivers = organizeDrivers(drivers, driver);
				}
			}else{
				for (INSBSpecifydriverhis insbSpecifydriverhis : specifyDriverhis) {
					INSBPerson driver = personDao.selectById(insbSpecifydriverhis.getPersonid());
					drivers = organizeDrivers(drivers, driver);
				}
			}
			specialDriverMap.put("proposalDrivers", drivers);

			// 其他信息
			INSBOrder queryInsbOrder = new INSBOrder();
			queryInsbOrder.setTaskid(processInstanceId);
			queryInsbOrder.setPrvid(inscomcode);
			INSBOrder orderInfo = orderDao.selectOne(queryInsbOrder);
			if(orderInfo != null){
				resultMap.put("deptcode", orderInfo.getDeptcode());
				resultMap.put("deptname", getDeptname(orderInfo.getDeptcode()));
			}else{
				resultMap.put("deptcode", "");
				resultMap.put("deptname", "");
				model.setMessage("订单数据为空!");
			}

            resultMap.put("proposalCarinfo", carInfoMap);
            resultMap.put("proposalInsured", insuredInfoMap);
            resultMap.put("proposalApplicant", applicantMap);
            resultMap.put("proposalCarOwner", carOwnerInfoMap);
            resultMap.put("proposalSpecifyDriver", specialDriverMap);

			// 补充信息
            List<Map<String, Object>> supplementlist = new ArrayList<Map<String, Object>>();
			INSBSupplementaryinfo temp = new INSBSupplementaryinfo();
			temp.setTaskid(processInstanceId);
			List<INSBSupplementaryinfo> templist = supplementaryinfoDao.selectList(temp);
			if (templist != null) {
                for (INSBSupplementaryinfo supplement : templist) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", supplement.getId());
                    map.put("inputtype", supplement.getInputtype());
                    map.put("isquotemust", supplement.getIsquotemust());
                    map.put("itemcode", supplement.getItemcode());
                    map.put("itemname", supplement.getItemname());
                    map.put("itemtype", supplement.getItemtype());
                    map.put("optional", JSONObject.fromObject(supplement.getOptional()));
                    supplementlist.add(map);
                }
            }
			resultMap.put("supplementlist", supplementlist);

			//核保补充数据项
			resultMap.put("supplyParam", insbInsuresupplyparamDao.getByTask(processInstanceId, inscomcode));

			model.setBody(resultMap);
		}catch(Exception e){
			model.setStatus("fail");
			if(model.getMessage()==null)
			model.setMessage("获取投保信息失败！");
            e.printStackTrace();

			return JSONObject.fromObject(model).toString();
		}

		model.setStatus("success");
		if(model.getMessage()==null)
		model.setMessage("获取投保信息成功！");

		return JSONObject.fromObject(model).toString();
	}

	private String getDeptname(String deptcode) {
		INSCDept inscDept = deptDao.selectByComcode(deptcode);
		if(inscDept != null){
			return inscDept.getComname();
		}
		return null;
	}

	private List<Map<String, Object>> organizeDrivers(List<Map<String, Object>> drivers, INSBPerson driver) {
		HashMap<String, Object> driverMap = new HashMap<String, Object>();
		driverMap.put("id", driver.getId());
		driverMap.put("name", driver.getName());
		driverMap.put("gender", driver.getGender());
		driverMap.put("licensetype", driver.getLicensetype());
		driverMap.put("licenseno", driver.getLicenseno());
		driverMap.put("birthday", ModelUtil.conbertToString(driver.getBirthday()));
		driverMap.put("licensedate", ModelUtil.conbertToString(driver.getLicensedate()));
		drivers.add(driverMap);
		return drivers;
	}

	private String organizeCarinfo(Map<String, Object> carInfoMap, INSBCarinfo carinfo,String inscomcode) {
		if(carinfo != null){
			carInfoMap.put("carlicenseno", carinfo.getCarlicenseno());// 车牌号
			carInfoMap.put("standardfullname", carinfo.getStandardfullname());// 车辆型号
			carInfoMap.put("engineno", carinfo.getEngineno());
			carInfoMap.put("vincode", carinfo.getVincode());
			carInfoMap.put("istransfer", carinfo.getIsTransfercar());
			if(carinfo.getTransferdate()!=null)
			carInfoMap.put("chgOwnerDate", ModelUtil.conbertToString(carinfo.getTransferdate()));//过户日期
			if(carinfo.getRegistdate()!=null)
			carInfoMap.put("registdate", ModelUtil.conbertToString(carinfo.getRegistdate()));
			carInfoMap.put("drivingarea", carinfo.getDrivingarea());// 行驶区域
			carInfoMap.put("specifydriver", carinfo.getSpecifydriver());// 指定驾驶人
			carInfoMap.put("preinscode", carinfo.getPreinscode());//上年投保公司
			if(carinfo.getBusinessstartdate()==null){
				INSBPolicyitem temp = new INSBPolicyitem();
				temp.setTaskid(carinfo.getTaskid());
				temp.setInscomcode(inscomcode);
				List<INSBPolicyitem> tempList = policyitemDao.selectList(temp);
				for (INSBPolicyitem item : tempList) {
					if("0".equals(item.getRisktype())){
						carinfo.setBusinessstartdate(item.getStartdate());
						carinfo.setBusinessenddate(item.getEnddate());
					}else{
						carinfo.setStrongstartdate(item.getStartdate());
						carinfo.setStrongenddate(item.getEnddate());
					}
					
				}
			}
			if(carinfo.getBusinessstartdate()!=null)
			carInfoMap.put("businessstartdate",
					ModelUtil.conbertToString(carinfo.getBusinessstartdate()));// 商业险开始日期
			if(carinfo.getBusinessenddate()!=null)
			carInfoMap.put("businessenddate",
					ModelUtil.conbertToString(carinfo.getBusinessenddate()));// 商业险结束日期
			if(carinfo.getStrongstartdate()!=null)
			carInfoMap.put("strongstartdate",
					ModelUtil.conbertToString(carinfo.getStrongstartdate()));// 交强险开始日期
			if(carinfo.getStrongenddate()!=null)
			carInfoMap.put("strongenddate",
					ModelUtil.conbertToString(carinfo.getStrongenddate()));// 交强险结束日期
			return carinfo.getSpecifydriver();
		}else{
			carInfoMap.put("istransfer","");
			carInfoMap.put("chgOwnerDate", "");
			carInfoMap.put("carlicenseno", "");// 车牌号
			carInfoMap.put("standardfullname", "");// 车辆型号
			carInfoMap.put("engineno", "");
			carInfoMap.put("vincode", "");
			carInfoMap.put("registdate", "");
			carInfoMap.put("drivingarea", "");// 行驶区域
			carInfoMap.put("specifydriver", "");// 指定驾驶人
			carInfoMap.put("preinscode", "");//上年投保公司
			carInfoMap.put("businessstartdate","");// 商业险开始日期
			carInfoMap.put("businessenddate","");// 商业险结束日期
			carInfoMap.put("strongstartdate","");// 交强险开始日期
			carInfoMap.put("strongenddate","");// 交强险结束日期
			return null;
		}
		
	}

	private List<INSBSpecifydriverhis> selectSpecifyDriverhis(String processInstanceId,String inscomcode) {
		INSBSpecifydriverhis specifydriverhis = new INSBSpecifydriverhis();
		specifydriverhis.setTaskid(processInstanceId);
		specifydriverhis.setInscomcode(inscomcode);
		return specifydriverhisDao.selectList(specifydriverhis);
	}
	private List<INSBSpecifydriver> selectSpecifyDriver(String processInstanceId) {
		INSBSpecifydriver specifydriverhis = new INSBSpecifydriver();
		specifydriverhis.setTaskid(processInstanceId);
		return specifydriverDao.selectList(specifydriverhis);
	}

	private INSBApplicanthis selectAppicanthis(String processInstanceId, String inscomcode) {
		INSBApplicanthis applicanthisTemp = new INSBApplicanthis();
		applicanthisTemp.setTaskid(processInstanceId);
		applicanthisTemp.setInscomcode(inscomcode);
        return applicanthisDao.selectOne(applicanthisTemp);
	}

	private INSBCarmodelinfohis selectCarmodelhis(String inscomcode, String carid) {
		INSBCarmodelinfohis carmodelinfohisTemp = new INSBCarmodelinfohis();
		carmodelinfohisTemp.setCarinfoid(carid);
		carmodelinfohisTemp.setInscomcode(inscomcode);
        return carmodelinfohisDao.selectOne(carmodelinfohisTemp);
	}

	private INSBCarinfohis selectCarinfohis(String processInstanceId, String inscomcode) {
		INSBCarinfohis carinfohisTemp = new INSBCarinfohis();
		carinfohisTemp.setInscomcode(inscomcode);
		carinfohisTemp.setTaskid(processInstanceId);
        return carinfohisDao.selectCarinfohis(carinfohisTemp);
	}

	private INSBInsuredhis selectInsuredhis(String processInstanceId, String inscomcode) {
		INSBInsuredhis insuredhisTemp = new INSBInsuredhis();
		insuredhisTemp.setTaskid(processInstanceId);
		insuredhisTemp.setInscomcode(inscomcode);
        return insuredhisDao.selectOne(insuredhisTemp);
	}

	/**
	 * 修改投保基础信息接口
	 * 
	 * @param proposalInfo
	 * @return
	 */
	public String updateProposalInfo(String proposalInfo) {
		Date date = new Date();
		CommonModel model = new CommonModel();
		JSONObject body = (JSONObject) JSONObject.fromObject(proposalInfo);
		String processInstanceId = (String) body.get("processInstanceId");
		String inscomcode = (String) body.get("inscomcode");
		String operator = (String) body.get("operator");
		/****************************************************************/
//		JSONObject insuredInfo = body.getJSONObject("proposalInsured");
		INSBInsuredhis insuredhis = selectInsuredhis(processInstanceId, inscomcode);
		if(insuredhis == null){
			INSBInsured insured = insuredDao.selectInsuredByTaskId(processInstanceId);
			INSBPerson insuredPerson = personDao.selectById(insured.getPersonid());
			insuredPerson = organizeInsuredInfo(body, insuredPerson);
			insuredPerson.setId(null);
			insuredPerson.setModifytime(null);
			insuredPerson.setCreatetime(date);
			personDao.insert(insuredPerson);
			insuredhis = EntityTransformUtil.insured2Insuredhis(insured, inscomcode);
			insuredhis.setCreatetime(date);
			insuredhis.setModifytime(null);
			insuredhis.setOperator(operator);
			insuredhis.setPersonid(insuredPerson.getId());
			insuredhisDao.insert(insuredhis);
		}else{
			INSBPerson insuredPerson = personDao.selectById(insuredhis.getPersonid());
			insuredPerson = organizeInsuredInfo(body, insuredPerson);
			insuredhis.setModifytime(date);
			personDao.updateById(insuredPerson);
		}
		/****************************************************************/
		INSBInsuredhis insuredhistemp = selectInsuredhis(processInstanceId, inscomcode);
		JSONObject applicantInfo = body.getJSONObject("proposalApplicant");
		INSBApplicanthis applicanthis = selectAppicanthis(processInstanceId, inscomcode);
		if(applicantInfo.getBoolean("isSameWithInsured")){
			if(applicanthis == null){
				INSBApplicant applicant = applicantDao.selectByTaskID(processInstanceId);
				applicant.setPersonid(insuredhistemp.getPersonid());
				applicanthis = EntityTransformUtil.applicant2Applicanthis(applicant, inscomcode);
				applicanthis.setCreatetime(date);
				applicanthis.setModifytime(null);
				applicanthis.setOperator(operator);
				applicanthisDao.insert(applicanthis);
			}else{
				applicanthis.setModifytime(date);
				applicanthis.setOperator(operator);
				applicanthis.setPersonid(insuredhistemp.getPersonid());
				applicanthisDao.updateById(applicanthis);
			}
		}else{
			if(applicanthis == null){
				INSBPerson applicantPerson = new INSBPerson();
				applicantPerson.setId(null);
				applicantPerson.setTaskid(processInstanceId);
				applicantPerson.setName(applicantInfo.getString("name"));
				applicantPerson.setIdcardno(applicantInfo.getString("idcardno"));
				applicantPerson.setIdcardtype(applicantInfo.getInt("idcardtype"));
				applicantPerson.setModifytime(null);
				applicantPerson.setCreatetime(date);
				applicantPerson.setOperator(operator);
				personDao.insert(applicantPerson);
				INSBApplicant applicant = applicantDao.selectByTaskID(processInstanceId);
				applicanthis = EntityTransformUtil.applicant2Applicanthis(applicant, inscomcode);
				applicanthis.setPersonid(applicantPerson.getId());
				applicanthis.setCreatetime(date);
				applicanthis.setModifytime(null);
				applicanthis.setOperator(operator);
				applicanthisDao.insert(applicanthis);
			}else{
//				INSBInsuredhis insuredhistemp = selectInsuredhis(processInstanceId, inscomcode);
				if(!insuredhistemp.getPersonid().equals(applicanthis.getPersonid())){
					INSBPerson applicantPerson = personDao.selectById(applicanthis.getPersonid());
					applicantPerson.setModifytime(date);
					applicantPerson.setTaskid(processInstanceId);
					applicantPerson.setOperator(operator);
					applicantPerson.setName(applicantInfo.getString("name"));
					applicantPerson.setIdcardno(applicantInfo.getString("idcardno"));
					applicantPerson.setIdcardtype(applicantInfo.getInt("idcardtype"));
					personDao.updateById(applicantPerson);
				}else{
					INSBPerson applicantPerson = new INSBPerson();
					applicantPerson.setId(null);
					applicantPerson.setTaskid(processInstanceId);
					applicantPerson.setName(applicantInfo.getString("name"));
					applicantPerson.setIdcardno(applicantInfo.getString("idcardno"));
					applicantPerson.setIdcardtype(applicantInfo.getInt("idcardtype"));
					applicantPerson.setModifytime(null);
					applicantPerson.setCreatetime(date);
					applicantPerson.setOperator(operator);
					personDao.insert(applicantPerson);
				}
				//applicanthis.setPersonid(applicantPerson.getId());
				applicanthis.setModifytime(date);
				applicanthis.setOperator(operator);
				applicanthisDao.updateById(applicanthis);
			}
		}
		/*******************************************************************************/
		JSONObject carOwnerInfo = body.getJSONObject("proposalCarOwner");
		if(carOwnerInfo.getBoolean("isSameWithInsured")){
			INSBCarowneinfo carowneinfo = carowneinfoDao.selectByTaskId(processInstanceId);
			carowneinfo.setPersonid(insuredhistemp.getPersonid());
		}else{
			INSBPerson applicantPerson = new INSBPerson();
			applicantPerson.setId(null);
			applicantPerson.setTaskid(processInstanceId);
			applicantPerson.setName(carOwnerInfo.getString("name"));
			applicantPerson.setIdcardno(carOwnerInfo.getString("idcardno"));
			applicantPerson.setIdcardtype(carOwnerInfo.getInt("idcardType"));
			applicantPerson.setGender(1);
			applicantPerson.setModifytime(null);
			applicantPerson.setCreatetime(date);
			applicantPerson.setOperator(operator);
			personDao.insert(applicantPerson);
		}
		/***********************************************************************************/
		INSBCarinfohis carinfohis = selectCarinfohis(processInstanceId, inscomcode);
		INSBCarinfo carinfo;
			carinfo = organizedCarinfo(processInstanceId, body);
			if(carinfohis == null){
				carinfohis = EntityTransformUtil.carinfo2Carinfohis(carinfo, inscomcode);
				carinfohisDao.insert(carinfohis);
			}else{
				carinfohis=organizedCarinfohis(carinfohis,body);
				carinfohisDao.updateById(carinfohis);
			}
			INSBCarmodelinfohis carmodelhis = selectCarmodelhis(inscomcode, carinfo.getId());
			if(carmodelhis == null){
				INSBCarmodelinfo carmodelinfo = selectcarmodel(carinfo.getId());
				INSBCarmodelinfohis carmodelinfohis = EntityTransformUtil.carmodelinfo2Carmodelinfohis(carmodelinfo, inscomcode);
				carmodelinfohis=organizedCarmodelinfohis(carmodelinfohis,body);
				carmodelinfohisDao.insert(carmodelinfohis);
			}else{
				carmodelhis=organizedCarmodelinfohis(carmodelhis,body);
				carmodelinfohisDao.updateById(carmodelhis);
			}
		/*****************************补充信息修改****************************************/
		JSONArray array = body.getJSONArray("supplementlist");
		for (Object object : array) {
			Map<String,Object> map = (Map<String,Object>)object;
			INSBSupplementaryinfo supplement = supplementaryinfoDao.selectById((String)map.get("id"));
			supplement.setInputtype((String)map.get("inputtype"));
			supplement.setItemcode((String)map.get("itemcode"));
			supplement.setItemname((String)map.get("itemname"));
			supplement.setItemtype((String)map.get("itemtype"));
			supplement.setOptional(JSONObject.fromObject(map.get("optional")).toString());
			supplementaryinfoDao.updateById(supplement);
		}
		
		/**********************************************************************************/
		JSONObject proposalSpecifyDriver = (JSONObject) body.get("proposalSpecifyDriver");
		if(proposalSpecifyDriver.getBoolean("isSameWithInsured")){
			INSBSpecifydriverhis specifydriverhis = specifydriverhisDao.selectSpecifyDriverhis(processInstanceId,inscomcode,insuredhistemp.getPersonid());
			if(specifydriverhis == null){
				List<INSBSpecifydriver> drivers = specifydriverDao.selectSpecifyDriverByTaskid(processInstanceId);
				if(drivers!=null && drivers.size()>0){
					for (INSBSpecifydriver specifydriver : drivers) {
						INSBPerson driver = organizeDriver(specifydriver,body);
						driver.setId(null);
						personDao.insert(driver);
						INSBSpecifydriverhis driverhis = EntityTransformUtil.specifydriver2Specifydriverhis(specifydriver, inscomcode);
						driverhis.setPersonid(driver.getId());
						specifydriverhisDao.insert(driverhis);
					}
				}else{
					List<Map<String,Object>> driverList = (List<Map<String,Object>>)proposalSpecifyDriver.get("proposalDrivers");
					if(driverList!=null && driverList.size()>0){
						for (Map<String, Object> map : driverList) {
							INSBPerson driver = new INSBPerson();

								driver.setBirthday(ModelUtil.conbertStringToNyrDate((String)map.get("birthday")));
								driver.setLicensedate(ModelUtil.conbertStringToNyrDate((String)map.get("licensedate")));

							driver.setGender((int)map.get("gender"));
							driver.setLicenseno((String)map.get("licenseno"));
							driver.setLicensetype((String)map.get("licensetype"));
							driver.setName((String)map.get("name"));
							personDao.insert(driver);
							specifydriverhis = new INSBSpecifydriverhis();
							specifydriverhis.setPersonid(driver.getId());
							specifydriverhisDao.insert(specifydriverhis);
						}
					}
				}
			}else {
				specifydriverhis.setPersonid(insuredhistemp.getPersonid());
				specifydriverhisDao.updateById(specifydriverhis);
			}
		}else {
			INSBSpecifydriverhis specifydriverhis = specifydriverhisDao.selectById(carinfohis.getSpecifydriver());
			if(specifydriverhis == null){
				List<Map<String,Object>> driverList = (List<Map<String,Object>>)proposalSpecifyDriver.get("proposalDrivers");
				if(driverList!=null && driverList.size()>0){
					for (Map<String, Object> map : driverList) {
						INSBPerson driver = new INSBPerson();

							driver.setBirthday(ModelUtil.conbertStringToNyrDate((String)map.get("birthday")));
							driver.setLicensedate(ModelUtil.conbertStringToNyrDate((String)map.get("licensedate")));

						driver.setGender((int)map.get("gender"));
						driver.setLicenseno((String)map.get("licenseno"));
						driver.setLicensetype((String)map.get("licensetype"));
						driver.setName((String)map.get("name"));
						personDao.insert(driver);
						specifydriverhis = new INSBSpecifydriverhis();
						specifydriverhis.setPersonid(driver.getId());
						specifydriverhisDao.insert(specifydriverhis);
					}
				}
			}else{
				if(!insuredhistemp.getPersonid().equals(specifydriverhis.getPersonid())){
					List<Map<String,Object>> driverList = (List<Map<String,Object>>)proposalSpecifyDriver.get("proposalDrivers");
					if(driverList!=null && driverList.size()>0){
						for (Map<String, Object> map : driverList) {
							INSBPerson driver = new INSBPerson();

								driver.setBirthday(ModelUtil.conbertStringToNyrDate((String)map.get("birthday")));
								driver.setLicensedate(ModelUtil.conbertStringToNyrDate((String)map.get("licensedate")));

							driver.setGender((int)map.get("gender"));
							driver.setLicenseno((String)map.get("licenseno"));
							driver.setLicensetype((String)map.get("licensetype"));
							driver.setName((String)map.get("name"));
							personDao.insert(driver);
							specifydriverhis = new INSBSpecifydriverhis();
							specifydriverhis.setPersonid(driver.getId());
							specifydriverhisDao.insert(specifydriverhis);
						}
					}
				}else{
					List<Map<String,Object>> driverList = (List<Map<String,Object>>)proposalSpecifyDriver.get("proposalDrivers");
					if(driverList!=null && driverList.size()>0){

							for (Map<String, Object> map : driverList) {
								INSBPerson driver = null;
								if(specifydriverhis.getPersonid().equals(map.get("id"))){
									driver = personDao.selectById(specifydriverhis.getPersonid());
										driver.setBirthday(ModelUtil.conbertStringToNyrDate((String)map.get("birthday")));
									driver.setLicensedate(ModelUtil.conbertStringToNyrDate((String)map.get("licensedate")));
									driver.setGender((int)map.get("gender"));
									driver.setLicenseno((String)map.get("licenseno"));
									driver.setLicensetype((String)map.get("licensetype"));
									driver.setName((String)map.get("name"));
									personDao.updateById(driver);
								}else if("".equals(map.get("id"))||map.get("id")==null){
									driver = new INSBPerson();
									driver.setBirthday(ModelUtil.conbertStringToNyrDate((String)map.get("birthday")));
									driver.setLicensedate(ModelUtil.conbertStringToNyrDate((String)map.get("licensedate")));
									driver.setGender((int)map.get("gender"));
									driver.setLicenseno((String)map.get("licenseno"));
									driver.setLicensetype((String)map.get("licensetype"));
									driver.setName((String)map.get("name"));
									personDao.insert(driver);
								}
								specifydriverhis.setPersonid(driver.getId());
								specifydriverhisDao.updateById(specifydriverhis);
							}

					}
				}
			}
		}
		model.setStatus("success");
		model.setMessage("修改投保信息成功");
		System.out.println(JSONObject.fromObject(model).toString());
		return JSONObject.fromObject(model).toString();
	}


	private INSBPerson organizeDriver(INSBSpecifydriver specifydriver, JSONObject body){
		INSBPerson driver = personDao.selectById(specifydriver.getPersonid());
		JSONObject jsonobj = (JSONObject)body.get("proposalSpecifyDriver");
		List<Map<String,Object>> driverList = (List<Map<String,Object>>)jsonobj.get("proposalDrivers");
		if(driverList!=null && driverList.size()>0){
			for (Map<String, Object> map : driverList) {
				if(driver.getId().equals(map.get("id"))){

						driver.setBirthday(ModelUtil.conbertStringToNyrDate((String)map.get("birthday")));
						driver.setLicensedate(ModelUtil.conbertStringToNyrDate((String)map.get("licensedate")));
						driver.setGender((int)map.get("gender"));
						driver.setLicenseno((String)map.get("licenseno"));
						driver.setLicensetype((String)map.get("licensetype"));
						driver.setName((String)map.get("name"));

				}
			}
		}
		return driver;
	}

	private INSBCarmodelinfohis organizedCarmodelinfohis(INSBCarmodelinfohis carmodelinfohis, JSONObject body) {
		JSONObject proposalCarinfo =(JSONObject) body.get("proposalCarinfo");
		carmodelinfohis.setBrandname(proposalCarinfo.getString("brandname"));
		carmodelinfohis.setFullweight(proposalCarinfo.getDouble("fullweight"));
		carmodelinfohis.setSeat(proposalCarinfo.getInt("seat"));
		carmodelinfohis.setStandardfullname(proposalCarinfo.getString("standardfullname"));
		carmodelinfohis.setUnwrtweight(proposalCarinfo.getDouble("unwrtweight"));
		return carmodelinfohis;
	}

	private INSBCarinfohis organizedCarinfohis(INSBCarinfohis carinfohis, JSONObject body){
		JSONObject proposalCarinfo =(JSONObject) body.get("proposalCarinfo");

			carinfohis.setBusinessstartdate(ModelUtil.conbertStringToNyrDate(proposalCarinfo.getString("businessstartdate")));
			carinfohis.setBusinessenddate(ModelUtil.conbertStringToNyrDate(proposalCarinfo.getString("businessenddate")));
			carinfohis.setStrongstartdate(ModelUtil.conbertStringToNyrDate(proposalCarinfo.getString("strongstartdate")));
			carinfohis.setStrongenddate(ModelUtil.conbertStringToNyrDate(proposalCarinfo.getString("strongenddate")));
			carinfohis.setRegistdate(ModelUtil.conbertStringToNyrDate(proposalCarinfo.getString("registdate")));
			carinfohis.setSpecifydriver(proposalCarinfo.getString("specifydriver"));
			carinfohis.setCarlicenseno(proposalCarinfo.getString("carlicenseno"));
			carinfohis.setDrivingarea(proposalCarinfo.getString("drivingarea"));
			carinfohis.setEngineno(proposalCarinfo.getString("engineno"));
			carinfohis.setPreinscode(proposalCarinfo.getString("preinscode"));
			carinfohis.setVincode(proposalCarinfo.getString("vincode"));

		return carinfohis;
	}

	private INSBCarinfo organizedCarinfo(String processInstanceId,JSONObject body){
		INSBCarinfo carinfo = carinfoDao.selectCarinfoByTaskId(processInstanceId);
		JSONObject proposalCarinfo =(JSONObject) body.get("proposalCarinfo");

			carinfo.setBusinessstartdate(ModelUtil.conbertStringToNyrDate(proposalCarinfo.getString("businessstartdate")));
			carinfo.setBusinessenddate(ModelUtil.conbertStringToNyrDate(proposalCarinfo.getString("businessenddate")));
			carinfo.setStrongstartdate(ModelUtil.conbertStringToNyrDate(proposalCarinfo.getString("strongstartdate")));
			carinfo.setStrongenddate(ModelUtil.conbertStringToNyrDate(proposalCarinfo.getString("strongenddate")));
			carinfo.setRegistdate(ModelUtil.conbertStringToNyrDate(proposalCarinfo.getString("registdate")));
			carinfo.setTransferdate(ModelUtil.conbertStringToNyrDate(proposalCarinfo.getString("chgOwnerDate")));
			carinfo.setCarlicenseno(proposalCarinfo.getString("carlicenseno"));
			carinfo.setIsTransfercar(proposalCarinfo.getString("istransfer"));
			carinfo.setDrivingarea(proposalCarinfo.getString("drivingarea"));
			carinfo.setEngineno(proposalCarinfo.getString("engineno"));
			carinfo.setPreinscode(proposalCarinfo.getString("preinscode"));
			carinfo.setVincode(proposalCarinfo.getString("vincode"));

//		carinfo.setstandardfullname(proposalCarinfo.getString("standardfullname"));
//		carinfo.setful(proposalCarinfo.getInt("fullweight"));
		return carinfo;
	}

	private INSBCarmodelinfo selectcarmodel(String carmodelid) {
		if(carmodelid != null){
			INSBCarmodelinfo carmodel = new INSBCarmodelinfo();
			carmodel.setCarinfoid(carmodelid);
			return carmodelinfoDao.selectOne(carmodel);
		}else{
			return null;
		}
	}

	private INSBPerson organizeInsuredInfo(JSONObject body,
			INSBPerson person) {
		JSONObject insuredInfo = (JSONObject) body.get("proposalInsured");
		person.setName(insuredInfo.getString("name"));
		person.setGender(insuredInfo.getInt("gender"));
		person.setEmail(insuredInfo.getString("email"));
		person.setIdcardno(insuredInfo.getString("idcardno"));
		person.setIdcardtype(insuredInfo.getInt("idcardtype"));
		person.setCellphone(insuredInfo.getString("cellphone"));
		return person;
	}

	/*

	public String getYPayInfo(String MSM_PARAM) {
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> bankList = new ArrayList<String>();
		bankList.add("农业银行");
		bankList.add("招商银行");
		bankList.add("工商银行");
		resultMap.put("sumfee", "保费");
		resultMap.put("deliveryfee", "快递费");
		resultMap.put("sumfee", "支付总计");
		resultMap.put("bankList", bankList);
		bodyMap.put("body", resultMap);
		System.out.println(JSONObject.fromObject(bodyMap).toString());
		return JSONObject.fromObject(bodyMap).toString();
	}

	public String getPayInfo(String MSM_PARAM) {
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("isSign", "1-已签约，0-未签约");
		resultMap.put("name", "张忠宝");
		resultMap.put("bankcard", "银行卡号");
		bodyMap.put("body", resultMap);

		System.out.println(JSONObject.fromObject(bodyMap).toString());
		return JSONObject.fromObject(bodyMap).toString();
	}
*/
}