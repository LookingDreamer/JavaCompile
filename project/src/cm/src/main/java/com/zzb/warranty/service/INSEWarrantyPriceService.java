package com.zzb.warranty.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.warranty.model.INSEWarrantyPrice;

import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */
public interface INSEWarrantyPriceService extends BaseService<INSEWarrantyPrice> {
    INSEWarrantyPrice getWarrantyPrice(Double carPrice, int warrantyPlan, boolean isImported, int years);

    List<INSEWarrantyPrice> selectWarrantyPrices(Double carPrice, boolean isImported);
}
