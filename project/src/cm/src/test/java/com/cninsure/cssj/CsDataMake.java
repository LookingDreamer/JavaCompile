package com.cninsure.cssj;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRisk;
import com.zzb.conf.entity.INSBRiskimg;
import com.zzb.conf.entity.INSBRiskkind;
import com.zzb.conf.entity.INSBRiskkindconfig;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.conf.service.INSBRiskService;
import com.zzb.conf.service.INSBRiskimgService;
import com.zzb.conf.service.INSBRiskkindService;
import com.zzb.conf.service.INSBRiskkindconfigService;


@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class CsDataMake {
	@Resource
	private INSBRiskkindconfigService kindconfigService;
	@Resource
	private INSBRiskService riskservice;
	@Resource
	private INSBRiskkindService riskkindservice;
	@Resource
	private INSBProviderService providerservice;
	@Resource
	private INSCCodeService codeservice;
	@Resource
	private INSBRiskimgService imgservice;
	
	@Test
	public void makeriskandkindTest(){
//		makeriskandkind("201137");
//		makeriskandkind("201637");
//		makeriskandkind("202737");
//		makeriskandkind("202237");
//		makeriskandkind("206637");
//		makeriskandkind("202637");
//		makeriskandkind("200537");
//		makeriskandkind("200237");
//		makeriskandkind("207737");
//		makeriskandkind("208537");
//		makeriskandkind("204444");
//		makeriskandkind("400144");
//		makeriskandkind("202537");
//		
//		
//		makeriskandkind("2011");
//		makeriskandkind("2016");
//		makeriskandkind("2027");
//		makeriskandkind("2022");
//		makeriskandkind("2066");
//		makeriskandkind("2026");
//		makeriskandkind("2005");
//		makeriskandkind("2002");
//		makeriskandkind("2073");
//		makeriskandkind("2086");
		makeriskandkind("208637");
		}
	
	public void clearriskandkind(String prvcode){
/*		
  		 DELETE FROM insbriskkind  WHERE EXISTS(SELECT 1 FROM insbrisk r WHERE id = riskid AND provideid='201137')
		 DELETE FROM INSBRiskimg  WHERE EXISTS(SELECT 1 FROM insbrisk r WHERE id = riskid AND provideid='201137')
		 DELETE FROM insbrisk WHERE provideid='201137';
*/
	}
	
	/**
	 * 测试数据准备 
	 * 准备  险种 及 险别信息 影像件信息 
	 */
	public void makeriskandkind(String prvcode){
		INSBProvider vo = new INSBProvider();
		vo.setPrvcode(prvcode);
		INSBProvider insbProvider  = providerservice.queryById(prvcode);
		INSCCode codevo = new INSCCode();
		codevo.setParentcode("insuranceimage");
		List<INSCCode> imgcodelist =codeservice.queryList(codevo);
		//险种
		INSBRisk risk = new INSBRisk();
		risk.setId(null);
		risk.setModifytime(new Date());
		risk.setRiskcode(prvcode);
		risk.setRiskname(insbProvider.getPrvname()+"车险");
		risk.setRiskshortname(insbProvider.getPrvname()+"车险");
		risk.setRisktype("1"); //1 车险  2非车险
		risk.setProvideid(prvcode);
		
		risk.setRenewaltype("1");//续保类型 1快速续保 0 非快速续保
		riskservice.insert(risk);
		risk.setStatus("0");
		
		List<INSBRiskkindconfig> configlist = kindconfigService.queryAll();
		for (INSBRiskkindconfig insbRiskkindconfig : configlist) {
			//险别
			INSBRiskkind kind = new INSBRiskkind();
			kind.setRiskid(risk.getId());
			kind.setKindcode(insbRiskkindconfig.getRiskkindcode());
			kind.setKindname(insbRiskkindconfig.getRiskkindname());
			kind.setKindtype(insbRiskkindconfig.getType());
			kind.setNotdeductible(insbRiskkindconfig.getIsdeductible());
			kind.setOperator(insbRiskkindconfig.getOperator());//是否保额选项  1是  0否
			kind.setIsamount("1");//是否保额选项  1是  0否
			kind.setNotdeductible(insbRiskkindconfig.getIsdeductible());//是否不计免赔 1是 2 不是
			kind.setAmountselect(insbRiskkindconfig.getDatatemp());
			kind.setIsusing("1");
			kind.setPreriskkind(insbRiskkindconfig.getPrekindcode());//前置险别
			riskkindservice.insert(kind);
		}		
		int j =1;
		for (INSCCode inscCode : imgcodelist) {
			INSBRiskimg  img = new INSBRiskimg();
			img.setRiskid(risk.getId());
			img.setRiskimgname(inscCode.getCodename());
			img.setRiskimgtype(inscCode.getCodevalue());
			img.setIsusing("1");
			imgservice.insert(img);
		}
	}
}
