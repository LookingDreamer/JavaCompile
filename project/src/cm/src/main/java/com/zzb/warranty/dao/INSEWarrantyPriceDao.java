package com.zzb.warranty.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.warranty.model.INSEWarrantyPrice;

import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */
public interface INSEWarrantyPriceDao extends BaseDao<INSEWarrantyPrice> {
    INSEWarrantyPrice selectWarrantyPrice(Double carPrice, int warrantyPlan, boolean isImported, int years);

    List<INSEWarrantyPrice> selectWarrantyPrices(Double carPrice, boolean isImported);
}
