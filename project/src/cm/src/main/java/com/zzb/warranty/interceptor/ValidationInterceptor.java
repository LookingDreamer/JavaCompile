package com.zzb.warranty.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.warranty.exception.UnauthorizedException;
import com.zzb.warranty.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by Administrator on 2017/1/11.
 */
public class ValidationInterceptor extends HandlerInterceptorAdapter {

    Logger logger = Logger.getLogger(ValidationInterceptor.class);

    @Resource
    private IRedisClient redisClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        logger.info(String.format("延保API请求[%s] %s, 请求内容：%s",
                request.getMethod(), request.getRequestURI(), request.toString()));

        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String[] paths = {
                "warranty/coupon/start",
                "warranty/payment/callback"
        };

        String uri = request.getRequestURI();
        for (String path : paths) {
            if (uri.contains(path)) {
                return true;
            }
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Parameter[] parameters = method.getParameters();
        //从header中得到token
        String token = request.getHeader("token");

        logger.info("当前token为" + token);
        if (!StringUtils.isBlank(token)) {
            //验证token
            String insbAgentString = (String) redisClient.get(Constants.TOKEN, token);
            logger.info("代理信息：" + insbAgentString);
            JSONObject jsonObject = JSON.parseObject(insbAgentString);
            if (jsonObject == null) {
                throw new UnauthorizedException();
            }

            // 拦截非广东地区代理
            String guangdongCode = "440000";
            String provinceCode = jsonObject.getString("province");

            if (!guangdongCode.equals(provinceCode) && !uri.contains("warranty/quote/multiplequote")) {
                throw new RuntimeException("延保产品当前只对广东地区代理开放");
            }

            // 检测客户端时间
            String clientTimestamp = request.getHeader("client_timestamp");
            if (!StringUtils.isBlank(clientTimestamp)) {
                long current = System.currentTimeMillis();
                try {
                    long clientTime = DateUtils.parse(clientTimestamp, DateUtils.pattern1).getTime();
                    if (Math.abs(current - clientTime) > 5 * 60 * 1000) {
                        throw new RuntimeException("客户端时间不正确，请调整时间后继续使用");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("客户端时间不正确，请调整时间后继续使用");
                }
            }

            INSBAgent agent = new INSBAgent();
            agent.setJobnum(jsonObject.getString("jobNum"));
            agent.setAgentcode(agent.getJobnum());
            agent.setName(jsonObject.getString("name"));
            agent.setDeptid(jsonObject.getString("deptid"));
            agent.setPlatformcode(jsonObject.getString("platformcode"));
            agent.setMobile(jsonObject.getString("mobile"));
            agent.setPhone(jsonObject.getString("phone"));
            agent.setComname(jsonObject.getString("comname"));
            agent.setJobnumtype(jsonObject.getInteger("jobNumType"));
            agent.setAgentkind(jsonObject.getInteger("agentKind"));

            //如果token验证成功，将token对应的用户id存在request中，便于之后注入
            request.setAttribute("current_agent_code", agent.getAgentcode());
            request.setAttribute("current_agent", agent);

            return true;
        }
        throw new UnauthorizedException();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
