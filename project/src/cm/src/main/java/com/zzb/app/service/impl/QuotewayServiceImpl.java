package com.zzb.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cninsure.core.utils.StringUtil;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBPermissionallot;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzb.app.service.QuotewayService;
import com.zzb.conf.entity.INSBPermission;


@Service
@Transactional
public class QuotewayServiceImpl implements QuotewayService{
	@Resource	
	private INSBEdiconfigurationDao ediDao;
	
	@Resource
	private INSBElfconfDao elfDao;
	
	@Resource
	private INSBAgentDao agentDao;
	
	@Resource
	private INSBPermissionDao insbPermissionDao;

	@Resource
	private INSBPermissionallotDao permissionallotDao;

	@Resource
	private INSBAgentDao insbAgentDao;

	@Override
	public String getpermissionsadd(String jobnum, boolean ignore) {
		List<Map<String, String>> resultlist = new ArrayList<Map<String,String>>();

		INSBAgent agent = insbAgentDao.selectByJobnum(jobnum);

		if (!ignore && agent != null && StringUtil.isNotEmpty(agent.getSetid())) {
			List<Map<String, String>> map =  permissionallotDao.selectPermissionBySetId(agent.getSetid());
			if(map != null && !map.isEmpty()) {
				for (Map<String, String> m  : map) {
					Map<String, String> permissions = new HashMap<String, String>();
					permissions.put("permissionname", m.get("permissionname"));
					permissions.put("addresscode", m.get("permissionpath"));
					resultlist.add(permissions);
				}
			}
		}

		// 认证过的代理人 如果没有配置权限可以跳过
		if (ignore) {
			List<INSBPermission> tempPermissionList = insbPermissionDao.selectAll();
			for (INSBPermission li : tempPermissionList) {
				Map<String, String> permissions = new HashMap<String, String>();
				permissions.put("permissionname", li.getPermissionname());
				permissions.put("addresscode", li.getPermissionpath());
				resultlist.add(permissions);
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("context", resultlist);
		JSONObject json = JSONObject.fromObject(result);
		return json.toString();
	}
	
	
}
