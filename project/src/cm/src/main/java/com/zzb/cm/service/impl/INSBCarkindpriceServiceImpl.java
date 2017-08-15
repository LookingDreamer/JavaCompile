package com.zzb.cm.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.redis.CMRedisClient;
import com.common.redis.Constants;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.conf.service.INSBPolicyitemService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.app.model.bean.AmountSelect;
import com.zzb.app.model.bean.RisksData;
import com.zzb.app.model.bean.SelectOption;
import com.zzb.cm.controller.vo.CarInsConfigVo;
import com.zzb.cm.controller.vo.CarInsuranceConfigVo;
import com.zzb.cm.controller.vo.SpecialRiskkindCfg;
import com.zzb.cm.controller.vo.SpecialRiskkindCfgVo;
import com.zzb.cm.entity.entityutil.EntityTransformUtil;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBCommonQuoteinfoService;
import com.zzb.cm.service.INSHCarkindpriceService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBRiskDao;
import com.zzb.conf.dao.INSBRiskkindDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRisk;
import com.zzb.conf.entity.INSBRiskkind;

@Service
@Transactional
public class INSBCarkindpriceServiceImpl extends BaseServiceImpl<INSBCarkindprice> implements
		INSBCarkindpriceService {
	@Resource
	private INSBCarkindpriceDao insbCarkindpriceDao;
	@Resource
	private INSBRiskDao insbRiskDao;
	@Resource
	private INSBRiskkindDao insbRiskkindDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
    @Resource
    private INSBCarinfohisDao insbCarinfohisDao;
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBSpecialkindconfigDao insbSpecialkindconfigDao;
	@Resource
	private INSHCarkindpriceService inshCarkindpriceService;
	@Resource
	INSBQuotetotalinfoService insbQuotetotalinfoService;
    @Resource
    INSBPolicyitemService insbPolicyitemService;
    @Resource
    INSBCarconfigDao insbCarconfigDao;
    @Resource
    INSBCommonQuoteinfoService insbCommonQuoteinfoService;

	@Override
	protected BaseDao<INSBCarkindprice> getBaseDao() {
		return insbCarkindpriceDao;
	}

	/**
	 * 修改保险配置信息
	 * liuchao
	 */
	@Override
	public String editInsuranceConfig(CarInsuranceConfigVo insConfigVo) {
		//修改保险配置信息
		String taskInstanceId = insConfigVo.getTaskInstanceId();

		if (StringUtil.isEmpty(taskInstanceId)) {
			LogUtil.error("任务号为空");
			return null;
		}

		//去除空元素
		this.removeNullItemsOfConfigList(insConfigVo.getBusiKindprice());
		this.removeNullItemsOfConfigList(insConfigVo.getNotdKindprice());
		this.removeNullItemsOfConfigList(insConfigVo.getStroKindprice());
		
		//修改前投保类型
		int updateBefore = insbCommonQuoteinfoService.getInsureConfigType(taskInstanceId, "");
		if(updateBefore == 4) updateBefore = 2; //4相当于单交
				
		if("Y".equals(insConfigVo.getEdit2AllList())){//修改到所有单方保险配置
			for (int i = 0; i < insConfigVo.getInscomcodeList().size(); i++) {
				//修改保存保险配置方法
				this.editCarConfigByInscomcode(insConfigVo.getOperator(), taskInstanceId, insConfigVo.getInscomcodeList().get(i), 
						insConfigVo.getBusiKindprice(), insConfigVo.getNotdKindprice(), insConfigVo.getStroKindprice());
				//更新报价信息表保费和修改折扣率
				this.updateQuoteinfo(taskInstanceId, insConfigVo.getInscomcodeList().get(i), insConfigVo.getBusdiscountrate(), 
						insConfigVo.getStrdiscountrate(), insConfigVo.getOperator());
			}
		}else{//只修改当前保险公司保险配置
			//修改保存保险配置方法
			this.editCarConfigByInscomcode(insConfigVo.getOperator(), taskInstanceId, insConfigVo.getThisInscomcode(), 
					insConfigVo.getBusiKindprice(), insConfigVo.getNotdKindprice(), insConfigVo.getStroKindprice());
			//更新报价信息表保费和修改折扣率
			this.updateQuoteinfo(taskInstanceId, insConfigVo.getThisInscomcode(), insConfigVo.getBusdiscountrate(), 
					insConfigVo.getStrdiscountrate(), insConfigVo.getOperator());
		}
		
		//修改后投保类型
		int updateAfter = insbCommonQuoteinfoService.getInsureConfigType(taskInstanceId, "");
		if(updateAfter == 4) updateAfter = 2; //4相当于单交
		
		LogUtil.info("(CM端)修改保险配置, 投保类型(1-单商,2-单交,3-混保)由 '" + updateBefore + "' 转为 '" + updateAfter+ "', taskid=" + taskInstanceId);
		if(insbCommonQuoteinfoService.isDoPTQuery(updateBefore, updateAfter)){
			CMRedisClient.getInstance().set(Constants.CM_GLOBAL, taskInstanceId + "startedBakQuery", "0", 20 * 60);
		}
		
		return "success";
	}
	
	/**
	 * 修改投保信息和修改商业险和交强险折扣率
	 */
	private void updateQuoteinfo(String taskInstanceId, String inscomcode, Double busdiscountrate, Double strdiscountrate, String operator) {
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("taskid",taskInstanceId);
		map.put("inscomcode",inscomcode);
		List<INSBCarkindprice> tempList = insbCarkindpriceDao.selectCarkindpriceList(map);
		//sumDisPrice折后保费，sumPrice折前保费，sumDisBusPrice折后商业险保费，
		//sumDisStrPrice折后交强险保费，sumBusPrice折前商业险保费，sumStrPrice折前交强险保费
		double sumDisPrice = 0.0d, sumPrice = 0.0d, sumBusPrice = 0.0d, 
				sumStrPrice = 0.0d, sumDisBusPrice = 0.0d, sumDisStrPrice = 0.0d;
		for (INSBCarkindprice carkindprice : tempList) {
            LogUtil.info(taskInstanceId+","+inscomcode+"险项"+carkindprice.getInskindcode()+"("+carkindprice.getInskindtype()+")保费: "+
                    carkindprice.getDiscountCharge()+"(折后), "+carkindprice.getAmountprice());

			//累加折后保费
			if(carkindprice.getDiscountCharge()!=null){
				sumDisPrice += carkindprice.getDiscountCharge();
			}
			//累加折前保费
			if(carkindprice.getAmountprice()!=null && carkindprice.getAmountprice()>0){
				sumPrice += carkindprice.getAmountprice();
			}else{
				if(carkindprice.getDiscountCharge()!=null){
					sumPrice += carkindprice.getDiscountCharge();
				}
			}

			//累加商业险保费和交强险保费
			//车船税不在保单表中体现
			if("0".equals(carkindprice.getInskindtype()) || "1".equals(carkindprice.getInskindtype())){
				//累加商业险折后保费
				if(carkindprice.getDiscountCharge()!=null){
					sumDisBusPrice += carkindprice.getDiscountCharge();
				}
				//累加商业险折前保费
				if(carkindprice.getAmountprice()!=null && carkindprice.getAmountprice()>0){
					sumBusPrice += carkindprice.getAmountprice();
				}else{
					if(carkindprice.getDiscountCharge()!=null){
						sumBusPrice += carkindprice.getDiscountCharge();
					}
				}
			}else if("2".equals(carkindprice.getInskindtype())){
				//累加交强险折后保费
				if(carkindprice.getDiscountCharge()!=null){
					sumDisStrPrice += carkindprice.getDiscountCharge();
				}
				//累加交强险折前保费
				if(carkindprice.getAmountprice()!=null && carkindprice.getAmountprice()>0){
					sumStrPrice += carkindprice.getAmountprice();
				}else{
					if(carkindprice.getDiscountCharge()!=null){
						sumStrPrice += carkindprice.getDiscountCharge();
					}
				}
			}
		}

        LogUtil.info(taskInstanceId+","+inscomcode+"保费: "+sumDisPrice+"(折后总), "+sumPrice+"(总), "+
                sumDisBusPrice+"(折后商), "+sumBusPrice+"(商), "+sumDisStrPrice+"(折后交), "+sumStrPrice+"(交), "+
                busdiscountrate+"(商折扣), "+strdiscountrate+"(交折扣)");

		//更新报价表记录
		insbQuotetotalinfoDao.updateQuoteDiscountAmount(sumDisPrice,taskInstanceId,inscomcode);
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(taskInstanceId);
		insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		LogUtil.info("INSBQuotetotalinfo|报表数据埋点|"+JSONObject.fromObject(insbQuotetotalinfo).toString());

		//更新订单表记录
        Date date = new Date();
		INSBOrder order = new INSBOrder();
		order.setTaskid(taskInstanceId);
		order.setPrvid(inscomcode);
		order = insbOrderDao.selectOne(order);
		if(order != null){
			order.setModifytime(date);
			order.setOperator(operator);
			order.setTotalpaymentamount(sumDisPrice);
			order.setTotalproductamount(sumPrice);
			double result = new BigDecimal(sumPrice).subtract(new BigDecimal(sumDisPrice)).doubleValue();
			order.setTotalpromotionamount(result);
			insbOrderDao.updateById(order);
		}

		//更新保单表记录
		//组织参数查询数据库中交强险和商业险的险别记录条数，用于删除或创建保单记录
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("mInstanceid", taskInstanceId);
		params.put("inscomcode", inscomcode);
		int strCount = insbCarkindpriceDao.getStrCount(params);
		int busCount = insbCarkindpriceDao.getBusCount(params);
        //快速续保
        INSBCarinfohis carinfohis = new INSBCarinfohis();
        carinfohis.setTaskid(taskInstanceId);
        carinfohis.setInscomcode(inscomcode);
        carinfohis = insbCarinfohisDao.selectOne(carinfohis);

		INSBPolicyitem policy = new INSBPolicyitem();
		policy.setTaskid(taskInstanceId);
		policy.setInscomcode(inscomcode);
		List<INSBPolicyitem> policyList = insbPolicyitemDao.selectList(policy);
		if(policyList != null && policyList.size() > 0){
			for (INSBPolicyitem insbPolicyitem : policyList) {
				if(insbPolicyitem != null){
					insbPolicyitem.setModifytime(date);
					insbPolicyitem.setOperator(operator);
					if ("0".equals(insbPolicyitem.getRisktype())) {//商业险保单
						if(busCount>0||(carinfohis != null && "1".equals(carinfohis.getInsureconfigsameaslastyear()))){//有商业险险别记录
							insbPolicyitem.setPremium(sumBusPrice);
							insbPolicyitem.setTotalepremium(sumDisPrice);
							insbPolicyitem.setDiscountCharge(sumDisBusPrice);
							insbPolicyitem.setDiscountRate(busdiscountrate);
							insbPolicyitemDao.updateById(insbPolicyitem);
						}else if (strCount>0){
							//没有商业险险别记录删除商业险保单记录(都没有的话或快速续保就不删除了，保留一个作为默认，否则EDI、精灵回写不了)
							insbPolicyitemDao.deleteById(insbPolicyitem.getId());
						}
					}else if("1".equals(insbPolicyitem.getRisktype())){//交强险保单
						if(strCount>0){//有交强险险别记录
							insbPolicyitem.setPremium(sumStrPrice);
							insbPolicyitem.setTotalepremium(sumDisPrice);
							insbPolicyitem.setDiscountCharge(sumDisStrPrice);
							insbPolicyitem.setDiscountRate(strdiscountrate);
							insbPolicyitemDao.updateById(insbPolicyitem);
						}else if (busCount>0||(carinfohis != null && "1".equals(carinfohis.getInsureconfigsameaslastyear()))){
							//没有交强险险别记录删除交强险保单记录(都没有的话或快速续保就不删除了，保留一个作为默认，否则EDI、精灵回写不了)
							insbPolicyitemDao.deleteById(insbPolicyitem.getId());
						}
					}
				}
			}
		}
		//按情况添加商业险保单记录
		try {
			if(busCount>0||(carinfohis!=null&&"1".equals(carinfohis.getInsureconfigsameaslastyear()))){
				if(policyList!=null && policyList.size()>0){
					for (int i = 0; i < policyList.size(); i++) {
						if("0".equals(policyList.get(i).getRisktype())){//商业险保单记录
							break;
						}
						if(i==(policyList.size()-1)){
							INSBPolicyitem temp = new INSBPolicyitem();
							PropertyUtils.copyProperties(temp, policyList.get(0));
							temp.setCreatetime(new Date());
							temp.setOperator(operator);
							temp.setRisktype("0");
							temp.setId(null);
							temp.setModifytime(null);
							temp.setProposalformno(null);
							temp.setPolicyno(null);
							temp.setPremium(sumBusPrice);
							temp.setInsureddate(null);
							temp.setTotalepremium(sumDisPrice);
							temp.setPaynum(null);
							temp.setCheckcode(null);
							temp.setDiscountCharge(sumDisBusPrice);
							temp.setDiscountRate(busdiscountrate);
							temp.setAmount(null);
							insbPolicyitemDao.insert(temp);
						}
					}
				}
			}
			//按情况添加交强险保单记录
			if(strCount>0){
				if(policyList!=null && policyList.size()>0){
					for (int i = 0; i < policyList.size(); i++) {
						if("1".equals(policyList.get(i).getRisktype())){//交强险保单记录
							break;
						}
						if(i==(policyList.size()-1)){
							INSBPolicyitem temp = new INSBPolicyitem();
							PropertyUtils.copyProperties(temp, policyList.get(0));
							temp.setCreatetime(new Date());
							temp.setOperator(operator);
							temp.setRisktype("1");
							temp.setId(null);
							temp.setModifytime(null);
							temp.setProposalformno(null);
							temp.setPolicyno(null);
							temp.setPremium(sumStrPrice);
							temp.setInsureddate(null);
							temp.setTotalepremium(sumDisPrice);
							temp.setPaynum(null);
							temp.setCheckcode(null);
							temp.setDiscountCharge(sumDisStrPrice);
							temp.setDiscountRate(strdiscountrate);
							temp.setAmount(null);
							insbPolicyitemDao.insert(temp);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成保单复制数据异常！");
		}
	}
	

	/**
	 * 通过保险公司代码和任务id查询已选择的险别配置信息
	 * liuchao
	 */
	public String editCarConfigByInscomcode(String operator, String processInstanceId, String inscomcode, List<CarInsConfigVo> busiKindprice,
			List<CarInsConfigVo> notdKindprice, List<CarInsConfigVo> stroKindprice){
		//备份insbCarkindprice到inshCarkindprice
		INSBCarkindprice queryInsbCarkindprice = new INSBCarkindprice();
		queryInsbCarkindprice.setTaskid(processInstanceId);
		queryInsbCarkindprice.setInscomcode(inscomcode);
		List<INSBCarkindprice> dataCarkindpriceList = this.queryList(queryInsbCarkindprice);
		if( dataCarkindpriceList != null && dataCarkindpriceList.size()>0){
			INSHCarkindprice delCarkindprice = new INSHCarkindprice();
			delCarkindprice.setTaskid(processInstanceId);
			delCarkindprice.setInscomcode(inscomcode);
			delCarkindprice.setFairyoredi("person");
			delCarkindprice.setNodecode("1");
			 inshCarkindpriceService.delete(delCarkindprice);
			
			for(INSBCarkindprice dataInsbCarkindprice : dataCarkindpriceList){			
				try {
					INSHCarkindprice inshCarkindprice = new INSHCarkindprice();
					PropertyUtils.copyProperties(inshCarkindprice, dataInsbCarkindprice);
					inshCarkindprice.setFairyoredi("person");
					inshCarkindprice.setNodecode("1");
					inshCarkindpriceService.insert(inshCarkindprice);
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		}
		//查询父级供应商编码
		String provideid =insbProviderDao.selectFatherProvider(inscomcode).getId();
		//得到此保险公司的可提供的险别选项信息(用于修改到所有单方时判断此保险公司是否支持)
		List<INSBRiskkind> riskkindList = new ArrayList<INSBRiskkind>();
		INSBRisk risk = new INSBRisk();
		risk.setProvideid(provideid);
//		risk.setProvideid(inscomcode);
		List<INSBRisk> riskList = insbRiskDao.selectList(risk);
		for (int i = 0; i < riskList.size(); i++) {
			INSBRiskkind temp = new INSBRiskkind();
			temp.setRiskid(riskList.get(i).getId());
			List<INSBRiskkind> tempList = insbRiskkindDao.selectList(temp);
			riskkindList.addAll(tempList);
		}
		//查询此任务中的保险公司已选择的险别记录
		INSBCarkindprice carkindprice = new INSBCarkindprice();
		carkindprice.setTaskid(processInstanceId);
		carkindprice.setInscomcode(inscomcode);
		List<INSBCarkindprice> carkindpriceList = insbCarkindpriceDao.selectList(carkindprice);
		//修改保险配置信息
		Date date = new Date();
		//商业险险种处理和交强险车车船税处理
		if(busiKindprice!=null){
			busiKindprice.addAll(stroKindprice);//商业险和交强险车船税合并处理方式相同
		}else{
			busiKindprice = stroKindprice;
		}
		
		List<INSBCarkindprice> newCarkindprices = new ArrayList<INSBCarkindprice>();
		
		//不计免赔险种处理
		if(notdKindprice!=null){
			for(int i = 0; i < notdKindprice.size(); i++){
				CarInsConfigVo insConfigVO = notdKindprice.get(i);
				if("Y".equals(insConfigVO.getIsChecked())){//勾选的
					INSBRiskkind rk = this.isHasThisRiskkind(riskkindList, insConfigVO.getInskindcode());//判断保险公司可提供的险别里是否存在指定险种
					if(rk != null){//此保险公司支持此险种选项
						//判断前置险种是否选择
						boolean flag = this.hasPreriskkind(busiKindprice, rk);
						//判断某种险种是否有保险配置记录
						INSBCarkindprice ckp = this.isHasThisCarkindPrice(carkindpriceList, insConfigVO.getInskindcode());
						if(flag){
							INSBRisk r = insbRiskDao.selectById(rk.getRiskid());
							if(ckp == null){//修改前没有选择此险种，做新添加处理
								ckp = new INSBCarkindprice();
								//赋值处理
								ckp = this.CarInsConfigVo2Carkindprice(operator, processInstanceId, inscomcode, insConfigVO, ckp, rk, r);
								ckp.setCreatetime(date);
								ckp.setSelecteditem(null);//不计免赔没有选项json
								insbCarkindpriceDao.insert(ckp);
							}else{//修改前有选择此险种，更新保费
								ckp.setDiscountCharge(insConfigVO.getAmountprice());
								insbCarkindpriceDao.updateById(ckp);
							}
							newCarkindprices.add(ckp);
						}else{
							if(ckp != null){//修改前有选择此险种，做删除处理
								insbCarkindpriceDao.deleteById(ckp.getId());
							}
						}
					}
				}else{//未勾选的
					//判断某种险种是否有保险配置记录
					INSBCarkindprice ckp = this.isHasThisCarkindPrice(carkindpriceList, insConfigVO.getInskindcode());
					if(ckp != null){//修改前已选择了此险种，做删除处理
						insbCarkindpriceDao.deleteById(ckp.getId());
					}
				}
			}
		}
		//商业险险种处理和交强险车车船税处理
		if(busiKindprice!=null){
			for(int i = 0; i < busiKindprice.size(); i++){
				CarInsConfigVo insConfigVO = busiKindprice.get(i);
//				if("PassengerIns".equals(insConfigVO.getInskindcode())){//乘客责任险显示单位元/座
//					Integer seats = getSeats(processInstanceId, inscomcode);
//					if(seats!=null){
//						insConfigVO.setAmount(douFormat(insConfigVO.getAmount()*seats));//保额
//					}
//				}
				if("Y".equals(insConfigVO.getIsChecked())){//勾选的
					INSBRiskkind rk = this.isHasThisRiskkind(riskkindList, insConfigVO.getInskindcode());//判断保险公司可提供的险别里是否存在指定险种
					if(rk != null){//此保险公司支持此险种选项
						//判断前置险种是否选择
						boolean flag = this.hasPreriskkind(busiKindprice, rk);
						//判断某种险种是否有保险配置记录
						INSBCarkindprice ckp = this.isHasThisCarkindPrice(carkindpriceList, insConfigVO.getInskindcode());
						//同步amount和selecteditem中保额类型(value.value)
						if(insConfigVO.getSelecteditemStrs()!=null){
							for(String str:insConfigVO.getSelecteditemStrs()){
								SelectOption option=this.toSelectOption(str);
								if( option != null && (!"01".equals(option.getTYPE()) && !"03".equals(option.getTYPE()))) {
									if( option.getVALUE() != null ) {
										insConfigVO.setAmount(Double.parseDouble(option.getVALUE().getVALUE()));
									}
								}
							}
						}
						if(flag){
							INSBRisk r = insbRiskDao.selectById(rk.getRiskid());
							List<AmountSelect> asList = this.toAmountSelectList(rk.getAmountselect());//要素供选项
							if(ckp != null){//修改前已选择了此险种，做更新处理
								//赋值处理
								ckp = this.CarInsConfigVo2Carkindprice(operator, processInstanceId, inscomcode, insConfigVO, ckp, rk, r);
//								ckp.setDiscountCharge(0.0d);
								ckp.setModifytime(date);
								if("0".equals(insConfigVO.getInskindtype())){
									//处理已选项json存储
									List<SelectOption> soList = this.toSelectOptionList(ckp.getSelecteditem());//数据库中已存已选项
									ckp.setSelecteditem(this.toSelecteditemJSON(soList, asList, insConfigVO.getSelecteditemStrs(), ckp));//处理更改后的已选项json数据
								}else if("2".equals(insConfigVO.getInskindtype())){
									ckp.setSelecteditem("购买");
								}else if("3".equals(insConfigVO.getInskindtype())){
									ckp.setSelecteditem("代缴");
								}
								insbCarkindpriceDao.updateById(ckp);
							}else{//修改前没有选择此险种，做新添加处理
								ckp = new INSBCarkindprice();
								//赋值处理
								ckp = this.CarInsConfigVo2Carkindprice(operator, processInstanceId, inscomcode, insConfigVO, ckp, rk, r);
								ckp.setCreatetime(date);
								if("0".equals(insConfigVO.getInskindtype())){
									//处理已选项json存储
									ckp.setSelecteditem(this.toSelecteditemJSON(null, asList, insConfigVO.getSelecteditemStrs(), ckp));//处理更改后的已选项json数据
								}else if("2".equals(insConfigVO.getInskindtype())){
									ckp.setSelecteditem("购买");
								}else if("3".equals(insConfigVO.getInskindtype())){
									ckp.setSelecteditem("代缴");
								}
								insbCarkindpriceDao.insert(ckp);
							}
							
							newCarkindprices.add(ckp);
							
							//更新特别险别配置信息
							if("05".equals(insConfigVO.getSpecialRiskkindFlag())){
								INSBSpecialkindconfig specialkindconfig = new INSBSpecialkindconfig();
								specialkindconfig.setTaskid(processInstanceId);
								specialkindconfig.setInscomcode(inscomcode);
								specialkindconfig.setKindcode(insConfigVO.getInskindcode());
								specialkindconfig = insbSpecialkindconfigDao.selectOne(specialkindconfig);
								if(specialkindconfig!=null){
									specialkindconfig.setModifytime(date);
									specialkindconfig.setOperator(operator);
									specialkindconfig.setCodevalue(insConfigVO.getSpecialRiskkindValue());
									insbSpecialkindconfigDao.updateById(specialkindconfig);
								}else{
									specialkindconfig = new INSBSpecialkindconfig();
									specialkindconfig.setModifytime(date);
									specialkindconfig.setCreatetime(date);
									specialkindconfig.setOperator(operator);
									specialkindconfig.setTaskid(processInstanceId);
									specialkindconfig.setInscomcode(inscomcode);
									specialkindconfig.setTypecode("05");
									specialkindconfig.setKindcode(insConfigVO.getInskindcode());
									specialkindconfig.setCodevalue(insConfigVO.getSpecialRiskkindValue());
									insbSpecialkindconfigDao.insert(specialkindconfig);
								}
							}
						}else{
							//删除特别险别配置信息
							if("04".equals(insConfigVO.getSpecialRiskkindFlag()) || "05".equals(insConfigVO.getSpecialRiskkindFlag())){
								INSBSpecialkindconfig specialkindconfig = new INSBSpecialkindconfig();
								specialkindconfig.setTaskid(processInstanceId);
								specialkindconfig.setInscomcode(inscomcode);
								specialkindconfig.setKindcode(insConfigVO.getInskindcode());
								List<INSBSpecialkindconfig> tempList = insbSpecialkindconfigDao.selectList(specialkindconfig);
								List<String> delIdList = new ArrayList<String>();
								for (INSBSpecialkindconfig skctemp : tempList) {
									delIdList.add(skctemp.getId());
								}
								insbSpecialkindconfigDao.deleteByIdInBatch(delIdList);
							}
							if(ckp != null){//修改前已选择了此险种，做删除处理
								insbCarkindpriceDao.deleteById(ckp.getId());
							}
						}
					}
				}else{//未勾选的
					//删除特别险别配置信息
					if("04".equals(insConfigVO.getSpecialRiskkindFlag()) || "05".equals(insConfigVO.getSpecialRiskkindFlag())){
						INSBSpecialkindconfig specialkindconfig = new INSBSpecialkindconfig();
						specialkindconfig.setTaskid(processInstanceId);
						specialkindconfig.setInscomcode(inscomcode);
						specialkindconfig.setKindcode(insConfigVO.getInskindcode());
						List<INSBSpecialkindconfig> tempList = insbSpecialkindconfigDao.selectList(specialkindconfig);
						List<String> delIdList = new ArrayList<String>();
						for (INSBSpecialkindconfig skctemp : tempList) {
							delIdList.add(skctemp.getId());
						}
						insbSpecialkindconfigDao.deleteByIdInBatch(delIdList);
					}
					//判断某种险种是否有保险配置记录
					INSBCarkindprice ckp = this.isHasThisCarkindPrice(carkindpriceList, insConfigVO.getInskindcode());
					if(ckp != null){//修改前已选择了此险种，做删除处理
						insbCarkindpriceDao.deleteById(ckp.getId());
					}
				}
			}
		}
		
		//修改总表insbcarconfig
		if(newCarkindprices.size() > 0){
			//先删除，后添加，与上面insbcarkindprice 一致, 否则平台查询回调获取投保类型就会有问题
			INSBCarconfig carconfig = new INSBCarconfig();
			carconfig.setTaskid(processInstanceId);
			List<INSBCarconfig> carconfigs = insbCarconfigDao.selectList(carconfig);
			if(null != carconfigs && carconfigs.size() > 0){
				LogUtil.info("CM更新保险配置, 删除已有的险别总表insbCarconfig,taskid="+processInstanceId+"操作人="+operator);
				for(INSBCarconfig insbCarconfig : carconfigs){
					insbCarconfigDao.deleteById(insbCarconfig.getId());
				}
			}
			
			List<INSBCarconfig> insbCarconfigs = new ArrayList<INSBCarconfig>();
			INSBCarconfig temp = null;
			for(INSBCarkindprice one : newCarkindprices){
				if(one == null) 
					continue;
				//去除上面删除的
				if(one.getId() != null && one.getId().length() > 0){
					if(insbCarkindpriceDao.selectById(one.getId()) == null)
						continue;
				}
				temp = new INSBCarconfig();
				temp.setOperator(one.getOperator());
				temp.setCreatetime(one.getCreatetime());
				temp.setTaskid(one.getTaskid());
				temp.setInskindtype(one.getInskindtype());
				temp.setInskindcode(one.getInskindcode());
				temp.setRiskcode(one.getRiskcode());
				temp.setAmount(one.getAmount()+"");
				temp.setAmountprice(one.getAmountprice());
				temp.setNotdeductible(one.getNotdeductible());
				temp.setSelecteditem(one.getSelecteditem());
				temp.setPreriskkind(one.getPreriskkind());
				temp.setPlankey(one.getPlankey());
				temp.setNoti(one.getInscomcode()); //放到中备注便于查数据
				insbCarconfigs.add(temp);
			}
			
			insbCarconfigDao.insertInBatch(insbCarconfigs);
			LogUtil.info("CM更新保险配置, 总表insbCarconfig的保险设置更新成功,taskid="+processInstanceId+"操作人="+operator);
		}
		return "success";
	}
	
	/**
	 * 判断前置险种是否选择
	 * liuchao
	 */
	public boolean hasPreriskkind(List<CarInsConfigVo> busiKindprice, INSBRiskkind rk){
		if(rk.getPreriskkind()!=null && !("".equals(rk.getPreriskkind()))){
			for (int j = 0; j < busiKindprice.size(); j++) {
				if("Y".equals(busiKindprice.get(j).getIsChecked()) && 
						rk.getPreriskkind().contains(busiKindprice.get(j).getInskindcode())){
					return true;
				}
			}
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 组织选项要素json数据
	 * liuchao
	 */
	public String toSelecteditemJSON(List<SelectOption> dbSOList, List<AmountSelect> asList, List<String> pgSOList, INSBCarkindprice ckp){
		//处理已选项json存储
		List<SelectOption> newSOList = new ArrayList<SelectOption>();
		if(dbSOList!=null){
			//不改变原来保额选项
			for (int i = 0; i < dbSOList.size(); i++) {
				SelectOption selectedOption = dbSOList.get(i);
				if(selectedOption !=null){
					// 处理错误数据。VehicleDemageIns的type是03的话，应该修改为01。
					if ("03".equals(selectedOption.getTYPE()) && ckp != null && "VehicleDemageIns".equals(ckp.getInskindcode())) {
						selectedOption.setTYPE("01");
					}
					if("01".equals(selectedOption.getTYPE())){
						newSOList.add(selectedOption);
						break;
					}
				}
			}
		}else{
			//设定保额选项为指定选项
			for (int i = 0; i < asList.size(); i++) {
				if("01".equals(asList.get(i).getTYPE())){
					SelectOption temp = new SelectOption();
					RisksData rd = new RisksData();
					rd.setKEY("指定");
					rd.setUNIT(asList.get(i).getVALUE().get(1).getUNIT());
					rd.setVALUE("");
					temp.setTYPE("01");
					temp.setVALUE(rd);
					newSOList.add(temp);
					break;
				}
			}
		}
		if(pgSOList!=null){
			//把页面中的除保额选项外选择要素选项结果添加到List中
			for (int i = 0; i < pgSOList.size(); i++) {
				SelectOption soTemp = this.toSelectOption(pgSOList.get(i));
				if(soTemp!=null){
					for (int j = 0; j < asList.size(); j++) {
						if(asList.get(j).getTYPE().equals(soTemp.getTYPE())){//可提供此选项要素
							newSOList.add(soTemp);
							break;
						}
					}
				}
			}
		}
		JSONArray jsonArray = JSONArray.fromObject(newSOList);
		return jsonArray.toString();
	}
	
	/**
	 * VO数据传递给INSBCarkindprice对象
	 * liuchao
	 */
	public INSBCarkindprice CarInsConfigVo2Carkindprice(String operator, String taskInstanceId, 
			String inscomcode, CarInsConfigVo insConfigVO, INSBCarkindprice carkindprice, 
			INSBRiskkind riskkind, INSBRisk risk){
		//新建carkindprice信息
		carkindprice.setOperator(operator);
		carkindprice.setTaskid(taskInstanceId);
		carkindprice.setInscomcode(inscomcode);
		carkindprice.setRiskcode(risk.getRiskcode());
//		carkindprice.setRiskname(insConfigVO.getRiskname());
		carkindprice.setRiskname(insConfigVO.getInskindname());
		carkindprice.setInskindcode(riskkind.getKindcode());
		carkindprice.setInskindtype(riskkind.getKindtype());
		if(insConfigVO.getAmount()==null){
			carkindprice.setAmount(0.0d);
		}else{
			carkindprice.setAmount(insConfigVO.getAmount());
		}
		carkindprice.setNotdeductible(riskkind.getNotdeductible());
		carkindprice.setDiscountCharge(insConfigVO.getAmountprice());
		carkindprice.setAmountprice(insConfigVO.getAmountprice());
		carkindprice.setPreriskkind(riskkind.getPreriskkind());
		return carkindprice;
	}
	
	/**
	 * 判断某种险种是否有保险配置记录
	 * liuchao
	 */
	public INSBCarkindprice isHasThisCarkindPrice(List<INSBCarkindprice> carkindpriceList, String kindcode){
		//获取每种险别是否有保险配置记录，如果有获取相应信息
		for (INSBCarkindprice insbCarkindprice : carkindpriceList) {
			if(insbCarkindprice.getInskindcode().equals(kindcode)){
				return insbCarkindprice;
			}
		}
		return null;
	}
	
	/**
	 * 判断保险公司可提供的险别里是否存在指定险种
	 * liuchao
	 */
	public INSBRiskkind isHasThisRiskkind(List<INSBRiskkind> riskkindList, String kindcode){
		INSBRiskkind riskkind = null;
		for (int i = 0; i < riskkindList.size(); i++) {
			if(riskkindList.get(i).getKindcode().equals(kindcode)){
				riskkind =  riskkindList.get(i);
				break;
			}
		}
		return riskkind;
	}
	
	/**
	 * 对页面返回的保险配置列表进行去除空对象处理
	 * liuchao
	 */
	public void removeNullItemsOfConfigList(List<CarInsConfigVo> KindpriceList){
		if(KindpriceList!=null){
			int i = 0;
			while(i < KindpriceList.size()){
				if(KindpriceList.get(i).getInskindcode()==null 
						&& KindpriceList.get(i).getInskindname()==null){//判断此元素是否为空
					KindpriceList.remove(i);//移除空元素
				}else{
					i++;
				}
			};
		}
	}
	
	/**
	 * 通过保险公司代码和任务id查询已选择的险别配置信息
	 * liuchao
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
//		List<Map<String, String>> orderedRiskkindList = insbRiskkindDao.selectOrderedRiskkindByInscomcode(inscomcode);
		for (int i = 0; i < orderedRiskkindList.size(); i++) {
//			Map<String, String> orderedRiskkind = orderedRiskkindList.get(i);
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
		INSBCarkindprice carkindpricetemp = new INSBCarkindprice();
		carkindpricetemp.setTaskid(processInstanceId);
		carkindpricetemp.setInscomcode(inscomcode);
		List<INSBCarkindprice> carkindpriceList = insbCarkindpriceDao.selectList(carkindpricetemp);
		//折后车船税、折后商业险总保费、折后交强险保费、其他费用
		double discountCarTax = 0d, discountBusTotalAmountprice = 0d, discountStrTotalAmountprice = 0d, otherAmountprice = 0d;
		INSBCarkindprice carkindprice = null;
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
					result.get(i).setSelecteditem(//险别要素已选项
							this.toSelectOptionList(carkindprice.getSelecteditem()));//json转为对象
					if(carkindprice.getDiscountCharge()!=null){
						discountBusTotalAmountprice += carkindprice.getDiscountCharge();
					}
//					if("PassengerIns".equals(orderedRiskkindList.get(i).get("kindcode"))){//乘客责任险显示单位元/座
//						Integer seats = getSeats(processInstanceId, inscomcode);
//						if(carkindprice.getAmount()!=null && seats!=null && seats!=0){
//							result.get(i).setAmount(douFormat(carkindprice.getAmount()/seats));//保额
//						}
//					}
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
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(processInstanceId);
		policyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
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
			resultMap.put("biztype", policyitemList.get(0).getBiztype());
		}
		return resultMap;
	}
	
	
	
	//得到座位数
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
	 * json转换为SelectOption对象
	 * liuchao
	 * */
	public SelectOption toSelectOption(String selectOptionJSON){
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("VALUE", RisksData.class);//指定复杂对象的类型
		JSONObject JsonObj = JSONObject.fromObject(selectOptionJSON);
		SelectOption selectOption = (SelectOption) JSONObject.toBean(JsonObj, SelectOption.class, classMap);
		return selectOption;
	}

	@Override
	public Map<String, Object> getPremiumInfo(String taskid, String inscomcode) {
		Map<String,Object> map = new HashMap<String, Object>();
		INSBCarkindprice temp = new INSBCarkindprice();
		temp.setTaskid(taskid);
		temp.setInscomcode(inscomcode);
		List<INSBCarkindprice> priceList = insbCarkindpriceDao.selectList(temp);
		if(priceList.size()>0){
			double sum = 0;
			for (INSBCarkindprice carkindprice : priceList) {
				if("0".equals(carkindprice.getInskindtype())){
					map.put("biPremium",carkindprice.getDiscountCharge());
				}else if("2".equals(carkindprice.getInskindtype())){
					map.put("ciPremium",carkindprice.getDiscountCharge());
				}else if("3".equals(carkindprice.getInskindtype())){
					map.put("taxPremium",carkindprice.getDiscountCharge());
				}else{
					map.put("otherPremium",carkindprice.getDiscountCharge());
				}
				sum+=carkindprice.getDiscountCharge()==null?0:carkindprice.getDiscountCharge();
			}
			map.put("sumPremium", sum);
		}
		return map;
	}

	@Override
	public List<Map<String, String>> selectAllriskkind(String inscomcode) {
		return insbCarkindpriceDao.selectAllriskkind(inscomcode);
	}

	@Override
	public double getTotalProductAmount(String taskid, String inscomcode) {
		Map<String, Object> map = new HashMap<>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		return insbCarkindpriceDao.getTotalProductAmount(map);
	}

	/**
	 * 查询特殊险别下的配置信息，如新增设备损失险
	 */
	@Override
	public List<Map<String, Object>> getSpecialRiskkindcfg(String mInstanceid,
			String inscomcode, String riskkindcode) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		INSBSpecialkindconfig insbSpecialkindconfig = new INSBSpecialkindconfig();
		insbSpecialkindconfig.setTaskid(mInstanceid);
		insbSpecialkindconfig.setInscomcode(inscomcode);
		insbSpecialkindconfig.setKindcode(riskkindcode);
		List<INSBSpecialkindconfig> insbSpecialkindconfigList = insbSpecialkindconfigDao.selectList(insbSpecialkindconfig);
		if(insbSpecialkindconfigList!=null && insbSpecialkindconfigList.size()>0){
			for(INSBSpecialkindconfig temp : insbSpecialkindconfigList){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", temp.getId());
				map.put("key", temp.getCodekey());
				map.put("value", temp.getCodevalue());
				result.add(map);
			}
		}
		return result;
	}

	/**
	 * 修改特殊险别下的配置信息，如新增设备损失险
	 */
	@Override
	public Map<String, String> editSpecialRiskkindcfg(SpecialRiskkindCfgVo specialRiskkindCfg) {
		Map<String, String> result = new HashMap<String, String>();
		String mInstanceid = specialRiskkindCfg.getmInstanceid();
		String inscomcode = specialRiskkindCfg.getInscomcode();
		String riskkindcode = specialRiskkindCfg.getRiskkindcode();
		String inscomcodeList = specialRiskkindCfg.getInscomcodeList();
		String operator = specialRiskkindCfg.getOperator();
		String resultStr = "";
		StringBuffer uncomlist = new StringBuffer();
		INSBProvider temp = null;
		if("Y".equals(specialRiskkindCfg.getEditToAll())){//修改到所有单方
			String[] comList = inscomcodeList.split(",");
			for(int i=0; i<comList.length; i++){
				resultStr = editSpecialRiskkindcfgForOneCom(mInstanceid, comList[i], operator, 
						"04", riskkindcode, specialRiskkindCfg.getSpcRiskkindCfg());
				if("fail".equals(resultStr)){
					temp = new INSBProvider();
					temp.setPrvcode(comList[i]);
					temp = insbProviderDao.selectOne(temp);
					if(temp!=null){
						uncomlist.append(temp.getPrvshotname()+",");
					}
				}
			}
		}else{//只修改此保险公司
			resultStr = editSpecialRiskkindcfgForOneCom(mInstanceid, inscomcode, operator, 
					"04", riskkindcode, specialRiskkindCfg.getSpcRiskkindCfg());
			if("fail".equals(resultStr)){
				temp = new INSBProvider();
				temp.setPrvcode(inscomcode);
				temp = insbProviderDao.selectOne(temp);
				if(temp!=null){
					uncomlist.append(temp.getPrvshotname()+",");
				}
			}
		}
		if("".equals(uncomlist.toString())){
			result.put("status", "success");
			result.put("msg", "修改险别配置信息成功！");
		}else{
			result.put("status", "success");
			result.put("msg", "修改险别配置信息成功！"+uncomlist.toString()+"以上保险公司不支持此险别已在保存时去除！");
		}
		return result;
	}
	
	/**
	 * 修改特殊险别下的配置信息子方法
	 */
	public String editSpecialRiskkindcfgForOneCom(String mInstanceid, String inscomcode, String operator, 
			String typeCode, String riskkindcode, List<SpecialRiskkindCfg> spcRiskkindCfg) {
		//首先查询此保险公司是否选择了此此险别
//		INSBCarkindprice carkindprice = new INSBCarkindprice();
//		carkindprice.setTaskid(mInstanceid);
//		carkindprice.setInscomcode(inscomcode);
//		carkindprice.setInskindcode(riskkindcode);
//		carkindprice = insbCarkindpriceDao.selectOne(carkindprice);
//		boolean hasriskkind = false;
//		if(carkindprice!=null){
//			hasriskkind = true;
//		}
		//判断此保险公司是否支持此险种
		String provideid =insbProviderDao.selectFatherProvider(inscomcode).getId();
		//得到此保险公司的可提供的险别选项信息(用于修改到所有单方时判断此保险公司是否支持)
		List<INSBRiskkind> riskkindList = new ArrayList<INSBRiskkind>();
		INSBRisk risk = new INSBRisk();
		risk.setProvideid(provideid);
		List<INSBRisk> riskList = insbRiskDao.selectList(risk);
		for (int i = 0; i < riskList.size(); i++) {
			INSBRiskkind temp = new INSBRiskkind();
			temp.setRiskid(riskList.get(i).getId());
			List<INSBRiskkind> tempList = insbRiskkindDao.selectList(temp);
			riskkindList.addAll(tempList);
		}
		INSBRiskkind rk = this.isHasThisRiskkind(riskkindList, riskkindcode);//判断保险公司可提供的险别里是否存在指定险种
		if(rk!=null){
			//查询出已存在的特殊险别配置信息
			INSBSpecialkindconfig specialkindconfig = new INSBSpecialkindconfig();
			specialkindconfig.setTaskid(mInstanceid);
			specialkindconfig.setInscomcode(inscomcode);
			specialkindconfig.setKindcode(riskkindcode);
			List<INSBSpecialkindconfig> insbSpecialkindconfigList = insbSpecialkindconfigDao.selectList(specialkindconfig);
			//删除已经删除的配置记录
			for (INSBSpecialkindconfig temp : insbSpecialkindconfigList) {
				for (int i=0; i<spcRiskkindCfg.size(); i++) {
					if(temp.getId().equals(spcRiskkindCfg.get(i).getId())){
						break;
					}
					if(i==(spcRiskkindCfg.size()-1)){
						insbSpecialkindconfigDao.deleteById(temp.getId());
					}
				}
			}
			//修改和插入页面中的配置信息
			for (int i=0; i<spcRiskkindCfg.size(); i++) {
				if(spcRiskkindCfg.get(i).getId() != null){
					for (INSBSpecialkindconfig temp : insbSpecialkindconfigList) {
						if(temp.getId().equals(spcRiskkindCfg.get(i).getId())){
							temp.setModifytime(new Date());
							temp.setOperator(operator);
							temp.setCodekey(spcRiskkindCfg.get(i).getCfgKey());
							temp.setCodevalue(spcRiskkindCfg.get(i).getCfgValue());
							insbSpecialkindconfigDao.updateById(temp);
						}
					}
				}else{
					INSBSpecialkindconfig temp = new INSBSpecialkindconfig();
					temp.setModifytime(new Date());
					temp.setCreatetime(new Date());
					temp.setOperator(operator);
					temp.setTaskid(mInstanceid);
					temp.setInscomcode(inscomcode);
					temp.setTypecode(typeCode);
					temp.setKindcode(riskkindcode);
					temp.setCodekey(spcRiskkindCfg.get(i).getCfgKey());
					temp.setCodevalue(spcRiskkindCfg.get(i).getCfgValue());
					insbSpecialkindconfigDao.insert(temp);
				}
			}
			return "success";
		}else{
			return "fail";
		}
	}
}