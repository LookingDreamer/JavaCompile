package com.lzgapi.order.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.ConfigUtil;
import com.common.HttpClientUtil;
import com.common.LZGUtil;
import com.common.WorkFlowUtil;
import com.lzgapi.order.model.LzgOrderCancleModel;
import com.lzgapi.order.model.LzgOrderSaveModel;
import com.lzgapi.order.model.LzgOrderUpdateModel;
import com.lzgapi.order.model.LzgPolicyModel;
import com.lzgapi.order.service.LzgOrderService;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBOrderlistenpushDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBOrderlistenpush;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.CommonModelforlzglogin;
import com.zzb.mobile.service.AppLoginService;

/**
 * 
 * 向蓝掌柜 同步订单信息
 * 
 * @author Administrator
 *
 */

@Service
@Transactional
public class LzgOrderServiceImpl implements LzgOrderService {
	@Resource
	private AppLoginService appLoginService;
	@Resource
	private INSBOrderService insbOrderInfoService;
	@Resource
	private INSBQuoteinfoDao quoteinfoDao;
	@Resource
	private INSBProviderDao insbProviderDao;
	@Resource
	private INSBOrderlistenpushDao insbOrderlistenpushDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private AppLoginService loginService;
	@Resource
	private INSBApplicantDao isnbApplicantDao;
	@Resource
	private INSBPersonDao insbPersonDao;
	@Resource
	private INSBAgentDao insbAgentDao;
	@Resource
	private ThreadPoolTaskExecutor taskthreadPool4workflow;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	private INSBInsuredDao insbInsuredDao;

	@Override
	public void updateOrderNormalData(String mainTaskId, String subTaskId,
			String taskName) {

		LogUtil.info("====蓝掌柜订单同步---maintaskid=" + mainTaskId + "---subtaskid="
				+ subTaskId + "---takname=" + taskName + "---更新订单普通状态");
//		if (taskName.endsWith("打单")) {
		if("打单".equals(taskName) || "配送".equals(taskName) || "结束".equals(taskName)){
			LogUtil.info("====蓝掌柜订单同步---maintaskid=" + mainTaskId
					+ "---subtaskid=" + subTaskId + "---takname=" + taskName
					+ "---订单完成");
			addOrderlistenpushData(mainTaskId, subTaskId, taskName, "4");
//		} else if (!"打单".equals(taskName) && !"配送".equals(taskName)
//				&& !"结束".equals(taskName)) {
		}else{
			LogUtil.info("====蓝掌柜订单同步---maintaskid=" + mainTaskId
					+ "---subtaskid=" + subTaskId + "---takname=" + taskName
					+ "---订单完成");
			addOrderlistenpushData(mainTaskId, subTaskId, taskName, "2");
		}

	}

	@Override
	public void cancleOrderFromCM(String mainTaskId, String subTaskId) {
		if (mainTaskId != null) {
			INSBOrderlistenpush model = insbOrderlistenpushDao
					.selectDataByTaskId(mainTaskId);
			if (model != null) {
				if (model.getSubtaskid() != null) {
					LogUtil.info("====CM取消蓝掌柜订单---maintaskid=" + mainTaskId
							+ "---subtaskid=" + subTaskId);
					addOrderlistenpushData(mainTaskId, model.getSubtaskid(),
							"取消", "3");
				}
			}
		} else {
			INSBOrderlistenpush orderModel = insbOrderlistenpushDao
					.selectDataBySubTaskId(subTaskId);
			if (orderModel != null) {
				if (orderModel.getTaskid() != null) {
					LogUtil.info("====CM取消蓝掌柜订单---maintaskid=" + mainTaskId
							+ "---subtaskid=" + subTaskId);
					addOrderlistenpushData(orderModel.getTaskid(), subTaskId,
							orderModel.getTaskname(), "3");
				}
			}
		}

	}

	@Override
	public void save4Again(String mainTaskId, String subTaskId,
			String taskName, String status) {
		addOrderlistenpushData(mainTaskId, subTaskId, taskName, status);

	}

	/**
	 * 更新订单信息
	 * 
	 * @param mainTaskId
	 * @param taskName
	 * @param status
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void addOrderlistenpushData(String mainTaskId, String subTaskId,
			String taskName, String status) {
		taskthreadPool4workflow.execute(new Runnable() {
			@Override
			public void run() {
				INSBOrderlistenpush orderModel = insbOrderlistenpushDao
						.selectDataByTaskId(mainTaskId);
				LogUtil.info("====CM向蓝掌柜订单同步---maintaskid=" + mainTaskId
						+ "---takname=" + taskName
						+ "---更新CM订单信息---CM最后一条订单信息=orderModel" + orderModel);

				if (orderModel != null) {
					orderModel.setTaskname(taskName);

					// 更新订单蓝掌柜
					if ("2".equals(status)) {
						LogUtil.info("====CM向蓝掌柜订单同步---maintaskid="
								+ mainTaskId + "---takname=" + taskName
								+ "---更新LZG订单普通信息---orderModel" + orderModel);
						updateLZG(orderModel, "9999");
						orderModel.setOperationtype("2");

						// 完成
					} else if ("4".equals(status)) {
						LogUtil.info("====CM向蓝掌柜订单同步---maintaskid="
								+ mainTaskId + "---takname=" + taskName
								+ "---更新LZG订单完成信息---orderModel" + orderModel);
						updateLZG(orderModel, "2");
						orderModel.setOperationtype("4");

						// 完成
					} else if ("3".equals(status)) {
						LogUtil.info("====CM向蓝掌柜订单同步---maintaskid="
								+ mainTaskId + "---takname=" + taskName
								+ "---更新LZG订单完成信息---orderModel" + orderModel);
						updateLZG(orderModel, "2");
						orderModel.setOperationtype("3");

						// 删除
					} else if ("1".equals(status)) {
						LogUtil.info("====CM向蓝掌柜订单同步---maintaskid="
								+ mainTaskId + "---takname=" + taskName
								+ "---更新LZG订单删除信息---orderModel" + orderModel);
						updateLZG(orderModel, "1");
						orderModel.setOperationtype("3");
					}
					orderModel.setCreatetime(new Date());
					orderModel.setModifytime(null);
					orderModel.setId(null);
					orderModel.setSubtaskid(subTaskId);
					insbOrderlistenpushDao.insert(orderModel);
					LogUtil.info("====CM向蓝掌柜订单同步---maintaskid=" + mainTaskId
							+ "---takname=" + taskName
							+ "---更新CM订单信息---orderModel" + orderModel);
				}
			}
		});
	}

	/**
	 * 向蓝掌柜更新信息
	 * 
	 * @param orderModel
	 * @param lzgUpdateModel
	 */
	private void updateLZG(INSBOrderlistenpush orderModel, String lzgType) {
		LogUtil.info("===CM向LZG同步订单信息---maintaskid=" + orderModel.getTaskid()
				+ "---taskcode=" + orderModel.getTaskcode());
		LzgOrderUpdateModel lzgUpdateModel = new LzgOrderUpdateModel();
		lzgUpdateModel.setOrderno(orderModel.getTaskid());
		lzgUpdateModel.setPlatform(Integer.valueOf(ConfigUtil
				.getPropString("lzg.zzb.platform.code")));

		INSBPolicyitem vo = new INSBPolicyitem();
		vo.setTaskid(orderModel.getTaskid());
		List<INSBPolicyitem> listPol = insbPolicyitemDao.selectList(vo);
		INSBInsured insured = insbInsuredDao.selectInsuredByTaskId(orderModel
				.getTaskid());
		INSBPerson insp = new INSBPerson();
		if (insured != null && !StringUtil.isEmpty(insured.getPersonid())) {
			insp = insbPersonDao.selectById(insured.getPersonid());
		}
		for (INSBPolicyitem pli : listPol) {
			LzgPolicyModel pm = new LzgPolicyModel();
			pm.setContNo(pli.getPolicyno());
			pm.setSupplierCode(pli.getInscomcode());
			pm.setRiskCode(pli.getInscomcode());
			pm.setSignDate(dateToString(pli.getInsureddate()));
			pm.setCvaliDate(dateToString(pli.getStartdate()));
			pm.setCinvaliDate(dateToString(pli.getEnddate()));
			pm.setContPrem("");//保费
			pm.setAmnt("");//保额
			if( pli.getDiscountCharge() != null ) {
				pm.setContPrem(pli.getDiscountCharge().toString());
			}
			if( pli.getAmount() != null ) {
				pm.setAmnt(pli.getAmount().toString());
			}
			pm.setInsuredIdno(insp.getIdcardno());// 被保人证件号
			pm.setInsuredName(insp.getName());// 被保人姓名
			lzgUpdateModel.getPolicylist().add(pm);
		}
		if ("0".equals(lzgType)) {
			LogUtil.info("===CM向LZG同步订单信息---maintaskid="
					+ orderModel.getTaskid() + "---taskcode="
					+ orderModel.getTaskcode() + "---取消订单");
			lzgUpdateModel.setStatus("9");
		} else if ("1".equals(lzgType)) {
			LogUtil.info("===CM向LZG同步订单信息---maintaskid="
					+ orderModel.getTaskid() + "---taskcode="
					+ orderModel.getTaskcode() + "---删除订单");
			lzgUpdateModel.setStatus("2");
		} else if ("2".equals(lzgType)) {
			LogUtil.info("===CM向LZG同步订单信息---maintaskid="
					+ orderModel.getTaskid() + "---taskcode="
					+ orderModel.getTaskcode() + "---完成订单");

			lzgUpdateModel.setStatus("1");
		} else if ("3".equals(lzgType)) {
			LogUtil.info("===CM向LZG同步订单信息---maintaskid="
					+ orderModel.getTaskid() + "---taskcode="
					+ orderModel.getTaskcode() + "---取消订单");

			lzgUpdateModel.setStatus("9");
		} else {

			LogUtil.info("===CM向LZG同步订单信息---maintaskid="
					+ orderModel.getTaskid() + "---taskcode="
					+ orderModel.getTaskcode() + "---未完成订单");

			lzgUpdateModel.setStatus("0");
		}
		String LZGStr = null;
		try {
			LogUtil.info("===CM向LZG同步订单信息---maintaskid="
					+ orderModel.getTaskid() + "---taskcode="
					+ orderModel.getTaskcode() + "---通知蓝掌柜");
			
			LZGStr = LZGUtil.noticeLZGUpdateOrder(lzgUpdateModel);
		} catch (Exception e) {
			LogUtil.info("===CM向LZG同步订单信息---maintaskid="
					+ orderModel.getTaskid() + "---taskcode="
					+ orderModel.getTaskcode() + "---通知蓝掌柜http异常---message="
					+ e.getMessage() + "---result=" + LZGStr.toString());
			e.printStackTrace();
			orderModel.setMessage(e.getMessage());

		}
		if (LZGStr != null) {
			Map<String, String> returnFlag = JSONObject.fromObject(LZGStr);
			if (!returnFlag.isEmpty()) {
				if ("OK".equals(returnFlag.get("status"))) {
					LogUtil.info("===CM向LZG同步订单信息---maintaskid="
							+ orderModel.getTaskid() + "---taskcode="
							+ orderModel.getTaskcode() + "---通知蓝掌柜完成");
					orderModel.setStatus("OK");
				} else if ("FAILED".equals(returnFlag.get("status"))) {
					LogUtil.info("===CM向LZG同步订单信息---maintaskid="
							+ orderModel.getTaskid() + "---taskcode="
							+ orderModel.getTaskcode() + "---通知蓝掌柜完蓝掌柜异常");
					orderModel.setStatus("FAILED");
				}
			} else {
				LogUtil.info("===CM向LZG同步订单信息---maintaskid="
						+ orderModel.getTaskid() + "---taskcode="
						+ orderModel.getTaskcode() + "---通知蓝掌柜完蓝掌柜异常");
				orderModel.setStatus("FAILED");
			}
			orderModel.setMessage(returnFlag.get("message"));
		} else {
			orderModel.setStatus("FAILED");
		}
	}

	@Override
	@Transactional
	public Map<String, String> cancleOrderFromLZG(String data) {

		Map<String, String> result = new HashMap<String, String>();

		LzgOrderCancleModel cancleModel = JSON.parseObject(data,
				LzgOrderCancleModel.class);
		LogUtil.info("===LZG取消订单---得到参数---cancleModel=" + cancleModel);

		INSBOrderlistenpush orderModel = insbOrderlistenpushDao
				.selectDataByTaskId(cancleModel.getOrderno());
		LogUtil.info("===LZG取消订单---maintaskid=" + orderModel.getTaskid()
				+ "---taskcode=" + orderModel.getTaskcode()
				+ "---得到当前订单信息---orderModel=" + orderModel);

		if (cancleModel.getOrderno() == null) {
			LogUtil.info("===LZG取消订单---得到当前订单信息异常---Orderno为null=");

			return result;
		}
		CommonModelforlzglogin lzgLoginModel = loginService.lzgLogin(
				cancleModel.getToken(), null);
		LogUtil.info("===LZG取消订单---maintaskid=" + orderModel.getTaskid()
				+ "---taskcode=" + orderModel.getTaskcode()
				+ "---验证参数信息合法性---lzgLoginModel=" + lzgLoginModel);

		if ("fail".equals(lzgLoginModel.getStatus())) {

			result.put("status", "FAIL");
			result.put("message", lzgLoginModel.getMessage());
			LogUtil.info("===LZG取消订单---maintaskid=" + orderModel.getTaskid()
					+ "---taskcode=" + orderModel.getTaskcode()
					+ "---验证参数信息合法性---失败" + lzgLoginModel.getMessage());
			return result;
		}

		String wStr = "fail";
		LogUtil.info("===LZG取消订单---maintaskid=" + orderModel.getTaskid()
				+ "---taskcode=" + orderModel.getTaskcode() + "---通知工作流---开始");
		if (orderModel.getSubtaskid() != null) {
			wStr = WorkFlowUtil.abortProcessByIdWorkflow(
					orderModel.getSubtaskid(), "sub", "front");
			LogUtil.info("===LZG取消订单---maintaskid=" + orderModel.getTaskid()
					+ "---taskcode=" + orderModel.getTaskcode()
					+ "---通知工作流取消子流程---工作流状态wStr=" + wStr);
		} else {
			wStr = WorkFlowUtil.abortProcessByIdWorkflow(
					cancleModel.getOrderno(), "main", "front");
			LogUtil.info("===LZG取消订单---maintaskid=" + orderModel.getTaskid()
					+ "---taskcode=" + orderModel.getTaskcode()
					+ "---通知工作流取消主流程---工作流状态wStr=" + wStr);
		}

		orderModel.setOperationtype("3");
		orderModel.setModifytime(new Date());

		@SuppressWarnings("unchecked")
		Map<String, String> wMap = JSONObject.fromObject(wStr);
		if ("fail".equals(wMap.get("message"))) {
			LogUtil.info("===LZG取消订单---maintaskid=" + orderModel.getTaskid()
					+ "---taskcode=" + orderModel.getTaskcode()
					+ "---通知工作流---工作流状态异常");

			if ("1".equals(cancleModel.getType())) {
				orderModel.setStatus("OK");
				orderModel.setMessage("删除成功");

				result.put("status", "OK");
				result.put("message", "成功");
				result.put("id", orderModel.getLzgid());

				LogUtil.info("===LZG取消订单---maintaskid="
						+ orderModel.getTaskid() + "---taskcode="
						+ orderModel.getTaskcode() + "---删除订单---成功");

				updateLZG(orderModel, "1");
			} else {
				LogUtil.info("===LZG取消订单---maintaskid="
						+ orderModel.getTaskid() + "---taskcode="
						+ orderModel.getTaskcode() + "---取消订单---失败");
				orderModel.setStatus("FAIL");
				orderModel.setMessage(wMap.get("reason"));

				result.put("status", "FAIL");
				result.put("message", "工作流调用失败");
				result.put("id", orderModel.getLzgid());

				updateLZG(orderModel, "9999");

			}
		} else {
			LogUtil.info("===LZG取消订单---maintaskid=" + orderModel.getTaskid()
					+ "---taskcode=" + orderModel.getTaskcode()
					+ "---取消订单---成功");
			orderModel.setStatus("OK");
			orderModel.setMessage(wMap.get("reason"));
			result.put("status", "OK");
			result.put("message", "成功");
			result.put("id", orderModel.getLzgid());
			updateLZG(orderModel, "0");
		}
		orderModel.setId(null);
		insbOrderlistenpushDao.insert(orderModel);

		return result;
	}

	@Override
	public void addorderListerpust(String taskid, String lzgid) {
		INSBOrderlistenpush order = new INSBOrderlistenpush();
		order.setTaskid(taskid);
		order.setPlatformtype("1");// 平台类型 1：懒掌柜
		order.setOperationtype("1");// 懒掌柜推动cm保存类型
		order.setTaskcode("1");// 任务状态 信息录入
		order.setLzgid(lzgid);
		order.setCreatetime(new Date());
		order.setModifytime(new Date());
		// 保存订单监听推送表
		insbOrderlistenpushDao.insert(order);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void addOrderData(INSBOrderlistenpush order, String productcode,
			String agentnum) {
		LogUtil.info("\r========蓝掌柜保存订单====--order信息==>" + productcode
				+ "订单信息--》" + order + "代理人工号" + agentnum);
		// 懒掌柜订单新增
		LzgOrderSaveModel model = transFormParams(order, productcode, agentnum);
		try {
			// 调用懒掌柜接口
			String LZGStr = LZGUtil.saveOrupdateLZGorder(model);
			// 懒掌柜返回值
			Map<String, String> returnFlag = JSONObject.fromObject(LZGStr);

			// 根据返回值修改临时订单表
			if (!returnFlag.isEmpty()) {
				order.setModifytime(new Date());
				if (returnFlag.get("status").equals("OK")) {
					order.setLzgid(returnFlag.get("id"));
				}
				order.setMessage(returnFlag.get("message"));
				order.setStatus(returnFlag.get("status"));

			} else {
				order.setModifytime(new Date());
				order.setStatus("FAILED");
				order.setMessage("接口调用返回null");
			}
			insbOrderlistenpushDao.updateById(order);
		} catch (Exception e) {
			order.setModifytime(new Date());
			order.setStatus("FAILED");
			order.setMessage("保存或者接口调用异常");
			insbOrderlistenpushDao.updateById(order);
			e.printStackTrace();
		}
	}

	public LzgOrderSaveModel transFormParams(INSBOrderlistenpush order,
			String productcode, String agentnum) {
		// 根据任务号去订单表查数据
		Map<String, String> param = new HashMap<String, String>();
		param.put("taskid", order.getTaskid());
		param.put("prvid", productcode);
		INSBOrder o = insbOrderDao.queryOrder(param);// 订单

		LzgOrderSaveModel model = new LzgOrderSaveModel();
		model.setId(order.getLzgid());
		model.setOrderno(order.getTaskid());
		model.setPlantform(Integer.valueOf(ConfigUtil
				.getPropString("lzg.zzb.platform.code")));
		model.setQuantity(1);// 数量
		if (o != null) {
			model.setPrice(o.getTotalpaymentamount());
			model.setTotalprice(o.getTotalpaymentamount());
		}
		model.setStatus("0");// 刚生成的订单状态都是未完成的
		INSBPolicyitem vo = new INSBPolicyitem();
		vo.setTaskid(order.getTaskid());
		vo.setInscomcode(productcode);
		List<INSBPolicyitem> listPol = insbPolicyitemDao.selectList(vo);
		INSBInsured insured = insbInsuredDao.selectInsuredByTaskId(order
				.getTaskid());
		INSBPerson insp = new INSBPerson();
		if (insured != null && !StringUtil.isEmpty(insured.getPersonid())) {
			insp = insbPersonDao.selectById(insured.getPersonid());
		}
		for (INSBPolicyitem pli : listPol) {
			LzgPolicyModel pm = new LzgPolicyModel();
			pm.setContNo(pli.getPolicyno());
			pm.setSupplierCode(pli.getInscomcode());
			pm.setRiskCode("");
			pm.setSignDate(dateToString(pli.getInsureddate()));
			pm.setCvaliDate(dateToString(pli.getStartdate()));
			pm.setCinvaliDate(dateToString(pli.getEnddate()));
			pm.setContPrem(pli.getDiscountCharge().toString());
			pm.setAmnt(pli.getAmount().toString());
			pm.setInsuredIdno(insp.getIdcardno());// 被保人证件号
			pm.setInsuredName(insp.getName());// 被保人姓名
			model.getPolicylist().add(pm);
		}
		return model;
	}

	@Override
	public CommonModel getProduct(String token, String lzgOtherUserId,
			String lzgManagerId, String lzgRequirementId, String lzgProductCode) {
		CommonModel model = new CommonModel();
		if (token == null || "".equals(token)) {
			model.setStatus("fail");
			model.setMessage("token不能为空");
			return model;
		}
		if (lzgOtherUserId == null || "".equals(lzgOtherUserId)) {
			model.setStatus("fail");
			model.setMessage("懒掌柜未绑定掌中保账户");
			return model;
		}

		return (CommonModel) appLoginService.lzgLogin(token, lzgOtherUserId);
	}

	@Override
	public CommonModel orderContinue(String token, String lzgOtherUserId,
			String lzgRequirementId, String lzgOtherOrderNo) {
		CommonModel model = new CommonModel();
		if (token == null || "".equals(token)) {
			model.setStatus("fail");
			model.setMessage("token不能为空");
			return model;
		}

		if (lzgOtherUserId == null || "".equals(lzgOtherUserId)) {
			model.setStatus("fail");
			model.setMessage("懒掌柜未绑定掌中保账户");
			return model;
		}
		// 自动登录
		model = (CommonModel) appLoginService.lzgLogin(token, lzgOtherUserId);
		if ("fail".equals(model)) {
			return model;
		}
		// 获取订单信息
		INSBOrder order = new INSBOrder();
		order.setOrderno(lzgOtherOrderNo);
		List<INSBOrder> os = insbOrderInfoService.getListByPrvidLike(order);
		if (os != null && os.size() > 0) {
			Map<Object, Object> map = (Map<Object, Object>) model.getBody();
			map = this.getOrderInfo(map, os.get(0));
			model.setBody(map);
		}

		return model;
	}

	private Map<Object, Object> getOrderInfo(Map<Object, Object> map,
			INSBOrder order) {
		map.put("order_id", order.getId());
		map.put("order_createtime", order.getCreatetime());
		map.put("order_moditytime", order.getModifytime());
		map.put("order_taskid", order.getTaskid());
		map.put("order_no", order.getOrderno());
		map.put("order_status", order.getOrderstatus());
		map.put("order_paymentstatus", order.getPaymentstatus());
		map.put("order_deliverystatus", order.getDeliverystatus());
		map.put("order_buyway", order.getBuyway());
		map.put("order_agentcode", order.getAgentcode());
		map.put("order_totalpaymenamount", order.getTotalpaymentamount());
		map.put("order_totalproductamount", order.getTotalproductamount());
		map.put("order_totalpromotionamount", order.getTotalpromotionamount());
		map.put("order_deptcode", order.getDeptcode());
		map.put("order_prvid", order.getPrvid());

		return map;
	}

	// 调用懒掌柜第三方批量保存订单接口，向懒掌柜同步非懒掌柜引流订单
	public CommonModel order2Lzg(String lzgManagerId, List<INSBOrder> orders) {
		CommonModel model = new CommonModel();
		String platform = ConfigUtil.getPropString("lzg.zzb.platform.code");
		// 第三方批量保存订单接口-非懒掌柜引流
		String url = ConfigUtil.getPropString("lzg.api.service.url")
				+ "/lm/requirement/savelist";
		for (INSBOrder order : orders) {

		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lzgmanagerid", lzgManagerId);
		map.put("platform", platform);
		map.put("orderlist", orders);
		JSONObject jsonObject = new JSONObject();
		if (map != null && !map.isEmpty()) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				Object value = entry.getValue();
				if (value != null) {
					jsonObject.put(entry.getKey(), value);
				}
			}
		}
		HttpClientUtil.doPostJsonString(url, jsonObject.toString());

		return model;
	}

	public String dateToString(Date date) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.format(date);
		}
		return null;
	}

	public Date StringToDate(String str) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

}
