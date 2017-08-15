package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBOrderpayment;

import java.util.List;
import java.util.Map;

public interface INSBOrderpaymentService extends BaseService<INSBOrderpayment> {
	public INSBOrderpayment selectBySerialNumber(String serialNumber);
	public boolean validateOrder(INSBOrderpayment payment);
	/**
	 * 退款任务列表数据
	 * @param map
	 * @return
	 */
	public List<Map> refund(Map map);

	/**
	 * 退款任务总数
	 * @param map
	 * @return
	 */
	public Long refundCount(Map map);

	/**
	 * 设置退款任务状态
	 * @param orderpayment
	 * @return
	 */
	public int updateRefundstatus(INSBOrderpayment orderpayment);

	/**
	 * 退款任务提交数据
	 * @param taskid
	 * @return
	 */
	public Map<String, Object> refundtask(String taskid);
}