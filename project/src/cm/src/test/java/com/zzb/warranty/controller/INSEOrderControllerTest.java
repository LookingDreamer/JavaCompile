package com.zzb.warranty.controller;

import com.zzb.warranty.dao.INSEOrderDao;
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

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/1/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml",
        "classpath:config/spring-config-db.xml", })
public class INSEOrderControllerTest {
    @Resource
    INSEOrderDao orderDao;


    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testAddOrder() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/warranty/order/add")
                                .header("token", "123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"quoteinfoid\": \"05bf5120d7dc11e6ea793a5cc003c26d\",\n" +
                                        "\n" +
                                        "    \"carownerinfo\": {\n" +
                                        "        \"samecarowner\": false,\n" +
                                        "    \t\"gender\": 0,\n" +
                                        "        \"name\": \"你好\",\n" +
                                        "        \"tel\": \"18680244102\",\n" +
                                        "        \"certificateType\":\"6\",\n" +
                                        "        \"certNumber\":\"46650460-6\",\n" +
                                        "        \"address\": \"广州市水荫路52号\"\n" +
                                        "    },\n" +
                                        "\n" +
                                        "\t\"insured\": {\n" +
                                        "\t\t\"samecarowner\": false,\n" +
                                        "    \t\"gender\": 0,\n" +
                                        "        \"name\": \"你好\",\n" +
                                        "        \"tel\": \"18680244102\",\n" +
                                        "        \"certificateType\":\"6\",\n" +
                                        "        \"certNumber\":\"46650460-6\",\n" +
                                        "        \"address\": \"广州市水荫路52号\"\n" +
                                        "    },\n" +
                                        "\n" +
                                        "    \"applicant\": {\n" +
                                        "    \t\"samecarowner\": false,\n" +
                                        "    \t\"gender\": 0,\n" +
                                        "        \"name\": \"你好\",\n" +
                                        "        \"tel\": \"18680244102\",\n" +
                                        "        \"certificateType\":\"6\",\n" +
                                        "        \"certNumber\":\"46650460-6\",\n" +
                                        "        \"address\": \"广州市水荫路52号\"\n" +
                                        "    },\n" +
                                        "\n" +
                                        "    \"legalrightclaim\": {\n" +
                                        "    \t\"samecarowner\": false,\n" +
                                        "    \t\"gender\": 0,\n" +
                                        "        \"name\": \"你好\",\n" +
                                        "        \"tel\": \"18680244102\",\n" +
                                        "        \"certificateType\":\"6\",\n" +
                                        "        \"certNumber\":\"46650460-6\",\n" +
                                        "        \"address\": \"广州市水荫路52号\"\n" +
                                        "    },\n" +
                                        "    \"needeinvoice\": true,\n" +
                                        "    \"invoiceemail\": \"abc@qq.com\",\n" +
                                        "    \"deliveryAddress\": \"广州市水荫路52号\"\n" +
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
    @Test
    public void testOrderDetails() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/warranty/order/orderdetails")
                                .header("token", "123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("\"orderno\":\"10001352-20170117103525\"")
                )
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        int status = response.getStatus();
        String body = response.getContentAsString();
        Assert.assertEquals(200, status);
        Assert.assertNotNull(body);
    }

    @Test
    public void testOrderList() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/warranty/order/orderlist")
                                .header("token", "123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "\"orderstatus\": 0,\n" +
                                        "\"pagesize\": 10,\n" +
                                        "\"pagenum\": 1\n" +
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

    @Test
    public void testOrderStatus() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .post("/warranty/order/orderstatus")
                                .header("token", "123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "\"orderno\": \"10000160-1484357843464\"\n" +
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
