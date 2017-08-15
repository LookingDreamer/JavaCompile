package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.controller.vo.OrderListVo;
import com.zzb.cm.entity.INSBQuotetotalinfo;

import java.util.List;
import java.util.Map;

public interface INSBQuotetotalinfoDao extends BaseDao<INSBQuotetotalinfo> {

    /**
     * 通过任务Id查询 区域信息
     *
     * @param map
     * @return
     */

    public INSBQuotetotalinfo select(Map<String, Object> map);

    public Map<String, Object> selectByTaskId(String selectByTaskId);

    List<Map<String, Object>> getInscomInfo(String taskid);

    /**
     * 通过实例id得到报价总表id用于任务组
     *
     * @param taskid
     * @return
     */
    public String selectByTaskId4TaskSet(String taskid);

    /**
     * 流程图： 代理人信息
     */
    public Map<String, Object> getAgentInfoByTaskId(String taskid);

    /**
     * 修改报价信息表的折扣后金额
     *
     * @param sumPrice
     * @param taskInstanceId
     * @param inscomcode
     */
    public void updateQuoteDiscountAmount(double sumPrice, String taskInstanceId, String inscomcode);

    /**
     * 根据业管查找订单信息
     *
     * @param map
     */
    public List<OrderListVo> getQuotetotalinfoByUserid(Map<String, Object> map);

    /**
     * 查询订单信息
     *
     * @param map
     */
    public List<OrderListVo> getQuotetotalinfoByParams(Map<String, Object> map);

    /**
     * 查询订单信息数量
     *
     * @param map
     */
    public long getCountQuotetotalinfoByParams(Map<String, Object> map);

    /**
     * 根据业管查找订单信息数量
     *
     * @param map
     */
    public long getCountQuotetotalinfoByUserid(Map<String, Object> map);


    /**
     * 通过主实例id得到代理人信息和车牌号
     *
     * @param param
     * @return
     */
    public Map<String, String> selectAgentDataByTaskIdAndSub(Map<String, String> param);

    /**
     * 通过主实例id得到代理人信息和车牌号
     *
     * @param param
     * @return
     */
    public Map<String, String> selectAgentDataByTaskId(String param);

    List<INSBQuotetotalinfo> selectTotal(Map<String, Object> map);

    long selectTotalCount(Map<String, Object> map);

    int deleteByTaskid(String taskid);

    int updateDeleteflagByTaskid(Map<String, Object> map);
}