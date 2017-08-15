package com.zzb.mobile.controller;

import com.cninsure.core.utils.JsonUtil;
import com.zzb.chn.util.JsonUtils;
import com.zzb.mobile.model.usercenter.CXReturnModel;
import com.zzb.mobile.model.usercenter.UserCenterReturnModel;
import com.zzb.mobile.service.UserCenterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by @Author hwc on 2017/6/20.
 */
@Controller
@RequestMapping("/mobile/basedata/usercenter/*")
public class UserCenterController {
    @Resource
    private UserCenterService userCenterService;

    @RequestMapping(value = "updateAgentData", method = RequestMethod.POST)
    @ResponseBody
    public String updateAgentData(@RequestBody CXReturnModel cxReturnModel) {
        UserCenterReturnModel userCenterReturnModel = userCenterService.updateUserDetailFromUserCenter(cxReturnModel);
        return JsonUtils.serialize(userCenterReturnModel);
    }
}
