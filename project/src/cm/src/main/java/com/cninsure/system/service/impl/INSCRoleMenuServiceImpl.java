package com.cninsure.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCMenuDao;
import com.cninsure.system.dao.INSCRoleMenuDao;
import com.cninsure.system.entity.INSCRoleMenu;
import com.cninsure.system.service.INSCRoleMenuService;

@Service
@Transactional
public class INSCRoleMenuServiceImpl extends BaseServiceImpl<INSCRoleMenu> implements INSCRoleMenuService {
	@Resource
	private INSCRoleMenuDao inscRoleMenuDao;
	@Resource
	private INSCMenuDao inscMenuDao;

	@Override
	protected BaseDao<INSCRoleMenu> getBaseDao() {
		return inscRoleMenuDao;
	}
	@Override
	public Map<String, String> repairRoleMenu(HttpSession session,String roleId, String menuIds) {
		
		
		
		//得到原来menuIds
		List<String> oldRoleMenuIds = getMenuIdsByRoleId(roleId);
		
		//当前menuIds
		List<String> newRoleMenuIds = new ArrayList<String>();
		
		//需要删除的menuIds
		List<String> deleteMenuIds = getMenuIdsByRoleId(roleId);
		
		String[] menuIdsArr = null;
		Map<String,String> resultMap = new HashMap<String,String>();
		try{
			if(menuIds.length()>0){
				menuIdsArr= menuIds.split(",");
				for(String str:menuIdsArr){
					newRoleMenuIds.add(str);
				}
			}
			
			//需要删除的menuIds
			deleteMenuIds.removeAll(newRoleMenuIds);
			for(String menuId:deleteMenuIds){
				INSCRoleMenu model = new INSCRoleMenu();
				model.setRoleid(roleId);
				model.setMenuid(menuId);
				inscRoleMenuDao.deleteByRoleIdMenuId(model);
			}
			
			//需要新增的menuIds
			newRoleMenuIds.removeAll(oldRoleMenuIds);
			
			//批量更新
			List<INSCRoleMenu> addList = new ArrayList<INSCRoleMenu>();
			for(String menuId:newRoleMenuIds){
				INSCRoleMenu model = new INSCRoleMenu();
				model.setRoleid(roleId);
				model.setMenuid(menuId);
				model.setOperator("u0001");
				model.setCreatetime(new Date());
				
				addList.add(model);
			}
			inscRoleMenuDao.insertInBatch(addList);
			
			
			resultMap.put("code", "0");
			resultMap.put("message", "修改成功");
		}catch (Exception e) {
			e.printStackTrace();
			resultMap.put("code", "1");
			resultMap.put("message", "操作失败，请稍候重试");
		}
		return resultMap;
	}
	/**
	 * 通过roleId 查询menuIds
	 * 
	 * @param roleId
	 * @return
	 */
	private List<String> getMenuIdsByRoleId(String roleId){
		INSCRoleMenu getRoleMenus = new INSCRoleMenu();
		getRoleMenus.setRoleid(roleId);
		List<INSCRoleMenu> oldRoleMenus =  inscRoleMenuDao.selectList(getRoleMenus);
		List<String> oldRoleMenuIds = new ArrayList<String>();
		for(INSCRoleMenu m:oldRoleMenus){
			oldRoleMenuIds.add(m.getMenuid());
		}
		return oldRoleMenuIds;
	}
	@Override
	public List<INSCRoleMenu> queryMenusByRoleId(String roleId) {
		return inscRoleMenuDao.selectMenusByRoleId(roleId);
	}
	@Override
	public List<INSCRoleMenu> queryAll() {
		return inscRoleMenuDao.selectAll();
	}
	
	
	
}
