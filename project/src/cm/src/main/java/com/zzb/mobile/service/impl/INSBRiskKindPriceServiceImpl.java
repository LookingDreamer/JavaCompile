package com.zzb.mobile.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.service.INSBCarmodelinfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.entity.INSCCode;
import com.zzb.app.model.bean.RisksData;
import com.zzb.app.model.bean.SelectOption;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBSpecialkindconfigDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBSpecialkindconfig;
import com.zzb.cm.entity.entityutil.EntityTransformUtil;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBRiskDao;
import com.zzb.conf.dao.INSBRiskkindconfigDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBRiskkindconfig;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.INSBRiskKindPriceService;

/**
 * 
 * @author zq
 * 详情表查询,通过taskID和inscomCode字段
 *
 */
@Service
@Transactional
public class INSBRiskKindPriceServiceImpl extends BaseServiceImpl<INSBCarkindprice>implements INSBRiskKindPriceService{
	
	@Resource
	private INSBCarkindpriceDao carkindpriceDao;
	@Resource
	private INSBRiskDao insbRiskDao;
	@Resource
	private INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource 
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
	private INSBRiskkindconfigDao insbRiskkindconfigDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBSpecialkindconfigDao insbSpecialkindconfigDao;
	@Resource
	private INSBCarinfoDao carinfoDao;
	@Resource
	private INSBCarinfohisDao carinfohisDao;
	@Resource
	private INSBPersonDao personDao;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBCarmodelinfoService insbCarmodelinfoService;
	@Override
	protected BaseDao<INSBCarkindprice> getBaseDao() {
		return carkindpriceDao;
	}
	/*
	 * 根据ID和comcode查询表INSBCarkindprice
	 * */
	@Override
	public List<INSBCarkindprice> allKindPrice(String processInstanceId,
			String inscomcode) {
		
		Map<String, Object> paramItem=new HashMap<String,Object>();
		paramItem.put("taskid", processInstanceId);
		paramItem.put("inscomcode",inscomcode);
		List<INSBCarkindprice> allKindPriceList=carkindpriceDao.selectOrderRiskkind(paramItem);
		return allKindPriceList;
	}
	/*
	 * 根据comcode查询表insbRisk
	 * */
	@Override
	public List<Map<String,Object>> riskKindByCode(String inscomcode) {
		List<Map<String,Object>> insbRiskkindList= insbRiskDao.selectRiskList(inscomcode);
		return insbRiskkindList;
	}
	/*
	 * 相关数据转为json格式
	 */
	public String allDtailesKind(String processInstanceId,
			String inscomcode){
		CommonModel model = new CommonModel();
		if (StringUtil.isEmpty(processInstanceId) || StringUtil.isEmpty(inscomcode)) {
			model.setStatus(CommonModel.STATUS_FAIL);
			model.setMessage("processInstanceId or inscomcode is null");
			return JSONObject.fromObject(model).toString();
		}
		//商业险总额
		double BPremium = 0.0;
		//为清单集合
		List<INSBCarkindprice> allKindPriceList = this.allKindPrice(processInstanceId,inscomcode);
		List<Map<String,Object>> riskKindByCodeList = this.riskKindByCode(inscomcode);
		//数据展示集合
		Map<String,Object> midList = new HashMap<String, Object>();
		//商业险
		List<Map<String,Object>> bussnussRisk = new ArrayList<Map<String,Object>>();
		//不计免赔
		List<Map<String,Object>> noSourceRisk = new ArrayList<Map<String,Object>>();
		//交强险和车船险
		List<Map<String,Object>> trafficRisk = new ArrayList<Map<String,Object>>();
		//新增设备险
		List<Map<String,Object>> newEqupmentRisk = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> newEqupmentRisks = new ArrayList<Map<String,Object>>();
		//被保人信息
		INSBInsuredhis insbInsuredhis = this.getInsuredhis(processInstanceId, inscomcode);
		if(insbInsuredhis!=null && (!insbInsuredhis.getPersonid().isEmpty())){
			INSBPerson insbPerson = personDao.selectById(insbInsuredhis.getPersonid());
			midList.put("BinName", insbPerson.getName());
		}else{
            INSBPerson insbPersonBin = personDao.selectJoinBinBytaskId(processInstanceId);
            String name = "";
            if (insbPersonBin != null) {
                name = insbPersonBin.getName();
            }
            midList.put("BinName", name);
		}
		//车辆信息
		INSBCarinfohis carinfohis = this.getCarinfohis(processInstanceId, inscomcode);
		INSBCarinfo carInfo = carinfoDao.selectCarinfoByTaskId(processInstanceId);
		if(carinfohis!=null){
			midList.put("Carlicenseno", carinfohis.getCarlicenseno());
		}else{
			midList.put("Carlicenseno", carInfo.getCarlicenseno());
		}
		Map<String,Object> bussnussRiskMap1 = null;
		Map<String,Object> noSourceRiskMap1 = null;
		Map<String,Object> trafficRiskMap = null;
		List<Object>noSourceList = new ArrayList<Object>();
		List<SelectOption> SelectOptionList = null;
		//公司名称
		for(int j=0; j<riskKindByCodeList.size(); j++){
			midList.put("inscomname",riskKindByCodeList.get(j).get("riskname"));
		}
		//获取特殊险种集合specialKindList
		INSCCode  sKindcode = new INSCCode();
		sKindcode.setCodetype("riskkindconfig");
		sKindcode.setParentcode("riskkindconfig");
		List<INSCCode> specialKindList = inscCodeDao.selectList(sKindcode);
		List<String> skCodeList = new ArrayList<String>();
		for (INSCCode inscCode : specialKindList) {
			skCodeList.add(inscCode.getCodename());
		}
		for(int i=0; i<allKindPriceList.size(); i++){
			if("0".equals(allKindPriceList.get(i).getInskindtype())){
				bussnussRiskMap1=new HashMap<String, Object>();
				//商业险
				if(allKindPriceList.get(i)!=null){
					bussnussRiskMap1.put("Amount",allKindPriceList.get(i).getAmount());
					bussnussRiskMap1.put("Amountprice",allKindPriceList.get(i).getDiscountCharge());
					if(allKindPriceList.get(i).getDiscountCharge() != null){
						BPremium+=allKindPriceList.get(i).getDiscountCharge();
					}
					//新增设备险
//					List<INSBSpecialkindconfig>sprecconfig=getSpecialkind(processInstanceId,inscomcode);
//					if("NewEquipmentIns".equals(allKindPriceList.get(i).getInskindcode())||
//							"CompensationDuringRepairIns".equals(allKindPriceList.get(i).getInskindcode())){
					if(skCodeList.contains(allKindPriceList.get(i).getInskindcode())){//判断是否是特殊险种
						if(getSpecialkind(processInstanceId,inscomcode)!=null){
							Map<String,Object> newEqupmentMap = null;
							Map<String,Object> newEqupmentMaps = null;
							for(INSBSpecialkindconfig specialkindconfig:getSpecialkind(processInstanceId,inscomcode)){
								if(allKindPriceList.get(i).getInskindcode().equals(specialkindconfig.getKindcode())){
									if(specialkindconfig.getTypecode().equals("04")){
										newEqupmentMap = new HashMap<String, Object>();
										newEqupmentMap.put("Codekey",specialkindconfig.getCodekey());
										newEqupmentMap.put("Codevalue",specialkindconfig.getCodevalue());
										INSBRiskkindconfig insbRiskkindconfigs=new INSBRiskkindconfig();
										insbRiskkindconfigs.setRiskkindcode(specialkindconfig.getKindcode());
										insbRiskkindconfigs = insbRiskkindconfigDao.selectOne(insbRiskkindconfigs);
										newEqupmentMap.put("kindname",insbRiskkindconfigs.getRiskkindname());
										newEqupmentRisk.add(newEqupmentMap);
									}else if(specialkindconfig.getTypecode().equals("05")){
										newEqupmentMaps = new HashMap<String, Object>();
										newEqupmentMaps.put("Codevalue",specialkindconfig.getCodevalue());
										INSBRiskkindconfig insbRiskkindconfigs=new INSBRiskkindconfig();
										insbRiskkindconfigs.setRiskkindcode(specialkindconfig.getKindcode());
										insbRiskkindconfigs = insbRiskkindconfigDao.selectOne(insbRiskkindconfigs);
										newEqupmentMaps.put("kindname",insbRiskkindconfigs.getRiskkindname());
										newEqupmentRisks.add(newEqupmentMaps);
									}
								}
							}
						}else{
							newEqupmentRisk = null;
							newEqupmentRisks = null;
						}
					}
					INSBRiskkindconfig insbRiskkindconfig = new INSBRiskkindconfig();
					insbRiskkindconfig.setRiskkindcode(allKindPriceList.get(i).getInskindcode());
					insbRiskkindconfig=insbRiskkindconfigDao.selectOne(insbRiskkindconfig);
					bussnussRiskMap1.put("kindname",insbRiskkindconfig.getShortname());

					if("PassengerIns".equals(allKindPriceList.get(i).getInskindcode()) && StringUtil.isNotEmpty(allKindPriceList.get(i).getSelecteditem())){//乘客责任险显示单位元/座
							JSONArray itemArray = JSONArray.fromObject(allKindPriceList.get(i).getSelecteditem());
							JSONObject item=JSONObject.fromObject(itemArray.get(0));
							if(item.get("TYPE").equals("01")){
								JSONArray valueArray = JSONArray.fromObject(item.get("VALUE"));
								JSONObject value = JSONObject.fromObject(valueArray.get(0));
                                Object keyValue = value.get("VALUE");

                                //保额跟选项相同
                                if (allKindPriceList.get(i).getAmount() != null && keyValue != null && StringUtil.isNotEmpty(keyValue.toString()) &&
                                        keyValue.toString().equals(String.valueOf(allKindPriceList.get(i).getAmount().intValue()))) {
                                    bussnussRiskMap1.put("Amount", value.get("KEY"));//元/座
                                } else {
                                    bussnussRiskMap1.put("Amount",douFormat(allKindPriceList.get(i).getAmount())+"元/座");//元/座
                                }
							}else{
								bussnussRiskMap1.put("Amount",douFormat(allKindPriceList.get(i).getAmount())+"元/座");//元/座
							}
							
					}else if("WadingIns".equals(allKindPriceList.get(i).getInskindcode())||
							"SpecifyingPlantCla".equals(allKindPriceList.get(i).getInskindcode()) || "VehicleDemageMissedThirdPartyCla".equals(allKindPriceList.get(i).getInskindcode())){
						bussnussRiskMap1.put("Amount","投保");
					}else if("GlassIns".equals(allKindPriceList.get(i).getInskindcode())){
						if(1d == allKindPriceList.get(i).getAmount()){
							bussnussRiskMap1.put("Amount","国产玻璃");
						}else{
							bussnussRiskMap1.put("Amount","进口玻璃");
						}
					}
//					SelectOptionList = toSelectOptionList(allKindPriceList.get(i).getSelecteditem());
//					if(SelectOptionList!=null && SelectOptionList.size()>0){
//						for (int j = 0; j < SelectOptionList.size(); j++) {
//							if(!"01".equals(SelectOptionList.get(j).getTYPE())){
//								bussnussRiskMap1.put("Amount",SelectOptionList.get(j).getVALUE().getKEY());
//							}
//						}
//					}
					bussnussRisk.add(bussnussRiskMap1);
				}
			}else if("1".equals(allKindPriceList.get(i).getInskindtype())){
				//不计免赔
				trafficRiskMap=new HashMap<String, Object>();
				if(allKindPriceList.get(i)!=null){
						if(allKindPriceList.get(i).getDiscountCharge()==null){
							allKindPriceList.get(i).setDiscountCharge(0.0);
							BPremium+=allKindPriceList.get(i).getDiscountCharge();
						}else{
							BPremium+=allKindPriceList.get(i).getDiscountCharge();
						}
						trafficRiskMap.put("Amount","投保");
						trafficRiskMap.put("Amountprice",allKindPriceList.get(i).getDiscountCharge());
						INSBRiskkindconfig insbRiskkindconfig=new INSBRiskkindconfig();
						insbRiskkindconfig.setRiskkindcode(allKindPriceList.get(i).getInskindcode());
						insbRiskkindconfig=insbRiskkindconfigDao.selectOne(insbRiskkindconfig);
						trafficRiskMap.put("kindname",insbRiskkindconfig.getShortname());
						noSourceRisk.add(trafficRiskMap);
				}
			}else if("3".equals(allKindPriceList.get(i).getInskindtype()) || "2".equals(allKindPriceList.get(i).getInskindtype())){
				//交强险车船税
				noSourceRiskMap1=new HashMap<String, Object>();
				if(allKindPriceList.get(i)!=null){
					noSourceRiskMap1.put("Amount",allKindPriceList.get(i).getSelecteditem());
					noSourceRiskMap1.put("Amountprice",allKindPriceList.get(i).getDiscountCharge());
					INSBRiskkindconfig insbRiskkindconfig=new INSBRiskkindconfig();
					insbRiskkindconfig.setRiskkindcode(allKindPriceList.get(i).getInskindcode());
					insbRiskkindconfig=insbRiskkindconfigDao.selectOne(insbRiskkindconfig);
					noSourceRiskMap1.put("kindname",insbRiskkindconfig.getRiskkindname());
					trafficRisk.add(noSourceRiskMap1);
				}
			}
		}
		List<INSBPolicyitem> getpoliRate=this.getpolicyRate(processInstanceId, inscomcode);
		for(INSBPolicyitem insbPolicyitem : getpoliRate){
			if(insbPolicyitem.getRisktype().equals("0")){
				if(insbPolicyitem.getDiscountRate()==null){
					midList.put("Brist","");
				}else{
					BigDecimal b   =   new   BigDecimal(insbPolicyitem.getDiscountRate());
					midList.put("Brist",new java.text.DecimalFormat("#0.0000").format(b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue()));
				}
			}else if(insbPolicyitem.getRisktype().equals("1")){
				if(insbPolicyitem.getDiscountRate()==null){
					midList.put("Trist","");
				}else{
					BigDecimal b   =   new   BigDecimal(insbPolicyitem.getDiscountRate());
					midList.put("Trist",new java.text.DecimalFormat("#0.0000").format(b.setScale(4,   BigDecimal.ROUND_HALF_UP).doubleValue()));
				}
			}
			midList.put("biztype", insbPolicyitem.getBiztype());
		}
		//浮动计算
		//获取车损保额
		INSBCarkindprice carkindprice=new INSBCarkindprice();
		carkindprice.setTaskid(processInstanceId);
		carkindprice.setInscomcode(inscomcode);
		carkindprice.setInskindcode("VehicleDemageIns");
		INSBCarkindprice carkindprices=carkindpriceDao.selectOne(carkindprice);
		//获取新车置购价
		INSBCarinfo carinfos=new INSBCarinfo();
		carinfos.setTaskid(processInstanceId);
		INSBCarinfo carinfo=insbCarinfoDao.selectOne(carinfos);
		INSBCarmodelinfo carmodelinfos=new INSBCarmodelinfo();
		carmodelinfos.setCarinfoid(carinfo.getId());
		INSBCarmodelinfo carmodelinfo= insbCarmodelinfoDao.selectOne(carmodelinfos);
		if(carmodelinfo==null){
			midList.put("down","0");
		}
		if(carkindprices==null||carkindprices.getDiscountRate()==null){
			midList.put("down","0");
		}else if(carkindprice!=null&&carkindprices.getDiscountRate()!=null){
			double rate=carkindprices.getDiscountRate();
			double down=0.0;
			if(carmodelinfo!=null&&carmodelinfo.getPrice()>0){
				down=rate/carmodelinfo.getPrice();
				BigDecimal decimal=new BigDecimal(down);
				midList.put("down",decimal.setScale(2,BigDecimal.ROUND_HALF_UP));
			}else{
				midList.put("down","0");
			}
		}else{
			midList.put("down","0");
		}
		noSourceList.add(noSourceRiskMap1);
		midList.put("Bussibuss",bussnussRisk);//商业险
		midList.put("newEqupmentRisk",newEqupmentRisk);//新增设备险
		midList.put("newEqupmentRisks",newEqupmentRisks);//新增设备险
		if(BPremium<=0){
			midList.put("BPremium","0");
		}else{
		BigDecimal BPremiumdecimal=new BigDecimal(BPremium);
		midList.put("BPremium",BPremiumdecimal.setScale(2,BigDecimal.ROUND_HALF_UP));
		}
		midList.put("NoSourceRisk",noSourceRisk);//不计免赔
		midList.put("TrafficRisk",trafficRisk);//交强险和车船险

		//http://pm.baoxian.in/zentao/task-view-1757.html
		//1757 【0603版本】【CM】给掌中保前端报价明细界面的接口，增加“新车购置价”字段（单方）
		if (carinfo != null) {
			Map<String, Object> carmodelMap = insbCarmodelinfoService.getCarmodelInfoByCarinfoId(processInstanceId,carinfo.getId(),inscomcode,"CARINFODIALOG");
			if (carmodelMap != null) {
				midList.put("price", carmodelMap.get("price"));
			}
		}

		model.setBody(midList);
		
		if(midList.isEmpty()){
			model.setStatus("fail");
			model.setMessage("数据为空");
			//model.setBody(allRiskListMap);
			JSONObject jsonObject = JSONObject.fromObject(model);
			return jsonObject.toString();
		}else{
			model.setStatus("success");
			model.setMessage("查询成功");
			JSONArray jsonObjectNew=JSONArray.fromObject(model);
			JSONObject jsonObject=JSONObject.fromObject(jsonObjectNew.get(0));
			String strResult=jsonObject.toString().replaceAll("\\\\", "");
			return strResult;
		}
		
	}
	
	//
	//保单表信息
	public List<INSBPolicyitem> getpolicyRate(String processInstanceId,
			String inscomcode){
		INSBPolicyitem insbPolicyitem=new INSBPolicyitem();
		insbPolicyitem.setTaskid(processInstanceId);
		insbPolicyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> rateList=insbPolicyitemDao.selectList(insbPolicyitem);
		return rateList;
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
	/*
	 * (non-Javadoc)
	 * @see com.zzb.mobile.service.INSBRiskKindPriceService#kindPrice(java.lang.String, java.lang.String)
	 * 用于返回INSBCarkindprice对象数据
	 */
	@Override
	public List<INSBCarkindprice> kindPrice(String processInstanceId, String inskindcode) {
		Map<String, Object> params=new HashMap<String,Object>();
		params.put("taskid", processInstanceId);
		params.put("inskindcode",inskindcode);
		List<INSBCarkindprice> list=carkindpriceDao.selectCarkindpriceList(params);
		//INSBCarkindprice carkindprices=carkindpriceDao.selectINSBCarkindprice(params);
		return list;
	}
	/*
	 * 修改保险配置页面数据
	 */
	@Override
	public void updateKindPriceItem(INSBCarkindprice carkindprice) {
		carkindpriceDao.updateById(carkindprice);
	}
	@Override
	public String kindPriceList(String processInstanceId,
			String insbcomcode) {
		Map<String, Object> params=new HashMap<String,Object>();
		params.put("taskid", processInstanceId);
		params.put("insbcomcode",insbcomcode);
		List<Map<String,String>>statusList=carkindpriceDao.selectStatus(params);
		
		Map<String,Object> kindMap=null;
		CommonModel model=new CommonModel();
		if(statusList!=null){
		//如果是在kind表中则为选中
				List<Object> kindpricelist=new ArrayList<Object>();
				for(int i=0;i<statusList.size();i++){
					kindMap=new HashMap<String, Object>();
					if(statusList.get(i).get("amount")!=null){
					kindMap.put("coverage",statusList.get(i).get("amount"));
					kindMap.put("isSelect",true);
					}else{
						kindMap.put("isSelect",false);
						kindMap.put("coverage","");
					}
					if(statusList.get(i).get("selecteditem")!=null){
						JSONArray array=JSONArray.fromObject(statusList.get(i).get("selecteditem").toLowerCase());	
						kindMap.put("insuredConfig",array);
					}
					kindMap.put("pricearea","150000-200000");
					kindMap.put("plankey","jjshx");
					kindMap.put("riskkindname",statusList.get(i).get("kindname"));
					kindMap.put("riskkindcode",statusList.get(i).get("inskindcode"));
					kindMap.put("isdeductible",statusList.get(i).get("notdeductible"));
					kindMap.put("flag",statusList.get(i).get("isusing"));
					kindMap.put("type",statusList.get(i).get("inskindtype"));
					kindpricelist.add(kindMap);
				}
				
		model.setStatus("success");
		model.setMessage("成功返回数据");
		model.setBody(kindpricelist);
		JSONObject jsonObject=JSONObject.fromObject(model);
		String strResult=jsonObject.toString().replaceAll("\\\\\"", "");
		return strResult;
		}else{
			model.setStatus("false");
			model.setMessage("返回数据失败");
			model.setBody(kindMap);
			JSONObject jsonObject=JSONObject.fromObject(model);
			return jsonObject.toString();
		}
		
	}	
	
//	private InsuredConfig stringToInsuredConfig(String res){
//		JSONArray jsonArray = JSONArray.fromObject(res);
//		Object[] datas = jsonArray.toArray();
//		JSONObject jsonObject = JSONObject.fromObject(datas[0]);
//		InsuredConfig config = (InsuredConfig) JSONObject.toBean(jsonObject, InsuredConfig.class);
//		JSONArray array = JSONArray.fromObject(config.getVALUE());
//		Object[] objects = array.toArray();
//		List<RisksData> risksDatas = new ArrayList<RisksData>();
//		for(Object object : objects){
//			JSONObject jsonObject2 = JSONObject.fromObject(object);
//			RisksData data = (RisksData) JSONObject.toBean(jsonObject2, RisksData.class);
//			risksDatas.add(data);
//		}
//		config.setVALUE(risksDatas);
//		return config;
//	}
	public Integer getSeats(String processInstanceId, String inscomcode){
		//取得车辆信息
		INSBCarinfo carInfo = insbCarinfoDao.selectCarinfoByTaskId(processInstanceId);
		INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
		carmodelinfo.setCarinfoid(carInfo.getId());
		carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
		INSBOrder order = new INSBOrder();
		order.setTaskid(processInstanceId);
		order.setPrvid(inscomcode);
		order = insbOrderDao.selectOne(order);
		if(order==null){
			//不能查到订单表明是人工报价节点
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carInfo.getId());
			carmodelinfohis.setInscomcode(inscomcode);
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
			if(carmodelinfohis!=null){
				carmodelinfo = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
			}
		 }
		return carmodelinfo.getSeat();
	}
	
	//截取小数点
		public double douFormat(Double target){
			DecimalFormat df = new DecimalFormat("#0.00");
			double tar = 0.0d;
			if(target!=null){
				tar = target;
			}
			return Double.parseDouble(df.format(tar));
		}
		
		/**
		 * 将险别要素已选项json类型数据转换为List<SelectOption>实体类
		 * liuchao
		 * */
		public List<SelectOption> toSelectOptionList(String selectOptionJSON){
			List<SelectOption> selectOptionList = new ArrayList<SelectOption>();
			try {
				JSONArray jsonArray = JSONArray.fromObject(selectOptionJSON);//字符串转换为JSONArray对象
				Map<String, Class> classMap = new HashMap<String, Class>();
				classMap.put("VALUE", RisksData.class);//指定复杂对象的类型
				ArrayList<SelectOption> selectList = //现将字符串转换为集合
						(ArrayList<SelectOption>)JSONArray.toCollection(jsonArray, SelectOption.class);
				for (int i = 0; i < selectList.size(); i++) {
					//遍历数组并将元素转换为SelectOption类型
					JSONObject temp = JSONObject.fromObject(selectList.get(i));
					SelectOption selectOption = (SelectOption) JSONObject.toBean(temp, SelectOption.class, classMap);
					selectOptionList.add(selectOption);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return selectOptionList;
		}
	//被保人历史表信息
		public INSBInsuredhis getInsuredhis(String taskid,String comcode){
				INSBInsuredhis insbInsuredhis=new INSBInsuredhis();
				insbInsuredhis.setTaskid(taskid);
				insbInsuredhis.setInscomcode(comcode);
				INSBInsuredhis Query_insbInsuredhis=insbInsuredhisDao.selectOne(insbInsuredhis);
				return Query_insbInsuredhis;
			}
	//车辆信息历史表内容
		public INSBCarinfohis getCarinfohis(String taskid, String inscomcode){
			INSBCarinfohis carinfohis=new INSBCarinfohis();
			carinfohis.setTaskid(taskid);
			carinfohis.setInscomcode(inscomcode);
			INSBCarinfohis Query_carinfohis=carinfohisDao.selectOne(carinfohis);
			return Query_carinfohis;
		}
}
