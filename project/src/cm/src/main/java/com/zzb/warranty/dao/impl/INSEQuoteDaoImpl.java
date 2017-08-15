package com.zzb.warranty.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.warranty.dao.INSEQuoteDao;
import com.zzb.warranty.model.INSEQuote;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/1/10.
 */
@Repository
public class INSEQuoteDaoImpl extends BaseDaoImpl<INSEQuote> implements INSEQuoteDao {
    @Override
    public String insertQuote(INSEQuote quote) {
        String id = generateId();
        quote.setId(id);
        insert(quote);
        return id;
    }

    @Override
    public void updateQuote(INSEQuote quote) {
        updateById(quote);
    }

    @Override
    public INSEQuote selectQuote(String quoteId) {

        return selectById(quoteId);
    }


}
