package com.zzb.mobile.controller;

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
 * Created by Administrator on 2017/3/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml",})
public class INSBPayChannelControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testShowPayChannel() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/mobile/basedata/myTask/showPayChannel")
                                .header("token", "e3cd38f5d3ec199fa447c9c8add76866")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "\"prvid\":\"201141\",\n" +
                                        "\"deptcode\":\"1241191001\",\n" +
                                        "\"taskid\":\"1859020\",\n" +
                                        "\"subInstanceId\":\"1859023\",\n" +
                                        "\"clienttype\":\"web\"\n" +
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
