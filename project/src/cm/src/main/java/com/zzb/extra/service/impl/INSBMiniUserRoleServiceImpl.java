package com.zzb.extra.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.extra.dao.INSBMiniPermissionDao;
import com.zzb.extra.dao.INSBMiniRoleDao;
import com.zzb.extra.dao.INSBMiniUserRoleDao;
import com.zzb.extra.dao.INSBRolePermissionDao;
import com.zzb.extra.entity.INSBAgentUser;
import com.zzb.extra.entity.INSBMiniPermission;
import com.zzb.extra.entity.INSBMiniRole;
import com.zzb.extra.entity.INSBMiniUserRole;
import com.zzb.extra.entity.INSBRolePermission;
import com.zzb.extra.service.INSBMiniUserRoleService;

import net.sf.json.JSONObject;
@Service
public class INSBMiniUserRoleServiceImpl extends BaseServiceImpl<INSBMiniUserRole> implements INSBMiniUserRoleService {
	
	@Resource
	private INSBMiniUserRoleDao insbMiniUserRoleDao;
	
	@Resource
	private INSBMiniRoleDao insbMiniRoleDao;
	
	@Resource
	private INSBMiniPermissionDao insbMiniPermissionDao;
	
	@Resource
	private INSBRolePermissionDao insbRolePermissionDao;

	@Override
	protected BaseDao<INSBMiniUserRole> getBaseDao() {
		// TODO Auto-generated method stub
		return insbMiniUserRoleDao;
	}
	@Override
	public void insert(INSBMiniUserRole insbMiniUserRole) {
		
	
		List<INSBAgentUser> agentUsers = insbMiniUserRoleDao.selectAgentUser(new HashMap<>());
		for(INSBAgentUser agent : agentUsers) {
			if(agent.getReferrerid() != null && !agent.getReferrerid().equals("")) {
				insbMiniUserRole.setId(UUIDUtils.random());
				insbMiniUserRole.setMiniuserid(agent.getId());
				insbMiniUserRole.setCreatetime(new Date());
				insbMiniUserRole.setRoleid("fd5befe0a94fd4cfaea89edee88024da");
				insbMiniUserRoleDao.saveObject(insbMiniUserRole);
			}else if (agent.getReferrerid() == null || agent.getReferrerid().equals("")) {
				
				Map<String, Object> param = new HashMap<>();
				param.put("referrerid", agent.getId());
				List<INSBAgentUser> tmpls = insbMiniUserRoleDao.selectAgentUser(param);
				if(tmpls.isEmpty() || tmpls.size()==0) {
					insbMiniUserRole.setId(UUIDUtils.random());
					insbMiniUserRole.setMiniuserid(agent.getId());
					insbMiniUserRole.setCreatetime(new Date());
					insbMiniUserRole.setRoleid("9c979c2acf80ccb6ebde0ae0907f77e6");
					insbMiniUserRoleDao.saveObject(insbMiniUserRole);
					
				}else {
					insbMiniUserRole.setId(UUIDUtils.random());
					insbMiniUserRole.setMiniuserid(agent.getId());
					insbMiniUserRole.setCreatetime(new Date());
					insbMiniUserRole.setRoleid("8f0b994200c987aebfe38fdfc313d660");
					insbMiniUserRoleDao.saveObject(insbMiniUserRole);
				}
			}
			
		}
		
		
	}

	@Override
	public String selectUserAttr(String openid) {
//		List<Map<String, Object>> reList = new ArrayList<>();
		List<INSBMiniPermission> lis = new ArrayList<>();
		INSBMiniRole role = null;
//		String name = "";
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> param = new HashMap<>();
		param.put("openid", openid);
		INSBAgentUser agent = insbMiniUserRoleDao.selectByOpenid(param);
		INSBMiniUserRole ur = insbMiniUserRoleDao.queryObjectByUserid(agent.getId());
//		List<Map<String, Object>> rows = insbMiniUserRoleDao.selectRoleByUserid(agent.getId());
		if(ur != null) {
			role = insbMiniRoleDao.findById(ur.getRoleid());
			if(role != null) {
				Map<String, Object> rolmap = new HashMap<>();
				rolmap.put("roleid", role.getId());
				List<INSBRolePermission> rps = insbRolePermissionDao.queryRperByRoleid(rolmap);
				for(INSBRolePermission rp : rps) {
					INSBMiniPermission permission = insbMiniPermissionDao.findById(rp.getPermissionid());
					lis.add(permission);
				}
			}
		}
		map.put("role", role);
		map.put("permissions", lis);
		
		return resultMap(map);
	}
	public  String resultMap(Map rows) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
      //  resultMap.put("success", true);
        resultMap.put("status", "success");
        resultMap.put("body", rows);
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        return jsonObject.toString();
    }

	

}
