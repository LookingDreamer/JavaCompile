package com.zzb.cm.servcie;

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.cm.entity.INSBFairyInsureErrorLog;
import com.zzb.cm.service.INSBFairyInsureErrorLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Date;

/**
 * author: wz
 * date: 2017/3/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml"})
public class INSBFairyInsureErrorLogTest {

    @Resource
    INSBFairyInsureErrorLogService fairyInsureErrorLogService;


    @Test
    public void testInsert() {
        INSBFairyInsureErrorLog log = new INSBFairyInsureErrorLog();
        log.setTaskId("123456");
        log.setInsuranceCompanyId("20853707");
        log.setCreateTime(new Date());
        log.setErrorCode("001");
        log.setErrorDesc("错误原因");
        log.setOperator("admin");

        for (int i = 0; i < 100; i++) {
            log.setId(UUIDUtils.create());
            fairyInsureErrorLogService.insert(log);
        }
    }

    @Test
    public void testQueryPageList() {
        INSBFairyInsureErrorLog insureErrorLog = new INSBFairyInsureErrorLog();
        insureErrorLog.setTaskId("123456");
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = new Pageable() {
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
                return pageNumber <= 0 ? 0 : pageNumber * pageSize;
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
        };
        Page<INSBFairyInsureErrorLog> page = fairyInsureErrorLogService.queryPageList(insureErrorLog, pageable);
        System.out.println(net.sf.json.JSONObject.fromObject(page));
    }
}
