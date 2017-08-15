package com.cninsure.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.exception.ServiceException;
import com.cninsure.system.dao.INSCMenuDao;
import com.cninsure.system.dao.INSCRoleDao;
import com.cninsure.system.dao.INSCRoleMenuDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.dao.INSCUserRoleDao;
import com.cninsure.system.entity.INSCMenu;
import com.cninsure.system.entity.INSCRole;
import com.cninsure.system.entity.INSCRoleMenu;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.entity.INSCUserRole;
import com.cninsure.system.service.INSCRoleService;

@Service
@Transactional
public class INSCRoleServiceImpl extends BaseServiceImpl<INSCRole> implements
		INSCRoleService {
	@Resource
	private INSCRoleDao insCRoleDao;
	
	@Resource
	private INSCRoleMenuDao inscRoleMenuDao;
	
	@Resource
	private INSCMenuDao inscMenuDao ;
	
	@Resource
	private INSCUserDao inscUserDao;
	
	@Resource
	private INSCUserRoleDao inscUserRoleDao;
	
	@Override
	protected BaseDao<INSCRole> getBaseDao() {
		return insCRoleDao;
	}

	@Override
	public List<String> selectRolecodesByRoleids(List<String> roleidList)
			throws ServiceException {
		if (roleidList != null && roleidList.size() > 0
				&& !roleidList.isEmpty()) {
			return insCRoleDao.selectRolecodesByRoleids(roleidList);
		}
		return null;
	}
	
	@Override
	public Map<String,Object> showRoleList(Map<String, Object> map) {
		List<Map<Object, Object>> infoList =  insCRoleDao.selectRoleLitByMap(map);
		//表现值转换
		for(Map<Object,Object> tempMap:infoList){
			if("1".equals(tempMap.get("status"))){
				tempMap.put("statusstr", "启用");
			}else if("0".equals(tempMap.get("status"))){
				tempMap.put("statusstr", "停用");
			}
		}
		Map<String, Object> initMap = new HashMap<String, Object>();
		initMap.put("total", insCRoleDao.selectCount());
		initMap.put("rows", infoList);
		return initMap;
	}
	

	@Override
	public int deleteRoleById(String roleId) {
		int result=0;
		INSCRoleMenu tempModel = new INSCRoleMenu();
		tempModel.setRoleid(roleId);
		try {
			long roleMenuCount = inscRoleMenuDao.selectCount(tempModel);
			if(roleMenuCount==0L){
				result =insCRoleDao.deleteById(roleId);
			}else{
				result = 0;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		}
		return result;
	}

	@Override
	public Map<String, String> updateRoleById(INSCRole model) {
		Map<String,String> resultMap = new HashMap<String,String>();
		try {
			insCRoleDao.updateById(model);
			resultMap.put("code", "0");
			resultMap.put("message", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "1");
			resultMap.put("message", "操作失败，请稍候重试");
		}
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public INSCRole queryById(String id) {
		INSCRole result = insCRoleDao.selectById(id);
			if("1".equals(result.getStatus())){
				result.setStatusstr("启用");
			}else if("0".equals(result.getStatus())){
				result.setStatusstr("停用");
			}
		return result;
	}

	@Override
	public List<Map<String,String>> initRoleTree(String roleId) {
		List<INSCMenu> treeList = inscMenuDao.selectAllByOrder("nodecode");
		List<Map<String,String>> resultTree = new ArrayList<Map<String,String>>();
		
		List<INSCRoleMenu> checkList = inscRoleMenuDao.selectMenusByRoleId(roleId);
		for(INSCMenu model:treeList){
			Map<String,String> tempMap = new HashMap<String,String>();
			for(INSCRoleMenu rmodel:checkList){
				if(rmodel.getMenuid().equals(model.getId())){
					tempMap.put("checked", "true");
					tempMap.put("open", "true");
				}
			}
			tempMap.put("pid", model.getParentnodecode());
			tempMap.put("id", model.getNodecode());
			tempMap.put("name", model.getNodename());
			tempMap.put("menuid", model.getId());
			resultTree.add(tempMap);
		}
		return resultTree;
	}

	@Override
	public Map<String, String> deleteRoleByIds(String roleIds) {
		Map<String,String> resultMap = new HashMap<String,String>();
		INSCRoleMenu tempModel = new INSCRoleMenu();
		try {
			String[] roleIdArray = roleIds.split(",");
			for(String roleId:roleIdArray){
				tempModel.setRoleid(roleId);
				long roleMenuCount = inscRoleMenuDao.selectCount(tempModel);
				if(roleMenuCount==0L){
					insCRoleDao.deleteById(roleId);
				}else{
					throw new RuntimeException("当前角色已经绑定菜单");
				}
				
			}
			resultMap.put("code", "0");
			resultMap.put("message", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "1");
			resultMap.put("message", "操作失败，请稍候重试");
		}
		return resultMap;
	}
	
	@Override
	public Map<String,Object> findRmainUsersByIds(Map<String,Object> param) {
		Map<String,Object> result = new HashMap<String,Object>();
		if(param.get("roleId")!=null && param.get("roleId")!=""){
			
			//查询关系表得到当前角色所有用户id
			List<INSCUserRole>  userRoleList= inscUserRoleDao.selectUsersByRoleId(param.get("roleId").toString());
			
			List<String> ids = new ArrayList<String>();
			if(userRoleList!=null&&userRoleList.size()>0){
				
				for(INSCUserRole model:userRoleList){
					ids.add(model.getUserid());
				}
				
				param.put("ids", ids);
				List<INSCUser> userList = inscUserDao.selectRmainUsersByMap(param);
				result.put("total", inscUserDao.selectCountRmainUsersByMap(param));
				result.put("rows", userList);
				return result;
				
				//如果当前角色没有绑定用户 则返回所有用户信息
			}else{
				param.remove("roleId");
				List<Map<Object, Object>> infoList = inscUserDao.selectUserListPaging(param);
				long total = inscUserDao.selectPagingCount(param);
//				List<INSCUser> userList = inscUserDao.selectAll();
				result.put("total", total);
				result.put("rows", infoList);
				return result;
			}
		}else {
			result.put("total", "");
			result.put("rows", "");
			return result;
		}
		
	}

	@Override
	public Map<String, String> deleteUsersByRoleId(String userIds,String roleId) {
		Map<String,String> resultMap = new HashMap<String,String>();
		INSCUserRole tempModel = new INSCUserRole();
		try {
			String [] userIdArray = userIds.split(",");
			for(String userid:userIdArray){
				//查询用户id关联角色数量
				tempModel.setUserid(userid);
				tempModel.setRoleid(roleId);
				Map<String,String> urId = new HashMap<String,String>();
				urId.put("userId", userid);
				urId.put("roleId", roleId);
				inscUserRoleDao.deleteByUserIdRoleId(urId);
			}
			resultMap.put("code", "0");
			resultMap.put("message", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "1");
			resultMap.put("message", "操作失败，请稍候重试");
		}
		return resultMap;
	}


	@Override
	public List<INSCRole> queryAll() {
		return insCRoleDao.selectAll();
	}
	
}
