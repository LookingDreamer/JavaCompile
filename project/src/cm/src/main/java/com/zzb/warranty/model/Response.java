package com.zzb.warranty.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Administrator on 2017/1/10.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";

    public static final String MESSAGE_SUCCESS = "操作成功";
    public static final String MESSAGE_FAIL = "操作失败";

    private String status;
    private String message;
    private Object body;

    public Response(){}

    public Response(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(String status, String message, Object body) {
        this(status, message);
        this.body = body;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
