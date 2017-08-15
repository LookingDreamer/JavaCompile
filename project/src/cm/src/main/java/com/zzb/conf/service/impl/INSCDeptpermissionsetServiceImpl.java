package com.zzb.conf.service.impl;
 
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.conf.dao.INSBPermissionsetDao;
import com.zzb.conf.dao.INSCDeptpermissionsetDao;
import com.zzb.conf.entity.INSBPermissionset;
import com.zzb.conf.entity.INSCDeptpermissionset;
import com.zzb.conf.service.INSCDeptpermissionsetService;

@Service
@Transactional
public class INSCDeptpermissionsetServiceImpl extends BaseServiceImpl<INSCDeptpermissionset> implements
		INSCDeptpermissionsetService {
	@Resource
	private INSCDeptpermissionsetDao inscDeptpermissionsetDao;
	@Resource
	private INSBPermissionsetDao inscPermissionsetDao;

	@Override
	protected BaseDao<INSCDeptpermissionset> getBaseDao() {
		return inscDeptpermissionsetDao;
	}

	@Override
	public Map<String, String> queryByComcode(String comcode) {
		return inscDeptpermissionsetDao.queryByComcode(comcode);
	}

	@Override
	public INSCDeptpermissionset getSetid(String deptid,String agentKind) {
		List<INSCDeptpermissionset> list=inscDeptpermissionsetDao.selectDeptByDeptid(deptid);
		for(INSCDeptpermissionset inscDeptpermissionset : list){
			String setId = null;
			if( "2".equals(agentKind) ) {	
				setId = inscDeptpermissionset.getFormalsetid();
			} else if( "3".equals(agentKind) ) {
				setId = inscDeptpermissionset.getChannelsetid();
			} else {
				setId = inscDeptpermissionset.getTrysetid();
				
			}
			if( setId != null && !"".equals(setId) ) {
				INSBPermissionset insbPermissionset=inscPermissionsetDao.selectById(setId);
				if( insbPermissionset != null && insbPermissionset.getStatus() != null 
						&& insbPermissionset.getStatus() == 2) {
					return inscDeptpermissionset;
				}
			}
		}
		return null;
	}

}