package com.zzb.warranty.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.warranty.dao.INSEQuoteDao;
import com.zzb.warranty.dao.INSEWarrantyPriceDao;
import com.zzb.warranty.exception.WarrantyInfoException;
import com.zzb.warranty.model.INSEQuote;
import com.zzb.warranty.model.INSEWarrantyPrice;
import com.zzb.warranty.service.INSEQuoteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/10.
 */
@Service
public class INSEQuoteServiceImpl extends BaseServiceImpl<INSEQuote> implements INSEQuoteService {
    @Resource
    INSEQuoteDao quoteDao;

    @Resource
    INSEWarrantyPriceDao warrantyPriceDao;

    @Override
    public INSEQuote getQuote(String quoteId) {
        return quoteDao.selectQuote(quoteId);
    }

    @Override
    public INSEQuote getComputedQuote(String vincode, String engineNo, Double carPrice, Date registerDate, int originalWarrantyYears,
                                      Date eWarrantyStartDate, Date eWarrantyEndDate, int eWarrantyType,
                                      String plateNumber, String standardFullName, String standardName, INSBAgent agent) throws Exception {
        Date today = new Date();

        if (registerDate.after(today)) {
            throw new WarrantyInfoException("初登日期不能晚于今天");
        }

        if (originalWarrantyYears * 12 - getMonthSpace(registerDate, today) < 9) {
            throw new WarrantyInfoException("很抱歉，投保车辆离原厂保修结束时间少于9个月，不能投保!");
        }

        if (eWarrantyStartDate.after(eWarrantyEndDate)) {
            throw new WarrantyInfoException("延保开始日期不能晚于结束日期");
        }

        Calendar c1 = Calendar.getInstance();
        c1.setTime(registerDate);
        int year = c1.get(Calendar.YEAR);
        int month = c1.get(Calendar.MONTH);
        int day = c1.get(Calendar.DAY_OF_MONTH);

        c1.clear();
        c1.set(year + originalWarrantyYears, month, day);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(eWarrantyStartDate);

        if (c2.before(c1)) {
            throw new WarrantyInfoException("延保日期需要从原厂保修期结束时开始");
        }

        // 计算保费
        Double amount = computeQuote(carPrice, originalWarrantyYears, getYearSpace(eWarrantyStartDate,
                eWarrantyEndDate), vincode, eWarrantyType);


        INSEQuote quote = new INSEQuote();
        quote.setVincode(vincode);
        quote.setCarprice(carPrice);
        quote.setRegisterdate(registerDate);
        quote.setOrigialwarrantyperiod(originalWarrantyYears);
        quote.setStartdate(eWarrantyStartDate);
        quote.setEnddate(new Date(eWarrantyEndDate.getTime() + 24 * 3600 * 1000 - 1000));
        quote.setExtendwarrantytype(eWarrantyType);
        quote.setPlateNumber(plateNumber);
        quote.setStandardfullname(standardFullName);
        quote.setStandardname(standardName);
        quote.setEngineno(engineNo);
        quote.setAmount(amount);
        quote.setCreatetime(new Date());
        quote.setId(UUIDUtils.create());
        quote.setOperator(agent.getName());
        insert(quote);

        return quote;
    }


    private Double computeQuote(Double carPrice, int originWarrantyYears, int eWarrantyYears, String vincode, int eWarrantytype) {

        boolean isImported = isImportedCar(vincode);
        Double actualCarPrice = getCarPriceAfterDepreciation(carPrice, originWarrantyYears * 12);
        actualCarPrice = carPrice;
        INSEWarrantyPrice warrantyPrice = warrantyPriceDao.selectWarrantyPrice(actualCarPrice, eWarrantytype, isImported, eWarrantyYears);

        if (warrantyPrice == null) {
            throw new RuntimeException("超出报价能力");
        }

        return warrantyPrice.getWarrantyPrice();
    }

    @Override
    protected BaseDao<INSEQuote> getBaseDao() {
        return quoteDao;
    }

    private static int getMonthSpace(Date startDate, Date endDate) throws Exception {
        if (startDate.after(endDate)) {
            throw new Exception("开始日期不能晚于结束日期");
        }

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(startDate);
        c2.setTime(endDate);

        int years = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        int month = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        int day = c2.get(Calendar.DAY_OF_MONTH) - c1.get(Calendar.DAY_OF_MONTH);

        return years * 12 + month + (day > 0 ? 1 : 0);
    }

    private static int getYearSpace(Date startDate, Date endDate) {

        if (!endDate.after(startDate)) {
            throw new RuntimeException("开始日期需要早于结束日期");
        }

        int result = 0;

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(startDate);
        c2.setTime(endDate);

        result = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

        return result == 0 ? 1 : Math.abs(result);

    }

    /**
     * @param vincode 车架号
     * @return
     */
    private boolean isImportedCar(String vincode) {
        assert vincode != null;
        return !(vincode.startsWith("L") || vincode.startsWith("l"));
    }

    private Double getCarPriceAfterDepreciation(Double carPrice, Integer monthSpace) {

        return carPrice - carPrice * monthSpace * 0.006;
    }
}
