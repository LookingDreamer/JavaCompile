package com.zzb.chn.aop;

import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.zzb.chn.util.JsonUtils;
import com.zzb.chn.util.SecurityUtil;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.mobile.model.CommonModel;

import net.sf.json.JSONObject;

/**
 * 第三方安全性校验aop
 */
@Aspect
@Component
public class ThirdSecureAdvice {
	private static ResourceBundle config = ResourceBundle.getBundle("config/config");
	@Resource
    private INSBChannelDao insbChannelDao;

    @Around("within(@org.springframework.stereotype.Controller com.zzb.chn.controller.CHNMerchantController) && @annotation(secureValid)")
    public Object validIdentityAndSecure(ProceedingJoinPoint pjp, SecureValid secureValid) throws Throwable {
        String className = pjp.getTarget().getClass().getSimpleName();
        String method = pjp.getSignature().getName();
        LogUtil.info(className + "." + method +"接口的调用校验开始====");
        Object[] args = pjp.getArgs();
        CommonModel result = new CommonModel();
        
        ServletRequestAttributes t = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        //String channelId = t.getRequest().getHeader("channelId");
        JSONObject jsonObject = JSONObject.fromObject(args[0]);
        String body = JsonUtils.serialize(args[0]);
        String channelId = jsonObject.get("channelId") + "";
        if (StringUtil.isEmpty(channelId)) {
        	channelId = jsonObject.get("channelinnercode") + "";
        }
        
        if (StringUtil.isNotEmpty(channelId)) {
	        INSBChannel cond = new INSBChannel();
	        cond.setChannelinnercode(channelId);
	        cond.setChildflag("1");
	        List<INSBChannel> insbChannels = insbChannelDao.selectList(cond);
	        
	        INSBChannel insbChannel = null;        
	        for (INSBChannel itChannel : insbChannels) {
	    		if ( !"0".equals(itChannel.getDeleteflag()) ) {
	    			insbChannel = itChannel;
	    			break;
	    		}
	    	}
	        if (insbChannel == null) {
	        	result.setStatus(CommonModel.STATUS_FAIL);
	        	result.setMessage("没有找到对应的渠道：" + channelId);
	        	return result;
	        }
        }
        
        String key = config.getString("channel.merchant.key");
        String md5Key = SecurityUtil.md5(body + key);
        String sign = t.getRequest().getHeader("sign");
        LogUtil.info(channelId + "--" + className + "." + method +"接口的调用校验====key:" + key + "传入sign:" + sign + "期望sign:" + md5Key);
        if (StringUtil.isEmpty(sign) || !sign.equals(md5Key)) {
        	result.setStatus(CommonModel.STATUS_FAIL);
        	result.setMessage("非法请求，校验失败");
        	return result;
        }
        
        try {
            Object tmpResult = pjp.proceed();            
            LogUtil.info(className + "." + method +"接口的调用校验完成====");
            return tmpResult;
        } catch (Exception e) {
        	LogUtil.error("请求执行异常：" + e.getMessage());
        	e.printStackTrace();
        	result.setStatus(CommonModel.STATUS_FAIL);
            result.setMessage("服务器异常，请联系管理员");
            return result;
        }
   
    }
}
