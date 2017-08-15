package com.zzb.conf.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBPrvaccountkey;



@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBPrvaccountkeyDaoXMLTest{
	@Resource
	private INSBPrvaccountkeyDao dao;
	
	@Test
	public void testInsert(){
		aa("鼎和","361a3d50c56b11e56348d5e588cbe7da");
		aa("利宝","cc589ee0c59f11e5501aa00a0d84772e");
		aa("平安","372a33a0c5a011e5159edd30719ea3be");
		aa("太保","1333b330c5a111e5242b07f6bf590ff5");
		aa("天平","5a163700c5a111e57470e193dd7bbc2b");
		aa("阳光","84c026a0c5a111e526fbff89afe27203");
		aa("永城","b8841aa0c5a111e5b58e6876624b8689");
		aa("长安","ef42ff20c5a111e5c34c2fd315fed2e4");
		aa("中华联合","079f6a90c5a211e50d86804cf0d23904");
		
	}
	
	private void aa(String aa,String id){
		List<INSBPrvaccountkey> list=new ArrayList<INSBPrvaccountkey>();
		
		SAXReader reader = new SAXReader();  
		Document document = null;
		 try {
			document = reader.read(new File("E:/study/My-FH-workspace/cm/src/test/resources/xml/"+aa+".xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}  
		 Element node = document.getRootElement();  
		 listNodes(list,node,id);
		 
		 for(INSBPrvaccountkey model:list){
			 System.out.println(model);
		 }
		 dao.insertInBatch(list);
	}
	public void listNodes(List<INSBPrvaccountkey> modelList,Element node,String id) {  
//        System.out.println("当前节点的名称：：" + node.getName());  
        // 获取当前节点的所有属性节点  
        List<Attribute> list = node.attributes();  
        for (Attribute attr : list) {  
        	INSBPrvaccountkey model = new INSBPrvaccountkey();
        	if("desc".equals(attr.getName())){
              	model.setNoti(attr.getValue());
//              	System.out.println("======="+attr.getValue());
              }
            if("name".equals(attr.getName())){
            	model.setParamname(attr.getValue());
            	model.setParamvalue(node.getText());
            	 model.setManagerid(id);
             	model.setCreatetime(new Date());
             	model.setOperator("admin");
            }
            if(model.getCreatetime()!=null){
            	System.out.println(model.getNoti());
            	 modelList.add(model);
            } 
           
           
        }  
  
        if (!(node.getTextTrim().equals(""))) {  
//            System.out.println("文本内容：：：：" + node.getText());  
        }  
  
        // 当前节点下面子节点迭代器  
        Iterator<Element> it = node.elementIterator();  
        // 遍历  
        while (it.hasNext()) {  
            // 获取某个子节点对象  
            Element e = it.next();  
            // 对子节点进行遍历  
            listNodes(modelList,e,id);  
        }  
    }  
	
	
}