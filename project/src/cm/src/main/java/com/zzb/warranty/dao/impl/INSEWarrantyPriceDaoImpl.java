package com.zzb.warranty.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.warranty.dao.INSEWarrantyPriceDao;
import com.zzb.warranty.model.INSEWarrantyPrice;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/16.
 */
@Repository
public class INSEWarrantyPriceDaoImpl extends BaseDaoImpl<INSEWarrantyPrice> implements INSEWarrantyPriceDao {

    @Override
    public INSEWarrantyPrice selectWarrantyPrice(Double carPrice, int warrantyPlan, boolean isImported, int years) {
        Map<String, Object> params = new HashMap<>();
        params.put("carPrice", carPrice);
        params.put("warrantyPlan", warrantyPlan);
        params.put("isImported", isImported);
        params.put("years", years);
        return this.sqlSessionTemplate.selectOne("selectWarrantyPrice", params);
    }

    @Override
    public List<INSEWarrantyPrice> selectWarrantyPrices(Double carPrice, boolean isImported) {
        Map<String, Object> params = new HashMap<>();
        params.put("carPrice", carPrice);
        params.put("isImported", isImported);
        return sqlSessionTemplate.selectList("selectWarrantyPrices", params);
    }

}
