package com.zzb.mobile.util;

import com.cninsure.core.tools.util.ValidateUtil;
import com.sys.games.redPaper.util.MoneyUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HaiJuan.Lei on 2016/10/11.
 * 发送红包测试类
 */
public class SendRedPacketTest {

    final static  String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
    public static void main(String[] args) {

        String wishing = ValidateUtil.getConfigValue("wx.marketing.wishword");
        String client_ip = ValidateUtil.getConfigValue("wx.marketing.clientip");
        String act_name = ValidateUtil.getConfigValue("wx.marketing.activityname");
        String remark = ValidateUtil.getConfigValue("wx.marketing.remark");

        String orderNNo =  MoneyUtils.getOrderNo() ;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nonce_str", MoneyUtils.buildRandom());//随机字符串
        map.put("mch_billno",orderNNo);//商户订单
        map.put("mch_id", "1355314002");//商户号
        map.put("wxappid", "wx89e8deb4c5d6b90a");//商户appid
        map.put("nick_name", "掌中保");//提供方名称
        map.put("send_name", "掌中保");//用户名
        map.put("re_openid", "oHD-Es_8kNsF21IK67VThC-zKDU4");//用户openid
        map.put("total_amount", 100);//付款金额
        map.put("total_num", 1);//红包发送总人数
        map.put("wishing",wishing);//红包祝福语
        map.put("client_ip", client_ip);//ip地址
        map.put("act_name", act_name);//活动名称
        map.put("remark", remark);//备注
//        map.put("wishing", "新年快乐");//红包祝福语
//        map.put("client_ip", "127.0.0.1");//ip地址
//        map.put("act_name", "掌中保过年派红包活动");//活动名称
//        map.put("remark", "新年新气象");//备注
        map.put("sign", MoneyUtils.createSign(map));//签名

        String result = "";
        try {
            result = MoneyUtils.doSendMoney(url, MoneyUtils.createXML(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result:"+result);
    }
}
