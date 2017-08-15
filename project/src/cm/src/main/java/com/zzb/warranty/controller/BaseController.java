package com.zzb.warranty.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.exception.DaoException;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.warranty.exception.OrderNotFoundException;
import com.zzb.warranty.exception.QuoteNotExistException;
import com.zzb.warranty.exception.UnauthorizedException;
import com.zzb.warranty.model.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/1/10.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseController {

    @Resource
    private IRedisClient redisClient;

    @ExceptionHandler
    @ResponseBody
    public Response except(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ex.printStackTrace();

        return new Response(Response.STATUS_FAIL, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Response handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        String message = "JSON格式错误或者参数格式错误，请检查确认";
        return new Response(Response.STATUS_FAIL, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Response handleMethodArgumentNotValidException(HttpServletResponse response, MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getDefaultMessage());
            errorMessage.append(", ");
        }
        ex.printStackTrace();
        return new Response(Response.STATUS_FAIL, errorMessage.substring(0, errorMessage.lastIndexOf(",")));
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public Response handleUnauthorizedException(HttpServletResponse response, UnauthorizedException e) {
        return new Response(Response.STATUS_FAIL, "会话超时，请重新登陆");
    }

    @ExceptionHandler(QuoteNotExistException.class)
    @ResponseBody
    public Response handleUnauthorizedException(HttpServletResponse response, QuoteNotExistException e) {
        return new Response(Response.STATUS_FAIL, "报价不存在, 请检查quoteinfoid的值");
    }

    @ExceptionHandler
    @ResponseBody
    public Response except(HttpServletRequest request, HttpServletResponse response, OrderNotFoundException ex) {
        return new Response(Response.STATUS_FAIL, "订单不存在");
    }

    @ExceptionHandler(DaoException.class)
    @ResponseBody
    public Response HandleDatabaseException(DaoException e) {
        e.printStackTrace();
        return new Response(Response.STATUS_FAIL, "数据库异常");
    }

    protected INSBAgent getCurrentAgent(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }

        if (!StringUtils.isBlank(token)) {
            JSONObject jsonObject = JSON.parseObject((String) redisClient.get(Constants.TOKEN, token));
            if (jsonObject == null) {
                throw new UnauthorizedException();
            }

            //TODO 此处直接从request里获取
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

            return agent;
        }

        throw new UnauthorizedException();
    }

}
