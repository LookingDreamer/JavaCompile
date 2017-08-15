package com.zzb.conf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.dao.INSBAutoconfigDao;
import com.zzb.conf.dao.INSBAutoconfigshowDao;
import com.zzb.conf.dao.INSBEdiconfigurationDao;
import com.zzb.conf.dao.INSBElfconfDao;
import com.zzb.conf.entity.INSBAutoconfig;
import com.zzb.conf.entity.INSBAutoconfigshowQueryModel;
import com.zzb.conf.entity.INSBEdiconfiguration;
import com.zzb.conf.entity.INSBElfconf;
import com.zzb.conf.service.INSBAutoconfigService;
import com.zzb.conf.service.INSBAutoconfigshowService;

@Service
@Transactional
@Deprecated
/**
 * It's deprecated. Please use <code>INSBAutoconfigshowServiceImpl</code> instead.
 */
public class INSBAutoconfigServiceImpl extends BaseServiceImpl<INSBAutoconfig>
		implements INSBAutoconfigService {
	@Resource
	private INSBAutoconfigDao insbAutoconfigDao;
	@Resource
	private INSCDeptDao deptDao;
	@Resource
	private INSBElfconfDao elfconfDao;
	@Resource
	private INSBEdiconfigurationDao ediconfigurationDao;
	@Resource
	private INSBAutoconfigshowDao autoconfigshowDao;
	
	@Resource
	private INSBAutoconfigshowService autoconfigshowService;
	
	/**
	 * 根据供应商id查询自动化配置信息
	 * @return
	 */
	@Override
	public List<String> queryByProId(Map<String, Object> map){
		return autoconfigshowDao.queryByProId(map);
	}
	
	@Override
	protected BaseDao<INSBAutoconfig> getBaseDao() {
		return insbAutoconfigDao;
	}

	@Override
	public int deleteByAgreementId(String agreementid, String conftype) {
		return autoconfigshowDao.deleteByAgreementId(agreementid, conftype);
	}
 
	@Override
	public int deleteByeElfId(String elfid, String quoteType) {
		return autoconfigshowDao.deletebyelfid(elfid, quoteType);
	}

	@Override
	public Map<String,String> saveEdiConfig(String type,String showid,String providerId, String deptId,String codevalue, String conftype, INSCUser user) {
		Map<String,String> result = new HashMap<String,String>();
		
		// 查出当前已经存在的网点
		Map<String, String> param = new HashMap<String, String>();
		param.put("contentid", codevalue);
		param.put("providerid", providerId);
		List<Map<String,String>> oldList = autoconfigshowDao.selectComcodeBbyContenIdAndProviderId(param);
		
		
		List<String> addDeptIdList = new ArrayList<String>();
		List<String> oldDeptIdList = new ArrayList<String>();
		List<String> updateDeptIdList = new ArrayList<String>();
		for(Map<String,String> map:oldList){
			oldDeptIdList.add(map.get("deptid"));
			
		}
		
		
		INSCDept deptModel = deptDao.selectByComcode(deptId);
		if (!deptModel.getComtype().equals("05")) {
			// 得到当前机构下所有网点级子机构
			List<String> allComcode = deptDao.selectComCodeByComtypeAndParentCodes4EDI(deptId);
			
			addDeptIdList.addAll(allComcode);
			updateDeptIdList.addAll(allComcode);
			
			
			//不存在的新增
			addDeptIdList.removeAll(oldDeptIdList);
			if (addDeptIdList != null && !addDeptIdList.isEmpty()) {
				List<INSBAutoconfig> addList = new ArrayList<INSBAutoconfig>();
				for (String deptCode : addDeptIdList) {
					INSBAutoconfig autoModel = new INSBAutoconfig();
//					autoModel.setCodevalue(codevalue);
					autoModel.setDeptid(deptCode);
					autoModel.setCreatetime(new Date());
					autoModel.setProviderid(providerId);
					autoModel.setConftype(conftype);
					LogUtil.info(codevalue+"--1---codevalue");
					autoModel.setContentid(codevalue);
					// 默认是传统的 TODO
					autoModel.setQuotetype(type);
					autoModel.setOperator(user.getUsercode());
					autoModel.setCodevalue(showid);
					addList.add(autoModel);
				}
				insbAutoconfigDao.insertInBatch(addList);
			}
			
			//重复的修改
			updateDeptIdList.retainAll(oldDeptIdList);
			if (updateDeptIdList != null && !updateDeptIdList.isEmpty()) {
				for(String updateDeptId:updateDeptIdList){
					for(Map<String,String> map:oldList){
						if(updateDeptId.equals(map.get("deptid"))){
							INSBAutoconfig autoModel = new INSBAutoconfig();
							autoModel.setId(map.get("id"));
//							autoModel.setCodevalue(codevalue);
							autoModel.setDeptid(updateDeptId);
							autoModel.setCreatetime(new Date());
							autoModel.setProviderid(providerId);
							autoModel.setConftype(conftype);
							LogUtil.info(codevalue+"--2---codevalue");
							autoModel.setContentid(codevalue);
							// 默认是传统的 TODO
							autoModel.setQuotetype(type);
							autoModel.setOperator(user.getUsercode());
							autoModel.setCodevalue(showid);
							insbAutoconfigDao.updateById(autoModel);
//							addList.add(autoModel);
							break;
						}
					}
				}
//				insbAutoconfigDao.updateInBatch(addList);
			}
			result.put("msg", "修改成功");
			return result;
		}else{
			//新增
			if(oldDeptIdList==null || oldDeptIdList.isEmpty()){
				INSBAutoconfig autoModel = new INSBAutoconfig();
//				autoModel.setCodevalue(codevalue);
				autoModel.setDeptid(deptId);
				autoModel.setCreatetime(new Date());
				autoModel.setProviderid(providerId);
				autoModel.setConftype(conftype);
				// 默认是传统的 TODO
				autoModel.setQuotetype(type);
				autoModel.setOperator(user.getUsercode());
				LogUtil.info(codevalue+"--3---codevalue");
				autoModel.setCodevalue(showid);
				autoModel.setContentid(codevalue);
				insbAutoconfigDao.insert(autoModel);
				result.put("msg", "新增成功");
				return result;
			}else if(oldDeptIdList.contains(deptId)){
				for(Map<String,String> map:oldList){
					if(deptId.equals(map.get("deptid"))){
						INSBAutoconfig autoModel = new INSBAutoconfig();
						autoModel.setId(map.get("id"));
//						autoModel.setCodevalue(codevalue);
						autoModel.setDeptid(deptId);
						autoModel.setCreatetime(new Date());
						autoModel.setProviderid(providerId);
						autoModel.setConftype(conftype);
						LogUtil.info(codevalue+"--4---codevalue");
						autoModel.setContentid(codevalue);
						// 默认是传统的 TODO
						autoModel.setQuotetype(type);
						autoModel.setOperator(user.getUsercode());
						autoModel.setCodevalue(showid);
						insbAutoconfigDao.updateById(autoModel);
					}
				}
				result.put("msg", "修改成功！");
				return result;
			}else{
				INSBAutoconfig autoModel = new INSBAutoconfig();
//				autoModel.setCodevalue(codevalue);
				autoModel.setDeptid(deptId);
				autoModel.setCreatetime(new Date());
				autoModel.setProviderid(providerId);
				autoModel.setConftype(conftype);
				LogUtil.info(codevalue+"--5---codevalue");
				autoModel.setContentid(codevalue);
				// 默认是传统的 TODO
				autoModel.setQuotetype(type);
				autoModel.setOperator(user.getUsercode());
				autoModel.setCodevalue(showid);
				insbAutoconfigDao.insert(autoModel);
				result.put("msg", "新增成功");
				return result;
			}
		}
	}

	@Override
	public String getEpathByAutoConfig(Map<String, String> param) {
		String contendId = autoconfigshowDao.selectContendIdByParam(param);
		if(contendId==null){
			return null;
		}
		if(param.get("quotetype")==null){
			return null;
		}
		
		//当前查询是精灵信息
		if("02".equals(param.get("quotetype"))){
			INSBElfconf elfModel =  elfconfDao.selectById(contendId);
			return elfModel.getElfpath();		
		}else if("01".equals(param.get("quotetype"))){
			INSBEdiconfiguration ediModel =  ediconfigurationDao.selectById(contendId);
			return ediModel.getInterfaceaddress();
		}
		return null;
	}


	/** 
	 * 根据confshow表id删除能力配置表
	 * @see com.zzb.conf.service.INSBAutoconfigService#deleteautobyshowid(java.lang.String)
	 */
	@Override
	public int deleteautobyshowid(String showid,String parentcodes,String deptid) {
			INSCDept dept = new INSCDept();
			dept.setParentcodes(parentcodes);
			List<INSCDept> deptlist = deptDao.selectDeptlikeparentcodes(parentcodes+"+"+deptid,deptid);
			int sum = 0;
			for (int i = 0; i < deptlist.size(); i++) {
				dept = deptlist.get(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("codevalue", showid);
				map.put("deptid", dept.getId());
				autoconfigshowDao.deleteautobyshowid(map);
				sum ++;
			}
			return sum;
	}

	
	@Override
	public void deleteautobyshowid(String id,String quotetype) {
		autoconfigshowDao.deleteautobyshowid(id,quotetype);
	}

	/**
	 * 监控用zd
	 * 
	 * hxx 同一个保险公司 网点  都走一个EDI/精灵配置
	 * 
	 */
	@Override
	public List<Object> getEpathByAutoConfig4New(Map<String, String> param) {
		List<Object> returnList = new ArrayList<>();
		
		
		List<String> tempDept = new ArrayList<String>();
		tempDept.add(param.get("quotetype"));
		
		INSBAutoconfigshowQueryModel queryModel = new INSBAutoconfigshowQueryModel();
		queryModel.setProviderid(param.get("providerid"));
		queryModel.setDeptId(param.get("deptid"));
		queryModel.setQuoteList(tempDept);
		queryModel.setConftype(param.get("conftype"));
		Map<String,Object>  resultMap = autoconfigshowService.getElfEdiByNearestDept(queryModel);
		
		
		if(resultMap.get("elf") !=null){
			returnList.add(resultMap.get("elf"));
		}
		if(resultMap.get("edi")!=null){
			returnList.add(resultMap.get("edi"));
		}
		
		if(returnList.size()>0){
			return returnList;
		}else{
			return null;
		}
		
		
		
//		INSBAutoconfig queryInsbAutoconfig = new INSBAutoconfig();
//		queryInsbAutoconfig.setQuotetype(param.get("quotetype"));
//		queryInsbAutoconfig.setDeptid(param.get("deptid"));
//		queryInsbAutoconfig.setProviderid(param.get("providerid"));
//		queryInsbAutoconfig.setConftype(param.get("conftype"));
//		List<INSBAutoconfig> reusltInsbAutoconfigs = insbAutoconfigDao.selectList(queryInsbAutoconfig);
//		
//		for(INSBAutoconfig dataInsbAutoconfig : reusltInsbAutoconfigs){
//			if(!StringUtil.isEmpty(dataInsbAutoconfig.getContentid())){
//				if("02".equals(dataInsbAutoconfig.getQuotetype())){
//					INSBElfconf elfModel =  elfconfDao.selectById(dataInsbAutoconfig.getContentid());
//					returnList.add(elfModel);
//				}else if("01".equals(dataInsbAutoconfig.getQuotetype())){
//					INSBEdiconfiguration ediModel =  ediconfigurationDao.selectById(dataInsbAutoconfig.getContentid());
//					returnList.add(ediModel);
//				}
//			}
//		}
//		if(returnList.size()>0){
//			return returnList;
//		}else{
//			return null;
//		}
//		
	
	}

}