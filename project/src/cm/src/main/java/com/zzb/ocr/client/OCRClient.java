package com.zzb.ocr.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Component;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.HttpClientUtil;
import com.zzb.ocr.bean.DrivingEntity;
import com.zzb.ocr.bean.DrivingResult;
import com.zzb.ocr.bean.IDCardEntity;
import com.zzb.ocr.bean.IDCardResult;

/**
 * 影像识别客户端
 * 
 * @author chengxt
 * @Version 1.0
 * @Company 力天卓越
 * @CreateDate 2016年1月7日
 */
@Component("ocrClient")
public class OCRClient {

	private String DRIVING_URL = "http://182.254.230.28:8080/ocr_driving/SrvXMLAPI";// 识别API的URL
	private String IDCARD_URL = "http://182.254.230.28:8080/ocr_idcard/SrvXMLAPI";// 识别API的URL
	private final String OCR_TYPE_IDCARD = "idcard";// 识别类型 身份证
	private final String OCR_TYPE_DRIVING = "driving";// 识别类型 驾驶证

	/**
	 * 构造方法初始化地址参数
	 */
	public OCRClient() {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
		String url = resourceBundle.getString("ocr.driving.url");
		if (StringUtil.isNotEmpty(url)) {
			DRIVING_URL = url;
		}
		url = resourceBundle.getString("ocr.idcard.url");
		if (StringUtil.isNotEmpty(url)) {
			IDCARD_URL = url;
		}
	}

	/**
	 * 行驶证识别
	 * 
	 * @param drivingPage
	 * @return
	 * @throws IOException
	 * @throws JAXBException
	 */
	public DrivingEntity ocrDriving(String drivingPage) throws IOException, JAXBException {
		LogUtil.info("行驶证识别开始，识别文件路径：" + drivingPage);
		DrivingEntity entity = null;
		File file = new File(drivingPage);
		if (file.isFile()) {
			String xml = getSendXML(OCR_TYPE_DRIVING, drivingPage.substring(drivingPage.lastIndexOf(".") + 1));
			String result = send(xml, file2byte(file), DRIVING_URL);
			LogUtil.info("行驶证识别返回结果：" + result);
			if (result.indexOf("OK") > 0) {
				DrivingResult icr = readXml(DrivingResult.class, result);
				if (StringUtil.isNotEmpty(icr.getError())) {
					LogUtil.warn("行驶证识别错误，错误代码：" + icr.getError());
				} else if (icr.getData() != null) {
					entity = icr.getData();
				}
			}
		}

		return entity;
	}

	/**
	 * 身份证识别，支持正反面识别
	 * 
	 * @param idcardPage
	 * @return
	 * @throws IOException
	 * @throws JAXBException
	 */
	public IDCardEntity ocrIdCard(String idcardPage) throws IOException, JAXBException {
		LogUtil.info("身份证识别开始，识别文件路径：" + idcardPage);
		IDCardEntity entity = null;
		File file = new File(idcardPage);
		if (file.isFile()) {
			String xml = getSendXML(OCR_TYPE_IDCARD, idcardPage.substring(idcardPage.lastIndexOf(".") + 1));
			String result = send(xml, file2byte(file), IDCARD_URL);
			LogUtil.info("身份证识别返回结果：" + result);
			if (result.indexOf("OK") > 0) {
				IDCardResult icr = readXml(IDCardResult.class, result);
				if (StringUtil.isNotEmpty(icr.getError())) {
					LogUtil.warn("身份证识别错误，错误代码：" + icr.getError());
				} else if (icr.getData() != null) {
					entity = icr.getData();
				}
			}
		}

		return entity;
	}

	/**
	 * 生成发送的xml文件
	 * 
	 * @param action
	 * @param ext
	 * @return
	 */
	private String getSendXML(String action, String ext) {
		ArrayList<String[]> arr = new ArrayList<String[]>();
		arr.add(new String[] { "action", action });
		arr.add(new String[] { "ext", ext });
		return getXML(arr, false);
	}

	/**
	 * 生成xml
	 * 
	 * @param arr
	 * @param IsUpper
	 * @return
	 */
	private String getXML(ArrayList<String[]> arr, boolean IsUpper) {
		if (arr == null || arr.size() == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		String tag = "";
		for (int idx = 0; idx < arr.size(); idx++) {
			tag = arr.get(idx)[0];
			if (IsUpper) {
				tag = tag.toUpperCase();
			}
			sb.append("<");
			sb.append(tag);
			sb.append(">");
			sb.append(arr.get(idx)[1]);
			sb.append("</");
			sb.append(tag);
			sb.append(">");
		}
		return sb.toString();
	}

	/**
	 * 发送请求
	 * 
	 * @param xml
	 * @param file
	 * @param url
	 * @return
	 */
	private String send(String xml, byte[] file, String url) {
		byte[] dest = new byte[xml.getBytes().length + file.length + "<file></file>".getBytes().length];
		int pos = 0;
		System.arraycopy(xml.getBytes(), 0, dest, pos, xml.getBytes().length);
		pos += xml.getBytes().length;
		System.arraycopy("<file>".getBytes(), 0, dest, pos, "<file>".getBytes().length);
		pos += "<file>".getBytes().length;
		System.arraycopy(file, 0, dest, pos, file.length);
		pos += file.length;
		System.arraycopy("</file>".getBytes(), 0, dest, pos, "</file>".getBytes().length);
		return HttpClientUtil.doPostBytes(url, dest);
	}

	/**
	 * 文件转换成byte
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private byte[] file2byte(File file) throws IOException {
		byte[] bytes = null;
		if (file != null) {
			InputStream is = new FileInputStream(file);
			int length = (int) file.length();
			if (length > Integer.MAX_VALUE) {// 当文件的长度超过了int的最大值
				System.out.println("this file is max ");
			}
			bytes = new byte[length];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			// 如果得到的字节长度和file实际的长度不一致就可能出错了
			if (offset < bytes.length) {
				System.out.println("file length is error");
			}
			is.close();
		}
		return bytes;
	}

	/**
	 * 结果转换成类
	 * 
	 * @param clazz
	 * @param content
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	private <T> T readXml(Class<T> clazz, String content) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller u = jc.createUnmarshaller();
		return (T) u.unmarshal(new StringReader(content.replace(new String(new byte[] { 13 }), "")));
	}

}
