package com.market;

import com.zzb.mobile.model.lastindanger.LastClaimBackInfo;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",})
public class MarketControllerTest {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	private final static String prefix="/mobile/marketing/";

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

//
//	@Test
//	public void testSubmitRegInfo() throws Exception {
//		MvcResult result = mockMvc
//				.perform(MockMvcRequestBuilders.post("/mobile/registered/submitRegInfo").
//								contentType(MediaType.APPLICATION_XML)
//								.accept(MediaType.TEXT_PLAIN).characterEncoding("utf-8")
//								.param("regInfoJSON", "{\"phone\":\"18565031102\",\"passWord\":\"123456787\",\"refNum\":\"620610411\","
//										+ "\"provinceCode\":\"110000\",\"openid\":\"oJhe7wJefPw8VxYTIKuJi0C_pA2o\",\"name\":\"雷海娟\","
//										+ "\"cityCode\":\"110100\",\"countyCode\":\"110101\"}")
//				)
////				.andDo(MockMvcResultHandlers.print())
//				.andReturn();
//
//		String out=result.getResponse().getContentAsString();
//		System.out.println("输出结果");
//		System.out.printf(out);
//
//		Assert.assertNotNull(result.toString());
//	}
//
	@Test
	public void testRegisterSuccess() throws Exception{
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get(prefix + "registerSuccess").
						characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).param("jobNum", "B000015885")
				).andReturn();

		String out=result.getResponse().getContentAsString();
		System.out.println("输出结果");
		System.out.printf(out);
		Assert.assertNotNull(result.toString());
	}

	@Test
	public void testGetReferrals() throws Exception{
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get(prefix + "getReferrals").
								characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).param("jobNum", "30000019")
				).andReturn();

		String out=result.getResponse().getContentAsString();
		System.out.println("输出结果");
		System.out.printf(out);
		Assert.assertNotNull(result.toString());
	}

	@Test
	public void testGetRankingList() throws Exception{
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get(prefix + "getRankingList").
								characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).param("jobNum", "30000019")
				).andReturn();
		String out=result.getResponse().getContentAsString();
		System.out.println("输出结果");
		System.out.printf(out);
		Assert.assertNotNull(result.toString());
	}
	@Test
	public void testGetWeekRankingList() throws Exception{
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get(prefix + "getWeekRankingList").
								characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).param("jobNum", "620136820")
				).andReturn();
		String out=result.getResponse().getContentAsString();
		System.out.println("输出结果");
		System.out.printf(out);
		Assert.assertNotNull(result.toString());
	}
	@Test
	public void testGetMyLotteryCodeInfo() throws Exception{
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get(prefix + "getMyLotteryCodeInfo").
								characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).param("jobNum", "620136820")
				).andReturn();
		String out=result.getResponse().getContentAsString();
		System.out.println("输出结果");
		System.out.printf(out);
		Assert.assertNotNull(result.toString());
	}

	@Test
	public void testGetAwardInfo() throws Exception{
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get(prefix + "getAwardInfo").
								characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON).param("jobNum", "610461519")
				).andReturn();
		String out=result.getResponse().getContentAsString();
		System.out.println("输出结果");
		System.out.printf(out);
		Assert.assertNotNull(result.toString());
	}

	public static void main(String[] args) {
		LastClaimBackInfo  info = new LastClaimBackInfo();
		System.out.println(JSONObject.fromObject(info).toString());
	}
}
