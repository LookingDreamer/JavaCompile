package com.zzb.ldap;

import java.security.MessageDigest;

public class LdapMd5 {

	private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
			.toCharArray();
	private static byte[] codes = new byte[256];

	static {
		for (int i = 0; i < 256; i++)
			codes[i] = -1;
		for (int i = 65; i <= 90; i++)
			codes[i] = ((byte) (i - 65));
		for (int i = 97; i <= 122; i++)
			codes[i] = ((byte) (26 + i - 97));
		for (int i = 48; i <= 57; i++)
			codes[i] = ((byte) (52 + i - 48));
		codes[43] = 62;
		codes[47] = 63;
	}

	private static char[] encode(byte[] data) {
		char[] out = new char[(data.length + 2) / 3 * 4];

		int i = 0;
		for (int index = 0; i < data.length; index += 4) {

			int val = 0xFF & data[i];
			val <<= 8;
			if (i + 1 < data.length) {
				val |= 0xFF & data[(i + 1)];
			}
			val <<= 8;
			if (i + 2 < data.length) {
				val |= 0xFF & data[(i + 2)];
			}
			out[(index + 3)] = alphabet[(val & 0x3F)];
			val >>= 6;
			out[(index + 2)] = alphabet[(val & 0x3F)];
			val >>= 6;
			out[(index + 1)] = alphabet[(val & 0x3F)];
			val >>= 6;
			out[index] = alphabet[(val & 0x3F)];

			i += 3;
		}
		out[out.length - 1] = alphabet[64];
		out[out.length - 2] = alphabet[64];
		return out;
	}

	private static byte[] decode(char[] data) {
		int tempLen = data.length;
		for (int ix = 0; ix < data.length; ix++) {
			if ((data[ix] > 'ÿ') || (codes[data[ix]] < 0)) {
				tempLen--;
			}

		}

		int len = tempLen / 4 * 3;
		if (tempLen % 4 == 3)
			len += 2;
		if (tempLen % 4 == 2)
			len++;

		byte[] out = new byte[len];

		int shift = 0;
		int accum = 0;
		int index = 0;

		for (int ix = 0; ix < data.length; ix++) {
			int value = data[ix] > 'ÿ' ? -1 : codes[data[ix]];

			if (value >= 0) {
				accum <<= 6;
				shift += 6;
				accum |= value;
				if (shift >= 8) {
					shift -= 8;
					out[(index++)] = ((byte) (accum >> shift & 0xFF));
				}

			}

		}

		if (index != out.length) {
			throw new Error("Miscalculated data length (wrote " + index
					+ " instead of " + out.length + ")");
		}

		return out;
	}
	/**
	 * ldap使用md5加密
	 * @param password
	 * @return
	 */
	public static String Md5Encode(String password) {
		try {
			MessageDigest sha = MessageDigest.getInstance("MD5");
			sha.reset();
			sha.update(password.getBytes());
			byte[] pwhash = sha.digest();
			char[] cb = encode(pwhash);
			return "{MD5}"+new String(cb);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 比较ldap中MD5加密密码和字符串密码
	 * @param digest 		{MD5}ISMvKXpXpadDiUoOSoAfww==
	 * @param password 		admin
	 * @return
	 */
	public static boolean verifyPassword(String digest, String password){
		try {
			MessageDigest sha = MessageDigest.getInstance("MD5");
			sha.reset();
			sha.update(password.getBytes());
			byte[] pwhash = sha.digest();
			if (digest.regionMatches(true, 0, "{MD5}", 0, 5)) {
			      digest = digest.substring(5);
			}
			byte[] hash = decode(digest.toCharArray());
			return MessageDigest.isEqual(hash, pwhash);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static void main(String[] args) {
		System.out.println(Md5Encode("111"));
	}
}
