package com.zzb.mobile.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCDept;
import com.common.CloudQueryUtil;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.conf.entity.INSBElfconf;
import com.zzb.conf.service.INSBTasksetService;
import com.zzb.mobile.dao.AppInsuredQuoteDao;
import com.zzb.mobile.entity.LastYearRiskInfo;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.lastinsured.LastYearPolicyBean;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
import com.zzb.mobile.service.AppLastYearRiskInfoService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class AppLastYearRiskInfoServiceImpl implements AppLastYearRiskInfoService {
	@Resource
	private INSBCarinfoDao carinfoDao;
	@Resource
	private AppInsuredQuoteDao appInsuredQuoteDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBTasksetService insbTasksetService;

	@Override
	public CommonModel getLastYearRiskInfo(LastYearRiskInfo lastYearRiskInfo) {
		CommonModel commonModel = new CommonModel();
		try {
			String taskid = lastYearRiskInfo.getProcessinstanceid();
			if (StringUtil.isEmpty(taskid)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("实例不能为空");
				return commonModel;
			}
			Map<String, String> input = lastYearRiskInfo.getInputData();
			if (null == input || input.size() < 0) {
				commonModel.setStatus("fail");
				commonModel.setMessage("必录项不能为空");
				return commonModel;
			}
			INSBCarinfo temp = new INSBCarinfo();
			temp.setTaskid(lastYearRiskInfo.getProcessinstanceid());
			temp = carinfoDao.selectOne(temp);
			String proid = temp.getPreinscode();
			if (StringUtil.isEmpty(proid)) {
				commonModel.setMessage("上年投保公司没查到");
			}
			String agentId = lastYearRiskInfo.getAgentId();
			if (StringUtil.isEmpty(agentId)) {
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人id不能为空");
				return commonModel;
			}
			// 查询投保区域,代理人所属机构是市区域
			INSCDept inscDept = appInsuredQuoteDao.sellectCityAreaByAgreeid(agentId);
			if (null == inscDept || StringUtil.isEmpty(inscDept.getCity())) {
				commonModel.setStatus("fail");
				commonModel.setMessage("代理人所属机构不存在");
				return commonModel;
			}
			// 查询报价表
			INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
			insbQuotetotalinfo.setTaskid(taskid);
			INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
			if (null == quotetotalinfo) {
				commonModel.setStatus("fail");
				commonModel.setMessage("报价总表信息不存在");
				return commonModel;
			}
			// 查询精灵信息
			INSBElfconf insbElfconf = appInsuredQuoteDao.selectOneElfconf(proid);
			String elfid = "";
			if (null != insbElfconf) {
				elfid = insbElfconf.getId();
			} else {
				commonModel.setStatus("fail");
				commonModel.setMessage("供应商精灵信息不存在");
				return commonModel;
			}
			LastYearPolicyInfoBean yearPolicyInfoBean = getLastYearPolicyInfoBean(input, taskid, proid, elfid,inscDept.getCity(), inscDept.getId());
			if("1".equals(yearPolicyInfoBean.getStatus())){
				commonModel.setStatus("fail");
				commonModel.setMessage("正在进行精灵查询...请稍后再试");
				return commonModel;
			}else{
				//组织返回数据
				LastYearPolicyBean lastYearPolicyBean = yearPolicyInfoBean.getLastYearPolicyBean();
				Map<String, Object> body = new HashMap<String, Object>();
				body.put("sumprem", lastYearPolicyBean!=null?lastYearPolicyBean.getSumprem():0);//总保费
				body.put("discount", lastYearPolicyBean!=null?lastYearPolicyBean.getDiscount():0);//折扣
				if(lastYearPolicyBean==null || lastYearPolicyBean.getDiscount()==0){  
					body.put("sumprice", lastYearPolicyBean!=null?lastYearPolicyBean.getSumprem():0);//原价
				}else{ 
					body.put("sumprice", lastYearPolicyBean.getSumprem()/lastYearPolicyBean.getDiscount());//原价
				}
				body.put("lastYearRiskinfos", lastYearPolicyBean!=null?yearPolicyInfoBean.getLastYearRiskinfos():"[]");
				commonModel.setBody(body);
				commonModel.setStatus("success");
				if(commonModel.getMessage()==null)
				commonModel.setMessage("查询上年险种信息成功！");
				System.out.println(JSONObject.fromObject(commonModel).toString());
				return commonModel;
			}
		} catch (Exception e) {
			commonModel.setStatus("faild");
			commonModel.setMessage("查询上年险种信息失败！");
			e.printStackTrace();
			System.out.println(JSONObject.fromObject(commonModel).toString());
			return commonModel;
		}
	}

	// 针对精灵
	private LastYearPolicyInfoBean getLastYearPolicyInfoBean(Map<String, String> input, String taskid, String pid,
			String elfid, String areaId, String deptid) {
		Map<String, String> map = insbTasksetService.getRenewalUserData(deptid);
		String userid = map.get("jabber");
		// 获取技能名称
		List<Map<String, String>> result = queryElfInfoByIdAndCode("outputItem", elfid);
		String abilityName = "";
		if (null != result && result.size() > 0) {
			abilityName = result.get(0).get("skillname");
		}
		// 云查询本地查询传入参数
		JSONObject object = new JSONObject();
		object.put("flag", "XB");
		object.put("robotId", pid);
		object.put("jabberId", userid);
		JSONObject inParas = new JSONObject();
		for (Map.Entry<String, String> entry : input.entrySet()) {
			inParas.put(entry.getKey().trim(), entry.getValue().trim());
		}
		object.put("inParas", inParas);
		object.put("areaId", areaId);
		object.put("abilityName", abilityName);
		object.put("eid", taskid);

		LastYearPolicyInfoBean lastYearPolicyInfoBean = null;
		try {
			String resultJson = CloudQueryUtil.getLastYearInsurePolicy(object.toString());
			JSONObject jsonObject = JSONObject.fromObject(resultJson);
			lastYearPolicyInfoBean = (LastYearPolicyInfoBean) JSONObject.toBean(jsonObject, LastYearPolicyInfoBean.class);
		} catch (Exception e) {
			lastYearPolicyInfoBean = null;
			e.printStackTrace();
		}
		return lastYearPolicyInfoBean;
	}
	
	private List<Map<String, String>> queryElfInfoByIdAndCode(String code,String elfid){
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", code);
		map.put("elfid", elfid);
		return appInsuredQuoteDao.selectInputCodeByElfId(map);
	}

}