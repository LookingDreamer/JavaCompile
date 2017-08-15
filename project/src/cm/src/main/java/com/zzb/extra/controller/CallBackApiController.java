package com.zzb.extra.controller;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.entity.INSBMiniOrderTrace;
import com.zzb.extra.model.INSBMiniOrderTraceModel;
import com.zzb.extra.service.INSBMiniCallBackService;
import com.zzb.extra.service.INSBMiniOrderTraceService;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hwc on 2016/12/12.
 */
@Controller
@RequestMapping("/miniCallBack/api/*")
public class CallBackApiController {
    @Resource
    private INSBMiniOrderTraceService insbMiniOrderTraceService;
    @Resource
    private INSBMiniCallBackService insbMiniCallBackService;

    @RequestMapping(value="/updateAgentOperate",method= RequestMethod.POST)
    @ResponseBody
    public String updateAgentOperate(@RequestBody INSBMiniOrderTrace insbMiniOrderTrace) throws ControllerException {
         try {
             LogUtil.info("updateAgentOperate called:"+JsonUtils.serialize(insbMiniOrderTrace));
             //System.out.println(JsonUtils.serialize(insbMiniOrderTrace));
             insbMiniOrderTraceService.updateOrderTraceState(insbMiniOrderTrace.getTaskid(),insbMiniOrderTrace.getProvidercode(),"17");
             return ParamUtils.resultMap(true, "操作成功");
         }catch (Exception e){
             return ParamUtils.resultMap(false, "更新状态出错！"+e.getMessage());
         }
    }

    @RequestMapping(value="/callBack",method= RequestMethod.POST)
    @ResponseBody
    public String callBack(@RequestBody QuoteBean quoteBean) throws ControllerException {
        try {
            LogUtil.info(quoteBean.getTaskId()+" 接收到渠道回调："+JsonUtils.serialize(quoteBean));
            insbMiniCallBackService.callBack(quoteBean);
            return ParamUtils.resultMap(true, "操作成功");
        }catch (Exception e){
            return ParamUtils.resultMap(false, "系统错误！"+e.getMessage());
        }
    }


}
