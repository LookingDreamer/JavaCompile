package com.common;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.redis.CMRedisClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dai on 2016/8/30.
 */
public class WorkflowFeedbackUtil {
    //报价
    public static final String quote_complete = "报价完成";
    public static final String quote_sendback = "退回修改";
    public static final String quote_reject = "拒绝承保";
    public static final String quote_redo = "重新报价";
    public static final String quote_failed = "报价失败";
    public static final String quote_storge_successful = "暂存成功";
    public static final String quote_storge_failed = "暂存失败";
    public static final String quote_timeout = "超时未回写";
    //核保
    public static final String underWriting_complete = "核保完成";
    public static final String underWriting_sendback = "退回修改";
    public static final String underWriting_reject = "拒绝承保";
    public static final String underWriting_redo = "重新核保";
    public static final String underWriting_failed = "核保失败";
    public static final String underWriting_storge_successful = "暂存成功";
    public static final String underWriting_storge_failed = "暂存失败";
    public static final String underWriting_timeout = "超时未回写";
    public static final String underWriting_loop = "核保轮询";
    //支付
    public static final String payment_complete = "支付成功";
    public static final String payment_reunderWriting = "重新核保";
    //二支
    public static final String secpayment_complete = "二次支付完成";
    //单方
    public static final String quote_cancel = "取消投保";
    //多方
    public static final String multiQuote_cancel = "取消报价";
    //核保
    public static final String underWrittn_complete = "承保完成";
    public static final String underWrittn_sendback = "承保退回";
    public static final String underWrittn_print = "打单完成";
    public static final String underWrittn_deliver = "配送完成";
    
    public static final String writeback_timeout = "回写超时";
    public static final String writeback_inconformity = "回写数据异常";
    public static final String writeback_duplication = "重复投保";
    public static final String writeback_fail = "回写数据保存失败";

    public static final String loop_success = "轮询成功";
    public static final String loop_fail = "轮询失败";

    public static final String manual_complete = "人工干预完成";
    //自动能力启动异常
    public static final String itf_fail = "能力启动异常";

    public static final int dbIndex = 2;
    public static final String module = "taskFeedBack";
    //过期时间为3天
    private static final int expire = 259200;

    /**
     * 设置流程轨迹备注与处理人
     * @param mainTaskId 主任务号（如果是子流程，该参数可为空）
     * @param subTaskId 子任务号（如果是主流程，该参数为空）
     * @param taskCode 当前流程环节编码
     * @param taskState 预期流程状态（比如当前状态是报价中，后面需要推报价完成，则预期流程状态就是完成，即Completed）
     * @param taskName 当前流程环节名称（在mainTaskId,subTaskId的值确定的情况下，该参数可为空）
     * @param taskFeedBack 备注（必填）
     * @param operator 处理人（必填）
     */
    public static void setWorkflowFeedback(String mainTaskId, String subTaskId, String taskCode, String taskState, String taskName, String taskFeedBack, String operator) {
        LogUtil.info(mainTaskId+","+subTaskId+","+taskCode+","+taskState+"设置备注："+taskFeedBack+",处理人："+operator);

        if (StringUtil.isEmpty(mainTaskId) && StringUtil.isEmpty(subTaskId)) return;
        if (StringUtil.isEmpty(taskCode) || StringUtil.isEmpty(taskState)) return;
        if (StringUtil.isEmpty(taskFeedBack)) return;

        Map<String, String> rdValue = new HashMap<>(2);
        rdValue.put("tfb", taskFeedBack);
        rdValue.put("opt", operator);

        CMRedisClient.getInstance().set(dbIndex, module, genKey(mainTaskId, subTaskId, taskCode, taskState), rdValue, expire);
    }

    /**
     * 获取流程轨迹备注与处理人
     * @param mainTaskId 主任务号
     * @param subTaskId 子任务号
     * @param taskCode 当前流程环节编码
     * @param taskState 当前流程环节状态
     * @param taskName 当前流程环节名称
     */
    public static Map<String, String> getWorkflowFeedback(String mainTaskId, String subTaskId, String taskCode, String taskState, String taskName) {
        Map<String, String> data = CMRedisClient.getInstance().get(dbIndex, module, genKey(mainTaskId, subTaskId, taskCode, taskState), Map.class);

        LogUtil.info(mainTaskId+","+subTaskId+","+taskCode+","+taskState+"获取备注："+(data!=null ? data.get("tfb") : "")+",处理人："+(data!=null ? data.get("opt") : ""));
        return data;
    }

    //删除
    public static void delWorkflowFeedback(String mainTaskId, String subTaskId, String taskCode, String taskState, String taskName) {
        LogUtil.info(mainTaskId+","+subTaskId+","+taskCode+","+taskState+"删除备注");
        CMRedisClient.getInstance().del(dbIndex, module, genKey(mainTaskId, subTaskId, taskCode, taskState));
    }

    private static String genKey(String mainTaskId, String subTaskId, String taskCode, String taskState) {
        String keyPre = null;
        if (StringUtil.isEmpty(subTaskId)) {
            keyPre = "main:" + mainTaskId;
        } else {
            keyPre = "sub:" + subTaskId;
        }
        return keyPre + ":" + taskCode + ":" + taskState;
    }
}
