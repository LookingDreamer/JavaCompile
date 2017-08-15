package com.common;

import java.util.Random;

import org.apache.log4j.Logger;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
/**
 * 调用云端查询接口 
 * 
 * @author hejie
 *
 */
public class GenerateSequenceUtil {
    /**
     * 时间格式生成序列
     * @return String
     */
    public static synchronized String generateSequenceNo(String taskid ,String subtaskid) {
    	int less =0;
    	if(StringUtil.isEmpty(subtaskid)){
    		Random r  = new Random();
    		less=r.nextInt(1000);
    	}else{
    		less = Math.abs(Integer.valueOf(subtaskid)-Integer.valueOf(taskid));
    	}
    	String orderno = taskid+"X"+less+"0000000000000000000";
    	LogUtil.info("订单号生成：taskid="+taskid+",subtaskid="+subtaskid+",orderno="+orderno.substring(0,18));
    	return orderno.substring(0,18);
    }
}
