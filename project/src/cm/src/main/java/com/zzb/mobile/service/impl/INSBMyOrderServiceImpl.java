package com.zzb.mobile.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.JsonUtil;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.*;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.util.MappingType;
import com.zzb.mobile.util.PayConfigMappingMgr;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.common.ModelUtil;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.INSBMyOrderService;

/*
 *我的订单接口服务的实现 
 */


@Service
public class INSBMyOrderServiceImpl extends BaseServiceImpl<INSBOrder> implements INSBMyOrderService {
	@Resource
	private INSBWorkflowmainDao insbWorkflowmainDao;
	@Resource 
	private INSBQuoteinfoDao insbquoteinfoDao;
	@Resource
	private INSBCarinfoDao insbcarinfoDao;
	@Resource 
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource 
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSCCodeDao inscCodeDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSCDeptDao inscDeptDao;
	@Resource
	private INSBOrderpaymentDao insbOrderpaymentDao;
	@Resource
	private INSBWorkflowsubDao insbWorkflowsubDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private INSBWorkflowsubtrackDao insbWorkflowsubtrackDao;
    @Resource
    private INSBPersonDao insbPersonDao;
    @Resource
    private INSBInsuredhisDao insuredhisDao;
	@Resource
	private AppInsuredQuoteDao appInsuredQuoteDao;
	@Resource
	private INSBUsercommentService insbUsercommentService;
	@Resource
	private INSBPaychannelDao insbPaychannelDao;
	@Override
	public String showMyOrder(String agentnum, String carlicenseno, String insuredname, 
			String orderstatus, int limit, long offset,String taskcreatetimeup,String taskcreatetimedown
			,String purchaserid,String purchaserchannel) {
		CommonModel resultAll = new CommonModel();
		try{
			Map<String,Object> queryParams = new HashMap<String,Object>();
			Map<String,Object> bodyResult = new HashMap<String,Object>();
			Map<String,Object> body = new HashMap<String,Object>();
			//组织查询条件
			queryParams.put("agentnum", agentnum);          //代理人工号
			queryParams.put("carlicenseno", carlicenseno);  //车牌号
			queryParams.put("insuredname", insuredname);    //被保人姓名
			queryParams.put("limit", limit);                //每页显示的数目
			queryParams.put("offset", offset);              //偏移量
			queryParams.put("taskcreatetimeup", taskcreatetimeup);     //时间前.contains(":")? taskcreatetimeup:taskcreatetimeup+" 00:00:00"
			queryParams.put("taskcreatetimedown", taskcreatetimedown);  //时间后

			//http://pm.baoxian.in/zentao/task-view-1612.html
			queryParams.put("purchaserid", purchaserid);  //渠道用户id
			queryParams.put("purchaserchannel", purchaserchannel);  //渠道id

			INSBAgent agent = new INSBAgent();
			String shareid = null;
			if (StringUtil.isEmpty(agentnum)) {
				resultAll.setMessage("我的订单查询失败,非法代理人");
				resultAll.setStatus(CommonModel.STATUS_FAIL);
				return JSONObject.fromObject(resultAll).toString();
			}
			agent.setJobnum(agentnum);
			agent = insbAgentDao.selectOne(agent);
			if(agent!=null){
				shareid = agent.getId();
				queryParams.put("shareagentnum", shareid);
			}

			List<Map<String,Object>> resultlist = null;
			long totalnum = 0;
			
			if("2".equals(orderstatus)){
				orderstatus = "20";
				queryParams.put("orderstatus",orderstatus);

				Map<String, Object> totalNumParams = new HashMap<String, Object>();
				// 组织查询条件
				totalNumParams.put("agentnum", agentnum); // 代理人工号
				totalNumParams.put("shareagentnum", shareid);
				totalNumParams.put("purchaserid", purchaserid);  //渠道用户id
				totalNumParams.put("purchaserchannel", purchaserchannel);  //渠道id

				List<Map<String, Object>> totalList = appInsuredQuoteDao.getTobePaymentOrderNum(totalNumParams);
				queryParams.put("limit", totalList.size());
				queryParams.put("offset", 0);              //偏移量
				resultlist =  insbWorkflowmainDao.getMyOrderList2Pay(queryParams);
				for(int j = resultlist.size() - 1;j >= 0;j--){
					Map<String, Object> result = resultlist.get(j);
					String processInstanceId = (String) result.get("processInstanceId");//主流程号
					List<Map<String,Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
					if(subQuoteInfoList==null||(subQuoteInfoList.size()==1&&StringUtil.isEmpty(subQuoteInfoList.get(0))))
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					for (int i = subQuoteInfoList.size()-1;i>=0;i--) {
						Map<String, Object> map =subQuoteInfoList.get(i);
						if(!"20".equals(map.get("taskcode"))){
							subQuoteInfoList.remove(i);
						}else{
							//去掉过期的单子 2016-03-30
							if(removeOverTimeData(result.get("processInstanceId"),map.get("inscomcode"))){
								subQuoteInfoList.remove(i);
							}
						}

						String inscomcode = (String) map.get("inscomcode");
						if (StringUtil.isNotEmpty(inscomcode)) {
							INSBProvider provider = new INSBProvider();
							provider.setPrvcode(inscomcode.substring(0, 4));
							provider = insbProviderDao.selectOne(provider);
							map.put("inscomlogo",provider == null ? "" : provider.getLogo());//保险公司logo
							List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment2(processInstanceId, (String) map.get("inscomcode"), null);
							if(usercomment!=null&&usercomment.size()>0){
								map.put("quotenoti", usercomment.get(0).getCommentcontent());//报价的提示语
							}else{
								map.put("quotenoti", "");//报价的提示语
							}

							//task1361 待支付订单表返回支付方式给前端
							INSBOrder insbOrder = new INSBOrder();
							insbOrder.setTaskid(processInstanceId);
							insbOrder.setPrvid(inscomcode);
							insbOrder = insbOrderDao.selectOne(insbOrder);			//获取订单表
							if(insbOrder!=null && insbOrder.getPaymentmethod()!=null){
								INSBPaychannel paychannel = insbPaychannelDao.selectById(insbOrder.getPaymentmethod());//根据订单表ID、获取paychannel的支付方式
								if (paychannel != null) {
									/*map.put("Paymentmethod", paychannel.getPaychannelname());
									map.put("Paytype", paychannel.getPaytype());*/
									map.put("typecode", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, paychannel.getPaytype()));
									map.put("channelId", paychannel.getId());
								} else{
									INSBWorkflowsub flowsub = this.insbWorkflowsubDao.selectByInstanceId((String)map.get("workflowinstanceid"));
									if (!"admin".equals(flowsub.getOperator())) {
										map.put("typecode", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, "03"));
										map.put("channelId", "4");
									} else {
										map.put("Paymentmethod", "");
										map.put("Paytype", "");
									}
								}

								if("20".equals(map.get("taskcode"))){//当前状态为20 支付的时候判断 是否 为柜台支付  柜台支付返回 柜台支付 状态
									if(!"admin".equals(map.get("operator")) && insbOrder.getPaymentmethod() != null && "4".equalsIgnoreCase(insbOrder.getPaymentmethod())){
										map.put("taskcode", "201");//柜台支付状态
									}
								}
							}
						}

					}
					result.put("quoteInfoList", subQuoteInfoList);
					if(subQuoteInfoList.size()==0){
						resultlist.remove(result);
					}
				}
				if (!resultlist.isEmpty()) {
					int num = resultlist.size();
					resultlist = resultlist.subList((int)offset, (int)(offset+limit>num?num:offset+limit));
//					resultlist = resultlist.subList(0, num > limit ? limit : num);
				}

				for (int j = totalList.size() - 1; j >= 0; j--) {
					Map<String, Object> result = totalList.get(j);
					// 去掉过期的单子 2016-03-30
					if (this.removeOverTimeData(result.get("taskid"),result.get("inscomcode"))) {
						totalList.remove(result);
					}
				}
				totalnum =totalList.size();

				//totalnum = insbWorkflowmainDao.myOrderCount2Pay(queryParams);
			}else if("1".equals(orderstatus)){
				orderstatus =null;
				queryParams.put("orderstatus",orderstatus);
				queryParams.replace("limit", null);
				queryParams.replace("offset",null);
				resultlist =  insbWorkflowmainDao.getMyOrderListNew(queryParams);
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				for(Map<String,Object> result : resultlist){
					if("closed".equalsIgnoreCase(result.get("taskstate").toString()))continue;
					String processInstanceId = (String) result.get("processInstanceId");
					List<Map<String,Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
					if(subQuoteInfoList==null||(subQuoteInfoList.size()==1&&StringUtil.isEmpty(subQuoteInfoList.get(0)))){
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					}
					for (int i=subQuoteInfoList.size()-1;i>=0;i--) {
						Map<String, Object> map =subQuoteInfoList.get(i);
                        boolean needtoremove = false;

						if(StringUtil.isEmpty(map.get("inscomcode"))
								||StringUtil.isEmpty(map.get("prvshotname"))
								||StringUtil.isEmpty(map.get("taskcode"))) {
                            needtoremove = true;
                        }

						//去掉过期的单子 2016-03-30
						if(removeOverTimeData2(result.get("processInstanceId"),map.get("inscomcode"))){
							needtoremove = true;
						}

                        if (needtoremove) {
                            subQuoteInfoList.remove(i);
                        }
					}
					result.put("quoteInfoList", subQuoteInfoList);
					int flag20;
					boolean isAllClosed = true;//子流程全部关闭
					if(subQuoteInfoList!=null && subQuoteInfoList.size()>0){
						List<String> lastNode = Arrays.asList(new String[]{"25","26","27","28","23","24","21","20"});
						flag20=0;
						for (Map<String, Object> map : subQuoteInfoList) {
							if(map!=null){
								if(lastNode.contains((String) map.get("taskcode"))){
									flag20=1;
								}
								if (!"37".equalsIgnoreCase((String) map.get("taskcode")) && !"30".equalsIgnoreCase((String) map.get("taskcode"))) {
									isAllClosed = false;
								}
							}
						}
						if(flag20!=1 && !isAllClosed){
							list.add(result);
						}
					}

				}

				int endIndex = list.size() > ((int)offset+limit)? ((int)offset+limit): list.size();
				List<Map<String,Object>> tempList = new ArrayList<Map<String,Object>>();
				for(int i=(int)offset;i<endIndex;i++){
					tempList.add(list.get(i));
				}

				resultlist = tempList;

                for(Map<String, Object> result : resultlist) {
                    String processInstanceId = (String) result.get("processInstanceId");
                    List<INSBInsuredhis> insuredhisList = insuredhisDao.selectByTaskid(processInstanceId);

                    if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
                        INSBPerson insbPerson = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
                        if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getName())) {
                            result.put("insuredname", insbPerson.getName());
                        }
                    }
                }

				totalnum = list.size();//待投保总共条数
			}else{
				orderstatus = null;
				queryParams.put("orderstatus",orderstatus);
				resultlist =  insbWorkflowmainDao.getMyOrderList(queryParams);
				for(Map<String,Object> result : resultlist){
					//if("closed".equalsIgnoreCase(result.get("taskstate").toString()))continue;
					String processInstanceId = (String) result.get("processInstanceId");
					List<Map<String,Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
					if(subQuoteInfoList==null||(subQuoteInfoList.size()==1&&StringUtil.isEmpty(subQuoteInfoList.get(0))))
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					for (int i=subQuoteInfoList.size()-1;i>0;i--) {
						Map<String, Object> map =subQuoteInfoList.get(i);
						if(StringUtil.isEmpty(map.get("inscomcode"))
								||StringUtil.isEmpty(map.get("prvshotname"))
								||StringUtil.isEmpty(map.get("taskcode")))
							subQuoteInfoList.remove(i);
					}
					if(subQuoteInfoList!=null&&subQuoteInfoList.size()!=0&&subQuoteInfoList.get(0)!=null)
						result.put("quoteInfoList", subQuoteInfoList);

                    List<INSBInsuredhis> insuredhisList = insuredhisDao.selectByTaskid(processInstanceId);
                    if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
                        INSBPerson insbPerson = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
                        if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getName())) {
                            result.put("insuredname", insbPerson.getName());
                        }
                    }
				}
				//totalnum = resultlist.size();
				totalnum = insbWorkflowmainDao.myOrderCount(queryParams);
			}

			Map<String,Object> map = new HashMap<String, Object>();
			map.put("resultlist", resultlist);
			body.put("orderinfo", map);               //查询成功，完成结果的返回
			//if(resultlist!=null){
				body.put("totalnum", totalnum);          //总共的数据条数  resultlist.size()先前的
			//}
			bodyResult.put("myorderinfo", body);
			resultAll.setBody(bodyResult);
			resultAll.setMessage("我的订单查询成功");
			resultAll.setStatus("success");

			return JSONObject.fromObject(resultAll).toString();
		}catch(Exception e){                         //查询失败，出现异常
			e.printStackTrace();
			resultAll.setMessage("我的订单查询失败");
			resultAll.setStatus("fail");
			return JSONObject.fromObject(resultAll).toString();
		}
	}

	//渠道-订单查询
	@Override
	public CommonModel showMyOrderForChn(String agentnum, String carlicenseno, String insuredname, 
			String orderstatus, int limit, long offset, String taskcreatetimeup, String taskcreatetimedown, String chnId, String chnUserId, String taskId) {
		CommonModel resultAll = new CommonModel();
		try{
			Map<String, Object> queryParams = new HashMap<String, Object>();
			Map<String, Object> bodyResult = new HashMap<String, Object>();
			
			//组织查询条件
			//queryParams.put("agentnum", agentnum);          //代理人工号
			queryParams.put("carlicenseno", carlicenseno);  //车牌号
			queryParams.put("insuredname", insuredname);    //被保人姓名
			queryParams.put("limit", limit);                //每页显示的数目
			queryParams.put("offset", offset);              //偏移量
			queryParams.put("taskcreatetimeup", taskcreatetimeup);      //时间前.contains(":")? taskcreatetimeup:taskcreatetimeup+" 00:00:00"
			queryParams.put("taskcreatetimedown", taskcreatetimedown);  //时间后
			queryParams.put("chnId", chnId);
			queryParams.put("chnUserId", chnUserId);
			queryParams.put("taskId", taskId);
			//INSBAgent agent = new INSBAgent();
			//agent.setJobnum(agentnum);
			//agent = insbAgentDao.selectOne(agent);
			//if (agent!=null) {
			//	String shareid = agent.getId();
			//	queryParams.put("shareagentnum", shareid);
			//}

			List<Map<String, Object>> resultlist = null;
			long totalnum = 0;
			
			if ("2".equals(orderstatus)) { //待支付订单
				orderstatus = "20";
				queryParams.put("orderstatus", orderstatus);
				resultlist =  insbWorkflowmainDao.getMyOrderList2PayForChn(queryParams);
				for(int j = resultlist.size() - 1; j >= 0; j--){
					Map<String, Object> result = resultlist.get(j);
					String processInstanceId = (String) result.get("processInstanceId");
					List<Map<String, Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskIdForChn(processInstanceId);
					if (subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0))))
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
						Map<String, Object> map =subQuoteInfoList.get(i);
						if (!"20".equals(map.get("taskcode"))) {
							subQuoteInfoList.remove(i);
						} else {
							//去掉过期的单子 2016-03-30
							if (removeOverTimeData(result.get("processInstanceId"), map.get("inscomcode"))) {
								subQuoteInfoList.remove(i);
							}
							if ("20".equals(map.get("taskcode"))) { //当前状态为20 支付的时候判断 是否 为柜台支付  柜台支付返回 柜台支付 状态
								if (!"admin".equals(map.get("operator"))) {
									map.put("taskcode", "201"); //柜台支付状态
								}
							}
						}
					}
					result.put("quoteInfoList", subQuoteInfoList);
					if (subQuoteInfoList.size() == 0) {
						resultlist.remove(result);
					} else {
                        List<INSBInsuredhis> insuredhisList = insuredhisDao.selectByTaskid(processInstanceId);
                        if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
                            INSBPerson insbPerson = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
                            if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getName())) {
                                result.put("insuredname", insbPerson.getName());
                            }
                        }
                    }
				}
				totalnum = resultlist.size();
				//totalnum = insbWorkflowmainDao.myOrderCount2Pay(queryParams);
			} else if("1".equals(orderstatus)) { //待投保订单
				orderstatus = null;
				queryParams.put("orderstatus", orderstatus);
				queryParams.replace("limit", null);
				queryParams.replace("offset", null);
				resultlist = insbWorkflowmainDao.getMyOrderListNewForChn(queryParams);
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				for(Map<String, Object> result : resultlist){
					if ("closed".equalsIgnoreCase(result.get("taskstate").toString())) continue;
					String processInstanceId = (String)result.get("processInstanceId");
					List<Map<String,Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskIdForChn(processInstanceId);
					if (subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0)))) {
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					}
					for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
						Map<String, Object> map = subQuoteInfoList.get(i);
						if(StringUtil.isEmpty(map.get("inscomcode"))
								||StringUtil.isEmpty(map.get("prvshotname"))
								||StringUtil.isEmpty(map.get("taskcode")))
							subQuoteInfoList.remove(i);
					}
					result.put("quoteInfoList", subQuoteInfoList);
					int flag20;
					if (subQuoteInfoList != null && subQuoteInfoList.size() > 0) {
						List<String> lastNode = Arrays.asList(new String[]{"25","26","27","28","23","24","21","20"});
						flag20 = 0;
						for (Map<String, Object> map : subQuoteInfoList) {
							if (map != null) {
								if (lastNode.contains((String)map.get("taskcode"))) {
									flag20 = 1;
								}
							}
							
						}
						if(flag20 != 1 ){
							list.add(result);
						}
					}
				}

				int endIndex = list.size() > ((int)offset + limit) ? ((int)offset + limit) : list.size();
				List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
				for(int i = (int)offset; i < endIndex; i++){
					tempList.add(list.get(i));
				}
				resultlist = tempList;

                for(Map<String, Object> result : resultlist) {
                    String processInstanceId = (String)result.get("processInstanceId");
                    List<INSBInsuredhis> insuredhisList = insuredhisDao.selectByTaskid(processInstanceId);

                    if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
                        INSBPerson insbPerson = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
                        if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getName())) {
                            result.put("insuredname", insbPerson.getName());
                        }
                    }
                }
				totalnum = list.size(); //待投保总共条数
				
			} else { //全部订单
				orderstatus = null;
				queryParams.put("orderstatus", orderstatus);
				resultlist =  insbWorkflowmainDao.getMyOrderListForChn(queryParams);
				for(Map<String, Object> result : resultlist){
					//if ("closed".equalsIgnoreCase(result.get("taskstate").toString())) continue;
					String processInstanceId = (String)result.get("processInstanceId");
					List<Map<String, Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskIdForChn(processInstanceId);
					LogUtil.info("showMyOrderForChn-全部订单:" + taskId + " " + JsonUtils.serialize(subQuoteInfoList));
					if(subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0))))
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
						Map<String, Object> map =subQuoteInfoList.get(i);
						if(StringUtil.isEmpty(map.get("inscomcode"))
								||StringUtil.isEmpty(map.get("prvshotname"))
								||StringUtil.isEmpty(map.get("taskcode")))
							subQuoteInfoList.remove(i);
					}
					if(subQuoteInfoList != null && subQuoteInfoList.size() != 0 && subQuoteInfoList.get(0) != null)
						result.put("quoteInfoList", subQuoteInfoList);

                    List<INSBInsuredhis> insuredhisList = insuredhisDao.selectByTaskid(processInstanceId);
                    if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
                        INSBPerson insbPerson = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
                        if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getName())) {
                            result.put("insuredname", insbPerson.getName());
                        }
                    }
				}
				//totalnum = resultlist.size();
				totalnum = insbWorkflowmainDao.myOrderCountForChn(queryParams);
			}
			
			bodyResult.put("totalnum", totalnum);       //总共的数据条数  resultlist.size()先前的
			bodyResult.put("resultlist", resultlist);
			
			resultAll.setBody(bodyResult);
			resultAll.setMessage("我的订单查询成功");
			resultAll.setStatus("success");

		}catch(Exception e){                         
			e.printStackTrace();
			resultAll.setMessage("我的订单查询失败");
			resultAll.setStatus("fail");
		}
		return resultAll;
	}

	//渠道-订单查询
	@Override
	public CommonModel showMyOrderForChnNew(String agentnum, String carlicenseno, String insuredname,
										 String orderstatus, int limit, long offset, String taskcreatetimeup, String taskcreatetimedown, String chnId, String chnUserId, String taskId) {
		CommonModel resultAll = new CommonModel();
		try{
			Map<String, Object> queryParams = new HashMap<String, Object>();
			Map<String, Object> bodyResult = new HashMap<String, Object>();
			Map<String,Object> body = new HashMap<String,Object>();
			//组织查询条件
			//queryParams.put("agentnum", agentnum);          //代理人工号
			queryParams.put("carlicenseno", carlicenseno);  //车牌号
			queryParams.put("insuredname", insuredname);    //被保人姓名
			queryParams.put("limit", limit);                //每页显示的数目
			queryParams.put("offset", offset);              //偏移量
			queryParams.put("taskcreatetimeup", taskcreatetimeup);      //时间前.contains(":")? taskcreatetimeup:taskcreatetimeup+" 00:00:00"
			queryParams.put("taskcreatetimedown", taskcreatetimedown);  //时间后
			queryParams.put("chnId", chnId);
			queryParams.put("chnUserId", chnUserId);
			queryParams.put("taskId", taskId);
			//INSBAgent agent = new INSBAgent();
			//agent.setJobnum(agentnum);
			//agent = insbAgentDao.selectOne(agent);
			//if (agent!=null) {
			//	String shareid = agent.getId();
			//	queryParams.put("shareagentnum", shareid);
			//}

			List<Map<String, Object>> resultlist = null;
			long totalnum = 0;

			if ("2".equals(orderstatus)) { //待支付订单
				orderstatus = "20";
				queryParams.put("orderstatus", orderstatus);
				resultlist =  insbWorkflowmainDao.getMyOrderList2PayForChn(queryParams);
				for(int j = resultlist.size() - 1; j >= 0; j--){
					Map<String, Object> result = resultlist.get(j);
					String processInstanceId = (String) result.get("processInstanceId");
					List<Map<String, Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskIdForChn(processInstanceId);
					if (subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0))))
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
						Map<String, Object> map =subQuoteInfoList.get(i);
						if (!"20".equals(map.get("taskcode"))) {
							subQuoteInfoList.remove(i);
						} else {
							//去掉过期的单子 2016-03-30
							if (removeOverTimeData(result.get("processInstanceId"), map.get("inscomcode"))) {
								subQuoteInfoList.remove(i);
							}
							if ("20".equals(map.get("taskcode"))) { //当前状态为20 支付的时候判断 是否 为柜台支付  柜台支付返回 柜台支付 状态
								if (!"admin".equals(map.get("operator"))) {
									map.put("taskcode", "201"); //柜台支付状态
								}
							}
						}
					}
					result.put("quoteInfoList", subQuoteInfoList);
					if (subQuoteInfoList.size() == 0) {
						resultlist.remove(result);
					} else {
						List<INSBInsuredhis> insuredhisList = insuredhisDao.selectByTaskid(processInstanceId);
						if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
							INSBPerson insbPerson = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
							if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getName())) {
								result.put("insuredname", insbPerson.getName());
							}
						}
					}
				}
				totalnum = resultlist.size();
				//totalnum = insbWorkflowmainDao.myOrderCount2Pay(queryParams);
			} else if("1".equals(orderstatus)) { //待投保订单
				orderstatus = null;
				queryParams.put("orderstatus", orderstatus);
				queryParams.replace("limit", null);
				queryParams.replace("offset", null);
				resultlist = insbWorkflowmainDao.getMyOrderListNewForChn(queryParams);
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				for(Map<String, Object> result : resultlist){
					if ("closed".equalsIgnoreCase(result.get("taskstate").toString())) continue;
					String processInstanceId = (String)result.get("processInstanceId");
					List<Map<String,Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskIdForChn(processInstanceId);
					if (subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0)))) {
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					}
					for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
						Map<String, Object> map = subQuoteInfoList.get(i);
						if(StringUtil.isEmpty(map.get("inscomcode"))
								||StringUtil.isEmpty(map.get("prvshotname"))
								||StringUtil.isEmpty(map.get("taskcode")))
							subQuoteInfoList.remove(i);
					}
					result.put("quoteInfoList", subQuoteInfoList);
					int flag20;
					if (subQuoteInfoList != null && subQuoteInfoList.size() > 0) {
						List<String> lastNode = Arrays.asList(new String[]{"25","26","27","28","23","24","21","20"});
						flag20 = 0;
						for (Map<String, Object> map : subQuoteInfoList) {
							if (map != null) {
								if (lastNode.contains((String)map.get("taskcode"))) {
									flag20 = 1;
								}
							}

						}
						if(flag20 != 1 ){
							list.add(result);
						}
					}
				}

				int endIndex = list.size() > ((int)offset + limit) ? ((int)offset + limit) : list.size();
				List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
				for(int i = (int)offset; i < endIndex; i++){
					tempList.add(list.get(i));
				}
				resultlist = tempList;

				for(Map<String, Object> result : resultlist) {
					String processInstanceId = (String)result.get("processInstanceId");
					List<INSBInsuredhis> insuredhisList = insuredhisDao.selectByTaskid(processInstanceId);

					if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
						INSBPerson insbPerson = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
						if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getName())) {
							result.put("insuredname", insbPerson.getName());
						}
					}
				}
				totalnum = list.size(); //待投保总共条数

			} else { //全部订单
				orderstatus = null;
				queryParams.put("orderstatus", orderstatus);
				resultlist =  insbWorkflowmainDao.getMyOrderListForChn(queryParams);
				for(Map<String, Object> result : resultlist){
					//if ("closed".equalsIgnoreCase(result.get("taskstate").toString())) continue;
					String processInstanceId = (String)result.get("processInstanceId");
					List<Map<String, Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskIdForChn(processInstanceId);
					LogUtil.info("showMyOrderForChn-全部订单:" + taskId + " " + JsonUtils.serialize(subQuoteInfoList));
					if(subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0))))
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
						Map<String, Object> map =subQuoteInfoList.get(i);
						if(StringUtil.isEmpty(map.get("inscomcode"))
								||StringUtil.isEmpty(map.get("prvshotname"))
								||StringUtil.isEmpty(map.get("taskcode")))
							subQuoteInfoList.remove(i);
					}
					if(subQuoteInfoList != null && subQuoteInfoList.size() != 0 && subQuoteInfoList.get(0) != null)
						result.put("quoteInfoList", subQuoteInfoList);

					List<INSBInsuredhis> insuredhisList = insuredhisDao.selectByTaskid(processInstanceId);
					if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
						INSBPerson insbPerson = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
						if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getName())) {
							result.put("insuredname", insbPerson.getName());
						}
					}
				}
				//totalnum = resultlist.size();
				totalnum = insbWorkflowmainDao.myOrderCountForChn(queryParams);
			}

			/*bodyResult.put("totalnum", totalnum);       //总共的数据条数  resultlist.size()先前的
			bodyResult.put("resultlist", resultlist);

			resultAll.setBody(bodyResult);
			resultAll.setMessage("我的订单查询成功");
			resultAll.setStatus("success");*/

			Map<String,Object> map = new HashMap<String, Object>();
			map.put("resultlist", resultlist);
			body.put("orderinfo", map);               //查询成功，完成结果的返回
			//if(resultlist!=null){
			body.put("totalnum", totalnum);          //总共的数据条数  resultlist.size()先前的
			//}
			bodyResult.put("myorderinfo", body);
			resultAll.setBody(bodyResult);
			resultAll.setMessage("我的订单查询成功");
			resultAll.setStatus("success");

			return resultAll;

		}catch(Exception e){
			e.printStackTrace();
			resultAll.setMessage("我的订单查询失败");
			resultAll.setStatus("fail");
		}
		return resultAll;
	}

	@Override
	public String showMyOrderForMinizzb(String channeluserid, String carlicenseno, String insuredname,
			String orderstatus, int limit, long offset,String taskcreatetimeup,String taskcreatetimedown) {
		CommonModel resultAll = new CommonModel();
		try{
			Map<String,Object> queryParams = new HashMap<String,Object>();
			Map<String,Object> bodyResult = new HashMap<String,Object>();
			Map<String,Object> body = new HashMap<String,Object>();
			//组织查询条件
//			queryParams.put("agentnum", agentnum);          //代理人工号
			queryParams.put("agentid", channeluserid);          //渠道用户uuid
			queryParams.put("carlicenseno", carlicenseno);  //车牌号
			queryParams.put("insuredname", insuredname);    //被保人姓名
			queryParams.put("limit", limit);                //每页显示的数目
			queryParams.put("offset", offset);              //偏移量
			queryParams.put("taskcreatetimeup", taskcreatetimeup);     //时间前.contains(":")? taskcreatetimeup:taskcreatetimeup+" 00:00:00"
			queryParams.put("taskcreatetimedown", taskcreatetimedown);  //时间后
			//INSBAgent agent = new INSBAgent();
			//agent.setJobnum(agentnum);
			//agent = insbAgentDao.selectOne(agent);
			//if(agent!=null){
			//	String shareid = agent.getId();
			//	queryParams.put("shareagentnum", shareid);
			//}

			List<Map<String,Object>> resultlist = null;
			long totalnum = 0;

			if("2".equals(orderstatus)){
				orderstatus = "20";
				queryParams.put("orderstatus",orderstatus);
//				resultlist =  insbWorkflowmainDao.getMyOrderList2Pay(queryParams);
				resultlist =  insbWorkflowmainDao.getMyOrderList2PayForMinizzb(queryParams);
				for(int j = resultlist.size() - 1;j >= 0;j--){
					Map<String, Object> result = resultlist.get(j);
					String processInstanceId = (String) result.get("processInstanceId");
					List<Map<String,Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
					if(subQuoteInfoList==null||(subQuoteInfoList.size()==1&&StringUtil.isEmpty(subQuoteInfoList.get(0))))
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					for (int i = subQuoteInfoList.size()-1;i>=0;i--) {
						Map<String, Object> map =subQuoteInfoList.get(i);
						if(!"20".equals(map.get("taskcode"))){
							subQuoteInfoList.remove(i);
						}else{
							//去掉过期的单子 2016-03-30
							if(removeOverTimeData(result.get("processInstanceId"),map.get("inscomcode"))){
								subQuoteInfoList.remove(i);
							}
							if("20".equals(map.get("taskcode"))){//当前状态为20 支付的时候判断 是否 为柜台支付  柜台支付返回 柜台支付 状态
								if(!"admin".equals(map.get("operator"))){
									map.put("taskcode", "201");//柜台支付状态
								}
							}
						}
					}
					result.put("quoteInfoList", subQuoteInfoList);
					if(subQuoteInfoList.size()==0){
						resultlist.remove(result);
					} else {
                        List<INSBInsuredhis> insuredhisList = insuredhisDao.selectByTaskid(processInstanceId);
                        if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
                            INSBPerson insbPerson = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
                            if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getName())) {
                                result.put("insuredname", insbPerson.getName());
                            }
                        }
                    }
				}
				totalnum = resultlist.size();
				//totalnum = insbWorkflowmainDao.myOrderCount2Pay(queryParams);
			}else if("1".equals(orderstatus)){
				orderstatus =null;
				queryParams.put("orderstatus",orderstatus);
				queryParams.replace("limit",null);
				queryParams.replace("offset",null);
//				resultlist =  insbWorkflowmainDao.getMyOrderListNew(queryParams);
				resultlist =  insbWorkflowmainDao.getMyOrderListNewForMinizzb(queryParams);
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				for(Map<String,Object> result : resultlist){
					if("closed".equalsIgnoreCase(result.get("taskstate").toString()))continue;
					String processInstanceId = (String) result.get("processInstanceId");
					List<Map<String,Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
					if(subQuoteInfoList==null||(subQuoteInfoList.size()==1&&StringUtil.isEmpty(subQuoteInfoList.get(0)))){
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					}
					for (int i=subQuoteInfoList.size()-1;i>0;i--) {
						Map<String, Object> map =subQuoteInfoList.get(i);
						if(StringUtil.isEmpty(map.get("inscomcode"))
								||StringUtil.isEmpty(map.get("prvshotname"))
								||StringUtil.isEmpty(map.get("taskcode")))
							subQuoteInfoList.remove(i);
					}
					result.put("quoteInfoList", subQuoteInfoList);
					int flag20;
					if(subQuoteInfoList!=null && subQuoteInfoList.size()>0){
						List<String> lastNode = Arrays.asList(new String[]{"25","26","27","28","23","24","21","20"});
						flag20=0;
						for (Map<String, Object> map : subQuoteInfoList) {
							if(map!=null){
								if(lastNode.contains((String) map.get("taskcode"))){
									flag20=1;
								}
							}

						}
						if(flag20!=1){
							list.add(result);
						}
					}
				}

				int endIndex = list.size() > ((int)offset+limit)? ((int)offset+limit): list.size();
				List<Map<String,Object>> tempList = new ArrayList<Map<String,Object>>();
				for(int i=(int)offset;i<endIndex;i++){
					tempList.add(list.get(i));
				}

				resultlist = tempList;

                for(Map<String, Object> result : resultlist) {
                    String processInstanceId = (String) result.get("processInstanceId");
                    List<INSBInsuredhis> insuredhisList = insuredhisDao.selectByTaskid(processInstanceId);

                    if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
                        INSBPerson insbPerson = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
                        if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getName())) {
                            result.put("insuredname", insbPerson.getName());
                        }
                    }
                }

				totalnum = list.size();//待投保总共条数


			}else{
				orderstatus = null;
				queryParams.put("orderstatus",orderstatus);
//				resultlist =  insbWorkflowmainDao.getMyOrderList(queryParams);
				resultlist =  insbWorkflowmainDao.getMyOrderListForMinizzb(queryParams);
				for(Map<String,Object> result : resultlist){
					if("closed".equalsIgnoreCase(result.get("taskstate").toString()))continue;
					String processInstanceId = (String) result.get("processInstanceId");
					List<Map<String,Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
					if(subQuoteInfoList==null||(subQuoteInfoList.size()==1&&StringUtil.isEmpty(subQuoteInfoList.get(0))))
						subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
					for (int i=subQuoteInfoList.size()-1;i>0;i--) {
						Map<String, Object> map =subQuoteInfoList.get(i);
						if(StringUtil.isEmpty(map.get("inscomcode"))
								||StringUtil.isEmpty(map.get("prvshotname"))
								||StringUtil.isEmpty(map.get("taskcode")))
							subQuoteInfoList.remove(i);
					}
					if(subQuoteInfoList!=null&&subQuoteInfoList.size()!=0&&subQuoteInfoList.get(0)!=null)
						result.put("quoteInfoList", subQuoteInfoList);

                    List<INSBInsuredhis> insuredhisList = insuredhisDao.selectByTaskid(processInstanceId);
                    if (insuredhisList != null && insuredhisList.size() > 0 && StringUtil.isNotEmpty(insuredhisList.get(0).getPersonid())) {
                        INSBPerson insbPerson = insbPersonDao.selectById(insuredhisList.get(0).getPersonid());
                        if (insbPerson != null && StringUtil.isNotEmpty(insbPerson.getName())) {
                            result.put("insuredname", insbPerson.getName());
                        }
                    }
				}
				//totalnum = resultlist.size();
				totalnum = insbWorkflowmainDao.myOrderCountForMinizzb(queryParams);
			}

			Map<String,Object> map = new HashMap<String, Object>();
			map.put("resultlist", resultlist);
			body.put("orderinfo", map);               //查询成功，完成结果的返回
			//if(resultlist!=null){
				body.put("totalnum",totalnum );          //总共的数据条数  resultlist.size()先前的
			//}
			bodyResult.put("myorderinfo", body);
			resultAll.setBody(bodyResult);
			resultAll.setMessage("我的订单查询成功");
			resultAll.setStatus("success");

			return JSONObject.fromObject(resultAll).toString();
		}catch(Exception e){                         //查询失败，出现异常
			e.printStackTrace();
			resultAll.setMessage("我的订单查询失败");
			resultAll.setStatus("fail");
			return JSONObject.fromObject(resultAll).toString();
		}
	}

	/**
	 * 判断支付有效期 true 过期不能支付
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	public boolean removeOverTimeData(Object taskid, Object inscomcode) {
		boolean result = true;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("taskid", taskid);
		map.put("inscomcode",inscomcode);
		//List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectPolicyitemByInscomTask(map);
		List<Map> policyitemList = insbPolicyitemDao.selectRemoveOverTimeData(map);
		if (policyitemList == null || policyitemList.isEmpty()) {
			LogUtil.error("removeOverTimeData:"+taskid+",prvid:"+inscomcode+",保单数据为空");
			return result;
		}
		Date syStartDate = null, jqStartDate = null;
		for(Map m : policyitemList){
			if (m== null || m.get("risktype") == null) continue;
			if("0".equals(m.get("risktype"))){
				syStartDate = m.get("startdate") == null ? null :(Date) m.get("startdate");
			}else{
				jqStartDate = m.get("startdate") == null ? null :(Date) m.get("startdate");
			}
		}
		if(syStartDate ==null && jqStartDate == null) {
            LogUtil.error("removeOverTimeData:"+taskid+",prvid:"+inscomcode+",起保日期为空");
			return result;
		}

        //选取先过期的日期
        Date quotesuccessTimes = null;

		/*//获取流程走到支付节点的时间
		INSBWorkflowsubtrack insbWorkflowsubtrack = insbWorkflowsubtrackDao.selectOneByTaskidAndInscomcode(map);
		if(null != insbWorkflowsubtrack){
			//支付有效期
			INSBProvider insbProvider = insbProviderDao.selectById(inscomcode.toString());

			if(null != insbProvider){
				if(null == insbProvider.getPayvalidity()){
					quotesuccessTimes = ModelUtil.gatFastPaydate(syStartDate, jqStartDate);
				}else{
					quotesuccessTimes = ModelUtil.gatFastPaydateToNow(syStartDate, jqStartDate, insbWorkflowsubtrack.getCreatetime(), insbProvider.getPayvalidity());
				}
			}

			if(!(ModelUtil.daysBetween(quotesuccessTimes, new Date()) > 0)){
				result = false;
			}
		}*/
		Map myMap = policyitemList.get(0);
		if (myMap != null) {
			Date createDate = myMap.get("createtime")== null ? null : (Date) myMap.get("createtime");
			if(null == myMap.get("payvalidity")){
				quotesuccessTimes = ModelUtil.gatFastPaydate(syStartDate, jqStartDate);
			}else{
				quotesuccessTimes = ModelUtil.gatFastPaydateToNow(syStartDate, jqStartDate, createDate, (int)myMap.get("payvalidity"));
			}
			if(!(ModelUtil.daysBetween(quotesuccessTimes, new Date()) > 0)){
				result = false;
			}
		}

        LogUtil.debug("removeOverTimeData:"+taskid+",prvid:"+inscomcode+",到期时间:"+quotesuccessTimes);
		return result;
	}

	public boolean removeOverTimeData2(Object taskid, Object inscomcode) {
		boolean result = true;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("taskid", taskid);
		map.put("inscomcode",inscomcode);
		//List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectPolicyitemByInscomTask(map);
		Date syStartDate = null, jqStartDate = null;
		List<Map> policyitemList = insbPolicyitemDao.selectRemoveOverTimeData(map);
		if (policyitemList == null || policyitemList.isEmpty()) {
			LogUtil.error("removeOverTimeData:"+taskid+",prvid:"+inscomcode+",保单数据为空");
			return result;
		}
		for(Map m : policyitemList){
			if (m== null || m.get("risktype") == null) continue;
			if("0".equals(m.get("risktype"))){
				syStartDate = m.get("startdate") == null ? null :(Date) m.get("startdate");
			}else{
				jqStartDate = m.get("startdate") == null ? null :(Date) m.get("startdate");
			}
		}
		if(syStartDate ==null && jqStartDate == null) {
            LogUtil.error("removeOverTimeData:"+taskid+",prvid:"+inscomcode+",起保日期为空");
			return result;
		}

		//选取先过期的日期
		Date quotesuccessTimes = null;

		quotesuccessTimes = ModelUtil.gatFastPaydate(syStartDate, jqStartDate);
		LogUtil.debug("removeOverTimeData:"+taskid+",prvid:"+inscomcode+",到期时间:"+quotesuccessTimes);

		if(!(ModelUtil.daysBetween(quotesuccessTimes, new Date()) > 0)){
			result = false;
		}

		return result;
	}


	/*
	 * 显示出单网点一个
	 */
	@Override
	public String showSingleSite(String processinstanceid,String inscomcode) {
		CommonModel resultAll = new CommonModel();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("taskid", processinstanceid);
			map.put("inscomcode", inscomcode);
			INSBOrder order = insbOrderDao.selectOrderByTaskId(map);
			INSCDept dept = new INSCDept();
			dept = inscDeptDao.selectByComcode(order.getDeptcode());
			Map<String,Object> bodyResult = new HashMap<String,Object>();
			bodyResult.put("deptname", dept.getComname());    //出单网点名称
			bodyResult.put("deptshortname", dept.getShortname());  //出单网点简称
			bodyResult.put("deptaddress", dept.getAddress());     //出单网点地址
			resultAll.setBody(bodyResult);
			resultAll.setMessage("出单网点查询成功");
			resultAll.setStatus("success");
			return JSONObject.fromObject(resultAll).toString(); 
		}catch(Exception e){
			e.printStackTrace();
			resultAll.setMessage("出单网点查询失败");
			resultAll.setStatus("fail");
			return JSONObject.fromObject(resultAll).toString();
		}
	}
	
	@Override
	protected BaseDao<INSBOrder> getBaseDao() {
		return null;  
	}
}
