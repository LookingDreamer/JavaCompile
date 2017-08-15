package com.zzb.extra.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.common.ModelUtil;
import com.zzb.chn.util.JsonUtils;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.extra.entity.INSBMiniOrderTrace;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.extra.service.INSBMiniOrderManageService;
import com.zzb.extra.util.ParamUtils;
import com.zzb.extra.util.StatusMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBMiniOrderTraceDao;
import com.zzb.extra.service.INSBMiniOrderTraceService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBMiniOrderTraceServiceImpl extends BaseServiceImpl<INSBMiniOrderTrace> implements
		INSBMiniOrderTraceService {
	@Resource
	private INSBMiniOrderTraceDao insbMiniOrderTraceDao;

	@Resource
	private INSBMiniOrderManageService insbMiniOrderManageService;

	@Resource
	private INSBOrderpaymentService insbOrderpaymentService;


	@Override
	protected BaseDao<INSBMiniOrderTrace> getBaseDao() {
		return insbMiniOrderTraceDao;
	}

	@Override
	public String queryOrderList(Map<String,Object> map){

		/*
		 *从渠道查询快要过期订单，保存到insbMiniOrderTrace表
		 */
		saveInvalidOrder();
		//查询
		//System.out.println(JsonUtils.serialize(map));
		long total = insbMiniOrderTraceDao.queryOrderListCount(map);
		List<Map<String,Object>> orderList = insbMiniOrderTraceDao.queryOrderList(map);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total",total);
		result.put("rows",orderList);
		//System.out.println(JsonUtils.serialize(result));
		return JsonUtils.serialize(result);
	}

	public String queryOrderPaytime(String taskId) {
		try {
			INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
			insbOrderpayment.setTaskid(taskId);
			insbOrderpayment.setPayresult("02");
			insbOrderpayment = insbOrderpaymentService.queryOne(insbOrderpayment);
			String paytime = "";
			if(insbOrderpayment!=null){
				paytime = (!"".equals(ModelUtil.conbertToStringsdf(insbOrderpayment.getPaytime())))?ModelUtil.conbertToStringsdf(insbOrderpayment.getPaytime()):ModelUtil.conbertToStringsdf(insbOrderpayment.getCreatetime());
			}
			return paytime;
		} catch (Exception e) {
			return ParamUtils.resultMap(false, "系统错误");
		}
	}

	public int updateOrderOperateState(INSBMiniOrderTrace insbMiniOrderTrace){
		return insbMiniOrderTraceDao.updateOrderOperateState(insbMiniOrderTrace);
	}

	public int updateAgentOperateByTaskId(INSBMiniOrderTrace insbMiniOrderTrace){
		insbMiniOrderTrace.setModifytime(new Date());
		insbMiniOrderTrace.setOperator("admin");
		return insbMiniOrderTraceDao.updateAgentOperateByTaskId(insbMiniOrderTrace);
	}

	public int updateOrderTraceByTaskId(INSBMiniOrderTrace insbMiniOrderTrace){
		insbMiniOrderTrace.setModifytime(new Date());
		return insbMiniOrderTraceDao.updateOrderTraceByTaskId(insbMiniOrderTrace);
	}

	public String updateOrderTraceState(String taskId,String providerCode,String taskState){
		try {
			LogUtil.info(taskId+" newTaskState:"+taskState+" updateOrderTraceState: called");
			INSBMiniOrderTrace insbMiniOrderTrace = new INSBMiniOrderTrace();
			insbMiniOrderTrace.setTaskid(taskId);
			insbMiniOrderTrace.setProvidercode(providerCode);
			insbMiniOrderTrace = this.queryOne(insbMiniOrderTrace);
			if (insbMiniOrderTrace != null && StringUtil.isNotEmpty(insbMiniOrderTrace.getId())) {
				LogUtil.info(taskId+" newTaskState:"+taskState+" updateOrderTraceState:"+JsonUtils.serialize(insbMiniOrderTrace));
				insbMiniOrderTrace.setTaskstate(taskState);
				if ("6".equals(taskState) || "17".equals(taskState)) {//核保成功
					insbMiniOrderTrace.setOperatestate("5");//待完成交易
					insbMiniOrderTrace.setNoti("待完成交易");
				}else if ("11".equals(taskState)) {//承保完成待配送
					insbMiniOrderTrace.setOperatestate("6");//已促成交易
					insbMiniOrderTrace.setNoti("已促成交易");
				//处理状态由待用户发起退款变为用户发起退款
				}else if("12".equals(taskState) && "2".equals(insbMiniOrderTrace.getOperatestate())){
					insbMiniOrderTrace.setOperatestate("4");//用户已发起退款
					insbMiniOrderTrace.setNoti("用户已申请退款");
				}else if("12".equals(taskState) && "1".equals(insbMiniOrderTrace.getOperatestate())){
					insbMiniOrderTrace.setOperatestate("4");//用户已发起退款
					insbMiniOrderTrace.setNoti("用户已申请退款");
					insbMiniOrderTrace.setReason("5");//用户已由上传意向转变为申请退款
				}else if("12".equals(taskState) && "0".equals(insbMiniOrderTrace.getOperatestate())){
					insbMiniOrderTrace.setOperatestate("4");
					insbMiniOrderTrace.setReason("6");//用户主动发起了退款
					insbMiniOrderTrace.setNoti("待处理状态下用户已申请退款");
				}
				insbMiniOrderTrace.setModifytime(new Date());
				this.updateById(insbMiniOrderTrace);
				LogUtil.info(taskId+" newTaskState:"+taskState+" updateOrderTraceState: sucess");
			}
		}catch (Exception e){
			LogUtil.error(e);
			LogUtil.info(taskId+" updateOrderTraceState 失败：errmsg="+e.getMessage());
		}
		return "sucess";
	}

	private void saveInvalidOrder() {
		Map<Object, Object> queryMap = new HashMap<Object, Object>();
		queryMap.put("channelId", "nqd_minizzb2016");
		queryMap.put("willInvalidOrder", "willInvalidOrder");
		List<Map<Object, Object>> channelOrderList = insbMiniOrderManageService.queryOrderPagingList(queryMap);
		if (channelOrderList != null) {
			for (Map<Object, Object> channelOrder : channelOrderList) {
				try {
					INSBMiniOrderTrace insbMiniOrderTrace = new INSBMiniOrderTrace();
					insbMiniOrderTrace.setTaskid(String.valueOf(channelOrder.get("taskId")));
					insbMiniOrderTrace = insbMiniOrderTraceDao.selectOne(insbMiniOrderTrace);
					//是否已经保存了数据到insbminiOrderTrace表，已经保存了，就不再保存
					if (insbMiniOrderTrace != null && StringUtil.isNotEmpty(insbMiniOrderTrace.getAgentid())) {
						//数据已经存在
						//LogUtil.info("数据已经存在：" + JsonUtils.serialize(channelOrder));
					} else {
						insbMiniOrderTrace = new INSBMiniOrderTrace();
						insbMiniOrderTrace.setId(UUIDUtils.random());
						insbMiniOrderTrace.setTaskid(String.valueOf(channelOrder.get("taskId")));
						insbMiniOrderTrace.setProvidercode(String.valueOf(channelOrder.get("prvId")));
						insbMiniOrderTrace.setAgentid(String.valueOf(channelOrder.get("channelUserId")));
						insbMiniOrderTrace.setCarlicenseno(String.valueOf(channelOrder.get("carLicenseNo")));
						insbMiniOrderTrace.setCarowner(String.valueOf(channelOrder.get("insureName")));
						insbMiniOrderTrace.setPrvname(String.valueOf(channelOrder.get("prvName")));
						//起保日期
						String startTime = StringUtil.isEmpty(String.valueOf(channelOrder.get("bizStartDate"))) ? String.valueOf(channelOrder.get("efcStartDate")) : String.valueOf(channelOrder.get("bizStartDate"));
						insbMiniOrderTrace.setStarttime(startTime);
						insbMiniOrderTrace.setTaskstate(String.valueOf(channelOrder.get("taskState")));
						String premium = StringUtil.isEmpty(String.valueOf(channelOrder.get("quoteAmount"))) ? String.valueOf(channelOrder.get("firstPayAmount")) : String.valueOf(channelOrder.get("quoteAmount"));
						insbMiniOrderTrace.setPremium(premium);
						insbMiniOrderTrace.setMsg(String.valueOf(channelOrder.get("msg")));
						insbMiniOrderTrace.setPaytime(this.queryOrderPaytime(insbMiniOrderTrace.getTaskid()));
						insbMiniOrderTrace.setCreatetime(new Date());
						insbMiniOrderTrace.setOperatestate("0");
						insbMiniOrderTrace.setAgentoperate("0");
						insbMiniOrderTraceDao.insert(insbMiniOrderTrace);
					}
				} catch (Exception e) {
					LogUtil.info("该笔数据保存错误：" + JsonUtils.serialize(channelOrder) + ",errMsg:" + e.getMessage());
				}
			}
		}
	}

}