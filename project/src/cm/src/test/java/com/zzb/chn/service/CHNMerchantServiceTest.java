package com.zzb.chn.service;

import com.BaseTest;
import com.zzb.mobile.model.CommonModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a description text
 *
 * @author wu-shangsen
 * @version 1.0, 2016/9/8
 */
public class CHNMerchantServiceTest extends BaseTest {

    @Autowired
    private CHNMerchantService chnMerchantService ;

    @Test
    public void queryAgreementArea() {
        Map<String, Object> paramMap = new HashMap<>() ;
        paramMap.put("channelId", "aaa") ;

        CommonModel commonModel = null ;
        try {
            commonModel = chnMerchantService.queryAgreementArea(paramMap) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(commonModel);
    }
}
