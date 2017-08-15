package com.zzb.warranty.controller;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/16.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String start = "2016-01-01";
        String end = "2017-02-02";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        int result = getMonthSpace(sdf.parse(start), sdf.parse(end));
        System.out.println(result);

    }
    private static int getMonthSpace(Date startDate, Date endDate) throws Exception {
        if (startDate.after(endDate)) {
            throw new Exception("开始日期不能晚于结束日期");
        }

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(startDate);
        c2.setTime(endDate);

        int years = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        int month = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        int day = c2.get(Calendar.DAY_OF_MONTH) - c1.get(Calendar.DAY_OF_MONTH);

        return years * 12 + month + (day > 0 ? 1 : 0);
    }
}
