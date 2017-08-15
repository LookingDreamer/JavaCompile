package com.zzb.cm.Interface.entity.car.model;

/**
 * 投保区域
 * Created by Administrator on 2015-10-14.
 */
public class InsArea {

    /*省份代码*/
    private String province;
    /*省份名称*/
    private String provinceName;
    /*城市代码*/
    private String city;
    /*城市名称*/
    private String cityName;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
