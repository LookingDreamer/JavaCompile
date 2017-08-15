package com.zzb.warranty.controller;

import com.cninsure.core.utils.LogUtil;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.warranty.model.INSEQuote;
import com.zzb.warranty.model.INSEWarrantyPrice;
import com.zzb.warranty.model.Response;
import com.zzb.warranty.service.INSEQuoteService;
import com.zzb.warranty.service.INSEWarrantyPriceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/10.
 */


@Controller
@RequestMapping("warranty/quote/*")
public class QuoteController extends BaseController {
    @Resource
    INSEQuoteService quoteService;
    @Resource
    INSEWarrantyPriceService warrantyPriceService;

    @RequestMapping(value = {"/getquote", "/getQuote"}, method = RequestMethod.POST)
    @ResponseBody
    public Response getQuote(@RequestBody @Valid INSEQuote quote, HttpServletRequest request) throws Exception {
        LogUtil.info("[延保]报价请求, 参数: %s", quote.toString());

        INSBAgent agent = getCurrentAgent(request);
        // 计算保费
        INSEQuote result = quoteService.getComputedQuote(
                quote.getVincode(),
                quote.getEngineno(),
                quote.getCarprice(),
                quote.getRegisterdate(),
                quote.getOrigialwarrantyperiod(),
                quote.getStartdate(),
                quote.getEnddate(),
                quote.getExtendwarrantytype(),
                quote.getPlateNumber(),
                quote.getStandardfullname(),
                quote.getStandardname(),
                agent
        );
        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, result);
    }

    @RequestMapping(value = "multiplequote", method = RequestMethod.GET)
    @ResponseBody
    public Response getMultipleQuote(@RequestParam(value = "carPrice") Double carPrice,
                                     @RequestParam(value = "isImported") Boolean isImported) {

        List<INSEWarrantyPrice> warrantyPrices = warrantyPriceService.selectWarrantyPrices(carPrice, isImported);

        if (warrantyPrices == null || warrantyPrices.size() == 0) {
            throw new RuntimeException("价格超出范围");
        }

        Response response = new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, warrantyPrices);
        return response;
    }

    private static int getMonthSpace(Date startDate, Date endDate) {
        if (!endDate.after(startDate)) {
            throw new RuntimeException("");
        }
        int result = 0;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(startDate);
        c2.setTime(endDate);
        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        return result == 0 ? 1 : Math.abs(result);
    }
}
