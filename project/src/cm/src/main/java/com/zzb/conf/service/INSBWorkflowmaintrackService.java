package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
import com.zzb.conf.entity.baseData.INSBQueryhistoryinfo;

public interface INSBWorkflowmaintrackService extends BaseService<INSBWorkflowmaintrack> {

	public Map<String,Object> getMyTaskLastNode(String instanceid);
	/**
	 * 
	 * @param taskId 任务跟踪号，主任务号  必填
	 * @param taskStatu 任务当前状态=="支付成功"
	 * @param closeReason 关闭流程原因=="全额退款"
	 * @param operator 操作人=="admin"
	 * @return jsonStr == {"message":"关闭成功","status":"success"}
	 */
	public String chnRefundThenCloseflow(String taskId,String taskStatu,String closeReason,String operator);
	/**
	 * 通过主流程实例id和任务状态查询任务轨迹
	 * */
	public INSBWorkflowmaintrack getMainTrack(String maininstanceid, String taskcode);
	
	/**
	 * 通过主流程实例id获取当前状态的任务轨迹
	 * */
	public INSBWorkflowmaintrack getMainTrackByInscomcode(String maininstanceid);

	/**
	 * 当日完成数量
	 */
	public long countDayTask(Map<String, Object> map);

	/**
	 * 当月完成数量
	 */
	public long countMonthTask(Map<String, Object> map);
	/**
	 * 流程图：业管
	 */
    public Map<String,Object> getUserInfo(String taskid);
    
    /**
     * 
     * 通过实例id得到流程历史状态
     * @param mianInstanceId
     * @param subInstanceId
     * @return
     */
    public List<Map<String,String>>  getWorkflowStatusByInstanceId(String mianInstanceId,String subInstanceId);
    
    /**
     * liuchao
	 * 人工核保页面核保轮询操作调用接口  
	 * 点击轮序按钮，调用工作流进入轮询节点，工作流调用成功，会调用轮询接口，页面直接回到我的任务页面，轮询接口返回结果后重新调用工作流接口
	 */
    public Map<String, Object> loopUnderwriting(String maininstanceId, String subInstanceId, String inscomcode, String userCode);
    
    /**
     * liuchao
     * 人工核保推送到核保轮询节点后，工作流回调接口  
	 * 每5分钟调用核保回写一次，一共调用三次（间隔时间和调用次数可以配置），每次使用流程中最新的精灵或EDI核保途径发起核保回写，
	 * 根据最后核保轮询回写结果，调用工作流对应接口
     */
    public Map<String, Object> toLoopUnderwriting(String maininstanceId, String subInstanceId, String inscomcode, String operator);
    
    /**
     * liuchao
     * 核保回写轮询后处理回写结果接口，精灵或EDI核保回写后调用
     * @param subInstanceId 子流程id
     * @param result  核保回写结果(success或fail)
     */
    public Map<String, Object> loopUWResultHandler(String subInstanceId, String result, String maininstanceId, String inscomcode, String msg);

	/**
	 * 推轮询节点工作流
	 * @param maininstanceId
	 * @param subInstanceId
	 * @param inscomcode
	 * @param result
	 * @param msg
	 * @return
	 */
	public Map<String, Object> loopWorkFlowToNext(String maininstanceId, String subInstanceId, String inscomcode, String result, String msg);

	/**
	 * 更新轮询结果详情
	 * @param maininstanceId
	 * @param subInstanceId
	 * @param inscomcode
	 */
	public Map<String, Object> updateLoopUnderWritingDetail(String maininstanceId, String subInstanceId, String inscomcode, String loopResult, String msg);

	/**
	 * 记录轮询结果详情
	 * @param loopId
	 * @param startTime
	 * @param loopResult
	 * @param msg
	 * @return
	 */
	public Map<String, Object> logLoopUnderWritingDetail(String loopId, String startTime, String loopResult, String msg);

    /**
     * 查询我的历史任务
     */
    public Map<String,Object> queryMyHistorytask(INSBQueryhistoryinfo insbqueryhistoryinfo,INSCUser inscuser);
    
    public void deleteSchedulerJob(String jobName) throws SchedulerException ;
    /**
     * 人工任务处理增加打开任务轨迹
     * @param param
     */
    public void addTrackdetail(INSBWorkflowmaintrack param,String operator);
}