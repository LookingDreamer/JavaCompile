package com.zzb.cm.Interface.controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.jobpool.dispatch.DispatchTaskService;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCUserService;
import com.cninsure.system.service.OnlineService;
import com.common.HttpClientUtil;
import com.common.redis.IRedisClient;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/openfire/*")
public class OpenfireServerController extends BaseController { 
	@Resource
	private IRedisClient redisClient;
    private static final String ONLINE = "cm:zzb:online_user";
    
    @Resource OnlineService onlineService ;
    
    @Resource DispatchTaskService dispatchService;
    
    @Resource INSCUserService inscUserService; 
    
    /**
	 * change status to busy online offline
	 * @throws Exception
	 */
	@RequestMapping(value = "onlineusersetStatus", method = RequestMethod.GET)
	@ResponseBody
	public void onlineusersetStatus(@RequestParam(value="status") String status,@RequestParam(value="usercode") String usercode) throws Exception{
		// 得到系统用户发送消息
		INSCUser fromUser = inscUserService.getByUsercode("admin");
		INSCUser toUser = inscUserService.getByUsercode(usercode);
		dispatchService.sendXmppMessage4User(fromUser, toUser, status);//发送消息通知openfire变更状态
		onlineService.changeOnlinestatus(usercode, Integer.parseInt(status));//通知调度处理
	}
	
	/**
	 * queryUsersetStatus
	 * @throws Exception
	 */
	@RequestMapping(value = "queryUsersetStatus", method = RequestMethod.GET)
	@ResponseBody
	public String queryUsersetStatus(@RequestParam(value="usercode") String usercode) throws Exception{
		// 得到系统用户发送消息
		INSCUser toUser = inscUserService.getByUsercode(usercode);
		JSONObject obj = new JSONObject();
		obj.put("type", toUser.getOnlinestatus());
		return obj.toString();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "onlineusers", method = RequestMethod.GET)
	@ResponseBody
	public void onlineusers(@RequestParam(value="jabber") String jabber,@RequestParam(value="status") String status,@RequestParam(value="usercode") String usercode,@RequestParam(value="secret") String secret) throws Exception{
		String newsecret = getResult(jabber+"@"+status);
//		http://119.29.111.199:9090/plugins/presence/status?jid=zhangdi@car.zzb&type=text
		if(newsecret.equals(secret)){
			if("onLine".equals(status)){
				redisClient.set(ONLINE, usercode,	 jabber);
				LogUtil.info("--登入---usercode:" + usercode + "jabberid:" + jabber + ",时间：" + LocalDateTime.now());
				onlineService.changeOnlinestatus(jabber.split("@")[0], 1);
			}else{
				String pluginUrl = "http://" + ValidateUtil.getConfigValue("fairy.host") + ":9090/plugins/presence/status?jid=" + usercode + "@" + ValidateUtil.getConfigValue("fairy.serviceName") + "&type=text";
//				String pluginUrl = "http://" + "119.29.111.199" + ":9090/plugins/presence/status?jid=" + usercode + "@" + "car.zzb" + "&type=text";
				String result = HttpClientUtil.doGet(pluginUrl, null);
				if(StringUtil.isNotEmpty(result)&&result.contains("Unavailable")){
					redisClient.del(ONLINE, usercode);
					LogUtil.info("--用户退出---usercode:" + usercode + "jabberid:" + jabber + ",时间：" + LocalDateTime.now());
					onlineService.changeOnlinestatus(jabber.split("@")[0], 0);
				}
				
			}
		}
		
	}
	public static  String  getResult(String inputStr)
    {
        BigInteger bigInteger=null;

        try {
         MessageDigest md = MessageDigest.getInstance("MD5");   
         byte[] inputData = inputStr.getBytes(); 
         md.update(inputData);
         bigInteger = new BigInteger(md.digest()); 
        } catch (Exception e) {e.printStackTrace();}
        return bigInteger.toString(16);
    }
}
