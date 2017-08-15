package com.zzb.warranty.model;

import com.cninsure.core.dao.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class INSEPerson extends BaseModel implements Identifiable {
    private static final long serialVersionUID = 1L;

    private String orderId;

    private boolean samecarowner;

    /**
     * 姓名
     */
    private String name;

    /**
     * 英文名
     */
    private String ename;

    /**
     * 性别
     */
    private Integer gender;

    /**
     *
     */
    private Integer age;

    private Date birthday;

    private String idcardtype;

    /**
     * 证件号码
     */
    private String idcardno;

    /**
     * 手机号
     */
    private String cellphone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 省
     */
    private Integer province;

    /**
     * 城
     */
    private Integer city;

    /**
     * 区县
     */
    private Integer area;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮编
     */
    private String zip;

    /**
     *
     */
    private String educateinfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIdcardno() {
        return idcardno;
    }

    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEducateinfo() {
        return educateinfo;
    }

    public void setEducateinfo(String educateinfo) {
        this.educateinfo = educateinfo;
    }

    public boolean isSamecarowner() {
        return samecarowner;
    }

    public void setSamecarowner(boolean samecarowner) {
        this.samecarowner = samecarowner;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdcardtype() {
        return idcardtype;
    }

    public void setIdcardtype(String idcardtype) {
        this.idcardtype = idcardtype;
    }
}