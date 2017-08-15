package com.cninsure.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.exception.ServiceException;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.dao.INSCRoleDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.dao.INSCUserRoleDao;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.entity.INSCUserRole;
import com.cninsure.system.service.INSCUserRoleService;

@Service
@Transactional
public class INSCUserRoleServiceImpl extends BaseServiceImpl<INSCUserRole>
		implements INSCUserRoleService {
	@Resource
	private INSCUserRoleDao inscUserRoleDao;

	@Resource
	private INSCRoleDao inscRoleDao;

	@Resource
	private INSCUserDao inscUserDao;

	@Override
	protected BaseDao<INSCUserRole> getBaseDao() {
		return inscUserRoleDao;
	}

	@Override
	public List<String> selectRoleidByUserid(String userId)
			throws ServiceException {
		if (StringUtils.isNotBlank(userId)) {
			return inscUserRoleDao.selectRoleidByUserid(userId);
		}
		return null;
	}

	@Override
	public String queryRoleIdsByUid(String id) {
		return inscUserRoleDao.queryRoleIdsByUid(id);
	}

	@Override
	public void insertUserRole(String userId, String roleids) {
		// 查询用户已有的角色
		String ids = queryRoleIdsByUid(userId);
		if (!StringUtil.isEmpty(ids)) {
			// 用户以前拥有目前没有的角色，需要删除
			String oldIds = userRoleIds(ids, roleids);
			System.out.println("oldIds=" + oldIds);
			if (!"".equals(oldIds)) {
				insertINSCUserRole(userId, oldIds, false);
			}
			// 用户目前拥有以前没有的角色，插入新的
			String newIds = userRoleIds(roleids, ids);
			System.out.println("newIds=" + newIds);
			if (!"".equals(newIds)) {
				insertINSCUserRole(userId, newIds, true);
			}
		} else {
			insertINSCUserRole(userId, roleids, true);
		}
	}

	private String userRoleIds(String ids, String newIds) {
		String result = "";
		for (String oid : ids.split(",")) {
			if (newIds.contains(oid)) {
				continue;
			} else {
				result += oid + ",";
			}
		}
		return "".equals(result) ? "" : result
				.substring(0, result.length() - 1);
	}

	/**
	 * 
	 * @param userId
	 * @param roleids
	 * @param flag
	 *            true 插入新的数据 false 更新status为0
	 */
	private void insertINSCUserRole(String userId, String roleids, boolean flag) {
		
		
		//批量新增
		List<INSCUserRole> addList = new ArrayList<INSCUserRole>();
		for (String roleId : roleids.split(",")) {
			INSCUserRole role = new INSCUserRole();
			role.setUserid(userId);
			role.setRoleid(roleId);
			role.setOperator("test");
			if (flag) {
				role.setCreatetime(new Date());
				role.setStatus("1");
				addList.add(role);
//				inscUserRoleDao.insert(role);
			} else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", userId);
				map.put("roleId", roleId);
				String id = inscUserRoleDao.selectOneByUidAndRid(map);
				if (!"".equals(id)) {
					role.setStatus("0");
					role.setModifytime(new Date());
					role.setId(id);
					inscUserRoleDao.updateById(role);
				}
			}

			inscUserRoleDao.insertInBatch(addList);
		}
	}

	@Override
	public Map<String, Object> getUsersByRoleid(Map<String, Object> map) {
		String roleId = map.get("roleId").toString();
		Map<String, Object> initMap = new HashMap<String, Object>();
		
		
		List<INSCUser> result = new ArrayList<INSCUser>();
		if (roleId == null||"".equals(roleId)) {
			initMap.put("total", 0);
			initMap.put("rows", result);
			return initMap;
		} else {
			// 得到所有用户id
			List<INSCUserRole> userRoleList = inscUserRoleDao.selectPageUsersByRoleId(map);

			// 根据当前用户查找所有角色
			for (INSCUserRole userRole : userRoleList) {
				String roleidStr = queryRoleIdsByUid(userRole.getUserid());
				String[] roleids = roleidStr.split(",");
				String roleName = "";
				for (String rileId : roleids) {
					roleName = roleName
							+ inscRoleDao.selectRoleNameById(rileId) + ",";
				}
				INSCUser user = inscUserDao
						.selectByUserId(userRole.getUserid());
				if (user != null) {
					if (roleName.length() > 0) {
						user.setRolenames(roleName.substring(0,
								roleName.length() - 1));
					}
					result.add(user);
				}

			}

			long total =inscUserRoleDao.selectPageCountUsersByRoleId(map); 
			initMap.put("total",total);
			initMap.put("rows", result);
			return initMap;
		}
	}

	@Override
	public Map<String, Object> saveUsersByRoleId(String userIds, String roleId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			
			//批量新增
			List<INSCUserRole> addList = new ArrayList<INSCUserRole>();
			for (String userId : userIds.split(",")) {
				INSCUserRole userRole = new INSCUserRole();
				userRole.setUserid(userId);
				userRole.setRoleid(roleId);
				userRole.setOperator("test");
				userRole.setCreatetime(new Date());
				userRole.setStatus("1");
				
				addList.add(userRole);
//				inscUserRoleDao.insert(userRole);
			}
			inscUserRoleDao.insertInBatch(addList);
			
			// resultMap = getUsersByRoleid(roleId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
}
