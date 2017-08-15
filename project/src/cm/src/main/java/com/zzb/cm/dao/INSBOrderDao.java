package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface INSBOrderDao extends BaseDao<INSBOrder>{
	public List<INSBOrder> selectOrderList(Map<String, Object> map);
	
	public INSBOrder selectOrderByTaskId(Map<String, Object> map);
	public List<String> selectOrdersByMap(Map<String, Object> map);
	public List<Map<String,Object>> selectOrderManageCode(Map<String, Object> map);
	
	public Long selectOrderManageCount(Map<String, Object> map);
	public List<INSBOrder> selectByPrvidLike(INSBOrder order);
	//public INSBOrder updateINSBOrder(INSBOrder insbOrder);
	public INSBOrder queryOrder(Map<String, String> param);
	public INSBOrder queryOrderByid(String orderid, String proid);

	/**
	 * liuchao  订单管理页面列表查询 
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> selectOrderManageCode01(Map<String, Object> params);
	
	/**
	 * liuchao  订单管理页面列表条数查询
	 * @param params
	 * @return
	 */
	public long selectOrderManageCodeCount01(Map<String, Object> params);
	
	/**
	 * liuchao  支付任务重新发起核保时判断核保途径
	 */
	public List<INSBWorkflowsubtrack> getUnderwritingTrack(String subInstanceId);
	
	/**
	 * 人工报价时报价成功后保费数据回写接口 liuchao
	 */
	public void priceDataBackForManualRecording(Map<String, Object> params);
	
	
	/**
	 *  报价回写  判断报价途径
	 */
	public List<INSBWorkflowsubtrack> getQuoteBackTrack(String subInstanceId);
	
	public List<String> getQuoteBackTrackStr(String subInstanceId);
	
	public List<String> getUnderwritingTrackStr(String subInstanceId);
	
	/**
	 * 杨威 	查询任务池中的待支付类型任务
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectOrderWorkflowsub(
            Map<String, Object> params);
	
	/**
	 * 杨威	查询任务池中的待支付类型任务数量
	 */
	public long countOrderWorkflowsub(Map<String, Object> params);
	
	/**
	 * 杨威	查询任务池中的待承保打单和待配送类型的任务
	 */
	public List<Map<String, Object>> selectOrderWorkflowmain(
            Map<String, Object> params);
	
	/**
	 * 杨威	查询任务池中的待承保打单和待配送类型的任务的任务数量
	 */
	public long countOrderWorkflowmain(Map<String, Object> params);
	
	/**
	 * 杨威   查询任务池中认证任务
	 */
	public List<Map<String,Object>> selectCertification(Map<String, Object> params);
	
	/**
	 * 杨威	查询任务池中认证任务总条数
	 */
	public long countCertification(Map<String, Object> params);
	
	/**
	 * 杨威 	查询业管是否有支付任务的权限
	 */
	public boolean checkPayPower(String userId);

	Page<INSBOrder> selectOrdersWithFairyQRPay(Date fromDateTime, Date toDateTime, Pageable pageable);

	Long selectOrdersCountWithFairyQRPay(Map<String, Object> query);
}