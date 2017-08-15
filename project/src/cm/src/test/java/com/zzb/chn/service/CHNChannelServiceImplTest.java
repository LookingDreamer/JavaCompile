package com.zzb.chn.service;

import com.BaseTest;
import com.zzb.chn.bean.QuoteBean;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is a description text
 *
 * @author wu-shangsen
 * @version 1.0, 2016/9/8
 */
public class CHNChannelServiceImplTest extends BaseTest {

    @Autowired
    private CHNChannelService channelService ;

    @Test
    public void getAgreementAreas() {
        QuoteBean quoteBean = new QuoteBean() ;
        quoteBean.setChannelId("d31aae502f41123ba2ea95853528695a");
        //quoteBean.setAgreementProvCode("110000");
        QuoteBean quoteBeanB = channelService.getAgreementAreas(quoteBean) ;
    }
}
