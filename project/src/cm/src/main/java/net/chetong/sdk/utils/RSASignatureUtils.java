/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA签名及验证工具类
 * 
 * @version 1.0
 * @author 温德彬
 * @time 2016年6月28日 下午5:34:23
 * @since JDK 1.7
 */
public class RSASignatureUtils {
	/**
	 * 验签算法
	 */
	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * 加密算法
	 */
	private static final String ENCRYPT_TYPE = "RSA";
	

	/**
	 * RSA签名算法
	 * @author 2016年6月28日 下午5:39:13 温德彬
	 * @param signContent 待签名内容
	 * @param privateKey 私钥秘钥
	 * @param charset 字符类型
	 * @return
	 */
	public static String sign(String signContent, String privateKey, String charset) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Utils.decode(privateKey));

			KeyFactory keyf = KeyFactory.getInstance(ENCRYPT_TYPE);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(signContent.getBytes(charset));

			byte[] signed = signature.sign();

			return Base64Utils.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
	/**
	 * RSA验签算法
	 * @author 2016年6月28日  下午5:41:44  温德彬
	 * @param signContent 待验签内容
	 * @param sign 签名值
	 * @param publicKey 公钥秘钥
	 * @param charset 字符类型
	 * @return
	 */
	public static boolean checkSign(String signContent, String sign, String publicKey, String charset) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPT_TYPE);
			byte[] encodedKey = Base64Utils.decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(signContent.getBytes(charset));

			boolean bverify = signature.verify(Base64Utils.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * RSA签名算法(不带字符类型)
	 * @author 2016年6月28日  下午5:44:50  温德彬
	 * @param signContent 待签名内容
	 * @param privateKey 私钥秘钥
	 * @return
	 */
	public static String sign(String signContent, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Utils.decode(privateKey));

			KeyFactory keyf = KeyFactory.getInstance(ENCRYPT_TYPE);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(signContent.getBytes());

			byte[] signed = signature.sign();

			return Base64Utils.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * RSA验签算法(不带字符类型)
	 * @author 2016年6月28日  下午5:45:38  温德彬
	 * @param signContent 待验签内容
	 * @param sign 签名值
	 * @param publicKey 公钥秘钥
	 * @return
	 */
	public static boolean checkSign(String signContent, String sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPT_TYPE);
			byte[] encodedKey = Base64Utils.decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(signContent.getBytes());

			boolean bverify = signature.verify(Base64Utils.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
