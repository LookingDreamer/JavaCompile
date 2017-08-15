package com.zzb.extra.model;

import com.zzb.mobile.model.SearchProviderModel;

/**
 * Created by liwucai on 2016/5/26 9:01.
 */
public class SearchProviderModelForMinizzb extends SearchProviderModel {

    private String channeluserid;//渠道用户id
    private String city;//投保地区
    private String usersource;//minizzb
    private String channelId;

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    public String getUsersource() {
        return usersource;
    }

    public void setUsersource(String usersource) {
        this.usersource = usersource;
    }

    public String getChanneluserid() {
        return channeluserid;
    }

    public void setChanneluserid(String channeluserid) {
        this.channeluserid = channeluserid;
    }


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }


}
