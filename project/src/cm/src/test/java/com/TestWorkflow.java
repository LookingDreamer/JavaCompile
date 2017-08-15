package com;

import com.cninsure.core.utils.StringUtil;
import com.common.HttpClientUtil;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dai on 2016/6/6.
 */
public class TestWorkflow {

    @Test
    public void testCompletePayTask() {
        Map<String, Object> map1 = new HashMap<String, Object>();

        // 1需要二次支付，0不需要二次支付，2重新核保
        map1.put("issecond", "2");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", map1);
        map.put("userid", "admin");
        map.put("processinstanceid", Long.parseLong("16245477"));
        map.put("taskName", "支付");
        JSONObject jsonObject = JSONObject.fromObject(map);
        Map<String, String> params = new HashMap<String, String>();
        params.put("datas", jsonObject.toString());
        System.out.println(params.toString());
      //测试工作流 http://119.29.64.221:8080/workflow
        String retStr = HttpClientUtil.doGet("http://115.159.237.42:9080/workflow/process/completeSubTask", params);
        System.out.println("调用工作流接口返回信息：" + retStr);
    }

    @Test
    public void testSecondPayTask() {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userid", "admin");
        params.put("taskName","二次支付确认");
        params.put("result", "3");
        params.put("processinstanceid", Integer.parseInt("13583819"));

        JSONObject bo= JSONObject.fromObject(params);
        Map<String,String> requestParams = new HashMap<String,String>();
        requestParams.put("datas", bo.toString());

        String result = HttpClientUtil.doGet("http://115.159.237.42:9080/workflow/process/completeSubTask", requestParams);
        System.out.println("调用工作流接口返回信息：" + result);
    }

    @Test
    public void testCompleteSubTask() {
        Map<String, String> dataMap= new HashMap<String,String>();
        dataMap.put("result", "1");
//        if("平台查询".equals("平台查询")){//通过
//        	dataMap.put("ptisful", "1");//表示直接推送平台查询结束，没有返回平台查询信息
//		}
//        String skipStep = "";
//        if(!StringUtil.isEmpty(skipStep)){
//            dataMap.put("skipSpirit", skipStep);
//        }
        dataMap.put("ptisful", "0");
        dataMap.put("underwriteway", "3");
        Map<String, Object> paramMap= new HashMap<String,Object>();
        paramMap.put("data", dataMap);
        
        paramMap.put("userid", "admin");
        paramMap.put("taskName", "EDI自动核保");
        paramMap.put("processinstanceid", Long.parseLong("1811205"));

        JSONObject jsonObject = JSONObject.fromObject(paramMap);
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("datas", jsonObject.toString());
//测试工作流 http://119.29.64.221:8080/workflow  dev http://10.68.4.104/workflow/  10.68.4.100:8080/workflow  zsc http://115.159.237.232:9080/workflow
        String result = HttpClientUtil.doGet("http://115.159.237.42:9080/workflow/process/completeSubTask", requestParams);
        System.out.println("调用工作流接口返回信息：" + result);

    }

    @Test
    public void testCompleteMainTask() {
        Map<String, String> dataMap= new HashMap<String,String>();
        dataMap.put("result", "1");

        String skipStep = "";
        if(!StringUtil.isEmpty(skipStep)){
            dataMap.put("skipSpirit", skipStep);
        }

        Map<String, Object> paramMap= new HashMap<String,Object>();
        paramMap.put("data", dataMap);
        paramMap.put("userid", "admin");
        paramMap.put("taskName", "精灵承保");
        paramMap.put("processinstanceid", Long.parseLong("14535839"));

        JSONObject jsonObject = JSONObject.fromObject(paramMap);
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("datas", jsonObject.toString());
        //dev http://10.68.4.104/workflow/   zsc  http://115.159.237.232:9080/workflow
        String result = HttpClientUtil.doGet("http://115.159.237.42:9080/workflow/process/completeTask", requestParams);
        System.out.println("调用工作流接口返回信息：" + result);

    }

    @Test
    public void testAbortProcess() {
        Map<String, Object> paramMap= new HashMap<String,Object>();
        paramMap.put("from", "back"); //后台拒绝承保
        paramMap.put("process", "sub");
        paramMap.put("processinstanceid", "1628535");

        JSONObject jsonObject = JSONObject.fromObject(paramMap);
        Map<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("datas", jsonObject.toString());

        String result = HttpClientUtil.doGet("http://115.159.237.42:9080/workflow/process/abortProcessById", requestParams);
        System.out.println("调用工作流接口返回信息：" + result);

    }
}
