package com.zzb.warranty.controller;

import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.zzb.cm.service.INSBCarinfohisService;
import com.zzb.warranty.model.INSECar;
import com.zzb.warranty.model.Response;
import com.zzb.warranty.service.INSECarService;
import com.zzb.warranty.util.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/17.
 */
@Controller
@RequestMapping(value = "warranty/carinfo/*")
public class CarinfoController extends BaseController {

    @Resource
    INSBCarinfohisService insbCarinfohisService;

    @Resource
    INSECarService carService;

    @RequestMapping(value = "searchcarinfo", method = RequestMethod.GET)
    @ResponseBody
    public Response searchCar(HttpServletRequest request, @RequestParam(required = false) String key) {
        LogUtil.info("[延保]搜索历史车辆, 参数: %s", key);

//        INSBAgent agent = getCurrentAgent(request);
//        if (StringUtils.isBlank(key)) {
//            return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, null);
//        }
//
//        List<Map<String, Object>> cars = carService.selectByOwnernameAndPlateNumber(agent.getJobnum(), key);
//
//        JSONArray jsonArray = new JSONArray();
//
//        for (Map<String, Object> carinfoMap : cars) {
//            carinfoMap.put("registerdate", DateUtils.format((Date) carinfoMap.get("registdate"), "yyyy-MM-dd"));
//            carinfoMap.put("plateNumber", carinfoMap.get("carlicenseno"));
//
//            JSONObject jsonObject = new JSONObject(carinfoMap);
//            jsonArray.add(jsonObject);
//        }
//
//        LogUtil.info("[延保]搜索历史车辆, 返回: %s", jsonArray);
//        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, jsonArray);

        return new Response(Response.STATUS_FAIL, "查询功能关闭");
    }

    @RequestMapping(value = "cardetails", method = RequestMethod.GET)
    @ResponseBody
    public Response getCarInfo(@RequestParam(value = "carid") String carId) {
        INSECar car = carService.getCarById(carId);

        if (car == null) {
            throw new RuntimeException("车辆信息不存在");
        }
        Map<String, Object> carinfoMap = BeanUtils.toMap(car);
        carinfoMap.put("registerdate", car.getRegistdate() == null ? null : DateUtils.format(car.getRegistdate(), "yyyy-MM-dd"));
        carinfoMap.put("plateNumber", car.getCarlicenseno());
        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, carinfoMap);
    }
}
