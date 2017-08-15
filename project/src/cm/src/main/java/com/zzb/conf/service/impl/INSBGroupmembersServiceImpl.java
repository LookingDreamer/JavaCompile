package com.zzb.conf.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBGroupmembersDao;
import com.zzb.conf.entity.INSBGroupmembers;
import com.zzb.conf.service.INSBGroupmembersService;

@Service
@Transactional
public class INSBGroupmembersServiceImpl extends BaseServiceImpl<INSBGroupmembers> implements INSBGroupmembersService {
	@Resource
	private INSBGroupmembersDao insbGroupmembersDao;

	@Override
	protected BaseDao<INSBGroupmembers> getBaseDao() {
		return insbGroupmembersDao;
	}

	@Override
	public boolean queryUserGroupPrivileges(String userId,String task){
		return true;
		//to-do 根据用户id查询其是否拥有此任务执行权限
		/*Map<String, String> param = new HashMap<String, String>();
		param.put("userId", userId);
		param.put("task", task);
		if(insbGroupmembersDao.queryPrivileges(param)>0){
			return true;
		}else{
			return false;
		}*/
	}
}