/**
 * 
 */
package com.zzb.mobile.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.cninsure.core.utils.LogUtil;
import org.apache.http.ParseException;
import org.jivesoftware.smack.util.StringUtils;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.HttpClientUtil;

import net.sf.json.JSONObject;

/**
 * @author chengxt
 * @Version 1.0
 * @Company 力天卓越
 * @CreateDate 2016年1月28日
 */
public class DownloadWxFile {


	private static String ACCESS_TOKEN_URL;
	private static String DOWNLOAD_URL;
	private static String PUBLIC_USER_ID;

	static {
		String url = ValidateUtil.getConfigValue("wx.access.token.url");
		if (StringUtil.isNotEmpty(url)) {
			ACCESS_TOKEN_URL = url;
		}
		url = ValidateUtil.getConfigValue("wx.download.url");
		if (StringUtil.isNotEmpty(url)) {
			DOWNLOAD_URL = url;
		}
		url = ValidateUtil.getConfigValue("wx.public.user.id");
		if (StringUtil.isNotEmpty(url)) {
			PUBLIC_USER_ID = url;
		}
	}

	/**
	 * 下载微信图片
	 * 
	 * @param media_id
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public static String download(String media_id, String filepath, String filename)
			throws ParseException, UnsupportedEncodingException, IOException {
		return download(PUBLIC_USER_ID, media_id, filepath, filename);
	}

	/**
	 * 下载微信图片
	 * 
	 * @param publicUserId
	 * @param media_id
	 * @param filepath
	 * @param filename
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	public static String download(String publicUserId, String media_id, String filepath, String filename)
			throws ParseException, UnsupportedEncodingException, IOException {
		String savepath = null;
		String access_token = getAccessToken(publicUserId);
		if (StringUtil.isNotEmpty(access_token)) {
			Map<String, String> params = new TreeMap<String, String>();
			params.put("access_token", access_token);
			params.put("media_id", media_id);
			savepath = HttpClientUtil.downloadFile(DOWNLOAD_URL, params, filepath, filename);
		}
		return savepath;
	}

	/**
	 * @param publicUserId
	 * @return
	 */
	private static String getAccessToken(String publicUserId) {
		String access_token = null;
		String jsonStr = HttpClientUtil.doGet(ACCESS_TOKEN_URL + "/" + PUBLIC_USER_ID, null, null);
		if (StringUtils.isNotEmpty(jsonStr)) {
			JSONObject jsonObj = JSONObject.fromObject(jsonStr);
			if (jsonObj.has("access_token")) {
				access_token = jsonObj.getString("access_token");
			}
		}
		LogUtil.info("http url 获取token , url = " + ACCESS_TOKEN_URL + "/" + PUBLIC_USER_ID + " ,返回token = " + access_token);
		return access_token;
	}

}
