package com.zzb.chn.util;

import java.security.MessageDigest;
import java.util.ResourceBundle;

import com.cninsure.core.utils.LogUtil;

public final class SecurityUtil {
    private SecurityUtil() {}
    private static ResourceBundle config = ResourceBundle.getBundle("config/config");

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
        	LogUtil.error("md5加密字符串[" + str + "]出现异常：" + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
    	String body = "{\"insureAreaCode\":\"441900\",\"channelId\":\"qd_9fEX3SLlVsYme3JY\"}";
    	String key = config.getString("channel.merchant.key");
    	System.out.println(key);
    	System.out.println(md5(body + key));
    }

}
