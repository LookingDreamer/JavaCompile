package com.zzb.mobile.util;

public class AmountUtil {
	public static String trans2Fen(String amountYuan){
		String amount = "0";
		String amounts[] = amountYuan.split("\\.");
		if(amounts.length >= 2){
			amount = amounts[0] + (amounts[1] + "00").substring(0, 2);
		}else if(amounts.length == 1){
			amount = amounts[0] + "00";
		}
		return Integer.valueOf(amount).toString();
	}
	public static String trans2Yuan(String amountFen){
		int length = amountFen.length();
		return amountFen.substring(0, length - 2) + "." + amountFen.substring(length -2);
	}
	
	public static Integer trans2Fen(Double amountYuan){
		return Integer.valueOf(trans2Fen(String.valueOf(amountYuan)));
		
	}
	
	public static Double trans2Yuan(Integer amountFen){
		return Double.valueOf(trans2Yuan(String.valueOf(amountFen)));
	}
}
