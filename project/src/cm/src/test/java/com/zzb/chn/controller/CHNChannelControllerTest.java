package com.zzb.chn.controller;

import com.BaseTest;
import com.zzb.chn.bean.QuoteBean;
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
 * This is a description text
 *
 * @author wu-shangsen
 * @version 1.0, 2016/9/8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml",
        "classpath:config/spring-config-db.xml",
        "classpath:config/spring-config-quartz.xml"})
public class CHNChannelControllerTest extends BaseTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void callback() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/warranty/order/add")
                                .header("token", "123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"mainInstanceId\":\"1890305\",\"subInstanceId\":\"1890306\",\"providerId\":\"20194419\",\"taskName\":\"承保政策限制\",\"taskCode\":\"51\",\"taskStatus\":\"Reserved\",\"dataFlag\":2}")
                )
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String body = response.getContentAsString();
        Assert.assertEquals(200, status);
        Assert.assertNotNull(body);
    }

    @Autowired
    private CHNChannelController chnChannelController ;

    @Test
    public void getAgreementAreas() {
        QuoteBean quoteBean = new QuoteBean() ;
        QuoteBean quoteBeanB = chnChannelController.getAgreementAreas(quoteBean, "d31aae502f41123ba2ea95853528695a") ;
    }
}
