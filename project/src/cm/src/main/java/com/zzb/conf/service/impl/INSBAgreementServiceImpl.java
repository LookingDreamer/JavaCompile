package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.dao.INSBAgreementDao;
import com.zzb.conf.dao.INSBAutoconfigDao;
import com.zzb.conf.dao.INSBAutoconfigshowDao;
import com.zzb.conf.dao.INSBElfconfDao;
import com.zzb.conf.dao.INSBProviderDao;
import com.zzb.conf.dao.INSBProviderandediDao;
import com.zzb.conf.dao.INSBRuleEngineDao;
import com.zzb.conf.entity.INSBAgreement;
import com.zzb.conf.entity.INSBAutoconfig;
import com.zzb.conf.entity.INSBAutoconfigshow;
import com.zzb.conf.entity.INSBElfconf;
import com.zzb.conf.service.INSBAgreementService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsbSaveTruleParms;

@Service
@Transactional
public class INSBAgreementServiceImpl extends BaseServiceImpl<INSBAgreement>
		implements INSBAgreementService {
	@Resource
	private INSBAgreementDao insbAgreementDao;
	@Resource
	private INSBElfconfDao elfconfDao;
	@Resource
	private INSBProviderandediDao providerandediDao;
//	@Resource
//	private INSBAutoconfigDao autoconfigDao;
	@Resource
	private INSBAutoconfigshowDao autoconfigshowDao;
	@Resource
	private INSCCodeDao codeDao;
	@Resource
	private INSBRuleEngineDao ruleEngineDao;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSBProviderDao providerDao;
	@Resource
	private INSBQuoteinfoDao insbQuoteinfoDao;
	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;

	@Override
	protected BaseDao<INSBAgreement> getBaseDao() {
		return insbAgreementDao;
	}

	@Override
	public Long queryCountVo(INSBAgreement agreement) {
		return insbAgreementDao.queryCountVo(agreement);
	}

	@Override
	public List<INSBAgreement> queryListVo(INSBAgreement agreement) {
		return insbAgreementDao.queryListVo(agreement);
	}

	@Override
	public List<INSBElfconf> queryElfByProviderId(String providerid) {
		List<INSBElfconf> elfList = elfconfDao.selectByPid(providerid);
		return elfList;
	}

	@Override
	public List<Map<String, String>> queryEDIByProviderId(String providerid) {
		List<Map<String, String>> ediList = providerandediDao
				.selectByPid(providerid);
		return ediList;
	}

	@Override
	public String saveAutoConfig(INSBAutoconfigshow autoconfig) {

		return autoconfigshowDao.insertReturnId(autoconfig);
	}

	@Override
	public String updateAutoConfig(INSBAutoconfigshow autoconfig) {
		autoconfigshowDao.updateById(autoconfig);
		return autoconfig.getId();
	}

	@Override
	public Map<String, Object> initProcessAutoData(String agreementId) {
		Map<String, Object> result = new HashMap<String, Object>();

		// 续保
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentcode", "processAuto");
		map.put("codetype", "renewal");
		List<INSCCode> renewalList = codeDao.selectINSCCodeByParentCode(map);

		// 核保
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("parentcode", "processAuto");
		map1.put("codetype", "underwriting");
		List<INSCCode> underwritingList = codeDao
				.selectINSCCodeByParentCode(map1);

		// 承保
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("parentcode", "processAuto");
		map3.put("codetype", "contract");
		List<INSCCode> contractList = codeDao.selectINSCCodeByParentCode(map3);

		// 报价
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("parentcode", "processAuto");
		map2.put("codetype", "quotetype");
		List<INSCCode> quotetypeList = codeDao.selectINSCCodeByParentCode(map2);

		result.put("quotetypeList", quotetypeList);
		result.put("underwritingList", underwritingList);
		result.put("renewalList", renewalList);
		result.put("contractList", contractList);

		return result;
	}

	@Override
	public String selectByAgreementId(String agreementId, String conftype) {
		Map<String, String> para = new HashMap<String, String>();
		if (!StringUtil.isEmpty(agreementId)) {
			para.put("agreementid", agreementId);
		}
		if (!StringUtil.isEmpty(conftype)) {
			para.put("conftype", conftype);
		}
		return autoconfigshowDao.selectByAgreementId(para);
	}

	@Override
	public Map<String, Object> initEditePageData(String id) {
		// 编辑页面能拿到
		Map<String, Object> result = new HashMap<String, Object>();
		INSBAgreement model = null;

		// 传统规则参数
		Map<String, Object> paramMap4Tradition = new HashMap<String, Object>();
		paramMap4Tradition.put("rule_base_type", "2");

		// 调度规则
		Map<String, Object> paramMap4Dispatch = new HashMap<String, Object>();
		paramMap4Dispatch.put("rule_base_type", "4");

		// 新增协议时 不进行规则初始化
		if (id != null) {
			model = insbAgreementDao.selectById(id);

			// if (model.getProviderid() != null&&
			// !"".equals(model.getProviderid())) {
			// paramMap4Tradition.put("company_id", model.getProviderid());
			// paramMap4Dispatch.put("company_id", model.getProviderid());
			// }

			if (model.getDeptid() != null && !"".equals(model.getDeptid())) {
				INSCDept deptModel = deptDao.selectById(model.getDeptid());
				paramMap4Tradition.put("city_id", deptModel.getCity());
				paramMap4Dispatch.put("city_id", deptModel.getCity());
			}

			// 传统
			List<Map<String, Object>> ruleList2 = ruleEngineDao
					.selectByParamMap(paramMap4Tradition);

			// 任务规则
			List<Map<String, Object>> ruleList4 = ruleEngineDao
					.selectByParamMap(paramMap4Dispatch);

			// 传统车险
			for (Map<String, Object> map3 : ruleList2) {
				if (model != null) {
					if (model.getAgreementtrule().equals(
							map3.get("rule_engine_id").toString())) {
						map3.put("ck", "1");
					} else {
						map3.put("ck", "0");
					}
				}
			}

			// 任务调度
			for (Map<String, Object> map4 : ruleList4) {
				if (model != null) {
					if (model.getAgreementtrule() == map4.get("id")) {
						map4.put("ck", "1");
					} else {
						map4.put("ck", "0");
					}
				}
			}

			result.put("ruleList2", ruleList2);
			result.put("ruleList4", ruleList4);

			return result;
		} else {
			return result;
		}

	}

	@Override
	public Map<String, Object> initRuleByProviderId(String deptId) {

		//根据deptId获取所有子级deptId
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> result2=new ArrayList<Map<String,Object>>();
		List<String> deptIdsList=deptDao.selectAllChildren(deptId);
		for (String string : deptIdsList) {
			Map<String, Object> paramMap4Tradition = new HashMap<String, Object>();
			INSCDept deptModel = deptDao.selectById(string);
			String cityId = deptModel.getCity();
			paramMap4Tradition.put("rule_base_type", "2");//传统
			paramMap4Tradition.put("city_id", cityId);
			List<Map<String, Object>> result21 = ruleEngineDao
					.selectByParamMap(paramMap4Tradition);
			for (Map<String, Object> map : result21) {
				result2.add(map);
			}
		}
		result.put("result2", result2);
		return result;
	}
	@Override
	public List<INSBAgreement> queryListAgreement(Map<String, Object> map) {
		List<INSBAgreement> result = insbAgreementDao.queryAgreement(map);
		for (INSBAgreement insbAgreement : result) {
			insbAgreement.setDeptid(deptDao.selectById(
					insbAgreement.getDeptid()).getComname());
			insbAgreement.setProviderid(providerDao.selectById(
					insbAgreement.getProviderid()).getPrvshotname());
			if ("1".equals(insbAgreement.getAgreementstatus())) {
				insbAgreement.setAgreementstatus("已生效");
			} else {
				insbAgreement.setAgreementstatus("未生效");
			}
		}
		return insbAgreementDao.queryAgreement(map);
	}

	@Override
	public List<INSBAgreement> queryAgreementByComecode(Map<String, Object> map) {
		List<INSBAgreement> result = insbAgreementDao.queryAgreementByComecode(map);
		for (INSBAgreement insbAgreement : result) {
			insbAgreement.setDeptid(deptDao.selectById(
					insbAgreement.getDeptid()).getComname());
			insbAgreement.setProviderid(providerDao.selectById(
					insbAgreement.getProviderid()).getPrvshotname());
			if ("1".equals(insbAgreement.getAgreementstatus())) {
				insbAgreement.setAgreementstatus("已生效");
			} else {
				insbAgreement.setAgreementstatus("未生效");
			}
		}
		return result;
	}

	@Override
	public long queryCountAgreement(Map<String, Object> map) {
		map.remove("offset");
		map.remove("limit");
		List<INSBAgreement> list = insbAgreementDao.queryAgreement(map);
		return list.size();
	}

	@Override
	public List<Map<Object, Object>> queryDeptList(String parentcode,
			String deptId) {
		if (StringUtil.isEmpty(parentcode)
				|| "source".equalsIgnoreCase(parentcode)) {
			parentcode = "";
		}
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object, Object>>();

		INSCDept deptModel = deptDao.selectById(deptId);
		String comgrade = deptModel.getComgrade();
		int comgradeInt = Integer.valueOf(comgrade.substring(1));
		String[] parentCodesArray = deptModel.getParentcodes().split("\\+");

		List<INSCDept> inscListDept = deptDao.selectByParentDeptCode(parentcode);
		String tempComgrade = inscListDept.get(0).getComgrade();
		int tempComgradeInt ;
		if("".equals(tempComgrade)){
			tempComgradeInt= 0;
		}else{
			tempComgradeInt= Integer.valueOf(tempComgrade.substring(1));
		}
		
		
		for (int i = 0; i < inscListDept.size(); i++) {
			if (tempComgradeInt < comgradeInt) {
				for (String pStr : parentCodesArray) {
					if (inscListDept.get(i).getComcode().equals(pStr)) {
						INSCDept tempDept = new INSCDept();
						Map<Object, Object> tempMap = new HashMap<Object, Object>();
						tempDept = inscListDept.get(i);
						tempMap.put("id", tempDept.getId());
						tempMap.put("pid", tempDept.getComcode());
						tempMap.put("name", tempDept.getComname());
						tempMap.put("isParent", "1".equals(tempDept
								.getChildflag()) ? "true" : "false");
						resultList.add(tempMap);
					}
				}
			} else if(tempComgradeInt == comgradeInt){
				INSCDept tempDept = new INSCDept();
				Map<Object, Object> tempMap = new HashMap<Object, Object>();
				tempDept = deptModel;

				tempMap.put("id", tempDept.getId());
				tempMap.put("pid", tempDept.getComcode());
				tempMap.put("name", tempDept.getComname());
				tempMap.put("isParent", "1".equals(tempDept
						.getChildflag()) ? "true" : "false");
				resultList.add(tempMap);
				break;
			}else {
				INSCDept tempDept = new INSCDept();
				Map<Object, Object> tempMap = new HashMap<Object, Object>();
				tempDept = inscListDept.get(i);
				tempMap.put("id", tempDept.getId());
				tempMap.put("pid", tempDept.getComcode());
				tempMap.put("name", tempDept.getComname());
				tempMap.put("isParent",
						"1".equals(tempDept.getChildflag()) ? "true" : "false");
				resultList.add(tempMap);
			}
		}
		return resultList;
	}

	@Override
	public List<Map<String, String>> queryTreeListByAgr(String parentcode,
			String comtype, String deptId) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		List<INSCDept> inscListDept = queryDeptListByPidAgr(parentcode, comtype);
		
		if("1200000000".equals(deptId)){
			for( INSCDept model:inscListDept){
				Map<String, String> tempMap = new HashMap<String, String>();

				tempMap.put("id", model.getId());
				tempMap.put("pid", model.getComcode());
				tempMap.put("name", model.getComname());
				tempMap.put("isParent",queryDeptListByPidAgr(model.getComcode(), comtype).size() > 0 ? "true"
						: "false");
				list.add(tempMap);
			}
			return list;
		}
		
		
		INSCDept deptModel = deptDao.selectById(deptId);
		String comgrade = deptModel.getComgrade();
		int comgradeInt = Integer.valueOf(comgrade.substring(1));
		String[] parentCodesArray = deptModel.getParentcodes().split("\\+");

		String tempComgrade = inscListDept.get(0).getComgrade();
		int tempComgradeInt ;
		if("".equals(tempComgrade)){
			tempComgradeInt= 0;
		}else{
			tempComgradeInt= Integer.valueOf(tempComgrade.substring(1));
		}
		
		
		
		for (int i = 0; i < inscListDept.size(); i++) {
			if (tempComgradeInt < comgradeInt) {
				for (String pStr : parentCodesArray) {
					if (inscListDept.get(i).getComcode().equals(pStr)) {
						INSCDept tempDept = new INSCDept();
						Map<String, String> tempMap = new HashMap<String, String>();
						tempDept = inscListDept.get(i);
						tempMap.put("id", tempDept.getId());
						tempMap.put("pid", tempDept.getComcode());
						tempMap.put("name", tempDept.getComname());
						tempMap.put("isParent",queryDeptListByPidAgr(inscListDept.get(i).getComcode(), comtype).size() > 0 ? "true"
								: "false");
						list.add(tempMap);
					}
				}
			} else if(tempComgradeInt == comgradeInt){
				INSCDept tempDept = new INSCDept();
				Map<String, String> tempMap = new HashMap<String, String>();
				tempDept = deptModel;

				tempMap.put("id", tempDept.getId());
				tempMap.put("pid", tempDept.getComcode());
				tempMap.put("name", tempDept.getComname());
				tempMap.put("isParent",queryDeptListByPidAgr(inscListDept.get(i).getComcode(), comtype).size() > 0 ? "true"
						: "false");
				list.add(tempMap);
				break;
			}else {
				INSCDept tempDept = new INSCDept();
				Map<String, String> tempMap = new HashMap<String, String>();
				tempDept = inscListDept.get(i);
				tempMap.put("id", tempDept.getId());
				tempMap.put("pid", tempDept.getComcode());
				tempMap.put("name", tempDept.getComname());
				tempMap.put("isParent",queryDeptListByPidAgr(inscListDept.get(i).getComcode(), comtype).size() > 0 ? "true"
						: "false");
				list.add(tempMap);
			}
		}
		return list;
	}
	private List<INSCDept> queryDeptListByPidAgr(String upcomcode,
			String comtype) {
		if (StringUtil.isEmpty(upcomcode)
				|| "source".equalsIgnoreCase(upcomcode)) {
			upcomcode = "";
		}
		Map<String, String> parm = new HashMap<String, String>();
		parm.put("upcomcode", upcomcode);
		if (!upcomcode.equals("")) {
			parm.put("comtype", comtype);
		}
		return deptDao.selectByParentDeptCodeAgr(parm);
	}

	@Override
	public List<String> getAllProvider(String comcode) {
		List<String> allProvider=insbAgreementDao.getAgreementProvider(comcode);
		return allProvider;
	}
	@Override
	public CommonModel getTrules(InsbSaveTruleParms model) {
		CommonModel mCommonModel=new CommonModel();
		Map<String, Object> map=new HashMap<String, Object>();
		List<String> cityIdsList=model.getCitys();
		try {
			map.put("cityids", cityIdsList);
			map.put("trulename", model.getTrulename()); 
			map.put("rule_base_type", "2");
			List<Map<String, Object>> result=ruleEngineDao.selectListByCity(map);
			mCommonModel.setStatus("success");
			mCommonModel.setMessage("查询成功！");
			mCommonModel.setBody(result);
			return mCommonModel;
		} catch (Exception e) {
			mCommonModel.setStatus("fail");
			mCommonModel.setMessage("查询失败！");
			return mCommonModel;
		}
	}

	@Override
	/**
	 * 协议核保功能查询 核保功能 1 : 开启  （默认） 0 ：关闭
	 */
	public Integer checkUnderwritestatus(String agreementid) {
		//协议核保功能查询 核保功能 1 : 开启  （默认） 0 ：关闭
		if (StringUtil.isNotEmpty(agreementid)) {
			INSBAgreement agreement = insbAgreementDao.selectById(agreementid);
			return agreement==null || agreement.getUnderwritestatus() == null ? 1:agreement.getUnderwritestatus();
		} else {
			return 1;
		}
	}

	@Override
	public INSBAgreement getAgreementByTaskIdAndInscomcode(String taskId, String insComCode) {
		INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
		insbQuotetotalinfo.setTaskid(taskId);
		insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
		if (insbQuotetotalinfo == null) {
			LogUtil.info("%s报价总表不存在记录", taskId);
			return null;
		}
		INSBQuoteinfo quoteinfo = new INSBQuoteinfo();
		quoteinfo.setQuotetotalinfoid(insbQuotetotalinfo.getId());
		quoteinfo.setInscomcode(insComCode);
		quoteinfo = insbQuoteinfoDao.selectOne(quoteinfo);
		if (quoteinfo == null) {
			LogUtil.info("%s报价分表不存在记录", taskId);
			return null;
		}
		return insbAgreementDao.selectById(quoteinfo.getAgreementid());
	}
}