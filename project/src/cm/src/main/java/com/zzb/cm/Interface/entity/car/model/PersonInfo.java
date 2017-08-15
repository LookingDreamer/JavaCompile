package com.zzb.cm.Interface.entity.car.model; /**
 *
 */

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzb.cm.Interface.entity.model.IdCardType;
import com.zzb.cm.Interface.entity.model.SexType;

/**
 * 人员信息
 *
 * @author austinChen
 *         created at 2015年6月15日 下午1:35:20
 */
public class PersonInfo implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 6482393019408603644L;

    /**
     * 中文姓名
     */
    private String name;

    /**
     * 英文名
     */
    private String enName;
    //0男1女
    private Integer sex;
    /*居住地址*/
    private String address;
    /*证件类型*/
    private Integer idCardType;
    /*证件号*/
    private String idCard;
    /*手机号码*/
    private String mobile;
    /*电话号码*/
    private String phone;
    /*电子邮箱*/
    private String email;
    /*民族*/
    private String national;
    /*出生日期*/
    private Date birthday;
    /*邮编*/
    private String postCode;

    /*杂项*/
    private Map<String, String> misc = new HashMap<String, String>();

    private String id;

    /*创建时间*/
    private Date created;
    /*更新时间*/
    private Date updated;


    public Date getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(Integer idCardType) {
		this.idCardType = idCardType;
	}

	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }


    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Map<String, String> getMisc() {
        return misc;
    }

    public void setMisc(Map<String, String> misc) {
        this.misc = misc;
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
