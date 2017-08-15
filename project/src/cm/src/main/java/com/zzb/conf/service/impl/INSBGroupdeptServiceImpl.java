package com.zzb.conf.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.dao.INSBGroupdeptDao;
import com.zzb.conf.entity.INSBGroupdept;
import com.zzb.conf.service.INSBGroupdeptService;

@Service
@Transactional
public class INSBGroupdeptServiceImpl extends BaseServiceImpl<INSBGroupdept> implements
		INSBGroupdeptService {
	@Resource
	private INSBGroupdeptDao insbGroupdeptDao;
	@Resource
	private INSCDeptDao deptDao;

	@Override
	protected BaseDao<INSBGroupdept> getBaseDao() {
		return insbGroupdeptDao;
	}

	@Override
	public void saveGroupWangdata(INSCUser user,String groupid, String deptid, String grade) {
		List<String> oldDeptIds=insbGroupdeptDao.selectDeptIdsByGroupId(groupid);
		
		if("5".equals(grade)){
				if(!oldDeptIds.isEmpty()&&oldDeptIds!=null){
					if(!oldDeptIds.contains(deptid)){
						INSBGroupdept groupModel = new INSBGroupdept();
						
						groupModel.setOperator(user.getUsername());
						groupModel.setCreatetime(new Date());
						groupModel.setDeptid(deptid);
						groupModel.setGroupid(groupid);
						insbGroupdeptDao.insert(groupModel);
					}
				}else{
					INSBGroupdept groupModel = new INSBGroupdept();
					groupModel.setOperator(user.getUsername());
					groupModel.setCreatetime(new Date());
					groupModel.setDeptid(deptid);
					groupModel.setGroupid(groupid);
					insbGroupdeptDao.insert(groupModel);
				}
		}else{
			List<String> deptIdList =  deptDao.selectComCodeByComtypeAndParentCodes4EDI(deptid);
			if(deptIdList!=null&&!deptIdList.isEmpty()){
				if(oldDeptIds!=null&&!oldDeptIds.isEmpty()){
					deptIdList.removeAll(oldDeptIds);
				}
				
				for(String deptId:deptIdList){
					INSBGroupdept groupModel = new INSBGroupdept();
					groupModel.setOperator(user.getUsername());
					groupModel.setCreatetime(new Date());
					groupModel.setDeptid(deptId);
					groupModel.setGroupid(groupid);
					insbGroupdeptDao.insert(groupModel);
				}
			}
		
		}
	}

}