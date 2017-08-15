package com.zzb.mobile.service.impl;

import com.alibaba.fastjson.JSON;
import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.sys.games.redPaper.util.MoneyUtils;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.mobile.dao.*;
import com.zzb.mobile.entity.INSBAgentMarketingNew;
import com.zzb.mobile.entity.INSBRefereeStatisticInfo;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.marketing.RankingModel;
import com.zzb.mobile.service.AppMarketingActivitiesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by HaiJuan.Lei on 2016/10/9.
 * 营销活动的具体实现
 */
@Service
@Transactional
public class AppMarketingActivitiesServiceImpl implements AppMarketingActivitiesService {

    final static  String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

    @Resource
    private INSBAgentService insbAgentService;

    @Resource
    private INSBAgentMarketingNewDao insbAgentMarketingNewDao;

    @Resource
    private INSBMarketingPersonalRankInfoModelDao insbMarketingPersonalRankInfoModelDao;

    @Resource
    private INSBPersonalLotteryCodeInfoDao insbPersonalLotteryCodeInfoDao;

    @Resource
    private INSBRefereeStatisticInfoDao insbRefereeStatisticInfoDao;

    @Resource
    private INSBAgentDao insbAgentDao;

    @Resource
    private INSBCoreAgentDao insbCoreAgentDao;


    @Override
    /**
     * 获取到认证成功的消息
     * 获取到注册成功的消息
     * 检验代理人是否有资格参加营销活动
     * @param agentCode 被推荐人工号
     * @param referee 推荐人工号
     * @Param tempAgentRegTime 临时工号注册时间
     * */
    public CommonModel isCanParticipateMarketing(String agentCode ,String referee,Date tempAgentRegTime) {
        CommonModel result = new CommonModel();
        result.setStatus("success");
        result.setMessage("success");

        //判断是否可以参加参加营销活动
        LogUtil.info("被推荐人："+ agentCode +"参与营销推荐活动");
        String startDate = ValidateUtil.getConfigValue("wx.marketing.startdate");
        String endDate = ValidateUtil.getConfigValue("wx.marketing.enddate");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟

        if(null != startDate && null!= endDate  ){
            Date now = new Date();
            String dateStr = sdf.format(now);//绑定的日期

            String temRegDate = sdf.format(tempAgentRegTime);
            int coreSameAgent = insbCoreAgentDao.getAgentCnt(agentCode);
            int redPaperNum = insbAgentMarketingNewDao.isSendRedPaper(agentCode);

            if(!StringUtil.isEmpty(referee) ){
                INSBAgent refereeAgent = insbAgentDao.selectByJobnum(referee);

                INSBAgent tempAgent = insbAgentDao.selectByJobnum(agentCode);
                Date createTimeOfAgent  = tempAgent.getCreatetime();
                LogUtil.info("被推荐人的核心正式工号生成时间是：" + createTimeOfAgent);

                //试用工号的注册时间在活动范围内
                //被推荐人的正式工号的生成时间createtime>=2016-10-17
                //认证成功的时间在活动范围内
                //推荐人不为空
                if(refereeAgent != null && dateStr.compareTo(startDate) > 0 && dateStr.compareTo(endDate) < 0
                        && sdf.format(createTimeOfAgent).compareTo(startDate)> 0
//                    && regDate.compareTo(startDate)> 0  && regDate.compareTo(endDate) < 0
                        && temRegDate.compareTo(startDate)> 0  && temRegDate.compareTo(endDate) < 0
                        && refereeAgent.getAgentkind() == 2 && refereeAgent.getJobnumtype() == 2
                        && refereeAgent.getApprovesstate() == 3
                        && !(coreSameAgent > 0)
                        && !(redPaperNum> 0)
                        ){
                    //认证成功，且认证成功的时间在活动范围内，且注册时间在活动范围内，且代理人工号不在核心代理人工号范围内 并且 推荐人不为空,参与发红包活动
                    LogUtil.info("被推荐人："+ agentCode +" ,推荐人：" + referee +"参与推荐活动");
                    CommonModel model = registerSuccess(agentCode);
                    System.out.println("model = " + model.getStatus());
                }else{
                    LogUtil.info("认证代理人：" +agentCode +"不在营销活动时间范围内,不参与活动，或者推荐人已获得红包 ：" + redPaperNum + " ,或者核心已有该代理人：" + coreSameAgent );
                    result.setStatus("fail");
                    result.setMessage("注册代理人：" + agentCode + "不在营销活动时间范围内,不参与活动");
                }

            }else
                LogUtil.error("营销活动的时间范围没有设置，新注册工号" +agentCode+"不参与活动,推荐人工号为空");
        }else{
            LogUtil.error("营销活动的时间范围没有设置，新注册工号" +agentCode+"不参与活动");
            result.setStatus("fail");
            result.setMessage("营销活动的时间范围没有设置，新注册工号" +agentCode+"不参与活动");
        }
        return result;
    }

    @Override
    /**
     * 获取到认证成功的消息
     * 1、保存数据
     * 2、发红包*/
    public CommonModel registerSuccess(String agentCode){
        CommonModel result = new CommonModel();
        result.setStatus("success");
        result.setMessage("success");



        INSBAgent queryAgent = null;
        if(!StringUtil.isEmpty(agentCode) ){
            INSBAgent agent = new INSBAgent();
            agent.setJobnum(agentCode);
            queryAgent = insbAgentService.queryOne(agent);
        }else{
            result.setStatus("fail");
            result.setMessage("代理人工号为空");
            return result;
        }
        String activityName = "";// 可配置
        INSBAgentMarketingNew agentNew = new INSBAgentMarketingNew();
        agentNew.setAgentcode(agentCode);
        agentNew.setDeptid(queryAgent.getDeptid());
        agentNew.setOpenid(queryAgent.getOpenid());
        agentNew.setSex(queryAgent.getSex());
        agentNew.setName(queryAgent.getName());
        agentNew.setJobnum(agentCode);
        agentNew.setPhone(queryAgent.getPhone());
        agentNew.setRegistertime(new Date());
        agentNew.setReferee(queryAgent.getReferrer());
//        agentNew.setRegistertime(queryAgent.getRegistertime());
        agentNew.setIdno(queryAgent.getIdno());
        agentNew.setIdnotype(queryAgent.getIdnotype());
        agentNew.setIdnotype(queryAgent.getIdnotype());
        agentNew.setActivityname(activityName);
        agentNew.setCreatetime(new Date());
        agentNew.setModifytime(new Date());
        agentNew.setIssentredpacket("false");
        agentNew.setInsbagentid(queryAgent.getId());
        insbAgentMarketingNewDao.insert(agentNew);

        INSBRefereeStatisticInfo refereeStatisticInfo = insbRefereeStatisticInfoDao.findByAgentCode(queryAgent.getAgentcode());
        if(null != refereeStatisticInfo ){ //更新
            INSBRefereeStatisticInfo refereeInfo = new INSBRefereeStatisticInfo();
            refereeInfo.setName(queryAgent.getName());
            refereeInfo.setAgentcode(queryAgent.getAgentcode());
            refereeInfo.setReferrals(refereeStatisticInfo.getReferrals() + 1);
            refereeInfo.setModifytime(new Date());
            refereeInfo.setId(refereeStatisticInfo.getId());
            insbRefereeStatisticInfoDao.updateInfo(refereeInfo);
        }else{ //插入新的记录
            INSBRefereeStatisticInfo refereeInfoNew = new INSBRefereeStatisticInfo();
            refereeInfoNew.setName(queryAgent.getName());
            refereeInfoNew.setAgentcode(queryAgent.getAgentcode());
            refereeInfoNew.setReferrals(1);
            refereeInfoNew.setCreatetime(new Date());
            refereeInfoNew.setModifytime(new Date());
            insbRefereeStatisticInfoDao.insert(refereeInfoNew);
        }

        String referee =  queryAgent.getReferrer(); //推荐人工号
        if(!StringUtil.isEmpty(referee) ){
            INSBAgent agent = new INSBAgent();
            agent.setJobnum(referee);
            INSBAgent queryReferee = insbAgentService.queryOne(agent);
            if( null != queryReferee ){
                //发红包，满足条件：1) 推荐人有关注车险掌中保公众号 2)被推荐人
                if(null != queryReferee.getOpenid() && !"".equals(queryReferee.getOpenid())
//                        && insbHasPolicyAgentDao.getAgentCnt(referee) > 0
                        ){
                    String wishing = ValidateUtil.getConfigValue("wx.marketing.wishword");
                    String client_ip = ValidateUtil.getConfigValue("wx.marketing.clientip");
                    String act_name = ValidateUtil.getConfigValue("wx.marketing.activityname");
                    String remark = ValidateUtil.getConfigValue("wx.marketing.remark");
                    int amount =  Integer.valueOf(ValidateUtil.getConfigValue("wx.marketing.amount"));
                    LogUtil.info("红包-wishing:"+wishing);
                    LogUtil.info("红包-client_ip:"+client_ip);
                    LogUtil.info("红包-act_name:"+act_name);
                    LogUtil.info("红包-remark:"+remark);
                    LogUtil.info("红包-amount:"+amount);
                    String orderNNo =  MoneyUtils.getOrderNo() ;
                    LogUtil.info("给推荐人发红包，订单号为：" + orderNNo);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("nonce_str", MoneyUtils.buildRandom());//随机字符串
                    map.put("mch_billno",orderNNo);//商户订单
                    map.put("mch_id", "1355314002");//商户号
                    map.put("wxappid", "wx89e8deb4c5d6b90a");//商户appid
                    map.put("nick_name", "掌中保");//提供方名称
                    map.put("send_name", "掌中保");//用户名
                    map.put("re_openid", queryReferee.getOpenid());//用户openid
                    map.put("total_amount", amount);//付款金额
                    map.put("total_num", 1);//红包发送总人数
                    map.put("wishing",wishing);//红包祝福语
                    map.put("client_ip", client_ip);//ip地址
                    map.put("act_name", act_name);//活动名称
                    map.put("remark", remark);//备注
                    map.put("sign", MoneyUtils.createSign(map));//签名


                   boolean flag=false;
                    try {
                        flag=MoneyUtils.doSendMoney(url, map);
                    }
                    catch (Exception ex)
                    {
                        LogUtil.error("发送红包出现异常！url:"+url+",data:"+ JSON.toJSONString(map));
                        LogUtil.error(ex);
                    }
                    if(flag){
                        // 更新推荐人红包状态
                        List<INSBAgentMarketingNew> agentList =insbAgentMarketingNewDao.getAgentInfo(agentCode);
                        if(agentList != null && agentList.size() > 0){
                            for(int i = 0;  i< agentList.size() ; i++){
                                String id = agentList.get(i).getId();
                                LogUtil.info("发红包成功，更新发红包状态，表id = " + id);
                                INSBAgentMarketingNew aNew = new INSBAgentMarketingNew();
                                aNew.setAgentcode(agentCode);
                                aNew.setReferee(referee);
                                aNew.setIssentredpacket("true");
                                aNew.setNoti("true");
                                aNew.setModifytime(new Date());
                                aNew.setId(id);
                                insbAgentMarketingNewDao.update(aNew);
                            }
                        }
                    }else{
                        List<INSBAgentMarketingNew> agentList = insbAgentMarketingNewDao.getAgentInfo(agentCode);
                        for(int i = 0; i< agentList.size() ; i++){
                            String id = agentList.get(i).getId();
                            LogUtil.info("发红包失败，更新发红包状态，表id = " + id );
                            INSBAgentMarketingNew aNew = new INSBAgentMarketingNew();
                            aNew.setAgentcode(agentCode);
                            aNew.setReferee(referee);
                            aNew.setIssentredpacket("false");
                            aNew.setNoti("false");
                            aNew.setModifytime(new Date());
                            aNew.setId(id);
                            insbAgentMarketingNewDao.update(aNew);
                        }
                    }
                    System.out.println("注册代理人成功发红包的结果result flag:" + flag);
                }
            }
        }
        return result;
    }

    /**
     * 获取历史的推荐人数排行榜情况
     * */
    public RankingModel getRankingList(String agentCode){
        RankingModel result = new RankingModel();
        result.setLotteryCodeCnt(0);//累计发放有效抽奖码数
        int showNum = Integer.valueOf(ValidateUtil.getConfigValue("market.myRecommend.showNum"));
        result.setMyLotteryInfo(insbMarketingPersonalRankInfoModelDao.getNewMylotteryInfo(agentCode));
        result.setRankList(insbMarketingPersonalRankInfoModelDao.getNewRank(showNum));
        result.setStatus("success");
        result.setMessage("success");
        return result;
    }

    /**
     * 获取当周的推荐人数排行榜情况
     * */
    public RankingModel getWeekRankingList(String agentCode){
        int showNum = Integer.valueOf(ValidateUtil.getConfigValue("market.myRecommend.showNum"));
        RankingModel result = new RankingModel();
        Map<String,Object> params=new HashMap<>();
        params.put("start",getMondayOfThisWeek());
        params.put("end", getSundayOfThisWeek());
        params.put("limit", showNum);
        result.setLotteryCodeCnt(0);
        result.setRankList(insbMarketingPersonalRankInfoModelDao.getWeekRank(params));
        params.put("agentcode", agentCode);
        result.setMyLotteryInfo(insbMarketingPersonalRankInfoModelDao.getWeekMylotteryInfo(params));
        result.setStatus("success");
        result.setMessage("success");
        return result;
    }


    /**
     * 获取推荐人的被推荐人信息
     * */
    @Override
    public List<INSBAgentMarketingNew> getReferrals(String agentCode) {
        return insbAgentMarketingNewDao.getReferrals(agentCode);
    }

    @Override
    public void updateAgentRedPacketStatus(String agentCode, String referee) {
        // 更新推荐人红包状态
        List<INSBAgentMarketingNew> agentList =insbAgentMarketingNewDao.getReferrals(referee);
        if(agentList != null && agentList.size() > 0){
            for(int i = 0;  i< agentList.size() ; i++){
                String id = agentList.get(i).getId();
                LogUtil.info("发红包成功，更新发红包状态，表id = " + id );
                INSBAgentMarketingNew aNew = new INSBAgentMarketingNew();
                aNew.setAgentcode(agentCode);
                aNew.setReferee(referee);
                aNew.setIssentredpacket("true");
                aNew.setNoti("true");
                aNew.setModifytime(new Date());
                aNew.setId(id);
                insbAgentMarketingNewDao.update(aNew);
            }
        }
    }


    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return df.format(c.getTime());
    }

    /**
     * 得到本周周日(下周周一零点的日期)
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 8);
        return df.format(c.getTime());
    }

    public static void main(String[] args)
    {
        System.out.println("1111111=" + getMondayOfThisWeek());
        System.out.println("22=" + getSundayOfThisWeek());
    }
}
