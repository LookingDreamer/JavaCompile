package com.zzb.extra.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.extra.entity.INSBChnTaxrate;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:config/spring-config.xml", "classpath:config/spring-mvc-config.xml"
		, "classpath:config/spring-security-config.xml", "classpath:config/spring-config-db.xml"})
public class INSBTaxrateDaoTests {

	@Resource
	private INSBTaxrateDao taxrateDao;
	
	@Test
	public void testSaveObj() {
		INSBChnTaxrate tax = new INSBChnTaxrate();
		tax.setId(UUIDUtils.random());
		tax.setCreatetime(new Date());
		tax.setChannelid("minizzb");
		tax.setEffectivetime(new Date());
		tax.setStatus("1");
		tax.setTaxrate(0.1);
		taxrateDao.insert(tax);
	}
	@Test
	public void testUpdate() {
		INSBChnTaxrate tax = new INSBChnTaxrate();
		tax.setId("985f4c16a5b08cae9e73abb39b2ad3fe");
		tax.setOperator("shiguiw");
		tax.setModifytime(new Date());
		tax.setChannelid("shigiwu");
		taxrateDao.updateById(tax);
	}
}
