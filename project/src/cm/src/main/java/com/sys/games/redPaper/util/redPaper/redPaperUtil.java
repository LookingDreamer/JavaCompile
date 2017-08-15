package com.sys.games.redPaper.util.redPaper;

import java.util.HashMap;
import java.util.Map;

import com.sys.games.redPaper.util.MoneyUtils;

public class redPaperUtil {
//  private static String _mch_id="1309162501";//商户号
//  private static String _wxappid="wx2d2952cbcbdba520";//商户appid
	
	private static String _mch_id="1355314002";//商户号
  private static String _wxappid="e6MDVhCqABIzWVZv4glQDmJ97sf64XFZ";//商户appid
  private static String _nick_name="掌中保车险";//提供方名称
  private static String _send_name="掌中保车险";//用户名
  private static String _wishing="电影票来啦";//红包祝福语
  private static String _client_ip="127.0.0.1";//ip地址
  private static String _act_name="给您发红包来了";//活动名称
  private static String _remark="全新掌中保";//备注
	
	
  public static String sendRedPaper(String _re_openid,String _total_amount){
	  
	  
	  
	    String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
	    String orderNNo =  MoneyUtils.getOrderNo() ; 
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nonce_str", MoneyUtils.buildRandom());//随机字符串
		map.put("mch_billno",orderNNo);//商户订单
		map.put("mch_id", _mch_id);//商户号
		map.put("wxappid", _wxappid);//商户appid
		map.put("nick_name",_nick_name);//提供方名称
		map.put("send_name",_send_name);//用户名
		map.put("re_openid",_re_openid);//用户openid
		map.put("total_amount", _total_amount);//付款金额
		map.put("total_num", 1);//红包发送总人数
		map.put("wishing",_wishing);//红包祝福语
		map.put("client_ip", _client_ip);//ip地址
		map.put("act_name", _act_name);//活动名称
		map.put("remark", _remark);//备注
		map.put("logo_imgurl","https://wx.gtimg.com/mch/img/ico-logo.png");//图片url
		map.put("share_imgurl","https://wx.gtimg.com/mch/img/ico-logo.png");//分享img
		map.put("sign", MoneyUtils.createSign(map));//签名
		
		String result = "";
		try {
			System.out.println("send_Begin: ");
			result = MoneyUtils.doSendMoney(url, MoneyUtils.createXML(map));
		    System.out.println("send_result: ");
		    System.out.println(MoneyUtils.createXML(map));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("result:"+result);
	  return result;
  }
}
