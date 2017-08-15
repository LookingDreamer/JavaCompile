package com.zzb.mobile.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.zzb.cm.service.INSBInsuredhisService;
import com.zzb.cm.service.INSBInsuresupplyparamService;
import com.zzb.cm.service.INSBPersonService;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.*;
import com.zzb.mobile.model.policyoperat.EditPolicyInfoParam;
import com.zzb.mobile.service.AppGeneralSettingService;

import com.zzb.mobile.service.INSBPaymentService;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCCodeService;
import com.common.ModelUtil;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.APPBorderService;
import com.zzb.mobile.service.INSBMyOrderService;
/**
 *
 * @author Administrator
 *根据任务id，查询获得此任务下的生成订单的信息
 */
@Service
@Transactional
public class APPBorderServiceImpl extends BaseServiceImpl<INSBOrder> implements APPBorderService{

	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBOrderdeliveryDao insbOrderdeliveryDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBCarkindpriceDao carkindpriceDao;
	@Resource
	private INSBOutorderdeptDao insbOutorderdeptDao;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;
	@Resource
	private INSBPaychannelDao insbPaychannelDao;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBMyOrderService insbMyOrderService;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
    @Resource
    private AppGeneralSettingService appGeneralSettingService;
	@Resource
	private INSBPaymentService insbPaymentService;
	@Resource
	INSBCarinfoDao insbCarinfoDao;
	@Resource
	INSBInsuredhisService insbInsuredhisService;
	@Resource
	INSBPersonService insbPersonService;
	@Resource
	INSBDistributiontypeDao insbDistributiontypeDao;
	@Resource
	INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	INSBPersonDao insbPersonDao;
	@Resource
	private INSBInsuresupplyparamService insbInsuresupplyparamService;


	@Override
	protected BaseDao<INSBOrder> getBaseDao() {
		return insbOrderDao;
	}

	/*
	 * 支付方式
	 */
	private INSBPaychannel getPayMethod(String processInstanceId,String orderid){
		INSBOrderpayment insbOrderpayment=new INSBOrderpayment();
		insbOrderpayment.setTaskid(processInstanceId);
		insbOrderpayment.setOrderid(orderid);
		insbOrderpayment=insbOrderpaymentDao.selectOne(insbOrderpayment);
		INSBPaychannel insbPaychannel=null;
		if(insbOrderpayment!=null){
			insbPaychannel=insbPaychannelDao.selectById(insbOrderpayment.getPaychannelid());
		}
		return insbPaychannel;
	}
	/*
	 * (non-Javadoc)
	 * @see com.zzb.mobile.service.APPBorderService#policyitem(java.lang.String)
	 * 保单表信息
	 */
	public List<Object> policyitem(String processInstanceId,String inscomcode) {
//		INSBPolicyitem policyitem=insbPolicyitemDao.selectPolicyitemByTaskId(processInstanceId);
//		INSBPolicyitem policyitemOne=new INSBPolicyitem();
//		policyitemOne.setTaskid(processInstanceId);
//		policyitemOne.setInscomcode(inscomcode);
//		policyitemOne.setRisktype("0");
//		INSBPolicyitem Query_policyitem=insbPolicyitemDao.selectOne(policyitemOne);
//		Date today=new Date();
//		//当前时间比较保单时间，比较的时间为结束时间
//		Date date=policyitem.getEnddate();
//		List<Object> policyitemList=new ArrayList<Object>();
//		Map<String, Object> PolicyitemMap=new HashMap<String, Object>();
//		 if(date==null){
//			 String startRisk=null;
//			 if(today.before(Query_policyitem.getEnddate())){
//					startRisk="true";
//					PolicyitemMap.put("startRisk",startRisk);
//				}else{
//					startRisk="false";
//					PolicyitemMap.put("startRisk",startRisk);
//				}
//		 }else{
//			if(today.before(date)&&date!=null){
//				String startRisk="true";
//				PolicyitemMap.put("startRisk",startRisk);
//			}else{
//				String startRisk="false";
//				PolicyitemMap.put("startRisk",startRisk);
//			}
//		}
		INSBPolicyitem policyitem=insbPolicyitemDao.selectPolicyitemByTaskId(processInstanceId);
		List<Object> policyitemList=new ArrayList<Object>();
		Map<String, Object> PolicyitemMap=new HashMap<String, Object>();
		PolicyitemMap.put("startRisk",judjePayDate(processInstanceId, inscomcode));
		PolicyitemMap.put("Paynum",policyitem.getPaynum());
		PolicyitemMap.put("Checkcode",policyitem.getCheckcode());
		PolicyitemMap.put("Startdate",policyitem.getCreatetime());//订单录入时间
		PolicyitemMap.put("risktype", policyitem.getRisktype());
		policyitemList.add(PolicyitemMap);
		return policyitemList;
	}
	/**
	 * 支付有效期 false 过期
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	private String judjePayDate(String taskid,String inscomcode){
		boolean flag = true;
		if(insbMyOrderService.removeOverTimeData(taskid, inscomcode)){
			flag = false;
		}
		return String.valueOf(flag);
	}

    private String getRegionNameByCode(String regionCode) {
        if (StringUtil.isEmpty(regionCode)) return "";

        try {
            Object regionData = appGeneralSettingService.getRegionByCode(regionCode).getBody();
            if (regionData == null) return "";

            String name = String.valueOf(((Map) regionData).get("comcodename"));
            if (StringUtil.isEmpty(name)) return "";

            return name;
        } catch (Exception e) {
            LogUtil.error("根据地区编码" + regionCode + "获取地区名称出错:" + e.getMessage());
            return "";
        }
    }

	/*
	 * (non-Javadoc)
	 * @see com.zzb.mobile.service.APPBorderService#borderDelivery(java.lang.String)
	 * 订单配送表信息
	 */
	@Override
	public List<Object> borderDelivery(String processInstanceId,String prvid) {
		INSBOrderdelivery queryInsbOrderdelivery = new INSBOrderdelivery();
		queryInsbOrderdelivery.setTaskid(processInstanceId);
		queryInsbOrderdelivery.setProviderid(prvid);
		INSBOrderdelivery insbOrderdelivery=insbOrderdeliveryDao.selectOne(queryInsbOrderdelivery);

		Map<String,Object> borderDeliveryMap=null;
		if(insbOrderdelivery!=null){
		    borderDeliveryMap=new HashMap<String, Object>();
			borderDeliveryMap.put("Id",insbOrderdelivery.getDeliveryaddressid());
			borderDeliveryMap.put("Deptcode",insbOrderdelivery.getDeptcode());
			borderDeliveryMap.put("Recipientname",insbOrderdelivery.getRecipientname());
			borderDeliveryMap.put("Tracenumber",processInstanceId);//业务跟踪号
			borderDeliveryMap.put("Recipientmobilephone",insbOrderdelivery.getRecipientmobilephone());
			borderDeliveryMap.put("Zip",insbOrderdelivery.getZip());
			borderDeliveryMap.put("Delivetype",insbOrderdelivery.getDelivetype());
			//delivename为配送方式
            String delivename=inscCodeService.transferValueToName("DeliveType", "DeliveType", insbOrderdelivery.getDelivetype());
			borderDeliveryMap.put("Delivename",delivename);

            StringBuilder recipientaddress = new StringBuilder();
            if (StringUtil.isNotEmpty(insbOrderdelivery.getRecipientprovince())) {
                recipientaddress.append(getRegionNameByCode(insbOrderdelivery.getRecipientprovince()));
            }
            if (StringUtil.isNotEmpty(insbOrderdelivery.getRecipientcity())) {
                recipientaddress.append(getRegionNameByCode(insbOrderdelivery.getRecipientcity()));
            }
            if (StringUtil.isNotEmpty(insbOrderdelivery.getRecipientarea())) {
                recipientaddress.append(getRegionNameByCode(insbOrderdelivery.getRecipientarea()));
            }
            recipientaddress.append(insbOrderdelivery.getRecipientaddress());
            borderDeliveryMap.put("Recipientaddress", recipientaddress.toString());
		}else{
			borderDeliveryMap=new HashMap<String, Object>();
			borderDeliveryMap.put("Recipientname","");
			borderDeliveryMap.put("Tracenumber",processInstanceId);//业务跟踪号
			borderDeliveryMap.put("Recipientaddress","");
			borderDeliveryMap.put("Recipientmobilephone","");
			borderDeliveryMap.put("Zip","");
			borderDeliveryMap.put("Delivetype","");
			borderDeliveryMap.put("Delivename","");
			borderDeliveryMap.put("outcom","");
		}

		//判断是否北京流程返回配送方式给前端
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(processInstanceId);
		insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		if(null!=insbQuotetotalinfo && "110000".equals(insbQuotetotalinfo.getInsprovincecode())){//报价总表判断是否北京地区
			INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();	//主任务号
			insbWorkflowsub.setMaininstanceid(processInstanceId);
			List <INSBWorkflowsub> insbWorkflowsubList = insbWorkflowsubDao.selectList(insbWorkflowsub);
			if(null!=insbWorkflowsubList && insbWorkflowsubList.size()>0){
				for(INSBWorkflowsub insbWorkflowsub1:insbWorkflowsubList) {
					INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();        //子任务号和inscomcode
					insbQuoteinfo.setWorkflowinstanceid(insbWorkflowsub1.getInstanceid());
					insbQuoteinfo.setInscomcode(prvid);
					insbQuoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);
					if (null != insbQuoteinfo) {
						INSBDistributiontype insbDistributiontype = new INSBDistributiontype();
						insbDistributiontype.setAgreementid(insbQuoteinfo.getAgreementid());    //协议号
						List<INSBDistributiontype> insbDistributiontypeList = insbDistributiontypeDao.selectList(insbDistributiontype);
						if(null!=insbDistributiontypeList && insbDistributiontypeList.size()>0){
							for(INSBDistributiontype insbDistributiontype1:insbDistributiontypeList) {
								if("3".equals(insbDistributiontype1.getDistritype())) {
									borderDeliveryMap.put("Delivetype", insbDistributiontype1.getDistritype());// 1-自取  2-快递 3-电子保单
									break;
								}
							}
						}

					}
				}
			}
		}

        List<Object> borderDeliveryList=new ArrayList<Object>(1);
		borderDeliveryList.add(borderDeliveryMap);

		return borderDeliveryList;
	}
	/*
	 * 数据整理转换为统一的json格式
	 */
	public String borderListToJson(String processInstanceId,String prvid){
		CommonModel model=new CommonModel();
		Map<String,Object> borderDetailMap=new HashMap<String,Object>();

        List<Object> borderDelivery = this.borderDelivery(processInstanceId,prvid);
        if(borderDelivery==null || borderDelivery.isEmpty()){
			borderDetailMap.put("BorderDelivery", "");
		}else{
			borderDetailMap.put("BorderDelivery", borderDelivery);
		}

        borderDetailMap.put("borderList",this.borderList(processInstanceId,prvid));
        borderDetailMap.put("policyitem",this.policyitem(processInstanceId,prvid));
		//判断支付有效期减去24小时之后和当前时间比较
		//比当前时间小的话 返回true，比当前时间大的返回false
		Date payDate = insbPaymentService.getPayTime(processInstanceId,prvid);
		LogUtil.info("aPPborder processInstanceId"+ processInstanceId +" payDate" + payDate.toLocaleString() + " now"+ Calendar.getInstance().getTime().toLocaleString());
		if (payDate != null) {
			payDate.setHours(0);
			payDate.setMinutes(0);
			payDate.setSeconds(0);
			borderDetailMap.put("pay", Calendar.getInstance().getTime().after(payDate));
		}

		//返回车辆属性，个人/单位
		INSBCarinfo insbCarinfo = new INSBCarinfo();
		insbCarinfo.setTaskid(processInstanceId);
		insbCarinfo = insbCarinfoDao.selectOne(insbCarinfo);
		String property = "null";
		if(insbCarinfo != null){
			//所属性质 0:个人用车, 1:企业用车,2:机关团体用车
			property = insbCarinfo.getProperty();
			if(StringUtil.isEmpty(property)){
				property = "null"; //默认值
			}
		}
		borderDetailMap.put("carinfoproperty", property);

		/*INSBInsuredhis insbInsuredhis = new INSBInsuredhis();
		insbInsuredhis.setTaskid(processInstanceId);
		insbInsuredhis.setInscomcode(prvid);
		insbInsuredhis = insbInsuredhisDao.selectOne(insbInsuredhis);
		int idCardType = 0;
		if(insbInsuredhis!=null){
			String personid = "";
			personid =insbInsuredhis.getPersonid();
			INSBPerson insbPerson = insbPersonDao.selectById(personid);
			if(insbPerson!=null){
				idCardType = insbPerson.getIdcardtype();
			}
		}
		borderDetailMap.put("idCardType", idCardType);//身份证类型*/



        //返回被保人手机号 需求合并
        //0603北京流程需求、返回已保存的被保人信息给前端显示
		Map<String,Object> insbPersonMap = new HashMap<String,Object>();
		List<Object> insbPersonList = new ArrayList<Object>();
        String insuredTelephone = "";

        INSBInsuredhis insuredhis = new INSBInsuredhis();
        insuredhis.setTaskid(processInstanceId);
		insuredhis.setInscomcode(prvid);
        insuredhis = insbInsuredhisService.queryOne(insuredhis);

		//1579需求、被保人邮箱和手机号在补充数据项表里面获取
		Map<String, String> supplyParam = new HashMap<>(1);
		supplyParam = insbInsuresupplyparamService.getParamsByTask(processInstanceId, prvid, false);
		if(!StringUtil.isEmpty(supplyParam) && supplyParam.containsKey("insuredMobile_value") && StringUtil.isNotEmpty(supplyParam.get("insuredMobile_value"))){
			insuredTelephone = supplyParam.get("insuredMobile_value");
		}

        if (insuredhis != null && StringUtil.isNotEmpty(insuredhis.getPersonid())) {
            INSBPerson person = insbPersonService.queryById(insuredhis.getPersonid());
			if (person != null) {
				insbPersonMap.put("idcardtype", person.getIdcardtype() != null ? person.getIdcardtype().intValue() : "");//证件类型 之前约定了大小写
				insbPersonMap.put("idcardno", person.getIdcardno());//证件号码
				insbPersonMap.put("name", person.getName());//姓名
				insbPersonMap.put("nation", person.getNation());//民族
				if (!StringUtil.isEmpty(person.getGender())) {
					insbPersonMap.put("gender", "0".equalsIgnoreCase(person.getGender().toString()) ? "男" : "女");//性别 0 男 1 女
				}
				insbPersonMap.put("birthday", StringUtil.isEmpty(person.getBirthday()) ? "" : DateUtil.toString(person.getBirthday()));//出生日期
				insbPersonMap.put("address", person.getAddress());//地址
				insbPersonMap.put("authority", person.getAuthority());//签发机关
				insbPersonMap.put("expdate", person.getExpdate());//证件有效期
				insbPersonMap.put("expstartdate", StringUtil.isEmpty(person.getExpstartdate()) ? "" : DateUtil.toString(person.getExpstartdate()));//证件起始日期
				insbPersonMap.put("expenddate", StringUtil.isEmpty(person.getExpenddate()) ? "" : DateUtil.toString(person.getExpenddate()));
				insbPersonMap.put("insuredtelephone", StringUtil.isNotEmpty(insuredTelephone) ? insuredTelephone : (person.getCellphone()!=null ? person.getCellphone() : ""));//手机号 也是之前约定了大小写
				insbPersonList.add(insbPersonMap);
				borderDetailMap.put("insbPersonInfo", insbPersonList);
			}
        }

		model.setBody(borderDetailMap);
		if(borderDetailMap.isEmpty()){
			model.setStatus("fail");
			model.setMessage("返回数据为空");
			JSONObject jsonObject=JSONObject.fromObject(model);
			return jsonObject.toString();
		}else{
			model.setStatus("success");
			model.setMessage("已成功返回数据");
			JSONObject jsonObject=JSONObject.fromObject(model);

			return jsonObject.toString();
		}
	}
	@Override
	public List<Object> borderList(String processInstanceId, String prvid) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		INSBOrder temp = new INSBOrder();
		temp.setTaskid(processInstanceId);
		temp.setPrvid(prvid);
		INSBOrder insbOrder = insbOrderDao.selectOne(temp);
		paramMap.put("inscomcode", insbOrder.getPrvid());
		paramMap.put("taskid", processInstanceId);
		List<Object> listborder = new ArrayList<Object>();
		Map<String, Object> bordermap = new HashMap<String, Object>();
		bordermap.put("Inscomcode", insbOrder.getPrvid());
		bordermap.put("Createtime", insbOrder.getCreatetime());
		bordermap.put("Currency", insbOrder.getCurrency());
		bordermap.put("Deliverystatus", insbOrder.getDeliverystatus());
		bordermap.put("Deptcode", insbOrder.getDeptcode());
		bordermap.put("Inputusercode", insbOrder.getInputusercode());
		// bordermap.put("Operator", insbOrder.getOperator());修改
		bordermap.put("Operator", insbOrder.getAgentname());
		// 这里取的是 代理人姓名了。之前拿的是后台操作人code，这是根据前端要求。
		// 总表中数据
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(processInstanceId);
		insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		// 报价子表中的数据
		INSBQuoteinfo insbQuoteinfo = new INSBQuoteinfo();
		insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
		insbQuoteinfo.setInscomcode(insbOrder.getPrvid());
		INSBQuoteinfo quoteinfo = insbQuoteinfoDao.selectOne(insbQuoteinfo);
		if (insbOrder == null) {
			bordermap.put("Totalpaymentamount", "");
		} else {
			double money = null == insbOrder.getTotalpaymentamount() ? 0.00 : insbOrder.getTotalpaymentamount();
			bordermap.put("Totalpaymentamount", formartDouble(money));
		}
		bordermap.put("Orderstatus", insbOrder.getOrderstatus());
		// 调用支付方式
//		INSBPaychannel paychannel = this.getPayMethod(processInstanceId, insbOrder.getId());
		if(StringUtils.isNotBlank(insbOrder.getPaymentmethod())){
			INSBPaychannel paychannel = insbPaychannelDao.selectById(insbOrder.getPaymentmethod());
			if (paychannel != null) {
				bordermap.put("Paymentmethod", paychannel.getPaychannelname());
				bordermap.put("Paytype", paychannel.getPaytype());
			} else{
				INSBWorkflowsub flowsub = this.insbWorkflowsubDao.selectByInstanceId(quoteinfo.getWorkflowinstanceid());
				if (!"admin".equals(flowsub.getOperator())) {
					bordermap.put("Paytype", "03");
					bordermap.put("Paymentmethod", "柜台Pos支付");
				} else {
					bordermap.put("Paymentmethod", "");
					bordermap.put("Paytype", "");
				}
			}
		}else{
			bordermap.put("Paymentmethod", "");
			bordermap.put("Paytype", "");
		}
		// 订单表中的状态
		String orderstatusName = inscCodeService.transferValueToName("orderstatus", "orderstatus", insbOrder.getOrderstatus());
		bordermap.put("orderstatusName", orderstatusName);
		bordermap.put("Orderno", insbOrder.getOrderno());// 获得orderNo
		// 这里是投保书按钮的入参
		bordermap.put("Brisktype", "0");
		bordermap.put("Crisktype", "1");
		// INSBOrderpayment insbOrderpayment=new INSBOrderpayment();
		// insbOrderpayment.setTaskid(processInstanceId);
		// insbOrderpayment.setOrderid(insbOrder.getId());
		// insbOrderpayment=insbOrderpaymentDao.selectOne(insbOrderpayment);
		// if(insbOrderpayment!=null){
		// bordermap.put("Paymentransaction",insbOrderpayment.getPaymentransaction());
		// bordermap.put("Paymentransstyle",insbOrderpayment.getPaychannelid());
		// }else{
		bordermap.put("Paymentransaction", insbOrder.getOrderno());
		bordermap.put("Paymentransstyle", insbOrder.getPaymentmethod());
		// }
		/*
		 * 此处为根据订单配送表公司Code查询机构名称
		 */
		INSCDept dept = new INSCDept();
		dept.setComcode(insbQuoteinfoDao.selectOne(insbQuoteinfo).getDeptcode());
		dept = deptDao.selectOne(dept);
		if (dept != null) {
			bordermap.put("outcom", dept.getComname());
		} else {
			bordermap.put("outcom", "");
		}
		// 商业险和交强险价格,
		List<INSBCarkindprice> listCarkindprice = carkindpriceDao.selectCarkindpriceList(paramMap);
		double tamount = 0;
		double camount = 0;
		for (int i = 0; i < listCarkindprice.size(); i++) {
			if (listCarkindprice.get(i).getDiscountCharge() != null) {
				if (listCarkindprice.get(i).getInskindtype().equals("2") && listCarkindprice.get(i).getDiscountCharge() >= 0) {
					camount += listCarkindprice.get(i).getDiscountCharge();
				} else if (listCarkindprice.get(i).getInskindtype().equals("3") && listCarkindprice.get(i).getDiscountCharge() >= 0) {
					camount += listCarkindprice.get(i).getDiscountCharge();
				}
				if (listCarkindprice.get(i).getInskindtype().equals("0") && listCarkindprice.get(i).getDiscountCharge() >= 0) {
					tamount += listCarkindprice.get(i).getDiscountCharge();
				}
				if (listCarkindprice.get(i).getInskindtype().equals("1") && listCarkindprice.get(i).getDiscountCharge() >= 0) {
					tamount += listCarkindprice.get(i).getDiscountCharge();
				}
			}
			BigDecimal tamountdecimal = new BigDecimal(tamount);
			BigDecimal camountdecimal = new BigDecimal(camount);
			bordermap.put("Tamount", camountdecimal.setScale(2, BigDecimal.ROUND_HALF_UP));
			bordermap.put("Camount", tamountdecimal.setScale(2, BigDecimal.ROUND_HALF_UP));

		}
		INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
		insbPolicyitem.setTaskid(processInstanceId);
		insbPolicyitem.setInscomcode(insbOrder.getPrvid());
		List<INSBPolicyitem> Policyitem = insbPolicyitemDao.selectList(insbPolicyitem);

		for (int i = 0; i < Policyitem.size(); i++) {
			if (Policyitem.get(i).getRisktype().equals("0")) {
				if (Policyitem.get(i).getStartdate() == null || Policyitem.get(i).getEnddate() == null) {
					bordermap.put("TBCreatetime", "");
				} else {
					bordermap.put("TBCreatetime",
							ModelUtil.conbertToString(Policyitem.get(i).getStartdate()) + "零时至" + ModelUtil.conbertToString(Policyitem.get(i).getEnddate()));
				}
				bordermap.put("TBProposalformno", Policyitem.get(i).getProposalformno());
			} else {
				if (Policyitem.get(i).getStartdate() == null || Policyitem.get(i).getEnddate() == null) {
					bordermap.put("TCCreatetime", "");
				} else {
					bordermap.put("TCCreatetime",
							ModelUtil.conbertToString(Policyitem.get(i).getStartdate()) + "零时至" + ModelUtil.conbertToString(Policyitem.get(i).getEnddate()));
				}
				bordermap.put("TCProposalformno", Policyitem.get(i).getProposalformno());
			}

		}
		listborder.add(bordermap);

		return listborder;
	}

	public double formartDouble(double d) {
        BigDecimal bg = new BigDecimal(d);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

	@Override
	public List<Object> borderListByOrderId(String orderId) {
		Map<String,Object> paramMap=new HashMap<String, Object>();
		INSBOrder insbOrder=insbOrderDao.selectById(orderId);
		paramMap.put("inscomcode",insbOrder.getPrvid());
		paramMap.put("taskid",insbOrder.getTaskid());
		List<Object> listborder=new ArrayList<Object>();
		Map<String, Object> bordermap=new HashMap<String, Object>();
			bordermap.put("Inscomcode",insbOrder.getPrvid());
			bordermap.put("Createtime", insbOrder.getCreatetime());
			bordermap.put("Currency", insbOrder.getCurrency());
			bordermap.put("Deliverystatus", insbOrder.getDeliverystatus());
			bordermap.put("Deptcode", insbOrder.getDeptcode());
			bordermap.put("Inputusercode", insbOrder.getInputusercode());
			bordermap.put("Operator", insbOrder.getOperator());
			//报价子表金额
			//总表中数据
			INSBQuotetotalinfo insbQuotetotalinfo=new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(insbOrder.getTaskid());
			insbQuotetotalinfo=insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			//报价子表中的数据
			INSBQuoteinfo insbQuoteinfo=new INSBQuoteinfo();
			insbQuoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
			insbQuoteinfo.setInscomcode(insbOrder.getPrvid());
			//INSBQuoteinfo insbQuoteinfo1=insbQuoteinfoDao.selectOne(insbQuoteinfo);
			if(insbQuoteinfoDao.selectOne(insbQuoteinfo)==null){
				bordermap.put("Totalpaymentamount","");
			}else{
				bordermap.put("Totalpaymentamount",insbQuoteinfoDao.selectOne(insbQuoteinfo).getQuotediscountamount());
			}
			//bordermap.put("Totalpaymentamount",insbOrder.getTotalpaymentamount());
			bordermap.put("Orderstatus",insbOrder.getOrderstatus());
			//调用支付方式
			INSBPaychannel paychannel=this.getPayMethod(insbOrder.getTaskid(),insbOrder.getId());
			if(paychannel!=null){
			bordermap.put("Paymentmethod",paychannel.getPaychannelname());
			bordermap.put("Paytype",paychannel.getPaytype());
			}else if(paychannel==null){
				bordermap.put("Paymentmethod","");
				bordermap.put("Paytype","");
			}
			String orderstatusName=inscCodeService.transferValueToName("orderstatus", "orderstatus",insbOrder.getOrderstatus());
			bordermap.put("Orderstatus",orderstatusName);
			bordermap.put("Orderno",insbOrder.getOrderno());//获得orderNo
			bordermap.put("Brisktype", "0");
			bordermap.put("Crisktype", "1");
			INSBOrderpayment insbOrderpayment=new INSBOrderpayment();
			insbOrderpayment.setTaskid(insbOrder.getTaskid());
			insbOrderpayment.setOrderid(insbOrder.getId());
			insbOrderpayment=insbOrderpaymentDao.selectOne(insbOrderpayment);
			if(insbOrderpayment!=null){
				bordermap.put("Paymentransaction",insbOrderpayment.getPaymentransaction());
				bordermap.put("Paymentransstyle",insbOrderpayment.getPaychannelid());
			}else{
				bordermap.put("Paymentransaction","");
				bordermap.put("Paymentransstyle","");
			}
		//商业险和交强险价格
		List<INSBCarkindprice> listCarkindprice=carkindpriceDao.selectCarkindpriceList(paramMap);
		double tamount=0;
		double camount=0;
			for(int i=0;i<listCarkindprice.size();i++){
				if(listCarkindprice.get(i).getInskindtype().equals("2")&&listCarkindprice.get(i).getDiscountCharge()!=null){
					camount +=listCarkindprice.get(i).getDiscountCharge();
				}else if(listCarkindprice.get(i).getInskindtype().equals("3")&&listCarkindprice.get(i).getDiscountCharge()!=null){
					camount +=listCarkindprice.get(i).getDiscountCharge();
				}
				if(listCarkindprice.get(i).getInskindtype().equals("0")&&listCarkindprice.get(i).getDiscountCharge()!=null){
					tamount +=listCarkindprice.get(i).getDiscountCharge();
				}else if(listCarkindprice.get(i).getInskindtype().equals("1")&&listCarkindprice.get(i).getDiscountCharge()!=null){
					tamount +=listCarkindprice.get(i).getDiscountCharge();
				}
				BigDecimal tamountdecimal=new BigDecimal(tamount);
				BigDecimal camountdecimal=new BigDecimal(camount);
				bordermap.put("Tamount",tamountdecimal.setScale(2,BigDecimal.ROUND_HALF_UP));
				bordermap.put("Camount",camountdecimal.setScale(2,BigDecimal.ROUND_HALF_UP));

			}
			INSBPolicyitem insbPolicyitem=new INSBPolicyitem();
			insbPolicyitem.setTaskid(insbOrder.getTaskid());
			insbPolicyitem.setInscomcode(insbOrder.getPrvid());
			List<INSBPolicyitem>Policyitem=insbPolicyitemDao.selectList(insbPolicyitem);

			for(int i=0;i<Policyitem.size();i++){
				if(Policyitem.get(i).getRisktype().equals("0")){
					bordermap.put("TBCreatetime", ModelUtil.conbertToString(Policyitem.get(i).getStartdate())+"零时至"
							 +ModelUtil.conbertToString(Policyitem.get(i).getEnddate()));
				}else{
					bordermap.put("TCCreatetime", ModelUtil.conbertToString(Policyitem.get(i).getStartdate())+"零时至"
							 +ModelUtil.conbertToString(Policyitem.get(i).getEnddate()));
				}

			}
		listborder.add(bordermap);

		return listborder;
	}
}
