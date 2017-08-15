package com.zzb.cm.pollingtask;

import java.util.List;

import javax.annotation.Resource;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.SpringContextHandle;
import com.zzb.conf.entity.INSBOrderlistenpush;
import com.zzb.conf.service.INSBOrderlistenpushService;
public class OrderTaskPolling {
	@Resource 
	INSBOrderlistenpushService insbOrderlistenpushService=SpringContextHandle.getBean("INSBOrderlistenpushServiceImpl");
	public void orderPollingTask(){
		String httpUrl = "http://"+ValidateUtil.getConfigValue("localhost.ip")+":" + ValidateUtil.getConfigValue("localhost.port") + "/" + ValidateUtil.getConfigValue("localhost.projectName") +"/cm4lzg/order/updateaddfail";
		// 查询订单监听表 “插入” 失败的数据
		List<INSBOrderlistenpush> listOrder = insbOrderlistenpushService.queryListBytype("FAILED");
		LogUtil.info("进入==订单监听轮询任务===查到的插入失败数据条数=="+listOrder.size()+"====");
		this.taskLzgAddOrder(httpUrl, listOrder);
		
	}
	private void taskLzgAddOrder(String httpUrl,List<INSBOrderlistenpush> listOrderlistenpust){
		for (int i = 0; i < listOrderlistenpust.size(); i++) {
			if(listOrderlistenpust.get(i).getOperationtype().equals("1")){
				insbOrderlistenpushService.updateAddFail(listOrderlistenpust.get(i));
			}else if (listOrderlistenpust.get(i).getOperationtype().equals("3")){
				
				
				insbOrderlistenpushService.save4Again(listOrderlistenpust.get(i).getTaskid(), listOrderlistenpust.get(i).getSubtaskid(), listOrderlistenpust.get(i).getTaskname(), "1");
			}else if(listOrderlistenpust.get(i).getOperationtype().equals("4")){
				insbOrderlistenpushService.save4Again(listOrderlistenpust.get(i).getTaskid(), listOrderlistenpust.get(i).getSubtaskid(), listOrderlistenpust.get(i).getTaskname(), "4");

			}
		}
	}
}