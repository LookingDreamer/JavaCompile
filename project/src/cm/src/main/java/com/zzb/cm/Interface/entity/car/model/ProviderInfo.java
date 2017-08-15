package com.zzb.cm.Interface.entity.car.model; /**
 * 
 */

import java.io.Serializable;

/**
 * 供应商信息。可附加上供应商能力描述
 * @author austinChen
 * created at 2015年6月12日 下午1:58:10
 */
public class ProviderInfo implements Serializable {
	
	/**
	 * 序列化编号
	 */
	private static final long serialVersionUID = 1021786890818634594L;
	private String id;
	/*图标路径*/
	private String picUrl;
    /*全称*/
	private String fullName;
    /*昵称*/
	private String nickName;
    /*公司ID*/
	private String comId;
    /*接口类型*/
	private String interfaceType;

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return "ProviderInfo{" +
                "comId='" + comId + '\'' +
                ", id='" + id + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", fullName='" + fullName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", interfaceType='" + interfaceType + '\'' +
                '}';
    }
}
