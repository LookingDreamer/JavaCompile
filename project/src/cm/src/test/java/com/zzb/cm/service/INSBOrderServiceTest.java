package com.zzb.cm.service;

import com.zzb.cm.entity.INSBOrder;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Calendar;

/**
 * author: wz
 * date: 2017/3/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml",
        "classpath:config/spring-config-db.xml", })
public class INSBOrderServiceTest {
    @Resource
    INSBOrderService orderService;

    @Test
    public void selectOrdersWithFairyQRPay() throws Exception {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH)-1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 999);

        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable;
        Page<INSBOrder> page = null;
        while (page == null || !page.isLast()) {

            pageable = new MyPageable(pageNumber++, pageSize);
            page = orderService.selectOrdersWithFairyQRPay(start.getTime(), end.getTime(), pageable);
//            for (INSBOrder order : page.getContent()) {
//
//            }

            Assert.assertNotNull(page);

            System.out.println(JSONObject.fromObject(page));
        }

    }


    private static class MyPageable implements Pageable {
        private int pageNumber;
        private int pageSize;

        public MyPageable(int pageNumber, int pageSize) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        @Override
        public int getPageNumber() {
            return pageNumber;
        }

        @Override
        public int getPageSize() {
            return pageSize;
        }

        @Override
        public int getOffset() {
            return pageNumber * pageSize;
        }

        @Override
        public Sort getSort() {
            return null;
        }

        @Override
        public Pageable next() {
            return null;
        }

        @Override
        public Pageable previousOrFirst() {
            return null;
        }

        @Override
        public Pageable first() {
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }
    }

}