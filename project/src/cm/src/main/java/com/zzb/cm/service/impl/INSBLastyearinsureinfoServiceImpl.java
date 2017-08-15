package com.zzb.cm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zzb.cm.dao.INSBRulequerycarinfoDao;
import com.zzb.mobile.model.ExtendCommonModel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.CloudQueryUtil;
import com.common.ModelUtil;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBLastyearinsurestatusDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBLastyearinsureinfo;
import com.zzb.cm.entity.INSBLastyearinsurestatus;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.service.INSBLastyearinsureinfoService;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.LastYearBizClaimsBean;
import com.zzb.mobile.model.LastYearClaimBean;
import com.zzb.mobile.model.lastindanger.BizClaims;
import com.zzb.mobile.model.lastindanger.LastClaimBackInfo;
import com.zzb.mobile.model.lastindanger.LastPolicy;
import com.zzb.mobile.service.AppInsuredQuoteService;

@Service
@Transactional
public class INSBLastyearinsureinfoServiceImpl extends BaseServiceImpl<INSBLastyearinsureinfo> implements
		INSBLastyearinsureinfoService {
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBLastyearinsurestatusDao insbLastyearinsurestatusDao;
	@Resource
	private INSBRulequerycarinfoDao insbRulequerycarinfoDao;
	@Resource
	public  Scheduler sched;
	@Resource
	private AppInsuredQuoteDao appInsuredQuoteDao;
	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;

	@Override
	public CommonModel queryLastYearClainInfo(String taskid) {
		CommonModel commonModel=new CommonModel();
		try {
			if(StringUtil.isEmpty(taskid)){
				commonModel.setMessage("实例id不能为空");
				commonModel.setStatus("fail");
				return commonModel;
			}
			INSBLastyearinsureinfo insbLastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);
			LastYearClaimBean yearClaimBean = new LastYearClaimBean();
			if(null != insbLastyearinsureinfo){
				yearClaimBean.setClaimtimes(null == insbLastyearinsureinfo.getSyclaimtimes()?0:insbLastyearinsureinfo.getSyclaimtimes());//理赔次数
				yearClaimBean.setClaimrate(null == insbLastyearinsureinfo.getSyclaimrate()?0.0:insbLastyearinsureinfo.getSyclaimrate());//理赔率
				yearClaimBean.setJqclaimtimes((null == insbLastyearinsureinfo.getJqclaimtimes()?0:insbLastyearinsureinfo.getJqclaimtimes())+"");//交强理赔次数
				yearClaimBean.setJqclaimrate(null == insbLastyearinsureinfo.getJqclaimrate()?0.0:insbLastyearinsureinfo.getJqclaimrate());//交强理赔率
				yearClaimBean.setFirstinsuretype(INSBLastyearinsureinfo.convertFirstInsureTypeToCm(insbLastyearinsureinfo.getFirstinsuretype()));//投保类型 非首次投保:0,新车首次投保:1,旧车首次投保:2
				yearClaimBean.setLastclaimsum(null == insbLastyearinsureinfo.getSylastclaimsum()?0.0:insbLastyearinsureinfo.getSylastclaimsum());//理赔金额
				yearClaimBean.setJqlastclaimsum(null == insbLastyearinsureinfo.getJqlastclaimsum()?0.0:insbLastyearinsureinfo.getJqlastclaimsum());//交强理赔金额
				yearClaimBean.setTrafficoffence((null == insbLastyearinsureinfo.getTrafficoffence()?0:insbLastyearinsureinfo.getTrafficoffence())+"");//交通违规次数
				yearClaimBean.setTrafficoffencediscount(null == insbLastyearinsureinfo.getTrafficoffencediscount()?0.0:insbLastyearinsureinfo.getTrafficoffencediscount());//交通违规系数
				yearClaimBean.setBwcommercialclaimtimes(insbLastyearinsureinfo.getBwcommercialclaimtimes());; // 商业险理赔次数
				yearClaimBean.setNoclaimdiscountcoefficient(insbLastyearinsureinfo.getNoclaimdiscountcoefficient());;// 无赔款优待系数-
				yearClaimBean.setCompulsoryclaimrate(null == insbLastyearinsureinfo.getCompulsoryclaimrate()?0.0:insbLastyearinsureinfo.getCompulsoryclaimrate());;// 交强险理赔系数-
				yearClaimBean.setBwcompulsoryclaimtimes(insbLastyearinsureinfo.getBwcompulsoryclaimtimes());;//交强险理赔次数
				yearClaimBean.setNoclaimdiscountcoefficientreasons(insbLastyearinsureinfo.getNoclaimdiscountcoefficientreasons());;// 无赔款折扣浮动原因-
				yearClaimBean.setCompulsoryclaimratereasons(insbLastyearinsureinfo.getCompulsoryclaimratereasons());;// 交强险事故浮动原因
				yearClaimBean.setSyclaims(convertToArrayList(insbLastyearinsureinfo.getSyclaims()));
				yearClaimBean.setJqclaims(convertToArrayList(insbLastyearinsureinfo.getJqclaims()));
			}
			commonModel.setMessage("操作成功");
			commonModel.setStatus("success");
			commonModel.setBody(yearClaimBean);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setMessage("操作失敗");
			commonModel.setStatus("fail");
		}
		return commonModel;
	}

	/**
	 * [{"caseEndTime":"2015-01-25 00:00:00.0","caseStartTime":"2015-01-24 00:00:00.0","insCorpCode":"CICP","insCorpName":"中华联合财产保险公司","policyId":"PDAA201544190000122719"}]
	 * @param syclaims 
	 * @return
	 */
	private List<LastYearBizClaimsBean> convertToArrayList(String syclaims) {
		if(StringUtil.isEmpty(syclaims) || !syclaims.contains("[")){
			return null;
		}
		JSONArray jsonArray = JSONArray.fromObject(syclaims);
		List<LastYearBizClaimsBean> bizClaimsBeans = new ArrayList<LastYearBizClaimsBean>();//商业险事故
		if(!jsonArray.isEmpty()){
			for(int i = 0;i < jsonArray.size();i ++){
				JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(i));
				if(jsonObject.isEmpty()){
					continue;
				}
				LastYearBizClaimsBean bean = new LastYearBizClaimsBean();
				bean.setCaseStartTime(jsonObject.optString("casestarttime"));
				bean.setCaseEndTime(jsonObject.optString("caseendtime"));
				bean.setInsCorpCode(jsonObject.optString("inscorpid"));
				bean.setInsCorpName(jsonObject.optString("inscorpname"));
				bean.setPolicyId(jsonObject.optString("policyid"));
				bean.setClaimAmount(jsonObject.containsKey("claimamount")?jsonObject.getDouble("claimamount"):0.0);
				bizClaimsBeans.add(bean);
			}
		}
		return bizClaimsBeans;
	}

	/**
	 * 普通平台查询返回数据校验
	 * 发动机号  vin码 处登记日期 车型名称
	 * @param taskid
	 * @param lastClaimBackInfo
	 * @return true 校验不通过，不入库
	 */
	private boolean compareData(String taskid,LastClaimBackInfo lastClaimBackInfo){
		//车辆信息校验，发动机，vin，初登记日期，车型名称
		//查询本地库数据
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(taskid);
		INSBCarinfo carinfo = insbCarinfoDao.selectOne(insbCarinfo);
		if(null == carinfo || null == lastClaimBackInfo){
			return true;
		}
		//如果carinfo中没有vin码，发动机号，处登记日期，默认不校验
		if(StringUtil.isEmpty(carinfo.getVincode()) || StringUtil.isEmpty(carinfo.getEngineno()) || null == carinfo.getRegistdate()){
			return false;
		}
		String vehiclename = "";
		//获取车型名称
		INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
		carmodelinfo.setCarinfoid(carinfo.getId());
		INSBCarmodelinfo insbCarmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
		if(null != insbCarmodelinfo){
			vehiclename = insbCarmodelinfo.getStandardfullname();
		}else{
			LogUtil.info(taskid+","+carinfo.getId()+"无车型信息");
			return true;
		}

		String registdate = ModelUtil.conbertToString(carinfo.getRegistdate());
		LogUtil.info(taskid+"车信息:Vincode="+carinfo.getVincode()+",Engineno="+carinfo.getEngineno()+",vehiclename="+vehiclename+",registdate="+registdate);

		return !carinfo.getVincode().equals(lastClaimBackInfo.getVin()) && !carinfo.getEngineno().equals(lastClaimBackInfo.getEngineNum())
				&& !vehiclename.equals(lastClaimBackInfo.getCarBrandName()) && !lastClaimBackInfo.getFirstRegDate().startsWith(registdate);
	}
	
	/**
	 * 如果大于80秒返回数据不入库
	 */
	@Override
	public CommonModel saveLastYearClaimsInfo(String taskid,LastClaimBackInfo lastClaimBackInfo) {
		LogUtil.info("普通平台查询回调保存开始"+taskid+"=saveLastYearClaimsInfo");
        ExtendCommonModel commonModel = new ExtendCommonModel();
		try {
			if(compareData(taskid, lastClaimBackInfo)){
				LogUtil.info("普通平台查询回调保存开始"+taskid+"=回调数据与当前数据库数据不匹配，不入库=saveLastYearClaimsInfo");
				commonModel.setStatus("fail");
				commonModel.setMessage("回调数据与当前数据库数据不匹配，不入库");
				return commonModel;
			}
			long times = 0;
			// 跟新平台查询表状态
			INSBLastyearinsurestatus insbLastyearinsurestatus = new INSBLastyearinsurestatus();
			insbLastyearinsurestatus.setTaskid(taskid);
			INSBLastyearinsurestatus lastyearinsurestatus = insbLastyearinsurestatusDao.selectOne(insbLastyearinsurestatus);
			if (null != lastyearinsurestatus) {
				lastyearinsurestatus.setOperator("平台查询");
				lastyearinsurestatus.setModifytime(new Date());
				lastyearinsurestatus.setCifendtime(new Date());
				// 80秒超时
				if(null != lastyearinsurestatus.getCifstarttime()){
					times = ModelUtil.twoDatesDifferSeconds(new Date(),lastyearinsurestatus.getCifstarttime());
				}
				LogUtil.info("普通平台查询回调保存开始"+taskid+"=saveLastYearClaimsInfo=平台查询耗时" + times + "秒");
				lastyearinsurestatus.setCifflag("0");// 默认未超时
				if (times > 80) {
					lastyearinsurestatus.setCifflag("1");// 超时了
				}
				insbLastyearinsurestatusDao.updateById(lastyearinsurestatus);
			}
			//未超时回调数据,数据入库
			if(times <= 80){
				CloudQueryUtil.setLastClaimBackInfo(taskid, lastClaimBackInfo);
				INSBLastyearinsureinfo lastyearinsureinfo = null;
				if(null != lastClaimBackInfo){
					//商业险理赔事故
					List<BizClaims> syclaimslist = lastClaimBackInfo.getBizClaims();
					String syclaims = "";
					if(null != syclaimslist && syclaimslist.size() > 0){
						JSONArray array = JSONArray.fromObject(syclaimslist);
						syclaims = array.toString();
					}
					//交强险理赔事故
					List<BizClaims> jqclaimslist = lastClaimBackInfo.getEfcClaims();
					String jqclaims = "";
					if(null != jqclaimslist && jqclaimslist.size() > 0){
						JSONArray array = JSONArray.fromObject(jqclaimslist);
						jqclaims = array.toString();
					}
					//商业险保单号，与投保时间
					List<LastPolicy> syLastPolicies = lastClaimBackInfo.getBizPolicies();
					//交强险保单号，与投保时间
					List<LastPolicy> jqLastPolicies = lastClaimBackInfo.getEfcPolicies();

					lastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskid);
					if(null == lastyearinsureinfo){
						lastyearinsureinfo = new INSBLastyearinsureinfo();
						lastyearinsureinfo.setCreatetime(new Date());
						lastyearinsureinfo.setOperator("cifback");
						lastyearinsureinfo.setTaskid(taskid);

						lastyearinsureinfo.setSyclaimrate(convertToDouble(lastClaimBackInfo.getBwCommercialClaimRate()));//商业理赔系数
						lastyearinsureinfo.setSyclaimtimes(lastClaimBackInfo.getClaimTimes());//平台商业险理赔次数
						lastyearinsureinfo.setJqclaimtimes(convertToInteger(lastClaimBackInfo.getCompulsoryClaimTimes()));//交强险理赔次数
						lastyearinsureinfo.setJqclaimrate(lastClaimBackInfo.getCompulsoryClaimRate());//交强险理赔系数
						lastyearinsureinfo.setFirstinsuretype(convertFirstInsureTypeToCm(lastClaimBackInfo.getFirstInsureType()));//投保类型 非首次投保:0,新车首次投保:1,旧车首次投保:2
						lastyearinsureinfo.setSylastclaimsum(lastClaimBackInfo.getBwLastClaimSum());//商业险理赔金额
//						lastyearinsureinfo.setJqlastclaimsum(jqlastclaimsum);//交强理赔金额
//						lastyearinsureinfo.setTrafficoffence(convertToInteger(trafficoffence));//交通违规次数
						lastyearinsureinfo.setTrafficoffencediscount(lastClaimBackInfo.getTrafficOffenceDiscount());//交通违规系数

						lastyearinsureinfo.setNoclaimdiscountcoefficient(lastClaimBackInfo.getNoClaimDiscountCoefficient());// 无赔款优待系数-
						lastyearinsureinfo.setCompulsoryclaimrate(lastClaimBackInfo.getCompulsoryClaimRate());//交强险理赔系数
						lastyearinsureinfo.setBwcompulsoryclaimtimes(lastClaimBackInfo.getBwCompulsoryClaimTimes());//上年交强险理赔次数
						lastyearinsureinfo.setBwcommercialclaimtimes(lastClaimBackInfo.getBwCommercialClaimTimes());//上年商业险理赔次数
						lastyearinsureinfo.setNoclaimdiscountcoefficientreasons(lastClaimBackInfo.getNoClaimDiscountCoefficientReasons());//无赔款折扣浮动原因
						lastyearinsureinfo.setCompulsoryclaimratereasons(lastClaimBackInfo.getCompulsoryClaimRateReasons());//交强险事故浮动原因
						lastyearinsureinfo.setSyclaims(syclaims);//商业险理赔列表
						lastyearinsureinfo.setJqclaims(jqclaims);//交强险理赔列表

						//上年投保公司信息
						lastyearinsureinfo.setSuppliername(findLastSupplierName(lastClaimBackInfo.getCmCompanyCode()));
						lastyearinsureinfo.setSupplierid(lastClaimBackInfo.getCmCompanyCode());
						//商业交强投保单号，投保时间
						if(null != syLastPolicies && syLastPolicies.size() > 0){
							lastyearinsureinfo.setSypolicyno(syLastPolicies.get(0).getPolicyId());
							lastyearinsureinfo.setSystartdate(syLastPolicies.get(0).getPolicyStartTime());
							lastyearinsureinfo.setSyenddate(syLastPolicies.get(0).getPolicyEndTime());
//							lastyearinsureinfo.setSuppliername(syLastPolicies.get(0).getInsCorpName());
//							String supplierid = findLastSupplierId(syLastPolicies.get(0));
//							lastyearinsureinfo.setSupplierid(supplierid);
						}
						if(null != jqLastPolicies && jqLastPolicies.size() > 0){
							lastyearinsureinfo.setJqpolicyno(jqLastPolicies.get(0).getPolicyId());
							lastyearinsureinfo.setJqstartdate(jqLastPolicies.get(0).getPolicyStartTime());
							lastyearinsureinfo.setJqenddate(jqLastPolicies.get(0).getPolicyEndTime());
						}
						lastyearinsureinfo.setSflag("2");
						//cif平台查询重复投保大于90天提示
						lastyearinsureinfo.setRepeatinsurance(lastClaimBackInfo.getRepeatInsurance());
						lastyearinsureinfo.setJqrepeatinsurance(lastClaimBackInfo.getJqrepeatInsurance());
						lastyearinsureinfo.setLoyaltyreasons(lastClaimBackInfo.getLoyaltyReasons());//客户忠诚度原因
						//insbLastyearinsureinfoDao.insert(lastyearinsureinfo);
					}else{
						lastyearinsureinfo.setModifytime(new Date());
						lastyearinsureinfo.setOperator("cifback");
						lastyearinsureinfo.setTaskid(taskid);

						lastyearinsureinfo.setSyclaimrate(convertToDouble(lastClaimBackInfo.getBwCommercialClaimRate()));//商业理赔系数
						lastyearinsureinfo.setSyclaimtimes(lastClaimBackInfo.getClaimTimes());//平台商业险理赔次数
						lastyearinsureinfo.setJqclaimtimes(convertToInteger(lastClaimBackInfo.getCompulsoryClaimTimes()));//交强险理赔次数
						lastyearinsureinfo.setJqclaimrate(lastClaimBackInfo.getCompulsoryClaimRate());//交强险理赔系数
						lastyearinsureinfo.setFirstinsuretype(convertFirstInsureTypeToCm(lastClaimBackInfo.getFirstInsureType()));//投保类型 非首次投保:0,新车首次投保:1,旧车首次投保:2
						lastyearinsureinfo.setSylastclaimsum(lastClaimBackInfo.getBwLastClaimSum());//商业险理赔金额
//						lastyearinsureinfo.setJqlastclaimsum(jqlastclaimsum);//交强理赔金额
//						lastyearinsureinfo.setTrafficoffence(convertToInteger(trafficoffence));//交通违规次数
						lastyearinsureinfo.setTrafficoffencediscount(lastClaimBackInfo.getTrafficOffenceDiscount());//交通违规系数

						lastyearinsureinfo.setNoclaimdiscountcoefficient(lastClaimBackInfo.getNoClaimDiscountCoefficient());// 无赔款优待系数-
						lastyearinsureinfo.setCompulsoryclaimrate(lastClaimBackInfo.getCompulsoryClaimRate());//交强险理赔系数
						lastyearinsureinfo.setBwcompulsoryclaimtimes(lastClaimBackInfo.getBwCompulsoryClaimTimes());//上年交强险理赔次数
						lastyearinsureinfo.setBwcommercialclaimtimes(lastClaimBackInfo.getBwCommercialClaimTimes());//上年商业险理赔次数
						lastyearinsureinfo.setNoclaimdiscountcoefficientreasons(lastClaimBackInfo.getNoClaimDiscountCoefficientReasons());//无赔款折扣浮动原因
						lastyearinsureinfo.setCompulsoryclaimratereasons(lastClaimBackInfo.getCompulsoryClaimRateReasons());//交强险事故浮动原因
						lastyearinsureinfo.setSyclaims(syclaims);//商业险理赔列表
						lastyearinsureinfo.setJqclaims(jqclaims);//交强险理赔列表

						//上年投保公司信息
						lastyearinsureinfo.setSuppliername(findLastSupplierName(lastClaimBackInfo.getCmCompanyCode()));
						lastyearinsureinfo.setSupplierid(lastClaimBackInfo.getCmCompanyCode());
						//商业交强投保单号，投保时间
						if(null != syLastPolicies && syLastPolicies.size() > 0){
							lastyearinsureinfo.setSypolicyno(syLastPolicies.get(0).getPolicyId());
							lastyearinsureinfo.setSystartdate(syLastPolicies.get(0).getPolicyStartTime());
							lastyearinsureinfo.setSyenddate(syLastPolicies.get(0).getPolicyEndTime());
//							lastyearinsureinfo.setSuppliername(syLastPolicies.get(0).getInsCorpName());
//							String supplierid = findLastSupplierId(syLastPolicies.get(0));
//							lastyearinsureinfo.setSupplierid(supplierid);
						}
						if(null != jqLastPolicies && jqLastPolicies.size() > 0){
							lastyearinsureinfo.setJqpolicyno(jqLastPolicies.get(0).getPolicyId());
							lastyearinsureinfo.setJqstartdate(jqLastPolicies.get(0).getPolicyStartTime());
							lastyearinsureinfo.setJqenddate(jqLastPolicies.get(0).getPolicyEndTime());
						}
						lastyearinsureinfo.setSflag("2");
						//cif平台查询重复投保大于90天提示
						lastyearinsureinfo.setRepeatinsurance(lastClaimBackInfo.getRepeatInsurance());
						lastyearinsureinfo.setJqrepeatinsurance(lastClaimBackInfo.getJqrepeatInsurance());
						lastyearinsureinfo.setLoyaltyreasons(lastClaimBackInfo.getLoyaltyReasons());//客户忠诚度原因
						//insbLastyearinsureinfoDao.updateById(lastyearinsureinfo);
					}
					commonModel.setExtend(Boolean.TRUE);
				}
				
				LogUtil.info("普通平台查询回调保存开始"+taskid+"=saveLastYearClaimsInfo=平台查询耗时小于80秒");
				List<INSBQuoteinfo> insbQuoteinfos = appInsuredQuoteDao.getSelectedProvidersByTaskid(taskid);
				if(null != insbQuoteinfos && insbQuoteinfos.size() > 0){
					for(INSBQuoteinfo insbQuoteinfo : insbQuoteinfos){
						String joname = taskid+"#"+insbQuoteinfo.getWorkflowinstanceid()+"#"+insbQuoteinfo.getInscomcode()+"#cifqueryjob";
						//删除定时器任务，保存数据
						JobKey jobKey = new JobKey(joname);
						if(sched.deleteJob(jobKey)){//true 定时器存在
							LogUtil.info("普通平台查询回调保存开始"+taskid+"=saveLastYearClaimsInfo=平台查询耗时小于80秒="+"关闭定时器jobname="+joname);
							//保存数据
							appInsuredQuoteService.dateIsRepeatInsured(taskid, insbQuoteinfo.getWorkflowinstanceid(), insbQuoteinfo.getInscomcode(),
									lastyearinsureinfo.getSyenddate(), lastyearinsureinfo.getJqenddate(),
									lastyearinsureinfo.getRepeatinsurance(), lastyearinsureinfo.getJqrepeatinsurance(),lastyearinsureinfo.getSupplierid());
							LogUtil.info("普通平台查询回调保存开始"+taskid+"=上年投保公司id="+lastyearinsureinfo.getSupplierid()+"=syenddate="+lastyearinsureinfo.getSyenddate()+"=jqenddate="+lastyearinsureinfo.getJqenddate());
							LogUtil.info("普通平台查询回调保存开始"+taskid+"=重复投保提示商业险="+lastyearinsureinfo.getRepeatinsurance()+"=重复投保提示交强险="+lastyearinsureinfo.getJqrepeatinsurance());
						}
					}
				}
				commonModel.setBody(lastyearinsureinfo.getSupplierid());
			}else{
				LogUtil.info("普通平台查询回调保存开始"+taskid+"=数据回调超时不入库=times="+times);
			}
			LogUtil.info("普通平台查询回调保存开始"+taskid+"=saveLastYearClaimsInfo结束了");
			commonModel.setStatus("success");
			commonModel.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}
	
	private String findLastSupplierName(String proid) {
		String resname = "";
		if(!StringUtil.isEmpty(proid)){
			INSBProvider insbProvider = insbProviderDao.selectById(proid);
			resname = null == insbProvider ? "" : insbProvider.getPrvshotname();
		}
		return resname;
	}

	/**
	 * 转换不合法默认为0
	 * @param str
	 * @return
	 */
	private int convertToInteger(String str){
		return StringUtil.isEmpty(str) || !ModelUtil.isNumeric(str)?0:Integer.parseInt(str);
	}
	private double convertToDouble(String str){
		return StringUtil.isEmpty(str) || !ModelUtil.isNumeric(str)?0:Double.parseDouble(str);
	}
	/**
	 * 非首次投保:0,新车首次投保:1,旧车首次投保:2
	 * @param firstInsureType
	 * @return
	 */
	private String convertFirstInsureTypeToCm(String firstInsureType){
		Map<String, String> map = new HashMap<String, String>();
		map.put("非首次投保", "0");
		map.put("新车首次投保", "1");
		map.put("旧车首次投保", "2");
		return StringUtil.isEmpty(map.get(firstInsureType))?"":map.get(firstInsureType);
	}

	@Override
	protected BaseDao<INSBLastyearinsureinfo> getBaseDao() {
		return null;
	}
}