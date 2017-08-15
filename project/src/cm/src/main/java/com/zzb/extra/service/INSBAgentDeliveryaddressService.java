package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBAgentDeliveryaddress;

import java.util.List;
import java.util.Map;

public interface INSBAgentDeliveryaddressService extends BaseService<INSBAgentDeliveryaddress> {

    public Map<String, Object> initAgentDeliveryaddressList(Map<String, Object> map);
    public List<Map<Object, Object>> queryAgentDeliveryAddressList(String agentid);

    /**
     * @param id 配置地址id
     * @param agentid 代理人id
      */
    public void setDefaultAgentDeliveryAddress(String id, String agentid);

    public int saveOrUpdateAgentDeliveryaddress(INSBAgentDeliveryaddress agentDeliveryaddress);

}