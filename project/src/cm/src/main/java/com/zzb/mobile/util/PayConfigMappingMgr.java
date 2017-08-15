package com.zzb.mobile.util;

import java.io.IOException;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class PayConfigMappingMgr {
	//private Resource configLocation="classpath:config//sqlMapConfig.xml";
	private static Document doc;

	public static Document getDoc() {
		return doc;
	}

	public static String getElements(String mapType) {
		List<Element> nodeList = doc.selectNodes("/configuration/confMapping/" + mapType);
		StringBuffer buf = new StringBuffer("");
		for(Element e : nodeList) {
			buf.append(e.asXML());
		}
		return buf.toString();
	}

	static{
		Resource res = new ClassPathResource("config/PayConfMap.xml");
		SAXReader read =new SAXReader();
		try {
			doc = read.read(res.getInputStream());
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}
	public static String getPayUrl(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/payUrl");
		Node base = doc.selectSingleNode("/configuration/baseSetting/basePayUrl");
		if(node!=null && base!=null)
			return base.getText()+node.getText();
		return null;
	}
	public static String getRefundUrl(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/refundUrl");
		Node base = doc.selectSingleNode("/configuration/baseSetting/basePayUrl");
		if(node!=null && base!=null)
			return base.getText()+node.getText();
		return null;
	}

	public static String getRefundmentUrl(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/refundmentUrl");
		Node base = doc.selectSingleNode("/configuration/baseSetting/basePayUrl");
		if(node!=null && base!=null)
			return base.getText()+node.getText();
		return null;
	}

	public static String getRobotCallbackUrl(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/robotCallbackUrl");
		Node base = doc.selectSingleNode("/configuration/baseSetting/basePayUrl");
		if(node!=null && base!=null)
			return base.getText()+node.getText();
		return null;
	}

	public static String getSecondPayCallbackUrl(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/secondPayCallbackUrl");
		Node base = doc.selectSingleNode("/configuration/baseSetting/basePayUrl");
		if(node!=null && base!=null)
			return base.getText()+node.getText();
		return null;
	}
	
	public static String getPayNoticeUrl(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/payNoticeUrl");
		if(node!=null)
			return node.getText();
		return null;
	}
	
	public static String getSignKey(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/signkey");
		if(node!=null)
			return node.getText();
		return null;
	}
	public static String getPaySource(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/paySource");
		if(node!=null)
			return node.getText();
		return null;
	}
	public static String getQueryResultUrl(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/queryResult");
		Node base = doc.selectSingleNode("/configuration/baseSetting/basePayUrl");
		if(node!=null && base!=null)
			return base.getText()+node.getText();
		return null;
	}
	public static String getFlowUrl(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/flowUrl");
		if(node!=null)
			return node.getText();
		return null;
	}
	public static String getPayKeyAttributes(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/keyAttributes");
		if(node!=null)
			return node.getText();
		return null;
	}
	public static String getChannelPayUrl(){
		Node node = doc.selectSingleNode("/configuration/baseSetting/channelPayUrl");
		if(node!=null)
			return node.getText();
		return null;
	}
	public static String getPayCodeByCmCode(String mapType,String cmCode){
		Node node = doc.selectSingleNode("/configuration/confMapping/"+mapType+"/map[@cmvalue='"+cmCode+"']");
//		Node node = doc.selectSingleNode("/configuration/confMapping/bankCardMapping/map[@cmvalue="+cmCode+"]");
		if(node!=null)
			return node.getText();
		return null;
	}
	public static String getCmCodeByPayCode(String mapType,String payCode){
		Element nodes =(Element) doc.selectSingleNode("/configuration/confMapping/"+mapType+"/map[text()='"+payCode+"']");
		if(nodes!=null)
			return nodes.attributeValue("cmvalue");
		return null;
	}
	public static String getPayNameByCmCode(String mapType,String cmCode){
		Attribute node = (Attribute)doc.selectSingleNode("/configuration/confMapping/"+mapType+"/map[@cmvalue='"+cmCode+"']/@typename");
		if(node!=null)
			return node.getText();
		return null;
	}
	public static String getCmCodeByPayCode(String mapType, String payCode, String typeCode) {
		List<Element> nodeList = doc.selectNodes("/configuration/confMapping/"+mapType+"/map[text()='"+payCode+"']");
		for(Element e : nodeList) {
			if(typeCode!=null && typeCode.equals(e.attributeValue("typecode"))) {
				return e.attributeValue("cmvalue");
			}
		}
		if (nodeList.size()>0)
			return nodeList.get(0).attributeValue("cmvalue");
		return null;
	}
	public static void main(String arg[]) throws IOException{
		System.out.println(PayConfigMappingMgr.getCmCodeByPayCode(MappingType.PAY_CHANNEL,"bestpay"));
	}
}
