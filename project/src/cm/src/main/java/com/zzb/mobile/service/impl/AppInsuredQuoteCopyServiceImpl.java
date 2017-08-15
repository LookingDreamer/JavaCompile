package com.zzb.mobile.service.impl;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.common.*;
import com.lzgapi.order.service.LzgOrderService;
import com.zzb.app.model.bean.RisksData;
import com.zzb.app.model.bean.SelectOption;
import com.zzb.app.service.AppQuotationService;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.INSBOrdernumberService;
import com.zzb.cm.service.INSBQuoteVerifyService;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.*;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBChannelService;
import com.zzb.conf.service.INSBTasksetService;
import com.zzb.extra.dao.INSBAgentTaskDao;
import com.zzb.extra.entity.INSBAgentTask;
import com.zzb.extra.model.SearchProviderModelForMinizzb;
import com.zzb.extra.model.SelectProviderBeanForMinizzb;
import com.zzb.mobile.CheckAddQuoteProviderRoleModel;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.*;
import com.zzb.mobile.model.lastinsured.*;
import com.zzb.mobile.model.lastinsured.LastYearClaimBean;
import com.zzb.mobile.service.AppInsuredQuoteCopyService;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.AppLoginService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

@Service
@Transactional
public class AppInsuredQuoteCopyServiceImpl implements AppInsuredQuoteCopyService {
	@Resource
	private INSBCarowneinfoDao insbCarowneinfoDao;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBCarinfohisDao insbCarinfohisDao;
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	private INSBCarconfigDao insbCarconfigDao;
	@Resource
	private INSBCarkindpriceDao insbCarkindpriceDao;
	@Resource
	private INSBApplicantDao insbApplicantDao;
	@Resource
	private INSBInsuredDao insbInsuredDao;
	@Resource
	private INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	private INSBApplicanthisDao insbApplicanthisDao;

	@Resource
	private INSBLegalrightclaimDao insbLegalrightclaimDao;
	@Resource
	private INSBRelationpersonDao insbRelationpersonDao;
	@Resource
	private INSBLegalrightclaimhisDao insbLegalrightclaimhisDao;
	@Resource
	private INSBRelationpersonhisDao insbRelationpersonhisDao;

	@Resource
	private  INSBFilebusinessDao insbFilebusinessDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBWorkflowmaintrackDao insbWorkflowmaintrackDao;
	@Resource
	private INSBUsercommentDao insbUsercommentDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	/**
	 * task-122 按照任务号，提供该单所有数据接口，供前端使用
	 * 将该报价的车辆信息、关系人信息、投保公司、保险配置复制，同时跳转到车险投保首页，将这些信息带入到新的报价流程中。
	 * @param srcTaskId
	 * @param inscomcode
	 * @return
	 */
	@Override
	public CommonModel copy(String srcTaskId, String inscomcode) {
		CommonModel commonModel = new CommonModel();
		Map<String, Object> body = new HashMap<String, Object>();
		try {
			if (StringUtil.isEmpty(srcTaskId)) {
				commonModel.setBody(null);
				commonModel.setStatus("fail");
				commonModel.setMessage("实例id不能为空");
				return commonModel;
			}
			if (StringUtil.isEmpty(inscomcode)) {
				commonModel.setBody(null);
				commonModel.setStatus("fail");
				commonModel.setMessage("保险公司不能为空");
				return commonModel;
			}

			// 保存车主信息
			INSBPerson insbPerson = insbPersonDao.selectCarOwnerPersonByTaskId(srcTaskId);

			body.put("carowneinfo", insbPerson);

			INSBInsuredhis insuredh = new INSBInsuredhis();
			insuredh.setTaskid(srcTaskId);
			insuredh.setInscomcode(inscomcode);
			insuredh= insbInsuredhisDao.selectOne(insuredh);
			if(insuredh != null) {
				INSBPerson insuredPerson = insbPersonDao.selectById(insuredh.getPersonid());
				body.put("insbInsured", insuredPerson);
				if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getId())
						&& StringUtil.isNotEmpty(insuredh.getPersonid())) {
					body.put("insuredSame", insbPerson.getId().equals(insuredh.getPersonid()));
				}
			} else {
				INSBInsured insured = new INSBInsured();
				insured.setTaskid(srcTaskId);
				//被保人
				INSBInsured insbInsured2 = insbInsuredDao.selectOne(insured);

				if(insbInsured2 != null) {
					INSBPerson insuredPerson = insbPersonDao.selectById(insbInsured2.getPersonid());
					body.put("insbInsured", insuredPerson);
					if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getId())
							&& StringUtil.isNotEmpty(insbInsured2.getPersonid())) {
						body.put("insuredSame", insbPerson.getId().equals(insbInsured2.getPersonid()));
					}
				}
			}

			//投保人
			INSBApplicanthis applicanthis = new INSBApplicanthis();
			applicanthis.setTaskid(srcTaskId);
			applicanthis.setInscomcode(inscomcode);
			applicanthis =insbApplicanthisDao.selectOne(applicanthis);
			if(applicanthis != null) {
				INSBPerson insbApplicant = insbPersonDao.selectById(applicanthis.getPersonid());
				if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getId())
						&& StringUtil.isNotEmpty(applicanthis.getPersonid())) {
					body.put("applicantSame", insbPerson.getId().equals(applicanthis.getPersonid()));
				}
				body.put("insbApplicant", insbApplicant);
			} else {
				INSBApplicant applicant = new INSBApplicant();
				applicant.setTaskid(srcTaskId);
				INSBApplicant insbApplicant2 = insbApplicantDao.selectOne(applicant);
				if(insbApplicant2 != null) {
					INSBPerson insbApplicant = insbPersonDao.selectById(insbApplicant2.getPersonid());
					if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getId())
							&& StringUtil.isNotEmpty(insbApplicant2.getPersonid())) {
						body.put("applicantSame", insbPerson.getId().equals(insbApplicant2.getPersonid()));
					}
					body.put("insbApplicant", insbApplicant);
				}
			}


			//权益索赔人
			INSBLegalrightclaimhis insbLegalrightclaimhis = new INSBLegalrightclaimhis();
			insbLegalrightclaimhis.setTaskid(srcTaskId);
			insbLegalrightclaimhis.setInscomcode(inscomcode);
			insbLegalrightclaimhis = insbLegalrightclaimhisDao.selectOne(insbLegalrightclaimhis);
			if(insbLegalrightclaimhis != null) {
				body.put("legalrightclaim", insbPersonDao.selectById(insbLegalrightclaimhis.getPersonid()));
			} else {
				INSBLegalrightclaim insbLegalrightclaim = new INSBLegalrightclaim();
				insbLegalrightclaim.setTaskid(srcTaskId);
				INSBLegalrightclaim legalrightclaim = insbLegalrightclaimDao.selectOne(insbLegalrightclaim);

				if(legalrightclaim != null) {
					body.put("legalrightclaim", insbPersonDao.selectById(legalrightclaim.getPersonid()));
				}
			}

			//联系人
			INSBRelationpersonhis insbRelationpersonhis = new INSBRelationpersonhis();
			insbRelationpersonhis.setTaskid(srcTaskId);
			insbRelationpersonhis.setInscomcode(inscomcode);
			insbRelationpersonhis = insbRelationpersonhisDao.selectOne(insbRelationpersonhis);

			if(insbRelationpersonhis != null) {
				body.put("relationperson", insbPersonDao.selectById(insbRelationpersonhis.getPersonid()));
			} else {
				INSBRelationperson insbRelationperson = new INSBRelationperson();
				insbRelationperson.setTaskid(srcTaskId);
				INSBRelationperson relationperson = insbRelationpersonDao.selectOne(insbRelationperson);

				if(relationperson != null) {
					body.put("relationperson", insbPersonDao.selectById(relationperson.getPersonid()));
				}
			}

			// 向车辆信息表插入车辆信息
			INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(srcTaskId);

			body.put("car", insbCarinfo);

			body.put("insbCarinfo", getSaveCarInfoModel(srcTaskId, inscomcode));

			body.put("carmodelinfo", getCarModelInfoModel(srcTaskId));

			if(null == insbCarinfo){
				commonModel.setStatus("fail");
				commonModel.setMessage("车辆信息不存在");
				return commonModel;
			}

			INSBCarmodelinfo insbCarmodelinfo = new INSBCarmodelinfo();
			insbCarmodelinfo.setCarinfoid(insbCarinfo.getId());
			INSBCarmodelinfo carmodelinfo = insbCarmodelinfoDao.selectOne(insbCarmodelinfo);
			body.put("selectCarmodel", carmodelinfo);


			//险别保险配置
			INSBCarconfig carconfig = new INSBCarconfig();
			carconfig.setTaskid(srcTaskId);
			List<INSBCarconfig> carconfigs = insbCarconfigDao.selectList(carconfig);
			//body.put("insbCarconfigs", carconfigs);

			INSBPolicyitem allpolicys = new INSBPolicyitem();
			allpolicys.setTaskid(srcTaskId);
			allpolicys.setInscomcode(inscomcode);
			List<INSBPolicyitem> re = insbPolicyitemDao.selectList(allpolicys);
			body.put("Policyitems", re);


			INSBQuotetotalinfo totalparams = new INSBQuotetotalinfo();
			totalparams.setTaskid(srcTaskId);
			INSBQuotetotalinfo total = insbQuotetotalinfoDao.selectOne(totalparams);
			if (null == total) {
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}

			INSBCarkindprice insureCarkindprice = new INSBCarkindprice();
			insureCarkindprice.setTaskid(srcTaskId);
			insureCarkindprice.setInscomcode(inscomcode);
			List<INSBCarkindprice> carkindpricesList = insbCarkindpriceDao.selectList(insureCarkindprice);
			body.put("insbCarkindprices", carkindpricesList);

			//影像
			INSBFilebusiness filebusiness = new INSBFilebusiness();
			filebusiness.setCode(srcTaskId);
			List<INSBFilebusiness> filebusinesses = insbFilebusinessDao.selectList(filebusiness);
			body.put("filebusinesses", filebusinesses);

			//comment
			INSBWorkflowmaintrack insbWorkflowmaintrack = new INSBWorkflowmaintrack();
			insbWorkflowmaintrack.setInstanceid(srcTaskId);
			insbWorkflowmaintrack.setTaskcode("1");//1	前端提交报价	信息录入
			List<INSBWorkflowmaintrack> insbWorkflowmaintracks = insbWorkflowmaintrackDao.selectList(insbWorkflowmaintrack);
			if(null != insbWorkflowmaintracks && insbWorkflowmaintracks.size() > 0){
				INSBWorkflowmaintrack workflowmaintrack = insbWorkflowmaintracks.get(0);
				INSBUsercomment insbUsercomment = new INSBUsercomment();
				insbUsercomment.setTrackid(workflowmaintrack.getId());
				insbUsercomment.setTracktype(1);
				insbUsercomment.setCommenttype(1);
				List<INSBUsercomment> list = insbUsercommentDao.selectList(insbUsercomment);
				if(!list.isEmpty()) {
					body.put("comment", list.get(0).getCommentcontenttype() == 0 ? "": list.get(0));
				}
			}
			
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
			commonModel.setBody(body);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
			commonModel.setBody(null);
		}
		return commonModel;
	}

	private SaveCarInfoModel getSaveCarInfoModel(String processinstanceid, String inscomcode){
		SaveCarInfoModel carInfoModel = new SaveCarInfoModel();
		//TODO 查询车辆信息封装 返回
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(processinstanceid);

		INSBCarinfohis carinfohis = new INSBCarinfohis();
		carinfohis.setTaskid(processinstanceid);
		carinfohis.setInscomcode(inscomcode);
		List<INSBCarinfohis> hisList = insbCarinfohisDao.selectList(carinfohis);
		if (!hisList.isEmpty()) {
			INSBCarinfohis carinfo = hisList.get(0);
			if(null != carinfo){
				carInfoModel.setProcessinstanceid(processinstanceid); //实例id
				if(StringUtil.isEmpty(carinfo.getRegistdate())){
					carInfoModel.setFirstRegisterDate(""); //车辆初登日期
				}else{
					carInfoModel.setFirstRegisterDate(ModelUtil.conbertToString(carinfo.getRegistdate())); //车辆初登日期
				}
				carInfoModel.setVin(StringUtil.isEmpty(carinfo.getVincode())?"":ModelUtil.hiddenVin(carinfo.getVincode())); //车辆识别代号
				carInfoModel.setEngineNo(StringUtil.isEmpty(carinfo.getEngineno())?"":ModelUtil.hiddenEngineNo(carinfo.getEngineno())); //发动机号
				carInfoModel.setTempVin(StringUtil.isEmpty(carinfo.getVincode())?"":carinfo.getVincode()); //车辆识别代号
				carInfoModel.setTempEngineNo(StringUtil.isEmpty(carinfo.getEngineno())?"":carinfo.getEngineno()); //发动机号
				carInfoModel.setModelCode(carinfo.getStandardfullname());//品牌型号
				carInfoModel.setChgOwnerFlag(carinfo.getIsTransfercar()); //是否过户车
				if(StringUtil.isEmpty(carinfo.getTransferdate())){
					carInfoModel.setChgOwnerDate(""); //车辆过户日期
				}else{
					carInfoModel.setChgOwnerDate(ModelUtil.conbertToString(carinfo.getTransferdate())); //车辆过户日期
				}
			}
			return carInfoModel;
		}

		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo); //车辆
		if(null != carinfo){
			carInfoModel.setProcessinstanceid(processinstanceid); //实例id
			if(StringUtil.isEmpty(carinfo.getRegistdate())){
				carInfoModel.setFirstRegisterDate(""); //车辆初登日期
			}else{
				carInfoModel.setFirstRegisterDate(ModelUtil.conbertToString(carinfo.getRegistdate())); //车辆初登日期
			}
			carInfoModel.setVin(StringUtil.isEmpty(carinfo.getVincode())?"":ModelUtil.hiddenVin(carinfo.getVincode())); //车辆识别代号
			carInfoModel.setEngineNo(StringUtil.isEmpty(carinfo.getEngineno())?"":ModelUtil.hiddenEngineNo(carinfo.getEngineno())); //发动机号
			carInfoModel.setTempVin(StringUtil.isEmpty(carinfo.getVincode())?"":carinfo.getVincode()); //车辆识别代号
			carInfoModel.setTempEngineNo(StringUtil.isEmpty(carinfo.getEngineno())?"":carinfo.getEngineno()); //发动机号
			carInfoModel.setModelCode(carinfo.getStandardfullname());//品牌型号
			carInfoModel.setChgOwnerFlag(carinfo.getIsTransfercar()); //是否过户车
			if(StringUtil.isEmpty(carinfo.getTransferdate())){
				carInfoModel.setChgOwnerDate(""); //车辆过户日期
			}else{
				carInfoModel.setChgOwnerDate(ModelUtil.conbertToString(carinfo.getTransferdate())); //车辆过户日期
			}
		}
		return carInfoModel;

	}

	/**
	 * 车型信息页面
	 * @param processinstanceid
	 * @return
	 */
	private CarModelInfoModel getCarModelInfoModel(String processinstanceid){
		CarModelInfoModel carModelInfo = new CarModelInfoModel();
		//TODO 查询车型息封装 返回
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(processinstanceid);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo); //车辆
		if(null != carinfo){
			INSBCarmodelinfo carModel = insbCarmodelinfoDao.selectByCarinfoId(carinfo.getId()); //车型
			if(null != carModel){
				carModelInfo.setProcessinstanceid(processinstanceid);//实例id
				carModelInfo.setModelCode(carModel.getStandardname());//品牌型号
				carModelInfo.setDisplacement(carModel.getDisplacement());//排量
				carModelInfo.setApprovedLoad(carModel.getSeat());//核载人数
				carModelInfo.setRulePriceProvideType(carModel.getCarprice());//车价选择
				carModelInfo.setInstitutionType(carinfo.getProperty());//所属性质：个人用车，企业用车，机关团体用车
				carModelInfo.setUseProperty(carinfo.getCarproperty()); //车辆性质，试用性质
				carModelInfo.setTonnage(carModel.getUnwrtweight()); //核定载质量
				carModelInfo.setWholeWeight(carModel.getFullweight());//整备质量
				carModelInfo.setCustomPrice(carModel.getPolicycarprice());//自定义车价
			}
		}

		return carModelInfo;
	}
}
 