package com.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dai on 2015/11/19.
 */
public class VehicleTypeMapper {

    public static final Map<String, Map<String, String>> Mapper = new HashMap<>(10);
    private static final Map<String, String> map1 = new HashMap<>(3);
    private static final Map<String, String> map10 = new HashMap<>(5);
    private static final Map<String, String> map11 = new HashMap<>(5);
    private static final Map<String, String> map12 = new HashMap<>(5);
    private static final Map<String, String> map2 = new HashMap<>(5);
    private static final Map<String, String> map3 = new HashMap<>(4);
    private static final Map<String, String> map4 = new HashMap<>(4);
    private static final Map<String, String> map6 = new HashMap<>(5);
    private static final Map<String, String> map15 = new HashMap<>(4);
    private static final Map<String, String> map16 = new HashMap<>(4);

    static {
        map1.put("1", "6座以下");
        map1.put("2", "6-10座");
        map1.put("3", "10座以上");
        Mapper.put("1", map1);

        map10.put("4", "6座以下");
        map10.put("5", "6-10座");
        map10.put("6", "10-20座");
        map10.put("7", "20-36座");
        map10.put("8", "36座以上");
        Mapper.put("10", map10);

        map11.put("9", "6座以下");
        map11.put("10", "6-10座");
        map11.put("11", "10-20座");
        map11.put("12", "20-36座");
        map11.put("13", "36座以上");
        Mapper.put("11", map11);

        map12.put("14", "2吨以下");
        map12.put("15", "2-5吨");
        map12.put("16", "5-10吨");
        map12.put("17", "10吨以上");
        map12.put("18", "低速载货汽车");
        Mapper.put("12", map12);

        map2.put("23", "6座以下");
        map2.put("24", "6-10座");
        map2.put("25", "10-20座");
        map2.put("26", "20-36座");
        map2.put("27", "36座以上");
        Mapper.put("2", map2);

        map3.put("28", "6-10座");
        map3.put("29", "10-20座");
        map3.put("30", "20-36座");
        map3.put("31", "36座以上");
        Mapper.put("3", map3);

        map4.put("32", "6-10座");
        map4.put("33", "10-20座");
        map4.put("34", "20-36座");
        map4.put("35", "36座以上");
        Mapper.put("4", map4);

        map6.put("36", "2吨以下");
        map6.put("37", "2-5吨");
        map6.put("38", "5-10吨");
        map6.put("39", "10吨以上");
        map6.put("40", "低速载货汽车");
        Mapper.put("6", map6);

        map15.put("41", "特种车型一");
        map15.put("42", "特种车型二");
        map15.put("43", "特种车型三");
        map15.put("44", "特种车型四");
        Mapper.put("15", map15);

        map16.put("19", "特种车型一");
        map16.put("20", "特种车型二");
        map16.put("21", "特种车型三");
        map16.put("22", "特种车型四");
        Mapper.put("16", map16);
    }
}
