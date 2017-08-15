package com.zzb.cm.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.DispatchService;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.RedisException;
import com.common.TaskConst;
import com.common.VehicleTypeMapper;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.controller.vo.CarinfoVO;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.dao.INSBApplicanthisDao;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBCarowneinfoDao;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBInvoiceinfoDao;
import com.zzb.cm.dao.INSBLegalrightclaimDao;
import com.zzb.cm.dao.INSBLegalrightclaimhisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBRelationpersonDao;
import com.zzb.cm.dao.INSBRelationpersonhisDao;
import com.zzb.cm.dao.INSBRulequerycarinfoDao;
import com.zzb.cm.dao.INSBRulequeryclaimsDao;
import com.zzb.cm.dao.INSBRulequeryotherinfoDao;
import com.zzb.cm.dao.INSBRulequeryrepeatinsuredDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBInvoiceinfo;
import com.zzb.cm.entity.INSBLastyearinsureinfo;
import com.zzb.cm.entity.INSBLegalrightclaim;
import com.zzb.cm.entity.INSBLegalrightclaimhis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBRelationperson;
import com.zzb.cm.entity.INSBRelationpersonhis;
import com.zzb.cm.entity.INSBRulequerycarinfo;
import com.zzb.cm.entity.INSBRulequeryclaims;
import com.zzb.cm.entity.INSBRulequeryotherinfo;
import com.zzb.cm.entity.INSBRulequeryrepeatinsured;
import com.zzb.cm.entity.entityutil.EntityTransformUtil;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBCommonQuoteinfoService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBGroupmembersDao;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowmaintrackdetailDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackdetailDao;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrackdetail;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrackdetail;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBCarinfoServiceImpl extends BaseServiceImpl<INSBCarinfo> implements
		INSBCarinfoService {
	@Resource
	private INSBCarinfoDao insbCarinfoDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBGroupmembersDao insbGroupmembersDao;
	@Resource
	private INSBCarmodelinfoDao insbCarmodelinfoDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBInsuredDao insbInsuredDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBInsuredhisDao insbInsuredhisDao;
	@Resource
	private INSBCarinfohisDao insbCarinfohisDao;
	@Resource
	private INSBCarmodelinfohisDao insbCarmodelinfohisDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;
	@Resource
	private INSCCodeService inscCodeService;
	@Resource
	private INSCUserDao inscUserDao;
	@Resource
	private INSBCarowneinfoDao insbCarowneinfoDao;
	@Resource
	private DispatchService dispatchService;
	@Resource
	private INSBWorkflowmainDao insbWorkflowmainDao;
	@Resource
	private INSBWorkflowmaintrackdetailDao insbWorkflowmaintrackdetailDao;
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
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBWorkflowsubtrackdetailDao insbWorkflowsubtrackdetailDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBRulequerycarinfoDao insbRulequerycarinfoDao;
	@Resource
	private INSBInvoiceinfoDao insbInvoiceinfoDao;
	@Resource
    private INSBCommonQuoteinfoService commonQuoteinfoService;
	@Resource
	private IRedisClient redisClient;

    @Resource
    private InterFaceService interFaceService;

	@Override
	protected BaseDao<INSBCarinfo> getBaseDao() {
		return insbCarinfoDao;
	}
//	@Override
	public String showMytaskInfoAll(Map<String, Object> map, int page) {
//		List<INSBCarinfo> infoList = insbCarinfoDao.selectCarinfoList(map);
//		List<Map<Object, Object>> resultList = new ArrayList<Map<Object,Object>>();
//		Map<Object, Object> initMap = new HashMap<Object, Object>();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		
//		/*分页数据计算*/
//		//获取总数据条数
//		int total=infoList.size();
//		//页面尺寸（每页数据条数，可以修改方法参数从请求中获取）
//		int pagesize=3;
//		//计算总共的页面数
//		int pages=(int) Math.ceil(total*1.0/pagesize);
//		//数据起止范围start~end（其中page为当前请求的页码）
//		int start =(page-1)*pagesize;
//		int end = page*pagesize;
//		//不满一页时end的处理，直接设置为数据的末条值
//		if(end>total){
//			end=total;
//		}
//		initMap.put("pagesize", pagesize);
//		initMap.put("pages", pages);
//		initMap.put("total",total );
//		for (int i = start; i < end; i++) {
//			INSBCarinfo tempInfo = infoList.get(i);
//			String taskid=tempInfo.getTaskid();
//			
//			INSBPolicyitem policyitem = insbPolicyitemDao.selectPolicyitemByTaskId(taskid);
//			INSBOrder order=insbOrderDao.selectOrderByTaskId(taskid);
//			
//			Map<Object, Object> tempMap = new HashMap<Object, Object>();
//			tempMap.put("id", tempInfo.getId());
//			tempMap.put("operator", tempInfo.getOperator());
//			if(tempInfo.getCreatetime()!=null){
//				tempMap.put("createtime", sdf.format(tempInfo.getCreatetime()));
//			}
//			if(tempInfo.getModifytime()!=null){
//				tempMap.put("modifytime", sdf.format(tempInfo.getModifytime()));
//			}
//			tempMap.put("noti", tempInfo.getNoti());
//			tempMap.put("selectins", tempInfo.getSelectins());
//			tempMap.put("owner", tempInfo.getOwner());
//			tempMap.put("ownername", tempInfo.getOwnername());
//			tempMap.put("carlicenseno", tempInfo.getCarlicenseno());
//			tempMap.put("standardfullname", tempInfo.getStandardfullname());
//			tempMap.put("selectins", tempInfo.getSelectins());
//			tempMap.put("operating1", "link(\'" + tempInfo.getTaskid() + "\');");
//			tempMap.put("operating2", "<a href=\"javascript:deletecarinfo(\'"
//					+ tempInfo.getId() + "\');\" >删除</a>");
//			
//			
//			if(policyitem!=null){
//				//被保人
//				tempMap.put("insuredname", policyitem.getInsuredname());
//				//所属团队
//				tempMap.put("team", policyitem.getTeam());
//				
//			}
//			if(order!=null){
//				//代理人
//				tempMap.put("agentname", order.getAgentname());
//				//支付方式
//				tempMap.put("paymentmetod", order.getPaymentmethod());
//				//订单号
//				tempMap.put("orderno", order.getOrderno());
//				//供应商
//				tempMap.put("prvid", order.getPrvid());
//				//网点
//				tempMap.put("deptcode", order.getDeptcode());
//			}
//			
//			resultList.add(tempMap);
//		}
//		initMap.put("rows", resultList);
//	    JSONObject jsonObject = JSONObject.fromObject(initMap);	
//	    return jsonObject.toString();
		return null;
	}
	/**
	 * 通过map参数查询车辆任务集合
	 */
	@Override
	public List<Map<Object, Object>> getCarTaskListByMap(Map<String, Object> paramMap) {
		//取出模糊查询数据
		List<Map<Object, Object>>listMap =new ArrayList<Map<Object,Object>>();
//		String insuredname = "";
//		String carlicenseno = "";
//		String carlicensenoOrinsuredname = "";
//		if(paramMap.get("insuredname")!=null){
//			insuredname = (String)paramMap.get("insuredname");
//		}
//		if(paramMap.get("carlicenseno")!=null){
//			carlicenseno = (String)paramMap.get("carlicenseno");
//		}
//		if(paramMap.get("carlicensenoOrinsuredname")!=null){
//			carlicensenoOrinsuredname = (String)paramMap.get("carlicensenoOrinsuredname");
//		}
//		//存放拆分后的数据List
//		 List<Map<Object, Object>> templist = new ArrayList<Map<Object,Object>>();
//		 //取得任务原始数据List
//		 List<Map<Object, Object>> carinfoList = insbCarinfoDao.getCarTaskListByMapOld(paramMap);
//		 //根据保险公司个数拆分规则报价任务为子任务
//		 for(int i=0; i<carinfoList.size(); i++){
//			 if(carinfoList.get(i)!=null){
//				 String taskid = (String)carinfoList.get(i).get("taskid");
//				//添加车险任务记录的操作
//				 carinfoList.get(i).put("dispatchstatus", "");
//				 carinfoList.get(i).put("tasktype", "");
//				 List<String> inscomcodeList = new ArrayList<String>();//保险公司code集合
//				 //通过是否生成订单判断是否在人工报价节点
//				 INSBOrder order = new INSBOrder();
//				 order.setTaskid(taskid);
//				 List<INSBOrder> orderList = insbOrderDao.selectList(order);
//				 if(orderList!=null && orderList.size()>0){
//					//已生成订单说明已经指定保险限公司
//					 Map<String,String> sample = new HashMap<String, String>();
//					 if(carinfoList.get(i).get("insuredname")!=null){
//						 sample.put("insuredname", (String)carinfoList.get(i).get("insuredname"));
//					 }
//					 if(carinfoList.get(i).get("carlicenseno")!=null){
//						 sample.put("carlicenseno", (String)carinfoList.get(i).get("carlicenseno"));
//					 }
//					//匹配是否符合模糊查询条件
//					 if(this.matchInsurednameAndCarlicenseno(insuredname, carlicenseno, carlicensenoOrinsuredname, sample)){
//						 if(orderList.get(0)!=null){
//							 if(orderList.get(0).getPrvid()!=null){
//								 carinfoList.get(i).put("inscomcode", orderList.get(0).getPrvid());
//								 carinfoList.get(i).remove("quotetotalinfoid");
//								 carinfoList.get(i).put("operating", "<a href=\"javascript:window.parent.openDialogForCM(\'business/cartaskmanage/showcartaskdetail?taskid="
//										 +taskid+"&inscomcode="+orderList.get(0).getPrvid()+"\');\">详细信息</a>");
//								 templist.add(carinfoList.get(i));
//							 }
//						 }
//					 }
//				 }else{
//					//通过查询到的quotetotalinfoid查询任务下的保险公司名称
//					 String quotetotalinfoid = (String)carinfoList.get(i).get("quotetotalinfoid");
//					 if(quotetotalinfoid!=null && !("".equals(quotetotalinfoid))){
//						 INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
//						 quoteinfo.setQuotetotalinfoid(quotetotalinfoid);
//						 List<INSBQuoteinfo> quoteinfoList = insbQuoteinfoDao.selectList(quoteinfo);
//						 for (int j = 0; j < quoteinfoList.size(); j++) {
//							 INSBQuoteinfo quInfo = quoteinfoList.get(j);
//							 if(quInfo!=null){
//								 if(quInfo.getInscomcode()!=null){
//									 inscomcodeList.add(quInfo.getInscomcode());
//								 }
//							 }
//						 }
//					 }
//					 //根据保险公司List拆分成单条车险任务记录
//					 for (int j = 0; j < inscomcodeList.size(); j++) {
//						 HashMap<Object, Object> temp = (HashMap)((HashMap)carinfoList.get(i)).clone();
//						 temp.put("inscomcode", inscomcodeList.get(j));
//						 INSBInsuredhis insuredhis = new INSBInsuredhis();
//						 insuredhis.setTaskid(taskid);
//						 insuredhis.setInscomcode(inscomcodeList.get(j));
//						 List<INSBInsuredhis> insuredhisList = insbInsuredhisDao.selectList(insuredhis);
//						 if(insuredhisList!=null && insuredhisList.size()>0){
//							 INSBPerson insured = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
//							 if(insured!=null){
//								 if(insured.getName()!=null){
//									 temp.replace("insuredname", insured.getName());
//								 }
//							 }
//						 }
//						 INSBCarinfohis carinfohis = new INSBCarinfohis();
//						 carinfohis.setTaskid(taskid);
//						 carinfohis.setInscomcode(inscomcodeList.get(j));
//						 List<INSBCarinfohis> carinfohisList = insbCarinfohisDao.selectList(carinfohis);
//						 if(carinfohisList!=null && carinfohisList.size()>0){
//							 if(carinfohisList.get(0).getCarlicenseno()!=null){
//								 temp.replace("carlicenseno", carinfohisList.get(0).getCarlicenseno());
//							 }
//						 }
//						 temp.remove("quotetotalinfoid");
//						 temp.put("operating", "<a href=\"javascript:window.parent.openDialogForCM(\'business/cartaskmanage/showcartaskdetail?taskid="
//								 +taskid+"&inscomcode="+inscomcodeList.get(j)+"\');\">详细信息</a>");
//						 Map<String,String> sample = new HashMap<String, String>();
//						 if(temp.get("insuredname")!=null){
//							 sample.put("insuredname", (String)carinfoList.get(i).get("insuredname"));
//						 }
//						 if(temp.get("carlicenseno")!=null){
//							 sample.put("carlicenseno", (String)carinfoList.get(i).get("carlicenseno"));
//						 }
//						 //匹配是否符合模糊查询条件
//						 if(this.matchInsurednameAndCarlicenseno(insuredname, carlicenseno, carlicensenoOrinsuredname, sample)){
//							 templist.add(temp);
//						 }
//					 }
//				 }
//			 }
//		 }
//		return templist;
		return listMap;
	}
	/**
	 * 车牌号、被保人模糊查询匹配方法
	 */
	public boolean matchInsurednameAndCarlicenseno(String insuredname, String carlicenseno, 
			String carlicensenoOrinsuredname, Map<String,String> sample){
		boolean nameFlag = false;
		boolean carFlag = false;
		boolean carAndNameFlag = false;
		//模糊查询匹配
		 if("".equals(insuredname)){
			 nameFlag = true;
		 }else{
			 if(sample.get("insuredname")!=null){
				 if(sample.get("insuredname").contains(insuredname)){
					 nameFlag = true;
				 }
			 }
		 }
		 if("".equals(carlicenseno)){
			 carFlag = true;
		 }else{
			 if(sample.get("carlicenseno")!=null){
				 if(sample.get("carlicenseno").contains(carlicenseno)){
					 carFlag = true;
				 }
			 }
		 }
		 if("".equals(carlicensenoOrinsuredname)){
			 carAndNameFlag = true;
		 }else{
			 if(sample.get("insuredname")!=null){
				 if(sample.get("insuredname").contains(carlicensenoOrinsuredname)){
					 carAndNameFlag = true;
				 }
			 }
			 if(sample.get("carlicenseno")!=null){
				 if(sample.get("carlicenseno").contains(carlicensenoOrinsuredname)){
					 carAndNameFlag = true;
				 }
			 }
		 }
		 if(carAndNameFlag && (nameFlag && carFlag)){
			 return true;
		 }else{
			 return false;
		 }
	}
	/**
	 * 通过map参数查询车辆任务集合并返回对应JSON对象
	 */
//	@Override
//	public String getJSONOfCarTaskListByMap(Map<String, Object> paramMap) {
//		List<Map<Object, Object>> carTaskList = this.getCarTaskListByMap(paramMap);
//		//根据返回结果做分页查询
//		List<Map<Object, Object>> subCarTaskList = new ArrayList<Map<Object,Object>>();
//		int limit = (Integer)paramMap.get("limit");
//		int offset = ((Long)paramMap.get("offset")).intValue();
//		int max = carTaskList.size()>=(limit+offset)?(limit+offset):carTaskList.size();
//		for (int i = offset; i < max; i++) {
//			subCarTaskList.add(carTaskList.get(i));
//		}
//		//组织返回数据
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("records", "10000");
//		map.put("page", 1);
//		map.put("total", carTaskList.size());
//		map.put("rows", subCarTaskList);
//		JSONObject jsonObject = JSONObject.fromObject(map);
//		return jsonObject.toString();
//	}
	
	/**
	 * 通过map参数查询车辆任务集合并返回对应JSON对象新版本
	 */
	@Override
	public String getJSONOfCarTaskListByMap(Map<String, Object> paramMap) {
		//车险任务管理,已完成和待处理      杨威
		List<Map<String, Object>> carTaskList = null;
		Long num = null;
		long offset = Long.valueOf(String.valueOf(paramMap.get("offset")));
		paramMap.put("offset", 0);


		if ("02".equals(paramMap.get("taskstate"))) {
			num = insbCarinfoDao.getCompletedCarTaskListCountByMap(paramMap);
			paramMap.put("limit", num);
			carTaskList = insbCarinfoDao.getCompletedCarTaskListByMap(paramMap);
		} else {
			//优化查询
			if(null != paramMap.get("singleFlag")){
				num = insbCarinfoDao.getSingleCarTaskCountByMap(paramMap);
				paramMap.put("limit", num);
				carTaskList = insbCarinfoDao.getSingleCarTaskListByMap(paramMap);// 通过各种关联查询返回数据
			} else {
				num = insbCarinfoDao.getCarTaskCountByMap(paramMap);
				paramMap.put("limit", num);
				carTaskList = insbCarinfoDao.getCarTaskListByMap(paramMap);// 通过各种关联查询返回数据
			}

		}

		List<Map<String, Object>> tempcarTaskList = null;
		// 根据业管配置的业管权限
		// tempcarTaskList =
		// remakeData(carTaskList,tempcarTaskList,String.valueOf(paramMap.get("usercode")),num);
		if (null != carTaskList && carTaskList.size() > 0) {// list里面有数据才进入数据权限过滤
			List<Map<String, String>> privileges = insbGroupmembersDao
					.selectGroupIdinfosByUserCodes4Tasklist(String.valueOf(paramMap.get("usercode")));
			StringBuffer privilegeStr = new StringBuffer();
			for (Map<String, String> privilege : privileges) {// 解析所有业管组及权限信息
				if (null != privilege.get("tasktype") && privilege.get("tasktype").contains(",")) {
					String[] tasks = privilege.get("tasktype").split(",");
					for (String task : tasks) {
						if (StringUtil.isNotEmpty(task))
							privilegeStr.append(task + "@" + privilege.get("deptid") + "@" + privilege.get("provideid"));
					}
				} else {
					privilegeStr.append(privilege.get("tasktype") + "@" + privilege.get("deptid") + "@" + privilege.get("provideid"));
				}
			}
			if (privilegeStr.toString().length() > 0) {// 有业管权限人员根据其具体权限控制可查看任务
			// paramMap.put("offset", 0);
			// paramMap.put("limit", num);
			// if("02".equals(paramMap.get("taskstate"))){
			// carTaskList=insbCarinfoDao.getCompletedCarTaskListByMap(paramMap);
			// }else{
			// carTaskList=
			// insbCarinfoDao.getCarTaskListByMap(paramMap);//通过各种关联查询返回数据
			// }
				LogUtil.debug(paramMap.get("usercode") + "用户业管权限privilege=" + privilegeStr.toString());
				tempcarTaskList = new ArrayList<Map<String, Object>>();
				try {
					for (Map<String, Object> cartask : carTaskList) {
						String cartaskinfo = "";
						if (TaskConst.QUOTING_2.equals(String.valueOf(cartask.get("maintaskcode")))
								&& !StringUtil.isEmpty(cartask.get("subcodevalue"))) {
							cartaskinfo = String.valueOf(cartask.get("subcodevalue")) + "@" + String.valueOf(cartask.get("deptcode")) + "@"
									+ (String.valueOf(cartask.get("inscomcode")).substring(0, 4));
						} else if (!StringUtil.isEmpty(cartask.get("maincodevalue"))) {
							// 如果任务类型为空则不在权限判定范围
							cartaskinfo = String.valueOf(cartask.get("maincodevalue")) + "@" + String.valueOf(cartask.get("deptcode")) + "@"
									+ (String.valueOf(cartask.get("inscomcode")).substring(0, 4));
						} else {
							cartaskinfo = "@" + String.valueOf(cartask.get("deptcode")) + "@"
									+ (String.valueOf(cartask.get("inscomcode")).substring(0, 4));
						}
						if (StringUtil.isNotEmpty(cartaskinfo) && !privilegeStr.toString().contains(cartaskinfo)) {// 不在权限组内，删除不显示
							LogUtil.debug(cartask.get("maininstanceId") + "=taskid=" + paramMap.get("usercode") + "用户业管权限无privilege=" + cartaskinfo);
							// carTaskList.remove(cartask);
							num--;
						} else {
							tempcarTaskList.add(cartask);
						}
					}
				} catch (Exception e) {
					LogUtil.info(paramMap.get("usercode") + "用户业管权限判断异常cartasklist=" + carTaskList);
					e.printStackTrace();
				}
			}
		}
		paramMap.put("limit", 10);
		//组织返回数据
//		List<Map<String, Object>> carTaskList = insbCarinfoDao.getCarTaskListByMap(paramMap);//通过各种关联查询返回数据
		Map<String, Object> map = new HashMap<String, Object>();
		if(null!=tempcarTaskList){//有业管权限人员根据其具体权限控制可查看任务
			for (int i = 0; i < tempcarTaskList.size(); i++) {
				Map<String, Object> temp = tempcarTaskList.get(i);
				String maininstanceid = (String) temp.get("maininstanceId");
				String subInstanceId = (String) temp.get("subInstanceId");
				String taskcode = (String) temp.get("taskcode");
				String inscomcode = (String) temp.get("inscomcode");
				String inscomName = (String) temp.get("inscomName");
				//添加查勘任务详细信息链接
				temp.put("operating", "<a href=\"javascript:window.parent.openLargeDialog(\'business/cartaskmanage/showcartaskdetail?maininstanceid="
						 +maininstanceid+"&inscomcode="+inscomcode+"&subinstanceid="+subInstanceId+"&taskcode="+taskcode+"\');\">详细信息</a>");
				temp.put("flowerror", "<a href=\"javascript:window.parent.openLargeDialog(\'business/cartaskmanage/showflowerror?maininstanceid="
						 +maininstanceid+"&inscomcode="+inscomcode+"&inscomName="+inscomName+"\');\">查看</a>");
				//setOperator(temp, maininstanceid);
			}
			map.put("records", "10000");
			map.put("page", 1);
			map.put("total",num);
			map.put("rows", tempcarTaskList.subList((int)offset, (int)(offset+10>num?num:offset+10)));
		}else{
			for (int i = 0; i < carTaskList.size(); i++) {
				Map<String, Object> temp = carTaskList.get(i);
				String maininstanceid = (String) temp.get("maininstanceId");
				String subInstanceId = (String) temp.get("subInstanceId");
				String taskcode = (String) temp.get("taskcode");
				String inscomcode = (String) temp.get("inscomcode");
				String inscomName = (String) temp.get("inscomName");
				//添加查勘任务详细信息链接
				temp.put("operating", "<a href=\"javascript:window.parent.openLargeDialog(\'business/cartaskmanage/showcartaskdetail?maininstanceid="
						 +maininstanceid+"&inscomcode="+inscomcode+"&subinstanceid="+subInstanceId+"&taskcode="+taskcode+"\');\">详细信息</a>");
				temp.put("flowerror", "<a href=\"javascript:window.parent.openLargeDialog(\'business/cartaskmanage/showflowerror?maininstanceid="
						 +maininstanceid+"&inscomcode="+inscomcode+"&inscomName="+inscomName+"\');\">查看</a>");
				//setOperator(temp, maininstanceid);
			}
			map.put("records", "10000");
			map.put("page", 1);
			map.put("total",num);
			map.put("rows", carTaskList.subList((int)offset, (int)(offset+10>num?num:offset+10)));
		}
		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject.toString();
	}

	private void setOperator(Map<String, Object> temp, String maininstanceid) {
		//任务在主流程时，实时任务表关联不到主流程，需重新查询主流程操作人信息
		if (temp.get("subtaskcode") == null) {
			INSBWorkflowmain workmain = insbWorkflowmainDao.selectByInstanceId(maininstanceid);
			temp.put("operatorcode", workmain.getOperator());

			if (StringUtil.isNotEmpty(workmain.getOperator())) {
				INSCUser opuser = inscUserDao.selectByUserCode(workmain.getOperator());
				if (opuser != null && StringUtil.isNotEmpty(opuser.getName())) {
					temp.put("operator", opuser.getName()+"("+ workmain.getOperator() +")");// CONCAT(inu. NAME, '(', ws.operator, ')') END AS operator,
				}
			}
		}
	}
	
	/**
	 * 通过保险公司code获取保险公司name以及上级公司的name
	 */
	public Map<String,String> getInscompany(String inscomcode){
		Map<String,String> temp=new HashMap<String,String>();
		INSBProvider provider = new INSBProvider();
		provider.setPrvcode(inscomcode);
		provider = insbProviderDao.selectOne(provider);
		if(provider!=null){
			temp.put("inscomcode", inscomcode);
			temp.put("inscomname", provider.getPrvshotname());//保险公司名称
			temp.put("province", provider.getProvince());
		}
		return temp;
	}
	/**
	 * 通过任务id和调用用途选择性查询车辆任务中车辆信息
	 */
	@Override
	public Map<String, Object> getCarTaskCarInfo(String taskId, String inscomcode, String opeType) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("taskid", taskId);
		temp.put("inscomcode", inscomcode);
		INSBProvider provider = new INSBProvider();
		provider.setPrvcode(inscomcode);
		provider = insbProviderDao.selectOne(provider);
		if(provider!=null){
			temp.put("inscomname", provider.getPrvshotname());//保险公司名称
			temp.put("parentinscomname", getTopProviderName(provider.getPrvcode()));//保险公司集团名称
		}
		temp.put("mileageList", inscCodeService.queryINSCCodeByCode("avgmileage","avgmileage"));//车辆行驶里程列表
		temp.put("drivingareaList", inscCodeService.queryINSCCodeByCode("CarDrivingArea","CarDrivingArea"));//车辆行驶区域列表
		temp.put("carpropertyList", inscCodeService.queryINSCCodeByCode("UseProps","UseProps"));//车辆使用性质列表
		temp.put("propertyList", inscCodeService.queryINSCCodeByCode("UserType","UserType"));//车辆所属性质列表
		//取得车辆投保地区信息
		INSBQuotetotalinfo quotetotalinfoTemp = new INSBQuotetotalinfo();
		quotetotalinfoTemp.setTaskid(taskId);
		quotetotalinfoTemp = insbQuotetotalinfoDao.selectOne(quotetotalinfoTemp);
		if(quotetotalinfoTemp !=null){
			if(quotetotalinfoTemp.getInsprovincename()!=null){
				temp.put("insprovincename", quotetotalinfoTemp.getInsprovincename());
			}
			if(quotetotalinfoTemp.getInscityname()!=null){
				temp.put("inscityname", quotetotalinfoTemp.getInscityname());
			}
			if(quotetotalinfoTemp.getId()!=null){
				 INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
				 quoteinfo.setQuotetotalinfoid(quotetotalinfoTemp.getId());
				 quoteinfo.setInscomcode(inscomcode);
				 quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
				 if(quoteinfo!=null){//获取投保地区信息
//					 if(quoteinfo.getInsprovincename()!=null){
//						 temp.put("subInsprovincename", quoteinfo.getInsprovincename());
//					 }
//					 if(quoteinfo.getInscityname()!=null){
//						 temp.put("subInscityname", quoteinfo.getInscityname());
//					 }
					 if(quoteinfo.getBuybusitype()!=null){//购买时供应商任务类型选项（01传统02网销03电销）
						 if("01".equals(quoteinfo.getBuybusitype())){
							 temp.put("buybusitype", "传统");
						 }else if("02".equals(quoteinfo.getBuybusitype())){
							 temp.put("buybusitype", "网销");
						 }else if("03".equals(quoteinfo.getBuybusitype())){
							 temp.put("buybusitype", "电销");
						 }
					 }
				 }
			}
		}
		//取得车辆信息
		INSBCarinfo carInfo = insbCarinfoDao.selectCarinfoByTaskId(taskId);
		temp.put("carinfoId", carInfo.getId());//车辆信息id
		INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
		carmodelinfo.setCarinfoid(carInfo.getId());
		carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
		//通过是否生成订单判断是否在人工报价节点
//		INSBWorkflowmain workflowmain = new INSBWorkflowmain();
//		workflowmain.setInstanceid(taskId);
//		workflowmain = insbWorkflowmainDao.selectOne(workflowmain);
//		 if(workflowmain!=null && "2".equals(workflowmain.getTaskcode())){
		INSBOrder order = new INSBOrder();
		order.setTaskid(taskId);
		order.setPrvid(inscomcode);
		order = insbOrderDao.selectOne(order);
//		if(order==null){
			//不能查到订单表明是人工报价节点
			//重新取得车辆信息
			INSBCarinfohis carInfohis = new INSBCarinfohis();
			carInfohis.setTaskid(taskId);
			carInfohis.setInscomcode(inscomcode);
			carInfohis = insbCarinfohisDao.selectOne(carInfohis);
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carInfo.getId());
			carmodelinfohis.setInscomcode(inscomcode);
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
			if(carmodelinfohis!=null){
				carmodelinfo = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
			}
			if(carInfohis!=null){
				carInfo = EntityTransformUtil.carinfohis2Carinfo(carInfohis);
			}
			Map<String, Object> rulequery = getCarInfoByTaskId(taskId);
			if(null!=carmodelinfohis&&null!=carInfohis&&null!=carInfohis.getCarproperty()){
				if(null!=VehicleTypeMapper.Mapper.get(carInfohis.getCarproperty())){
					String vehicleType = VehicleTypeMapper.Mapper.get(carInfohis.getCarproperty()).get(commonQuoteinfoService.getVehicleType(carInfohis, carmodelinfohis));
					temp.put("vehicleType", vehicleType); 
				} 
			}
			if(null!=rulequery.get("vehicletype")&&!"".equals(rulequery.get("vehicletype"))){
				temp.put("vehicleType", rulequery.get("vehicletype")); 
			}
//		 }
		 //得到驾驶人信息
		 INSBCarowneinfo carowner = new INSBCarowneinfo();
		 carowner.setTaskid(taskId);
		 carowner = insbCarowneinfoDao.selectOne(carowner);
		 INSBPerson ownerinfo = insbPersonDao.selectById(carowner.getPersonid());
		if(carmodelinfo!=null){
			if(carmodelinfo.getStandardfullname()!=null){
				temp.put("standardfullname", carmodelinfo.getStandardfullname());//车辆信息描述
			}
			if(carmodelinfo.getStandardname()!=null){
				temp.put("standardname", carmodelinfo.getStandardname());//车辆标准名称
			}
			if(carmodelinfo.getFamilyname()!=null){
				temp.put("familyname", carmodelinfo.getFamilyname());//车辆系列名称
			}
			if(carmodelinfo.getBrandname()!=null){
				temp.put("brandname", carmodelinfo.getBrandname());//品牌名称
			}
			if(carmodelinfo.getId()!=null){
				temp.put("id", carmodelinfo.getId());//车型表id
			}
			if(carmodelinfo.getSeat()!=null){
				temp.put("seat", carmodelinfo.getSeat());//承载人数
			}
			if(carmodelinfo.getUnwrtweight()!=null){
				temp.put("unwrtweight", carmodelinfo.getUnwrtweight());//核定载重量
			}
			if(carmodelinfo.getDisplacement()!=null){
				temp.put("displacement", carmodelinfo.getDisplacement());//发动机排量
			}
			if(carmodelinfo.getFullweight()!=null){
				temp.put("fullweight", carmodelinfo.getFullweight());//整备质量
			}
			if(carmodelinfo.getListedyear()!=null){
				temp.put("listedyear", carmodelinfo.getListedyear());//上市年份
			}
			if(carmodelinfo.getPrice()!=null){
				temp.put("price", carmodelinfo.getTaxprice());//新车购置价
			}
			if(carmodelinfo.getTaxprice()!=null){
				temp.put("addtaxprice", carmodelinfo.getPrice()+
						carmodelinfo.getTaxprice());//新车购置价含税
			}
			if(carmodelinfo.getPolicycarprice()!=null){
				temp.put("policycarprice", carmodelinfo.getPolicycarprice());//投保车价
			}
			if(carmodelinfo.getIsstandardcar()!=null){
				temp.put("isstandardcar", carmodelinfo.getIsstandardcar());//是否标准车型
			}
			if(carmodelinfo.getCarprice()!=null){
				temp.put("carprice", carmodelinfo.getCarprice());//车价选择
			}
			if(carmodelinfo.getCardeploy()!=null){
				temp.put("cardeploy", carmodelinfo.getCardeploy());//车型配置
			}
			if(carmodelinfo.getTaxprice()!=null){
				temp.put("taxprice", carmodelinfo.getTaxprice());//税额
			}
			if(carmodelinfo.getAnalogyprice()!=null){
				temp.put("analogyprice", carmodelinfo.getAnalogyprice());//类比价格
			}
			if(carmodelinfo.getAnalogytaxprice()!=null){
				temp.put("analogytaxprice", carmodelinfo.getAnalogytaxprice());//类比税额
			}
			if(carmodelinfo.getAliasname()!=null){
				temp.put("aliasname", carmodelinfo.getAliasname());//别名
			}
			if(carmodelinfo.getGearbox()!=null){
				temp.put("gearbox", carmodelinfo.getGearbox());//变速箱
			}
			if(carmodelinfo.getLoads()!=null){
				temp.put("loads", carmodelinfo.getLoads());//载荷
			}
		}
		if(carInfo!=null){
			if(carInfo.getOwner()!=null){
				temp.put("ownerid", carInfo.getOwner());//车主id
			}
			if(carInfo.getEngineno()!=null){
				temp.put("engineno", carInfo.getEngineno());//发动机号
			}
			if(carInfo.getCarlicenseno()!=null){
				temp.put("carlicenseno", carInfo.getCarlicenseno());//车牌号
			}
			if(carInfo.getVincode()!=null){
				temp.put("vincode", carInfo.getVincode());//车辆识别代码
			}
			if(carInfo.getOwnername()!=null){
				temp.put("ownername", carInfo.getOwnername());//车主姓名
			}
			if(carInfo.getRegistdate()!=null){
				temp.put("registdate", sdf.format(carInfo.getRegistdate()).toString());//车辆初始登录日期
			}
			if(carInfo.getMileage()!=null && !("".equals(carInfo.getMileage()))){
				temp.put("mileage", carInfo.getMileage());//平均行驶里程code
				temp.put("mileagevalue", inscCodeService.transferValueToName("avgmileage","avgmileage",carInfo.getMileage()));//平均行驶里程
			}
			if(carInfo.getDrivingarea()!=null && !("".equals(carInfo.getDrivingarea()))){
				temp.put("drivingarea", carInfo.getDrivingarea());//车辆行驶区域code
				temp.put("drivingareavalue", inscCodeService.transferValueToName("CarDrivingArea","CarDrivingArea",carInfo.getDrivingarea()));//车辆行驶区域
			}
			if(carInfo.getId()!=null){
				temp.put("id", carInfo.getId());//车辆信息id
			}
			if(carInfo.getIsTransfercar()!=null){
				temp.put("isTransfercar", carInfo.getIsTransfercar());//是否是过户车
			}
			//以下是车辆信息修改弹出框需要增加的取值字段
			if(carInfo.getCarproperty()!=null && !("".equals(carInfo.getCarproperty()))){
				temp.put("carproperty", carInfo.getCarproperty());//车辆使用性质code
				temp.put("carpropertyvalue", inscCodeService.transferValueToName("UseProps","UseProps",carInfo.getCarproperty()));//车辆使用性质
			}
			if(carInfo.getProperty()!=null && !("".equals(carInfo.getProperty()))){
				temp.put("property", carInfo.getProperty());//车辆所属性质code
				temp.put("propertyvalue", inscCodeService.transferValueToName("UserType","UserType",carInfo.getProperty()));//车辆所属性质
			}
			if("1".equals(carInfo.getIsTransfercar()) && carInfo.getTransferdate()!=null){
				temp.put("transferdate", sdf.format(carInfo.getTransferdate()));//过户日期
			}
			if (carInfo.getInsureconfigsameaslastyear() != null) {
				temp.put("insureconfigsameaslastyear", carInfo.getInsureconfigsameaslastyear());
			}
		}
		if(ownerinfo!=null){
			temp.put("ownerid", ownerinfo.getId());//车主id
			if(ownerinfo.getName()!=null){
				temp.put("ownername", ownerinfo.getName());//车主姓名
				temp.put("cellphone", ownerinfo.getCellphone());//车主手机号
			}
		}
		return temp;
	}

	/**
	 * 通过供应商code查询此供应商最上层供应商
	 */
	private String getTopProviderName(String inscomcode){
		if (StringUtils.isEmpty(inscomcode) || inscomcode.length()<4) {
			return null;
		}
		INSBProvider provider = new INSBProvider();
		provider.setPrvcode(inscomcode.substring(0,4));
		provider = insbProviderDao.selectOne(provider);
		if(provider!=null){
			return provider.getPrvshotname();
		}else{
			return null;
		}
	}
	
	/**
	 * 通过任务id查询车辆任务中其他信息
	 */
	@Override
	public Map<String, Object> getCarTaskOtherInfo(String taskId, String inscomcode, String opeType){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> temp = new HashMap<String, Object>();
        
        //取得是否电销
        INSBRulequeryotherinfo otherinfo = new INSBRulequeryotherinfo();
        otherinfo.setTaskid(taskId);
        INSBRulequeryotherinfo info = insbrulequeryotherinfoDao.selectOne(otherinfo);
        if(null!=info){
        	temp.put("pureesale", info.getPureesale());//是否通过纯电销投保
        }
        
		//取得车辆信息
		INSBCarinfo carInfo = insbCarinfoDao.selectCarinfoByTaskId(taskId);
		//得到车型信息
		/*INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
		carmodelinfo.setCarinfoid(carInfo.getId());
		carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);*/
		INSBOrder order = new INSBOrder();
		order.setTaskid(taskId);
		order.setPrvid(inscomcode);
		order = insbOrderDao.selectOne(order);
//		if(order==null){
			//不能查到订单表明是人工报价节点
			//重新得到车辆信息和车型信息数据
			INSBCarinfohis carInfohis = new INSBCarinfohis();
			carInfohis.setTaskid(taskId);
			carInfohis.setInscomcode(inscomcode);
			carInfohis = insbCarinfohisDao.selectOne(carInfohis);
			/*INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carInfo.getId());
			carmodelinfohis.setInscomcode(inscomcode);
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);*/
			if(carInfohis!=null){
				carInfo = EntityTransformUtil.carinfohis2Carinfo(carInfohis);
			}
			/*if(carmodelinfohis!=null){
				carmodelinfo = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
			}*/
//		}

        if(carInfo!=null){
            temp.put("carinfoId", carInfo.getId());
//				if(carInfo.getPreinscode()!=null && !("".equals(carInfo.getPreinscode()))){
//					INSBProvider provider = new INSBProvider();
//					provider.setPrvcode(carInfo.getPreinscode());
//					provider = insbProviderDao.selectOne(provider);
//					temp.put("preinscode", carInfo.getPreinscode());//上年投保公司code
//					if(provider!=null)
//					temp.put("preinsShortname", provider.getPrvshotname());//上年投保公司名称
//				}
            if(carInfo.getSpecifydriver()!=null && !("".equals(carInfo.getSpecifydriver()))){
                temp.put("Specifydriver", carInfo.getSpecifydriver());//是否指定驾驶人
            }else{
                temp.put("Specifydriver", "否");
            }
            if(carInfo.getCloudinscompany()!=null){
                temp.put("cloudinscompany", carInfo.getCloudinscompany());//云查询保险公司
            }
        }

        //xiugai wangyang
        if(order!=null){
            INSBOrderpayment orderpayment = new INSBOrderpayment();
            orderpayment.setOrderid(order.getId());
            orderpayment = insbOrderpaymentDao.selectOne(orderpayment);
            if(orderpayment!=null){
                if(orderpayment.getInsurecono()!=null){
                    temp.put("insurecono", orderpayment.getInsurecono());//支付流水号
                }
            }
            //01-续保,02-转保,03-新保,10-异地(客户忠诚度)
            if("01".equals(order.getCustomerinsurecode())){
                temp.put("customerinsurecode","01-续保");
            }else if("02".equals(order.getCustomerinsurecode())){
                temp.put("customerinsurecode","02-转保");
            }else if("03".equals(order.getCustomerinsurecode())){
                temp.put("customerinsurecode","03-新保");
            }else if("10".equals(order.getCustomerinsurecode())){
                temp.put("customerinsurecode","10-异地");
            }

        }

		temp.put("taskid", taskId);
		temp.put("inscomcode", inscomcode);
		//查询发票信息
		Map<String,String> params=new HashMap<String,String>();
		params.put("taskid", taskId);
		//params.put("inscomcode", inscomcode);
		params.put("inscomcode", null);
		INSBInvoiceinfo insbInvoiceinfo=insbInvoiceinfoDao.selectByTaskidAndComcode(params);
		if(insbInvoiceinfo!=null){
            temp.put("invoicetype", insbInvoiceinfo.getInvoicetype());
            temp.put("bankname",insbInvoiceinfo.getBankname());
            temp.put("accountnumber",insbInvoiceinfo.getAccountnumber());
            temp.put("identifynumber",insbInvoiceinfo.getIdentifynumber());
            temp.put("registerphone",insbInvoiceinfo.getRegisterphone());
            temp.put("registeraddress",insbInvoiceinfo.getRegisteraddress());
            temp.put("email",insbInvoiceinfo.getEmail());
		}

		//查询出单网点
		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(taskId);
		quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
		INSBQuoteinfo quoteinfo = null;
		if(quotetotalinfo !=null){
			quoteinfo = new INSBQuoteinfo();
			quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
			quoteinfo.setInscomcode(inscomcode);
			quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
			if(quoteinfo!=null){//获取投保地区信息
				if(!StringUtils.isEmpty(quoteinfo.getDeptcode())){
					INSCDept dept = inscDeptDao.selectByComcode(quoteinfo.getDeptcode());
					if(dept!=null){
						temp.put("deptComName", dept.getShortname());
						temp.put("deptComCode", dept.getComcode());
					}
				}
			}
		}

//		if("EDIT".equalsIgnoreCase(opeType)||"SHOW".equalsIgnoreCase(opeType)){
		INSBLastyearinsureinfo insbLastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskId);
		if( insbLastyearinsureinfo != null ) {
			temp.put("preinscode", insbLastyearinsureinfo.getSupplierid());//上年投保公司code
			temp.put("preinsShortname", insbLastyearinsureinfo.getSuppliername());//上年投保公司名称
            temp.put("preinsSypolicyno", insbLastyearinsureinfo.getSypolicyno());//上年商业险保单号
			// 非首次投保:0,新车首次投保:1,旧车首次投保:2
			if( "0".equals(INSBLastyearinsureinfo.convertFirstInsureTypeToCm(insbLastyearinsureinfo.getFirstinsuretype())) ) {
				temp.put("firstInsureType", "非首次投保");
			} else if( "1".equals(INSBLastyearinsureinfo.convertFirstInsureTypeToCm(insbLastyearinsureinfo.getFirstinsuretype()) )) {
				temp.put("firstInsureType", "新车首次投保");
			} else if( "2".equals(INSBLastyearinsureinfo.convertFirstInsureTypeToCm(insbLastyearinsureinfo.getFirstinsuretype()) )) {
				temp.put("firstInsureType", "旧车首次投保");
			}
			try {
				if (StringUtils.isNotEmpty(insbLastyearinsureinfo.getBwcommercialclaimtimes().toString())) {
					temp.put("syclaimtimes", insbLastyearinsureinfo.getBwcommercialclaimtimes());//商业险出险次数
				}
			} catch (NullPointerException e) {
				temp.put("syclaimtimes",0);
			}
			try {
				if (StringUtils.isNotEmpty(insbLastyearinsureinfo.getBwcompulsoryclaimtimes().toString())) {
					temp.put("jqclaimtimes", insbLastyearinsureinfo.getBwcompulsoryclaimtimes());//交强险出险次数
				}
			} catch (NullPointerException e) {
				temp.put("jqclaimtimes", 0);
			}

            //获取平台起保起期 /止期
            if(StringUtils.isEmpty(insbLastyearinsureinfo.getSystartdate())){
                temp.put("systartdate", "");
            }else{
//                if(insbLastyearinsureinfo.getSystartdate().length() > 10){
//                    temp.put("systartdate", insbLastyearinsureinfo.getSystartdate().substring(0, 10));
//                }else{
                    temp.put("systartdate", insbLastyearinsureinfo.getSystartdate());
//                }
            }
            if(StringUtils.isEmpty(insbLastyearinsureinfo.getSyenddate())){
                temp.put("syenddate", "");
            }else{
//                if(insbLastyearinsureinfo.getSyenddate().length() > 10){
//                    temp.put("syenddate", insbLastyearinsureinfo.getSyenddate().substring(0, 10));
//                }else{
                    temp.put("syenddate", insbLastyearinsureinfo.getSyenddate());
//                }
            }
            if(StringUtils.isEmpty(insbLastyearinsureinfo.getJqstartdate())){
                temp.put("jqstartdate", "");
            }else{
//                if(insbLastyearinsureinfo.getJqstartdate().length() > 10){
//                    temp.put("jqstartdate", insbLastyearinsureinfo.getJqstartdate().substring(0, 10));
//                }else{
                    temp.put("jqstartdate", insbLastyearinsureinfo.getJqstartdate());
//                }
            }
            if(StringUtils.isEmpty(insbLastyearinsureinfo.getJqenddate())){
                temp.put("jqenddate", "");
            }else{
//                if(insbLastyearinsureinfo.getJqenddate().length() > 10){
//                    temp.put("jqenddate", insbLastyearinsureinfo.getJqenddate().substring(0, 10));
//                }else{
                    temp.put("jqenddate", insbLastyearinsureinfo.getJqenddate());
                }
//            }
            Map<String, Object> rulequery = getCarInfoByTaskId(taskId);
            if(null!=rulequery.get("loyaltyreasons")){//如果规则平台查询有值，则先赋值
            	temp.put("loyaltyreasons", rulequery.get("loyaltyreasons"));
            }
            if(StringUtils.isNotEmpty(insbLastyearinsureinfo.getLoyaltyreasons())){//以普通平台查询的信息为准
            	temp.put("loyaltyreasons", insbLastyearinsureinfo.getLoyaltyreasons());
            }
		}
        //获取redis里平台信息
        /*LastYearPolicyInfoBean lastYearPolicyInfoBean = RedisClient.get(taskId, LastYearPolicyInfoBean.class);
        if(lastYearPolicyInfoBean!=null){
            if(lastYearPolicyInfoBean.getLastYearClaimBean()!=null){
//				temp.put("syclaimtimes", lastYearPolicyInfoBean.getLastYearClaimBean().getClaimtimes());//出险次数
//				temp.put("jqclaimtimes", lastYearPolicyInfoBean.getLastYearClaimBean().getJqclaimtimes());//交强险出险次数
                temp.put("syclaimtimes", lastYearPolicyInfoBean.getLastYearClaimBean().getBwcommercialclaimtimes());//商业险出险次数
                temp.put("jqclaimtimes", lastYearPolicyInfoBean.getLastYearClaimBean().getBwcompulsoryclaimtimes());//交强险出险次数
            }else {
                temp.put("syclaimtimes", 0);
                temp.put("jqclaimtimes", 0);
            }
        }*/

        boolean hasbusi = false;
        boolean hasstr = false;
        //查询保险起止日期
        INSBPolicyitem policyitem = new INSBPolicyitem();
        policyitem.setTaskid(taskId);
        policyitem.setInscomcode(inscomcode);
        List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);

        if(policyitemList!=null && policyitemList.size()>0){
            for (int i = 0; i < policyitemList.size(); i++) {
                if("0".equals(policyitemList.get(i).getRisktype())){//商业险起止日期
                    if(policyitemList.get(i).getStartdate()!=null){
                        temp.put("businessstartdate", sdf.format(policyitemList.get(i).getStartdate()).toString());
                    }
                    if(policyitemList.get(i).getEnddate()!=null){
                        temp.put("businessenddate", sdf.format(policyitemList.get(i).getEnddate()).toString());
                    }
                    if(policyitemList.get(i).getProposalformno()!=null){//商业险投保单号
                        temp.put("busProposalformno", policyitemList.get(i).getProposalformno());
                    }
                    if(policyitemList.get(i).getPolicyno()!=null){//商业险保单号
                        temp.put("busPolicyno", policyitemList.get(i).getPolicyno());
                    }
                    if(policyitemList.get(i).getCheckcode()!=null){//校验码
                        temp.put("checkcode", policyitemList.get(i).getCheckcode());
                    }
                    hasbusi = true;
                }else if("1".equals(policyitemList.get(i).getRisktype())){//交强险起止日期
                    if(policyitemList.get(i).getStartdate()!=null){
                        temp.put("compulsorystartdate", sdf.format(policyitemList.get(i).getStartdate()).toString());
                    }
                    if(policyitemList.get(i).getEnddate()!=null){
                        temp.put("compulsoryenddate", sdf.format(policyitemList.get(i).getEnddate()).toString());
                    }
                    if(policyitemList.get(i).getProposalformno()!=null){//交强险投保单号
                        temp.put("strProposalformno", policyitemList.get(i).getProposalformno());
                    }
                    if(policyitemList.get(i).getPolicyno()!=null){//交强险保单号
                        temp.put("strPolicyno", policyitemList.get(i).getPolicyno());
                    }
                    if(policyitemList.get(i).getCheckcode()!=null){//校验码
                        temp.put("checkcode", policyitemList.get(i).getCheckcode());
                    }
                    hasstr = true;
                }
            }
        }
        temp.put("hasbusi", hasbusi);
        temp.put("hasstr", hasstr);
//		}
		return temp;
	}
	@Resource
	private INSBRulequeryotherinfoDao insbrulequeryotherinfoDao;
	@Resource
	private INSBRulequerycarinfoDao insbrulequerycarinfoDao;
	@Resource
	private INSBRulequeryrepeatinsuredDao insbrulequeryrepeatinsuredDao;
	@Resource
	private INSBRulequeryclaimsDao insbrulequeryclaimsDao;
	@Override
	public Map<String, Object> getCarInfoByTaskId(String taskid) { 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		INSBCarinfo iNSBCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
		Map<String, Object> temp = new HashMap<String, Object>();
		if(iNSBCarinfo!=null){
			temp.put("carlicenseno", iNSBCarinfo.getCarlicenseno());
			temp.put("ownername", iNSBCarinfo.getOwnername());
			temp.put("property", iNSBCarinfo.getProperty());
			temp.put("standardfullname", iNSBCarinfo.getStandardfullname());
			temp.put("carproperty", iNSBCarinfo.getCarproperty());
			temp.put("vincode", iNSBCarinfo.getVincode());
			temp.put("isTransfercar", iNSBCarinfo.getIsTransfercar());
			temp.put("mileage", iNSBCarinfo.getMileage());
			temp.put("drivingarea", iNSBCarinfo.getDrivingarea());
			temp.put("engineno", iNSBCarinfo.getEngineno());
			if(null!=iNSBCarinfo.getTransferdate())
				temp.put("transferdate", sdf.format(iNSBCarinfo.getTransferdate()));
			if(null!=iNSBCarinfo.getRegistdate())	
				temp.put("registdate", sdf.format(iNSBCarinfo.getRegistdate()));
		}
		INSBRulequerycarinfo rulecarinfo = new INSBRulequerycarinfo();
		rulecarinfo.setTaskid(taskid);
		List<INSBRulequerycarinfo> rulecarinfos = insbrulequerycarinfoDao.selectList(rulecarinfo);
		if(null!=rulecarinfos && rulecarinfos.size()>0){ 
			rulecarinfo = rulecarinfos.get(0);
			temp.put("licenseno", rulecarinfo.getLicenseno());//车牌
			temp.put("engineno", rulecarinfo.getEngineno());//发动机
			temp.put("vinno", rulecarinfo.getVinno());//vin码
			temp.put("enrolldate", rulecarinfo.getEnrolldate());//初登日期
			temp.put("carbrandname", rulecarinfo.getCarbrandname());//品牌名称
			temp.put("price", rulecarinfo.getPrice() != null && rulecarinfo.getPrice() > 0.0 ? rulecarinfo.getPrice().toString() : "");// 新车购置价
			temp.put("taxprice", rulecarinfo.getTaxprice() != null && rulecarinfo.getTaxprice() > 0.0 ? rulecarinfo.getTaxprice().toString() : "");// 新车购置价(含税)
			temp.put("analogyprice", rulecarinfo.getAnalogyprice() != null && rulecarinfo.getAnalogyprice() > 0.0 ? rulecarinfo.getAnalogyprice().toString() : "");// 类比价
			temp.put("analogytaxprice", rulecarinfo.getAnalogytaxprice() != null && rulecarinfo.getAnalogytaxprice() > 0.0 ? rulecarinfo.getAnalogytaxprice().toString() : "");// 类比价(含税)
			temp.put("carmodeldate", rulecarinfo.getCarmodeldate());//上市年份
			temp.put("seatcnt", rulecarinfo.getSeatcnt());//核定载客
			temp.put("modelload", String.valueOf(rulecarinfo.getModelload()));//核定载质量
			temp.put("fullload", String.valueOf(rulecarinfo.getFullload()));//整备质量
			temp.put("displacement", String.valueOf(rulecarinfo.getDisplacement()));//排气量
			temp.put("trademodelcode", rulecarinfo.getTrademodelcode());//行业车型编码
			temp.put("selfinsurerate", String.valueOf(rulecarinfo.getSelfinsurerate()));//自主核保系数
			temp.put("selfchannelrate", String.valueOf(rulecarinfo.getSelfchannelrate()));//自主渠道系数
			temp.put("ncdrate", String.valueOf(rulecarinfo.getNcdrate()));//NCD系数
			temp.put("vehicletype", rulecarinfo.getVehicletype());//车辆种类
			temp.put("basicriskpremium", String.valueOf(rulecarinfo.getBasicriskpremium()));//机动车车损纯风险基准保费
			temp.put("insureco", rulecarinfo.getInsureco());//查询公司名称
			temp.put("modelcode", rulecarinfo.getModelcode());//车型编码
			temp.put("creattime", sdf.format(rulecarinfo.getModifytime()));//数据有效时间
		}else{
			temp.put("result", 0);//表示规则平台查询没有返回数据
		}
		INSBRulequeryrepeatinsured insbrulequeryrepeatinsured = new INSBRulequeryrepeatinsured();
		insbrulequeryrepeatinsured.setTaskid(taskid);
		List<INSBRulequeryrepeatinsured> insbrulequeryrepeatinsureds = insbrulequeryrepeatinsuredDao.selectList(insbrulequeryrepeatinsured);
		if(null!=insbrulequeryrepeatinsureds && insbrulequeryrepeatinsureds.size()>0){ 
			temp.put("repeats", insbrulequeryrepeatinsureds);
			for(INSBRulequeryrepeatinsured s:insbrulequeryrepeatinsureds){
				if(s.getRisktype() == null || s.getRisktype().equals(temp.get("risktype1")) ||  s.getRisktype().equals(temp.get("risktype0")))
						continue;
				if("1".equals(s.getRisktype())){
					temp.put("inscorpid1", s.getInscorpid());//保险公司ID
					temp.put("inscorpcode1", s.getInscorpcode());//保险公司代码
					temp.put("inscorpname1", s.getInscorpname());//保险公司名称
					temp.put("policystarttime1", s.getPolicystarttime());//出险时间
					temp.put("policyendtime1", s.getPolicyendtime());//结案时间
					temp.put("policyid1", s.getPolicyid());//保单号
					temp.put("risktype1", s.getRisktype());//0 商业险 1 交强险 
				}else if("0".equals(s.getRisktype())){
					temp.put("inscorpid0", s.getInscorpid());//保险公司ID
					temp.put("inscorpcode0", s.getInscorpcode());//保险公司代码
					temp.put("inscorpname0", s.getInscorpname());//保险公司名称
					temp.put("policystarttime0", s.getPolicystarttime());//出险时间
					temp.put("policyendtime0", s.getPolicyendtime());//结案时间
					temp.put("policyid0", s.getPolicyid());//保单号
					temp.put("risktype0", s.getRisktype());//0 商业险 1 交强险 
				}
			}
		}
			
		INSBRulequeryclaims insbrulequeryclaim = new INSBRulequeryclaims();
		insbrulequeryclaim.setTaskid(taskid);
		List<INSBRulequeryclaims> insbrulequeryclaims = insbrulequeryclaimsDao.selectList(insbrulequeryclaim);
		List<Map<String, String>> syclaimes = new ArrayList<Map<String, String>>();
		List<Map<String, String>> jqclaimes = new ArrayList<Map<String, String>>();
		if(null!=insbrulequeryclaims && insbrulequeryclaims.size()>0){
			temp.put("claims", insbrulequeryclaims);
			Map<String, String> syclaime,jqclaime;
			boolean flag = false;
			for(INSBRulequeryclaims c:insbrulequeryclaims){
				if("1".equals(c.getRisktype())){
					flag = false;
					for(Map<String, String> map : jqclaimes){
						if((c.getCasestarttime() + c.getRisktype()).equals(map.get("casestarttimec1") + map.get("risktypec1"))){
							flag = true; break;
						}
					}
					if(flag) continue;
					jqclaime = new HashMap<String, String>();
					jqclaime.put("inscorpidc1", c.getInscorpid());//保险公司ID
					jqclaime.put("inscorpcodec1", c.getInscorpcode());//保险公司代码
					jqclaime.put("inscorpnamec1", c.getInscorpname());//保险公司名称
					jqclaime.put("casestarttimec1", c.getCasestarttime());//出险时间
					jqclaime.put("caseendtimec1", c.getCaseendtime());//结案时间
					jqclaime.put("claimamountc1", String.valueOf(c.getClaimamount()));//理赔金额
					jqclaime.put("policyidc1", c.getPolicyid());//保单号
					jqclaime.put("risktypec1", c.getRisktype());//0 商业险 1 交强险
					jqclaimes.add(jqclaime);
				}else if("0".equals(c.getRisktype())){ 
					flag = false;
					for(Map<String, String> map : syclaimes){
						if((c.getCasestarttime() + c.getRisktype()).equals(map.get("casestarttimec0") + map.get("risktypec0"))){
							flag = true;	break;
						}
					}
					if(flag)
						continue;
					
					syclaime = new HashMap<String, String>();
					syclaime.put("inscorpidc0", c.getInscorpid());//保险公司ID
					syclaime.put("inscorpcodec0", c.getInscorpcode());//保险公司代码
					syclaime.put("inscorpnamec0", c.getInscorpname());//保险公司名称
					syclaime.put("casestarttimec0", c.getCasestarttime());//出险时间
					syclaime.put("caseendtimec0", c.getCaseendtime());//结案时间
					syclaime.put("claimamountc0", String.valueOf(c.getClaimamount()));//理赔金额
					syclaime.put("policyidc0", c.getPolicyid());//保单号
					syclaime.put("risktypec0", c.getRisktype());//0 商业险 1 交强险
					syclaimes.add(syclaime);
				}
			}
		}
		temp.put("syclaimes", syclaimes);
		temp.put("jqclaimes", jqclaimes);
		INSBRulequeryotherinfo otherinfo = new INSBRulequeryotherinfo();
		otherinfo.setTaskid(taskid);
		List<INSBRulequeryotherinfo> others = insbrulequeryotherinfoDao.selectList(otherinfo);
		if(null!=others && others.size()>0){
			otherinfo = others.get(0);
			temp.put("noclaimdiscountcoefficient", String.valueOf(otherinfo.getNoclaimdiscountcoefficient()));//无赔款优待系数
			temp.put("noclaimdiscountcoefficientreasons", otherinfo.getNoclaimdiscountcoefficientreasons());//无赔款折扣浮动原因
			temp.put("loyaltyreasons", otherinfo.getLoyaltyreasons());//客户忠诚度原因
			temp.put("trafficoffencediscount", String.valueOf(otherinfo.getTrafficoffencediscount()));//交通违法系数
			temp.put("compulsoryclaimrate", String.valueOf(otherinfo.getCompulsoryclaimrate()));//交强险理赔系数
			temp.put("compulsoryclaimratereasons", otherinfo.getCompulsoryclaimratereasons());//交强险浮动原因
			temp.put("firstinsuretype", otherinfo.getFirstinsuretype());//投保类型
			temp.put("bwcommercialclaimtimes", otherinfo.getBwcommercialclaimtimes());//上年商业险理赔次数
			temp.put("bwlastclaimsum", otherinfo.getBwlastclaimsum().toString());//上年商业险理赔金额
			temp.put("bwcompulsoryclaimtimes", otherinfo.getBwcompulsoryclaimtimes());//上年交强险理赔次数
			temp.put("bwlastcompulsoryclaimsum", String.valueOf(otherinfo.getBwlastcompulsoryclaimsum()));//上年交强险理赔金额
			temp.put("claimtimes", otherinfo.getClaimtimes());//平台商业险理赔次数
			temp.put("compulsoryclaimtimes", otherinfo.getCompulsoryclaimtimes());//平台交强险理赔次数
			temp.put("syendmark", otherinfo.getSyendmark());//商业险起保标识
			temp.put("errormsgsy", otherinfo.getErrormsgsy());//商业险起保提示语
			temp.put("jqendmark", otherinfo.getJqendmark());//交强险起保标识
			temp.put("errormsgjq", otherinfo.getErrormsgjq());//交强险起保提示语
			temp.put("efcdiscount", otherinfo.getEfcdiscount());//交强险折扣保费
			temp.put("vehicletaxoverduefine", otherinfo.getVehicletaxoverduefine());//车船税滞纳金
			temp.put("riskclass", otherinfo.getRiskclass());//风险类别
			temp.put("pureesale", otherinfo.getPureesale());//是否通过纯电销投保
			temp.put("bwcommercialclaimrate", otherinfo.getBwcommercialclaimrate());//上年商业险赔付率
			temp.put("lwarrearstax", otherinfo.getLwarrearstax());//往年补缴税额
			temp.put("platformcarprice", otherinfo.getPlatformcarprice());//平台车价
			temp.put("drunkdrivingrate", otherinfo.getDrunkdrivingrate());//酒驾系数
		}
		return temp;
	}

	@Override
	public String showMytaskInfo(Map<String, Object> map, int page) {
//		List<INSBCarinfo> carinfoList = insbCarinfoDao.selectCarinfoList(map);
//		List<INSBPolicyitem> policyitemList = insbPolicyitemDao
//				.selectPolicyitemList(map);
//
//		List<Map<Object, Object>> resultList = new ArrayList<Map<Object, Object>>();
//		Map<Object, Object> initMap = new HashMap<Object, Object>();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		// 数据条数
//		int total = 0;
//		if(carinfoList.size()>0){
//			total = carinfoList.size();
//		}else if(policyitemList.size()>0){
//			total = policyitemList.size();
//		}
//		// 页面尺寸（每页数据条数）
//		int pagesize = 10;
//		// 总共的页面数
//		int pages = (int) Math.ceil(total * 1.0 / pagesize);
//		// 数据起止范围start~end
//		int start = (page - 1) * pagesize;
//		int end = page * pagesize;
//		if (end > total) {
//			end = total;
//		}
//		initMap.put("pagesize", pagesize);
//		initMap.put("pages", pages);
//		initMap.put("total", total);
//		if (carinfoList.size()>0) {
//			for (int i = start; i < end; i++) {
//				INSBCarinfo tempInfo = carinfoList.get(i);
//
//				String taskid = tempInfo.getTaskid();
//				INSBPolicyitem policyitem = insbPolicyitemDao.selectPolicyitemByTaskId(taskid);
//				INSBOrder order=insbOrderDao.selectOrderByTaskId(taskid);
//				Map<Object, Object> tempMap = new HashMap<Object, Object>();
//				tempMap.put("id", tempInfo.getId());
//				tempMap.put("operator", tempInfo.getOperator());
//				if (tempInfo.getCreatetime() != null) {
//					tempMap.put("createtime",
//							sdf.format(tempInfo.getCreatetime()));
//				}
//				if (tempInfo.getModifytime() != null) {
//					tempMap.put("modifytime",
//							sdf.format(tempInfo.getModifytime()));
//				}
//				tempMap.put("noti", tempInfo.getNoti());
//				tempMap.put("selectins", tempInfo.getSelectins());
//				tempMap.put("owner", tempInfo.getOwner());
//				tempMap.put("ownername", tempInfo.getOwnername());
//				tempMap.put("carlicenseno", tempInfo.getCarlicenseno());
//				tempMap.put("standardfullname", tempInfo.getStandardfullname());
//				tempMap.put("selectins", tempInfo.getSelectins());
//				
//				if(policyitem!=null){
//					//被保人
//					tempMap.put("insuredname", policyitem.getInsuredname());
//				}
//				if(order!=null){
//					//代理人
//					tempMap.put("agentname", order.getAgentname());
//				}
//				
//				tempMap.put("operating1", "link(\'" + tempInfo.getTaskid() + "\');");
//				tempMap.put(
//						"operating2",
//						"<a href=\"javascript:deletecarinfo(\'"
//								+ tempInfo.getId() + "\');\" >删除</a>");
//				resultList.add(tempMap);
//			}
//		} else if (policyitemList.size()>0) {
//			for (int i = start; i < end; i++) {
//				INSBPolicyitem tempPolicyitemInfo = policyitemList.get(i);
//				String taskid = tempPolicyitemInfo.getTaskid();
//				INSBPolicyitem policyitem = insbPolicyitemDao.selectPolicyitemByTaskId(taskid);
//				INSBOrder order=insbOrderDao.selectOrderByTaskId(taskid);
//				INSBCarinfo tempInfo = insbCarinfoDao
//						.selectCarinfoByTaskId(taskid);
//
//				Map<Object, Object> tempMap = new HashMap<Object, Object>();
//
//				tempMap.put("id", tempInfo.getId());
//				tempMap.put("operator", tempInfo.getOperator());
//				if (tempInfo.getCreatetime() != null) {
//					tempMap.put("createtime",
//							sdf.format(tempInfo.getCreatetime()));
//				}
//				if (tempInfo.getModifytime() != null) {
//					tempMap.put("modifytime",
//							sdf.format(tempInfo.getModifytime()));
//				}
//				tempMap.put("noti", tempInfo.getNoti());
//				tempMap.put("selectins", tempInfo.getSelectins());
//				tempMap.put("owner", tempInfo.getOwner());
//				tempMap.put("ownername", tempInfo.getOwnername());
//				tempMap.put("carlicenseno", tempInfo.getCarlicenseno());
//				tempMap.put("standardfullname", tempInfo.getStandardfullname());
//				tempMap.put("selectins", tempInfo.getSelectins());
//				
//				if(policyitem!=null){
//					//被保人
//					tempMap.put("insuredname", policyitem.getInsuredname());
//				}
//				if(order!=null){
//					//代理人
//					tempMap.put("agentname", order.getAgentname());
//				}
//				
//				tempMap.put("operating1", "link(\'" + tempInfo.getTaskid() + "\');");
//				tempMap.put(
//						"operating2",
//						"<a href=\"javascript:deletecarinfo(\'"
//								+ tempInfo.getTaskid() + "\');\" >删除</a>");
//				resultList.add(tempMap);
//			}
//		}
//
//		initMap.put("rows", resultList);
//		JSONObject jsonObject = JSONObject.fromObject(initMap);
//		return jsonObject.toString();
		return null;
	}

	
	@Override
	public String showMytaskInfo_ad(Map<String, Object> map, int page) {
//		List<String> orderList = insbOrderDao.selectOrdersByMap(map);
//
//		List<Map<Object, Object>> resultList = new ArrayList<Map<Object, Object>>();
//		Map<Object, Object> initMap = new HashMap<Object, Object>();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		// 数据条数
//		int total = 0;
//		if(orderList.size()>0){
//			total = orderList.size();
//		}
//		// 页面尺寸（每页数据条数）
//		int pagesize = 3;
//		// 总共的页面数
//		int pages = (int) Math.ceil(total * 1.0 / pagesize);
//		// 数据起止范围start~end
//		int start = (page - 1) * pagesize;
//		int end = page * pagesize;
//		if (end > total) {
//			end = total;
//		}
//		initMap.put("pagesize", pagesize);
//		initMap.put("pages", pages);
//		initMap.put("total", total);
//		if (orderList.size()>0) {
//			for (int i = start; i < end; i++) {
//
//				String taskid = orderList.get(i);
//				INSBCarinfo tempInfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
//				INSBPolicyitem policyitem = insbPolicyitemDao.selectPolicyitemByTaskId(taskid);
//				INSBOrder order=insbOrderDao.selectOrderByTaskId(taskid);
//				Map<Object, Object> tempMap = new HashMap<Object, Object>();
//				tempMap.put("id", tempInfo.getId());
//				tempMap.put("operator", tempInfo.getOperator());
//				if (tempInfo.getCreatetime() != null) {
//					tempMap.put("createtime",
//							sdf.format(tempInfo.getCreatetime()));
//				}
//				if (tempInfo.getModifytime() != null) {
//					tempMap.put("modifytime",
//							sdf.format(tempInfo.getModifytime()));
//				}
//				tempMap.put("noti", tempInfo.getNoti());
//				tempMap.put("selectins", tempInfo.getSelectins());
//				tempMap.put("owner", tempInfo.getOwner());
//				tempMap.put("ownername", tempInfo.getOwnername());
//				tempMap.put("carlicenseno", tempInfo.getCarlicenseno());
//				tempMap.put("standardfullname", tempInfo.getStandardfullname());
//				tempMap.put("selectins", tempInfo.getSelectins());
//				
//				if(policyitem!=null){
//					//被保人
//					tempMap.put("insuredname", policyitem.getInsuredname());
//				}
//				if(order!=null){
//					//代理人
//					tempMap.put("agentname", order.getAgentname());
//				}
//				
//				tempMap.put("operating1", "link(\'" + tempInfo.getTaskid() + "\');");
//				tempMap.put(
//						"operating2",
//						"<a href=\"javascript:deletecarinfo(\'"
//								+ tempInfo.getId() + "\');\" >删除</a>");
//				resultList.add(tempMap);
//			}
//		}
//		initMap.put("rows", resultList);
//		JSONObject jsonObject = JSONObject.fromObject(initMap);
//		return jsonObject.toString();
		return null;
	}
	
	@Override
	public String editCarInfoAndModelInfo(CarinfoVO carinfo,INSBCarmodelinfo carmodelinfo) {
		Date date = new Date();
		String taskid = carinfo.getTaskid();
		String inscomcode = carinfo.getInscomcode();
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
		//2016年2月17日新需求要求每个节点都要查询修改历史表数据
		noHasOrder = true;
		//修改车辆信息
		INSBCarinfohis carinfohis = new INSBCarinfohis();
		carinfohis.setTaskid(taskid);
		carinfohis.setInscomcode(inscomcode);
		carinfohis = insbCarinfohisDao.selectOne(carinfohis);
		INSBCarinfo carinfoTemp = new INSBCarinfo();
		carinfoTemp.setTaskid(taskid);
		carinfoTemp = insbCarinfoDao.selectOne(carinfoTemp);
		if(noHasOrder){//操作车辆信息历史表数据
			if(carinfohis!=null){
				carinfohis.setModifytime(date);
			}else{//从车辆信息表中抽取数据复制到历史表中
				carinfohis = EntityTransformUtil.carinfo2Carinfohis(carinfoTemp, inscomcode);
				carinfohis.setCreatetime(date);
				carinfohis.setModifytime(null);
			}
			carinfohis.setOperator(carinfo.getOperator());
			carinfohis.setCarproperty(carinfo.getCarproperty());
			carinfohis.setProperty(carinfo.getProperty());
			carinfohis.setEngineno(carinfo.getEngineno());
			carinfohis.setVincode(carinfo.getVincode());
			carinfohis.setRegistdate(this.String2Date(carinfo.getRegistdate()));
			carinfohis.setIsTransfercar(carinfo.getIsTransfercar());
			if("1".equals(carinfo.getIsTransfercar())){
				carinfohis.setTransferdate(this.String2Date(carinfo.getTransferdate()));
			}
			carinfohis.setMileage(carinfo.getMileage());
			carinfohis.setDrivingarea(carinfo.getDrivingarea());
			carinfohis.setStandardfullname(carmodelinfo.getStandardname());
			if(carinfohis.getId()!=null){//修改车辆信息历史表数据
				insbCarinfohisDao.updateById(carinfohis);
			}else{//新建车辆信息历史表数据
				insbCarinfohisDao.insert(carinfohis);
			}
		}else{//修改车辆信息表数据
			carinfoTemp.setModifytime(date);
			carinfoTemp.setOperator(carinfo.getOperator());
			carinfoTemp.setCarproperty(carinfo.getCarproperty());
			carinfoTemp.setProperty(carinfo.getProperty());
			carinfoTemp.setEngineno(carinfo.getEngineno());
			carinfoTemp.setVincode(carinfo.getVincode());
			carinfoTemp.setRegistdate(this.String2Date(carinfo.getRegistdate()));
			carinfoTemp.setIsTransfercar(carinfo.getIsTransfercar());
			if("1".equals(carinfo.getIsTransfercar())){
				carinfoTemp.setTransferdate(this.String2Date(carinfo.getTransferdate()));
			}
			carinfoTemp.setMileage(carinfo.getMileage());
			carinfoTemp.setDrivingarea(carinfo.getDrivingarea());
			carinfoTemp.setStandardfullname(carmodelinfo.getStandardname());
			insbCarinfoDao.updateById(carinfoTemp);
			LogUtil.info("INSBCarinfo|报表数据埋点|"+JSONObject.fromObject(carinfoTemp).toString());
		}
		//修改车型信息
		INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
		carmodelinfohis.setCarinfoid(carinfoTemp.getId());
		carmodelinfohis.setInscomcode(inscomcode);
		carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
		INSBCarmodelinfo carmodelinfoTemp = new INSBCarmodelinfo();
		carmodelinfoTemp.setCarinfoid(carinfoTemp.getId());
		carmodelinfoTemp = insbCarmodelinfoDao.selectOne(carmodelinfoTemp);
		if(null == carmodelinfoTemp){//修改车型信息表数据
			carmodelinfoTemp = new INSBCarmodelinfo();
			carmodelinfoTemp.setCarinfoid(carinfoTemp.getId());
			carmodelinfoTemp.setTaxprice(carmodelinfo.getPrice());
			carmodelinfoTemp.setAnalogyprice(carmodelinfo.getPrice());
			carmodelinfoTemp.setAnalogytaxprice(carmodelinfo.getPrice());
			carmodelinfoTemp.setModifytime(date);
			carmodelinfoTemp.setOperator(carmodelinfo.getOperator());
			carmodelinfoTemp.setStandardfullname(carmodelinfo.getStandardname());
			carmodelinfoTemp.setStandardname(carmodelinfo.getStandardname());
			carmodelinfoTemp.setIsstandardcar(carmodelinfo.getIsstandardcar());
			carmodelinfoTemp.setSeat(carmodelinfo.getSeat());
			carmodelinfoTemp.setUnwrtweight(carmodelinfo.getUnwrtweight());
			carmodelinfoTemp.setDisplacement(carmodelinfo.getDisplacement());
			carmodelinfoTemp.setFullweight(carmodelinfo.getFullweight());
			carmodelinfoTemp.setCarprice(carmodelinfo.getCarprice());
			carmodelinfoTemp.setCreatetime(new Date());
			if("2".equals(carmodelinfo.getCarprice())){
				carmodelinfoTemp.setPolicycarprice(carmodelinfo.getPolicycarprice());
			}
			carmodelinfoTemp.setListedyear(carmodelinfo.getListedyear());
			carmodelinfoTemp.setPrice(carmodelinfo.getPrice());
			insbCarmodelinfoDao.insert(carmodelinfoTemp);
		}
		if(noHasOrder){//操作车型信息历史表数据
			if(carmodelinfohis!=null){//修改车型信息历史表数据
				carmodelinfohis.setModifytime(date);
			} else if (carmodelinfoTemp != null) {//新建车型信息历史表数据
				carmodelinfohis = EntityTransformUtil.carmodelinfo2Carmodelinfohis(carmodelinfoTemp, inscomcode);
                carmodelinfohis.setModifytime(null);
                carmodelinfohis.setCreatetime(date);
			}
            if (carmodelinfohis != null) {
                carmodelinfohis.setOperator(carmodelinfo.getOperator());
                carmodelinfohis.setStandardfullname(carmodelinfo.getStandardname());
				carmodelinfohis.setStandardname(carmodelinfo.getStandardname());
                carmodelinfohis.setIsstandardcar(carmodelinfo.getIsstandardcar());
                carmodelinfohis.setSeat(carmodelinfo.getSeat());
                carmodelinfohis.setUnwrtweight(carmodelinfo.getUnwrtweight());
                carmodelinfohis.setDisplacement(carmodelinfo.getDisplacement());
                carmodelinfohis.setFullweight(carmodelinfo.getFullweight());
                carmodelinfohis.setCarprice(carmodelinfo.getCarprice());
                if ("2".equals(carmodelinfo.getCarprice())) {
                    carmodelinfohis.setPolicycarprice(carmodelinfo.getPolicycarprice());
                }
                carmodelinfohis.setListedyear(carmodelinfo.getListedyear());
                carmodelinfohis.setPrice(carmodelinfo.getPrice());
                carmodelinfohis.setTaxprice(carmodelinfo.getPrice());
                //2016-11-18 新增设置精友code
                if (StringUtil.isNotEmpty(carmodelinfo.getVehicleid())){
                    String cifcode = interFaceService.getRbCode(carmodelinfo.getVehicleid(),inscomcode,taskid);
                    carmodelinfohis.setJyCode(cifcode);
                    carmodelinfohis.setRbCode(cifcode); 
                }
				//bug8760 新增车辆种类
				carmodelinfohis.setSyvehicletypename(carmodelinfo.getSyvehicletypename());
                if (carmodelinfohis.getId() != null) {//修改车型信息历史表数据
                    insbCarmodelinfohisDao.updateById(carmodelinfohis);
                } else {//新建车型信息历史表数据
                    insbCarmodelinfohisDao.insert(carmodelinfohis);
                }
            }
		} else if (carmodelinfoTemp != null){//修改车型信息表数据
			carmodelinfoTemp = new INSBCarmodelinfo();
			carmodelinfoTemp.setCreatetime(date);
			carmodelinfoTemp.setOperator(carmodelinfo.getOperator());
			carmodelinfoTemp.setStandardfullname(carmodelinfo.getStandardname());
			carmodelinfoTemp.setStandardname(carmodelinfo.getStandardname());
			carmodelinfoTemp.setIsstandardcar(carmodelinfo.getIsstandardcar());
			carmodelinfoTemp.setSeat(carmodelinfo.getSeat());
			carmodelinfoTemp.setUnwrtweight(carmodelinfo.getUnwrtweight());
			carmodelinfoTemp.setDisplacement(carmodelinfo.getDisplacement());
			carmodelinfoTemp.setFullweight(carmodelinfo.getFullweight());
			carmodelinfoTemp.setCarprice(carmodelinfo.getCarprice());
			if("2".equals(carmodelinfo.getCarprice())){
				carmodelinfoTemp.setPolicycarprice(carmodelinfo.getPolicycarprice());
			}
			carmodelinfoTemp.setListedyear(carmodelinfo.getListedyear());
			carmodelinfoTemp.setPrice(carmodelinfo.getPrice());
			insbCarmodelinfoDao.updateById(carmodelinfoTemp);
		}
		//修改保单表里车辆全称信息信息
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(taskid);
		policyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> policyitemList  = insbPolicyitemDao.selectList(policyitem);
		if(policyitemList!=null && policyitemList.size()>0){
			for (int i = 0; i < policyitemList.size(); i++) {
				if(policyitemList.get(i) != null){
					policyitemList.get(i).setModifytime(date);
					policyitemList.get(i).setOperator(carmodelinfo.getOperator());
					policyitemList.get(i).setStandardfullname(carmodelinfo.getStandardname());
					insbPolicyitemDao.updateById(policyitemList.get(i));
				}
			}
		}
		return "success";
	}
	/**
	 * String转Date
	 * */
	public Date String2Date(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString="";
		if(dateStr!=null){
			dateString = dateStr.trim();
		}
		if(dateString.length()>10){
			dateString = dateString.substring(0, 10);
		}
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			return date;
		}
		return date;
	}
	
	/**
	 * 车险任务分配
	 */
//	@Override
//	public String carTaskDispatch(String userid, List<Map<String,Object>> paramsList) {
//		try {
//			// 选择多个任务同时分配，循环调用工作流接口分配
//			for (int i = 0; i < paramsList.size(); i++) {
//				Map<String, String> params = new HashMap<String, String>();
//				Map<String, Object> datas = new HashMap<String, Object>();
//				datas.put("taskid", (String)paramsList.get(i).get("taskid"));//活动任务节点id
//				datas.put("processinstanceid", (String)paramsList.get(i).get("maininstanceId"));//流程实例ID
//				datas.put("process", ((String)paramsList.get(i).get("subInstanceId")).equals(
//						(String)paramsList.get(i).get("maininstanceId"))?"main":"sub");//流程标示，（main）为主流程，（sub）为子流程
//				datas.put("userid", userid);//指定人员的id或重新指定人员的id
//				JSONObject datasJSON = JSONObject.fromObject(datas);
//				params.put("datas", datasJSON.toString());
//				System.out.println(datasJSON.toString());
//				String result = "";
//				if(paramsList.get(i).get("operator")==null || "".equals(paramsList.get(i).get("operator"))){
//					//params.put("userId", userid);//指定人员的id
//					//调取工作流任务认领接口
//					result = HttpClientUtil.doGet("http://203.195.141.57:8080/workflow/process/claimTask", params);
//				}else{
//					//params.put("userId", (String)paramsList.get(i).get("operator"));//已指定人员的id
//					//params.put("targetUserId", userid);//重新指定人员的id
//					//调工作流改派任务接口
//					result = HttpClientUtil.doGet("http://203.195.141.57:8080/workflow/process/delegateTask", params);
//				}
//				System.out.println(result);
//				//得到请求结果json{"fail":"errorTask '1' not found","message":"fail"}
//				JSONObject jo = JSONObject.fromObject(result);
//				if("success".equals(jo.getString("message"))){
//					//如果指派人员成功，暂不同步维护数据库
//				}else{
//					throw new Exception(jo.getString("reason"));
////					throw new Exception(jo.getString("fail"));
//				}
//			}
//			return "success";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "fail";
//		}
//	}
	
	/**
	 * 车险任务分配
	 */
	@Override
	public String carTaskDispatch(String toUser, String maininstanceId, String subInstanceId, 
			String inscomcode, String operator, INSCUser loginUser) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			INSCUser temp = inscUserDao.selectByUserCode(toUser);
			if(temp!=null){
				Map<String, String> param = new HashMap<String, String>();
				if(maininstanceId.equals(subInstanceId)){
					if(operator==null||"".equals(operator)){
						dispatchService.getTask(maininstanceId, inscomcode, 1, loginUser, temp);
					}else if("check".equals(operator)){//验证主任务是否已经被打开
						INSBWorkflowmaintrackdetail lastmodel = insbWorkflowmaintrackdetailDao.copyTaskFeed4CompletedState(maininstanceId);
						if(null!=lastmodel && "Proccessing".equals(lastmodel.getOperatorstate())){
							result.put("status", "presuccess");
							result.put("msg", "任务有可能正在处理中，是否仍要分配？");
							return JSONObject.fromObject(result).toString();
						}else if(TaskConst.CLOSE_37.equals(lastmodel.getTaskcode())||TaskConst.END_33.equals(lastmodel.getTaskcode())
								||TaskConst.VERIFYINGFAILED_30.equals(lastmodel.getTaskcode())){
							result.put("status", "closed");
							result.put("msg", "任务已经关闭或者结束，不需要继续分配！");
							return JSONObject.fromObject(result).toString();
						}
					}else{
						dispatchService.reassignment(maininstanceId, inscomcode, 1, loginUser, temp);
					}
				}else{
					if(operator==null||"".equals(operator)){
						dispatchService.getTask(subInstanceId, inscomcode, 2, loginUser, temp);
					}else if("check".equals(operator)){//验证子任务是否已经被打开
						param.put("subTaskId", subInstanceId);
						INSBWorkflowsubtrackdetail lastmodel = insbWorkflowsubtrackdetailDao.selectLastModelBySubInstanceId(param);
						if(null!=lastmodel && "Proccessing".equals(lastmodel.getOperatorstate())){
							result.put("status", "presuccess");
							result.put("msg", "任务有可能正在处理中，是否仍要分配？");
							return JSONObject.fromObject(result).toString();
						}else if(TaskConst.CLOSE_37.equals(lastmodel.getTaskcode())||TaskConst.END_33.equals(lastmodel.getTaskcode())
								||TaskConst.VERIFYINGFAILED_30.equals(lastmodel.getTaskcode())){
							result.put("status", "closed");
							result.put("msg", "任务已经关闭或者结束，不需要继续分配！");
							return JSONObject.fromObject(result).toString();
						}
					}else{
						dispatchService.reassignment(subInstanceId, inscomcode, 2, loginUser, temp);
					}
				}
				result.put("status", "success");
				result.put("msg", "任务分配成功！");
			}else{
				result.put("status", "fail");
				result.put("msg", "业管编号不存在！");
			}
			return JSONObject.fromObject(result).toString();
		} catch (RedisException re) {
			re.printStackTrace();
			result.put("status", "fail");
			result.put("msg", "任务分配失败！请重试！");
			return JSONObject.fromObject(result).toString();
		} catch (Exception re) {
			re.printStackTrace();
			result.put("status", "fail");
			result.put("msg", "任务分配失败！");
			return JSONObject.fromObject(result).toString();
		}
	}
	
	/**
	 * 停止调度调用接口
	 */
	@Override
	public String stopCarTaskDispatch(String maininstanceId, String subInstanceId, 
			String inscomcode, INSCUser loginUser) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if(maininstanceId.equals(subInstanceId)){
				INSBWorkflowmain workmain = insbWorkflowmainDao.selectByInstanceId(maininstanceId);
				if(StringUtils.isEmpty(workmain.getOperator())){
					dispatchService.pauseTask(maininstanceId, inscomcode, 1, loginUser.getUsercode());
				}else{
					result.put("status", "fail");
					result.put("msg", "此任务已经分配到人，不能暂停调度！");
					return JSONObject.fromObject(result).toString();
				}
			}else{
				INSBWorkflowsub worksub = insbWorkflowsubDao.selectByInstanceId(subInstanceId);
				if(StringUtils.isEmpty(worksub.getOperator())){
					dispatchService.pauseTask(subInstanceId, inscomcode, 2, loginUser.getUsercode());
				}else{
					result.put("status", "fail");
					result.put("msg", "此任务已经分配到人，不能暂停调度！");
					return JSONObject.fromObject(result).toString();
				}
			}
			result.put("status", "success");
			result.put("msg", "停止调度成功！");
			return JSONObject.fromObject(result).toString();
		} catch (RedisException re) {
			re.printStackTrace();
			result.put("status", "fail");
			result.put("msg", "停止调度失败！请重试！");
			return JSONObject.fromObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "fail");
			result.put("msg", "停止调度失败！");
			return JSONObject.fromObject(result).toString();
		}
	}
	
	/**
	 * 重启调度调用接口
	 */
	@Override
	public String restartCarTaskDispatch(String loginUsercode,String maininstanceId, String subInstanceId, 
			String inscomcode, INSCUser loginUser) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if(maininstanceId.equals(subInstanceId)){
					dispatchService.restartTask(maininstanceId, inscomcode, loginUsercode, 1, loginUser.getUsercode());
			}else{
					dispatchService.restartTask(subInstanceId, inscomcode, loginUsercode, 2, loginUser.getUsercode());
			}
			result.put("status", "success");
			result.put("msg", "重启调度成功！");
			return JSONObject.fromObject(result).toString();
		} catch (RedisException re) {
			re.printStackTrace();
			result.put("status", "fail");
			result.put("msg", "重启调度失败！请重试！");
			return JSONObject.fromObject(result).toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "fail");
			result.put("msg", "重启调度失败！");
			return JSONObject.fromObject(result).toString();
		}
	}
	
	/**
	 * 查询订单所有基础信息
	 */
	@Override
	public Map<String, Object> getTaskAllInfo(String taskId, String inscomcode) {
		//获取车辆车型信息
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> carInfoMapTemp = new HashMap<String, Object>();
		carInfoMapTemp.put("taskid", taskId);
		carInfoMapTemp.put("inscomcode", inscomcode);

		
		INSBProvider provider = new INSBProvider();
		provider.setPrvcode(inscomcode);
		provider = insbProviderDao.selectOne(provider);
		if(provider!=null){
			carInfoMapTemp.put("inscomname", provider.getPrvshotname());//保险公司名称
			carInfoMapTemp.put("parentinscomname", getTopProviderName(provider.getPrvcode()));//保险公司集团名称
		}
		Map<String, Object> otherInfoMaptemp = new HashMap<String, Object>();
		//取得车辆投保地区信息
		INSBQuotetotalinfo quotetotalinfo = new INSBQuotetotalinfo();
		quotetotalinfo.setTaskid(taskId);
		quotetotalinfo = insbQuotetotalinfoDao.selectOne(quotetotalinfo);
		INSBQuoteinfo quoteinfo = null;
		if(quotetotalinfo !=null){
			if(quotetotalinfo.getInsprovincename()!=null){
				carInfoMapTemp.put("insprovincename", quotetotalinfo.getInsprovincename());
			}
			if(quotetotalinfo.getInscityname()!=null){
				carInfoMapTemp.put("inscityname", quotetotalinfo.getInscityname());
			}
			if(quotetotalinfo.getId()!=null){
				 quoteinfo = new INSBQuoteinfo();
				 quoteinfo.setQuotetotalinfoid(quotetotalinfo.getId());
				 quoteinfo.setInscomcode(inscomcode);
				 quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
				 if(quoteinfo!=null){//获取投保地区信息
//					 if(quoteinfo.getInsprovincename()!=null){
//						 temp.put("subInsprovincename", quoteinfo.getInsprovincename());
//					 }
//					 if(quoteinfo.getInscityname()!=null){
//						 temp.put("subInscityname", quoteinfo.getInscityname());
//					 }
					 if(quoteinfo.getBuybusitype()!=null){//购买时供应商任务类型选项（01传统02网销03电销）
						 if("01".equals(quoteinfo.getBuybusitype())){
							 carInfoMapTemp.put("buybusitype", "传统");
						 }else if("02".equals(quoteinfo.getBuybusitype())){
							 carInfoMapTemp.put("buybusitype", "网销");
						 }else if("03".equals(quoteinfo.getBuybusitype())){
							 carInfoMapTemp.put("buybusitype", "电销");
						 }
					 }
					 if(!StringUtils.isEmpty(quoteinfo.getDeptcode())){
						 INSCDept dept = inscDeptDao.selectByComcode(quoteinfo.getDeptcode());
						 if(dept!=null){
							 otherInfoMaptemp.put("deptComName", dept.getShortname());
							 otherInfoMaptemp.put("deptComCode", dept.getComcode());
						 }
					 }
				 }
			}
		}
		//取得车辆信息
		INSBCarinfo carInfo = insbCarinfoDao.selectCarinfoByTaskId(taskId);
		carInfoMapTemp.put("carinfoId", carInfo.getId());//车辆信息id
		INSBCarmodelinfo carmodelinfo = new INSBCarmodelinfo();
		carmodelinfo.setCarinfoid(carInfo.getId());
		carmodelinfo = insbCarmodelinfoDao.selectOne(carmodelinfo);
		//通过是否生成订单判断是否在人工报价节点
		INSBWorkflowmain workflowmain = new INSBWorkflowmain();
		workflowmain.setInstanceid(taskId);
		workflowmain = insbWorkflowmainDao.selectOne(workflowmain);
//		if(workflowmain!=null && "2".equals(workflowmain.getTaskcode())){
		INSBOrder order = new INSBOrder();
		order.setTaskid(taskId);
		order.setPrvid(inscomcode);
		order = insbOrderDao.selectOne(order);
//		if(order==null){
			//不能查到订单表明是人工报价节点
			//重新取得车辆信息
			INSBCarinfohis carInfohis = new INSBCarinfohis();
			carInfohis.setTaskid(taskId);
			carInfohis.setInscomcode(inscomcode);
			carInfohis = insbCarinfohisDao.selectOne(carInfohis);
			INSBCarmodelinfohis carmodelinfohis = new INSBCarmodelinfohis();
			carmodelinfohis.setCarinfoid(carInfo.getId());
			carmodelinfohis.setInscomcode(inscomcode);
			carmodelinfohis = insbCarmodelinfohisDao.selectOne(carmodelinfohis);
			if(carmodelinfohis!=null){
				carmodelinfo = EntityTransformUtil.carmodelinfohis2Carmodelinfo(carmodelinfohis);
			}
			if(carInfohis!=null){
				carInfo = EntityTransformUtil.carinfohis2Carinfo(carInfohis);
			}
//		 }
		 //得到驾驶人信息
		 INSBCarowneinfo carownerinfo = new INSBCarowneinfo();
		 carownerinfo.setTaskid(taskId);
		 carownerinfo = insbCarowneinfoDao.selectOne(carownerinfo);
		 INSBPerson ownerinfo = insbPersonDao.selectById(carownerinfo.getPersonid());
		if(carmodelinfo!=null){
			if(carmodelinfo.getStandardfullname()!=null){
				carInfoMapTemp.put("standardfullname", carmodelinfo.getStandardfullname());//车辆信息描述
			}
			if(carmodelinfo.getStandardname()!=null){
				carInfoMapTemp.put("standardname", carmodelinfo.getStandardname());//车辆标准名称
			}
			if(carmodelinfo.getFamilyname()!=null){
				carInfoMapTemp.put("familyname", carmodelinfo.getFamilyname());//车辆系列名称
			}
			if(carmodelinfo.getBrandname()!=null){
				carInfoMapTemp.put("brandname", carmodelinfo.getBrandname());//品牌名称
			}
			if(carmodelinfo.getId()!=null){
				carInfoMapTemp.put("id", carmodelinfo.getId());//车型表id
			}
			if(carmodelinfo.getSeat()!=null){
				carInfoMapTemp.put("seat", carmodelinfo.getSeat());//承载人数
			}
			if(carmodelinfo.getUnwrtweight()!=null){
				carInfoMapTemp.put("unwrtweight", carmodelinfo.getUnwrtweight());//核定载重量
			}
			if(carmodelinfo.getDisplacement()!=null){
				carInfoMapTemp.put("displacement", carmodelinfo.getDisplacement());//发动机排量
			}
			if(carmodelinfo.getFullweight()!=null){
				carInfoMapTemp.put("fullweight", carmodelinfo.getFullweight());//整备质量
			}
			if(carmodelinfo.getListedyear()!=null){
				carInfoMapTemp.put("listedyear", carmodelinfo.getListedyear());//上市年份
			}
			if(carmodelinfo.getPrice()!=null){
				carInfoMapTemp.put("price", carmodelinfo.getTaxprice());//新车购置价
			}
			if(carmodelinfo.getTaxprice()!=null){
				carInfoMapTemp.put("addtaxprice", carmodelinfo.getPrice()+
						carmodelinfo.getTaxprice());//新车购置价含税
			}
			if(carmodelinfo.getPolicycarprice()!=null){
				carInfoMapTemp.put("policycarprice", carmodelinfo.getPolicycarprice());//投保车价
			}
			if(carmodelinfo.getIsstandardcar()!=null){
				carInfoMapTemp.put("isstandardcar", carmodelinfo.getIsstandardcar());//是否标准车型
			}
			if(carmodelinfo.getCarprice()!=null){
				carInfoMapTemp.put("carprice", carmodelinfo.getCarprice());//车价选择
			}
			if(carmodelinfo.getCardeploy()!=null){
				carInfoMapTemp.put("cardeploy", carmodelinfo.getCardeploy());//车型配置
			}
			if(carmodelinfo.getTaxprice()!=null){
				carInfoMapTemp.put("taxprice", carmodelinfo.getTaxprice());//税额
			}
			if(carmodelinfo.getAnalogyprice()!=null){
				carInfoMapTemp.put("analogyprice", carmodelinfo.getAnalogyprice());//类比价格
			}
			if(carmodelinfo.getAnalogytaxprice()!=null){
				carInfoMapTemp.put("analogytaxprice", carmodelinfo.getAnalogytaxprice());//类比税额
			}
			if(carmodelinfo.getAliasname()!=null){
				carInfoMapTemp.put("aliasname", carmodelinfo.getAliasname());//别名
			}
			if(carmodelinfo.getGearbox()!=null){
				carInfoMapTemp.put("gearbox", carmodelinfo.getGearbox());//变速箱
			}
			if(carmodelinfo.getLoads()!=null){
				carInfoMapTemp.put("loads", carmodelinfo.getLoads());//载荷
			}
		}
		if(carInfo!=null){
			if(carInfo.getOwner()!=null){
				carInfoMapTemp.put("ownerid", carInfo.getOwner());//车主id
			}
			if(carInfo.getEngineno()!=null){
				carInfoMapTemp.put("engineno", carInfo.getEngineno());//发动机号
			}
			if(carInfo.getCarlicenseno()!=null){
				carInfoMapTemp.put("carlicenseno", carInfo.getCarlicenseno());//车牌号
			}
			if(carInfo.getVincode()!=null){
				carInfoMapTemp.put("vincode", carInfo.getVincode());//车辆识别代码
			}
			if(carInfo.getOwnername()!=null){
				carInfoMapTemp.put("ownername", carInfo.getOwnername());//车主姓名
			}
			if(carInfo.getRegistdate()!=null){
				carInfoMapTemp.put("registdate", sdf.format(carInfo.getRegistdate()).toString());//车辆初始登录日期
			}
			if(carInfo.getMileage()!=null && !("".equals(carInfo.getMileage()))){
				carInfoMapTemp.put("mileage", carInfo.getMileage());//平均行驶里程code
				carInfoMapTemp.put("mileagevalue", inscCodeService.transferValueToName("avgmileage","avgmileage",carInfo.getMileage()));//平均行驶里程
			}
			if(carInfo.getDrivingarea()!=null && !("".equals(carInfo.getDrivingarea()))){
				carInfoMapTemp.put("drivingarea", carInfo.getDrivingarea());//车辆行驶区域code
				carInfoMapTemp.put("drivingareavalue", inscCodeService.transferValueToName("CarDrivingArea","CarDrivingArea",carInfo.getDrivingarea()));//车辆行驶区域
			}
			if(carInfo.getId()!=null){
				carInfoMapTemp.put("id", carInfo.getId());//车辆信息id
			}
			if(carInfo.getIsTransfercar()!=null){
				carInfoMapTemp.put("isTransfercar", carInfo.getIsTransfercar());//是否是过户车
			}
			//以下是车辆信息修改弹出框需要增加的取值字段
			if(carInfo.getCarproperty()!=null && !("".equals(carInfo.getCarproperty()))){
				carInfoMapTemp.put("carproperty", carInfo.getCarproperty());//车辆使用性质code
				carInfoMapTemp.put("carpropertyvalue", inscCodeService.transferValueToName("UseProps","UseProps",carInfo.getCarproperty()));//车辆使用性质
			}
			if(carInfo.getProperty()!=null && !("".equals(carInfo.getProperty()))){
				carInfoMapTemp.put("property", carInfo.getProperty());//车辆所属性质code
				carInfoMapTemp.put("propertyvalue", inscCodeService.transferValueToName("UserType","UserType",carInfo.getProperty()));//车辆所属性质
			}
			if(carInfo.getTransferdate()!=null){
				carInfoMapTemp.put("transferdate", sdf.format(carInfo.getTransferdate()));//过户日期
			}
		}
		if(ownerinfo!=null){
			carInfoMapTemp.put("ownerid", ownerinfo.getId());//车主id
			if(ownerinfo.getName()!=null){
				carInfoMapTemp.put("ownername", ownerinfo.getName());//车主姓名
			}
		}
		//获取关系人信息
		INSBPerson applicant = new INSBPerson();
		INSBPerson insured = new INSBPerson();
		INSBPerson personForRight = new INSBPerson();
		INSBPerson linkPerson = new INSBPerson();
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
		//判断是否为人工报价节点
//		if(workflowmain!=null && "2".equals(workflowmain.getTaskcode())){
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
		Map<String, Object> relationPersonInfoMapTemp = new HashMap<String, Object>();
		relationPersonInfoMapTemp.put("taskid", taskId);
		relationPersonInfoMapTemp.put("inscomcode", inscomcode);
		if(applicant!=null){
			if(applicant.getName()!=null){
				relationPersonInfoMapTemp.put("applicantname", applicant.getName());//投保人姓名
			}
		}
		if(insured!=null){
			if(insured.getName()!=null){
				relationPersonInfoMapTemp.put("insuredname", insured.getName());//被保人姓名
			}
		}
		if(personForRight!=null){
			if(personForRight.getName()!=null){
				relationPersonInfoMapTemp.put("personForRightname", personForRight.getName());//权益索赔人姓名
			}
		}
		if(linkPerson!=null){
			if(linkPerson.getName()!=null){
				relationPersonInfoMapTemp.put("linkPersonname", linkPerson.getName());//联系人姓名
			}
		}
		//查询其他信息
		//查询保险起止日期
		otherInfoMaptemp.put("taskid", taskId);
		otherInfoMaptemp.put("inscomcode", inscomcode);
		INSBPolicyitem policyitem = new INSBPolicyitem();
		policyitem.setTaskid(taskId);
		policyitem.setInscomcode(inscomcode);
		List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectList(policyitem);
		//查询上年商业险保险公司
		INSBLastyearinsureinfo insbLastRearInsureInfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskId);
		if(insbLastRearInsureInfo!=null){
			otherInfoMaptemp.put("preinscode", insbLastRearInsureInfo.getSupplierid());//上年投保公司code
			otherInfoMaptemp.put("preinsShortname", insbLastRearInsureInfo.getSuppliername());//上年投保公司名称
		}
		if(carInfo!=null){
			otherInfoMaptemp.put("carinfoId", carInfo.getId());
//			if(carInfo.getPreinscode()!=null && !("".equals(carInfo.getPreinscode()))){
//				INSBProvider preProvider = new INSBProvider();
//				preProvider.setPrvcode(carInfo.getPreinscode());
//				preProvider = insbProviderDao.selectOne(preProvider);
//				otherInfoMaptemp.put("preinscode", carInfo.getPreinscode());//上年投保公司code
//				if(preProvider!=null)
//				otherInfoMaptemp.put("preinsShortname", preProvider.getPrvshotname());//上年投保公司名称
//			}
			if(carInfo.getSpecifydriver()!=null && !("".equals(carInfo.getSpecifydriver()))){
				otherInfoMaptemp.put("Specifydriver", carInfo.getSpecifydriver());//是否指定驾驶人
			}else{
				otherInfoMaptemp.put("Specifydriver", "否");
			}
			if(carInfo.getCloudinscompany()!=null){
				otherInfoMaptemp.put("cloudinscompany", carInfo.getCloudinscompany());//云查询保险公司
			}
		}
		boolean hasbusi = false;
		boolean hasstr = false;
		if(policyitemList!=null && policyitemList.size()>0){
			for (int i = 0; i < policyitemList.size(); i++) {
				if("0".equals(policyitemList.get(i).getRisktype())){//商业险起止日期
					if(policyitemList.get(i).getStartdate()!=null){
						otherInfoMaptemp.put("businessstartdate", sdf.format(policyitemList.get(i).getStartdate()).toString());
					}
					if(policyitemList.get(i).getEnddate()!=null){
						otherInfoMaptemp.put("businessenddate", sdf.format(policyitemList.get(i).getEnddate()).toString());
					}
					if(policyitemList.get(i).getProposalformno()!=null){//商业险投保单号
						otherInfoMaptemp.put("busProposalformno", policyitemList.get(i).getProposalformno());
					}
					if(policyitemList.get(i).getCheckcode()!=null){//校验码
						otherInfoMaptemp.put("checkcode", policyitemList.get(i).getCheckcode());
					}
					hasbusi = true;
				}else if("1".equals(policyitemList.get(i).getRisktype())){//交强险起止日期
					if(policyitemList.get(i).getStartdate()!=null){
						otherInfoMaptemp.put("compulsorystartdate", sdf.format(policyitemList.get(i).getStartdate()).toString());
					}
					if(policyitemList.get(i).getEnddate()!=null){
						otherInfoMaptemp.put("compulsoryenddate", sdf.format(policyitemList.get(i).getEnddate()).toString());
					}
					if(policyitemList.get(i).getProposalformno()!=null){//交强险投保单号
						otherInfoMaptemp.put("strProposalformno", policyitemList.get(i).getProposalformno());
					}
					if(policyitemList.get(i).getCheckcode()!=null){//校验码
						otherInfoMaptemp.put("checkcode", policyitemList.get(i).getCheckcode());
					}
					hasstr = true;
				}
			}
		}
		otherInfoMaptemp.put("hasbusi", hasbusi);
		otherInfoMaptemp.put("hasstr", hasstr);
//		INSBOrder orderags =new INSBOrder();
//		orderags.setTaskid(taskId);
//		orderags.setPrvid(inscomcode);
//		INSBOrder order = insbOrderDao.selectOne(orderags);
		if(order!=null){
			INSBOrderpayment orderpayment = new INSBOrderpayment();
			orderpayment.setOrderid(order.getId());
			orderpayment = insbOrderpaymentDao.selectOne(orderpayment);
			if(orderpayment!=null){
				if(orderpayment.getInsurecono()!=null){
					otherInfoMaptemp.put("insurecono", orderpayment.getInsurecono());//支付流水号
				}
			}
		}	
		//获取redis里平台信息
		LastYearPolicyInfoBean lastYearPolicyInfoBean = redisClient.get(Constants.TASK, taskId, LastYearPolicyInfoBean.class);
		if(lastYearPolicyInfoBean!=null){
			if(lastYearPolicyInfoBean.getLastYearClaimBean()!=null){
				otherInfoMaptemp.put("syclaimtimes", lastYearPolicyInfoBean.getLastYearClaimBean().getBwcommercialclaimtimes());//商业险出险次数
				otherInfoMaptemp.put("jqclaimtimes", lastYearPolicyInfoBean.getLastYearClaimBean().getBwcompulsoryclaimtimes());//交强险出险次数
			}else {
				otherInfoMaptemp.put("syclaimtimes", 0);
				otherInfoMaptemp.put("jqclaimtimes", 0);	
			}
		}else{
			INSBLastyearinsureinfo insbLastyearinsureinfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskId);
			if( insbLastyearinsureinfo != null ) {
				try {
					if (StringUtils.isNotEmpty(insbLastyearinsureinfo.getBwcommercialclaimtimes().toString())) {
						otherInfoMaptemp.put("syclaimtimes", insbLastyearinsureinfo.getBwcommercialclaimtimes());//商业险出险次数
					}
				} catch (NullPointerException e) {
					otherInfoMaptemp.put("syclaimtimes",0);
				}
				try {
					if (StringUtils.isNotEmpty(insbLastyearinsureinfo.getBwcompulsoryclaimtimes().toString())) {
						otherInfoMaptemp.put("jqclaimtimes", insbLastyearinsureinfo.getBwcompulsoryclaimtimes());//交强险出险次数
					}
				} catch (NullPointerException e) {
					otherInfoMaptemp.put("jqclaimtimes", 0);
				}
			}
		}
		//获取平台起保起期 /止期
		INSBLastyearinsureinfo lastInsInfo = insbRulequerycarinfoDao.queryLastYearClainInfo(taskId);
		if(lastInsInfo!=null){
			if(StringUtils.isEmpty(lastInsInfo.getSystartdate())){
				otherInfoMaptemp.put("systartdate", "");
			}else{
				if(lastInsInfo.getSystartdate().length() > 10){
					otherInfoMaptemp.put("systartdate", lastInsInfo.getSystartdate().substring(0, 10));
				}else{
					otherInfoMaptemp.put("systartdate", lastInsInfo.getSystartdate());
				}
			}
			if(StringUtils.isEmpty(lastInsInfo.getSyenddate())){
				otherInfoMaptemp.put("syenddate", "");
			}else{
				if(lastInsInfo.getSyenddate().length() > 10){
					otherInfoMaptemp.put("syenddate", lastInsInfo.getSyenddate().substring(0, 10));
				}else{
					otherInfoMaptemp.put("syenddate", lastInsInfo.getSyenddate());
				}
			}
			if(StringUtils.isEmpty(lastInsInfo.getJqstartdate())){
				otherInfoMaptemp.put("jqstartdate", "");
			}else{
				if(lastInsInfo.getJqstartdate().length() > 10){
					otherInfoMaptemp.put("jqstartdate", lastInsInfo.getJqstartdate().substring(0, 10));
				}else{
					otherInfoMaptemp.put("jqstartdate", lastInsInfo.getJqstartdate());
				}
			}
			if(StringUtils.isEmpty(lastInsInfo.getJqenddate())){
				otherInfoMaptemp.put("jqenddate", "");
			}else{
				if(lastInsInfo.getJqenddate().length() > 10){
					otherInfoMaptemp.put("jqenddate", lastInsInfo.getJqenddate().substring(0, 10));
				}else{
					otherInfoMaptemp.put("jqenddate", lastInsInfo.getJqenddate());
				}
			}
		}
		result.put("carInfo", carInfoMapTemp);
		result.put("relationPersonInfo", relationPersonInfoMapTemp);
		result.put("otherInfo", otherInfoMaptemp);
		return result;
	}
}