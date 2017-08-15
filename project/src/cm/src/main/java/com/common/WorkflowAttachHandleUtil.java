package com.common;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.redis.CMRedisClient;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Dai on 2017/2/20.
 */
public class WorkflowAttachHandleUtil {
    public static final int dbIndex = 8;
    public static final String module = "wfAttach";
    //过期时间为3天
    private static final int expire = 259200;
    //taskName TO taskCode
    private static ResourceBundle resourceBundleTaskcode = ResourceBundle.getBundle("config/taskCode");

    public static void record(String reqMethod, String reqService, Map<String, String> reqParams) {
        if (reqParams == null || reqParams.isEmpty() || StringUtil.isEmpty(reqMethod) || StringUtil.isEmpty(reqService)) {
            LogUtil.error("空的请求参数");
            return;
        }

        String dataString = reqParams.get("datas");
        if (StringUtil.isEmpty(dataString)) {
            LogUtil.error("空的请求参数体");
            return;
        }

        Map<String, Object> paramMap = (Map<String, Object>)JSONObject.toBean(JSONObject.fromObject(dataString), Map.class);

        String processinstanceid = String.valueOf(paramMap.get("processinstanceid"));
        //关闭任务
        if (StringUtil.isEmpty(processinstanceid)) {
            processinstanceid = String.valueOf(paramMap.get("mainprocessinstanceid"));
        }
        if (StringUtil.isEmpty(processinstanceid)) {
            LogUtil.error("空的processinstanceid");
            return;
        }
        LogUtil.info(processinstanceid+"设置记录："+reqService);

        String taskName = String.valueOf(paramMap.get("taskName"));

        //取消、或是拒保: main|sub
        if (StringUtil.isEmpty(taskName)) {
            taskName = String.valueOf(paramMap.get("process"));
        }
        //关闭任务
        if (StringUtil.isEmpty(taskName) && paramMap.containsKey("mainprocessinstanceid")) {
            taskName = "main";
        }
        //人工录单完成并且同时完成回写
        if (reqService.contains("/process/completeLudanAndWriteBack")) {
            taskName = "人工报价";
        }

        Map<String, Object> rdValue = new HashMap<>(2);
        rdValue.put("reqMethod", reqMethod);
        rdValue.put("reqService", reqService);
        rdValue.put("reqParams", dataString);
        rdValue.put("time", System.currentTimeMillis());
        rdValue.put("source", "cm");

        CMRedisClient.getInstance().set(dbIndex, module, genKey(processinstanceid, taskName), rdValue, expire);
    }

    public static Map<String, Object> getRecord(String processinstanceid, String taskName) {
        String key = genKey(processinstanceid, taskName);
        Map<String, Object> data = CMRedisClient.getInstance().get(dbIndex, module, key, Map.class);

        LogUtil.info("获取记录："+key);
        return data;
    }

    //删除
    public static void delRecord(String processinstanceid, String taskName) {
        String key = genKey(processinstanceid, taskName);
        LogUtil.info("删除记录："+key);
        CMRedisClient.getInstance().del(dbIndex, module, key);
    }

    private static String genKey(String processinstanceid, String taskName) {
        String taskCode = null;
        try {
            taskCode = resourceBundleTaskcode.getString(taskName);
        } catch (Exception e) {
            LogUtil.error(processinstanceid+" PropertyResourceBundle 资源获取失败："+e.getMessage());
        }
        if (StringUtil.isEmpty(taskCode)) {
            taskCode = taskName;
        }
        return processinstanceid + ":" + taskCode;
    }
    /**
     * 根据taskname获取taskcode
     * @param key
     * @return
     */
    public static String getBundleResource(String key) {
        try {
            return resourceBundleTaskcode.getString(key);
        } catch (Exception e) {
            LogUtil.error("PropertyResourceBundle 资源获取失败："+e.getMessage());
            return key;
        }
    }
    //工作流的
    public static Map<String, Object> getWfRecord(String mainInstanceId, String subInstanceId, String taskCode, String taskStatus) {
        String key = genWfKey(mainInstanceId, subInstanceId, taskCode, taskStatus);
        Map<String, Object> data = CMRedisClient.getInstance().get(dbIndex, module, key, Map.class);

        LogUtil.info("获取记录："+key);
        return data;
    }

    //删除工作流的
    public static void delWfRecord(String mainInstanceId, String subInstanceId, String taskCode, String taskStatus) {
        String key = genWfKey(mainInstanceId, subInstanceId, taskCode, taskStatus);
        LogUtil.info("删除记录："+key);
        CMRedisClient.getInstance().del(dbIndex, module, key);
    }

    //工作流的
    private static String genWfKey(String mainInstanceId, String subInstanceId, String taskCode, String taskStatus) {
        String processinstanceid = mainInstanceId;
        if (StringUtil.isNotEmpty(subInstanceId)) {
            processinstanceid = subInstanceId;
        }
        return processinstanceid + ":" + taskCode + ":" + taskStatus;
    }
}
