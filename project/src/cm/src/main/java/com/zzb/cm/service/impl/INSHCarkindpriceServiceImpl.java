package com.zzb.cm.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.app.model.bean.AmountSelect;
import com.zzb.app.model.bean.RisksData;
import com.zzb.app.model.bean.SelectOption;
import com.zzb.cm.controller.vo.CarInsConfigVo;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBSpecialkindconfigDao;
import com.zzb.cm.dao.INSHCarkindpriceDao;
import com.zzb.cm.dao.INSHPolicyitemDao;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBSpecialkindconfig;
import com.zzb.cm.entity.INSHCarkindprice;
import com.zzb.cm.entity.INSHPolicyitem;
import com.zzb.cm.service.INSHCarkindpriceService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBRiskDao;
import com.zzb.conf.dao.INSBRiskkindDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class INSHCarkindpriceServiceImpl extends BaseServiceImpl<INSHCarkindprice> implements
		INSHCarkindpriceService {
	@Resource
	private INSHCarkindpriceDao inshCarkindpriceDao;
	@Resource
	private INSBRiskDao insbRiskDao;
	@Resource
	private INSBRiskkindDao insbRiskkindDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSHPolicyitemDao inshPolicyitemDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBSpecialkindconfigDao insbSpecialkindconfigDao;
	@Override
	protected BaseDao<INSHCarkindprice> getBaseDao() {
		return inshCarkindpriceDao;
	}

	/**
	 * 通过保险公司代码和任务id查询已选择的险别配置信息
	 */
	@Override
	public Map<String,Object> getCarInsConfigByInscomcode(String inscomcode, String processInstanceId) {
		String provideid = "";
		INSBProvider p =  insbProviderDao.selectFatherProvider(inscomcode);
		if(p!=null){
			provideid = p.getId();
		}
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<CarInsConfigVo> result = new ArrayList<CarInsConfigVo>();
		//查询排序后的险种信息
		List<Map<String, String>> orderedRiskkindList = insbRiskkindDao.selectOrderedRiskkindByInscomcode(provideid);
		for (int i = 0; i < orderedRiskkindList.size(); i++) {
			if(orderedRiskkindList.get(i)!=null){
				CarInsConfigVo carconfig = new CarInsConfigVo();
				carconfig.setRiskcode(orderedRiskkindList.get(i).get("riskcode"));//险种code
				carconfig.setRiskname(orderedRiskkindList.get(i).get("riskname"));//险种名称
				carconfig.setInskindcode(orderedRiskkindList.get(i).get("kindcode"));//险别code
				carconfig.setInskindname(orderedRiskkindList.get(i).get("kindname"));//险种名称
				carconfig.setInskindtype(orderedRiskkindList.get(i).get("kindtype"));//险种类型（0商业险、1不计免赔、2交强险、3车船税）
				carconfig.setNotdeductible(orderedRiskkindList.get(i).get("notdeductible"));//标记是否不计免赔
				carconfig.setPreriskkind(orderedRiskkindList.get(i).get("preriskkind"));//前置险种code
				carconfig.setSpecialRiskkindFlag(orderedRiskkindList.get(i).get("specialRiskkindFlag"));//特殊险别标志
				if("0".equals(orderedRiskkindList.get(i).get("kindtype"))){//商业险存在选项
					carconfig.setAmountSlecets(//此险种可提供的要素及选项
							this.toAmountSelectList(orderedRiskkindList.get(i).get("amountselect")));//json转为对象
				}
				result.add(carconfig);
			}
		}
		//查询此任务中的保险公司已选择的险别记录
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("taskid", processInstanceId);
		map.put("inscomcode", inscomcode);
		List<INSHCarkindprice> carkindpriceList = inshCarkindpriceDao.selectByInscomcode(map);
		//折后车船税、折后商业险总保费、折后交强险保费、其他费用
		double discountCarTax = 0d, discountBusTotalAmountprice = 0d, discountStrTotalAmountprice = 0d, otherAmountprice = 0d;
		INSHCarkindprice carkindprice = null;
		//组织返回页面数据
		for(int i = 0; i < result.size(); i++){
			//获取每种险别是否有保险配置记录，如果有获取相应信息
			carkindprice = this.isHasThisCarkindPrice(carkindpriceList, result.get(i).getInskindcode());
			//组织数据
			if(carkindprice != null){//说明此险别已被客户选择
				result.get(i).setIsChecked("Y");//是否已被选择标记
				result.get(i).setId(carkindprice.getId());//此险别的保险配置表中id
				result.get(i).setAmount(carkindprice.getAmount());//保额
//				result.get(i).setAmountprice(carkindprice.getAmountprice());//保费
				result.get(i).setAmountprice(carkindprice.getDiscountCharge());//折后保费
				if("05".equals(result.get(i).getSpecialRiskkindFlag())){//修理期间费用补偿险需要查询特殊险别值
					INSBSpecialkindconfig specialkindconfig = new INSBSpecialkindconfig();
					specialkindconfig.setTaskid(processInstanceId);
					specialkindconfig.setInscomcode(inscomcode);
					specialkindconfig.setKindcode(result.get(i).getInskindcode());
					specialkindconfig = insbSpecialkindconfigDao.selectOne(specialkindconfig);
					if(specialkindconfig != null){
						result.get(i).setSpecialRiskkindValue(specialkindconfig.getCodevalue());
					}else{
						result.get(i).setSpecialRiskkindValue("0");
					}
				}
				if("04".equals(result.get(i).getSpecialRiskkindFlag())){
					INSBSpecialkindconfig insbSpecialkindconfig = new INSBSpecialkindconfig();
					insbSpecialkindconfig.setTaskid(processInstanceId);
					insbSpecialkindconfig.setInscomcode(inscomcode);
					insbSpecialkindconfig.setKindcode(result.get(i).getInskindcode());
					List<INSBSpecialkindconfig> insbSpecialkindconfigList = insbSpecialkindconfigDao.selectList(insbSpecialkindconfig);
					List<String> specialKVList = new ArrayList<String>();
					if(insbSpecialkindconfigList!=null && insbSpecialkindconfigList.size()>0){
						for(INSBSpecialkindconfig temp : insbSpecialkindconfigList){
							specialKVList.add("设备："+temp.getCodekey()+",保额："+temp.getCodevalue());
						}
					}
					result.get(i).setSpecialKVList(specialKVList);
				}
				if("0".equals(carkindprice.getInskindtype())){//商业险存在选项
					//险别要素已选项
					result.get(i).setSelecteditem(this.toSelectOptionList(carkindprice.getSelecteditem()));//json转为对象
					if(carkindprice.getDiscountCharge()!=null){
						discountBusTotalAmountprice += carkindprice.getDiscountCharge();
					}
				}else if("1".equals(carkindprice.getInskindtype())){
					if(carkindprice.getDiscountCharge()!=null){
						discountBusTotalAmountprice += carkindprice.getDiscountCharge();
					}
				}else if("2".equals(carkindprice.getInskindtype())){
					if(carkindprice.getDiscountCharge()!=null){
						discountStrTotalAmountprice += carkindprice.getDiscountCharge();
					}
				}else if("3".equals(carkindprice.getInskindtype())){
					if(carkindprice.getDiscountCharge()!=null){
						discountCarTax += carkindprice.getDiscountCharge();
					}
				}else{
					if(carkindprice.getDiscountCharge()!=null){
						otherAmountprice += carkindprice.getDiscountCharge();
					}
				}
			}else{
				result.get(i).setIsChecked("N");
			}
		}
		//将封装好的信息提供给cm页面保险配置中使用
		resultMap.put("insConfigList", result);
		resultMap.put("discountBusTotalAmountprice", douFormat(discountBusTotalAmountprice));//商业险保费合计
		resultMap.put("discountStrTotalAmountprice", douFormat(discountStrTotalAmountprice));//交强险保费合计
		resultMap.put("discountCarTax", douFormat(discountCarTax));//车船税
		resultMap.put("otherAmountprice", douFormat(otherAmountprice));//其他费用
		//查询商业险和交强险折扣率
		INSHPolicyitem policyitem = new INSHPolicyitem();
		policyitem.setTaskid(processInstanceId);
		policyitem.setInscomcode(inscomcode);
		policyitem.setNodecode("A");
		List<INSHPolicyitem> policyitemList = inshPolicyitemDao.selectList(policyitem);
		if(policyitemList!=null && policyitemList.size()>0){
			for (int i = 0; i < policyitemList.size(); i++) {
				if("0".equals(policyitemList.get(i).getRisktype())){//商业险起止日期
					resultMap.put("busdiscountrateFlag", true);
					if(policyitemList.get(i).getDiscountRate()!=null && policyitemList.get(i).getDiscountRate()>0.0d){
						resultMap.put("busdiscountrate", String.format("%.4f", policyitemList.get(i).getDiscountRate()));
					}else{
						resultMap.put("busdiscountrate", null);
					}
				}else if("1".equals(policyitemList.get(i).getRisktype())){//交强险起止日期
					resultMap.put("strdiscountrateFlag", true);
					if(policyitemList.get(i).getDiscountRate()!=null && policyitemList.get(i).getDiscountRate()>0.0d){
						resultMap.put("strdiscountrate", String.format("%.4f", policyitemList.get(i).getDiscountRate()));
					}else{
						resultMap.put("strdiscountrate", null);
					}
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * 将险别要素选项的json类型数据转换为List<AmountSelect>实体类
	 * liuchao
	 * */
	public List<AmountSelect> toAmountSelectList(String amountSelectJSON){
		List<AmountSelect> amountSelectList = new ArrayList<AmountSelect>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(amountSelectJSON);//字符串转换为JSONArray对象
			Map<String, Class> classMap = new HashMap<String, Class>();
			classMap.put("VALUE", RisksData.class);//指定复杂对象的类型
			ArrayList<AmountSelect> selectList = //现将字符串转换为集合
					(ArrayList<AmountSelect>)JSONArray.toCollection(jsonArray, AmountSelect.class);
			for (int i = 0; i < selectList.size(); i++) {
				//遍历数组并将元素转换为AmountSelect类型
				JSONObject temp = JSONObject.fromObject(selectList.get(i));
				AmountSelect amountSelect = (AmountSelect) JSONObject.toBean(temp, AmountSelect.class, classMap);
				amountSelectList.add(amountSelect);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amountSelectList;
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
	
	/**
	 * 判断某种险种是否有保险配置记录
	 * liuchao
	 */
	public INSHCarkindprice isHasThisCarkindPrice(List<INSHCarkindprice> carkindpriceList, String kindcode){
		if (carkindpriceList == null || carkindpriceList.isEmpty()) return null;

		for (INSHCarkindprice inshCarkindprice : carkindpriceList) {
			if(inshCarkindprice.getInskindcode().equals(kindcode)){
				return inshCarkindprice;
			}
		}
		return null;
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
}