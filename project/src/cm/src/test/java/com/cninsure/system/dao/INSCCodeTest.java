package com.cninsure.system.dao;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCCode;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.model.AppCodeModel;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSCCodeTest {
	@Resource
	private INSCCodeDao dao;
	
	@Test
	public void insertTest3(){
		INSCCode code = new INSCCode();
		
		code.setId(UUIDUtils.random());
		code.setCreatetime(new Date());
		code.setOperator("1");
		code.setCodetype("tasktype");
		code.setParentcode("tasktype");
		code.setNoti("任务类型");

		code.setCodevalue("h");
		code.setCodename("认证任务");
		code.setCodeorder(60);
		
		
	
		dao.insert(code);
	}
	

	@Test
	public void insertTest2(){
		INSCCode code = new INSCCode();
		code.setId(UUIDUtils.random());
		code.setCreatetime(new Date());
		code.setOperator("qxl");
		code.setCodetype("agentnoti");
		code.setParentcode("agentnoti");
		code.setNoti("代理人备注");

		code.setCodevalue("4");
//		code.setCodename("指定第一受益人备注");
//		code.setCodename("修改车辆信息备注");
//		code.setCodename("客户信息备注");
		code.setCodename("指定起保日期");
//		code.setCodename("其它事宜备注");
		code.setCodeorder(4);
		code.setProp1(1);//0   flase
		
		dao.insert(code);
	}
	
	@Test
	public void getBankListTest(){
		INSCCode result = dao.selectBankList("009");
		System.out.println(result);
	}
	
	/**
	 * 工作流订单状态对应关系
	 */
	@Test
	public void insertSex(){
		INSCCode code = new INSCCode();
		code.setCreatetime(new Date());
		code.setOperator("1");
		code.setCodetype("contract");
		code.setParentcode("processAuto");
		code.setNoti("协议-流程自动化-承保");

		code.setCodevalue("33");
		code.setCodename("人工");
		code.setCodeorder(3);
		
		dao.insert(code);
	}
	
	@Test
	public void insertCode(){
//		影像类型	agentidcardpositive	insuranceimage	43	代理人身份证正面照	43
//		影像类型	agentidcardopposite	insuranceimage	44	代理人身份证背面照	44
//		影像类型	agentbankcard	insuranceimage	45	银行卡正面照	45
//		影像类型	agentqualification	insuranceimage	46	资格证正面照	46

		
		INSCCode code = new INSCCode();
		code.setCreatetime(new Date());
		code.setOperator("admin");
		
		code.setCodetype("agentqualification");
		code.setParentcode("insuranceimage");
		code.setNoti("影像类型");

		code.setCodevalue("46");
		code.setCodename("资格证正面照");
		code.setCodeorder(46);
		
		dao.insert(code);
	}
	
	@Test
	public void selectCodeValueByCodeNameTest(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("codetype", "workflowNodelName");
		map.put("codename", "EDI报价");
		String codeValue = dao.selectCodeValueByCodeName(map);
		System.out.println(codeValue);
	}
	
	@Test
	public void selectByTyps(){
		List<String> list = new ArrayList<String>(); 
		list.add("itemtype");
		list.add("notdeductible");
		list.add("kindtype");
		list.add("orderstatus");
		List<AppCodeModel> listcode = dao.selectByTypes(list);
		
		for(AppCodeModel model:listcode){
			System.out.println(model);
		}
	}

	/**
	 * 工作流订单状态对应关系
	 */
	@Test
	public void insertTest1(){
		INSCCode code = new INSCCode();
		code.setCreatetime(new Date());
		code.setOperator("1");
		code.setCodetype("workflowNodelName");
		code.setParentcode("workflowNodelName");
		code.setNoti("工作流节点状态码对应-订单状态");

		code.setCodevalue("38");
		code.setCodename("轮询");
		code.setProp2("轮询");
		code.setCodeorder(175);
		code.setId(UUIDUtils.random());
		
		dao.insert(code);
	}
	
	@Test
	public void selectByOrderStatusTest(){
		System.out.println(dao.selectByOrderStatus("1"));
	}
	
	/**
	 * 维护工作流节点状态
	 */
	@Test
	public void insertTest() {
		
		
		INSCCode code1 = new INSCCode();
		
		code1.setCreatetime(new Date());
		code1.setOperator("1");
		code1.setCodetype("tasktype");
		code1.setParentcode("tasktype");
		code1.setNoti("任务类型");

		
		code1.setCodevalue("i");
		code1.setCodename("打单");
		code1.setCodeorder(57);
		code1.setId(UUIDUtils.random());
		
		
		dao.insert(code1);
		
	}
	
	/**
	 * 维护 添加精灵能力选项"平台"
	 */
	@Test
	public void insertAutoConfigTypeCodeTest() {
		/*INSCCode code1 = new INSCCode();
		code1.setCreatetime(new Date());
		code1.setOperator("1");
		code1.setCodetype("conftype");
		code1.setParentcode("conftype");
		code1.setNoti("自动化配置类型");
		code1.setCodevalue("05");
		code1.setCodename("平台");
		code1.setCodeorder(5);
		
		dao.insert(code1);*/
	}
	
	/**
	 * 添加代理人 等级 无 ，V1-V9
	 */
	@Test
	public void insertAgentLevelTest() {
		
		/*INSCCode code1 = new INSCCode();
		
		for(int i=0;i<10;i++){
			code1.setCreatetime(new Date());
			code1.setOperator("1");
			code1.setCodetype("agentlevel");
			code1.setParentcode("agentlevel");
			code1.setNoti("代理人等级");
			
			String j=Integer.toString(i);
			code1.setCodevalue(j);
			code1.setCodename("V"+j);
			code1.setCodeorder(i+1);
			dao.insert(code1);
		}*/
	}
	
	public static void main(String[] args) {
		List<INSBAgreement> agreementlist = new ArrayList<INSBAgreement>();
		
		INSBAgreement m1 = new INSBAgreement();
		m1.setAgreementname("张不");
		
		INSBAgreement m2 = new INSBAgreement();
		m2.setAgreementname("按张3");
		
		INSBAgreement m3 = new INSBAgreement();
		m3.setAgreementname("张按");
		
		agreementlist.add(m1);
		agreementlist.add(m2);
		agreementlist.add(m3);
		
		for(INSBAgreement model:agreementlist){
			System.out.println(model.getAgreementname());
		}
		System.out.println("=----------------------------------------------------------------");
		
		
		Comparator<INSBAgreement> cmp = new Comparator<INSBAgreement>() {
			Comparator<Object> cmp = Collator.getInstance(java.util.Locale.CHINA);
			@Override
			public int compare(INSBAgreement o1, INSBAgreement o2) {
				if(cmp.compare(o1.getAgreementname(), o2.getAgreementname())>0){
					return 1;
				}else if(cmp.compare(o1.getAgreementname(), o2.getAgreementname())<0){
					return -1;
				}
				return 0;
			} 
		};
		
		Collections.sort(agreementlist, cmp);
		for(INSBAgreement model:agreementlist){
			System.out.println(model.getAgreementname());
		}
	}

}
