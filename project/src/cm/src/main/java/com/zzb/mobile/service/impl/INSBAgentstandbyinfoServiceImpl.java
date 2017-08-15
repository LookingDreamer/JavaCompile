package com.zzb.mobile.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.LobUtil;
import com.cninsure.core.utils.LogUtil;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.mobile.dao.INSBAgentstandbyinfoDao;
import com.zzb.mobile.entity.INSBAgentstandbyinfo;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.INSBAgentstandbyinfoService;

@Service
@Transactional
public class INSBAgentstandbyinfoServiceImpl extends BaseServiceImpl<INSBAgentstandbyinfo> implements
		INSBAgentstandbyinfoService {
	@Resource
	private INSBAgentstandbyinfoDao insbAgentstandbyinfoDao;
	@Resource
	private INSCCodeService codeService;

	@Override
	protected BaseDao<INSBAgentstandbyinfo> getBaseDao() {
		return insbAgentstandbyinfoDao;
	}


	@Override
	public CommonModel getQuickBrushTypeList() {
		Map<Object, Object> resulemap =new HashMap<Object, Object>();
		CommonModel commonModel = new CommonModel();
		List<INSCCode> quickBrushTypelist = codeService.queryINSCCodeByCode("QuickBrushType", "QuickBrushType");
		LogUtil.info("getQuickBrushTypeList" + JSONArray.fromObject(quickBrushTypelist).toString());
		if(null != quickBrushTypelist && !"".equals(quickBrushTypelist)){
			for(INSCCode ic : quickBrushTypelist){
				resulemap.put(ic.getCodevalue(),ic.getCodename());
			}
			commonModel.setBody(quickBrushTypelist);
			commonModel.setMessage("quickBrushTypelist");
			commonModel.setStatus("success");
		}else{
			commonModel.setMessage("暂无quickBrushTypelist信息");
			commonModel.setStatus("fail");
		}
		return commonModel;
	}

	@Override
	public CommonModel getQuickBrushType(Map<Object, Object> hashMap) {
		CommonModel commonModel = new CommonModel();
		String agentId = (String) hashMap.get("agentId");
		INSBAgentstandbyinfo temp = new INSBAgentstandbyinfo();
		temp.setAgentid(agentId);
		INSBAgentstandbyinfo agentstandbyinfo = insbAgentstandbyinfoDao.selectOne(temp);
		if(null != agentstandbyinfo && !"".equals(agentstandbyinfo)){
			commonModel.setMessage(agentstandbyinfo.getQuickBrushType());
			commonModel.setStatus("success");
		}else{
			commonModel.setMessage("没有设置快刷类型！");
			commonModel.setStatus("fail");
		}
		return commonModel;
	}

	@Override
	public CommonModel saveOrUpdataQuickBrushType(Map<Object, Object> hashMap) {
		CommonModel commonModel = new CommonModel();
		String agentId = (String) hashMap.get("agentId");
		String quickBrushType = (String) hashMap.get("quickBrushType");
		INSBAgentstandbyinfo temp = new INSBAgentstandbyinfo();
		temp.setAgentid(agentId);
		INSBAgentstandbyinfo agentstandbyinfo = insbAgentstandbyinfoDao.selectOne(temp);
		if(null == agentstandbyinfo || "".equals(agentstandbyinfo)){
			INSBAgentstandbyinfo agentquickbrushtype = new INSBAgentstandbyinfo();
			agentquickbrushtype.setAgentid(agentId);
			agentquickbrushtype.setQuickBrushType(quickBrushType);
			insbAgentstandbyinfoDao.insert(agentquickbrushtype);
			commonModel.setMessage("inster");
			commonModel.setStatus("success");
			return commonModel;
		}else{
			agentstandbyinfo.setQuickBrushType(quickBrushType);
			int i = insbAgentstandbyinfoDao.updateById(agentstandbyinfo);
			if(i>0){
				commonModel.setMessage("update");
				commonModel.setStatus("success");
			}else{
				commonModel.setMessage("update");
				commonModel.setStatus("fail");
			}
			return commonModel;
		}
	}
}