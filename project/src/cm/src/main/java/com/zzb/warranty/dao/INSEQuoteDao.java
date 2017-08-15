package com.zzb.warranty.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.warranty.model.INSEQuote;

/**
 * Created by Administrator on 2017/1/10.
 */
public interface INSEQuoteDao extends BaseDao<INSEQuote>{

    String insertQuote(INSEQuote quote);

    void updateQuote(INSEQuote quote);

    INSEQuote selectQuote(String quoteId);
}
