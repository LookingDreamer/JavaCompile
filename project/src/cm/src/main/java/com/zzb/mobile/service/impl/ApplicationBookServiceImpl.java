package com.zzb.mobile.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.cm.dao.INSBApplicanthisDao;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBCarowneinfoDao;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBLegalrightclaimhisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBSpecialkindconfigDao;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBLegalrightclaimhis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBSpecialkindconfig;
import com.zzb.conf.dao.INSBOrderdeliveryDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBRiskkindconfigDao;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRiskkindconfig;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.ApplicationBookService;

/*
 * 投保书信息
 */
@Service
@Transactional
public class ApplicationBookServiceImpl extends BaseServiceImpl<INSBPolicyitem>implements ApplicationBookService{
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBCarinfoDao carinfoDao;
	@Resource
	private INSBCarinfohisDao carinfohisDao;
	@Resource 
	private INSBCarkindpriceDao carkindpriceDao;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBCarowneinfoDao carowneinfoDao;
	@Resource
	private INSBApplicanthisDao applicanthisDao;
	@Resource
	private INSBLegalrightclaimhisDao insbLegalrightclaimhisDao;
	@Resource
	private INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	private INSBRiskkindconfigDao insbRiskkindconfigDao;
	@Resource
	private INSBCarmodelinfoDao carmodelinfoDao;
	@Resource
	private INSBCarmodelinfohisDao carmodelinfohisDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBOrderdeliveryDao insbOrderdeliveryDao;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSBSpecialkindconfigDao insbSpecialkindconfigDao;
	@Override
	protected BaseDao<INSBPolicyitem> getBaseDao() {
		return insbPolicyitemDao;
	}
	@Override
	public String applicationBoolSource(String taskid, String risktype,String prvid) {
		Map<String,Object>allOfItem=new HashMap<String, Object>();
		INSBPerson insbPerson=new INSBPerson();
		//INSBPolicyitem对象内容
		Map<String,Object> policyitemKey=new HashMap<String, Object>();
		List<Map<String,Object>>newEqupmentRisk=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>>newEqupmentRisks=new ArrayList<Map<String,Object>>();
		Map<String,Object>newEqupmentRiskMap=null;
		Map<String,Object>newEqupmentRiskMaps=null;
		INSBPolicyitem insbPolicyitems=new INSBPolicyitem();
		insbPolicyitems.setTaskid(taskid);
		insbPolicyitems.setRisktype(risktype);
		insbPolicyitems.setInscomcode(prvid);
		List<INSBPolicyitem>policyItemList=insbPolicyitemDao.selectList(insbPolicyitems);
		INSBOrderdelivery queryInsbOrderdelivery = new INSBOrderdelivery();
		queryInsbOrderdelivery.setTaskid(taskid);
		queryInsbOrderdelivery.setProviderid(prvid);
		INSBOrderdelivery insbOrderdelivery=insbOrderdeliveryDao.selectOne(queryInsbOrderdelivery);
		String delivename=null;
		if(insbOrderdelivery!=null){
			if(insbOrderdelivery.getDelivetype()==null||insbOrderdelivery.getDelivetype().isEmpty()){
				delivename="未选择";
			}else{
				delivename=inscCodeService.transferValueToName("DeliveType", "DeliveType", insbOrderdelivery.getDelivetype());
			}
		}else{
			delivename="未选择";
		}
		INSBOrder queryInsbOrder = new INSBOrder();
		queryInsbOrder.setTaskid(taskid);
		queryInsbOrder.setPrvid(prvid);
		INSBOrder insbOrder=insbOrderDao.selectOne(queryInsbOrder);
		INSBProvider insbProvider=new INSBProvider();
		insbProvider.setPrvcode(insbOrder.getPrvid());
		insbProvider=insbProviderDao.selectOne(insbProvider);
		for(INSBPolicyitem insbPolicyitem:policyItemList){
			//policyitemKey=new HashMap<String, Object>();
			if(insbProvider==null||insbProvider.getPrvshotname().isEmpty()){
				policyitemKey.put("comName", "");
			}else{
				policyitemKey.put("comName", insbProvider.getPrvshotname());
			}
			policyitemKey.put("Startdate",insbPolicyitem.getStartdate());
			policyitemKey.put("Enddate", insbPolicyitem.getEnddate());
			policyitemKey.put("Risktype", insbPolicyitem.getRisktype());
			//policyitemKey.put("Insuredname", insbPolicyitem.getInsuredname());
			policyitemKey.put("Policyno", insbPolicyitem.getPolicyno());
			policyitemKey.put("Netcom", delivename);
		}
		allOfItem.put("policyitemKey", policyitemKey);
		//车辆信息内容
		//TODO 投保书
		Map<String,Object> carInfoKey=new HashMap<String, Object>();
		INSBCarinfohis carinfohis=this.getCarinfohis(taskid, prvid);
		INSBCarinfo carInfo=carinfoDao.selectCarinfoByTaskId(taskid);
		INSBCarowneinfo carowneinfo= this.getcarowneinfo(taskid);
		if(carinfohis!=null){
			insbPerson=insbPersonDao.selectById(carowneinfo.getPersonid());
			carInfoKey.put("Carlicenseno", carinfohis.getCarlicenseno());
			carInfoKey.put("Carid", carinfohis.getCarid());
			carInfoKey.put("Ownername", insbPerson.getName());
			carInfoKey.put("Engineno", carinfohis.getEngineno());
			carInfoKey.put("Vincode", carinfohis.getVincode());
			String drivAreaname=inscCodeService.transferValueToName("CarDrivingArea", "CarDrivingArea", carinfohis.getDrivingarea());
			carInfoKey.put("Drivingarea", drivAreaname);
			carInfoKey.put("Mileage", carinfohis.getMileage());
			carInfoKey.put("Registdate",carinfohis.getRegistdate());
			carInfoKey.put("Property",carinfohis.getProperty());//所属性质
			carInfoKey.put("Carproperty",carinfohis.getCarproperty());//车辆性质
			allOfItem.put("carInfoKey",carInfoKey);
		}else if(carinfohis==null){
			carInfoKey.put("Carlicenseno", carInfo.getCarlicenseno());
			carInfoKey.put("Carid", carInfo.getCarid());
			carInfoKey.put("Ownername", carInfo.getOwnername());
			carInfoKey.put("Engineno", carInfo.getEngineno());
			carInfoKey.put("Vincode", carInfo.getVincode());
			String drivAreaname=inscCodeService.transferValueToName("CarDrivingArea", "CarDrivingArea", carInfo.getDrivingarea());
			carInfoKey.put("Drivingarea", drivAreaname);
			carInfoKey.put("Mileage", carInfo.getMileage());
			carInfoKey.put("Registdate",carInfo.getRegistdate());
			carInfoKey.put("Property",carInfo.getProperty());//所属性质
			carInfoKey.put("Carproperty",carInfo.getCarproperty());//车辆性质
			allOfItem.put("carInfoKey",carInfoKey);
		}
		INSBCarmodelinfohis carmodelinfohis=this.getCarmodelinfohis(carInfo.getId(),prvid);
		if(carmodelinfohis!=null){
			carInfoKey.put("RatedPassengerCapacity",carmodelinfohis.getSeat()==null?"5":carmodelinfohis.getSeat());
			carInfoKey.put("Fullweight",carmodelinfohis.getFullweight());
			carInfoKey.put("Unwrtweight",carmodelinfohis.getUnwrtweight());
			carInfoKey.put("Cardeploy",carmodelinfohis.getCardeploy());
			carInfoKey.put("Brandname",carmodelinfohis.getStandardname());
			carInfoKey.put("Displacement", carmodelinfohis.getDisplacement());//排气量
		}else if(carmodelinfohis==null){
			INSBCarmodelinfo carmodelinfo=new INSBCarmodelinfo();
			carmodelinfo.setCarinfoid(carInfo.getId());
			carmodelinfo= carmodelinfoDao.selectOne(carmodelinfo);
			carInfoKey.put("RatedPassengerCapacity",carmodelinfo.getSeat()==null?"5":carmodelinfo.getSeat());
			carInfoKey.put("Fullweight",carmodelinfo.getFullweight());
			carInfoKey.put("Unwrtweight",carmodelinfo.getUnwrtweight());
			carInfoKey.put("Cardeploy",carmodelinfo.getCardeploy());
			carInfoKey.put("Brandname",carmodelinfo.getStandardname());
			carInfoKey.put("Displacement", carmodelinfo.getDisplacement());//排气量
		}
			//商业险计划
			Map<String,Object> policyitem=new HashMap<String, Object>();
			policyitem.put("taskId", taskid);
			policyitem.put("inscomcode", insbOrder.getPrvid());
			List<INSBCarkindprice> carkindList= carkindpriceDao.selectByTaskidAndInscomcode(policyitem);
			Set<Object>price=new HashSet<Object>();
			Map<String,Object> carkindMap=null;
			INSBRiskkindconfig insbRiskkindconfig=null;
			//报价信息,主要看kindtype,risktype
			double amountSum=0;
            for (INSBCarkindprice aCarKindList : carkindList) {
                insbRiskkindconfig = new INSBRiskkindconfig();
                String insKindType = aCarKindList.getInskindtype();
                if (risktype.equals("0")) {
                    if (aCarKindList.getDiscountCharge() != null) {
                        if (insKindType.equals("0") && aCarKindList.getDiscountCharge() >= 0
                                && !aCarKindList.getInskindcode().endsWith("NewEquipmentIns") && !aCarKindList.getInskindcode().endsWith("CompensationDuringRepairIns")) {
                            carkindMap = new HashMap<>();
                            JSONArray itemArray = JSONArray.fromObject(aCarKindList.getSelecteditem());
							if (itemArray != null && !itemArray.isEmpty()) {
								JSONObject item = itemArray.getJSONObject(0);
								JSONObject value = item.getJSONObject("VALUE");
								carkindMap.put("Amount", "01".equals(item.getString("TYPE")) ? aCarKindList.getAmount() : value.get("KEY"));
							}
                            amountSum += aCarKindList.getDiscountCharge();
                            insbRiskkindconfig.setRiskkindcode(aCarKindList.getInskindcode());
                            insbRiskkindconfig = insbRiskkindconfigDao.selectOne(insbRiskkindconfig);
                            carkindMap.put("Inskindcode", insbRiskkindconfig.getShortname());
                            carkindMap.put("Amountprice", aCarKindList.getDiscountCharge());
                        } else if (insKindType.equals("1") && aCarKindList.getDiscountCharge() >= 0) {
                            carkindMap = new HashMap<>();
                            carkindMap.put("Amount", "投保");
                            amountSum += aCarKindList.getDiscountCharge();
                            insbRiskkindconfig.setRiskkindcode(aCarKindList.getInskindcode());
                            insbRiskkindconfig = insbRiskkindconfigDao.selectOne(insbRiskkindconfig);
                            carkindMap.put("Inskindcode", insbRiskkindconfig.getShortname());
                            carkindMap.put("Amountprice", aCarKindList.getDiscountCharge());
                        }
                    }
                } else if (risktype.equals("1")) {
                    if ((insKindType.equals("2") || insKindType.equals("3")) && aCarKindList.getDiscountCharge() >= 0) {
                        carkindMap = new HashMap<>();
                        carkindMap.put("Amount", aCarKindList.getSelecteditem());
                        amountSum += aCarKindList.getDiscountCharge();
                        insbRiskkindconfig.setRiskkindcode(aCarKindList.getInskindcode());
                        insbRiskkindconfig = insbRiskkindconfigDao.selectOne(insbRiskkindconfig);
                        carkindMap.put("Inskindcode", insbRiskkindconfig.getShortname());
                        carkindMap.put("Amountprice", aCarKindList.getDiscountCharge());
                    }
                }
                BigDecimal decimal = new BigDecimal(amountSum);
                allOfItem.put("amountSum", decimal.setScale(2, BigDecimal.ROUND_HALF_UP));
                policyitemKey.put("Totalepremium", decimal.setScale(2, BigDecimal.ROUND_HALF_UP));
                price.add(carkindMap);
            }
			//新增设备险
			if(risktype.equals("0")){
				if(getSpecialkind(taskid,prvid)!=null){
					Map<String,Object> newEqupmentMap =null;
					for(INSBSpecialkindconfig specialkindconfig:getSpecialkind(taskid,prvid)){
							INSBRiskkindconfig insbRiskkindconfigs=new INSBRiskkindconfig();
							insbRiskkindconfigs.setRiskkindcode(specialkindconfig.getKindcode());
							insbRiskkindconfigs=insbRiskkindconfigDao.selectOne(insbRiskkindconfigs);
							newEqupmentMap=new HashMap<String, Object>();
							newEqupmentRiskMap=new HashMap<String,Object>();
							newEqupmentRiskMaps=new HashMap<String,Object>();
							if(specialkindconfig.getTypecode().equals("05")){
								newEqupmentRiskMap.put("specialType", specialkindconfig.getTypecode());
								newEqupmentMap.put("Codekey","");
								newEqupmentMap.put("Codevalue",specialkindconfig.getCodevalue()+"/天");
								newEqupmentRisk.add(newEqupmentMap);
								newEqupmentRiskMap.put("Inskindcode",insbRiskkindconfigs.getShortname());
								newEqupmentRiskMap.put("specialKindList", newEqupmentRisk);
								price.add(newEqupmentRiskMap);
							}else if(specialkindconfig.getTypecode().equals("04")){
								newEqupmentRiskMaps.put("specialType", specialkindconfig.getTypecode());
								newEqupmentMap.put("Codekey",specialkindconfig.getCodekey());
								newEqupmentMap.put("Codevalue",specialkindconfig.getCodevalue());
								newEqupmentRisks.add(newEqupmentMap);
								newEqupmentRiskMaps.put("Inskindcode",insbRiskkindconfigs.getShortname());
								newEqupmentRiskMaps.put("specialKindList", newEqupmentRisks);
								price.add(newEqupmentRiskMaps);
							}
					}
				}else{
					newEqupmentRisk=null;
				}
			}
			allOfItem.put("carkindList", price);
			Map<String,Object> insbLegalrightclaimMap=new HashMap<String,Object>();
//			INSBPerson insbPerson=new INSBPerson();
			//被保人信息
		INSBInsuredhis insbInsuredhis=this.getInsuredhis(taskid, prvid);
		if(insbInsuredhis!=null && (!insbInsuredhis.getPersonid().isEmpty())){
			insbPerson=insbPersonDao.selectById(insbInsuredhis.getPersonid());
			if(insbPerson!=null){
				insbLegalrightclaimMap.put("BinCellphone", insbPerson.getCellphone());
				policyitemKey.put("Insuredname", insbPerson.getName());
				insbLegalrightclaimMap.put("BinEmail", insbPerson.getEmail());
				insbLegalrightclaimMap.put("BinGender", insbPerson.getGender());
				insbLegalrightclaimMap.put("BinName", insbPerson.getName());
				insbLegalrightclaimMap.put("Bincardno", insbPerson.getIdcardno());
				insbLegalrightclaimMap.put("Bincardtype", insbPerson.getIdcardtype());
			}
		}else{
			INSBPerson insbPersonBin= insbPersonDao.selectJoinBinBytaskId(taskid);
			if(insbPersonBin!=null) {
				insbLegalrightclaimMap.put("BinCellphone", insbPersonBin.getCellphone());
				insbLegalrightclaimMap.put("BinEmail", insbPersonBin.getEmail());
				insbLegalrightclaimMap.put("BinGender", insbPersonBin.getGender());
				insbLegalrightclaimMap.put("BinName", insbPersonBin.getName());
				insbLegalrightclaimMap.put("Bincardno", insbPersonBin.getIdcardno());
				insbLegalrightclaimMap.put("Bincardtype", insbPersonBin.getIdcardtype());
			}
		}
		//索赔权益人员信息
		INSBLegalrightclaimhis bLegalrightclaimhisPerson=this.getinsbLegalrightclaimhis(taskid, prvid);
		if(bLegalrightclaimhisPerson!=null && (!bLegalrightclaimhisPerson.getPersonid().isEmpty())){
			insbPerson=insbPersonDao.selectById(bLegalrightclaimhisPerson.getPersonid());
			if(insbPerson!=null) {
				insbLegalrightclaimMap.put("LegalCellphone", insbPerson.getCellphone());
				insbLegalrightclaimMap.put("LegalEmail", insbPerson.getEmail());
				insbLegalrightclaimMap.put("LegalGender", insbPerson.getGender());
				insbLegalrightclaimMap.put("LegalName", insbPerson.getName());
				insbLegalrightclaimMap.put("Legalcardno", insbPerson.getIdcardno());
				insbLegalrightclaimMap.put("Legalcardtype", insbPerson.getIdcardtype());
			}
		}else{
			INSBPerson insbPersonLegal= insbPersonDao.selectJoinLegalBytaskId(taskid);
			if(insbPersonLegal!=null) {
				insbLegalrightclaimMap.put("LegalCellphone", insbPersonLegal.getCellphone());
				insbLegalrightclaimMap.put("LegalEmail", insbPersonLegal.getEmail());
				insbLegalrightclaimMap.put("LegalGender", insbPersonLegal.getGender());
				insbLegalrightclaimMap.put("LegalName", insbPersonLegal.getName());
				insbLegalrightclaimMap.put("Legalcardno", insbPersonLegal.getIdcardno());
				insbLegalrightclaimMap.put("Legalcardtype", insbPersonLegal.getIdcardtype());
			}
		}
		//投保人信息
		INSBApplicanthis insbApplicanthis=this.getApplicanthis(taskid, prvid);
		if(insbApplicanthis!=null && (!insbApplicanthis.getPersonid().isEmpty())){
			insbPerson=insbPersonDao.selectById(insbApplicanthis.getPersonid());
			if(insbPerson!=null) {
				insbLegalrightclaimMap.put("AppCellphone", insbPerson.getCellphone());
				insbLegalrightclaimMap.put("AppEmail", insbPerson.getEmail());
				insbLegalrightclaimMap.put("AppGender", insbPerson.getGender());
				insbLegalrightclaimMap.put("AppName", insbPerson.getName());
				insbLegalrightclaimMap.put("Appcardno", insbPerson.getIdcardno());
				insbLegalrightclaimMap.put("Appcardtype", insbPerson.getIdcardtype());
			}
		}else{
			INSBPerson insbPersonApp= insbPersonDao.selectJoinAppBytaskId(taskid);
			if(insbPersonApp!=null){
				insbLegalrightclaimMap.put("AppCellphone", insbPersonApp.getCellphone());
				insbLegalrightclaimMap.put("AppEmail", insbPersonApp.getEmail());
				insbLegalrightclaimMap.put("AppGender", insbPersonApp.getGender());
				insbLegalrightclaimMap.put("AppName", insbPersonApp.getName());
				insbLegalrightclaimMap.put("Appcardno", insbPersonApp.getIdcardno());
				insbLegalrightclaimMap.put("Appcardtype", insbPersonApp.getIdcardtype());
			}else{
				insbLegalrightclaimMap.put("AppCellphone","");
				insbLegalrightclaimMap.put("AppEmail","");
				insbLegalrightclaimMap.put("AppGender", "");
				insbLegalrightclaimMap.put("AppName", "");
				insbLegalrightclaimMap.put("Appcardno", "");
				insbLegalrightclaimMap.put("Appcardtype", "");
			}
		}
			allOfItem.put("insbLegalrightclaimMap", insbLegalrightclaimMap);
			CommonModel commonModel=new CommonModel();
			if(allOfItem.isEmpty()){
				commonModel.setStatus("faile");
				commonModel.setBody(allOfItem);
				commonModel.setMessage("数据加载失败");
			}else{
				commonModel.setStatus("success");
				commonModel.setBody(allOfItem);
				commonModel.setMessage("数据加载成功");
			}
			JSONObject object=JSONObject.fromObject(commonModel);
		return object.toString();
	}
	//新增设备险
		public List<INSBSpecialkindconfig> getSpecialkind(String processInstanceId,
				String inscomcode ){
			INSBSpecialkindconfig insbSpecialkindconfig=new INSBSpecialkindconfig();
			insbSpecialkindconfig.setTaskid(processInstanceId);
			insbSpecialkindconfig.setInscomcode(inscomcode);
			List<INSBSpecialkindconfig>InsbSprecialkindzList=insbSpecialkindconfigDao.selectList(insbSpecialkindconfig);
			return InsbSprecialkindzList;
		}
	//被保人历史表信息
		public INSBInsuredhis getInsuredhis(String taskid,String comcode){
			INSBInsuredhis insbInsuredhis=new INSBInsuredhis();
			insbInsuredhis.setTaskid(taskid);
			insbInsuredhis.setInscomcode(comcode);
			INSBInsuredhis Query_insbInsuredhis=insbInsuredhisDao.selectOne(insbInsuredhis);
			return Query_insbInsuredhis;
		}
	//投保人历史表信息	
		public INSBApplicanthis getApplicanthis(String taskid,String comcode){
			INSBApplicanthis applicanthis=new INSBApplicanthis();
			applicanthis.setTaskid(taskid);
			applicanthis.setInscomcode(comcode);
			INSBApplicanthis Query_applicanthis=applicanthisDao.selectOne(applicanthis);
			return Query_applicanthis;
		}
	//权益索赔人历史表相关信息
		public INSBLegalrightclaimhis getinsbLegalrightclaimhis(String taskid,String comcode){
			INSBLegalrightclaimhis insbLegalrightclaimhis=new INSBLegalrightclaimhis();
			insbLegalrightclaimhis.setTaskid(taskid);
			insbLegalrightclaimhis.setInscomcode(comcode);
			INSBLegalrightclaimhis Query_Legalrightclaimhis=insbLegalrightclaimhisDao.selectOne(insbLegalrightclaimhis);
			return Query_Legalrightclaimhis;
		}
	//车辆信息历史表内容
		public INSBCarinfohis getCarinfohis(String taskid, String inscomcode){
			INSBCarinfohis carinfohis=new INSBCarinfohis();
			carinfohis.setTaskid(taskid);
			carinfohis.setInscomcode(inscomcode);
			//原来的
			//INSBCarinfohis Query_carinfohis=carinfohisDao.selectOne(carinfohis);
			//TODO 修改后, 车辆性质和所属性质根据码表带出
			INSBCarinfohis Query_carinfohis=carinfohisDao.selectCarinfohis(carinfohis);
			return Query_carinfohis;
		}
	//车辆基本信息历史表内容
		public INSBCarmodelinfohis getCarmodelinfohis(String carinfoid ,String comcode){
			INSBCarmodelinfohis carmodelinfohis=new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carinfoid);
			carmodelinfohis.setInscomcode(comcode);
			INSBCarmodelinfohis query_carmodelinfohis=carmodelinfohisDao.selectOne(carmodelinfohis);
			return query_carmodelinfohis;
		}
	//车主信息
		public INSBCarowneinfo getcarowneinfo(String taskid){
			INSBCarowneinfo carowneinfo=new INSBCarowneinfo();
			carowneinfo.setTaskid(taskid);
			INSBCarowneinfo Query_carowneinfo=carowneinfoDao.selectOne(carowneinfo);
			return Query_carowneinfo;
		}
		

}
