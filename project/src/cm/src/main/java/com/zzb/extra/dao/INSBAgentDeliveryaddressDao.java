package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBAgentDeliveryaddress;

import java.util.List;
import java.util.Map;

public interface INSBAgentDeliveryaddressDao extends BaseDao<INSBAgentDeliveryaddress> {

    public List<Map<Object, Object>> selectAgentDeliveryaddressListPaging(Map<String, Object> data);
    public List<Map<Object, Object>> queryAgentDeliveryAddressList(String agentid);

    /** 按代理人id修改是否 默认配送地址 */
    public int updateIsdefaultByAgentid(String agentid,String isdefault);

    /** 按配送地址id修改是否 默认配送地址 */
    public int updateIsdefaultById(String id, String isdefault);


    public int addAgentDeliveryaddress(INSBAgentDeliveryaddress agentDeliveryaddress);
    
    public long selectCountDeliveryaddress(Map<String, Object> map);
}