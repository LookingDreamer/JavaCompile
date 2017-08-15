package com.zzb.extra.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.extra.entity.INSBAgentPrize;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.extra.service.INSBAgentPrizeService;
import com.zzb.extra.service.INSBMiniCallBackService;
import com.zzb.extra.service.INSBMiniOrderTraceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;

/**
 * Created by HWC on 2016/12/15.
 */
@Service
@Transactional
public class INSBMiniCallBackServiceImpl implements INSBMiniCallBackService {

    @Resource
    private INSBMiniOrderTraceService insbMiniOrderTraceService;

    @Resource
    private INSBAgentPrizeService insbAgentPrizeService;

    @Resource
    private INSBAccountDetailsService insbAccountDetailsService;

    public String callBack(QuoteBean quoteBean) {
        String taskId = quoteBean.getTaskId();
        String taskState = quoteBean.getTaskState();
        String inscomcode = quoteBean.getPrvId();
        String channelId = quoteBean.getChannelId();
        try {
            switch (taskState) {
                case "3"://报价成功待支付
                    break;
                case "4"://支付成功
                    updatePrizeStatusToFour(taskId,inscomcode);
                    LogUtil.info("支付成功，锁定红包开始:"+taskId);
                    insbAccountDetailsService.refreshRedPackets(taskId,inscomcode);
                    LogUtil.info("支付成功，锁定红包结束:"+taskId);
                    break;
                case "5"://补齐影像
                    break;
                case "6"://核保成功
                    updateOrderTrace(quoteBean);
                    break;
                case "21"://支付确认中
                    break;
                case "11"://承保成功
                    updateOrderTrace(quoteBean);
                    LogUtil.info("承保成功，发放佣金开始:"+taskId);
                    insbAccountDetailsService.genCommissionAndRedPackets(taskId, inscomcode);
                    LogUtil.info("承保成功，发放佣金结束:"+taskId);
                    break;
                case "12"://全额保费退款
                    break;
                case "13"://全额保费退款成功
                    break;
                case "15"://完成
                    updateOrderTrace(quoteBean);
                    break;
                case "17"://核保中
                    updateOrderTrace(quoteBean);
                    break;
                case "19"://核保失败
                    updateOrderTrace(quoteBean);
                    break;
                default:
                    LogUtil.info(channelId + " : " + "taskId= " + taskId + " prvId= " + inscomcode + " taskState=" + taskState + " 不是回调的状态");
                    break;
            }

        } catch (Exception e) {
            LogUtil.error("回调处理异常" + taskId + "-" + inscomcode + "-" + taskState, e);
        }
        return "receive ok";
    }

    private void updateOrderTrace(QuoteBean quoteBean){
        insbMiniOrderTraceService.updateOrderTraceState(quoteBean.getTaskId(),quoteBean.getPrvId(),quoteBean.getTaskState());
    }

    private void updatePrizeStatusToFour(String taskid,String providercode){
        try{
            LogUtil.info("callBackService-updateStatusToFour called:"+taskid+" "+providercode);
            INSBAgentPrize insbAgentPrize = new INSBAgentPrize();
            insbAgentPrize.setTaskid(taskid);
            insbAgentPrize.setProvidercode(providercode);
            insbAgentPrize.setStatus("3");//待发放
            insbAgentPrize = insbAgentPrizeService.queryOne(insbAgentPrize);
            if(null != insbAgentPrize && taskid.equals(insbAgentPrize.getTaskid())){
                insbAgentPrize.setStatus("4");//支付完成改成 已支付待发放
                insbAgentPrizeService.updateById(insbAgentPrize);
            }
        }catch (Exception e){
            LogUtil.info("callBackService-updateStatusToFour:"+e.getMessage());
        }
    }
}
