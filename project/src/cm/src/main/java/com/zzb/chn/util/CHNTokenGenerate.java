package com.zzb.chn.util;

import org.apache.commons.lang3.RandomStringUtils;

public class CHNTokenGenerate {
	public  static String generateToken(){
		return RandomStringUtils.randomAlphanumeric(32);
	}
	public static void main(String[] args){
		for(int i = 0; i < 100; i++){
			System.out.println("qd_" + RandomStringUtils.randomAlphanumeric(16) + " " + RandomStringUtils.randomAlphanumeric(32));
		}
	}
}
