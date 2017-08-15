package com.zzb.warranty.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by Administrator on 2017/1/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml",})
public class QuoteControllerTest {


    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetQuote() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/warranty/quote/getquote")
                                .header("token", "111")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"vincode\": \"ASDFASD******FGAS\",\n" +
                                        "    \"standardname\": \"丰田GTM7161GB轿车\",\n" +
                                        "    \"engineno\": \"L***ASF\",\n" +
                                        "    \"standardfullname\": \"丰田GTM7161GB轿车\",\n" +
                                        "    \"registerdate\": \"2017-01-07\",\n" +
                                        "    \"extendwarrantytype\": 0,\n" +
                                        "    \"startdate\": \"2017-01-09\",\n" +
                                        "    \"enddate\": \"2018-01-09\",\n" +
                                        "    \"carprice\": \"86000\",\n" +
                                        "    \"origialwarrantyperiod\": 1,\n" +
                                        "    \"platenumber\": \"粤A88888\"\n" +
                                        "}")
                )
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String body = response.getContentAsString();
        Assert.assertEquals(200, status);
        Assert.assertNotNull(body);
    }
}
