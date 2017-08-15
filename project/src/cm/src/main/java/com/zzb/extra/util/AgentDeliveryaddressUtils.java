package com.zzb.extra.util;

import com.zzb.extra.entity.INSBAgentDeliveryaddress;

/**
 * Created by liwucai on 2016/5/21 16:07.
 */
public class AgentDeliveryaddressUtils {

    /**
     * 复制 sourceAddress 属性值到 targetAddress 属性。
     * @param sourceAddress
     * @param targetAddress
     * @return
     */
    public static INSBAgentDeliveryaddress copySouceToTarget(INSBAgentDeliveryaddress sourceAddress, INSBAgentDeliveryaddress targetAddress){
        if(sourceAddress!=null){
            if(sourceAddress.getRecipientname()!=null && !"".equals(sourceAddress.getRecipientname())){
                targetAddress.setRecipientname(sourceAddress.getRecipientname());
            }
            if(sourceAddress.getRecipientmobilephone()!=null && !"".equals(sourceAddress.getRecipientmobilephone())){
                targetAddress.setRecipientmobilephone(sourceAddress.getRecipientmobilephone());
            }
            if(sourceAddress.getRecipientprovince()!=null && !"".equals(sourceAddress.getRecipientprovince())){
                targetAddress.setRecipientprovince(sourceAddress.getRecipientprovince());
            }
            if(sourceAddress.getRecipientcity()!=null && !"".equals(sourceAddress.getRecipientcity())){
                targetAddress.setRecipientcity(sourceAddress.getRecipientcity());
            }
            if(sourceAddress.getRecipientarea()!=null && !"".equals(sourceAddress.getRecipientarea())){
                targetAddress.setRecipientarea(sourceAddress.getRecipientarea());
            }
            if(sourceAddress.getRecipientaddress()!=null && !"".equals(sourceAddress.getRecipientaddress())){
                targetAddress.setRecipientaddress(sourceAddress.getRecipientaddress());
            }
            if(sourceAddress.getZip()!=null && !"".equals(sourceAddress.getZip())){
                targetAddress.setZip(sourceAddress.getZip());
            }
            if(sourceAddress.getAgentid()!=null && !"".equals(sourceAddress.getAgentid())){
                targetAddress.setAgentid(sourceAddress.getAgentid());
            }
            if(sourceAddress.getDelivetype()!=null && !"".equals(sourceAddress.getDelivetype())){
                targetAddress.setDelivetype(sourceAddress.getDelivetype());
            }
            if(sourceAddress.getIsdefault()!=null && !"".equals(sourceAddress.getIsdefault())){
                targetAddress.setIsdefault(sourceAddress.getIsdefault());
            }
        }
        return targetAddress;
    }
}
