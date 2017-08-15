package com.cninsure.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.service.INSCDeptService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class INSCDeptServiceImpl extends BaseServiceImpl<INSCDept> implements
		INSCDeptService {
	@Resource
	private INSCDeptDao inscDeptDao;
	
	@Override
	protected BaseDao<INSCDept> getBaseDao() {
		return inscDeptDao;
	}

	// @Cacheable(value="deptCache", key="#parentcode")
	@Override
	public List<Map<Object, Object>> queryDeptList(String parentcode) {
		LogUtil.info("dept tree parentinnercode= " + parentcode);
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object, Object>>();
		List<INSCDept> inscListDept = queryDeptListByPid(parentcode);
		for (int i = 0; i < inscListDept.size(); i++) {
			INSCDept tempDept = new INSCDept();
			Map<Object, Object> tempMap = new HashMap<Object, Object>();
			tempDept = inscListDept.get(i);

			/* zTree数据 */
			tempMap.put("id", tempDept.getId());
			tempMap.put("pid", tempDept.getComcode());
			tempMap.put("name", tempDept.getComname());
			tempMap.put("isParent",
					"1".equals(tempDept.getChildflag()) ? "true" : "false");

			resultList.add(tempMap);
		}
		return resultList;
	}

	@Override
	public int addDeptData(INSCDept dept) {

		return inscDeptDao.addDeptDatas(dept);
	}

	private List<INSCDept> queryDeptListByPid(String parentcode) {
		if (StringUtil.isEmpty(parentcode)
				|| "source".equalsIgnoreCase(parentcode)) {
			parentcode = "";
		}
		return inscDeptDao.selectByParentDeptCode(parentcode);
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
		return inscDeptDao.selectByParentDeptCodeAgr(parm);
	}

	@Override
	public List<Map<String, String>> queryTreeList(String parentcode) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<INSCDept> inscListDept = queryDeptListByPid(parentcode);
		for (INSCDept dept : inscListDept) {
			if ( StringUtil.isNotEmpty(dept.getComname()) ) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", dept.getId());
				map.put("pId", dept.getUpcomcode());
				map.put("name", dept.getComname());
				map.put("comtype", dept.getComtype());
				map.put("isParent", "1".equals(dept.getChildflag()) ? "true"
						: "false");
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public List<Map<String, String>> queryTreeListByAgr(String parentcode,
			String comtype) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<INSCDept> inscListDept = queryDeptListByPidAgr(parentcode, comtype);
		for (INSCDept dept : inscListDept) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", dept.getId());
			map.put("pId", dept.getUpcomcode());
			map.put("name", dept.getComname());
			map.put("isParent",
					queryDeptListByPidAgr(dept.getComcode(), comtype).size() > 0 ? "true"
							: "false");
			list.add(map);
		}
		return list;
	}

	@Override
	public List<Map<String, String>> queryListByPcode4Group(String parentcode) {
		return inscDeptDao.selectByParentDeptCode4Group(parentcode);
	}

	@Override
	public int updateDeptById(String id) {
		return inscDeptDao.updateDeptByid(id);
	}

	@Override
	public int updateDeptByIddel(String id) {
		return inscDeptDao.updateDeptByiddel(id);
	}

	@Override
	public INSCDept getLegalPersonDept(String deptCode) {
		INSCDept dept = inscDeptDao.selectByComcode(deptCode);
		if ("03".compareTo(dept.getComtype()) >= 0) {
			return dept;
		}
		if (dept.getComtype().equals("03"))
			return dept;
		while (!dept.getComtype().equals("03")) {
			dept = inscDeptDao.selectByComcode(dept.getUpcomcode());
			if (dept.getComtype().equals("03")) {
				return dept;
			}

		}
		return null;
	}

	@Override
	public String queryByComCode(String userorganization) {
		INSCDept temp = new INSCDept();
		temp.setComcode(userorganization);
		return inscDeptDao.selectOne(temp).getId();
	}

	/**
	 * 获得机构信息
	 * 
	 * @param dept
	 * @param modifyDept
	 * @return
	 */
	public INSCDept getModifideDeptInfo(INSCDept dept, INSCDept modifyDept) {
		dept.setId(modifyDept.getId());
		dept.setDeptinnercode(modifyDept.getDeptinnercode());
		dept.setComcode(modifyDept.getComcode());
		dept.setUpcomcode(modifyDept.getUpcomcode());
		dept.setComname(modifyDept.getComname());
		dept.setShortname(modifyDept.getShortname());
		dept.setComkind(modifyDept.getComkind());
		dept.setComtype(modifyDept.getComtype());
		dept.setComgrade(modifyDept.getComgrade());
		dept.setRearcomcode(modifyDept.getRearcomcode());
		dept.setProvince(modifyDept.getProvince());
		dept.setCity(modifyDept.getCity());
		dept.setCounty(modifyDept.getCounty());
		dept.setAddress(modifyDept.getAddress());
		dept.setZipcode(modifyDept.getZipcode());
		dept.setPhone(modifyDept.getPhone());
		dept.setFax(modifyDept.getFax());
		dept.setEmail(modifyDept.getEmail());
		dept.setWebaddress(modifyDept.getWebaddress());
		dept.setSatrapname(modifyDept.getSatrapname());
		dept.setSatrapcode(modifyDept.getSatrapcode());
		dept.setChildflag(modifyDept.getChildflag());
		dept.setTreelevel(modifyDept.getTreelevel());
		dept.setOperator(modifyDept.getOperator());
		dept.setCreatetime(modifyDept.getCreatetime());
		dept.setModifytime(modifyDept.getModifytime());
		dept.setParentcodes(modifyDept.getParentcodes());
		return dept;
	}


	@Override
	public INSCDept getOrgDept(String deptCode) {
		INSCDept dept = inscDeptDao.selectByComcode(deptCode);
		if ("02".compareTo(dept.getComtype()) >= 0) {
			return dept;
		}
		if (dept.getComtype().equals("02"))
			return dept;
		while (!dept.getComtype().equals("02")) {
			dept = inscDeptDao.selectByComcode(dept.getUpcomcode());
			if (dept.getComtype().equals("02")) {
				return dept;
			}

		}
		return null;
	}

	@Override
	public INSCDept getOrgDeptByDeptCode(String deptCode) {
		if (StringUtil.isNotEmpty(deptCode)) {
			return inscDeptDao.selectByComcode(deptCode);
		}
		return null;
	}

	@Override
	public List<String> selectByParentDeptCode4groups(String parentcode) {
		return inscDeptDao.selectByParentDeptCode4groups(parentcode);
	}

	@Override
	public List<Map<String, String>> queryTreeList4Data(String parentcode,
			String deptId) {
		if (StringUtil.isEmpty(parentcode)
				|| "source".equalsIgnoreCase(parentcode)) {
			parentcode = "";
		}
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		if ("1200000000".equals(deptId)) {
			List<INSCDept> inscListDept = queryDeptListByPid(parentcode);
			for (INSCDept model : inscListDept) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("id", model.getId());
				tempMap.put("pid", model.getComcode());
				tempMap.put("name", model.getShortname());
				tempMap.put("isParent",
						"1".equals(model.getChildflag()) ? "true" : "false");
				list.add(tempMap);
			}
			return list;
		}

		// 登陆人机构信息
		INSCDept deptModel = inscDeptDao.selectById(deptId);
		String comgrade = deptModel.getComgrade();
		int comgradeInt = Integer.valueOf(comgrade.substring(1));
		String[] parentCodesArray = deptModel.getParentcodes().split("\\+");

		List<INSCDept> inscListDept = queryDeptListByPid(parentcode);
		if (inscListDept.isEmpty() || inscListDept == null) {
			return list;
		}
		String tempComgrade = inscListDept.get(0).getComgrade();
		int tempComgradeInt;
		if ("".equals(tempComgrade)) {
			tempComgradeInt = 0;
		} else {
			tempComgradeInt = Integer.valueOf(tempComgrade.substring(1));
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
						tempMap.put("isParent", "1".equals(tempDept
								.getChildflag()) ? "true" : "false");
						list.add(tempMap);
					}
				}
			} else if (tempComgradeInt == comgradeInt) {
				INSCDept tempDept = new INSCDept();
				Map<String, String> tempMap = new HashMap<String, String>();
				tempDept = deptModel;
				tempMap.put("id", tempDept.getId());
				tempMap.put("pid", tempDept.getComcode());
				tempMap.put("name", tempDept.getComname());
				tempMap.put("isParent",
						"1".equals(tempDept.getChildflag()) ? "true" : "false");
				list.add(tempMap);
				break;
			} else {
				INSCDept tempDept = new INSCDept();
				Map<String, String> tempMap = new HashMap<String, String>();
				tempDept = inscListDept.get(i);
				tempMap.put("id", tempDept.getId());
				tempMap.put("pid", tempDept.getComcode());
				tempMap.put("name", tempDept.getComname());
				tempMap.put("isParent",
						"1".equals(tempDept.getChildflag()) ? "true" : "false");
				list.add(tempMap);
			}
		}
		return list;
	}

	@Override
	public List<Map<String, String>> queryTreeList4Data2(String parentcode,
														String deptId) {
		if (StringUtil.isEmpty(parentcode)
				|| "source".equalsIgnoreCase(parentcode)) {
			parentcode = "";
		}
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		if ("1200000000".equals(deptId)) {
			List<INSCDept> inscListDept = queryDeptListByPid(parentcode);
			for (INSCDept model : inscListDept) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("id", model.getId());
				tempMap.put("pid", model.getComcode());
				tempMap.put("name", model.getShortname());
				if (model.getComcode() != null && "1200000000".equals(model.getComcode())) {
					tempMap.put("isParent","true");
				} else {
					tempMap.put("isParent","false");
				}
				list.add(tempMap);
			}
			return list;
		}

		// 登陆人机构信息
		INSCDept deptModel = inscDeptDao.selectById(deptId);
		String comgrade = deptModel.getComgrade();
		int comgradeInt = Integer.valueOf(comgrade.substring(1));
		String[] parentCodesArray = deptModel.getParentcodes().split("\\+");

		List<INSCDept> inscListDept = queryDeptListByPid(parentcode);
		if (inscListDept.isEmpty() || inscListDept == null) {
			return list;
		}
		String tempComgrade = inscListDept.get(0).getComgrade();
		int tempComgradeInt;
		if ("".equals(tempComgrade)) {
			tempComgradeInt = 0;
		} else {
			tempComgradeInt = Integer.valueOf(tempComgrade.substring(1));
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
						tempMap.put("isParent", "1".equals(tempDept
								.getChildflag()) ? "true" : "false");
						list.add(tempMap);
					}
				}
			} else if (tempComgradeInt == comgradeInt) {
				INSCDept tempDept = new INSCDept();
				Map<String, String> tempMap = new HashMap<String, String>();
				tempDept = deptModel;
				tempMap.put("id", tempDept.getId());
				tempMap.put("pid", tempDept.getComcode());
				tempMap.put("name", tempDept.getComname());
				tempMap.put("isParent", "false");
				list.add(tempMap);
				break;
			}
		}
		return list;
	}

	@Override
	public List<INSCDept> selectDeptlikeparentcodes(String parentcodes,String detpid) {
		return inscDeptDao.selectDeptlikeparentcodes(parentcodes,detpid);
	}

	@Override
	public List<INSCDept> selectDeptlikeparentcodes2(String parentcodes,String detpid) {
		return inscDeptDao.selectDeptlikeparentcodes2(parentcodes,detpid);
	}

	@Override
	public List<String> selectWDidsByPatId(Map<String, String> detpid) {
		return inscDeptDao.queryWDidsByPatId(detpid);
	}

	/**
	 * 生成机构树使用  liuchao
	 */
	@Override
	public List<Map<String, Object>> selectDeptTreeByParentCode(
			String parentcode) {
		if(parentcode == null){
			parentcode = "";
		}
		return inscDeptDao.selectDeptTreeByParentCode(parentcode);	
	}
	/*
	 * 生成登陆者可见的出单网点机构树
	 * parentcode(父节点):第一次点击触发时,parentcode为空.
	 * userorganization(登陆者所属机构):当parentcode为空时,根据userorganization来查.
	 */
	@Override
	public List<Map<String, Object>> selectPartTreeByParentCode(
			Map<String,String> params) {
		if(params.get("parentcode") == null){
			params.put("parentcode","");
		}else{
			params.put("userorganization","");
		}
//		return inscDeptDao.selectPartTreeByParentCode(params);
		List<Map<String,Object>> list=inscDeptDao.selectPartTreeByParentCode(params);
		//显示子节点的单选框
		for(Map<String,Object> map:list){
			if(!"04".equals((String)map.get("comgrade"))){
				map.put("nocheck","true");
			}
		}
		return list;
	}
	/*
	 * 生成登陆者可见的出单网点机构树
	 * parentcode(父节点):第一次点击触发时,parentcode为空.
	 * userorganization(登陆者所属机构):当parentcode为空时,根据userorganization来查.
	 */
	@Override
	public List<Map<String, Object>> selectPartTreeByParentCodeCheckAll(
			Map<String,String> params) {
		if(params.get("parentcode") == null){
			params.put("parentcode","");
		}else{
			params.put("userorganization","");
		}
		List<Map<String,Object>> list=inscDeptDao.selectPartTreeByParentCode(params);
		//团队级别的不可选
		for(Map<String,Object> map:list){
			if("05".equals((String)map.get("comgrade"))){
				map.put("nocheck","true");
			}
		}
		return list;
	}

	@Override
	public List<Map<String, String>> queryTreeList4PrvAccount(String parentcode, String deptId) {
		if (StringUtil.isEmpty(parentcode)
				|| "source".equalsIgnoreCase(parentcode)) {
			parentcode = "";
		}
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		if ("1200000000".equals(deptId)) {
			List<INSCDept> inscListDept = queryDeptListByPid(parentcode);
			for (INSCDept model : inscListDept) {
				if("06".equals(model.getComtype())){
					continue;
				}
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("id", model.getId());
				tempMap.put("pid", model.getComcode());
				tempMap.put("name", model.getShortname());
				tempMap.put("isParent",
						"1".equals(model.getChildflag()) ? "true" : "false");
				list.add(tempMap);
			}
			return list;
		}
		
		// 登陆人机构信息
		INSCDept deptModel = inscDeptDao.selectById(deptId);
		String comgrade = deptModel.getComgrade();
		int comgradeInt = Integer.valueOf(comgrade.substring(1));
		String[] parentCodesArray = deptModel.getParentcodes().split("\\+");

		List<INSCDept> inscListDept = queryDeptListByPid(parentcode);
		if (inscListDept.isEmpty() || inscListDept == null) {
			return list;
		}
		String tempComgrade = inscListDept.get(0).getComgrade();
		int tempComgradeInt;
		if ("".equals(tempComgrade)) {
			tempComgradeInt = 0;
		} else {
			tempComgradeInt = Integer.valueOf(tempComgrade.substring(1));
		}

		for (int i = 0; i < inscListDept.size(); i++) {
			if("06".equals(inscListDept.get(i).getComtype())){
				continue;
			}
			if (tempComgradeInt < comgradeInt) {
				for (String pStr : parentCodesArray) {
					if (inscListDept.get(i).getComcode().equals(pStr)) {
						INSCDept tempDept = new INSCDept();
						Map<String, String> tempMap = new HashMap<String, String>();
						tempDept = inscListDept.get(i);
						tempMap.put("id", tempDept.getId());
						tempMap.put("pid", tempDept.getComcode());
						tempMap.put("name", tempDept.getShortname());
						tempMap.put("isParent", "1".equals(tempDept
								.getChildflag()) ? "true" : "false");
						list.add(tempMap);
					}
				}
			} else if (tempComgradeInt == comgradeInt) {
				INSCDept tempDept = new INSCDept();
				Map<String, String> tempMap = new HashMap<String, String>();
				tempDept = deptModel;
				tempMap.put("id", tempDept.getId());
				tempMap.put("pid", tempDept.getComcode());
				tempMap.put("name", tempDept.getShortname());
				tempMap.put("isParent",
						"1".equals(tempDept.getChildflag()) ? "true" : "false");
				list.add(tempMap);
				break;
			} else {
				INSCDept tempDept = new INSCDept();
				Map<String, String> tempMap = new HashMap<String, String>();
				tempDept = inscListDept.get(i);
				tempMap.put("id", tempDept.getId());
				tempMap.put("pid", tempDept.getComcode());
				tempMap.put("name", tempDept.getShortname());
				tempMap.put("isParent",
						"1".equals(tempDept.getChildflag()) ? "true" : "false");
				list.add(tempMap);
			}
		}
		return list;
	}
	@Override
	public List<Map<String, String>> queryTreeListByCity(String upcomcode,
			String city) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, Object> mapd = new HashMap<String, Object>();
		mapd.put("upcomcode", upcomcode);
		mapd.put("city", city);
		List<INSCDept> inscListDept = inscDeptDao.selectWdByCity(mapd);
		for (INSCDept dept : inscListDept) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", dept.getId());
			map.put("pId", dept.getUpcomcode());
			map.put("name", dept.getComname());
			map.put("comtype", dept.getComtype());
			map.put("isParent", "1".equals(dept.getChildflag()) ? "true"
					: "false");
			list.add(map);
		}
		return list;
	}

	@Override
	public INSCDept getPlatformDept(String deptid) {
		INSCDept dept = inscDeptDao.selectByComcode(deptid);
		if ("02".compareTo(dept.getComtype()) >= 0) {
			return dept;
		}
		if ("02".equals(dept.getComtype()))
			return dept;
		while (!"02".equals(dept.getComtype())) {
			dept = inscDeptDao.selectByComcode(dept.getUpcomcode());
			if (dept.getComtype().equals("02")) {
				return dept;
			}

		}
		return null;
	}

	@Override
	public List<Map<String, String>> getDeptListByCity(String id, String city,
			String platformcode) {
		Map<String, String> mapdMap = new HashMap<String, String>();
		mapdMap.put("deptid", id);
		mapdMap.put("city", city);
		mapdMap.put("platformcode", platformcode);
		List<Map<String, String>> queryTreeListByAgr = inscDeptDao.queryTreeListByCity(mapdMap);
		return queryTreeListByAgr;
	}
	
	@Override
	public List<Map<String, String>> dept4Tree(String userorganization, String comgrade, String type){
		if (StringUtils.isEmpty(userorganization)) {
			return null;
		}
		String grade = comgrade;
		if (StringUtils.isEmpty(comgrade)) {
			grade = "04"; // 默认网点及以上层级
		}
		return inscDeptDao.list4Tree(userorganization, grade, type);
	}

	@Override
	public List<Map<String, String>> dept4Tree2(String userorganization, String comgrade, String type){
		if (StringUtils.isEmpty(userorganization)) {
			return null;
		}
		String grade = comgrade;

		return inscDeptDao.list4Tree(userorganization, grade, type);
	}
	
	@Override
	public String getPingTaiDeptId(String deptId){
		String result="";
		INSCDept deptModel = inscDeptDao.selectById(deptId);
		if(deptModel==null){
			return result;
		}
		
		String tempInnerCode = deptModel.getDeptinnercode();
		if(StringUtils.isEmpty(tempInnerCode)){
			return result;
		}
		if(tempInnerCode.length()==5){
			result = deptId;
		}else if(tempInnerCode.length()<5){
			result = deptId;
		}else if(tempInnerCode.length()>5){
			String pingTaiInnerCode = tempInnerCode.substring(0, 5);
			result =  inscDeptDao.seleDeptIdByInnerCode(pingTaiInnerCode);
		}
		return result;
	}

	public String getPlatformInnercode(String deptInnercode) {
		return (StringUtil.isNotEmpty(deptInnercode) && deptInnercode.length() >= 5) ? deptInnercode.substring(0, 5) : null;
	}
}