package com.zzb.warranty.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.warranty.dao.INSEWarrantyPriceDao;
import com.zzb.warranty.model.INSEWarrantyPrice;
import com.zzb.warranty.service.INSEWarrantyPriceService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */
@Service
public class INSEWarrantyPriceServiceImpl extends BaseServiceImpl<INSEWarrantyPrice> implements INSEWarrantyPriceService {
    @Resource
    INSEWarrantyPriceDao warrantyPriceDao;

    Logger logger = Logger.getLogger(INSEWarrantyPriceServiceImpl.class);

    @Override
    protected BaseDao<INSEWarrantyPrice> getBaseDao() {
        return warrantyPriceDao;
    }

    /**
     *
     * @param carPrice  车的价格
     * @param warrantyPlan  延保方案
     * @param isImported    是否为进口车
     * @param years 延保年数
     * @return
     */
    @Override
    public INSEWarrantyPrice getWarrantyPrice(Double carPrice, int warrantyPlan, boolean isImported, int years) {

        logger.info(String.format("获取价格区间报价，参数为：carPrice = %s, warrantyPlan=%s, isImported=%b, years=%s",
                carPrice, warrantyPlan, isImported, years));

        if (years < 0) {
            return null;
        }

        return warrantyPriceDao.selectWarrantyPrice(carPrice, warrantyPlan, isImported, years);
    }

    @Override
    public List<INSEWarrantyPrice> selectWarrantyPrices(Double carPrice, boolean isImported) {

        return warrantyPriceDao.selectWarrantyPrices(carPrice, isImported);
    }


}
