package com.zzb.cm.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.BeanUtils;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class INSBOrderDaoImpl extends BaseDaoImpl<INSBOrder> implements
        INSBOrderDao {
	
	public List<INSBOrder> selectOrderList(Map<String, Object> map){
			return this.sqlSessionTemplate.selectList(this.getSqlName("select"),map);
	}

	@Override
	public INSBOrder selectOrderByTaskId(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskId"),map);
	}

	@Override
	public List<String> selectOrdersByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList("ad_searchOrderListByMap",map);
	}

	@Override
	public List<Map<String,Object>> selectOrderManageCode(Map<String, Object> map) { 
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOrderManageCode"),map);
	}

	@Override
	public Long selectOrderManageCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOrderManageCount"),map);
	}

	@Override
	public List<INSBOrder> selectByPrvidLike(INSBOrder order) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByPrvidLike"),order);
	}

	@Override
	public INSBOrder queryOrder(Map<String, String> param) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryOrder"),param);
	}

	@Override
	public INSBOrder queryOrderByid(String orderid, String proid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderid", orderid);
		params.put("providerid", proid);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryOrderByid"),params);
	}

	/**
	 * liuchao  订单管理页面列表查询 
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectOrderManageCode01(
			Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOrderManageCode01"),params);
	}

	/**
	 * liuchao  订单管理页面列表条数查询
	 * @param params
	 * @return
	 */
	@Override
	public long selectOrderManageCodeCount01(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOrderManageCodeCount01"),params);
	}

	/**
	 * liuchao  支付任务重新发起核保时判断核保途径
	 */
	@Override
	public List<INSBWorkflowsubtrack> getUnderwritingTrack(String subInstanceId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getUnderwritingTrack"),subInstanceId);
	}

	/**
	 * 人工报价时报价成功后保费数据回写接口 liuchao
	 */
	@Override
	public void priceDataBackForManualRecording(Map<String, Object> params) {
		this.sqlSessionTemplate.update(this.getSqlName("priceDataBackForManualRecording"),params);
	}

	@Override
	public List<INSBWorkflowsubtrack> getQuoteBackTrack(String subInstanceId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getQuoteBackTrack"),subInstanceId);
	}

	@Override
	public List<String> getQuoteBackTrackStr(String subInstanceId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getQuoteBackTrackStr"),subInstanceId);
	}

	@Override
	public List<String> getUnderwritingTrackStr(String subInstanceId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getUnderwritingTrackStr"),subInstanceId);
	}
	
	/**
	 * 杨威 	查询任务池中的待支付类型任务
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectOrderWorkflowsub(
			Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOrderWorkflowsub"),params);
	}
	
	/**
	 *  杨威	查询任务池中的待承保打单和待配送类型的任务
	 */
	@Override
	public List<Map<String, Object>> selectOrderWorkflowmain(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOrderWorkflowmain2"),params);
	}
	/**
	 * 杨威	查询查询任务池中的待支付类型任务数量
	 */
	@Override
	public long countOrderWorkflowsub(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("countOrderWorkflowsub"),params);
	}
	
	/**
	 * 查询任务池中的待承保打单和待配送类型的任务数量
	 */
	@Override
	public long countOrderWorkflowmain(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("countOrderWorkflowmain2"),params);	}
	
	/**
	 * 杨威   查询任务池中认证任务
	 */
	@Override
	public List<Map<String, Object>> selectCertification(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectCertification"),params);
	}
	
	/**
	 * 杨威	查询任务池中认证任务总条数
	 */
	@Override
	public long countCertification(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("countCertification"),params);
	}
	
	/**
	 * 杨威 	查询业管是否有支付任务的权限
	 */
	@Override
	public boolean checkPayPower(String userId) {
		int count = this.sqlSessionTemplate.selectOne(this.getSqlName("checkPayPower"),userId);
		if(count > 0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Page<INSBOrder> selectOrdersWithFairyQRPay(Date fromDateTime, Date toDateTime, Pageable pageable) {
		Map<String, Object> query = new HashMap<>();
		query.put("fromDateTime", fromDateTime);
		query.put("toDateTime", toDateTime);
		if (pageable != null) {
			Map<String, Object> params = BeanUtils.toMap(getRowBounds(pageable));
			query.putAll(params);
			if (pageable.getSort() != null) {
				query.put("sorting", pageable.getSort().toString());
			}
		}
		List<INSBOrder> result = sqlSessionTemplate.selectList(getSqlName("selectOrdersWithFairyQRPay"), query);
		if (result == null) {
			result = Collections.emptyList();
		}
		Long total = selectOrdersCountWithFairyQRPay(query);
		return new PageImpl<>(result, pageable, total);
	}

	public Long selectOrdersCountWithFairyQRPay(Map<String, Object> query) {
		return (Long) sqlSessionTemplate.selectOne(getSqlName("selectOrdersCountWithFairyQRPay"), query);
	}
}