package com.zzb.cm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBMultiplePriceListService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.model.CommonModel;
import com.zzb.model.SelectInsuredConfigBean;
import com.zzb.model.SelectInsuredConfigModel;
@Service
@Transactional
public class INSBMultiplePriceListServiceImpl implements
		INSBMultiplePriceListService {
	
	@Resource
	private AppInsuredQuoteDao appInsuredQuoteDao;
	@Resource
	private INSBPolicyitemDao insbPolicyitemDao;
	@Resource
	INSBQuotetotalinfoService insbQuotetotalinfoService;
	@Resource
	INSBQuoteinfoService insbQuoteinfoService;
	@Resource 
	INSBWorkflowsubService insbWorkflowsubService;
	@Resource
	INSBWorkflowmainService insbWorkflowmainService;
	
	@Override
	public CommonModel insuredConfList(String taskid, String inscomcode) {
		CommonModel commonModel = new CommonModel();
		try {
			if(StringUtil.isEmpty(taskid) || StringUtil.isEmpty(inscomcode)){
				commonModel.setStatus("fail");
				commonModel.setMessage("参数不能为空");
				return commonModel;
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskid", taskid);
			map.put("inscomcode", inscomcode);
			List<INSBCarkindprice> carkindprices = appInsuredQuoteDao.getInsuredConfigs(map);
			if(null == carkindprices || carkindprices.size() <= 0){
				commonModel.setStatus("fail");
				commonModel.setMessage("不存在保险配置信息");
				return commonModel;
			}
			SelectInsuredConfigModel configModel = new SelectInsuredConfigModel();
			List<SelectInsuredConfigBean> buessList = new ArrayList<SelectInsuredConfigBean>();
			List<SelectInsuredConfigBean> bjmpList = new ArrayList<SelectInsuredConfigBean>();;
			List<SelectInsuredConfigBean> vehicleList = new ArrayList<SelectInsuredConfigBean>();;
			double discountRate=0.0;
			double vehicleDiscountRate=0.0;
			List<INSBPolicyitem> rateBeans=insbPolicyitemDao.getListByMap(map);
			if (rateBeans!=null) {
				for (INSBPolicyitem Policyitems : rateBeans) {
					if ("1".equals(Policyitems.getRisktype())&&Policyitems.getDiscountRate()!=null) {
						vehicleDiscountRate+=Policyitems.getDiscountRate().doubleValue();
					}
					if ("0".equals(Policyitems.getRisktype())&&Policyitems.getDiscountRate()!=null) {
						discountRate+=Policyitems.getDiscountRate().doubleValue();
					}
				}
			}
			if (discountRate==0.0) {
				discountRate=1.0;
			}
            if (vehicleDiscountRate==0.0) {
            	vehicleDiscountRate=1.0;
			}
			double totalamountprice = 0.0;
			for(INSBCarkindprice insbCarkindprice : carkindprices){
				SelectInsuredConfigBean bean = new SelectInsuredConfigBean();
				bean.setRiskname(insbCarkindprice.getRiskname());
				bean.setInskindcode(insbCarkindprice.getRiskcode());
				bean.setDiscountCharge(insbCarkindprice.getDiscountCharge());
				bean.setAmount(insbCarkindprice.getAmount());
				if("0".equals(insbCarkindprice.getInskindtype())){
					if(null != insbCarkindprice.getDiscountCharge()){
						totalamountprice += insbCarkindprice.getDiscountCharge();
					}
					buessList.add(bean);
				}else if("1".equals(insbCarkindprice.getInskindtype())){
					if(null != insbCarkindprice.getDiscountCharge()){
						totalamountprice += insbCarkindprice.getDiscountCharge();
					}
					bjmpList.add(bean);
				}else{
					vehicleList.add(bean);
				}
			}
			configModel.setBuessList(buessList);
			configModel.setBjmpList(bjmpList);
			configModel.setVehicleList(vehicleList);
			configModel.setDiscountRate(discountRate);
			configModel.setTotalamountprice(totalamountprice);
			configModel.setVehicleDiscountRate(vehicleDiscountRate);
			configModel.setProcessstatus(getProcessStatus(taskid, inscomcode));
			commonModel.setStatus("success");
			commonModel.setMessage("查询成功");
			commonModel.setBody(configModel);
		} catch (Exception e) {
			e.printStackTrace();
			commonModel.setStatus("fail");
			commonModel.setMessage("操作失败");
		}
		return commonModel;
	}

	@Override
	public Map<String, Object> getDeptInfo(String taskid, String inscomcode) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("taskid", taskid);
		map.put("inscomcode", inscomcode);
		return appInsuredQuoteDao.getDeptInfo(map);
	}
	
	/**
	 * 查询子流程是否处于选择投保节点
	 */
	public String getProcessStatus(String taskid,String provid){
		String result;
		INSBQuotetotalinfo totalInfo = new INSBQuotetotalinfo();
		totalInfo.setTaskid(taskid);
		totalInfo = insbQuotetotalinfoService.queryOne(totalInfo);
		INSBQuoteinfo quoteInfo = new INSBQuoteinfo();
		quoteInfo.setQuotetotalinfoid(totalInfo.getId());
		quoteInfo.setInscomcode(provid);
		quoteInfo = insbQuoteinfoService.queryOne(quoteInfo);
		String subInstanceid = quoteInfo.getWorkflowinstanceid();
		INSBWorkflowsub workflowsub = new INSBWorkflowsub();
		workflowsub.setInstanceid(subInstanceid);
		workflowsub = insbWorkflowsubService.queryOne(workflowsub);
		String workflowcode = workflowsub.getTaskcode();
		if ("33".equals(workflowcode)) {
			INSBWorkflowmain main = new INSBWorkflowmain();
			main.setInstanceid(taskid);
			INSBWorkflowmain main2 = insbWorkflowmainService.queryOne(main);
			workflowcode = main2.getTaskcode();
		}
		List<String> list1 = Arrays.asList("1","2","3","4","6","7","8","31","32","15");//报价中
		List<String> list2 = Arrays.asList("18","17","16");//核保中
		List<String> list3 = Arrays.asList("25","26","27","28");//支付成功
		List<String> list4 = Arrays.asList("23","24","29");//承保完成
		if (list1.contains(workflowcode)) {
			result = "1";//报价中
		}else if(list2.contains(workflowcode)){
			result = "2";//核保中
		}else if ("14".equals(workflowcode)) {
			result = "0";//报价成功
		}else if ("13".equals(workflowcode)) {
			result = "3";//退回修改
		}else if ("19".equals(workflowcode)) {
			result = "4";//核保退回
		}else if ("20".equals(workflowcode)) {
			result = "5";//核保成功
		}else if ("21".equals(workflowcode)) {
			result = "6";//支付确认中
		}else if (list3.contains(workflowcode)) {
			result = "7";//支付成功
		}else if (list4.contains(workflowcode)) {
			result = "8";//承保完成
		}else if ("30".equals(workflowcode)) {
			result = "9";//拒绝承保
		}else if ("34".equals(workflowcode)) {
			result = "10";//放弃承保
		}else if ("36".equals(workflowcode)) {
			result = "11";//暂停支付
		}else {
			result = "12";//已结束
		}
		return result;
	}
}
