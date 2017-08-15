package com.zzb.extra.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.HttpSender;
import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.service.INSBMiniCallChnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hwc on 2016/12/12.
 */
@Service
@Transactional
public class INSBMiniCallChnServiceImpl implements INSBMiniCallChnService {

    @Resource
    private INSCCodeService inscCodeService;

    private static Map<String, String> httpHead = new HashMap<>();

    static {
        httpHead.put("innerPipe", "zheshiyigemimi!");
        httpHead.put("Content-Type", "application/json;charset=utf-8");
    }

    public String applyRefund(String taskid,String providercode){
        try {
            INSCCode inscCode = new INSCCode();
            inscCode.setParentcode("ChannelForMini");
            inscCode.setCodetype("channelurl");
            inscCode.setCodevalue("01");
            inscCode = inscCodeService.queryOne(inscCode);
            String url = "";
            String signUrl = "";
            if (null != inscCode) {
                url = inscCode.getProp2() + "/channel/reimbursement";
                signUrl = inscCode.getProp2() + "/channel/getSignForInner";
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskId",taskid);
            map.put("prvId",providercode);
            String json = JsonUtils.serialize(map);
            httpHead.put("channelId", "nqd_minizzb2016");
            String sign = HttpSender.doPost(signUrl, json, httpHead, "UTF-8");
            httpHead.put("sign",sign);
            String res  = HttpSender.doPost(url, json, httpHead, "UTF-8");
            Map resMap = JsonUtils.deserialize(res, HashMap.class);
            return String.valueOf(resMap.get("status"));
        }catch (Exception e){
            LogUtil.info(JsonUtils.serialize(taskid) + "" + e.getMessage());
            return "error";
        }
    }

}
