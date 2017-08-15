package com.zzb.ads.controller;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.conf.service.INSBAgreementService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 */
@Controller
@RequestMapping("/ads/*")
public class AdsController {
    @Resource
    private INSCDeptService deptService;
    @Resource
    private INSBAgreementService insbAgreementService;

    @RequestMapping(value="querydepttree",method= RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> queryDeptList(HttpServletResponse response) throws ControllerException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return deptService.queryListByPcode4Group("1200000000");
    }

    @RequestMapping(value="getagreementbycomcode",method=RequestMethod.GET)
    @ResponseBody
    public Map<Object, Object> getAgreementByComcode(@ModelAttribute PagingParams para, String comcode, String agreementcode
            , String queryagreementcode, String queryagreementname, String citys, String providerid,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");
        Map<Object, Object> initMap = new HashMap<Object, Object>();
        Map<String, Object> map = BeanUtils.toMap(para);
        map.put("comcode", comcode);
        if (StringUtil.isEmpty(agreementcode)) {
            map.put("agreementcode", "011");
        } else {
            map.put("agreementcode", agreementcode);
        }

        map.put("queryagreementcode", queryagreementcode);
        map.put("queryagreementname", queryagreementname);
        map.put("citys", citys);
        map.put("providerid", providerid);
        initMap.put("records", "10000");
        initMap.put("page", 1);
        initMap.put("rows", insbAgreementService.queryListAgreement(map));
        initMap.put("total", insbAgreementService.queryCountAgreement(map));
        return initMap;
    }
}
