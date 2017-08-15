package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.controller.vo.DeliveryInfoItem;
import com.zzb.cm.controller.vo.OrderManageVO;
import com.zzb.cm.controller.vo.OrderManageVO01;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface INSBOrderService extends BaseService<INSBOrder> {

	public Map<String, Object> getTaskSummary(String taskid, String inscomcode);
	
	/**
	 * 通过taskid查询支付信息 
	 * @param taskid
	 * @return
	 */
	public Map<String,Object>  getPaymentinfo(String taskid, String inscomcode);

	/*
	 * 二次支付成功，查询方法
	 */
	public String getMediumPayment(String processinstanceid, String inscomcode, String taskid, String usercode) throws Exception;

	/*
	 * 配送完成 
	 */
	public String getDeliverySuccess(String processinstanceid, String usercode, String inscomcode, String deltype, String codevalue, String tracenumber) throws Exception;

	/*
	 * 承保打单配送完成
	 */
	public String undwrtDeliverySuccess(String processinstanceid, String usercode, String inscomcode, String deltype, String codevalue, String tracenumber, String taskcode);

	/*
	 * 订单管理页面信息liuchao
	 */
	public Map<String,Object> allSelectOrderManageCode(OrderManageVO01 myTaskVo01, INSCUser loginUser);
	
	/*
	 * 认证任务认领方法liuchao
	 */
	public Map<String,Object> getCertificationTask(String userId, String cfTaskId);

	public Map<String,Object> getCertificationTask2(String userCode, String cfTaskId);
	/*
	 * 车险任务认领方法liuchao
	 */
	public Map<String,Object> getCarInsureTask(String maininstanceId, String subInstanceId,
                                               String inscomcode, String mainOrsub, INSCUser loginUser);
	/*
	 * 分页声明
	 */
	public Map<String,Object> getLimitAndPageSize(OrderManageVO myTaskVo, String userId);
	/*
	 * 支付完成接口
	 */
	public String getPaySuccess(String processinstanceid, String inscomcode, String taskid, String usercode, String issecond)throws Exception;
	
	/*
	 * 人工录单完成接口
	 */
	public String ManualRecording(String processinstanceid, String isRebackWriting, String usercode);
	
	/*
	 * 人工录单退回修改接口
	 */
	public String ManualRecordingBack(String processinstanceid, String usercode);
	
	/*
	 * 人工回写完成接口
	 */
	public String manualWriteBackSuccess(String processinstanceid, String usercode);
	
	/*
	 * 人工回写退回修改
	 */
	public String manualWriteBack(String processinstanceid, String usercode);
	
	/*
	 * 人工核保完成接口,taskid  主流程id，processinstanceid 子流程id
	 */
	
	/*
	 * 人工核保返回修改
	 */
	public String manualUnderWritingBack(String processinstanceid, String usercode);
	
	
	public String updatePaymentinfo(String taskid, String subInstanceId, String inscomcode, String paymentmethod, String checkcode, String insurecono, String tradeno, String agentnum);
	
	/*
	 * 在配送完成，保存配送编号和物流公司的编号信息
	 * 配送编号 -运单号tracenumber  ，codevalue 物流公司编号
	 */
	public String saveTraceNumber(String processinstanceid, String tracenumber, String codevalue, String inscomcode);
	/**
	 * 根据供应商模糊查询
	 * @param order
	 * @return
	 */
	public List<INSBOrder> getListByPrvidLike(INSBOrder order);
	
	public void manualUnderWritingSuccess(String processinstanceid, String taskid, String inscomcode, String usercode) throws Exception;
	
	public String manualUnderWritingRestart(String processinstanceid, String taskid, String inscomcode, String usercode);
	
	/**
	 * liuchao  支付任务重新发起核保时判断核保途径
	 */
	public List<INSBWorkflowsubtrack> getUnderwritingTrack(String subInstanceId);
	
	/*
	 * 获取核保回写结果
	 */
	public String getUnderWritingResult(String taskid, String inscomcode);
	/*
	 * 获取重新查询承保结果
	 */
	public String getUnderwResult(String taskid, String inscomcode);
	
	/*
	 * 获取报价回写结果
	 */
	public String getQuoteBackResult(String taskid, String inscomcode);
	
	/**
	 * 报价回写 时判断报价途径
	 */
	public List<INSBWorkflowsubtrack> getQuoteBackTrack(String subInstanceId);
	
	/**
	 * 报价回写 时判断报价途径wang ,返回精灵 ，edi 去重后的taskcode
	 */
	public List<String> getQuoteBackTrackStr(String subInstanceId);
	
	
	/**
	 * 核保回写 时判断核保途径wang ,返回精灵 ，edi 去重后的taskcode
	 */
	public List<String> getUnderwritingTrackStr(String subInstanceId);
	
	/**
	 * 重新查询承保结果  时判断承保途径wang ,返回精灵 ，edi 去重后的taskcode
	 */
	public List<String> getUnderwTrackStr(String subInstanceId);
	
	/**
	 * 修改配送信息wang 
	 */
	public String editDeliveryInfo(DeliveryInfoItem deliveryInfo, INSCUser user);
	public String ManualRecordingRe(String processinstanceid, String usercode);
	/**
	 * 通过主流程id，判断是否为费改地区.如果不能查询到相应的数据,返回默认为费改地区 true
	 * @param taskid  主流程id
	 * @param instanceid  子流程id，当主流程id为空，子流程id必须传
	 * @return
	 */
	public boolean isFeeflagArea(String taskid, String instanceid);
	
	public List<Map<String, Object>> getSecondPaymentinfo(String taskid, String inscomcode);
	
	public INSBOrder getOneOrderByTaskIdAndInscomcode(String taskid, String inscomcode);
	
	public String getQuerySecondPayURL(String payChannelId);

	Page<INSBOrder> selectOrdersWithFairyQRPay(Date fromDateTime, Date toDateTime, Pageable pageable);
}