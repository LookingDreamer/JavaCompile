package com.common;

import java.util.HashMap;
import java.util.Map;

public class PlateUtil {

	
	public final static Map<String, String> data = new HashMap<String, String>();
	static{
		data.put("京","110000");
		data.put("津","120000");
		data.put("冀","130000");
		data.put("晋","140000");
		data.put("蒙","150000");
		data.put("辽","210000");
		data.put("吉","220000");
		data.put("黑","230000");
		data.put("沪","310000");
		data.put("苏","320000");
		data.put("浙","330000");
		data.put("皖","340000");
		data.put("闽","350000");
		data.put("赣","360000");
		data.put("鲁","370000");
		data.put("豫","410000");
		data.put("鄂","420000");
		data.put("湘","430000");
		data.put("粤","440000");
		data.put("桂","450000");
		data.put("琼","460000");
		data.put("渝","500000");
		data.put("川","510000");
		data.put("黔","520000");
		data.put("滇","530000");
		data.put("藏","540000");
		data.put("陕","610000");
		data.put("甘","620000");
		data.put("青","630000");
		data.put("宁","640000");
		data.put("新","650000");
		data.put("台","710000");
		data.put("港","810000");
		data.put("澳","820000");
	}
	
	public String getAreaId(String plate){
		return data.get(plate.substring(0, 1));
	}
}
