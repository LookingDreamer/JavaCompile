package com.zzb.warranty.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.warranty.model.INSEQuote;

import java.util.Date;

/**
 * Created by Administrator on 2017/1/10.
 */
public interface INSEQuoteService extends BaseService<INSEQuote> {

    INSEQuote getQuote(String quoteId);

    INSEQuote getComputedQuote(String vincode, String engineNo, Double carPrice, Date registerDate, int originalWarrantyYears,
                               Date eWarrantyStartDate, Date eWarrantyEndDate, int eWarrantyType,
                               String plateNumber, String standardFullName, String standardName, INSBAgent agent) throws Exception;

}
