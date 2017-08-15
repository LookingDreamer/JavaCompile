package com.zzb.mobile.util;

public class TokenMgr {
	public static String generateToken(String jobNumber){
		return EncodeUtils.encodeMd5(jobNumber+String.valueOf(System.currentTimeMillis()));
	}
}
