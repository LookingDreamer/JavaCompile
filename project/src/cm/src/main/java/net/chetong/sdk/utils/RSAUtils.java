/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.alibaba.fastjson.JSONObject;

import net.chetong.sdk.enums.ChetongErrorEnum;
import net.chetong.sdk.enums.CommonParamEnum;
import net.chetong.sdk.exception.ChetongApiException;

/**
 * RSA加解密工具类
 * @version 1.0
 * @author 温德彬
 * @time 2016年6月28日  下午4:05:08
 * @since JDK 1.7
 */
public class RSAUtils {
    /**
     * HASH字符（字节转字符）
     */
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    /**
     * 加密算法（默认RSA）
     */
	private static final String ENCRYPT_TYPE="RSA";
	
	/**
	 * RSA最大加密明文大小  
	 */
    private static final int MAX_ENCRYPT_BLOCK = 117;  
      
    /** 
     * RSA最大解密密文大小 
     */  
    private static final int MAX_DECRYPT_BLOCK = 128; 
	
	/**
	 * 通过文件路径加载公钥
	 * @author 2016年6月28日  下午5:20:24  温德彬
	 * @param filePath 公钥所在路径
	 * @return
	 * @throws Exception
	 */
	public static String loadPublicKeyByFile(String filePath) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				sb.append(readLine);
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}
	
	
	/**
	 * 通过字符串获取公钥
	 * @author 2016年6月28日  下午5:21:16  温德彬
	 * @param publicKeyStr 公钥字符串
	 * @return
	 * @throws Exception
	 */
	public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr)
			throws Exception {
		try {
			byte[] buffer = Base64Utils.decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPT_TYPE);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}
	
	/**
	 * 从文件路径中加载私钥
	 * @author 2016年6月28日  下午5:23:25  温德彬
	 * @param filePath 秘钥所在路径
	 * @return
	 * @throws Exception
	 */
	public static String loadPrivateKeyByFile(String filePath) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				sb.append(readLine);
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}
	
	/**
	 * 通过字符串获取私钥
	 * @author 2016年6月28日  下午5:25:13  温德彬
	 * @param privateKeyStr 私钥字符串
	 * @return
	 * @throws Exception
	 */
	public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr)
			throws Exception {
		try {
			byte[] buffer = Base64Utils.decode(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPT_TYPE);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}
	
	/**
	 * RSA公钥加密算法
	 * @author 2016年6月28日  下午5:26:22  温德彬
	 * @param publicKey 公钥秘钥
	 * @param plainTextData 加密字节流
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(ENCRYPT_TYPE);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encryptedData = encryptedLongData(plainTextData, cipher,MAX_ENCRYPT_BLOCK);  
			return encryptedData;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

    /**
     * 对数据分段加密或解密 (防止IllegalBlockSizeException错误) 
     * @author 2016年7月13日  上午10:11:53  温德彬
     * @param plainTextData 加密数据<br>
     * @param cipher 秘钥对象<br>
     * @param maxLength 最大长度<br>
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
	private static byte[] encryptedLongData(byte[] plainTextData, Cipher cipher,int maxLength)
			throws IllegalBlockSizeException, BadPaddingException, IOException {
		int inputLen = plainTextData.length;  
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
		int offSet = 0;  
		byte[] cache;  
		int i = 0;  
		while (inputLen - offSet > 0) {  
		    if (inputLen - offSet > maxLength) {  
		        cache = cipher.doFinal(plainTextData, offSet, maxLength);  
		    } else {  
		        cache = cipher.doFinal(plainTextData, offSet, inputLen - offSet);  
		    }  
		    out.write(cache, 0, cache.length);  
		    i++;  
		    offSet = i * maxLength;  
		}  
		byte[] encryptedData = out.toByteArray();  
		out.close();
		return encryptedData;
	}
	
	/**
	 * RSA私钥加密算法
	 * @author 2016年6月28日  下午5:28:07  温德彬
	 * @param privateKey 私钥秘钥
	 * @param plainTextData 加密字节流
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("加密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(ENCRYPT_TYPE);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] encryptedData = encryptedLongData(plainTextData, cipher,MAX_ENCRYPT_BLOCK);  
			return encryptedData;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * RSA私钥解密算法
	 * @author 2016年6月28日  下午5:29:58  温德彬
	 * @param privateKey 私钥秘钥
	 * @param cipherData 解密字节流
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(ENCRYPT_TYPE);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decryptedData = encryptedLongData(cipherData, cipher,MAX_DECRYPT_BLOCK);  
			return decryptedData;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}
	
	/**
	 * RSA公钥解密算法
	 * @author 2016年6月28日  下午5:31:08  温德彬
	 * @param publicKey 公钥秘钥
	 * @param cipherData 解密字节流
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("解密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(ENCRYPT_TYPE);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] decryptedData = encryptedLongData(cipherData, cipher,MAX_DECRYPT_BLOCK);  
			return decryptedData;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}


    /**
     * 字节流转字符串
     * @author 2016年6月28日  下午5:32:08  温德彬
     * @param data 转换的字节流
     * @return
     */
	public static String byteArrayToString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
			if (i < data.length - 1) {
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}  

	/**
	 * 对参数先进行RSA加密再进行签名
	 * @author 2016年6月29日  下午2:29:07  温德彬
	 * @param paramMap 参数MAP 
	 * @param ctPublicKey 公钥秘钥
	 * @param privateKey 私钥秘钥
	 * @param charset 加密方式
	 * @return
	 * @throws Exception 
	 */
	public static Map<String,Object> encryptAndSign(Map<String, Object> paramMap,String ctPublicKey,String privateKey) throws ChetongApiException{
		//业务参数
		Map<String,Object> businessParamMap = new HashMap<String,Object>();
		//结果参数
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<String> codeList = CommonParamEnum.getCodeList();
		//只对业务参数进行加密
		for (Entry<String, Object>  map : paramMap.entrySet()) {
			if(!codeList.contains(map.getKey())){
				businessParamMap.put(map.getKey(), map.getValue());
			}else{
				resultMap.put(map.getKey(), map.getValue());
			}
		}
		//对要加密的业务参数进行字典序排序
		String sortParams = sortParams(businessParamMap);
		byte[] plainTextData;
		try {
			plainTextData = sortParams.getBytes(CommonParamEnum.CHARSET.getDefaultValue());
			//加载公钥
			RSAPublicKey rsaPublicKey = loadPublicKeyByStr(ctPublicKey);
			//车童公钥加密
			byte[] cipherData = encrypt(rsaPublicKey, plainTextData);
			String cipher=Base64Utils.encode(cipherData);
			//加密结果放在最终结果
			resultMap.put(CommonParamEnum.BIZ_DATA.getCode(), cipher);
			//客户私钥签名
			String otherSortParams = sortParams(resultMap);
			String signstr=RSASignatureUtils.sign(otherSortParams,privateKey);
			resultMap.put(CommonParamEnum.SIGN.getCode(), signstr);
		} catch (Exception e) {
			throw new ChetongApiException(ChetongErrorEnum.ENCRYPT_SIGN_FAILED.getCode(),e.getMessage());
		}
		
		return resultMap;
	}
	/**
	 * 对服务传过来的参数进行验签并解密
	 * @author 2016年6月29日  下午3:48:01  温德彬
	 * @param params 参数对象
	 * @param publicKey 车童公钥
	 * @param privateKey 客户私钥
	 * @param isDecrypt 是否解密
	 * @return
	 * @throws ChetongApiException 
	 */
	public static String checkSignAndDecrypt(Map<String, Object> params, String publicKey, String privateKey, boolean isDecrypt) throws ChetongApiException{
		//获取公有参数
		String bizData = (String)params.get(CommonParamEnum.BIZ_DATA.getCode());
		String sign = (String)params.get(CommonParamEnum.SIGN.getCode());
		//删除参数中的签名
		params.remove(CommonParamEnum.SIGN.getCode());
		//对结果进行字典序排列
		String sortParams = sortParams(params);
		String charset = (String)params.get(CommonParamEnum.CHARSET.getCode());
		//对结果进行车童公钥验签
		boolean isCheck = RSASignatureUtils.checkSign(sortParams, sign, publicKey, charset);
		//对结果进行客户私钥解密
		if(!isCheck){
			throw new ChetongApiException(ChetongErrorEnum.CHECK_SIGN_FAILED.getCode(),ChetongErrorEnum.CHECK_SIGN_FAILED.getErrMsg());
		}
		if(isDecrypt){
			try {
				//加载私钥
				RSAPrivateKey rsaPrivateKey = loadPrivateKeyByStr(privateKey);
				//客户私钥解密
				byte[] res=RSAUtils.decrypt(rsaPrivateKey, Base64Utils.decode(bizData));
				return new String(res,charset);
			} catch (Exception e) {
				throw new ChetongApiException(ChetongErrorEnum.ENCRYPT_SIGN_FAILED.getCode(),e.getMessage());
			}
		}
		return bizData;
		
	}
	
	/**
	 * 对Map数据进行字典排序
	 * @author 2016年6月29日  下午2:40:20  温德彬
	 * @param sortParams
	 * @return
	 */
	public static String sortParams(Map<String,Object> sortParams){
		JSONObject jsonObj = new JSONObject(sortParams.size(), true);
	    List<String> codes = new ArrayList<String>(sortParams.keySet());
	    Collections.sort(codes);
	    for (String code : codes) {
	    	jsonObj.put(code, sortParams.get(code));
		}
	    return jsonObj.toJSONString();
	} 

}
