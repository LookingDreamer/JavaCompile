package com.common;

import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.StringUtil;
import com.qcloud.cosapi.api.CosCloud;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class CosUtils {

	private static String bucketName = ValidateUtil.getConfigValue("cos.bucketName");

	private static CosCloud getCos(){
		int APP_ID = Integer.parseInt(ValidateUtil.getConfigValue("cos.APP_ID"));
		String SECRET_ID = ValidateUtil.getConfigValue("cos.secretID");
		String SECRET_KEY = ValidateUtil.getConfigValue("cos.secretKey");
		return new CosCloud(APP_ID, SECRET_ID, SECRET_KEY,60);
	}

	public static Map<String,String> uploadCosFile(String file,String from) throws Exception{
		Map<String,String> resultMap = new HashMap<String, String>();
		if(StringUtil.isEmpty(file)||file.length()<=0){
			resultMap.put("code", "-1");
			resultMap.put("msg", "上传文件出错,文件为空");
			resultMap.put("url", "");
			return resultMap;
		}
		CosCloud cos = getCos();
		try {
			String checkFilePath = "/" + from + "/" + DateUtil.getCurrentDate() + "/";
			String result = null;
			result = cos.getFolderList(bucketName, checkFilePath, 1, "", 0, CosCloud.FolderPattern.Both);
			if (!"0".equals(getErrorCode(result))) {
				result = cos.createFolder(bucketName, checkFilePath);
				if (!"0".equals(getErrorCode(result))) {
					resultMap.put("code", "-1");
					resultMap.put("msg", "上传文件出错,新建目录出错，请查看连接参数");
					resultMap.put("url", "");
					return resultMap;
				}
			}
			String remotePath = checkFilePath + file.substring(file.lastIndexOf("/") + 1, file.length());
			String localPath = file;
			result = cos.uploadFile(bucketName, remotePath, localPath);
			if (!"0".equals(getErrorCode(result))) {
				resultMap.put("code", "-1");
				resultMap.put("msg", "上传文件出错,上传不成功");
				resultMap.put("url", "");
				return resultMap;
			} else {
				JSONObject json = JSONObject.parseObject(result);
				JSONObject json1 = JSONObject.parseObject(json.getString("data"));
				resultMap.put("code", "0");
				resultMap.put("msg", "上传成功");
				resultMap.put("url", json1.getString("access_url"));
				return resultMap;
			}
		}
		catch(Exception ex)
		{
			resultMap.put("code", "-1");
			resultMap.put("msg", "上传文件出错,异常");
			resultMap.put("url", "");
			return resultMap;
		}
		finally{
			if(cos!=null) {
				// 关闭释放资源
				cos.shutdown();
			}
		}
	}

	public static String getErrorCode(String result){
		return JSONObject.parseObject(result).get("code").toString();
	}
}
