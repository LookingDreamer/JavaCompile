package com.zzb.extra.util;

import com.zzb.conf.entity.INSBAgent;

/**
 * Created by liwucai on 2016/5/6 9:13.
 */
public class AgentUtils {

    /**
     * 复制 sourceAgent 属性值到 targetAgent 属性。
     * @param sourceAgent
     * @param targetAgent
     * @return
     */
    public static INSBAgent copySouceToTarget(INSBAgent sourceAgent, INSBAgent targetAgent){
        if(sourceAgent!=null){
            if(sourceAgent.getName()!=null && !"".equals(sourceAgent.getName())){
                targetAgent.setName(sourceAgent.getName());
            }
            if(sourceAgent.getNickname()!=null && !"".equals(sourceAgent.getNickname())){
                targetAgent.setNickname(sourceAgent.getNickname());
            }
            if(sourceAgent.getSex()!=null && !"".equals(sourceAgent.getSex())){
                targetAgent.setSex(sourceAgent.getSex());
            }
            if(sourceAgent.getEmail()!=null && !"".equals(sourceAgent.getEmail())){
                targetAgent.setEmail(sourceAgent.getEmail());
            }
            if(sourceAgent.getAgentcode()!=null && !"".equals(sourceAgent.getAgentcode())){
                targetAgent.setAgentcode(sourceAgent.getAgentcode());
            }
            if(sourceAgent.getMobile()!=null && !"".equals(sourceAgent.getMobile())){
                targetAgent.setMobile(sourceAgent.getMobile());
            }
            if(sourceAgent.getMobilephone1use()!=null ){
                targetAgent.setMobilephone1use(sourceAgent.getMobilephone1use());
            }
            if(sourceAgent.getMobile2()!=null && !"".equals(sourceAgent.getMobile2())){
                targetAgent.setMobile2(sourceAgent.getMobile2());
            }
            if(sourceAgent.getMobilephone2use()!=null ){
                targetAgent.setMobilephone2use(sourceAgent.getMobilephone2use());
            }
            if(sourceAgent.getTelnum()!=null && !"".equals(sourceAgent.getTelnum())){
                targetAgent.setTelnum(sourceAgent.getTelnum());
            }
            if(sourceAgent.getIstest()!=null ){
                targetAgent.setIstest(sourceAgent.getIstest());
            }
            if(sourceAgent.getReferrer()!=null && !"".equals(sourceAgent.getReferrer())){
                targetAgent.setReferrer(sourceAgent.getReferrer());
            }
            if(sourceAgent.getBankcard()!=null && !"".equals(sourceAgent.getBankcard())){
                targetAgent.setBankcard(sourceAgent.getBankcard());
            }
            if(sourceAgent.getUsersource()!=null && !"".equals(sourceAgent.getUsersource())){
                targetAgent.setUsersource(sourceAgent.getUsersource());
            }
            if(sourceAgent.getRegistertime()!=null ){
                targetAgent.setRegistertime(sourceAgent.getRegistertime());
            }
            if(sourceAgent.getTesttime()!=null ){
                targetAgent.setTesttime(sourceAgent.getTesttime());
            }
            if(sourceAgent.getIdno()!=null && !"".equals(sourceAgent.getIdno())){
                targetAgent.setIdno(sourceAgent.getIdno());
            }
            if(sourceAgent.getIdnotype()!=null && !"".equals(sourceAgent.getIdnotype())){
                targetAgent.setIdnotype(sourceAgent.getIdnotype());
            }
            if(sourceAgent.getFirstlogintime()!=null){
                targetAgent.setFirstlogintime(sourceAgent.getFirstlogintime());
            }
            if(sourceAgent.getApprovesstate()!=null){
                targetAgent.setApprovesstate(sourceAgent.getApprovesstate());
            }
            if(sourceAgent.getLivingcityid()!=null && !"".equals(sourceAgent.getLivingcityid())){
                targetAgent.setLivingcityid(sourceAgent.getLivingcityid());
            }
            if(sourceAgent.getLsatlogintime()!=null ){
                targetAgent.setLsatlogintime(sourceAgent.getLsatlogintime());
            }
            if(sourceAgent.getCompression()!=null ){
                targetAgent.setCompression(sourceAgent.getCompression());
            }
            if(sourceAgent.getRenewaltime()!=null ){
                targetAgent.setRenewaltime(sourceAgent.getRenewaltime());
            }
            if(sourceAgent.getBelongs2bank()!=null && !"".equals(sourceAgent.getBelongs2bank())){
                targetAgent.setBelongs2bank(sourceAgent.getBelongs2bank());
            }
            if(sourceAgent.getAddress()!=null && !"".equals(sourceAgent.getAddress())){
                targetAgent.setAddress(sourceAgent.getAddress());
            }
            if(sourceAgent.getNoti()!=null && !"".equals(sourceAgent.getNoti())){
                targetAgent.setNoti(sourceAgent.getNoti());
            }
            if(sourceAgent.getAgentstatus()!=null){
                targetAgent.setAgentstatus(sourceAgent.getAgentstatus());
            }
            if(sourceAgent.getWeixinheadphotourl()!=null){
                targetAgent.setWeixinheadphotourl(sourceAgent.getWeixinheadphotourl());
            }

            if(sourceAgent.getRecommendtype()!=null && !"".equals(sourceAgent.getRecommendtype())){
                targetAgent.setRecommendtype(sourceAgent.getRecommendtype());
            }
            if(sourceAgent.getReferrerid()!=null && !"".equals(sourceAgent.getReferrerid())){
                targetAgent.setReferrerid(sourceAgent.getReferrerid());
            }

            /*if(sourceAgent.getPushChannel()!=null && !"".equals(sourceAgent.getPushChannel())){
                targetAgent.setPushChannel(sourceAgent.getPushChannel());
            }//推广渠道新增之后，不修改
            if(sourceAgent.getPushWay()!=null && !"".equals(sourceAgent.getPushWay())){
                targetAgent.setPushWay(sourceAgent.getPushWay());
            }//推广方式新增之后，不修改 */

        }
        return targetAgent;
    }

}
