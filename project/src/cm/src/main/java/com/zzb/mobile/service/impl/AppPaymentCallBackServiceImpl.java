package com.zzb.mobile.service.impl;
 
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.mobile.entity.INSBPayResult;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppPaymentCallBackService;
import com.zzb.mobile.service.AppPaymentService;
import com.zzb.mobile.service.INSBPayResultService;

@Service
public class AppPaymentCallBackServiceImpl implements AppPaymentCallBackService{
	//private static Logger logger = Logger.getLogger(AppPaymentCallBackService.class);
	@Resource 
	private INSBOrderpaymentService orderpaymentService;	
	@Resource  
	private AppPaymentService payService;
	@Resource 
	private INSBPayResultService paymentService;
	@Resource 
	private INSBWorkflowmainService  flowService;
	@Override
	public String callBack(String bizId,String bizTransactionId, String insSerialNo,
			String paySerialNo, String amount, String transDate,
			String payResult, String orderState, String payResultDesc,String nonceStr,String sign) {
		//保存支付信息
		 
		INSBOrderpayment orderpayment = new INSBOrderpayment();
		orderpayment.setPayflowno(bizTransactionId);
		List<INSBOrderpayment> orderpaymentflowList = orderpaymentService.queryList(orderpayment);
		if(orderpaymentflowList.size()==0){
			LogUtil.info("支付回掉接口失败==bizId:"+bizId+",bizTransactionId:"+bizTransactionId+",insSerialNo:"+insSerialNo+",paySerialNo:"+paySerialNo+
					",amount:"+amount+",transDate:"+transDate+",payResult:"+payResult+",orderState:"+orderState+",payResultDesc:"+payResultDesc);
			return "200";
		}
		bizId = orderpaymentflowList.get(0).getPaymentransaction();
		
		LogUtil.info("进入支付回掉接口==bizId:"+bizId+",bizTransactionId:"+bizTransactionId+",insSerialNo:"+insSerialNo+",paySerialNo:"+paySerialNo+
				",amount:"+amount+",transDate:"+transDate+",payResult:"+payResult+",orderState:"+orderState+",payResultDesc:"+payResultDesc);
		INSBPayResult result = addPayResult(bizId,bizTransactionId, insSerialNo, paySerialNo,amount, transDate, payResult, orderState, payResultDesc, nonceStr, sign);
		 //***************************更改订单支付状态**************************************
		payService.updateOrderPayment(bizId,bizTransactionId,null, result.getPaySerialNo(),orderState, result.getTransDate());
	    //***************************更改订单支付状态**************************************
		if("02".equals(orderState)){
			payService.updateOrder(bizId, "2", "04");//更改订单状态
			//***************************工作流流程流转**************************************
			CommonModel model2 = payService.completeTask(bizId);
			//***************************工作流流程流转**************************************
			if(model2.getStatus().equals("success")){
				result.setStatus(2);
				paymentService.updateById(result); //更改支付结果里的状态
			}
		} 
		LogUtil.info("结束支付回掉接口==bizId:"+bizId);
		return "200";
	}
	@Override
	public INSBPayResult addPayResult(String bizId,String payFlowNo, String insSerialNo,String paySerialNo, String amount, String transDate,String payResult, String orderState, String payResultDesc,String nonceStr,String sign) {
		LogUtil.info("进入addPayResult方法==bizId:"+bizId+",payFlowNo:"+payFlowNo+",insSerialNo:"+insSerialNo+",paySerialNo:"+paySerialNo+
				",amount:"+amount+",transDate:"+transDate+",payResult:"+payResult+",orderState:"+orderState+",payResultDesc:"+payResultDesc);
		DateFormat df =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		//***************************先保存**********************************************
		/*INSBPayResult result = new INSBPayResult();
		INSBPayResult payresult;
		result.setBizId(bizId);
		payresult=paymentService.queryOne(result);*/
		/*if(payresult==null){*/
		INSBPayResult result = new INSBPayResult();
		/*}else
			result=payresult;*/
		result.setBizId(bizId);
		if(!StringUtil.isEmpty(amount))
			result.setAmount(Double.valueOf(amount));
		result.setPayflowno(payFlowNo);
		result.setInsSerialNo(insSerialNo);
		result.setPayResult(payResultDesc);
		result.setOrderState(orderState);
		result.setPayResult(payResult);
		result.setPaySerialNo(paySerialNo);
		result.setPayflowno(payFlowNo);
		result.setNonceStr(nonceStr);
		result.setSign(sign);
		Date date = new Date();
		try {
			if(!StringUtil.isEmpty(transDate))
				date = df.parse(transDate);
		} catch (ParseException e) {
		}
		result.setTransDate(date);
		/*if(payresult==null)*/
			paymentService.insert(result);
		/*else{
			result.setModifytime(new Date());
			paymentService.updateById(result);
		}*/
		LogUtil.info("关闭addPayResult方法==bizId:"+bizId);
		return result;
	}
	public INSBOrderpayment updateOrderStatus(String bizId, String paySerialNo,String status, Date transDate){
		return payService.updateOrderPayment(bizId,"","", paySerialNo,status, transDate);
	}
	public CommonModel continueWorkFlow(String taskid){
		return payService.completeTask(taskid);
	}
}
