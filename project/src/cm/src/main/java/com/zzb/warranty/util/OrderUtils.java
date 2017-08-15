package com.zzb.warranty.util;

import java.util.Date;

/**
 * Created by Administrator on 2017/1/16.
 */
public class OrderUtils {

    private static final String PATTERN = "yyyyMMddHHmmss";

    public static String createOrderNo(String jobNum) {
        return String.format("%s-%s", jobNum, DateUtils.format(new Date(), PATTERN));
    }
}
